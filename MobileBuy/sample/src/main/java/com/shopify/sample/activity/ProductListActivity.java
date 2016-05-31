/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Product;
import com.shopify.sample.R;
import com.shopify.sample.activity.base.SampleListActivity;
import com.shopify.sample.dialog.HSVColorPickerDialog;
import com.shopify.sample.ui.ProductDetailsTheme;

import java.util.ArrayList;
import java.util.List;


/**
 * This activity allows the user to select a product to purchase from a list of all products in a collection.
 */
public class ProductListActivity extends SampleListActivity {

    static final String EXTRA_COLLECTION_ID = "ProductListActivity.EXTRA_COLLECTION_ID";

    private Long collectionId;
    private ProductDetailsTheme theme;
    private boolean useProductDetailsActivity;
    private View accentColorView;
    private View productViewOptionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.product_list_activity);

        setTitle(R.string.choose_product);

        useProductDetailsActivity = false;
        theme = new ProductDetailsTheme(getResources());

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_COLLECTION_ID)) {
            collectionId = intent.getLongExtra(EXTRA_COLLECTION_ID, -1);
        }

        productViewOptionsContainer = findViewById(R.id.product_view_options_container);
        productViewOptionsContainer.setVisibility(View.GONE);

        ((Switch) findViewById(R.id.product_details_activity_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                useProductDetailsActivity = isChecked;
                productViewOptionsContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        ((Switch) findViewById(R.id.theme_style_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                theme.setStyle(isChecked ? ProductDetailsTheme.Style.LIGHT : ProductDetailsTheme.Style.DARK);
            }
        });

        ((Switch) findViewById(R.id.product_image_bg_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                theme.setShowProductImageBackground(isChecked);
            }
        });

        accentColorView = findViewById(R.id.accent_color_view);
        accentColorView.setBackgroundColor(theme.getAccentColor());
        accentColorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAccentColor();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If we haven't already loaded the products from the store, do it now
        if (listView.getAdapter() == null && !isFetching) {
            isFetching = true;
            showLoadingDialog(R.string.loading_data);

            Callback<List<Product>> callback = new Callback<List<Product>>() {
                @Override
                public void success(List<Product> products) {
                    isFetching = false;
                    dismissLoadingDialog();
                    onFetchedProducts(products);
                }

                @Override
                public void failure(BuyClientError error) {
                    isFetching = false;
                    onError(error);
                }
            };

            if (collectionId != null) {
                getSampleApplication().getProducts(collectionId, callback);
            } else {
                List<Product> allProducts = new ArrayList<>();
                getSampleApplication().getAllProducts(1, allProducts, callback);
            }
        }
    }

    private void chooseAccentColor() {
        HSVColorPickerDialog cpd = new HSVColorPickerDialog(this, theme.getAccentColor(), new HSVColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void colorSelected(Integer color) {
                theme.setAccentColor(color);
                accentColorView.setBackgroundColor(color);
            }
        });
        cpd.setTitle(R.string.choose_accent_color);
        cpd.show();
    }

    /**
     * Once the products are fetched from the server, set our listView adapter so that the products appear on screen.
     *
     * @param products
     */
    private void onFetchedProducts(final List<Product> products) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<String> productTitles = new ArrayList<String>();
                for (Product product : products) {
                    productTitles.add(product.getTitle());
                }

                listView.setAdapter(new ArrayAdapter<>(ProductListActivity.this, R.layout.simple_list_item, productTitles));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (useProductDetailsActivity) {
                            launchProductDetailsActivity(products.get(position));
                        } else {
                            createCheckout(products.get(position));
                        }
                    }
                });
            }
        });
    }

    private void launchProductDetailsActivity(Product product) {
        getSampleApplication().launchProductDetailsActivity(this, product, theme);
    }

    /**
     * When the user selects a product, create a new checkout for that product.
     *
     * @param product
     */
    private void createCheckout(final Product product) {
        showLoadingDialog(R.string.syncing_data);

        getSampleApplication().createCheckout(product, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                dismissLoadingDialog();
                onCheckoutCreated(checkout);
            }

            @Override
            public void failure(BuyClientError error) {
                // If we can't create a checkout, we still want to give the user the cart permalink option in the CheckoutActivity
                dismissLoadingDialog();
                Toast.makeText(ProductListActivity.this, getString(R.string.unsupported_gateway), Toast.LENGTH_LONG).show();
                startActivity(new Intent(ProductListActivity.this, CheckoutActivity.class));
            }
        });
    }

    /**
     * If the selected product requires shipping, show the list of shipping rates so the user can pick one.
     * Otherwise, skip to the discounts activity (gift card codes and discount codes).
     *
     * @param checkout
     */
    private void onCheckoutCreated(Checkout checkout) {
        startActivity(new Intent(ProductListActivity.this, DiscountActivity.class));
    }
}

