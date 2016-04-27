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

package com.shopify.buy.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shopify.buy.R;
import com.shopify.buy.customTabs.CustomTabActivityHelper;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;
import com.shopify.buy.model.Shop;
import com.shopify.buy.utils.CurrencyFormatter;

import java.net.HttpURLConnection;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * The fragment that controls the presentation of the {@link Product} details.
 */
public class ProductDetailsFragment extends Fragment {

    private ProductDetailsListener productDetailsListener;

    private ProductDetailsFragmentView view;
    private ProgressDialog progressDialog;

    private Product product;
    private ProductVariant variant;
    private String productId;
    private BuyClient buyClient;
    private Shop shop;

    private ProductDetailsTheme theme;
    private Button checkoutButton;

    private boolean viewCreated;

    private final AtomicBoolean cancelledCheckout = new AtomicBoolean(false);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ProductDetailsFragmentView) inflater.inflate(R.layout.fragment_product_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated = true;

        initTheme();
        configureCheckoutButton();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initializeBuyClient();
        initializeProgressDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (view.isImageAreaExpanded()) {
                view.setImageAreaSize(false);
            } else {
                productDetailsListener.onCancel(null);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // fetch the Shop and Product data if we don't have them already
        if (product == null && !TextUtils.isEmpty(productId)) {
            fetchProduct(productId);
        }
        if (shop == null) {
            fetchShop();
        }

        showProductIfReady();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkoutButton.setEnabled(true);
        cancelledCheckout.set(false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            productDetailsListener = (ProductDetailsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnProductDetailsCompletedListener");
        }
    }

    private void initTheme() {
        // First try to get the theme from the bundle, then fallback to a default theme
        Bundle arguments = getArguments();
        if (arguments != null) {
            Parcelable bundleTheme = arguments.getParcelable(ProductDetailsConfig.EXTRA_THEME);
            if (bundleTheme != null && bundleTheme instanceof ProductDetailsTheme) {
                theme = (ProductDetailsTheme) bundleTheme;
            }
        }
        if (theme == null) {
            theme = new ProductDetailsTheme(getResources());
        }
        view.setTheme(theme);
    }

    private void configureCheckoutButton() {
        checkoutButton = (Button)view.findViewById(R.id.checkout_button);

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVariant(variant);
                checkoutButton.setEnabled(false);
                cancelledCheckout.set(false);
                createWebCheckout();

                showProgressDialog(getString(R.string.loading), getString(R.string.loading_checkout_page), new Runnable() {
                    @Override
                    public void run() {
                        checkoutButton.setEnabled(true);
                        cancelledCheckout.set(true);
                    }
                });
            }
        });
    }

    private void initializeBuyClient() {

        Bundle bundle = getArguments();

        // Retrieve all the items required to create a BuyClient
        String apiKey = bundle.getString(ProductDetailsConfig.EXTRA_SHOP_API_KEY);
        String shopDomain = bundle.getString(ProductDetailsConfig.EXTRA_SHOP_DOMAIN);
        String channelId = bundle.getString(ProductDetailsConfig.EXTRA_SHOP_CHANNEL_ID);
        String applicationName = bundle.getString(ProductDetailsConfig.EXTRA_SHOP_APPLICATION_NAME);

        // Retrieve the id of the Product we are going to display
        productId = bundle.getString(ProductDetailsConfig.EXTRA_SHOP_PRODUCT_ID);

        // If we have a full product object in the bundle, we don't need to fetch it
        if (bundle.containsKey(ProductDetailsConfig.EXTRA_SHOP_PRODUCT)) {
            product = Product.fromJson(bundle.getString(ProductDetailsConfig.EXTRA_SHOP_PRODUCT));
            variant = product.getVariants().get(0);
        }

        // If we have a full shop object in the bundle, we don't need to fetch it
        if (bundle.containsKey(ProductDetailsConfig.EXTRA_SHOP_SHOP)) {
            shop = Shop.fromJson(bundle.getString(ProductDetailsConfig.EXTRA_SHOP_SHOP));
        }

        // Retrieve the optional settings
        String webReturnToUrl = bundle.getString(ProductDetailsConfig.EXTRA_WEB_RETURN_TO_URL);
        String webReturnToLabel = bundle.getString(ProductDetailsConfig.EXTRA_WEB_RETURN_TO_LABEL);

        // Create the BuyClient
        buyClient = BuyClientFactory.getBuyClient(shopDomain, apiKey, channelId, applicationName);

        // Set the optional web return to values
        if (!TextUtils.isEmpty(webReturnToUrl)) {
            buyClient.setWebReturnToUrl(webReturnToUrl);
        }

        if (!TextUtils.isEmpty(webReturnToLabel)) {
            buyClient.setWebReturnToLabel(webReturnToLabel);
        }
    }

    private void initializeProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
    }

    private void showProgressDialog(final String title, final String message, final Runnable onCancel) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setTitle(title);
                progressDialog.setMessage(message);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        onCancel.run();
                    }
                });
                progressDialog.show();
            }
        });
    }

    void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    private void fetchShop() {
        buyClient.getShop(new Callback<Shop>() {
            @Override
            public void success(Shop shop, Response response) {
                ProductDetailsFragment.this.shop = shop;
                showProductIfReady();
            }

            @Override
            public void failure(RetrofitError error) {
                productDetailsListener.onFailure(createErrorBundle(ProductDetailsConstants.ERROR_GET_SHOP_FAILED, BuyClient.getErrorBody(error)));
            }
        });
    }

    private void fetchProduct(final String productId) {
        buyClient.getProduct(productId, new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                if (product != null) {
                    // Default to having the first variant selected in the UI
                    ProductVariant variant = product.getVariants().get(0);
                    ProductDetailsFragment.this.product = product;
                    ProductDetailsFragment.this.variant = variant;
                    showProductIfReady();
                } else {
                    productDetailsListener.onFailure(createErrorBundle(ProductDetailsConstants.ERROR_GET_PRODUCT_FAILED, "Product id not found: " + productId));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                productDetailsListener.onFailure(createErrorBundle(ProductDetailsConstants.ERROR_GET_PRODUCT_FAILED, BuyClient.getErrorBody(error)));
            }
        });
    }

    private Bundle createErrorBundle(int errorCode, String errorMessage) {
        Bundle bundle = new Bundle();
        bundle.putInt(ProductDetailsConstants.EXTRA_ERROR_CODE, errorCode);
        bundle.putString(ProductDetailsConstants.EXTRA_ERROR_MESSAGE, errorMessage);
        return bundle;
    }

    private void showProductIfReady() {
        // Check for the prerequisites before populating the views
        if (!viewCreated || product == null || shop == null) {
            // we're still loading, make sure we show the progress dialog
            if (!progressDialog.isShowing()) {
                showProgressDialog(getString(R.string.loading), getString(R.string.loading_product_details), new Runnable() {
                    @Override
                    public void run() {
                        getActivity().finish();
                    }
                });
            }
            return;
        }

        NumberFormat currencyFormat = CurrencyFormatter.getFormatter(Locale.getDefault(), shop.getCurrency());

        // Create a VariantSelectionController which will manage the dialogs that allow the user to pick a product variant
        VariantSelectionController variantSelectionController = new VariantSelectionController(getActivity(), view, product, variant, theme, currencyFormat);
        variantSelectionController.setListener(onVariantSelectedListener);

        // Tell the view that it can populate the product details components now
        view.setCurrencyFormat(currencyFormat);
        view.onProductAvailable(ProductDetailsFragment.this, product, variant);

        // Disable the checkout button if the selected product variant is sold out
        checkoutButton.setEnabled(variant.isAvailable());
    }

    private final VariantSelectionController.OnVariantSelectedListener onVariantSelectedListener = new VariantSelectionController.OnVariantSelectedListener() {
        @Override
        public void onVariantSelected(ProductVariant variant) {
            ProductDetailsFragment.this.variant = variant;
            view.setVariant(variant);

            // Disable the checkout button if the selected product variant is sold out
            checkoutButton.setEnabled(variant.isAvailable());
        }
    };

    /**
     * Creates a checkout for use with the web checkout flow
     */
    private void createWebCheckout() {
        // Build a Cart with the selected ProductVariant
        Cart cart = new Cart();
        cart.addVariant(variant);

        // Create the checkout using our Cart
        buyClient.createCheckout(new Checkout(cart), new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                if (response.getStatus() == HttpURLConnection.HTTP_CREATED) {
                    // Start the web checkout
                    launchWebCheckout(checkout);
                } else {
                    onCheckoutFailure();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                onCheckoutFailure();
            }
        });

    }

    /**
     * Show the error message in a {@link Snackbar}
     */
    private void onCheckoutFailure() {
        dismissProgressDialog();

        CoordinatorLayout snackbarLayout = (CoordinatorLayout) view.findViewById(R.id.snackbar_location);

        Snackbar snackbar = Snackbar.make(snackbarLayout, R.string.default_checkout_error, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.error_background));

        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar snackbar) {
                super.onShown(snackbar);
                checkoutButton.setEnabled(false);
            }

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                checkoutButton.setEnabled(true);
            }
        });

        snackbar.show();
    }

    /**
     * Launch Chrome, and open the correct url for our {@code Checkout}
     *
     * @param checkout
     */
    private void launchWebCheckout(Checkout checkout) {
        // if the user dismissed the progress dialog before we got here, abort
        if (cancelledCheckout.getAndSet(false)) {
            return;
        }

        dismissProgressDialog();

        String uri = checkout.getWebUrl();

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(theme.getAppBarBackgroundColor(getResources()))
                .setShowTitle(true)
                .build();
        CustomTabActivityHelper.openCustomTab(
                getActivity(), customTabsIntent, Uri.parse(uri), new BrowserFallback());


        // The checkout was successfully started, let the listener know.
        Bundle bundle = new Bundle();
        bundle.putString(ProductDetailsConstants.EXTRA_CHECKOUT, checkout.toJsonString());
        productDetailsListener.onSuccess(bundle);
    }


    /**
     * A Fallback that opens any available Browser when Custom Tabs is not available
     */
    private class BrowserFallback implements CustomTabActivityHelper.CustomTabFallback {

        @Override
        public void openUri(Activity activity, Uri uri) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setData(uri);

            try {
                intent.setPackage("com.android.chrome");
                startActivity(intent);

            } catch (Exception launchChromeException) {
                try {
                    // Chrome could not be opened, attempt to us other launcher
                    intent.setPackage(null);
                    startActivity(intent);

                } catch (Exception launchOtherException) {
                    onCheckoutFailure();
                }
            }

        }
    }



}
