package com.shopify.sample.view.products;

import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.presenter.products.Product;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.ListViewItemHolder;
import com.shopify.sample.view.base.ShopifyDraweeView;

import java.text.NumberFormat;

import butterknife.BindView;

final class ProductListItemViewModel extends ListItemViewModel<Product> {

  ProductListItemViewModel(final Product payload) {
    super(payload, R.layout.product_list_item);
  }

  @Override public ListViewItemHolder<Product, ListItemViewModel<Product>> createViewHolder() {
    return new ProductListItemViewModel.ViewItemHolder();
  }

  static final class ViewItemHolder extends ListViewItemHolder<Product, ListItemViewModel<Product>> {
    static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    @BindView(R.id.image) ShopifyDraweeView imageView;
    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.price) TextView priceView;

    @Override public void bindModel(final ListItemViewModel<Product> listViewItemModel) {
      super.bindModel(listViewItemModel);
      imageView.loadShopifyImage(listViewItemModel.payload().imageUrl());
      titleView.setText(listViewItemModel.payload().title());
      priceView.setText(CURRENCY_FORMAT.format(listViewItemModel.payload().minPrice()));
    }
  }
}
