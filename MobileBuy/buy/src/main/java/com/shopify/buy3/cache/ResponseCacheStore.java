package com.shopify.buy3.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

interface ResponseCacheStore {
  @Nullable ResponseCacheRecord cacheRecord(@NonNull String cacheKey) throws IOException;

  @Nullable ResponseCacheRecordEditor cacheRecordEditor(@NonNull String cacheKey) throws IOException;

  void remove(@NonNull String cacheKey) throws IOException;

  void delete() throws IOException;
}
