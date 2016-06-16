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

import java.util.Date;
import java.util.List;

/**
 * Represents the amount that the merchant is charging a customer for shipping to the specified address
 */
public class ShippingRate {

    protected String id;

    protected String price;

    protected String title;

    @SerializedName("delivery_range")
    protected List<Date> deliveryRangeDates;

    /**
     * @return A list of one or two dates representing the possible range of delivery for this shipping rate.
     */
    public List<Date> getDeliveryRangeDates() {
        return deliveryRangeDates;
    }

    /**
     * @return The price of this shipping rate.
     */
    public String getPrice() {
        return price;
    }

    /**
     * @return The title of this shipping rate.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The unique identifier of this shipping rate.
     */
    public String getId() {
        return id;
    }

}
