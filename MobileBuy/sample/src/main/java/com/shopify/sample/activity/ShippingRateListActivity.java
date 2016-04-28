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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.dataprovider.RetrofitError;
import com.shopify.sample.R;
import com.shopify.sample.activity.base.SampleListActivity;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.ShippingRate;

import java.util.List;

import retrofit2.Response;

/**
 * If the selected product requires shipping, this activity allows the user to select a list of shipping rates.
 * For the sample app, the shipping address has been hardcoded and we will only see the shipping rates applicable to that address.
 */
public class ShippingRateListActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.choose_shipping_rate);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If we haven't already loaded the products from the store, do it now
        if (listView.getAdapter() == null && !isFetching) {
            isFetching = true;
            showLoadingDialog(R.string.loading_data);
            fetchShippingRates();
        }
    }

    /**
     * Fetching shipping rates requires communicating with 3rd party shipping servers, so we need to poll until all the rates have been fetched.
     */
    private void fetchShippingRates() {
        getSampleApplication().getShippingRates(new Callback<List<ShippingRate>>() {
            @Override
            public void success(List<ShippingRate> shippingRates, Response response) {
                isFetching = false;

                // The application should surface to the user that their items cannot be shipped to that location
                if (shippingRates.size() == 0) {
                    Toast.makeText(ShippingRateListActivity.this, R.string.no_shipping_rates, Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }

                onFetchedShippingRates(shippingRates);
            }

            @Override
            public void failure(RetrofitError error) {
                isFetching = false;

                // Handle error
                onError(error);
            }
        });
    }

    /**
     * Once the shipping rates have been fetched, display the name and price of each rate in the list.
     *
     * @param shippingRates
     */
    private void onFetchedShippingRates(final List<ShippingRate> shippingRates) {
        dismissLoadingDialog();

        listView.setAdapter(new ArrayAdapter<ShippingRate>(ShippingRateListActivity.this, R.layout.shipping_rate_list_item, shippingRates) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;

                if (view == null) {
                    view = View.inflate(getContext(), R.layout.shipping_rate_list_item, null);
                }

                ShippingRate rate = getItem(position);
                ((TextView) view.findViewById(R.id.list_item_left_text)).setText(rate.getTitle());
                ((TextView) view.findViewById(R.id.list_item_right_text)).setText('$' + rate.getPrice());

                return view;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onShippingRateSelected(shippingRates.get(position));
            }
        });
    }

    /**
     * When the user selects a shipping rate, set that rate on the checkout and proceed to the discount activity.
     *
     * @param shippingRate
     */
    private void onShippingRateSelected(ShippingRate shippingRate) {
        showLoadingDialog(R.string.syncing_data);

        getSampleApplication().setShippingRate(shippingRate, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                dismissLoadingDialog();
                startActivity(new Intent(ShippingRateListActivity.this, DiscountActivity.class));
            }

            @Override
            public void failure(RetrofitError error) {
                onError(error);
            }
        });
    }

}

