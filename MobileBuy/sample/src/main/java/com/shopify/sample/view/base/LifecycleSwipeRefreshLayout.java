package com.shopify.sample.view.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

public class LifecycleSwipeRefreshLayout extends SwipeRefreshLayout implements LifecycleOwner {

  private final LifecycleRegistry registry = new LifecycleRegistry(this);

  public LifecycleSwipeRefreshLayout(final Context context) {
    super(context);
    initialize();
  }

  public LifecycleSwipeRefreshLayout(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  @Override
  public Lifecycle getLifecycle() {
    return registry;
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    registry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
  }

  @Override
  protected void onDetachedFromWindow() {
    registry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    super.onDetachedFromWindow();
  }

  private void initialize() {
    registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
  }
}
