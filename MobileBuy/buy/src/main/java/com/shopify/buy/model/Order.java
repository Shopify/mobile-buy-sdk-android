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

package com.shopify.buy.model;

import com.google.gson.annotations.SerializedName;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Order extends ShopifyObject {

    @SerializedName("order_number")
    protected String orderNumber;

    protected String name;

    @SerializedName("processed_at")
    protected Date processedAt;

    @SerializedName("status_url")
    protected String statusUrl;

    @SerializedName("order_status_url")
    protected String orderStatusUrl;

    protected String currency;

    @SerializedName("total_price")
    protected String totalPrice;

    protected Boolean cancelled;

    @SerializedName("cancel_reason")
    protected String cancelReason;

    @SerializedName("cancelled_at")
    protected Date cancelledAt;

    @SerializedName("fulfilled_line_items")
    protected List<LineItem> fulfilledLineItems;

    @SerializedName("unfulfilled_line_items")
    protected List<LineItem> unfulfilledLineItems;

    protected List<LineItem> lineItems;

    /**
     * No args constructor for use in serialization.
     */
    public Order() {
    }

    /**
     * @return URL for the website showing the order status.  This url will pass an authentication token for the currently logged in user. This is only available for Orders returned using {@link BuyClientImpl#getOrder(Customer, String, Callback)} or {@link BuyClientImpl#getOrders(Customer, Callback)}
     */
    public String getOrderStatusUrl() {
        return orderStatusUrl;
    }

    /**
     * @return URL for the website showing the order status. This is only available for Orders returned using {@link BuyClientImpl#completeCheckout(Checkout, Callback)}
     */
    public String getStatusUrl() {
        return statusUrl;
    }

    /**
     * @return The name of the Order.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Numerical identifier unique to the shop.
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * @return The unique identifier of the Order within Shopify.
     */
    public String getOrderId() {
        return String.valueOf(id);
    }

    /**
     * @return The date and time when the order was processed.
     */
    public Date getProcessedAt() {
        return processedAt;
    }

    /**
     * @return The currency of the Order.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return The total price of the order.
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * @return true if the Order was cancelled.
     */
    public Boolean isCancelled() {
        return cancelled;
    }

    /**
     * @return The reason the Order was cancelled.
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * @return The date the Order was cancelled.
     */
    public Date getCancelledAt() {
        return cancelledAt;
    }

    /**
     * @return A list of the fulfilled Line Items in an Order
     */
    public List<LineItem> getFulfilledLineItems() {
        return fulfilledLineItems;
    }

    /**
     * @return A List of the unfulfilled Line Items in the Order
     */
    public List<LineItem> getUnfulfilledLineItems() {
        return unfulfilledLineItems;
    }

    /**
     * @return A List of all the Line Items in the Order
     */
    public List<LineItem> getLineItems() {
        if (lineItems == null) {
            lineItems = new ArrayList<>();
            if (!CollectionUtils.isEmpty(fulfilledLineItems)) {
                lineItems.addAll(fulfilledLineItems);
            }
            if (!CollectionUtils.isEmpty(unfulfilledLineItems)) {
                lineItems.addAll(unfulfilledLineItems);
            }
        }
        return lineItems;
    }
}
