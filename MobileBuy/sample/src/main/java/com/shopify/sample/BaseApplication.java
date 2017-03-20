/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package com.shopify.sample;

import android.app.Application;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.shopify.sample.view.ScreenRouter;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class BaseApplication extends Application {
  private final FrescoMemoryTrimmableRegistry frescoMemoryTrimmableRegistry = new FrescoMemoryTrimmableRegistry();

  @Override
  public void onCreate() {
    super.onCreate();
    initFresco();
  }

  private void initFresco() {
    final ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(this);

    File cacheDir = getExternalCacheDir();
    if (cacheDir == null) {
      cacheDir = getCacheDir();
    }

    final DiskCacheConfig.Builder mainDiskCacheConfigBuilder = DiskCacheConfig.newBuilder(this);
    mainDiskCacheConfigBuilder.setBaseDirectoryName("image_cache");
    mainDiskCacheConfigBuilder.setBaseDirectoryPath(cacheDir);
    mainDiskCacheConfigBuilder.setMaxCacheSize(100 * 1024 * 1024);
    mainDiskCacheConfigBuilder.setVersion(1);
    configBuilder.setMainDiskCacheConfig(mainDiskCacheConfigBuilder.build());

    final ImagePipelineConfig.Builder config = ImagePipelineConfig.newBuilder(this);
    if (BuildConfig.DEBUG) {
      final Set<RequestListener> requestListeners = new HashSet<>();
      requestListeners.add(new RequestLoggingListener());
      config.setRequestListeners(requestListeners);

      FLog.setMinimumLoggingLevel(FLog.VERBOSE);
    }
    configBuilder.setMemoryTrimmableRegistry(frescoMemoryTrimmableRegistry);

    Fresco.initialize(this, config.build());
  }

  @Override
  public void onTrimMemory(final int level) {
    super.onTrimMemory(level);

    switch (level) {

      case TRIM_MEMORY_RUNNING_MODERATE:
      case TRIM_MEMORY_RUNNING_LOW:
      case TRIM_MEMORY_BACKGROUND:
        frescoMemoryTrimmableRegistry.trim(MemoryTrimType.OnSystemLowMemoryWhileAppInBackground);
        break;

      case TRIM_MEMORY_RUNNING_CRITICAL:
      case TRIM_MEMORY_COMPLETE:
        frescoMemoryTrimmableRegistry.trim(MemoryTrimType.OnCloseToDalvikHeapLimit);
        break;
    }
  }

  private static class FrescoMemoryTrimmableRegistry implements MemoryTrimmableRegistry {

    final List<MemoryTrimmable> trimmables = new LinkedList<>();

    @Override
    public void registerMemoryTrimmable(final MemoryTrimmable trimmable) {
      trimmables.add(trimmable);
    }

    @Override
    public void unregisterMemoryTrimmable(final MemoryTrimmable trimmable) {
      trimmables.remove(trimmable);
    }

    synchronized void trim(final MemoryTrimType trimType) {
      for (MemoryTrimmable trimmable : trimmables) {
        trimmable.trim(trimType);
      }
    }
  }
}
