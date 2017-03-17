package com.shopify.sample.view.products;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.presenter.products.Product;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.ListItemViewHolder;
import com.shopify.sample.view.widget.image.ShopifyDraweeView;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.OnClick;

final class ProductListItemViewModel extends ListItemViewModel<Product> {

  ProductListItemViewModel(final Product payload) {
    super(payload, R.layout.product_list_item);
  }

  @Override public ListItemViewHolder<Product, ListItemViewModel<Product>> createViewHolder(
    final ListItemViewHolder.OnClickListener onClickListener) {
    return new ItemViewHolder(onClickListener);
  }

  static final class ItemViewHolder extends ListItemViewHolder<Product, ListItemViewModel<Product>> {
    static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    @BindView(R.id.image) ShopifyDraweeView imageView;
    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.price) TextView priceView;

    ItemViewHolder(@NonNull final OnClickListener onClickListener) {
      super(onClickListener);
    }

    @Override public void bindModel(@NonNull final ListItemViewModel<Product> listViewItemModel) {
      super.bindModel(listViewItemModel);
      imageView.loadShopifyImage(listViewItemModel.payload().imageUrl());
      titleView.setText(listViewItemModel.payload().title());
      priceView.setText(CURRENCY_FORMAT.format(listViewItemModel.payload().minPrice()));
    }

    @SuppressWarnings("unchecked") @OnClick({R.id.image, R.id.title, R.id.price})
    void onClick() {
      onClickListener().onClick(itemModel());
    }
  }
}
