package com.shopify.sample.view.product;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.shopify.sample.R;
import com.shopify.sample.presenter.product.Product;
import com.shopify.sample.util.Util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shopify.sample.util.Util.mapItems;
import static com.shopify.sample.util.Util.minItem;

public final class ProductDetailsView extends NestedScrollView {
  static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

  @BindView(R.id.title) TextView titleView;
  @BindView(R.id.price) TextView priceView;
  @BindView(R.id.description) TextView descriptionView;

  public ProductDetailsView(final Context context) {
    super(context);
  }

  public ProductDetailsView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  public ProductDetailsView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public void renderProduct(final Product product) {
    titleView.setText(product.title());
    priceView.setText(getResources().getString(R.string.price_from, formatMinPrice(product)));
    descriptionView.setText(Html.fromHtml(product.description()));
  }

  private String formatMinPrice(final Product product) {
    List<BigDecimal> prices = mapItems(product.variants(), Product.Variant::price);
    BigDecimal minPrice = minItem(prices, BigDecimal.ZERO, BigDecimal::compareTo);
    return CURRENCY_FORMAT.format(minPrice);
  }
}
