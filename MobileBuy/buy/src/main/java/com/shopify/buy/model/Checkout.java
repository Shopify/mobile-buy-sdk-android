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
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.shopify.buy.dataprovider.BuyClientUtils;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.model.internal.MarketingAttribution;
import com.shopify.buy.utils.CollectionUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The checkout object. This is the main object that you will interact with when creating orders on Shopify.
 * After making changes to your checkout object by calling any of the setter functions, make sure you call
 * {@link com.shopify.buy.dataprovider.BuyClient#updateCheckout(Checkout, Callback) updateCheckoutAddressAndEmail}.
 */
public class Checkout extends ShopifyObject {

    private static final String ATTRIBUTES_JSON_KEY = "attributes";

    private String email;

    protected String token;

    @Deprecated
    @SerializedName("order_id")
    private Long orderId;

    private Order order;

    @SerializedName("requires_shipping")
    private Boolean requiresShipping;

    @SerializedName("taxes_included")
    private Boolean taxesIncluded;

    private String currency;

    @SerializedName("subtotal_price")
    private String subtotalPrice;

    @SerializedName("total_tax")
    private String totalTax;

    @SerializedName("total_price")
    private String totalPrice;

    @SerializedName("payment_url")
    private String paymentUrl;

    @SerializedName("payment_due")
    private String paymentDue;

    @SerializedName("reservation_time")
    private Long reservationTime;

    @SerializedName("reservation_time_left")
    protected Long reservationTimeLeft;

    @SerializedName("line_items")
    private List<LineItem> lineItems;

    @SerializedName("tax_lines")
    private List<TaxLine> taxLines;

    private Discount discount;

    @SerializedName("billing_address")
    private Address billingAddress;

    @SerializedName("shipping_address")
    private Address shippingAddress;

    @SerializedName("shipping_rate")
    private ShippingRate shippingRate;

    @SerializedName("marketing_attribution")
    private MarketingAttribution marketingAttribution;

    @SerializedName("web_url")
    private String webUrl;

    @SerializedName("web_return_to_url")
    private String webReturnToUrl;

    @SerializedName("web_return_to_label")
    private String webReturnToLabel;

    final private String channel = "mobile_app";

    @SerializedName("gift_cards")
    private List<GiftCard> giftCards;

    @Deprecated
    @SerializedName("order_status_url")
    private String orderStatusUrl;

    @SerializedName("created_at")
    private Date createdAtDate;

    @SerializedName("credit_card")
    private CreditCard creditCard;

    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("privacy_policy_url")
    private String privacyPolicyUrl;

    @SerializedName("refund_policy_url")
    private String refundPolicyUrl;

    @SerializedName("terms_of_service_url")
    private String termsOfServiceUrl;

    @SerializedName("source_name")
    private String sourceName;

    @SerializedName("source_identifier")
    private String sourceIdentifier;

    private String note;

    private transient List<CheckoutAttribute> attributes;

    public Checkout() {
    }

    public Checkout(Cart cart) {
        lineItems = new ArrayList<LineItem>(cart.getLineItems());
    }

    public Checkout(LineItem lineItem) {
        lineItems = new ArrayList<>();
        lineItems.add(lineItem);
    }

    public Checkout(String token) {
        this.token = token;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public void setLineItems(Cart cart) {
        this.lineItems.clear();
        this.lineItems.addAll(cart.getLineItems());
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = new ArrayList<>(lineItems);
    }

    /**
     * @return The identifier of the source of this checkout (used  for tracking purposes). For the Mobile Buy SDK, this is set to the Mobile App Channel ID of the shop, which you can find in your shop's admin portal by navigating to Integration under Mobile App.
     */
    public String getSourceIdentifier() {
        return sourceIdentifier;
    }

    /**
     * @return The name of the source of this checkout (used for tracking purposes). For the Mobile Buy SDK, this is set to "mobile_app".
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * @return The website URL for the terms of service for the checkout.
     */
    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    /**
     * @return The website URL for the refund policy for the checkout.
     */
    public String getRefundPolicyUrl() {
        return refundPolicyUrl;
    }

    /**
     * @return The website URL for the privacy policy for the checkout.
     */
    public String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    /**
     * @return Customer ID associated with the checkout.
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @return Creation date of the checkout.
     */
    public Date getCreatedAtDate() {
        return createdAtDate;
    }

    /**
     * @return The {@link CreditCard} stored on the checkout.
     */
    public CreditCard getCreditCard() {
        return creditCard;
    }

    /**
     * @deprecated Use {@link #getOrder()}.
     *
     * @return The order status.
     */
    public String getOrderStatusUrl() {
        return orderStatusUrl;
    }

    /**
     * @return The customer's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return Unique token for the checkout.
     */
    public String getToken() {
        return token;
    }

    /**
     * @deprecated Use {@link #getOrder()}.
     *
     * @return The order id.
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * @return This value is null until the checkout is complete. Once it is completed, it will containin the {@link Order#name}, {@link Order#id}, and {@link Order#statusUrl}
     */
    public Order getOrder() {
        return order;
    }

    /**
     * @return {@code true} if checkout has been completed and there's an order with an id, {@code false} otherwise.
     */
    public boolean hasOrderId() {
        return order != null && order.getId() != null && order.getId() > 0;
    }

    /**
     * @return {@code true} if the fulfillment of this checkout requires shipping, {@code false} otherwise.
     */
    public Boolean isRequiresShipping() {
        return requiresShipping;
    }

    /**
     * @return {@code true} if taxes are included in the price, {@code false} otherwise.
     */
    public Boolean isTaxesIncluded() {
        return taxesIncluded;
    }

    /**
     * @return The three letter code (ISO 4217) for the currency used for the payment.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return The list of {@link GiftCard} objects that have been added to this checkout.
     */
    public List<GiftCard> getGiftCards() {
        return giftCards;
    }

    /***
     * @return Price of the order before shipping and taxes.
     */
    public String getSubtotalPrice() {
        return subtotalPrice;
    }

    /**
     * @return The sum of all the taxes applied to the line items in the order.
     */
    public String getTotalTax() {
        return totalTax;
    }

    /**
     * @return The sum of all the prices of all the items in the order (taxes and discounts included).
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * @return URL to the payment gateway.
     */
    public String getPaymentUrl() {
        return paymentUrl;
    }

    /**
     * @return The payment due after applying giftcards or other methods of partial payment
     */
    public String getPaymentDue() {
        return paymentDue;
    }

    /**
     * @return The reservation time on this checkout (in seconds).
     */
    public Long getReservationTime() {
        return reservationTime;
    }

    /**
     * @return Reservation time remaining on the checkout (in seconds).
     */
    public Long getReservationTimeLeft() {
        return reservationTimeLeft;
    }

    /**
     * @return The list of {@link LineItem} objects in this checkout. Note that these are different from {@link CartLineItem} objects in that they do not contain the {@link ProductVariant}.
     */
    public List<LineItem> getLineItems() {
        return lineItems;
    }

    /**
     * @return List of tax line objects on the checkout.
     */
    public List<TaxLine> getTaxLines() {
        return taxLines;
    }

    /**
     * @return The mailing address associated with the payment method.
     */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /**
     * @return The mailing address that the order will be shipped to.
     */
    public Address getShippingAddress() {
        return shippingAddress;
    }

    /**
     * @return The shipping rate chosen for the checkout.
     */
    public ShippingRate getShippingRate() {
        return shippingRate;
    }

    /**
     * @return The {@link Discount} that has been applied to the checkout, if applicable.
     */
    public Discount getDiscount() {
        return discount;
    }

    /**
     * @return URL which is used for completing checkout in browser.
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * @return An optional note attached to the order.
     */
    public String getNote() {
        return note;
    }

    /**
     * @return An optional list of {@link CheckoutAttribute} attached to the checkout.
     */
    public List<CheckoutAttribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        return attributes;
    }

    /**
     * For internal use only.
     *
     * @return The {@link MarketingAttribution} associated with the checkout.
     */
    public MarketingAttribution getMarketingAttribution() {
        return marketingAttribution;
    }

    /**
     * For internal use only.
     *
     * @param sourceIdentifier The source identifier.
     */
    public void setSourceIdentifier(String sourceIdentifier) {
        this.sourceIdentifier = sourceIdentifier;
    }

    /**
     * For internal use only.
     *
     * @param sourceName The source name.
     */
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    /**
     * @param customerId The customer's id.
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @param email The customer's email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param billingAddress The mailing address associated with the payment method.
     */
    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     * The default reservation time on a checkout is 300 seconds (5 minutes).
     * Setting the reservation time to 0 and updating the checkout (via {@link com.shopify.buy.dataprovider.BuyClient#updateCheckout(Checkout, Callback) updateCheckoutAddressAndEmail(checkout, callback)})
     * will release the inventory reserved by this checkout. This can also be done by calling {@link com.shopify.buy.dataprovider.BuyClient#removeProductReservationsFromCheckout(Checkout, Callback) removeProductReservationsFromCheckout(checkout, callback)}.
     *
     * @param reservationTime The reservation time on this checkout (in seconds).
     */
    public void setReservationTime(long reservationTime) {
        this.reservationTime = reservationTime;
    }

    /**
     * @param shippingAddress The mailing address that the order will be shipped to.
     */
    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * @param shippingRate The shipping rate chosen for the checkout.
     */
    public void setShippingRate(ShippingRate shippingRate) {
        this.shippingRate = shippingRate;
    }

    /**
     * @param paymentDue Amount of payment due on the checkout.
     */
    public void setPaymentDue(String paymentDue) {
        this.paymentDue = paymentDue;
    }

    /**
     * @param code The code for the {@link Discount} to apply to this checkout.
     */
    public void setDiscountCode(String code) {
        discount = new Discount(code);
    }

    /**
     * For internal use only.
     *
     * @param marketingAttribution A {@link MarketingAttribution}.
     */
    public void setMarketingAttribution(MarketingAttribution marketingAttribution) {
        this.marketingAttribution = marketingAttribution;
    }

    /**
     * @param webReturnToUrl The URL Scheme of the host app. Used to return to the app from the web checkout.
     */
    public void setWebReturnToUrl(String webReturnToUrl) {
        this.webReturnToUrl = webReturnToUrl;
    }

    /**
     * @param webReturnToLabel The button title that will appear after checkout to return to the host app. Defaults to "Return to 'application'", where 'application' is the `applicationName` set on the BuyClient.
     */
    public void setWebReturnToLabel(String webReturnToLabel) {
        this.webReturnToLabel = webReturnToLabel;
    }

    /**
     * @param note An optional note attached to the order.
     */
    public void setNote(String note) {
        this.note = note;
    }


    /**
     * Set the token for the Checkout.
     *
     * @param token The token to set.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * For internal use only. To apply a gift card to your checkout, use {@link com.shopify.buy.dataprovider.BuyClient#applyGiftCard(String, Checkout, Callback) applyGiftCard(giftCardCode, checkout, callback)}.
     * @param giftCard The gift card to add.
     */
    public void addGiftCard(GiftCard giftCard) {
        if (giftCards == null) {
            giftCards = new ArrayList<>();
        }

        if (giftCard != null) {
            giftCards.add(giftCard);
        }
    }

    /**
     * @param giftCard A {@link GiftCard} to remove from this checkout.
     */
    public void removeGiftCard(GiftCard giftCard) {
        if (giftCards != null && giftCard != null) {
            giftCards.remove(giftCard);
        }
    }

    /**
     * Creates an identical copy of the Checkout for use in updates.
     *
     * @return A checkout suitable for sending in an update.
     */
    public Checkout copy() {
        Checkout copy = Checkout.fromJson(this.toJsonString());
        return copy;
    }

    /**
     * @return The total number of product variants in the cart (the sum of quantities across all line items).
     */
    public Integer getTotalQuantity() {
        if (CollectionUtils.isEmpty(lineItems)) {
            return 0;
        }

        int quantity = 0;
        for (LineItem lineItem : lineItems) {
            quantity += lineItem.getQuantity();
        }
        return quantity;
    }

    /**
     * @param json The json input.
     * @return A checkout object created using the values in the JSON string.
     *
     */
    public static Checkout fromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        Gson gson = BuyClientUtils.createDefaultGson(Checkout.class);

        JsonObject checkoutElement = gson.fromJson(json, JsonElement.class).getAsJsonObject();

        Checkout checkout = gson.fromJson(checkoutElement, Checkout.class);

        JsonElement attributeElement = checkoutElement.get(ATTRIBUTES_JSON_KEY);

        if (attributeElement != null) {
            List<CheckoutAttribute> attributes;

            // The attributes are an array of CheckoutAttributes when received from the server, and are flattened and serialized as a hash map
            // when serializing for the server, or internally.  We have to check to see which case it is and deserialize appropriately.
            if (attributeElement.isJsonArray()) {
                attributes = gson.fromJson(attributeElement, new TypeToken<List<CheckoutAttribute>>() {
                }.getType());
            } else {
                // We serialize the CheckoutAttributes to a hash map internally, and for sending to the server
                HashMap<String, String> attributesHashMap = gson.fromJson(attributeElement, HashMap.class);
                attributes = new ArrayList<>();

                for (Map.Entry<String, String> pair : attributesHashMap.entrySet()) {
                    attributes.add(new CheckoutAttribute(pair.getKey(), pair.getValue()));
                }
            }
            checkout.attributes = attributes;
        }

        return checkout;
    }


    public static class CheckoutSerializer implements JsonSerializer<Checkout> {

        @Override
        public JsonElement serialize(final Checkout checkout, final Type typeOfSrc, final JsonSerializationContext context) {
            Gson gson = BuyClientUtils.createDefaultGson(Checkout.class);

            JsonObject checkoutObject = gson.toJsonTree(checkout).getAsJsonObject();

            // Replace the CheckoutAttributes List with a hash map which is the input expected by the server
            if (checkout.getAttributes() != null && checkout.getAttributes().size() > 0) {
                HashMap<String, String> attributesHashMap = new HashMap<>();
                for (CheckoutAttribute attribute : checkout.attributes) {
                    attributesHashMap.put(attribute.getName(), attribute.getValue());
                }

                JsonElement attributesElement = context.serialize(attributesHashMap);
                checkoutObject.remove(ATTRIBUTES_JSON_KEY);
                checkoutObject.add(ATTRIBUTES_JSON_KEY, attributesElement);
            }

            return checkoutObject;
        }
    }

    public static class CheckoutDeserializer implements JsonDeserializer<Checkout> {

        @Override
        public Checkout deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return fromJson(json.toString());
        }
    }

}
