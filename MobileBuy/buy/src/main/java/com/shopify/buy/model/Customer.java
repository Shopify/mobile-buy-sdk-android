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

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.shopify.buy.dataprovider.BuyClientUtils;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Customer extends ShopifyObject {

    protected String email;

    @SerializedName("accepts_marketing")
    protected Boolean acceptsMarketing;

    @SerializedName("created_at")
    protected Date createdAtDate;

    @SerializedName("updated_at")
    protected Date updatedAtDate;

    @SerializedName("first_name")
    protected String firstName;

    @SerializedName("last_name")
    protected String lastName;

    @SerializedName("orders_count")
    protected Long ordersCount;

    protected String state;

    @SerializedName("total_spent")
    protected String totalSpent;

    protected String note;

    @SerializedName("verified_email")
    protected Boolean verifiedEmail;

    @SerializedName("multipass_identifier")
    protected String multipassIdentifier;

    @SerializedName("tax_exempt")
    protected Boolean taxExempt;

    protected String tags;
    protected Set<String> tagSet;

    @SerializedName("last_order_id")
    protected Long lastOrderId;

    @SerializedName("last_order_name")
    protected String lastOrderName;

    protected List<Address> addresses;

    @SerializedName("default_address")
    protected Address defaultAddress;

    @Override
    public Long getId() {
        return super.getId();
    }

    /**
     * @return The email for this customer.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return {code true} if this customer accepts marketing.
     */
    public boolean acceptsMarketing() {
        return acceptsMarketing != null && acceptsMarketing;
    }

    /**
     * @return The date this product was created.
     */
    public Date getCreatedAtDate() {
        return createdAtDate;
    }

    /**
     * @return The date this product was last updated.
     */
    public Date getUpdatedAtDate() {
        return updatedAtDate;
    }

    /**
     * @return The first name for this customer.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return The last name for this customer.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return The number of orders this customer has.
     */
    public long getOrdersCount() {
        return ordersCount;
    }

    /**
     * @return The current state of this customer.
     */
    public String getState() {
        return state;
    }

    /**
     * @return The total amount of money that the customer has spent at the shop.
     */
    public String getTotalSpent() {
        return totalSpent;
    }

    /**
     * @return A note about the customer.
     */
    public String getNote() {
        return note;
    }

    /**
     * @return {code true} if the customer email address has been verified.
     */
    public boolean isVerifiedEmail() {
        return verifiedEmail != null && verifiedEmail;
    }

    /**
     * @return The customer's identifier used with Multipass login.
     */
    public String getMultipassIdenfier() {
        return multipassIdentifier;
    }

    /**
     * @return Indicates whether the customer should be charged taxes when placing orders. Valid values are {@code true} and {@code false}.
     **/
    public boolean isTaxExempt() {
        return taxExempt != null && taxExempt;
    }

    /**
     * @return A list of additional categorizations that a customer can be tagged with.
     */
    public Set<String> getTags() {
        return tagSet;
    }

    /**
     * @return The id of the customer's last order.
     */
    public Long getLastOrderId() {
        return lastOrderId;
    }

    /**
     * @return The name of the customer's last order. This is directly related to the Order's name field.
     */
    public String getLastOrderName() {
        return lastOrderName;
    }

    /**
     * @return A list of addresses for the customer.
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * @return The default address for the customer.
     */
    public Address getDefaultAddress() {
        return defaultAddress;
    }

    public void setAcceptsMarketing(boolean acceptsMarketing) {
        this.acceptsMarketing = acceptsMarketing;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    public void setMultipassIdentifier(String multipassIdentifier) {
        this.multipassIdentifier = multipassIdentifier;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTags(Set<String> tags) {
        tagSet = tags;
        this.tags = TextUtils.join(",", tags);
    }

    public static class CustomerDeserializer implements JsonDeserializer<Customer> {
        @Override
        public Customer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return fromJson(json.toString());
        }
    }

    /**
     * A Customer object created using the values in the JSON string.
     *
     * @param json The input json.
     * @return A {@link Customer}
     */
    public static Customer fromJson(String json) {
        Gson gson = BuyClientUtils.createDefaultGson(Customer.class);
        Customer customer = gson.fromJson(json, Customer.class);

        // Create the tagSet.
        customer.tagSet = new HashSet<>();

        // Populate the tagSet from the comma separated list.
        if (!TextUtils.isEmpty(customer.tags)) {
            for (String tag : customer.tags.split(",")) {
                String myTag = tag.trim();
                customer.tagSet.add(myTag);
            }
        }

        return customer;
    }

}
