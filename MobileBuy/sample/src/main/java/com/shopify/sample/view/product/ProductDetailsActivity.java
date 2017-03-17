package com.shopify.sample.view.product;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.shopify.sample.R;
import com.shopify.sample.interactor.product.RealFetchProductDetails;
import com.shopify.sample.presenter.product.Product;
import com.shopify.sample.presenter.product.ProductDetailsViewPresenter;
import com.shopify.sample.view.widget.image.ImageGalleryView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ProductDetailsActivity extends AppCompatActivity implements ProductDetailsViewPresenter.View {
  public static final String EXTRAS_PRODUCT_ID = "product_id";
  public static final String EXTRAS_PRODUCT_IMAGE_URL = "product_image_url";
  public static final String EXTRAS_PRODUCT_TITLE = "product_title";

  @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayoutView;
  @BindView(R.id.toolbar) Toolbar toolbarView;
  @BindView(R.id.image_gallery) ImageGalleryView imageGalleryView;
  @BindView(R.id.product_details) ProductDetailsView productDetailsView;

  private ProductDetailsViewPresenter presenter;

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_details);
    ButterKnife.bind(this);

    String productId = getIntent().getStringExtra(EXTRAS_PRODUCT_ID);
    String productTitle = getIntent().getStringExtra(EXTRAS_PRODUCT_TITLE);
    String productImageUrl = getIntent().getStringExtra(EXTRAS_PRODUCT_IMAGE_URL);

    checkNotNull(productId, "productId == null");
    checkNotNull(productTitle, "productTitle == null");

    presenter = new ProductDetailsViewPresenter(productId, new RealFetchProductDetails());

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle(productTitle);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    swipeRefreshLayoutView.setOnRefreshListener(() -> presenter.fetchProduct());

    imageGalleryView.renderImages(Arrays.asList(productImageUrl));
  }

  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  @Override public void showProgress(final long requestId) {
    swipeRefreshLayoutView.setRefreshing(true);
  }

  @Override public void hideProgress(final long requestId) {
    swipeRefreshLayoutView.setRefreshing(false);
  }

  @Override public void onAttachedToWindow() {
    super.onAttachedToWindow();
    presenter.attachView(this);
    presenter.fetchProduct();
  }

  @Override public void showError(final long requestId, final Throwable t) {
    Snackbar.make(productDetailsView, R.string.default_error, Snackbar.LENGTH_LONG).show();
  }

  @Override public void renderProduct(final Product product) {
    imageGalleryView.renderImages(product.images());
    productDetailsView.renderProduct(product);
  }

  @SuppressWarnings("unused")
  public static final class FabBehaviour extends CoordinatorLayout.Behavior<View> {

    public FabBehaviour() {
    }

    public FabBehaviour(final Context context, final AttributeSet attrs) {
      super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
      return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
      FloatingActionButton actionButton = (FloatingActionButton) child;
      if (dependency.getHeight() / 3 < Math.abs(dependency.getTop())) {
        if (actionButton.isShown()) {
          actionButton.hide();
        }
      } else {
        if (!actionButton.isShown()) {
          actionButton.show();
        }
      }
      return false;
    }
  }
}
