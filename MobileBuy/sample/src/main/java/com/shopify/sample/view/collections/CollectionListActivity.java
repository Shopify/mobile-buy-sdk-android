package com.shopify.sample.view.collections;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.shopify.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class CollectionListActivity extends AppCompatActivity {

  @BindView(R.id.collection_list) CollectionListView collectionListView;
  @BindView(R.id.toolbar) Toolbar toolbarView;

  @SuppressWarnings("ConstantConditions") @Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_collection_list);
    ButterKnife.bind(this);

    setSupportActionBar(toolbarView);
    getSupportActionBar().setTitle(R.string.collection_list_title);
    getSupportActionBar().setLogo(R.drawable.ic_logo);
  }
}
