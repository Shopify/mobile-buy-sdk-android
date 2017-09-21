package com.shopify.sample.view.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class LifecycleFrameLayout extends FrameLayout implements LifecycleOwner {

  private final LifecycleRegistry registry = new LifecycleRegistry(this);

  public LifecycleFrameLayout(@NonNull final Context context) {
    super(context);
    initialize();
  }

  public LifecycleFrameLayout(@NonNull final Context context, @Nullable final AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public LifecycleFrameLayout(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public LifecycleFrameLayout(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
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
