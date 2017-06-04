package com.shopify.sample.view.cart;

import android.support.annotation.NonNull;

import com.shopify.sample.domain.interactor.CartAddItemInteractor;
import com.shopify.sample.domain.interactor.CartFetchInteractor;
import com.shopify.sample.domain.interactor.CartRemoveItemInteractor;
import com.shopify.sample.domain.interactor.CartWatchInteractor;
import com.shopify.sample.domain.interactor.RealCartAddItemInteractor;
import com.shopify.sample.domain.interactor.RealCartFetchInteractor;
import com.shopify.sample.domain.interactor.RealCartRemoveItemInteractor;
import com.shopify.sample.domain.interactor.RealCartWatchInteractor;
import com.shopify.sample.domain.model.Cart;
import com.shopify.sample.domain.model.CartItem;
import com.shopify.sample.view.BasePaginatedListViewModel;
import com.shopify.sample.view.base.ListItemViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableTransformer;

@SuppressWarnings("WeakerAccess")
public class CartListViewModel extends BasePaginatedListViewModel<CartItem> implements
  CartListItemViewModel.OnChangeQuantityClickListener {
  private final CartWatchInteractor cartWatchInteractor = new RealCartWatchInteractor();
  private final CartAddItemInteractor cartAddItemInteractor = new RealCartAddItemInteractor();
  private final CartRemoveItemInteractor cartRemoveItemInteractor = new RealCartRemoveItemInteractor();
  private final CartFetchInteractor cartFetchInteractor = new RealCartFetchInteractor();

  @Override protected ObservableTransformer<String, List<CartItem>> nextPageRequestComposer() {
    return upstream -> upstream.flatMap(cursor -> cartWatchInteractor.execute().map(Cart::cartItems));
  }

  @NonNull @Override protected List<ListItemViewModel> convertAndMerge(@NonNull final List<CartItem> newItems,
    @NonNull final List<ListItemViewModel> existingItems) {
    List<ListItemViewModel> viewModels = new ArrayList<>();
    for (CartItem item : newItems) {
      viewModels.add(new CartListItemViewModel(item, this));
    }
    if (!newItems.isEmpty()) {
      viewModels.add(new CartSubtotalListItemViewModel(cartFetchInteractor.execute()));
    }
    return viewModels;
  }

  @Override public void onAddCartItemClick(final CartItem cartItem) {
    cartAddItemInteractor.execute(cartItem);
  }

  @Override public void onRemoveCartItemClick(final CartItem cartItem) {
    cartRemoveItemInteractor.execute(cartItem);
  }
}
