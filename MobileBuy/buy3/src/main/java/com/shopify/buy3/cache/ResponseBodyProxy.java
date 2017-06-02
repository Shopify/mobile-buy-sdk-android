package com.shopify.buy3.cache;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpCodec;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import okio.Timeout;
import timber.log.Timber;

import static com.shopify.buy3.cache.Utils.closeQuietly;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static okhttp3.internal.Util.discard;

final class ResponseBodyProxy extends ResponseBody {
  private final String contentType;
  private final String contentLength;
  private final Source responseBodySource;

  ResponseBodyProxy(@NonNull ResponseCacheRecordEditor cacheRecordEditor, @NonNull Response sourceResponse) {
    this.contentType = sourceResponse.header("Content-Type");
    this.contentLength = sourceResponse.header("Content-Length");
    this.responseBodySource = new ProxySource(cacheRecordEditor, sourceResponse.body().source());
  }

  @Override public MediaType contentType() {
    return contentType != null ? MediaType.parse(contentType) : null;
  }

  @Override public long contentLength() {
    try {
      return contentLength != null ? Long.parseLong(contentLength) : -1;
    } catch (NumberFormatException e) {
      Timber.w(e, "failed to parse content length");
      return -1;
    }
  }

  @Override public BufferedSource source() {
    return Okio.buffer(responseBodySource);
  }

  private static class ProxySource implements Source {
    final ResponseCacheRecordEditor cacheRecordEditor;
    final ResponseBodyCacheSink responseBodyCacheSink;
    final Source responseBodySource;
    boolean closed;

    ProxySource(ResponseCacheRecordEditor cacheRecordEditor, Source responseBodySource) {
      this.cacheRecordEditor = cacheRecordEditor;
      this.responseBodySource = responseBodySource;
      responseBodyCacheSink = new ResponseBodyCacheSink(Okio.buffer(cacheRecordEditor.bodySink())) {
        @Override void onException(Exception e) {
          abortCacheQuietly();
          Timber.w(e, "failed to write to cache response sink");
        }
      };
    }

    @Override public long read(Buffer sink, long byteCount) throws IOException {
      long bytesRead;
      try {
        bytesRead = responseBodySource.read(sink, byteCount);
      } catch (IOException e) {
        if (!closed) {
          closed = true;
          abortCacheQuietly();
        }
        throw e;
      }

      if (bytesRead == -1) {
        if (!closed) {
          closed = true;
          commitCache();
        }
        return -1;
      }

      responseBodyCacheSink.copyFrom(sink, sink.size() - bytesRead, bytesRead);
      return bytesRead;
    }

    @Override public Timeout timeout() {
      return responseBodySource.timeout();
    }

    @Override public void close() throws IOException {
      if (closed) return;
      closed = true;

      if (discard(this, HttpCodec.DISCARD_STREAM_TIMEOUT_MILLIS, MILLISECONDS)) {
        responseBodySource.close();
        commitCache();
      } else {
        responseBodySource.close();
        abortCacheQuietly();
      }
    }

    private void commitCache() {
      try {
        responseBodyCacheSink.close();
        cacheRecordEditor.commit();
      } catch (Exception e) {
        closeQuietly(responseBodyCacheSink);
        abortCacheQuietly();
        Timber.w(e, "failed to commit cache response");
      }
    }

    private void abortCacheQuietly() {
      closeQuietly(responseBodyCacheSink);
      try {
        cacheRecordEditor.abort();
      } catch (Exception ignore) {
        // ignore
      }
    }
  }
}
