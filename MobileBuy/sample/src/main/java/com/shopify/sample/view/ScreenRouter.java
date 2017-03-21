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

package com.shopify.sample.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.shopify.sample.view.base.CartClickActionEvent;
import com.shopify.sample.view.cart.CartActivity;
import com.shopify.sample.view.collections.CollectionClickActionEvent;
import com.shopify.sample.view.collections.CollectionProductClickActionEvent;
import com.shopify.sample.view.product.ProductDetailsActivity;
import com.shopify.sample.view.products.ProductClickActionEvent;
import com.shopify.sample.view.products.ProductListActivity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ScreenRouter {
  private interface SingletonHolder {
    ScreenRouter INSTANCE = new ScreenRouter();
  }

  @SuppressWarnings("unchecked") public static <T extends ScreenActionEvent> void route(@NonNull final Context context,
    @NonNull final T screenActionEvent) {
    SingletonHolder.INSTANCE.routeInternal(context, screenActionEvent);
  }

  private final Map<String, BiConsumer<Context, ? extends ScreenActionEvent>> routes = new LinkedHashMap<>();

  private ScreenRouter() {
    this
      .<CollectionClickActionEvent>registerInternal(CollectionClickActionEvent.ACTION, (context, event) -> {
        Intent intent = new Intent(context, ProductListActivity.class);
        intent.putExtra(ProductListActivity.EXTRAS_COLLECTION_ID, event.id());
        intent.putExtra(ProductListActivity.EXTRAS_COLLECTION_IMAGE_URL, event.imageUrl());
        intent.putExtra(ProductListActivity.EXTRAS_COLLECTION_TITLE, event.title());
        intent.putExtra(ScreenActionEvent.class.getName(), event);
        context.startActivity(intent);
      })
      .<CollectionProductClickActionEvent>registerInternal(CollectionProductClickActionEvent.ACTION, ((context, event) -> {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtra(ProductDetailsActivity.EXTRAS_PRODUCT_ID, event.id());
        intent.putExtra(ProductDetailsActivity.EXTRAS_PRODUCT_IMAGE_URL, event.imageUrl());
        intent.putExtra(ProductDetailsActivity.EXTRAS_PRODUCT_TITLE, event.title());
        intent.putExtra(ScreenActionEvent.class.getName(), event);
        context.startActivity(intent);
      }))
      .<ProductClickActionEvent>registerInternal(ProductClickActionEvent.ACTION, ((context, event) -> {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtra(ProductDetailsActivity.EXTRAS_PRODUCT_ID, event.id());
        intent.putExtra(ProductDetailsActivity.EXTRAS_PRODUCT_IMAGE_URL, event.imageUrl());
        intent.putExtra(ProductDetailsActivity.EXTRAS_PRODUCT_TITLE, event.title());
        intent.putExtra(ScreenActionEvent.class.getName(), event);
        context.startActivity(intent);
      }))
      .<CartClickActionEvent>registerInternal(CartClickActionEvent.ACTION, ((context, event) -> {
        Intent intent = new Intent(context, CartActivity.class);
        intent.putExtra(ScreenActionEvent.class.getName(), event);
        context.startActivity(intent);
      }));
  }

  private <T extends ScreenActionEvent> ScreenRouter registerInternal(final String action, final BiConsumer<Context, T> consumer) {
    routes.put(action, consumer);
    return this;
  }

  @SuppressWarnings("unchecked") private <T extends ScreenActionEvent> void routeInternal(final Context context,
    final T screenActionEvent) {
    checkNotNull(context, "context == null");
    checkNotNull(screenActionEvent, "screenActionEvent == null");
    BiConsumer<Context, T> consumer = (BiConsumer<Context, T>) routes.get(screenActionEvent.action());
    if (consumer == null) {
      throw new IllegalArgumentException(String.format("Can't find route for: %s", screenActionEvent.action));
    }
    consumer.accept(context, screenActionEvent);
  }
}
