package com.shopify.sample.view.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.shopify.sample.R;
import com.shopify.sample.view.widget.image.ImageGalleryView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ProductDetailsActivity extends AppCompatActivity {
  public static final String EXTRAS_PRODUCT_ID = "product_id";
  public static final String EXTRAS_PRODUCT_IMAGE_URL = "product_image_url";
  public static final String EXTRAS_PRODUCT_TITLE = "product_title";

  @BindView(R.id.toolbar) Toolbar toolbarView;
  @BindView(R.id.image_gallery) ImageGalleryView imageGalleryView;

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_details);
    ButterKnife.bind(this);

    String productId = getIntent().getStringExtra(EXTRAS_PRODUCT_ID);
    String productTitle = getIntent().getStringExtra(EXTRAS_PRODUCT_TITLE);
    String productImageUrl = getIntent().getStringExtra(EXTRAS_PRODUCT_IMAGE_URL);

    checkNotNull(productId, "productId == null");
    checkNotNull(productTitle, "productTitle == null");

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle(productTitle);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    imageGalleryView.images(Arrays.asList(productImageUrl, productImageUrl, productImageUrl, productImageUrl, productImageUrl));
  }

  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
