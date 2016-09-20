/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.sample.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.util.LongSparseArray;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopify.buy.model.Image;
import com.shopify.buy.model.OptionValue;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;
import com.shopify.buy.utils.ColorBlender;
import com.shopify.buy.utils.DeviceUtils;
import com.shopify.sample.R;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Presents the {@link Product} details.
 */
public class ProductDetailsFragmentView extends RelativeLayout implements ProductDetailsImageAreaTouchHandler.ImageAreaCallback, ViewPager.OnPageChangeListener, ProductImagePagerAdapter.BackgroundColorListener, AppBarLayout.OnOffsetChangedListener {

    private static final int VIEW_PAGER_POSITION_NOT_AVAILABLE = -1;
    private static final String INSTANCE_STATE_SUPER = "superInstanceState";
    private static final String INSTANCE_STATE_TOOLBAR_COLLAPSED = "restoredStateToolBarCollapsed";
    private static final String INSTANCE_STATE_IMAGE_INDEX = "restoredStateImageIndex";

    private static final long IMAGE_AREA_FEATURES_ANIMATION_DURATION = 300;

    // variables for storing state information
    private boolean restoredStateAppbarIsCollapsed = false;
    private int restoredStateImageIndex = VIEW_PAGER_POSITION_NOT_AVAILABLE;

    // variables for the image area
    private int imageAreaHeight;
    private ViewGroup imageAreaWrapper;
    private Drawable imageOverlayBackgroundDrawable;

    // variables for the checkout button
    private ViewGroup checkoutButtonContainer;

    // variables for the image pager
    private ViewPager imagePager;
    private int[] backgroundColors;
    private TabLayout pagingIndicator;
    private ProductDetailsImageAreaTouchHandler imageAreaTouchHandler;

    // booleans to hold current state
    private boolean isViewPagerBeingDragged = false;
    private boolean imageAreaIsExpanded = false;
    private boolean appBarIsCollapsed = false;
    private boolean viewsAreConfigured = false;

    // stores the currently selected option values
    private final LongSparseArray<ProductDetailsVariantOptionView> visibleOptionViews = new LongSparseArray<>();

    // default currency formatter for display
    private NumberFormat currencyFormat;

    // fragment holding this view
    private ProductDetailsFragment fragment;

    // variables for the custom action bar
    private ActionBar actionBar;
    private boolean dropShadowIsShowing;
    private TextView toolbarTitle;
    private int homeDrawable;

    private AppBarLayout appBarLayout;

    // models used by the view
    private Product product;
    private ProductVariant variant;
    private ProductDetailsTheme theme;

    public ProductDetailsFragmentView(Context context) {
        super(context);
    }

    public ProductDetailsFragmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE_SUPER, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_STATE_TOOLBAR_COLLAPSED, appBarIsCollapsed);
        bundle.putInt(INSTANCE_STATE_IMAGE_INDEX, getCurrentProductImageIndex());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            restoredStateAppbarIsCollapsed = bundle.getBoolean(INSTANCE_STATE_TOOLBAR_COLLAPSED);
            restoredStateImageIndex = bundle.getInt(INSTANCE_STATE_IMAGE_INDEX, VIEW_PAGER_POSITION_NOT_AVAILABLE);
            state = bundle.getParcelable(INSTANCE_STATE_SUPER);
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * Sets the models and fills in the subviews with data
     *
     * @param fragment the fragment that owns this view
     * @param product the product to display
     * @param variant the variant to display
     */
    public void onProductAvailable(ProductDetailsFragment fragment, Product product, ProductVariant variant) {
        this.fragment = fragment;
        this.product = product;
        this.variant = variant;
        doViewConfiguration();
    }

    /**
     * Updates the product details and image for the currently selected variant
     * @param variant the {@link ProductVariant} to display
     */
    public void setVariant(ProductVariant variant) {
        this.variant = variant;
        updateProductDetails();
        setCurrentImage(product.getImage(variant));
    }

    /**
     * Populates the subviews with the relevant data from a {@link Product}
     */
    private void populateSubviews() {
        Resources res = getResources();
        List<Image> images = product.getImages();

        // don't show the pagingIndicator if we only have one image
        pagingIndicator.setVisibility(images.size() > 1 ? View.VISIBLE : View.INVISIBLE);

        imageAreaWrapper.setBackgroundColor(theme.getBackgroundColor(getResources()));

        Point displaySize = getDisplaySize();
        imagePager.setAdapter(new ProductImagePagerAdapter(images, displaySize.x, displaySize.y, this, theme.getBackgroundColor(res)));
        if (restoredStateImageIndex != VIEW_PAGER_POSITION_NOT_AVAILABLE && product.hasImage()) {
            setCurrentImage(product.getImages().get(restoredStateImageIndex));
        } else {
            setCurrentImage(product.getImage(variant));
        }

        pagingIndicator.setupWithViewPager(imagePager);
        pagingIndicator.setSelectedTabIndicatorColor(theme.getAccentColor());
        pagingIndicator.setBackgroundColor(theme.getBackgroundColor(res));

        // Setup the option views for product variant selection
        for (int i = 2; i >= 0; i--) {
            ProductDetailsVariantOptionView optionView = new ProductDetailsVariantOptionView(this, i, res, theme);
            if (i >= product.getOptions().size()) {
                optionView.hide();
            } else {
                visibleOptionViews.put(product.getOptions().get(i).getId(), optionView);
                optionView.setTheme(theme);
            }
        }

        findViewById(R.id.divider1).setBackgroundColor(theme.getDividerColor(res));
        findViewById(R.id.divider2).setBackgroundColor(theme.getDividerColor(res));

        updateProductDetails();

        // Collapse the appbar if necessary
        if (restoredStateAppbarIsCollapsed) {
            appBarLayout.setExpanded(false);
        }

        // Setup toolbar values
        toolbarTitle.setText(product.getTitle());

        // Setup minimum height for scrolling content
        // It needs to be at least the height of the screen to allow the CollapsingToolbarLayout to fully collapse
        int minimumHeight = displaySize.y - checkoutButtonContainer.getLayoutParams().height - getActionBarHeightPixels() - getStatusBarHeight() + 2;
        findViewById(R.id.product_details_container).setMinimumHeight(minimumHeight);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Initializes the views and populates them with {@link Product} details.
     */
    private void doViewConfiguration() {
        if (viewsAreConfigured) {
            return;
        }
        viewsAreConfigured = true;

        initializeImageArea();
        initializeActionBar();
        initializeImageOverlay();
        initializeCheckoutButton();
        populateSubviews();
        fragment.dismissProgressDialog();
    }

    /**
     * Get the display size.
     * @return display size in {@link Point}.
     */
    private Point getDisplaySize() {
        Point pt = new Point();

        if (DeviceUtils.isTablet(getResources())) {
            // For tablets we have explicitly set a dialog window size, so lets return that here
            pt.x = fragment.getActivity().getWindow().getAttributes().width;
            pt.y = fragment.getActivity().getWindow().getAttributes().height;
        } else {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            display.getSize(pt);
        }

        return pt;
    }

    /**
     * Get the Action Bar height.
     * @return the action bar height in pixels.
     */
    private int getActionBarHeightPixels() {
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return 0;
    }

    /**
     * Sets up the image area.
     * If the product has images, the height of the image area will be set equal to the width of the screen.
     * If the product does not have an image, the height of the image area will be equal to the action bar height.
     */
    private void initializeImageArea() {
        imageAreaWrapper = (ViewGroup) findViewById(R.id.product_details_image_wrapper);

        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        if (DeviceUtils.isTablet(getResources())) {
            screenHeight = fragment.getActivity().getWindow().getAttributes().height;
            screenWidth = fragment.getActivity().getWindow().getAttributes().width;
        }

        // Make sure the description area below the image is at least 40% of the screen height
        int minDetailsHeightInPx = Math.round(screenHeight * 0.4f);

        boolean hasImage = product != null && product.hasImage();
        int maxHeightInPx = hasImage ? screenHeight - minDetailsHeightInPx : getActionBarHeightPixels();
        imageAreaHeight = Math.min(screenWidth, maxHeightInPx);

        // Create the image pager, and set the listener
        imagePager = (ViewPager) findViewById(R.id.image_pager);
        imagePager.setAdapter(new ProductImagePagerAdapter(null, screenWidth, imageAreaHeight, this, 0));
        imagePager.setOnPageChangeListener(this);

        // Set the touch handler that registers flinging down/up for resizing image
        imageAreaTouchHandler = new ProductDetailsImageAreaTouchHandler(getContext(), this);
        imagePager.setOnTouchListener(imageAreaTouchHandler);

        pagingIndicator = (TabLayout) findViewById(R.id.indicator);

        ViewGroup.LayoutParams params = imageAreaWrapper.getLayoutParams();
        params.height = imageAreaHeight;
    }

    @Override
    public void toggleImageAreaSize() {
        setImageAreaSize(!imageAreaIsExpanded);
    }

    @Override
    public void setImageAreaSize(final boolean grow) {
        final int finalHeight = grow ? getHeight() : imageAreaHeight;

        // hide or show the checkout button
        if (grow && !imageAreaIsExpanded) {
            hideCheckoutButton(IMAGE_AREA_FEATURES_ANIMATION_DURATION);
        } else if (!grow && imageAreaIsExpanded) {
            showCheckoutButton(IMAGE_AREA_FEATURES_ANIMATION_DURATION);
        }

        // Animate the image changing size
        ValueAnimator anim = ValueAnimator.ofInt(imageAreaWrapper.getHeight(), finalHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams containerLayoutParams = imageAreaWrapper.getLayoutParams();
                containerLayoutParams.height = val;
                imageAreaWrapper.setLayoutParams(containerLayoutParams);
            }

        });
        anim.setDuration(IMAGE_AREA_FEATURES_ANIMATION_DURATION);

        // Disable touch handler for duration of image resizing
        imagePager.setOnTouchListener(null);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                // Re-enable the touch handle on the image
                imagePager.setOnTouchListener(imageAreaTouchHandler);

                if (grow) {
                    onExpandImageArea();
                } else {
                    onCollapseImageArea();
                }
            }
        });

        anim.start();
    }

    public void hideCheckoutButton(long duration) {
        checkoutButtonContainer.animate().setDuration(duration).y(getHeight()).start();
    }

    public void showCheckoutButton(long duration) {
        checkoutButtonContainer.animate().setDuration(duration).y(getHeight() - checkoutButtonContainer.getHeight()).start();
    }

    /**
     * Called when the image area has finished expanding.
     */
    private void onExpandImageArea() {
        imageAreaIsExpanded = true;
        setActionBarIconBack();
    }

    /**
     * Called when the image area has finishe collapsing.
     */
    private void onCollapseImageArea() {
        imageAreaIsExpanded = false;
        setActionBarIconClose();
    }

    private void setActionBarIconClose() {
        actionBar.setHomeAsUpIndicator(homeDrawable);
    }

    private void setActionBarIconBack() {
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    /**
     * Initializes the action bar.
     */
    private void initializeActionBar() {
        Resources res = getResources();

        AppCompatActivity activity = (AppCompatActivity) fragment.getActivity();
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_action_bar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(theme.getProductTitleColor(res));

        // Add a custom behavior to the appBarLayout.  We want it to pass touches to its children instead of scrolling.
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        layoutParams.setBehavior(new ScrollFlingAppBarLayoutBehavior());
        appBarLayout.setLayoutParams(layoutParams);

        // Listen for changes in offset so we can change the alpha on the image overlay
        appBarLayout.addOnOffsetChangedListener(this);

        // Setup the action bar
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(8);

        // If we are restoring the view we may need to manually adjust the icon
        homeDrawable = R.drawable.ic_close_white_24dp;
        if (imageAreaIsExpanded) {
            setActionBarIconBack();
        } else {
            setActionBarIconClose();
        }
    }

    /**
     * Changes the appearance of the image area and {@link AppBarLayout} as the {@code AppBarLayout} collapses.
     *
     * @param appBarLayout the layout that is being offset
     * @param offset the current offset
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();

        // The action bar is always collapsed if the product has no images
        float ratio = product.hasImage() ? (float) Math.abs(offset) / (float) maxScroll : 1.0f;

        // Adjust the close button icon for visibility if we are using the light theme
        if (theme.getStyle() == ProductDetailsTheme.Style.LIGHT) {
            if (ratio < 0.5) {
                if (homeDrawable != R.drawable.ic_close_white_24dp) {
                    homeDrawable = R.drawable.ic_close_white_24dp;
                    actionBar.setHomeAsUpIndicator(homeDrawable);
                }
            } else {
                if (homeDrawable != R.drawable.ic_close_black_24dp) {
                    homeDrawable = R.drawable.ic_close_black_24dp;
                    actionBar.setHomeAsUpIndicator(homeDrawable);
                }
            }
        }

        if (ratio == 1) {
            showDropShadow();
            appBarIsCollapsed = true;
        } else if (ratio >= 0) {
            hideDropShadow();
            appBarIsCollapsed = false;
        }

        // Disable the image pager if the AppBarLayout has started to collapse
        if (ratio > 0) {
            if (!imagePager.isFakeDragging()) {
                imagePager.beginFakeDrag();
            }
        } else {
            if(imagePager.isFakeDragging()) {
                imagePager.endFakeDrag();
            }
        }

        // Using the ratio to calculate the alpha value will cause the image and toolbar to fade in or out as the user scrolls
        int alpha = Math.round(ratio * 255f);
        imageOverlayBackgroundDrawable.setAlpha(alpha);
        toolbarTitle.setAlpha(ratio);
    }

    // hide the drop shadow for older versions of Android
    private void hideDropShadow() {
        if (!dropShadowIsShowing) {
            return;
        }
        dropShadowIsShowing = false;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            View dropShadow = findViewById(R.id.toolbar_drop_shadow);
            dropShadow.setVisibility(INVISIBLE);
        }

    }

    // Show the drop shadow for older versions of Android
    private void showDropShadow() {
        if (dropShadowIsShowing) {
            return;
        }
        dropShadowIsShowing = true;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            View dropShadow = findViewById(R.id.toolbar_drop_shadow);
            dropShadow.setVisibility(VISIBLE);
        }

    }

    private void initializeImageOverlay() {
        View imageOverlay = findViewById(R.id.image_overlay);
        imageOverlayBackgroundDrawable = new ColorDrawable(theme.getAppBarBackgroundColor(getResources()));
        imageOverlayBackgroundDrawable.setAlpha(0);
        imageOverlay.setBackgroundDrawable(imageOverlayBackgroundDrawable);
    }

    private void initializeCheckoutButton() {
        checkoutButtonContainer = (ViewGroup) findViewById(R.id.checkout_button_container);
        checkoutButtonContainer.setBackgroundColor(theme.getAccentColor());

        Button checkoutButton = (Button) findViewById(R.id.checkout_button);

        int disabledTextAlpha = 64; // 0.25 * 255
        checkoutButton.setTextColor(new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_enabled}
                },
                new int[]{
                        ColorUtils.setAlphaComponent(theme.getCheckoutLabelColor(getResources()), disabledTextAlpha),
                        theme.getCheckoutLabelColor(getResources()),}
        ));
    }

    /**
     * Fills in the views with all the {@link Product} details.
     */
    private void updateProductDetails() {
        Resources res = getResources();

        findViewById(R.id.product_details_container).setBackgroundColor(theme.getBackgroundColor(res));

        // Product title
        TextView textViewTitle = (TextView) findViewById(R.id.product_title);
        textViewTitle.setText(product.getTitle());
        textViewTitle.setTextColor(theme.getProductTitleColor(res));

        // Product price
        TextView textViewPrice = (TextView) findViewById(R.id.product_price);
        String priceWithCurrency = currencyFormat.format(Double.parseDouble(variant.getPrice()));
        textViewPrice.setText(priceWithCurrency);
        textViewPrice.setTextColor(theme.getAccentColor());

        // Product "compare at" price (appears below the actual price with a strikethrough style)
        TextView textViewCompareAtPrice = (TextView)findViewById(R.id.product_compare_at_price);
        if (!variant.isAvailable()) {
            textViewCompareAtPrice.setVisibility(View.VISIBLE);
            textViewCompareAtPrice.setText(getResources().getString(R.string.sold_out));
            textViewCompareAtPrice.setTextColor(getResources().getColor(R.color.error_background));
            textViewCompareAtPrice.setPaintFlags(0);
        } else if (!TextUtils.isEmpty(variant.getCompareAtPrice())) {
            textViewCompareAtPrice.setVisibility(View.VISIBLE);
            String compareAtPriceWithCurrency = currencyFormat.format(Double.parseDouble(variant.getCompareAtPrice()));
            textViewCompareAtPrice.setText(compareAtPriceWithCurrency);
            textViewCompareAtPrice.setTextColor(theme.getCompareAtPriceColor(res));
            textViewCompareAtPrice.setPaintFlags(textViewCompareAtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textViewCompareAtPrice.setVisibility(View.GONE);
        }

        // Set the correct values on the ProductDetailsVariantOptionViews
        List<OptionValue> optionValues = variant.getOptionValues();
        for (OptionValue optionValue : optionValues) {
            ProductDetailsVariantOptionView optionView = visibleOptionViews.get(Long.valueOf(optionValue.getOptionId()));
            if (optionView != null) {
                optionView.setOptionValue(optionValue);
            }
        }

        // Product description
        TextView textViewDescription = (TextView) findViewById(R.id.product_description);
        textViewDescription.setText(Html.fromHtml(product.getBodyHtml()), TextView.BufferType.SPANNABLE);
        textViewDescription.setTextColor(theme.getProductDescriptionColor(res));

        // Make the links clickable in the description
        // http://stackoverflow.com/questions/2734270/how-do-i-make-links-in-a-textview-clickable
        textViewDescription.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setCurrentImage(Image image) {
        if (image == null) {
            return;
        }
        ProductImagePagerAdapter adapter = (ProductImagePagerAdapter) imagePager.getAdapter();
        List<Image> images = adapter.getImages();
        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).equals(image)) {
                    imagePager.setCurrentItem(i, true);
                    break;
                }
            }
        }
    }

    public int getCurrentProductImageIndex() {
        if (imagePager != null) {
            return imagePager.getCurrentItem();
        }
        return VIEW_PAGER_POSITION_NOT_AVAILABLE;
    }

    @Override
    public void onBackgroundColorFound(int color, int position) {
        if (!theme.shouldShowProductImageBackground()) {
            return;
        }

        if (backgroundColors == null) {
            backgroundColors = new int[product.getImages().size()];
            Arrays.fill(backgroundColors, 0);
        }
        backgroundColors[position] = color;
        if (getCurrentProductImageIndex() == position) {
            imageAreaWrapper.setBackgroundColor(backgroundColors[position]);
        }
    }

    @Override
    public boolean shouldShowBackgroundColor() {
        return theme.shouldShowProductImageBackground();
    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (!theme.shouldShowProductImageBackground()) return;

        if (backgroundColors == null || backgroundColors.length <= position + 1) return;

        // As the user scrolls horizontally between product images, slowly update the
        // background color by blending the two adjacent background colors together.

        int color1 = backgroundColors[position];
        int color2 = backgroundColors[position + 1];

        if (color1 == 0 || color2 == 0) return;

        imageAreaWrapper.setBackgroundColor(ColorBlender.getBlendedColor(color1, color2, positionOffset));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // Guard against image resizing (fling up/down effect) while the user is scrolling horizontally between images
        if (state == ViewPager.SCROLL_STATE_DRAGGING && !isViewPagerBeingDragged) {
            isViewPagerBeingDragged = true;
            imagePager.setOnTouchListener(null);
        }
        if ((state == ViewPager.SCROLL_STATE_SETTLING || state == ViewPager.SCROLL_STATE_IDLE) && isViewPagerBeingDragged) {
            isViewPagerBeingDragged = false;
            imagePager.setOnTouchListener(imageAreaTouchHandler);
        }
    }

    public void setTheme(ProductDetailsTheme theme) {
        this.theme = theme;
        setBackgroundColor(theme.getBackgroundColor(getResources()));
    }

    public void setCurrencyFormat(NumberFormat currencyFormat) {
        this.currencyFormat = currencyFormat;
    }

    public boolean isImageAreaExpanded() {
        return imageAreaIsExpanded;
    }

    /**
     * Custom {@link android.support.design.widget.AppBarLayout.Behavior} to allow the {@link AppBarLayout} to pass touches on to its children.
     */
    public class ScrollFlingAppBarLayoutBehavior extends AppBarLayout.Behavior {
        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
            // don't intercept ony touches, pass them on to the views children
            return false;
        }
    }
}
