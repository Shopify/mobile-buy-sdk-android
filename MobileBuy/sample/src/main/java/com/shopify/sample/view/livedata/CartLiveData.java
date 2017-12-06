package com.shopify.sample.view.livedata;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.shopify.sample.domain.model.Cart;
import com.shopify.sample.domain.model.CartItem;
import com.shopify.sample.domain.model.ProductDetail;

import static com.shopify.sample.util.Util.firstItem;
import static com.shopify.sample.util.Util.mapItems;

public class CartLiveData extends MutableLiveData<Cart> {

  private static CartLiveData instance;

  @MainThread
  public static CartLiveData get() {
    if (instance == null) {
      instance = new CartLiveData();
      instance.setValue(new Cart());
    }
    return instance;
  }

  public void addToCart(ProductDetail product) {
    if (product == null || product.variants.size() == 0) {
      return;
    }
    Cart cart = getCart();
    ProductDetail.Variant firstVariant = product.variants.get(0);
    CartItem cartItem = new CartItem(
      product.id,
      firstVariant.id,
      product.title,
      firstVariant.title,
      firstVariant.price,
      mapItems(firstVariant.selectedOptions, it -> new CartItem.Option(it.name, it.value)), firstItem(product.images));
    cart.add(cartItem);
    setValue(cart);
  }

  @SuppressWarnings("ConstantConditions")
  @NonNull
  private Cart getCart() {
    return getValue();
  }

  private CartLiveData() {
  }
}
