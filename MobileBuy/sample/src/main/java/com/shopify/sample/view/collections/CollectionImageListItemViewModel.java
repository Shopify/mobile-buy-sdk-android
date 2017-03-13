package com.shopify.sample.view.collections;

import android.view.View;

import com.shopify.sample.R;
import com.shopify.sample.presenter.collections.Collection;
import com.shopify.sample.view.ScreenRouter;
import com.shopify.sample.view.base.ListItemViewModel;
import com.shopify.sample.view.base.ListViewItemHolder;
import com.shopify.sample.view.base.ShopifyDraweeView;

import butterknife.BindView;
import butterknife.OnClick;

final class CollectionImageListItemViewModel extends ListItemViewModel<Collection> {

  CollectionImageListItemViewModel(final Collection payload) {
    super(payload, R.layout.collection_image_list_item);
  }

  @Override public ListViewItemHolder<Collection, ListItemViewModel<Collection>> createViewHolder() {
    return new ViewItemHolder();
  }

  static final class ViewItemHolder extends ListViewItemHolder<Collection, ListItemViewModel<Collection>> {
    @BindView(R.id.image) ShopifyDraweeView imageView;

    @Override public void bindModel(final ListItemViewModel<Collection> listViewItemModel) {
      super.bindModel(listViewItemModel);
      imageView.loadShopifyImage(listViewItemModel.payload().imageUrl());
    }

    @OnClick(R.id.image)
    void onImageClick(final View v) {
      ScreenRouter.route(v.getContext(), new CollectionClickActionEvent(itemModel().payload()));
    }
  }
}
