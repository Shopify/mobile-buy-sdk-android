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

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.sample.R;
import com.shopify.sample.activity.base.SampleListActivity;
import com.shopify.buy.model.Collection;
import com.shopify.sample.application.SampleApplication;
import com.shopify.sample.customer.CustomerLoginActivity;
import com.shopify.sample.customer.CustomerOrderListActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * The first activity in the app flow. Allows the user to browse the list of collections and drill down into a list of products.
 */
public class CollectionListActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.choose_collection);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If we haven't already loaded the products from the store, do it now
        if (listView.getAdapter() == null && !isFetching) {
            isFetching = true;
            showLoadingDialog(R.string.loading_data);

            // Fetch the collections
            getSampleApplication().getCollections(new Callback<List<Collection>>() {
                @Override
                public void success(List<Collection> collections) {
                    isFetching = false;
                    dismissLoadingDialog();
                    onFetchedCollections(collections);
                }

                @Override
                public void failure(BuyClientError error) {
                    isFetching = false;
                    onError(error);
                }
            });
        }

        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.collection_list_activity, menu);

        if (SampleApplication.getCustomer() == null) {
            menu.removeItem(R.id.action_logout);
        } else {
            menu.removeItem(R.id.action_login);
        }

        return true;
    }

    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_login: {
                final Intent intent = new Intent(this, CustomerLoginActivity.class);
                startActivity(intent);
                return true;
            }

            case R.id.action_logout: {
                SampleApplication.setCustomer(null);
                SampleApplication.getBuyClient().logoutCustomer(new Callback<Void>() {
                    @Override
                    public void success(Void body) {
                    }

                    @Override
                    public void failure(BuyClientError error) {
                    }
                });
                invalidateOptionsMenu();
                return true;
            }

            case R.id.action_orders:
                onOrdersClick();
                return true;

            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    /**
     * Once the collections are fetched from the server, set our listView adapter so that the collections appear on screen.
     *
     * @param collections
     */
    private void onFetchedCollections(final List<Collection> collections) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final List<String> collectionTitles = new ArrayList<String>();

                // Add an 'All Products' collection just in case there are products that do not belong to a collection
                collectionTitles.add(getString(R.string.all_products));
                for (Collection collection : collections) {
                    collectionTitles.add(collection.getTitle());
                }

                listView.setAdapter(new ArrayAdapter<>(CollectionListActivity.this, R.layout.simple_list_item, collectionTitles));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            onCollectionClicked(null);
                        } else {
                            onCollectionClicked(collections.get(position - 1).getCollectionId());
                        }
                    }
                });
            }
        });
    }

    /**
     * When the user picks a collection, launch the product list activity to display the products in that collection.
     *
     * @param collectionId
     */
    private void onCollectionClicked(Long collectionId) {
        Intent intent = new Intent(this, ProductListActivity.class);
        if (collectionId != null) {
            intent.putExtra(ProductListActivity.EXTRA_COLLECTION_ID, collectionId);
        }
        startActivity(intent);
    }

    private void onOrdersClick() {
        final Intent customerOrderListActivityIntent = new Intent(this, CustomerOrderListActivity.class);
        if (SampleApplication.getCustomer() == null) {
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, customerOrderListActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            final Intent intent = new Intent(this, CustomerLoginActivity.class);
            intent.putExtra(CustomerLoginActivity.EXTRAS_PENDING_ACTIVITY_INTENT, pendingIntent);
            startActivity(intent);
        } else {
            startActivity(customerOrderListActivityIntent);
        }
    }
}
