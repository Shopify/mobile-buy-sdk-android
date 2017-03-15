package com.shopify.sample.view.products;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.shopify.sample.R;
import com.shopify.sample.view.widget.image.ShopifyDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shopify.sample.util.Util.checkNotNull;

public final class ProductListActivity extends AppCompatActivity {
  public static final String EXTRAS_COLLECTION_ID = "collection_id";
  public static final String EXTRAS_COLLECTION_IMAGE_URL = "collection_image_url";
  public static final String EXTRAS_COLLECTION_TITLE = "collection_title";

  @BindView(R.id.toolbar) Toolbar toolbarView;
  @BindView(R.id.collection_image) ShopifyDraweeView collectionImageView;
  @BindView(R.id.product_list) ProductListView productListView;

  @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_list);
    ButterKnife.bind(this);

    String collectionId = getIntent().getStringExtra(EXTRAS_COLLECTION_ID);
    String collectionTitle = getIntent().getStringExtra(EXTRAS_COLLECTION_TITLE);
    String collectionImageUrl = getIntent().getStringExtra(EXTRAS_COLLECTION_IMAGE_URL);

    checkNotNull(collectionId, "collectionId == null");
    checkNotNull(collectionTitle, "collectionTitle == null");

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle(collectionTitle);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    collectionImageView.loadShopifyImage(collectionImageUrl);
    productListView.refresh(collectionId);
  }

  @Override public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
