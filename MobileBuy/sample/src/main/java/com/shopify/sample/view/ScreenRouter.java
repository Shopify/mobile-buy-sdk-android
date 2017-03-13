package com.shopify.sample.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.shopify.sample.view.collections.CollectionClickActionEvent;
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
      });
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
