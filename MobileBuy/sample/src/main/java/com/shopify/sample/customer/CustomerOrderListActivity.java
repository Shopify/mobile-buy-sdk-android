/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package com.shopify.sample.customer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.model.LineItem;
import com.shopify.buy.model.Order;
import com.shopify.sample.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class CustomerOrderListActivity extends AppCompatActivity implements CustomerOrderListViewPresenter.View {

    private static final String LOG_TAG = CustomerOrderListActivity.class.getSimpleName();

    @BindView(R.id.order_list)
    RecyclerView orderListView;

    private ProgressDialog progressDialog;

    private final CustomerOrderListViewPresenter presenter = new CustomerOrderListViewPresenter();

    @Override
    public void showOrderList(final List<Order> orders) {
        orderListView.setAdapter(new OrderListViewAdapter(orders, LayoutInflater.from(this)));
    }

    @Override
    public void showError(final Throwable t) {
        if (t instanceof BuyClientError) {
            Log.e(LOG_TAG, "Error: " + ((BuyClientError) t).getRetrofitErrorBody(), t);
        } else {
            Log.e(LOG_TAG, t.getMessage(), t);
        }
        Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.customer_order_list_activity);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                CustomerOrderListActivity.this.finish();
            }
        });
        progressDialog.setMessage(getString(R.string.please_wait));

        orderListView.setLayoutManager(new LinearLayoutManager(this));
        orderListView.setHasFixedSize(true);

        presenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    static final class OrderListViewAdapter extends RecyclerView.Adapter<OrderListViewAdapter.OrderViewHolder> {

        static class OrderViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.date)
            TextView dateView;

            @BindView(R.id.total_price)
            TextView totalPriceView;

            @BindView(R.id.line_item_layout)
            LinearLayout lineItemLayoutView;

            OrderViewHolder(final View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            void bind(final Order order, final LayoutInflater layoutInflater) {
                dateView.setText(order.getProcessedAt().toString());
                totalPriceView.setText(itemView.getResources().getString(R.string.customer_order_list_total_price, order.getTotalPrice(), order.getCurrency()));

                lineItemLayoutView.removeAllViews();
                if (order.getLineItems() != null) {
                    for (LineItem lineItem : order.getLineItems()) {
                        final View lineItemView = layoutInflater.inflate(R.layout.list_view_item_order_line_item, lineItemLayoutView, false);
                        ((TextView) lineItemView.findViewById(R.id.title)).setText(lineItem.getTitle());
                        ((TextView) lineItemView.findViewById(R.id.variant_title)).setText(lineItem.getVariantTitle());
                        ((TextView) lineItemView.findViewById(R.id.price)).setText("$" + lineItem.getPrice());

                        lineItemLayoutView.addView(lineItemView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }
            }
        }

        private final List<Order> orders;

        private final LayoutInflater layoutInflater;

        OrderListViewAdapter(final List<Order> orders, final LayoutInflater layoutInflater) {
            this.orders = orders;
            this.layoutInflater = layoutInflater;
        }

        @Override
        public OrderViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            return new OrderViewHolder(layoutInflater.inflate(R.layout.list_view_item_order, parent, false));
        }

        @Override
        public void onBindViewHolder(final OrderViewHolder viewHolder, final int position) {
            viewHolder.bind(orders.get(position), layoutInflater);
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }
    }
}
