// Generated from graphql_java_gen gem

package com.shopify.buy3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shopify.graphql.support.AbstractResponse;
import com.shopify.graphql.support.Arguments;
import com.shopify.graphql.support.Error;
import com.shopify.graphql.support.Query;
import com.shopify.graphql.support.SchemaViolationError;
import com.shopify.graphql.support.TopLevelResponse;

import com.shopify.graphql.support.ID;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Storefront {
    public static QueryRootQuery query(QueryRootQueryDefinition queryDef) {
        StringBuilder queryString = new StringBuilder("{");
        QueryRootQuery query = new QueryRootQuery(queryString);
        queryDef.define(query);
        queryString.append('}');
        return query;
    }

    public static class QueryResponse {
        private TopLevelResponse response;
        private QueryRoot data;

        public QueryResponse(TopLevelResponse response) throws SchemaViolationError {
            this.response = response;
            this.data = response.getData() != null ? new QueryRoot(response.getData()) : null;
        }

        public QueryRoot getData() {
            return data;
        }

        public List<Error> getErrors() {
            return response.getErrors();
        }

        public String toJson() {
            return new Gson().toJson(response);
        }

        public String prettyPrintJson() {
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(response);
        }

        public static QueryResponse fromJson(String json) throws SchemaViolationError {
            final TopLevelResponse response = new Gson().fromJson(json, TopLevelResponse.class);
            return new QueryResponse(response);
        }
    }

    public static MutationQuery mutation(MutationQueryDefinition queryDef) {
        StringBuilder queryString = new StringBuilder("mutation{");
        MutationQuery query = new MutationQuery(queryString);
        queryDef.define(query);
        queryString.append('}');
        return query;
    }

    public static class MutationResponse {
        private TopLevelResponse response;
        private Mutation data;

        public MutationResponse(TopLevelResponse response) throws SchemaViolationError {
            this.response = response;
            this.data = response.getData() != null ? new Mutation(response.getData()) : null;
        }

        public Mutation getData() {
            return data;
        }

        public List<Error> getErrors() {
            return response.getErrors();
        }

        public String toJson() {
            return new Gson().toJson(response);
        }

        public String prettyPrintJson() {
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(response);
        }

        public static MutationResponse fromJson(String json) throws SchemaViolationError {
            final TopLevelResponse response = new Gson().fromJson(json, TopLevelResponse.class);
            return new MutationResponse(response);
        }
    }

    public interface AppliedGiftCardQueryDefinition {
        void define(AppliedGiftCardQuery _queryBuilder);
    }

    /**
    * Details about the gift card used on the checkout.
    */
    public static class AppliedGiftCardQuery extends Query<AppliedGiftCardQuery> {
        AppliedGiftCardQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        /**
        * The amount that was used taken from the Gift Card by applying it.
        */
        public AppliedGiftCardQuery amountUsed() {
            startField("amountUsed");

            return this;
        }

        /**
        * The amount left on the Gift Card.
        */
        public AppliedGiftCardQuery balance() {
            startField("balance");

            return this;
        }

        /**
        * The last characters of the Gift Card code
        */
        public AppliedGiftCardQuery lastCharacters() {
            startField("lastCharacters");

            return this;
        }
    }

    /**
    * Details about the gift card used on the checkout.
    */
    public static class AppliedGiftCard extends AbstractResponse<AppliedGiftCard> implements Node {
        public AppliedGiftCard() {
        }

        public AppliedGiftCard(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "amountUsed": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "balance": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "lastCharacters": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public AppliedGiftCard(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "AppliedGiftCard";
        }

        /**
        * The amount that was used taken from the Gift Card by applying it.
        */

        public BigDecimal getAmountUsed() {
            return (BigDecimal) get("amountUsed");
        }

        public AppliedGiftCard setAmountUsed(BigDecimal arg) {
            optimisticData.put(getKey("amountUsed"), arg);
            return this;
        }

        /**
        * The amount left on the Gift Card.
        */

        public BigDecimal getBalance() {
            return (BigDecimal) get("balance");
        }

        public AppliedGiftCard setBalance(BigDecimal arg) {
            optimisticData.put(getKey("balance"), arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * The last characters of the Gift Card code
        */

        public String getLastCharacters() {
            return (String) get("lastCharacters");
        }

        public AppliedGiftCard setLastCharacters(String arg) {
            optimisticData.put(getKey("lastCharacters"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "amountUsed": return false;

                case "balance": return false;

                case "id": return false;

                case "lastCharacters": return false;

                default: return false;
            }
        }
    }

    public interface AttributeQueryDefinition {
        void define(AttributeQuery _queryBuilder);
    }

    /**
    * Represents a generic custom attribute.
    */
    public static class AttributeQuery extends Query<AttributeQuery> {
        AttributeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * Key or name of the attribute.
        */
        public AttributeQuery key() {
            startField("key");

            return this;
        }

        /**
        * Value of the attribute.
        */
        public AttributeQuery value() {
            startField("value");

            return this;
        }
    }

    /**
    * Represents a generic custom attribute.
    */
    public static class Attribute extends AbstractResponse<Attribute> {
        public Attribute() {
        }

        public Attribute(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "key": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "value": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "Attribute";
        }

        /**
        * Key or name of the attribute.
        */

        public String getKey() {
            return (String) get("key");
        }

        public Attribute setKey(String arg) {
            optimisticData.put(getKey("key"), arg);
            return this;
        }

        /**
        * Value of the attribute.
        */

        public String getValue() {
            return (String) get("value");
        }

        public Attribute setValue(String arg) {
            optimisticData.put(getKey("value"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "key": return false;

                case "value": return false;

                default: return false;
            }
        }
    }

    public static class AttributeInput implements Serializable {
        private String key;

        private String value;

        public AttributeInput(String key, String value) {
            this.key = key;

            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public AttributeInput setKey(String key) {
            this.key = key;
            return this;
        }

        public String getValue() {
            return value;
        }

        public AttributeInput setValue(String value) {
            this.value = value;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("key:");
            Query.appendQuotedString(_queryBuilder, key.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("value:");
            Query.appendQuotedString(_queryBuilder, value.toString());

            _queryBuilder.append('}');
        }
    }

    public interface AvailableShippingRatesQueryDefinition {
        void define(AvailableShippingRatesQuery _queryBuilder);
    }

    /**
    * A collection of available shipping rates for a checkout.
    */
    public static class AvailableShippingRatesQuery extends Query<AvailableShippingRatesQuery> {
        AvailableShippingRatesQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * Whether or not the shipping rates are ready. The `shippingRates` field is `null` when this value is
        * `false`. This field should be polled until its value becomes `true`. 
        */
        public AvailableShippingRatesQuery ready() {
            startField("ready");

            return this;
        }

        /**
        * The fetched shipping rates. `null` until the `ready` field is `true`.
        */
        public AvailableShippingRatesQuery shippingRates(ShippingRateQueryDefinition queryDef) {
            startField("shippingRates");

            _queryBuilder.append('{');
            queryDef.define(new ShippingRateQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    /**
    * A collection of available shipping rates for a checkout.
    */
    public static class AvailableShippingRates extends AbstractResponse<AvailableShippingRates> {
        public AvailableShippingRates() {
        }

        public AvailableShippingRates(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "ready": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "shippingRates": {
                        List<ShippingRate> optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            List<ShippingRate> list1 = new ArrayList<>();
                            for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                list1.add(new ShippingRate(jsonAsObject(element1, key)));
                            }

                            optional1 = list1;
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "AvailableShippingRates";
        }

        /**
        * Whether or not the shipping rates are ready. The `shippingRates` field is `null` when this value is
        * `false`. This field should be polled until its value becomes `true`. 
        */

        public Boolean getReady() {
            return (Boolean) get("ready");
        }

        public AvailableShippingRates setReady(Boolean arg) {
            optimisticData.put(getKey("ready"), arg);
            return this;
        }

        /**
        * The fetched shipping rates. `null` until the `ready` field is `true`.
        */

        public List<ShippingRate> getShippingRates() {
            return (List<ShippingRate>) get("shippingRates");
        }

        public AvailableShippingRates setShippingRates(List<ShippingRate> arg) {
            optimisticData.put(getKey("shippingRates"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "ready": return false;

                case "shippingRates": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutQueryDefinition {
        void define(CheckoutQuery _queryBuilder);
    }

    /**
    * A container for all the information required to checkout items and pay.
    */
    public static class CheckoutQuery extends Query<CheckoutQuery> {
        CheckoutQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public CheckoutQuery appliedGiftCards(AppliedGiftCardQueryDefinition queryDef) {
            startField("appliedGiftCards");

            _queryBuilder.append('{');
            queryDef.define(new AppliedGiftCardQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The available shipping rates for this Checkout. Should only be used when checkout `requiresShipping`
        * is `true` and the shipping address is valid. 
        */
        public CheckoutQuery availableShippingRates(AvailableShippingRatesQueryDefinition queryDef) {
            startField("availableShippingRates");

            _queryBuilder.append('{');
            queryDef.define(new AvailableShippingRatesQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The date and time when the checkout was completed.
        */
        public CheckoutQuery completedAt() {
            startField("completedAt");

            return this;
        }

        /**
        * The date and time when the checkout was created.
        */
        public CheckoutQuery createdAt() {
            startField("createdAt");

            return this;
        }

        /**
        * The currency code for the Checkout.
        */
        public CheckoutQuery currencyCode() {
            startField("currencyCode");

            return this;
        }

        /**
        * A list of extra information that is added to the checkout.
        */
        public CheckoutQuery customAttributes(AttributeQueryDefinition queryDef) {
            startField("customAttributes");

            _queryBuilder.append('{');
            queryDef.define(new AttributeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The customer associated with the checkout.
        */
        public CheckoutQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The email attached to this checkout.
        */
        public CheckoutQuery email() {
            startField("email");

            return this;
        }

        public class LineItemsArguments extends Arguments {
            LineItemsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, false);
            }

            public LineItemsArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            public LineItemsArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface LineItemsArgumentsDefinition {
            void define(LineItemsArguments args);
        }

        /**
        * A list of line item objects, each one containing information about an item in the checkout.
        */
        public CheckoutQuery lineItems(int first, CheckoutLineItemConnectionQueryDefinition queryDef) {
            return lineItems(first, args -> {}, queryDef);
        }

        /**
        * A list of line item objects, each one containing information about an item in the checkout.
        */
        public CheckoutQuery lineItems(int first, LineItemsArgumentsDefinition argsDef, CheckoutLineItemConnectionQueryDefinition queryDef) {
            startField("lineItems");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new LineItemsArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutLineItemConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutQuery note() {
            startField("note");

            return this;
        }

        /**
        * The resulting order from a paid checkout.
        */
        public CheckoutQuery order(OrderQueryDefinition queryDef) {
            startField("order");

            _queryBuilder.append('{');
            queryDef.define(new OrderQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The Order Status Page for this Checkout, null when checkout is not completed.
        */
        public CheckoutQuery orderStatusUrl() {
            startField("orderStatusUrl");

            return this;
        }

        /**
        * The amount left to be paid. This is equal to the cost of the line items, taxes and shipping minus
        * discounts and gift cards.
        */
        public CheckoutQuery paymentDue() {
            startField("paymentDue");

            return this;
        }

        /**
        * Whether or not the Checkout is ready and can be completed. Checkouts may have asynchronous
        * operations that can take time to finish. If you want to complete a checkout or ensure all the fields
        * are populated and up to date, polling is required until the value is true. 
        */
        public CheckoutQuery ready() {
            startField("ready");

            return this;
        }

        /**
        * States whether or not the fulfillment requires shipping.
        */
        public CheckoutQuery requiresShipping() {
            startField("requiresShipping");

            return this;
        }

        /**
        * The shipping address to where the line items will be shipped.
        */
        public CheckoutQuery shippingAddress(MailingAddressQueryDefinition queryDef) {
            startField("shippingAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Once a shipping rate is selected by the customer it is transitioned to a `shipping_line` object.
        */
        public CheckoutQuery shippingLine(ShippingRateQueryDefinition queryDef) {
            startField("shippingLine");

            _queryBuilder.append('{');
            queryDef.define(new ShippingRateQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Price of the checkout before shipping, taxes, and discounts.
        */
        public CheckoutQuery subtotalPrice() {
            startField("subtotalPrice");

            return this;
        }

        /**
        * Specifies if the Checkout is tax exempt.
        */
        public CheckoutQuery taxExempt() {
            startField("taxExempt");

            return this;
        }

        /**
        * Specifies if taxes are included in the line item and shipping line prices.
        */
        public CheckoutQuery taxesIncluded() {
            startField("taxesIncluded");

            return this;
        }

        /**
        * The sum of all the prices of all the items in the checkout, taxes and discounts included.
        */
        public CheckoutQuery totalPrice() {
            startField("totalPrice");

            return this;
        }

        /**
        * The sum of all the taxes applied to the line items and shipping lines in the checkout.
        */
        public CheckoutQuery totalTax() {
            startField("totalTax");

            return this;
        }

        /**
        * The date and time when the checkout was last updated.
        */
        public CheckoutQuery updatedAt() {
            startField("updatedAt");

            return this;
        }

        /**
        * The url pointing to the checkout accessible from the web.
        */
        public CheckoutQuery webUrl() {
            startField("webUrl");

            return this;
        }
    }

    /**
    * A container for all the information required to checkout items and pay.
    */
    public static class Checkout extends AbstractResponse<Checkout> implements Node {
        public Checkout() {
        }

        public Checkout(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "appliedGiftCards": {
                        List<AppliedGiftCard> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new AppliedGiftCard(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "availableShippingRates": {
                        AvailableShippingRates optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new AvailableShippingRates(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "completedAt": {
                        DateTime optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = Utils.parseDateTime(jsonAsString(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "createdAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "currencyCode": {
                        responseData.put(key, CurrencyCode.fromGraphQl(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "customAttributes": {
                        List<Attribute> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new Attribute(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "email": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "lineItems": {
                        responseData.put(key, new CheckoutLineItemConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "note": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "order": {
                        Order optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Order(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "orderStatusUrl": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "paymentDue": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "ready": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "requiresShipping": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "shippingAddress": {
                        MailingAddress optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new MailingAddress(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "shippingLine": {
                        ShippingRate optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ShippingRate(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "subtotalPrice": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "taxExempt": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "taxesIncluded": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "totalPrice": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "totalTax": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "updatedAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "webUrl": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public Checkout(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "Checkout";
        }

        public List<AppliedGiftCard> getAppliedGiftCards() {
            return (List<AppliedGiftCard>) get("appliedGiftCards");
        }

        public Checkout setAppliedGiftCards(List<AppliedGiftCard> arg) {
            optimisticData.put(getKey("appliedGiftCards"), arg);
            return this;
        }

        /**
        * The available shipping rates for this Checkout. Should only be used when checkout `requiresShipping`
        * is `true` and the shipping address is valid. 
        */

        public AvailableShippingRates getAvailableShippingRates() {
            return (AvailableShippingRates) get("availableShippingRates");
        }

        public Checkout setAvailableShippingRates(AvailableShippingRates arg) {
            optimisticData.put(getKey("availableShippingRates"), arg);
            return this;
        }

        /**
        * The date and time when the checkout was completed.
        */

        public DateTime getCompletedAt() {
            return (DateTime) get("completedAt");
        }

        public Checkout setCompletedAt(DateTime arg) {
            optimisticData.put(getKey("completedAt"), arg);
            return this;
        }

        /**
        * The date and time when the checkout was created.
        */

        public DateTime getCreatedAt() {
            return (DateTime) get("createdAt");
        }

        public Checkout setCreatedAt(DateTime arg) {
            optimisticData.put(getKey("createdAt"), arg);
            return this;
        }

        /**
        * The currency code for the Checkout.
        */

        public CurrencyCode getCurrencyCode() {
            return (CurrencyCode) get("currencyCode");
        }

        public Checkout setCurrencyCode(CurrencyCode arg) {
            optimisticData.put(getKey("currencyCode"), arg);
            return this;
        }

        /**
        * A list of extra information that is added to the checkout.
        */

        public List<Attribute> getCustomAttributes() {
            return (List<Attribute>) get("customAttributes");
        }

        public Checkout setCustomAttributes(List<Attribute> arg) {
            optimisticData.put(getKey("customAttributes"), arg);
            return this;
        }

        /**
        * The customer associated with the checkout.
        */

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public Checkout setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        /**
        * The email attached to this checkout.
        */

        public String getEmail() {
            return (String) get("email");
        }

        public Checkout setEmail(String arg) {
            optimisticData.put(getKey("email"), arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * A list of line item objects, each one containing information about an item in the checkout.
        */

        public CheckoutLineItemConnection getLineItems() {
            return (CheckoutLineItemConnection) get("lineItems");
        }

        public Checkout setLineItems(CheckoutLineItemConnection arg) {
            optimisticData.put(getKey("lineItems"), arg);
            return this;
        }

        public String getNote() {
            return (String) get("note");
        }

        public Checkout setNote(String arg) {
            optimisticData.put(getKey("note"), arg);
            return this;
        }

        /**
        * The resulting order from a paid checkout.
        */

        public Order getOrder() {
            return (Order) get("order");
        }

        public Checkout setOrder(Order arg) {
            optimisticData.put(getKey("order"), arg);
            return this;
        }

        /**
        * The Order Status Page for this Checkout, null when checkout is not completed.
        */

        public String getOrderStatusUrl() {
            return (String) get("orderStatusUrl");
        }

        public Checkout setOrderStatusUrl(String arg) {
            optimisticData.put(getKey("orderStatusUrl"), arg);
            return this;
        }

        /**
        * The amount left to be paid. This is equal to the cost of the line items, taxes and shipping minus
        * discounts and gift cards.
        */

        public BigDecimal getPaymentDue() {
            return (BigDecimal) get("paymentDue");
        }

        public Checkout setPaymentDue(BigDecimal arg) {
            optimisticData.put(getKey("paymentDue"), arg);
            return this;
        }

        /**
        * Whether or not the Checkout is ready and can be completed. Checkouts may have asynchronous
        * operations that can take time to finish. If you want to complete a checkout or ensure all the fields
        * are populated and up to date, polling is required until the value is true. 
        */

        public Boolean getReady() {
            return (Boolean) get("ready");
        }

        public Checkout setReady(Boolean arg) {
            optimisticData.put(getKey("ready"), arg);
            return this;
        }

        /**
        * States whether or not the fulfillment requires shipping.
        */

        public Boolean getRequiresShipping() {
            return (Boolean) get("requiresShipping");
        }

        public Checkout setRequiresShipping(Boolean arg) {
            optimisticData.put(getKey("requiresShipping"), arg);
            return this;
        }

        /**
        * The shipping address to where the line items will be shipped.
        */

        public MailingAddress getShippingAddress() {
            return (MailingAddress) get("shippingAddress");
        }

        public Checkout setShippingAddress(MailingAddress arg) {
            optimisticData.put(getKey("shippingAddress"), arg);
            return this;
        }

        /**
        * Once a shipping rate is selected by the customer it is transitioned to a `shipping_line` object.
        */

        public ShippingRate getShippingLine() {
            return (ShippingRate) get("shippingLine");
        }

        public Checkout setShippingLine(ShippingRate arg) {
            optimisticData.put(getKey("shippingLine"), arg);
            return this;
        }

        /**
        * Price of the checkout before shipping, taxes, and discounts.
        */

        public BigDecimal getSubtotalPrice() {
            return (BigDecimal) get("subtotalPrice");
        }

        public Checkout setSubtotalPrice(BigDecimal arg) {
            optimisticData.put(getKey("subtotalPrice"), arg);
            return this;
        }

        /**
        * Specifies if the Checkout is tax exempt.
        */

        public Boolean getTaxExempt() {
            return (Boolean) get("taxExempt");
        }

        public Checkout setTaxExempt(Boolean arg) {
            optimisticData.put(getKey("taxExempt"), arg);
            return this;
        }

        /**
        * Specifies if taxes are included in the line item and shipping line prices.
        */

        public Boolean getTaxesIncluded() {
            return (Boolean) get("taxesIncluded");
        }

        public Checkout setTaxesIncluded(Boolean arg) {
            optimisticData.put(getKey("taxesIncluded"), arg);
            return this;
        }

        /**
        * The sum of all the prices of all the items in the checkout, taxes and discounts included.
        */

        public BigDecimal getTotalPrice() {
            return (BigDecimal) get("totalPrice");
        }

        public Checkout setTotalPrice(BigDecimal arg) {
            optimisticData.put(getKey("totalPrice"), arg);
            return this;
        }

        /**
        * The sum of all the taxes applied to the line items and shipping lines in the checkout.
        */

        public BigDecimal getTotalTax() {
            return (BigDecimal) get("totalTax");
        }

        public Checkout setTotalTax(BigDecimal arg) {
            optimisticData.put(getKey("totalTax"), arg);
            return this;
        }

        /**
        * The date and time when the checkout was last updated.
        */

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Checkout setUpdatedAt(DateTime arg) {
            optimisticData.put(getKey("updatedAt"), arg);
            return this;
        }

        /**
        * The url pointing to the checkout accessible from the web.
        */

        public String getWebUrl() {
            return (String) get("webUrl");
        }

        public Checkout setWebUrl(String arg) {
            optimisticData.put(getKey("webUrl"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "appliedGiftCards": return true;

                case "availableShippingRates": return true;

                case "completedAt": return false;

                case "createdAt": return false;

                case "currencyCode": return false;

                case "customAttributes": return true;

                case "customer": return true;

                case "email": return false;

                case "id": return false;

                case "lineItems": return true;

                case "note": return false;

                case "order": return true;

                case "orderStatusUrl": return false;

                case "paymentDue": return false;

                case "ready": return false;

                case "requiresShipping": return false;

                case "shippingAddress": return true;

                case "shippingLine": return true;

                case "subtotalPrice": return false;

                case "taxExempt": return false;

                case "taxesIncluded": return false;

                case "totalPrice": return false;

                case "totalTax": return false;

                case "updatedAt": return false;

                case "webUrl": return false;

                default: return false;
            }
        }
    }

    public static class CheckoutAttributesUpdateInput implements Serializable {
        private String note;

        private List<AttributeInput> customAttributes;

        private Boolean allowPartialAddresses;

        public String getNote() {
            return note;
        }

        public CheckoutAttributesUpdateInput setNote(String note) {
            this.note = note;
            return this;
        }

        public List<AttributeInput> getCustomAttributes() {
            return customAttributes;
        }

        public CheckoutAttributesUpdateInput setCustomAttributes(List<AttributeInput> customAttributes) {
            this.customAttributes = customAttributes;
            return this;
        }

        public Boolean getAllowPartialAddresses() {
            return allowPartialAddresses;
        }

        public CheckoutAttributesUpdateInput setAllowPartialAddresses(Boolean allowPartialAddresses) {
            this.allowPartialAddresses = allowPartialAddresses;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            if (note != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("note:");
                Query.appendQuotedString(_queryBuilder, note.toString());
            }

            if (customAttributes != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("customAttributes:");
                _queryBuilder.append('[');

                String listSeperator1 = "";
                for (AttributeInput item1 : customAttributes) {
                    _queryBuilder.append(listSeperator1);
                    listSeperator1 = ",";
                    item1.appendTo(_queryBuilder);
                }
                _queryBuilder.append(']');
            }

            if (allowPartialAddresses != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("allowPartialAddresses:");
                _queryBuilder.append(allowPartialAddresses);
            }

            _queryBuilder.append('}');
        }
    }

    public interface CheckoutAttributesUpdatePayloadQueryDefinition {
        void define(CheckoutAttributesUpdatePayloadQuery _queryBuilder);
    }

    public static class CheckoutAttributesUpdatePayloadQuery extends Query<CheckoutAttributesUpdatePayloadQuery> {
        CheckoutAttributesUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutAttributesUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutAttributesUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutAttributesUpdatePayload extends AbstractResponse<CheckoutAttributesUpdatePayload> {
        public CheckoutAttributesUpdatePayload() {
        }

        public CheckoutAttributesUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutAttributesUpdatePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutAttributesUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutAttributesUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutCompleteFreePayloadQueryDefinition {
        void define(CheckoutCompleteFreePayloadQuery _queryBuilder);
    }

    public static class CheckoutCompleteFreePayloadQuery extends Query<CheckoutCompleteFreePayloadQuery> {
        CheckoutCompleteFreePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutCompleteFreePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutCompleteFreePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutCompleteFreePayload extends AbstractResponse<CheckoutCompleteFreePayload> {
        public CheckoutCompleteFreePayload() {
        }

        public CheckoutCompleteFreePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutCompleteFreePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCompleteFreePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCompleteFreePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutCompleteWithCreditCardPayloadQueryDefinition {
        void define(CheckoutCompleteWithCreditCardPayloadQuery _queryBuilder);
    }

    public static class CheckoutCompleteWithCreditCardPayloadQuery extends Query<CheckoutCompleteWithCreditCardPayloadQuery> {
        CheckoutCompleteWithCreditCardPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The checkout on which the payment was applied.
        */
        public CheckoutCompleteWithCreditCardPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * A representation of the attempted payment.
        */
        public CheckoutCompleteWithCreditCardPayloadQuery payment(PaymentQueryDefinition queryDef) {
            startField("payment");

            _queryBuilder.append('{');
            queryDef.define(new PaymentQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutCompleteWithCreditCardPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutCompleteWithCreditCardPayload extends AbstractResponse<CheckoutCompleteWithCreditCardPayload> {
        public CheckoutCompleteWithCreditCardPayload() {
        }

        public CheckoutCompleteWithCreditCardPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "payment": {
                        Payment optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Payment(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutCompleteWithCreditCardPayload";
        }

        /**
        * The checkout on which the payment was applied.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCompleteWithCreditCardPayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * A representation of the attempted payment.
        */

        public Payment getPayment() {
            return (Payment) get("payment");
        }

        public CheckoutCompleteWithCreditCardPayload setPayment(Payment arg) {
            optimisticData.put(getKey("payment"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCompleteWithCreditCardPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "payment": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutCompleteWithTokenizedPaymentPayloadQueryDefinition {
        void define(CheckoutCompleteWithTokenizedPaymentPayloadQuery _queryBuilder);
    }

    public static class CheckoutCompleteWithTokenizedPaymentPayloadQuery extends Query<CheckoutCompleteWithTokenizedPaymentPayloadQuery> {
        CheckoutCompleteWithTokenizedPaymentPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The checkout on which the payment was applied.
        */
        public CheckoutCompleteWithTokenizedPaymentPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * A representation of the attempted payment.
        */
        public CheckoutCompleteWithTokenizedPaymentPayloadQuery payment(PaymentQueryDefinition queryDef) {
            startField("payment");

            _queryBuilder.append('{');
            queryDef.define(new PaymentQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutCompleteWithTokenizedPaymentPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutCompleteWithTokenizedPaymentPayload extends AbstractResponse<CheckoutCompleteWithTokenizedPaymentPayload> {
        public CheckoutCompleteWithTokenizedPaymentPayload() {
        }

        public CheckoutCompleteWithTokenizedPaymentPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "payment": {
                        Payment optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Payment(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutCompleteWithTokenizedPaymentPayload";
        }

        /**
        * The checkout on which the payment was applied.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCompleteWithTokenizedPaymentPayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * A representation of the attempted payment.
        */

        public Payment getPayment() {
            return (Payment) get("payment");
        }

        public CheckoutCompleteWithTokenizedPaymentPayload setPayment(Payment arg) {
            optimisticData.put(getKey("payment"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCompleteWithTokenizedPaymentPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "payment": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CheckoutCreateInput implements Serializable {
        private String email;

        private List<CheckoutLineItemInput> lineItems;

        private MailingAddressInput shippingAddress;

        private String note;

        private List<AttributeInput> customAttributes;

        private Boolean allowPartialAddresses;

        public String getEmail() {
            return email;
        }

        public CheckoutCreateInput setEmail(String email) {
            this.email = email;
            return this;
        }

        public List<CheckoutLineItemInput> getLineItems() {
            return lineItems;
        }

        public CheckoutCreateInput setLineItems(List<CheckoutLineItemInput> lineItems) {
            this.lineItems = lineItems;
            return this;
        }

        public MailingAddressInput getShippingAddress() {
            return shippingAddress;
        }

        public CheckoutCreateInput setShippingAddress(MailingAddressInput shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public String getNote() {
            return note;
        }

        public CheckoutCreateInput setNote(String note) {
            this.note = note;
            return this;
        }

        public List<AttributeInput> getCustomAttributes() {
            return customAttributes;
        }

        public CheckoutCreateInput setCustomAttributes(List<AttributeInput> customAttributes) {
            this.customAttributes = customAttributes;
            return this;
        }

        public Boolean getAllowPartialAddresses() {
            return allowPartialAddresses;
        }

        public CheckoutCreateInput setAllowPartialAddresses(Boolean allowPartialAddresses) {
            this.allowPartialAddresses = allowPartialAddresses;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            if (email != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("email:");
                Query.appendQuotedString(_queryBuilder, email.toString());
            }

            if (lineItems != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("lineItems:");
                _queryBuilder.append('[');

                String listSeperator1 = "";
                for (CheckoutLineItemInput item1 : lineItems) {
                    _queryBuilder.append(listSeperator1);
                    listSeperator1 = ",";
                    item1.appendTo(_queryBuilder);
                }
                _queryBuilder.append(']');
            }

            if (shippingAddress != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("shippingAddress:");
                shippingAddress.appendTo(_queryBuilder);
            }

            if (note != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("note:");
                Query.appendQuotedString(_queryBuilder, note.toString());
            }

            if (customAttributes != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("customAttributes:");
                _queryBuilder.append('[');

                String listSeperator1 = "";
                for (AttributeInput item1 : customAttributes) {
                    _queryBuilder.append(listSeperator1);
                    listSeperator1 = ",";
                    item1.appendTo(_queryBuilder);
                }
                _queryBuilder.append(']');
            }

            if (allowPartialAddresses != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("allowPartialAddresses:");
                _queryBuilder.append(allowPartialAddresses);
            }

            _queryBuilder.append('}');
        }
    }

    public interface CheckoutCreatePayloadQueryDefinition {
        void define(CheckoutCreatePayloadQuery _queryBuilder);
    }

    public static class CheckoutCreatePayloadQuery extends Query<CheckoutCreatePayloadQuery> {
        CheckoutCreatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The new checkout object.
        */
        public CheckoutCreatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutCreatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutCreatePayload extends AbstractResponse<CheckoutCreatePayload> {
        public CheckoutCreatePayload() {
        }

        public CheckoutCreatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutCreatePayload";
        }

        /**
        * The new checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCreatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutCustomerAssociatePayloadQueryDefinition {
        void define(CheckoutCustomerAssociatePayloadQuery _queryBuilder);
    }

    public static class CheckoutCustomerAssociatePayloadQuery extends Query<CheckoutCustomerAssociatePayloadQuery> {
        CheckoutCustomerAssociatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutCustomerAssociatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutCustomerAssociatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutCustomerAssociatePayload extends AbstractResponse<CheckoutCustomerAssociatePayload> {
        public CheckoutCustomerAssociatePayload() {
        }

        public CheckoutCustomerAssociatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutCustomerAssociatePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCustomerAssociatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCustomerAssociatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutCustomerDisassociatePayloadQueryDefinition {
        void define(CheckoutCustomerDisassociatePayloadQuery _queryBuilder);
    }

    public static class CheckoutCustomerDisassociatePayloadQuery extends Query<CheckoutCustomerDisassociatePayloadQuery> {
        CheckoutCustomerDisassociatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutCustomerDisassociatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutCustomerDisassociatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutCustomerDisassociatePayload extends AbstractResponse<CheckoutCustomerDisassociatePayload> {
        public CheckoutCustomerDisassociatePayload() {
        }

        public CheckoutCustomerDisassociatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutCustomerDisassociatePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCustomerDisassociatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCustomerDisassociatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutEmailUpdatePayloadQueryDefinition {
        void define(CheckoutEmailUpdatePayloadQuery _queryBuilder);
    }

    public static class CheckoutEmailUpdatePayloadQuery extends Query<CheckoutEmailUpdatePayloadQuery> {
        CheckoutEmailUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The checkout object with the updated email.
        */
        public CheckoutEmailUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutEmailUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutEmailUpdatePayload extends AbstractResponse<CheckoutEmailUpdatePayload> {
        public CheckoutEmailUpdatePayload() {
        }

        public CheckoutEmailUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutEmailUpdatePayload";
        }

        /**
        * The checkout object with the updated email.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutEmailUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutEmailUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutGiftCardApplyPayloadQueryDefinition {
        void define(CheckoutGiftCardApplyPayloadQuery _queryBuilder);
    }

    public static class CheckoutGiftCardApplyPayloadQuery extends Query<CheckoutGiftCardApplyPayloadQuery> {
        CheckoutGiftCardApplyPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutGiftCardApplyPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutGiftCardApplyPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutGiftCardApplyPayload extends AbstractResponse<CheckoutGiftCardApplyPayload> {
        public CheckoutGiftCardApplyPayload() {
        }

        public CheckoutGiftCardApplyPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutGiftCardApplyPayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutGiftCardApplyPayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutGiftCardApplyPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutGiftCardRemovePayloadQueryDefinition {
        void define(CheckoutGiftCardRemovePayloadQuery _queryBuilder);
    }

    public static class CheckoutGiftCardRemovePayloadQuery extends Query<CheckoutGiftCardRemovePayloadQuery> {
        CheckoutGiftCardRemovePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutGiftCardRemovePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutGiftCardRemovePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutGiftCardRemovePayload extends AbstractResponse<CheckoutGiftCardRemovePayload> {
        public CheckoutGiftCardRemovePayload() {
        }

        public CheckoutGiftCardRemovePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutGiftCardRemovePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutGiftCardRemovePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutGiftCardRemovePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutLineItemQueryDefinition {
        void define(CheckoutLineItemQuery _queryBuilder);
    }

    /**
    * A single line item in the checkout, grouped by variant and attributes.
    */
    public static class CheckoutLineItemQuery extends Query<CheckoutLineItemQuery> {
        CheckoutLineItemQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        /**
        * Extra information in the form of an array of Key-Value pairs about the line item.
        */
        public CheckoutLineItemQuery customAttributes(AttributeQueryDefinition queryDef) {
            startField("customAttributes");

            _queryBuilder.append('{');
            queryDef.define(new AttributeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The quantity of the line item.
        */
        public CheckoutLineItemQuery quantity() {
            startField("quantity");

            return this;
        }

        /**
        * Title of the line item. Defaults to the product's title.
        */
        public CheckoutLineItemQuery title() {
            startField("title");

            return this;
        }

        /**
        * Product variant of the line item.
        */
        public CheckoutLineItemQuery variant(ProductVariantQueryDefinition queryDef) {
            startField("variant");

            _queryBuilder.append('{');
            queryDef.define(new ProductVariantQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    /**
    * A single line item in the checkout, grouped by variant and attributes.
    */
    public static class CheckoutLineItem extends AbstractResponse<CheckoutLineItem> implements Node {
        public CheckoutLineItem() {
        }

        public CheckoutLineItem(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customAttributes": {
                        List<Attribute> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new Attribute(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "quantity": {
                        responseData.put(key, jsonAsInteger(field.getValue(), key));

                        break;
                    }

                    case "title": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "variant": {
                        ProductVariant optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ProductVariant(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public CheckoutLineItem(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "CheckoutLineItem";
        }

        /**
        * Extra information in the form of an array of Key-Value pairs about the line item.
        */

        public List<Attribute> getCustomAttributes() {
            return (List<Attribute>) get("customAttributes");
        }

        public CheckoutLineItem setCustomAttributes(List<Attribute> arg) {
            optimisticData.put(getKey("customAttributes"), arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * The quantity of the line item.
        */

        public Integer getQuantity() {
            return (Integer) get("quantity");
        }

        public CheckoutLineItem setQuantity(Integer arg) {
            optimisticData.put(getKey("quantity"), arg);
            return this;
        }

        /**
        * Title of the line item. Defaults to the product's title.
        */

        public String getTitle() {
            return (String) get("title");
        }

        public CheckoutLineItem setTitle(String arg) {
            optimisticData.put(getKey("title"), arg);
            return this;
        }

        /**
        * Product variant of the line item.
        */

        public ProductVariant getVariant() {
            return (ProductVariant) get("variant");
        }

        public CheckoutLineItem setVariant(ProductVariant arg) {
            optimisticData.put(getKey("variant"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customAttributes": return true;

                case "id": return false;

                case "quantity": return false;

                case "title": return false;

                case "variant": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutLineItemConnectionQueryDefinition {
        void define(CheckoutLineItemConnectionQuery _queryBuilder);
    }

    public static class CheckoutLineItemConnectionQuery extends Query<CheckoutLineItemConnectionQuery> {
        CheckoutLineItemConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public CheckoutLineItemConnectionQuery edges(CheckoutLineItemEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutLineItemEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public CheckoutLineItemConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutLineItemConnection extends AbstractResponse<CheckoutLineItemConnection> {
        public CheckoutLineItemConnection() {
        }

        public CheckoutLineItemConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<CheckoutLineItemEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new CheckoutLineItemEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutLineItemConnection";
        }

        /**
        * A list of edges.
        */

        public List<CheckoutLineItemEdge> getEdges() {
            return (List<CheckoutLineItemEdge>) get("edges");
        }

        public CheckoutLineItemConnection setEdges(List<CheckoutLineItemEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public CheckoutLineItemConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutLineItemEdgeQueryDefinition {
        void define(CheckoutLineItemEdgeQuery _queryBuilder);
    }

    public static class CheckoutLineItemEdgeQuery extends Query<CheckoutLineItemEdgeQuery> {
        CheckoutLineItemEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CheckoutLineItemEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        public CheckoutLineItemEdgeQuery node(CheckoutLineItemQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutLineItemQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutLineItemEdge extends AbstractResponse<CheckoutLineItemEdge> {
        public CheckoutLineItemEdge() {
        }

        public CheckoutLineItemEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new CheckoutLineItem(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutLineItemEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public CheckoutLineItemEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        public CheckoutLineItem getNode() {
            return (CheckoutLineItem) get("node");
        }

        public CheckoutLineItemEdge setNode(CheckoutLineItem arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public static class CheckoutLineItemInput implements Serializable {
        private ID variantId;

        private int quantity;

        private List<AttributeInput> customAttributes;

        public CheckoutLineItemInput(ID variantId, int quantity) {
            this.variantId = variantId;

            this.quantity = quantity;
        }

        public ID getVariantId() {
            return variantId;
        }

        public CheckoutLineItemInput setVariantId(ID variantId) {
            this.variantId = variantId;
            return this;
        }

        public int getQuantity() {
            return quantity;
        }

        public CheckoutLineItemInput setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public List<AttributeInput> getCustomAttributes() {
            return customAttributes;
        }

        public CheckoutLineItemInput setCustomAttributes(List<AttributeInput> customAttributes) {
            this.customAttributes = customAttributes;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("variantId:");
            Query.appendQuotedString(_queryBuilder, variantId.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("quantity:");
            _queryBuilder.append(quantity);

            if (customAttributes != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("customAttributes:");
                _queryBuilder.append('[');

                String listSeperator1 = "";
                for (AttributeInput item1 : customAttributes) {
                    _queryBuilder.append(listSeperator1);
                    listSeperator1 = ",";
                    item1.appendTo(_queryBuilder);
                }
                _queryBuilder.append(']');
            }

            _queryBuilder.append('}');
        }
    }

    public static class CheckoutLineItemUpdateInput implements Serializable {
        private ID id;

        private ID variantId;

        private Integer quantity;

        private List<AttributeInput> customAttributes;

        public ID getId() {
            return id;
        }

        public CheckoutLineItemUpdateInput setId(ID id) {
            this.id = id;
            return this;
        }

        public ID getVariantId() {
            return variantId;
        }

        public CheckoutLineItemUpdateInput setVariantId(ID variantId) {
            this.variantId = variantId;
            return this;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public CheckoutLineItemUpdateInput setQuantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public List<AttributeInput> getCustomAttributes() {
            return customAttributes;
        }

        public CheckoutLineItemUpdateInput setCustomAttributes(List<AttributeInput> customAttributes) {
            this.customAttributes = customAttributes;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            if (id != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("id:");
                Query.appendQuotedString(_queryBuilder, id.toString());
            }

            if (variantId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("variantId:");
                Query.appendQuotedString(_queryBuilder, variantId.toString());
            }

            if (quantity != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("quantity:");
                _queryBuilder.append(quantity);
            }

            if (customAttributes != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("customAttributes:");
                _queryBuilder.append('[');

                String listSeperator1 = "";
                for (AttributeInput item1 : customAttributes) {
                    _queryBuilder.append(listSeperator1);
                    listSeperator1 = ",";
                    item1.appendTo(_queryBuilder);
                }
                _queryBuilder.append(']');
            }

            _queryBuilder.append('}');
        }
    }

    public interface CheckoutLineItemsAddPayloadQueryDefinition {
        void define(CheckoutLineItemsAddPayloadQuery _queryBuilder);
    }

    public static class CheckoutLineItemsAddPayloadQuery extends Query<CheckoutLineItemsAddPayloadQuery> {
        CheckoutLineItemsAddPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutLineItemsAddPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutLineItemsAddPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutLineItemsAddPayload extends AbstractResponse<CheckoutLineItemsAddPayload> {
        public CheckoutLineItemsAddPayload() {
        }

        public CheckoutLineItemsAddPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutLineItemsAddPayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutLineItemsAddPayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutLineItemsAddPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutLineItemsRemovePayloadQueryDefinition {
        void define(CheckoutLineItemsRemovePayloadQuery _queryBuilder);
    }

    public static class CheckoutLineItemsRemovePayloadQuery extends Query<CheckoutLineItemsRemovePayloadQuery> {
        CheckoutLineItemsRemovePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CheckoutLineItemsRemovePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutLineItemsRemovePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutLineItemsRemovePayload extends AbstractResponse<CheckoutLineItemsRemovePayload> {
        public CheckoutLineItemsRemovePayload() {
        }

        public CheckoutLineItemsRemovePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutLineItemsRemovePayload";
        }

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutLineItemsRemovePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutLineItemsRemovePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutLineItemsUpdatePayloadQueryDefinition {
        void define(CheckoutLineItemsUpdatePayloadQuery _queryBuilder);
    }

    public static class CheckoutLineItemsUpdatePayloadQuery extends Query<CheckoutLineItemsUpdatePayloadQuery> {
        CheckoutLineItemsUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutLineItemsUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutLineItemsUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutLineItemsUpdatePayload extends AbstractResponse<CheckoutLineItemsUpdatePayload> {
        public CheckoutLineItemsUpdatePayload() {
        }

        public CheckoutLineItemsUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutLineItemsUpdatePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutLineItemsUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutLineItemsUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutShippingAddressUpdatePayloadQueryDefinition {
        void define(CheckoutShippingAddressUpdatePayloadQuery _queryBuilder);
    }

    public static class CheckoutShippingAddressUpdatePayloadQuery extends Query<CheckoutShippingAddressUpdatePayloadQuery> {
        CheckoutShippingAddressUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutShippingAddressUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutShippingAddressUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutShippingAddressUpdatePayload extends AbstractResponse<CheckoutShippingAddressUpdatePayload> {
        public CheckoutShippingAddressUpdatePayload() {
        }

        public CheckoutShippingAddressUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutShippingAddressUpdatePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutShippingAddressUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutShippingAddressUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CheckoutShippingLineUpdatePayloadQueryDefinition {
        void define(CheckoutShippingLineUpdatePayloadQuery _queryBuilder);
    }

    public static class CheckoutShippingLineUpdatePayloadQuery extends Query<CheckoutShippingLineUpdatePayloadQuery> {
        CheckoutShippingLineUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated checkout object.
        */
        public CheckoutShippingLineUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CheckoutShippingLineUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutShippingLineUpdatePayload extends AbstractResponse<CheckoutShippingLineUpdatePayload> {
        public CheckoutShippingLineUpdatePayload() {
        }

        public CheckoutShippingLineUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkout": {
                        Checkout optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Checkout(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CheckoutShippingLineUpdatePayload";
        }

        /**
        * The updated checkout object.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutShippingLineUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutShippingLineUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkout": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CollectionQueryDefinition {
        void define(CollectionQuery _queryBuilder);
    }

    /**
    * A collection represents a grouping of products that a shop owner can create to organize them or make
    * their shops easier to browse.
    */
    public static class CollectionQuery extends Query<CollectionQuery> {
        CollectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public class DescriptionArguments extends Arguments {
            DescriptionArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Truncates string after the given length.
            */
            public DescriptionArguments truncateAt(Integer value) {
                if (value != null) {
                    startArgument("truncateAt");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface DescriptionArgumentsDefinition {
            void define(DescriptionArguments args);
        }

        /**
        * Stripped description of the collection, single line with HTML tags removed.
        */
        public CollectionQuery description() {
            return description(args -> {});
        }

        /**
        * Stripped description of the collection, single line with HTML tags removed.
        */
        public CollectionQuery description(DescriptionArgumentsDefinition argsDef) {
            startField("description");

            DescriptionArguments args = new DescriptionArguments(_queryBuilder);
            argsDef.define(args);
            DescriptionArguments.end(args);

            return this;
        }

        /**
        * The description of the collection, complete with HTML formatting.
        */
        public CollectionQuery descriptionHtml() {
            startField("descriptionHtml");

            return this;
        }

        /**
        * A human-friendly unique string for the collection automatically generated from its title. Limit of
        * 255 characters. 
        */
        public CollectionQuery handle() {
            startField("handle");

            return this;
        }

        public class ImageArguments extends Arguments {
            ImageArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Image width in pixels between 1 and 2048
            */
            public ImageArguments maxWidth(Integer value) {
                if (value != null) {
                    startArgument("maxWidth");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Image height in pixels between 1 and 2048
            */
            public ImageArguments maxHeight(Integer value) {
                if (value != null) {
                    startArgument("maxHeight");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * If specified, crop the image keeping the specified region
            */
            public ImageArguments crop(CropRegion value) {
                if (value != null) {
                    startArgument("crop");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

            /**
            * Image size multiplier retina displays. Must be between 1 and 3
            */
            public ImageArguments scale(Integer value) {
                if (value != null) {
                    startArgument("scale");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface ImageArgumentsDefinition {
            void define(ImageArguments args);
        }

        /**
        * Image associated with the collection.
        */
        public CollectionQuery image(ImageQueryDefinition queryDef) {
            return image(args -> {}, queryDef);
        }

        /**
        * Image associated with the collection.
        */
        public CollectionQuery image(ImageArgumentsDefinition argsDef, ImageQueryDefinition queryDef) {
            startField("image");

            ImageArguments args = new ImageArguments(_queryBuilder);
            argsDef.define(args);
            ImageArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new ImageQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public class ProductsArguments extends Arguments {
            ProductsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, false);
            }

            public ProductsArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            public ProductsArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface ProductsArgumentsDefinition {
            void define(ProductsArguments args);
        }

        /**
        * List of products in the collection.
        */
        public CollectionQuery products(int first, ProductConnectionQueryDefinition queryDef) {
            return products(first, args -> {}, queryDef);
        }

        /**
        * List of products in the collection.
        */
        public CollectionQuery products(int first, ProductsArgumentsDefinition argsDef, ProductConnectionQueryDefinition queryDef) {
            startField("products");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new ProductsArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new ProductConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The collection’s name. Limit of 255 characters.
        */
        public CollectionQuery title() {
            startField("title");

            return this;
        }

        /**
        * The date and time when the collection was last modified.
        */
        public CollectionQuery updatedAt() {
            startField("updatedAt");

            return this;
        }
    }

    /**
    * A collection represents a grouping of products that a shop owner can create to organize them or make
    * their shops easier to browse.
    */
    public static class Collection extends AbstractResponse<Collection> implements Node {
        public Collection() {
        }

        public Collection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "description": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "descriptionHtml": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "handle": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "image": {
                        Image optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Image(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "products": {
                        responseData.put(key, new ProductConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "title": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "updatedAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public Collection(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "Collection";
        }

        /**
        * Stripped description of the collection, single line with HTML tags removed.
        */

        public String getDescription() {
            return (String) get("description");
        }

        public Collection setDescription(String arg) {
            optimisticData.put(getKey("description"), arg);
            return this;
        }

        /**
        * The description of the collection, complete with HTML formatting.
        */

        public String getDescriptionHtml() {
            return (String) get("descriptionHtml");
        }

        public Collection setDescriptionHtml(String arg) {
            optimisticData.put(getKey("descriptionHtml"), arg);
            return this;
        }

        /**
        * A human-friendly unique string for the collection automatically generated from its title. Limit of
        * 255 characters. 
        */

        public String getHandle() {
            return (String) get("handle");
        }

        public Collection setHandle(String arg) {
            optimisticData.put(getKey("handle"), arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * Image associated with the collection.
        */

        public Image getImage() {
            return (Image) get("image");
        }

        public Collection setImage(Image arg) {
            optimisticData.put(getKey("image"), arg);
            return this;
        }

        /**
        * List of products in the collection.
        */

        public ProductConnection getProducts() {
            return (ProductConnection) get("products");
        }

        public Collection setProducts(ProductConnection arg) {
            optimisticData.put(getKey("products"), arg);
            return this;
        }

        /**
        * The collection’s name. Limit of 255 characters.
        */

        public String getTitle() {
            return (String) get("title");
        }

        public Collection setTitle(String arg) {
            optimisticData.put(getKey("title"), arg);
            return this;
        }

        /**
        * The date and time when the collection was last modified.
        */

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Collection setUpdatedAt(DateTime arg) {
            optimisticData.put(getKey("updatedAt"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "description": return false;

                case "descriptionHtml": return false;

                case "handle": return false;

                case "id": return false;

                case "image": return true;

                case "products": return true;

                case "title": return false;

                case "updatedAt": return false;

                default: return false;
            }
        }
    }

    public interface CollectionConnectionQueryDefinition {
        void define(CollectionConnectionQuery _queryBuilder);
    }

    public static class CollectionConnectionQuery extends Query<CollectionConnectionQuery> {
        CollectionConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public CollectionConnectionQuery edges(CollectionEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new CollectionEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public CollectionConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CollectionConnection extends AbstractResponse<CollectionConnection> {
        public CollectionConnection() {
        }

        public CollectionConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<CollectionEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new CollectionEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CollectionConnection";
        }

        /**
        * A list of edges.
        */

        public List<CollectionEdge> getEdges() {
            return (List<CollectionEdge>) get("edges");
        }

        public CollectionConnection setEdges(List<CollectionEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public CollectionConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface CollectionEdgeQueryDefinition {
        void define(CollectionEdgeQuery _queryBuilder);
    }

    public static class CollectionEdgeQuery extends Query<CollectionEdgeQuery> {
        CollectionEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CollectionEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        public CollectionEdgeQuery node(CollectionQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new CollectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CollectionEdge extends AbstractResponse<CollectionEdge> {
        public CollectionEdge() {
        }

        public CollectionEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new Collection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CollectionEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public CollectionEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        public Collection getNode() {
            return (Collection) get("node");
        }

        public CollectionEdge setNode(Collection arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    /**
    * The set of valid sort keys for the collections query.
    */
    public enum CollectionSortKeys {
        ID,

        RELEVANCE,

        TITLE,

        UPDATED_AT,

        UNKNOWN_VALUE;

        public static CollectionSortKeys fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "ID": {
                    return ID;
                }

                case "RELEVANCE": {
                    return RELEVANCE;
                }

                case "TITLE": {
                    return TITLE;
                }

                case "UPDATED_AT": {
                    return UPDATED_AT;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case ID: {
                    return "ID";
                }

                case RELEVANCE: {
                    return "RELEVANCE";
                }

                case TITLE: {
                    return "TITLE";
                }

                case UPDATED_AT: {
                    return "UPDATED_AT";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface CreditCardQueryDefinition {
        void define(CreditCardQuery _queryBuilder);
    }

    /**
    * Credit card information used for a payment.
    */
    public static class CreditCardQuery extends Query<CreditCardQuery> {
        CreditCardQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CreditCardQuery brand() {
            startField("brand");

            return this;
        }

        public CreditCardQuery expiryMonth() {
            startField("expiryMonth");

            return this;
        }

        public CreditCardQuery expiryYear() {
            startField("expiryYear");

            return this;
        }

        public CreditCardQuery firstDigits() {
            startField("firstDigits");

            return this;
        }

        public CreditCardQuery firstName() {
            startField("firstName");

            return this;
        }

        public CreditCardQuery lastDigits() {
            startField("lastDigits");

            return this;
        }

        public CreditCardQuery lastName() {
            startField("lastName");

            return this;
        }

        /**
        * Masked credit card number with only the last 4 digits displayed
        */
        public CreditCardQuery maskedNumber() {
            startField("maskedNumber");

            return this;
        }
    }

    /**
    * Credit card information used for a payment.
    */
    public static class CreditCard extends AbstractResponse<CreditCard> {
        public CreditCard() {
        }

        public CreditCard(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "brand": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "expiryMonth": {
                        Integer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsInteger(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "expiryYear": {
                        Integer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsInteger(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "firstDigits": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "firstName": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "lastDigits": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "lastName": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "maskedNumber": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CreditCard";
        }

        public String getBrand() {
            return (String) get("brand");
        }

        public CreditCard setBrand(String arg) {
            optimisticData.put(getKey("brand"), arg);
            return this;
        }

        public Integer getExpiryMonth() {
            return (Integer) get("expiryMonth");
        }

        public CreditCard setExpiryMonth(Integer arg) {
            optimisticData.put(getKey("expiryMonth"), arg);
            return this;
        }

        public Integer getExpiryYear() {
            return (Integer) get("expiryYear");
        }

        public CreditCard setExpiryYear(Integer arg) {
            optimisticData.put(getKey("expiryYear"), arg);
            return this;
        }

        public String getFirstDigits() {
            return (String) get("firstDigits");
        }

        public CreditCard setFirstDigits(String arg) {
            optimisticData.put(getKey("firstDigits"), arg);
            return this;
        }

        public String getFirstName() {
            return (String) get("firstName");
        }

        public CreditCard setFirstName(String arg) {
            optimisticData.put(getKey("firstName"), arg);
            return this;
        }

        public String getLastDigits() {
            return (String) get("lastDigits");
        }

        public CreditCard setLastDigits(String arg) {
            optimisticData.put(getKey("lastDigits"), arg);
            return this;
        }

        public String getLastName() {
            return (String) get("lastName");
        }

        public CreditCard setLastName(String arg) {
            optimisticData.put(getKey("lastName"), arg);
            return this;
        }

        /**
        * Masked credit card number with only the last 4 digits displayed
        */

        public String getMaskedNumber() {
            return (String) get("maskedNumber");
        }

        public CreditCard setMaskedNumber(String arg) {
            optimisticData.put(getKey("maskedNumber"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "brand": return false;

                case "expiryMonth": return false;

                case "expiryYear": return false;

                case "firstDigits": return false;

                case "firstName": return false;

                case "lastDigits": return false;

                case "lastName": return false;

                case "maskedNumber": return false;

                default: return false;
            }
        }
    }

    public static class CreditCardPaymentInput implements Serializable {
        private BigDecimal amount;

        private String idempotencyKey;

        private MailingAddressInput billingAddress;

        private String vaultId;

        private Boolean test;

        public CreditCardPaymentInput(BigDecimal amount, String idempotencyKey, MailingAddressInput billingAddress, String vaultId) {
            this.amount = amount;

            this.idempotencyKey = idempotencyKey;

            this.billingAddress = billingAddress;

            this.vaultId = vaultId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public CreditCardPaymentInput setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public String getIdempotencyKey() {
            return idempotencyKey;
        }

        public CreditCardPaymentInput setIdempotencyKey(String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        public MailingAddressInput getBillingAddress() {
            return billingAddress;
        }

        public CreditCardPaymentInput setBillingAddress(MailingAddressInput billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public String getVaultId() {
            return vaultId;
        }

        public CreditCardPaymentInput setVaultId(String vaultId) {
            this.vaultId = vaultId;
            return this;
        }

        public Boolean getTest() {
            return test;
        }

        public CreditCardPaymentInput setTest(Boolean test) {
            this.test = test;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("amount:");
            Query.appendQuotedString(_queryBuilder, amount.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("idempotencyKey:");
            Query.appendQuotedString(_queryBuilder, idempotencyKey.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("billingAddress:");
            billingAddress.appendTo(_queryBuilder);

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("vaultId:");
            Query.appendQuotedString(_queryBuilder, vaultId.toString());

            if (test != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("test:");
                _queryBuilder.append(test);
            }

            _queryBuilder.append('}');
        }
    }

    /**
    * The part of the image that should remain after cropping.
    */
    public enum CropRegion {
        BOTTOM,

        CENTER,

        LEFT,

        RIGHT,

        TOP,

        UNKNOWN_VALUE;

        public static CropRegion fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "BOTTOM": {
                    return BOTTOM;
                }

                case "CENTER": {
                    return CENTER;
                }

                case "LEFT": {
                    return LEFT;
                }

                case "RIGHT": {
                    return RIGHT;
                }

                case "TOP": {
                    return TOP;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case BOTTOM: {
                    return "BOTTOM";
                }

                case CENTER: {
                    return "CENTER";
                }

                case LEFT: {
                    return "LEFT";
                }

                case RIGHT: {
                    return "RIGHT";
                }

                case TOP: {
                    return "TOP";
                }

                default: {
                    return "";
                }
            }
        }
    }

    /**
    * Currency codes
    */
    public enum CurrencyCode {
        AED,

        AFN,

        ALL,

        AMD,

        ANG,

        AOA,

        ARS,

        AUD,

        AWG,

        AZN,

        BAM,

        BBD,

        BDT,

        BGN,

        BHD,

        BND,

        BOB,

        BRL,

        BSD,

        BTN,

        BWP,

        BYR,

        BZD,

        CAD,

        CDF,

        CHF,

        CLP,

        CNY,

        COP,

        CRC,

        CZK,

        DKK,

        DOP,

        DZD,

        EGP,

        ETB,

        EUR,

        FJD,

        GBP,

        GEL,

        GHS,

        GMD,

        GTQ,

        GYD,

        HKD,

        HNL,

        HRK,

        HTG,

        HUF,

        IDR,

        ILS,

        INR,

        ISK,

        JEP,

        JMD,

        JOD,

        JPY,

        KES,

        KGS,

        KHR,

        KMF,

        KRW,

        KWD,

        KYD,

        KZT,

        LAK,

        LBP,

        LKR,

        LRD,

        LSL,

        LTL,

        LVL,

        MAD,

        MDL,

        MGA,

        MKD,

        MMK,

        MNT,

        MOP,

        MUR,

        MVR,

        MWK,

        MXN,

        MYR,

        MZN,

        NAD,

        NGN,

        NIO,

        NOK,

        NPR,

        NZD,

        OMR,

        PEN,

        PGK,

        PHP,

        PKR,

        PLN,

        PYG,

        QAR,

        RON,

        RSD,

        RUB,

        RWF,

        SAR,

        SBD,

        SCR,

        SDG,

        SEK,

        SGD,

        SRD,

        STD,

        SYP,

        THB,

        TMT,

        TND,

        TRY,

        TTD,

        TWD,

        TZS,

        UAH,

        UGX,

        USD,

        UYU,

        UZS,

        VEF,

        VND,

        VUV,

        WST,

        XAF,

        XCD,

        XOF,

        XPF,

        YER,

        ZAR,

        ZMW,

        UNKNOWN_VALUE;

        public static CurrencyCode fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "AED": {
                    return AED;
                }

                case "AFN": {
                    return AFN;
                }

                case "ALL": {
                    return ALL;
                }

                case "AMD": {
                    return AMD;
                }

                case "ANG": {
                    return ANG;
                }

                case "AOA": {
                    return AOA;
                }

                case "ARS": {
                    return ARS;
                }

                case "AUD": {
                    return AUD;
                }

                case "AWG": {
                    return AWG;
                }

                case "AZN": {
                    return AZN;
                }

                case "BAM": {
                    return BAM;
                }

                case "BBD": {
                    return BBD;
                }

                case "BDT": {
                    return BDT;
                }

                case "BGN": {
                    return BGN;
                }

                case "BHD": {
                    return BHD;
                }

                case "BND": {
                    return BND;
                }

                case "BOB": {
                    return BOB;
                }

                case "BRL": {
                    return BRL;
                }

                case "BSD": {
                    return BSD;
                }

                case "BTN": {
                    return BTN;
                }

                case "BWP": {
                    return BWP;
                }

                case "BYR": {
                    return BYR;
                }

                case "BZD": {
                    return BZD;
                }

                case "CAD": {
                    return CAD;
                }

                case "CDF": {
                    return CDF;
                }

                case "CHF": {
                    return CHF;
                }

                case "CLP": {
                    return CLP;
                }

                case "CNY": {
                    return CNY;
                }

                case "COP": {
                    return COP;
                }

                case "CRC": {
                    return CRC;
                }

                case "CZK": {
                    return CZK;
                }

                case "DKK": {
                    return DKK;
                }

                case "DOP": {
                    return DOP;
                }

                case "DZD": {
                    return DZD;
                }

                case "EGP": {
                    return EGP;
                }

                case "ETB": {
                    return ETB;
                }

                case "EUR": {
                    return EUR;
                }

                case "FJD": {
                    return FJD;
                }

                case "GBP": {
                    return GBP;
                }

                case "GEL": {
                    return GEL;
                }

                case "GHS": {
                    return GHS;
                }

                case "GMD": {
                    return GMD;
                }

                case "GTQ": {
                    return GTQ;
                }

                case "GYD": {
                    return GYD;
                }

                case "HKD": {
                    return HKD;
                }

                case "HNL": {
                    return HNL;
                }

                case "HRK": {
                    return HRK;
                }

                case "HTG": {
                    return HTG;
                }

                case "HUF": {
                    return HUF;
                }

                case "IDR": {
                    return IDR;
                }

                case "ILS": {
                    return ILS;
                }

                case "INR": {
                    return INR;
                }

                case "ISK": {
                    return ISK;
                }

                case "JEP": {
                    return JEP;
                }

                case "JMD": {
                    return JMD;
                }

                case "JOD": {
                    return JOD;
                }

                case "JPY": {
                    return JPY;
                }

                case "KES": {
                    return KES;
                }

                case "KGS": {
                    return KGS;
                }

                case "KHR": {
                    return KHR;
                }

                case "KMF": {
                    return KMF;
                }

                case "KRW": {
                    return KRW;
                }

                case "KWD": {
                    return KWD;
                }

                case "KYD": {
                    return KYD;
                }

                case "KZT": {
                    return KZT;
                }

                case "LAK": {
                    return LAK;
                }

                case "LBP": {
                    return LBP;
                }

                case "LKR": {
                    return LKR;
                }

                case "LRD": {
                    return LRD;
                }

                case "LSL": {
                    return LSL;
                }

                case "LTL": {
                    return LTL;
                }

                case "LVL": {
                    return LVL;
                }

                case "MAD": {
                    return MAD;
                }

                case "MDL": {
                    return MDL;
                }

                case "MGA": {
                    return MGA;
                }

                case "MKD": {
                    return MKD;
                }

                case "MMK": {
                    return MMK;
                }

                case "MNT": {
                    return MNT;
                }

                case "MOP": {
                    return MOP;
                }

                case "MUR": {
                    return MUR;
                }

                case "MVR": {
                    return MVR;
                }

                case "MWK": {
                    return MWK;
                }

                case "MXN": {
                    return MXN;
                }

                case "MYR": {
                    return MYR;
                }

                case "MZN": {
                    return MZN;
                }

                case "NAD": {
                    return NAD;
                }

                case "NGN": {
                    return NGN;
                }

                case "NIO": {
                    return NIO;
                }

                case "NOK": {
                    return NOK;
                }

                case "NPR": {
                    return NPR;
                }

                case "NZD": {
                    return NZD;
                }

                case "OMR": {
                    return OMR;
                }

                case "PEN": {
                    return PEN;
                }

                case "PGK": {
                    return PGK;
                }

                case "PHP": {
                    return PHP;
                }

                case "PKR": {
                    return PKR;
                }

                case "PLN": {
                    return PLN;
                }

                case "PYG": {
                    return PYG;
                }

                case "QAR": {
                    return QAR;
                }

                case "RON": {
                    return RON;
                }

                case "RSD": {
                    return RSD;
                }

                case "RUB": {
                    return RUB;
                }

                case "RWF": {
                    return RWF;
                }

                case "SAR": {
                    return SAR;
                }

                case "SBD": {
                    return SBD;
                }

                case "SCR": {
                    return SCR;
                }

                case "SDG": {
                    return SDG;
                }

                case "SEK": {
                    return SEK;
                }

                case "SGD": {
                    return SGD;
                }

                case "SRD": {
                    return SRD;
                }

                case "STD": {
                    return STD;
                }

                case "SYP": {
                    return SYP;
                }

                case "THB": {
                    return THB;
                }

                case "TMT": {
                    return TMT;
                }

                case "TND": {
                    return TND;
                }

                case "TRY": {
                    return TRY;
                }

                case "TTD": {
                    return TTD;
                }

                case "TWD": {
                    return TWD;
                }

                case "TZS": {
                    return TZS;
                }

                case "UAH": {
                    return UAH;
                }

                case "UGX": {
                    return UGX;
                }

                case "USD": {
                    return USD;
                }

                case "UYU": {
                    return UYU;
                }

                case "UZS": {
                    return UZS;
                }

                case "VEF": {
                    return VEF;
                }

                case "VND": {
                    return VND;
                }

                case "VUV": {
                    return VUV;
                }

                case "WST": {
                    return WST;
                }

                case "XAF": {
                    return XAF;
                }

                case "XCD": {
                    return XCD;
                }

                case "XOF": {
                    return XOF;
                }

                case "XPF": {
                    return XPF;
                }

                case "YER": {
                    return YER;
                }

                case "ZAR": {
                    return ZAR;
                }

                case "ZMW": {
                    return ZMW;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case AED: {
                    return "AED";
                }

                case AFN: {
                    return "AFN";
                }

                case ALL: {
                    return "ALL";
                }

                case AMD: {
                    return "AMD";
                }

                case ANG: {
                    return "ANG";
                }

                case AOA: {
                    return "AOA";
                }

                case ARS: {
                    return "ARS";
                }

                case AUD: {
                    return "AUD";
                }

                case AWG: {
                    return "AWG";
                }

                case AZN: {
                    return "AZN";
                }

                case BAM: {
                    return "BAM";
                }

                case BBD: {
                    return "BBD";
                }

                case BDT: {
                    return "BDT";
                }

                case BGN: {
                    return "BGN";
                }

                case BHD: {
                    return "BHD";
                }

                case BND: {
                    return "BND";
                }

                case BOB: {
                    return "BOB";
                }

                case BRL: {
                    return "BRL";
                }

                case BSD: {
                    return "BSD";
                }

                case BTN: {
                    return "BTN";
                }

                case BWP: {
                    return "BWP";
                }

                case BYR: {
                    return "BYR";
                }

                case BZD: {
                    return "BZD";
                }

                case CAD: {
                    return "CAD";
                }

                case CDF: {
                    return "CDF";
                }

                case CHF: {
                    return "CHF";
                }

                case CLP: {
                    return "CLP";
                }

                case CNY: {
                    return "CNY";
                }

                case COP: {
                    return "COP";
                }

                case CRC: {
                    return "CRC";
                }

                case CZK: {
                    return "CZK";
                }

                case DKK: {
                    return "DKK";
                }

                case DOP: {
                    return "DOP";
                }

                case DZD: {
                    return "DZD";
                }

                case EGP: {
                    return "EGP";
                }

                case ETB: {
                    return "ETB";
                }

                case EUR: {
                    return "EUR";
                }

                case FJD: {
                    return "FJD";
                }

                case GBP: {
                    return "GBP";
                }

                case GEL: {
                    return "GEL";
                }

                case GHS: {
                    return "GHS";
                }

                case GMD: {
                    return "GMD";
                }

                case GTQ: {
                    return "GTQ";
                }

                case GYD: {
                    return "GYD";
                }

                case HKD: {
                    return "HKD";
                }

                case HNL: {
                    return "HNL";
                }

                case HRK: {
                    return "HRK";
                }

                case HTG: {
                    return "HTG";
                }

                case HUF: {
                    return "HUF";
                }

                case IDR: {
                    return "IDR";
                }

                case ILS: {
                    return "ILS";
                }

                case INR: {
                    return "INR";
                }

                case ISK: {
                    return "ISK";
                }

                case JEP: {
                    return "JEP";
                }

                case JMD: {
                    return "JMD";
                }

                case JOD: {
                    return "JOD";
                }

                case JPY: {
                    return "JPY";
                }

                case KES: {
                    return "KES";
                }

                case KGS: {
                    return "KGS";
                }

                case KHR: {
                    return "KHR";
                }

                case KMF: {
                    return "KMF";
                }

                case KRW: {
                    return "KRW";
                }

                case KWD: {
                    return "KWD";
                }

                case KYD: {
                    return "KYD";
                }

                case KZT: {
                    return "KZT";
                }

                case LAK: {
                    return "LAK";
                }

                case LBP: {
                    return "LBP";
                }

                case LKR: {
                    return "LKR";
                }

                case LRD: {
                    return "LRD";
                }

                case LSL: {
                    return "LSL";
                }

                case LTL: {
                    return "LTL";
                }

                case LVL: {
                    return "LVL";
                }

                case MAD: {
                    return "MAD";
                }

                case MDL: {
                    return "MDL";
                }

                case MGA: {
                    return "MGA";
                }

                case MKD: {
                    return "MKD";
                }

                case MMK: {
                    return "MMK";
                }

                case MNT: {
                    return "MNT";
                }

                case MOP: {
                    return "MOP";
                }

                case MUR: {
                    return "MUR";
                }

                case MVR: {
                    return "MVR";
                }

                case MWK: {
                    return "MWK";
                }

                case MXN: {
                    return "MXN";
                }

                case MYR: {
                    return "MYR";
                }

                case MZN: {
                    return "MZN";
                }

                case NAD: {
                    return "NAD";
                }

                case NGN: {
                    return "NGN";
                }

                case NIO: {
                    return "NIO";
                }

                case NOK: {
                    return "NOK";
                }

                case NPR: {
                    return "NPR";
                }

                case NZD: {
                    return "NZD";
                }

                case OMR: {
                    return "OMR";
                }

                case PEN: {
                    return "PEN";
                }

                case PGK: {
                    return "PGK";
                }

                case PHP: {
                    return "PHP";
                }

                case PKR: {
                    return "PKR";
                }

                case PLN: {
                    return "PLN";
                }

                case PYG: {
                    return "PYG";
                }

                case QAR: {
                    return "QAR";
                }

                case RON: {
                    return "RON";
                }

                case RSD: {
                    return "RSD";
                }

                case RUB: {
                    return "RUB";
                }

                case RWF: {
                    return "RWF";
                }

                case SAR: {
                    return "SAR";
                }

                case SBD: {
                    return "SBD";
                }

                case SCR: {
                    return "SCR";
                }

                case SDG: {
                    return "SDG";
                }

                case SEK: {
                    return "SEK";
                }

                case SGD: {
                    return "SGD";
                }

                case SRD: {
                    return "SRD";
                }

                case STD: {
                    return "STD";
                }

                case SYP: {
                    return "SYP";
                }

                case THB: {
                    return "THB";
                }

                case TMT: {
                    return "TMT";
                }

                case TND: {
                    return "TND";
                }

                case TRY: {
                    return "TRY";
                }

                case TTD: {
                    return "TTD";
                }

                case TWD: {
                    return "TWD";
                }

                case TZS: {
                    return "TZS";
                }

                case UAH: {
                    return "UAH";
                }

                case UGX: {
                    return "UGX";
                }

                case USD: {
                    return "USD";
                }

                case UYU: {
                    return "UYU";
                }

                case UZS: {
                    return "UZS";
                }

                case VEF: {
                    return "VEF";
                }

                case VND: {
                    return "VND";
                }

                case VUV: {
                    return "VUV";
                }

                case WST: {
                    return "WST";
                }

                case XAF: {
                    return "XAF";
                }

                case XCD: {
                    return "XCD";
                }

                case XOF: {
                    return "XOF";
                }

                case XPF: {
                    return "XPF";
                }

                case YER: {
                    return "YER";
                }

                case ZAR: {
                    return "ZAR";
                }

                case ZMW: {
                    return "ZMW";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface CustomerQueryDefinition {
        void define(CustomerQuery _queryBuilder);
    }

    /**
    * A customer represents a customer account with the shop. Customer accounts store contact information
    * for the customer, saving logged-in customers the trouble of having to provide it at every checkout.
    */
    public static class CustomerQuery extends Query<CustomerQuery> {
        CustomerQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * Indicates whether the customer has consented to be sent marketing material via email.
        */
        public CustomerQuery acceptsMarketing() {
            startField("acceptsMarketing");

            return this;
        }

        public class AddressesArguments extends Arguments {
            AddressesArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, false);
            }

            public AddressesArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            public AddressesArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface AddressesArgumentsDefinition {
            void define(AddressesArguments args);
        }

        /**
        * A list of addresses for the customer.
        */
        public CustomerQuery addresses(int first, MailingAddressConnectionQueryDefinition queryDef) {
            return addresses(first, args -> {}, queryDef);
        }

        /**
        * A list of addresses for the customer.
        */
        public CustomerQuery addresses(int first, AddressesArgumentsDefinition argsDef, MailingAddressConnectionQueryDefinition queryDef) {
            startField("addresses");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new AddressesArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The date and time when the customer was created.
        */
        public CustomerQuery createdAt() {
            startField("createdAt");

            return this;
        }

        /**
        * The customer’s default address.
        */
        public CustomerQuery defaultAddress(MailingAddressQueryDefinition queryDef) {
            startField("defaultAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The customer’s name, email or phone number.
        */
        public CustomerQuery displayName() {
            startField("displayName");

            return this;
        }

        /**
        * The customer’s email address.
        */
        public CustomerQuery email() {
            startField("email");

            return this;
        }

        /**
        * The customer’s first name.
        */
        public CustomerQuery firstName() {
            startField("firstName");

            return this;
        }

        /**
        * A unique identifier for the customer.
        */
        public CustomerQuery id() {
            startField("id");

            return this;
        }

        /**
        * The customer’s last name.
        */
        public CustomerQuery lastName() {
            startField("lastName");

            return this;
        }

        public class OrdersArguments extends Arguments {
            OrdersArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, false);
            }

            public OrdersArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            public OrdersArguments sortKey(OrderSortKeys value) {
                if (value != null) {
                    startArgument("sortKey");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

            public OrdersArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Supported filter parameters: 
            * <ul>
            * <li>status</li>
            * <li>financial_status</li>
            * <li>fulfillment_status</li>
            * <li>source_name</li>
            * <li>chargeback_status</li>
            * <li>risk_level</li>
            * <li>customer_id</li>
            * <li>email</li>
            * <li>credit_card_last4</li>
            * <li>processed_at</li>
            * <li>checkout_token</li>
            * <li>cart_token</li>
            * <li>location_id</li>
            * <li>channel_id</li>
            * <li>discount_code</li>
            * <li>tag</li>
            * <li>since_id</li>
            * <li>updated_at</li>
            * <li>created_at</li>
            * 
            * </ul>
            */
            public OrdersArguments query(String value) {
                if (value != null) {
                    startArgument("query");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }
        }

        public interface OrdersArgumentsDefinition {
            void define(OrdersArguments args);
        }

        /**
        * The orders associated with the customer.
        */
        public CustomerQuery orders(int first, OrderConnectionQueryDefinition queryDef) {
            return orders(first, args -> {}, queryDef);
        }

        /**
        * The orders associated with the customer.
        */
        public CustomerQuery orders(int first, OrdersArgumentsDefinition argsDef, OrderConnectionQueryDefinition queryDef) {
            startField("orders");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new OrdersArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new OrderConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The customer’s phone number.
        */
        public CustomerQuery phone() {
            startField("phone");

            return this;
        }

        /**
        * The date and time when the customer information was updated.
        */
        public CustomerQuery updatedAt() {
            startField("updatedAt");

            return this;
        }
    }

    /**
    * A customer represents a customer account with the shop. Customer accounts store contact information
    * for the customer, saving logged-in customers the trouble of having to provide it at every checkout.
    */
    public static class Customer extends AbstractResponse<Customer> {
        public Customer() {
        }

        public Customer(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "acceptsMarketing": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "addresses": {
                        responseData.put(key, new MailingAddressConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "createdAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "defaultAddress": {
                        MailingAddress optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new MailingAddress(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "displayName": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "email": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "firstName": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "lastName": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "orders": {
                        responseData.put(key, new OrderConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "phone": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "updatedAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "Customer";
        }

        /**
        * Indicates whether the customer has consented to be sent marketing material via email.
        */

        public Boolean getAcceptsMarketing() {
            return (Boolean) get("acceptsMarketing");
        }

        public Customer setAcceptsMarketing(Boolean arg) {
            optimisticData.put(getKey("acceptsMarketing"), arg);
            return this;
        }

        /**
        * A list of addresses for the customer.
        */

        public MailingAddressConnection getAddresses() {
            return (MailingAddressConnection) get("addresses");
        }

        public Customer setAddresses(MailingAddressConnection arg) {
            optimisticData.put(getKey("addresses"), arg);
            return this;
        }

        /**
        * The date and time when the customer was created.
        */

        public DateTime getCreatedAt() {
            return (DateTime) get("createdAt");
        }

        public Customer setCreatedAt(DateTime arg) {
            optimisticData.put(getKey("createdAt"), arg);
            return this;
        }

        /**
        * The customer’s default address.
        */

        public MailingAddress getDefaultAddress() {
            return (MailingAddress) get("defaultAddress");
        }

        public Customer setDefaultAddress(MailingAddress arg) {
            optimisticData.put(getKey("defaultAddress"), arg);
            return this;
        }

        /**
        * The customer’s name, email or phone number.
        */

        public String getDisplayName() {
            return (String) get("displayName");
        }

        public Customer setDisplayName(String arg) {
            optimisticData.put(getKey("displayName"), arg);
            return this;
        }

        /**
        * The customer’s email address.
        */

        public String getEmail() {
            return (String) get("email");
        }

        public Customer setEmail(String arg) {
            optimisticData.put(getKey("email"), arg);
            return this;
        }

        /**
        * The customer’s first name.
        */

        public String getFirstName() {
            return (String) get("firstName");
        }

        public Customer setFirstName(String arg) {
            optimisticData.put(getKey("firstName"), arg);
            return this;
        }

        /**
        * A unique identifier for the customer.
        */

        public ID getId() {
            return (ID) get("id");
        }

        public Customer setId(ID arg) {
            optimisticData.put(getKey("id"), arg);
            return this;
        }

        /**
        * The customer’s last name.
        */

        public String getLastName() {
            return (String) get("lastName");
        }

        public Customer setLastName(String arg) {
            optimisticData.put(getKey("lastName"), arg);
            return this;
        }

        /**
        * The orders associated with the customer.
        */

        public OrderConnection getOrders() {
            return (OrderConnection) get("orders");
        }

        public Customer setOrders(OrderConnection arg) {
            optimisticData.put(getKey("orders"), arg);
            return this;
        }

        /**
        * The customer’s phone number.
        */

        public String getPhone() {
            return (String) get("phone");
        }

        public Customer setPhone(String arg) {
            optimisticData.put(getKey("phone"), arg);
            return this;
        }

        /**
        * The date and time when the customer information was updated.
        */

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Customer setUpdatedAt(DateTime arg) {
            optimisticData.put(getKey("updatedAt"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "acceptsMarketing": return false;

                case "addresses": return true;

                case "createdAt": return false;

                case "defaultAddress": return true;

                case "displayName": return false;

                case "email": return false;

                case "firstName": return false;

                case "id": return false;

                case "lastName": return false;

                case "orders": return true;

                case "phone": return false;

                case "updatedAt": return false;

                default: return false;
            }
        }
    }

    public interface CustomerAccessTokenQueryDefinition {
        void define(CustomerAccessTokenQuery _queryBuilder);
    }

    /**
    * A CustomerAccessToken represents the unique token required to make modifications to the customer
    * object.
    */
    public static class CustomerAccessTokenQuery extends Query<CustomerAccessTokenQuery> {
        CustomerAccessTokenQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The customer’s access token.
        */
        public CustomerAccessTokenQuery accessToken() {
            startField("accessToken");

            return this;
        }

        /**
        * The date and time when the customer access token expires.
        */
        public CustomerAccessTokenQuery expiresAt() {
            startField("expiresAt");

            return this;
        }
    }

    /**
    * A CustomerAccessToken represents the unique token required to make modifications to the customer
    * object.
    */
    public static class CustomerAccessToken extends AbstractResponse<CustomerAccessToken> {
        public CustomerAccessToken() {
        }

        public CustomerAccessToken(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "accessToken": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "expiresAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAccessToken";
        }

        /**
        * The customer’s access token.
        */

        public String getAccessToken() {
            return (String) get("accessToken");
        }

        public CustomerAccessToken setAccessToken(String arg) {
            optimisticData.put(getKey("accessToken"), arg);
            return this;
        }

        /**
        * The date and time when the customer access token expires.
        */

        public DateTime getExpiresAt() {
            return (DateTime) get("expiresAt");
        }

        public CustomerAccessToken setExpiresAt(DateTime arg) {
            optimisticData.put(getKey("expiresAt"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "accessToken": return false;

                case "expiresAt": return false;

                default: return false;
            }
        }
    }

    public static class CustomerAccessTokenCreateInput implements Serializable {
        private String email;

        private String password;

        public CustomerAccessTokenCreateInput(String email, String password) {
            this.email = email;

            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public CustomerAccessTokenCreateInput setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public CustomerAccessTokenCreateInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("email:");
            Query.appendQuotedString(_queryBuilder, email.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("password:");
            Query.appendQuotedString(_queryBuilder, password.toString());

            _queryBuilder.append('}');
        }
    }

    public interface CustomerAccessTokenCreatePayloadQueryDefinition {
        void define(CustomerAccessTokenCreatePayloadQuery _queryBuilder);
    }

    public static class CustomerAccessTokenCreatePayloadQuery extends Query<CustomerAccessTokenCreatePayloadQuery> {
        CustomerAccessTokenCreatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The newly created customer access token object.
        */
        public CustomerAccessTokenCreatePayloadQuery customerAccessToken(CustomerAccessTokenQueryDefinition queryDef) {
            startField("customerAccessToken");

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerAccessTokenCreatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerAccessTokenCreatePayload extends AbstractResponse<CustomerAccessTokenCreatePayload> {
        public CustomerAccessTokenCreatePayload() {
        }

        public CustomerAccessTokenCreatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customerAccessToken": {
                        CustomerAccessToken optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerAccessToken(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAccessTokenCreatePayload";
        }

        /**
        * The newly created customer access token object.
        */

        public CustomerAccessToken getCustomerAccessToken() {
            return (CustomerAccessToken) get("customerAccessToken");
        }

        public CustomerAccessTokenCreatePayload setCustomerAccessToken(CustomerAccessToken arg) {
            optimisticData.put(getKey("customerAccessToken"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAccessTokenCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customerAccessToken": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerAccessTokenDeletePayloadQueryDefinition {
        void define(CustomerAccessTokenDeletePayloadQuery _queryBuilder);
    }

    public static class CustomerAccessTokenDeletePayloadQuery extends Query<CustomerAccessTokenDeletePayloadQuery> {
        CustomerAccessTokenDeletePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The destroyed access token.
        */
        public CustomerAccessTokenDeletePayloadQuery deletedAccessToken() {
            startField("deletedAccessToken");

            return this;
        }

        /**
        * ID of the destroyed customer access token.
        */
        public CustomerAccessTokenDeletePayloadQuery deletedCustomerAccessTokenId() {
            startField("deletedCustomerAccessTokenId");

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerAccessTokenDeletePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerAccessTokenDeletePayload extends AbstractResponse<CustomerAccessTokenDeletePayload> {
        public CustomerAccessTokenDeletePayload() {
        }

        public CustomerAccessTokenDeletePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "deletedAccessToken": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "deletedCustomerAccessTokenId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAccessTokenDeletePayload";
        }

        /**
        * The destroyed access token.
        */

        public String getDeletedAccessToken() {
            return (String) get("deletedAccessToken");
        }

        public CustomerAccessTokenDeletePayload setDeletedAccessToken(String arg) {
            optimisticData.put(getKey("deletedAccessToken"), arg);
            return this;
        }

        /**
        * ID of the destroyed customer access token.
        */

        public String getDeletedCustomerAccessTokenId() {
            return (String) get("deletedCustomerAccessTokenId");
        }

        public CustomerAccessTokenDeletePayload setDeletedCustomerAccessTokenId(String arg) {
            optimisticData.put(getKey("deletedCustomerAccessTokenId"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAccessTokenDeletePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "deletedAccessToken": return false;

                case "deletedCustomerAccessTokenId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerAccessTokenRenewPayloadQueryDefinition {
        void define(CustomerAccessTokenRenewPayloadQuery _queryBuilder);
    }

    public static class CustomerAccessTokenRenewPayloadQuery extends Query<CustomerAccessTokenRenewPayloadQuery> {
        CustomerAccessTokenRenewPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The renewed customer access token object.
        */
        public CustomerAccessTokenRenewPayloadQuery customerAccessToken(CustomerAccessTokenQueryDefinition queryDef) {
            startField("customerAccessToken");

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerAccessTokenRenewPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerAccessTokenRenewPayload extends AbstractResponse<CustomerAccessTokenRenewPayload> {
        public CustomerAccessTokenRenewPayload() {
        }

        public CustomerAccessTokenRenewPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customerAccessToken": {
                        CustomerAccessToken optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerAccessToken(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAccessTokenRenewPayload";
        }

        /**
        * The renewed customer access token object.
        */

        public CustomerAccessToken getCustomerAccessToken() {
            return (CustomerAccessToken) get("customerAccessToken");
        }

        public CustomerAccessTokenRenewPayload setCustomerAccessToken(CustomerAccessToken arg) {
            optimisticData.put(getKey("customerAccessToken"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAccessTokenRenewPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customerAccessToken": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerActivateInput implements Serializable {
        private String activationToken;

        private String password;

        public CustomerActivateInput(String activationToken, String password) {
            this.activationToken = activationToken;

            this.password = password;
        }

        public String getActivationToken() {
            return activationToken;
        }

        public CustomerActivateInput setActivationToken(String activationToken) {
            this.activationToken = activationToken;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public CustomerActivateInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("activationToken:");
            Query.appendQuotedString(_queryBuilder, activationToken.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("password:");
            Query.appendQuotedString(_queryBuilder, password.toString());

            _queryBuilder.append('}');
        }
    }

    public interface CustomerActivatePayloadQueryDefinition {
        void define(CustomerActivatePayloadQuery _queryBuilder);
    }

    public static class CustomerActivatePayloadQuery extends Query<CustomerActivatePayloadQuery> {
        CustomerActivatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The customer object.
        */
        public CustomerActivatePayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerActivatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerActivatePayload extends AbstractResponse<CustomerActivatePayload> {
        public CustomerActivatePayload() {
        }

        public CustomerActivatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerActivatePayload";
        }

        /**
        * The customer object.
        */

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerActivatePayload setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerActivatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customer": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerAddressCreatePayloadQueryDefinition {
        void define(CustomerAddressCreatePayloadQuery _queryBuilder);
    }

    public static class CustomerAddressCreatePayloadQuery extends Query<CustomerAddressCreatePayloadQuery> {
        CustomerAddressCreatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The new customer address object.
        */
        public CustomerAddressCreatePayloadQuery customerAddress(MailingAddressQueryDefinition queryDef) {
            startField("customerAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerAddressCreatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerAddressCreatePayload extends AbstractResponse<CustomerAddressCreatePayload> {
        public CustomerAddressCreatePayload() {
        }

        public CustomerAddressCreatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customerAddress": {
                        MailingAddress optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new MailingAddress(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAddressCreatePayload";
        }

        /**
        * The new customer address object.
        */

        public MailingAddress getCustomerAddress() {
            return (MailingAddress) get("customerAddress");
        }

        public CustomerAddressCreatePayload setCustomerAddress(MailingAddress arg) {
            optimisticData.put(getKey("customerAddress"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAddressCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customerAddress": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerAddressDeletePayloadQueryDefinition {
        void define(CustomerAddressDeletePayloadQuery _queryBuilder);
    }

    public static class CustomerAddressDeletePayloadQuery extends Query<CustomerAddressDeletePayloadQuery> {
        CustomerAddressDeletePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * ID of the deleted customer address.
        */
        public CustomerAddressDeletePayloadQuery deletedCustomerAddressId() {
            startField("deletedCustomerAddressId");

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerAddressDeletePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerAddressDeletePayload extends AbstractResponse<CustomerAddressDeletePayload> {
        public CustomerAddressDeletePayload() {
        }

        public CustomerAddressDeletePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "deletedCustomerAddressId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAddressDeletePayload";
        }

        /**
        * ID of the deleted customer address.
        */

        public String getDeletedCustomerAddressId() {
            return (String) get("deletedCustomerAddressId");
        }

        public CustomerAddressDeletePayload setDeletedCustomerAddressId(String arg) {
            optimisticData.put(getKey("deletedCustomerAddressId"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAddressDeletePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "deletedCustomerAddressId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerAddressUpdatePayloadQueryDefinition {
        void define(CustomerAddressUpdatePayloadQuery _queryBuilder);
    }

    public static class CustomerAddressUpdatePayloadQuery extends Query<CustomerAddressUpdatePayloadQuery> {
        CustomerAddressUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The customer’s updated mailing address.
        */
        public CustomerAddressUpdatePayloadQuery customerAddress(MailingAddressQueryDefinition queryDef) {
            startField("customerAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerAddressUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerAddressUpdatePayload extends AbstractResponse<CustomerAddressUpdatePayload> {
        public CustomerAddressUpdatePayload() {
        }

        public CustomerAddressUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customerAddress": {
                        MailingAddress optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new MailingAddress(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerAddressUpdatePayload";
        }

        /**
        * The customer’s updated mailing address.
        */

        public MailingAddress getCustomerAddress() {
            return (MailingAddress) get("customerAddress");
        }

        public CustomerAddressUpdatePayload setCustomerAddress(MailingAddress arg) {
            optimisticData.put(getKey("customerAddress"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAddressUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customerAddress": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerCreateInput implements Serializable {
        private String email;

        private String password;

        private String firstName;

        private String lastName;

        private Boolean acceptsMarketing;

        public CustomerCreateInput(String email, String password) {
            this.email = email;

            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public CustomerCreateInput setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public CustomerCreateInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getFirstName() {
            return firstName;
        }

        public CustomerCreateInput setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public String getLastName() {
            return lastName;
        }

        public CustomerCreateInput setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Boolean getAcceptsMarketing() {
            return acceptsMarketing;
        }

        public CustomerCreateInput setAcceptsMarketing(Boolean acceptsMarketing) {
            this.acceptsMarketing = acceptsMarketing;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("email:");
            Query.appendQuotedString(_queryBuilder, email.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("password:");
            Query.appendQuotedString(_queryBuilder, password.toString());

            if (firstName != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("firstName:");
                Query.appendQuotedString(_queryBuilder, firstName.toString());
            }

            if (lastName != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("lastName:");
                Query.appendQuotedString(_queryBuilder, lastName.toString());
            }

            if (acceptsMarketing != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("acceptsMarketing:");
                _queryBuilder.append(acceptsMarketing);
            }

            _queryBuilder.append('}');
        }
    }

    public interface CustomerCreatePayloadQueryDefinition {
        void define(CustomerCreatePayloadQuery _queryBuilder);
    }

    public static class CustomerCreatePayloadQuery extends Query<CustomerCreatePayloadQuery> {
        CustomerCreatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The created customer object.
        */
        public CustomerCreatePayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerCreatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerCreatePayload extends AbstractResponse<CustomerCreatePayload> {
        public CustomerCreatePayload() {
        }

        public CustomerCreatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerCreatePayload";
        }

        /**
        * The created customer object.
        */

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerCreatePayload setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customer": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CustomerRecoverPayloadQueryDefinition {
        void define(CustomerRecoverPayloadQuery _queryBuilder);
    }

    public static class CustomerRecoverPayloadQuery extends Query<CustomerRecoverPayloadQuery> {
        CustomerRecoverPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerRecoverPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerRecoverPayload extends AbstractResponse<CustomerRecoverPayload> {
        public CustomerRecoverPayload() {
        }

        public CustomerRecoverPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerRecoverPayload";
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerRecoverPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerResetInput implements Serializable {
        private String resetToken;

        private String password;

        public CustomerResetInput(String resetToken, String password) {
            this.resetToken = resetToken;

            this.password = password;
        }

        public String getResetToken() {
            return resetToken;
        }

        public CustomerResetInput setResetToken(String resetToken) {
            this.resetToken = resetToken;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public CustomerResetInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("resetToken:");
            Query.appendQuotedString(_queryBuilder, resetToken.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("password:");
            Query.appendQuotedString(_queryBuilder, password.toString());

            _queryBuilder.append('}');
        }
    }

    public interface CustomerResetPayloadQueryDefinition {
        void define(CustomerResetPayloadQuery _queryBuilder);
    }

    public static class CustomerResetPayloadQuery extends Query<CustomerResetPayloadQuery> {
        CustomerResetPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The customer object which was reset.
        */
        public CustomerResetPayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerResetPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerResetPayload extends AbstractResponse<CustomerResetPayload> {
        public CustomerResetPayload() {
        }

        public CustomerResetPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerResetPayload";
        }

        /**
        * The customer object which was reset.
        */

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerResetPayload setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerResetPayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customer": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerUpdateInput implements Serializable {
        private String firstName;

        private String lastName;

        private String email;

        private String password;

        private Boolean acceptsMarketing;

        public String getFirstName() {
            return firstName;
        }

        public CustomerUpdateInput setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public String getLastName() {
            return lastName;
        }

        public CustomerUpdateInput setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public CustomerUpdateInput setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public CustomerUpdateInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public Boolean getAcceptsMarketing() {
            return acceptsMarketing;
        }

        public CustomerUpdateInput setAcceptsMarketing(Boolean acceptsMarketing) {
            this.acceptsMarketing = acceptsMarketing;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            if (firstName != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("firstName:");
                Query.appendQuotedString(_queryBuilder, firstName.toString());
            }

            if (lastName != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("lastName:");
                Query.appendQuotedString(_queryBuilder, lastName.toString());
            }

            if (email != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("email:");
                Query.appendQuotedString(_queryBuilder, email.toString());
            }

            if (password != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("password:");
                Query.appendQuotedString(_queryBuilder, password.toString());
            }

            if (acceptsMarketing != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("acceptsMarketing:");
                _queryBuilder.append(acceptsMarketing);
            }

            _queryBuilder.append('}');
        }
    }

    public interface CustomerUpdatePayloadQueryDefinition {
        void define(CustomerUpdatePayloadQuery _queryBuilder);
    }

    public static class CustomerUpdatePayloadQuery extends Query<CustomerUpdatePayloadQuery> {
        CustomerUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The updated customer object.
        */
        public CustomerUpdatePayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */
        public CustomerUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CustomerUpdatePayload extends AbstractResponse<CustomerUpdatePayload> {
        public CustomerUpdatePayload() {
        }

        public CustomerUpdatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "userErrors": {
                        List<UserError> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new UserError(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "CustomerUpdatePayload";
        }

        /**
        * The updated customer object.
        */

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerUpdatePayload setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        /**
        * List of errors that occurred executing the mutation.
        */

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put(getKey("userErrors"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customer": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface DomainQueryDefinition {
        void define(DomainQuery _queryBuilder);
    }

    /**
    * Represents a web address.
    */
    public static class DomainQuery extends Query<DomainQuery> {
        DomainQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The host name of the domain (eg: 
        * <ul>
        *  `example.com`).
        * </ul>
        */
        public DomainQuery host() {
            startField("host");

            return this;
        }

        /**
        * Whether SSL is enabled or not.
        */
        public DomainQuery sslEnabled() {
            startField("sslEnabled");

            return this;
        }

        /**
        * The URL of the domain (eg: 
        * <ul>
        * //example.com`).
        * </ul>
        */
        public DomainQuery url() {
            startField("url");

            return this;
        }
    }

    /**
    * Represents a web address.
    */
    public static class Domain extends AbstractResponse<Domain> {
        public Domain() {
        }

        public Domain(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "host": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "sslEnabled": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "url": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "Domain";
        }

        /**
        * The host name of the domain (eg: 
        * <ul>
        *  `example.com`).
        * </ul>
        */

        public String getHost() {
            return (String) get("host");
        }

        public Domain setHost(String arg) {
            optimisticData.put(getKey("host"), arg);
            return this;
        }

        /**
        * Whether SSL is enabled or not.
        */

        public Boolean getSslEnabled() {
            return (Boolean) get("sslEnabled");
        }

        public Domain setSslEnabled(Boolean arg) {
            optimisticData.put(getKey("sslEnabled"), arg);
            return this;
        }

        /**
        * The URL of the domain (eg: 
        * <ul>
        * //example.com`).
        * </ul>
        */

        public String getUrl() {
            return (String) get("url");
        }

        public Domain setUrl(String arg) {
            optimisticData.put(getKey("url"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "host": return false;

                case "sslEnabled": return false;

                case "url": return false;

                default: return false;
            }
        }
    }

    public interface ImageQueryDefinition {
        void define(ImageQuery _queryBuilder);
    }

    /**
    * Represents an image resource.
    */
    public static class ImageQuery extends Query<ImageQuery> {
        ImageQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A word or phrase to share the nature or contents of an image.
        */
        public ImageQuery altText() {
            startField("altText");

            return this;
        }

        /**
        * A unique identifier for the image.
        */
        public ImageQuery id() {
            startField("id");

            return this;
        }

        /**
        * The location of the image as a URL.
        */
        public ImageQuery src() {
            startField("src");

            return this;
        }
    }

    /**
    * Represents an image resource.
    */
    public static class Image extends AbstractResponse<Image> {
        public Image() {
        }

        public Image(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "altText": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "id": {
                        ID optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ID(jsonAsString(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "src": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "Image";
        }

        /**
        * A word or phrase to share the nature or contents of an image.
        */

        public String getAltText() {
            return (String) get("altText");
        }

        public Image setAltText(String arg) {
            optimisticData.put(getKey("altText"), arg);
            return this;
        }

        /**
        * A unique identifier for the image.
        */

        public ID getId() {
            return (ID) get("id");
        }

        public Image setId(ID arg) {
            optimisticData.put(getKey("id"), arg);
            return this;
        }

        /**
        * The location of the image as a URL.
        */

        public String getSrc() {
            return (String) get("src");
        }

        public Image setSrc(String arg) {
            optimisticData.put(getKey("src"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "altText": return false;

                case "id": return false;

                case "src": return false;

                default: return false;
            }
        }
    }

    public interface ImageConnectionQueryDefinition {
        void define(ImageConnectionQuery _queryBuilder);
    }

    public static class ImageConnectionQuery extends Query<ImageConnectionQuery> {
        ImageConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public ImageConnectionQuery edges(ImageEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new ImageEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public ImageConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ImageConnection extends AbstractResponse<ImageConnection> {
        public ImageConnection() {
        }

        public ImageConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<ImageEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new ImageEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "ImageConnection";
        }

        /**
        * A list of edges.
        */

        public List<ImageEdge> getEdges() {
            return (List<ImageEdge>) get("edges");
        }

        public ImageConnection setEdges(List<ImageEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public ImageConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface ImageEdgeQueryDefinition {
        void define(ImageEdgeQuery _queryBuilder);
    }

    public static class ImageEdgeQuery extends Query<ImageEdgeQuery> {
        ImageEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public ImageEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        public ImageEdgeQuery node(ImageQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new ImageQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ImageEdge extends AbstractResponse<ImageEdge> {
        public ImageEdge() {
        }

        public ImageEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new Image(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "ImageEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public ImageEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        public Image getNode() {
            return (Image) get("node");
        }

        public ImageEdge setNode(Image arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public interface MailingAddressQueryDefinition {
        void define(MailingAddressQuery _queryBuilder);
    }

    /**
    * Represents a mailing address for customers and shipping.
    */
    public static class MailingAddressQuery extends Query<MailingAddressQuery> {
        MailingAddressQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public MailingAddressQuery address1() {
            startField("address1");

            return this;
        }

        public MailingAddressQuery address2() {
            startField("address2");

            return this;
        }

        public MailingAddressQuery city() {
            startField("city");

            return this;
        }

        public MailingAddressQuery company() {
            startField("company");

            return this;
        }

        public MailingAddressQuery country() {
            startField("country");

            return this;
        }

        public MailingAddressQuery countryCode() {
            startField("countryCode");

            return this;
        }

        public MailingAddressQuery firstName() {
            startField("firstName");

            return this;
        }

        public class FormattedArguments extends Arguments {
            FormattedArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            public FormattedArguments withName(Boolean value) {
                if (value != null) {
                    startArgument("withName");
                    _queryBuilder.append(value);
                }
                return this;
            }

            public FormattedArguments withCompany(Boolean value) {
                if (value != null) {
                    startArgument("withCompany");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface FormattedArgumentsDefinition {
            void define(FormattedArguments args);
        }

        public MailingAddressQuery formatted() {
            return formatted(args -> {});
        }

        public MailingAddressQuery formatted(FormattedArgumentsDefinition argsDef) {
            startField("formatted");

            FormattedArguments args = new FormattedArguments(_queryBuilder);
            argsDef.define(args);
            FormattedArguments.end(args);

            return this;
        }

        public MailingAddressQuery lastName() {
            startField("lastName");

            return this;
        }

        public MailingAddressQuery latitude() {
            startField("latitude");

            return this;
        }

        public MailingAddressQuery longitude() {
            startField("longitude");

            return this;
        }

        public MailingAddressQuery name() {
            startField("name");

            return this;
        }

        public MailingAddressQuery phone() {
            startField("phone");

            return this;
        }

        public MailingAddressQuery province() {
            startField("province");

            return this;
        }

        public MailingAddressQuery provinceCode() {
            startField("provinceCode");

            return this;
        }

        public MailingAddressQuery zip() {
            startField("zip");

            return this;
        }
    }

    /**
    * Represents a mailing address for customers and shipping.
    */
    public static class MailingAddress extends AbstractResponse<MailingAddress> implements Node {
        public MailingAddress() {
        }

        public MailingAddress(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "address1": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "address2": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "city": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "company": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "country": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "countryCode": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "firstName": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "formatted": {
                        List<String> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(jsonAsString(element1, key));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "lastName": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "latitude": {
                        Double optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsDouble(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "longitude": {
                        Double optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsDouble(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "name": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "phone": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "province": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "provinceCode": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "zip": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public MailingAddress(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "MailingAddress";
        }

        public String getAddress1() {
            return (String) get("address1");
        }

        public MailingAddress setAddress1(String arg) {
            optimisticData.put(getKey("address1"), arg);
            return this;
        }

        public String getAddress2() {
            return (String) get("address2");
        }

        public MailingAddress setAddress2(String arg) {
            optimisticData.put(getKey("address2"), arg);
            return this;
        }

        public String getCity() {
            return (String) get("city");
        }

        public MailingAddress setCity(String arg) {
            optimisticData.put(getKey("city"), arg);
            return this;
        }

        public String getCompany() {
            return (String) get("company");
        }

        public MailingAddress setCompany(String arg) {
            optimisticData.put(getKey("company"), arg);
            return this;
        }

        public String getCountry() {
            return (String) get("country");
        }

        public MailingAddress setCountry(String arg) {
            optimisticData.put(getKey("country"), arg);
            return this;
        }

        public String getCountryCode() {
            return (String) get("countryCode");
        }

        public MailingAddress setCountryCode(String arg) {
            optimisticData.put(getKey("countryCode"), arg);
            return this;
        }

        public String getFirstName() {
            return (String) get("firstName");
        }

        public MailingAddress setFirstName(String arg) {
            optimisticData.put(getKey("firstName"), arg);
            return this;
        }

        public List<String> getFormatted() {
            return (List<String>) get("formatted");
        }

        public MailingAddress setFormatted(List<String> arg) {
            optimisticData.put(getKey("formatted"), arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public String getLastName() {
            return (String) get("lastName");
        }

        public MailingAddress setLastName(String arg) {
            optimisticData.put(getKey("lastName"), arg);
            return this;
        }

        public Double getLatitude() {
            return (Double) get("latitude");
        }

        public MailingAddress setLatitude(Double arg) {
            optimisticData.put(getKey("latitude"), arg);
            return this;
        }

        public Double getLongitude() {
            return (Double) get("longitude");
        }

        public MailingAddress setLongitude(Double arg) {
            optimisticData.put(getKey("longitude"), arg);
            return this;
        }

        public String getName() {
            return (String) get("name");
        }

        public MailingAddress setName(String arg) {
            optimisticData.put(getKey("name"), arg);
            return this;
        }

        public String getPhone() {
            return (String) get("phone");
        }

        public MailingAddress setPhone(String arg) {
            optimisticData.put(getKey("phone"), arg);
            return this;
        }

        public String getProvince() {
            return (String) get("province");
        }

        public MailingAddress setProvince(String arg) {
            optimisticData.put(getKey("province"), arg);
            return this;
        }

        public String getProvinceCode() {
            return (String) get("provinceCode");
        }

        public MailingAddress setProvinceCode(String arg) {
            optimisticData.put(getKey("provinceCode"), arg);
            return this;
        }

        public String getZip() {
            return (String) get("zip");
        }

        public MailingAddress setZip(String arg) {
            optimisticData.put(getKey("zip"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "address1": return false;

                case "address2": return false;

                case "city": return false;

                case "company": return false;

                case "country": return false;

                case "countryCode": return false;

                case "firstName": return false;

                case "formatted": return false;

                case "id": return false;

                case "lastName": return false;

                case "latitude": return false;

                case "longitude": return false;

                case "name": return false;

                case "phone": return false;

                case "province": return false;

                case "provinceCode": return false;

                case "zip": return false;

                default: return false;
            }
        }
    }

    public interface MailingAddressConnectionQueryDefinition {
        void define(MailingAddressConnectionQuery _queryBuilder);
    }

    public static class MailingAddressConnectionQuery extends Query<MailingAddressConnectionQuery> {
        MailingAddressConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public MailingAddressConnectionQuery edges(MailingAddressEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public MailingAddressConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class MailingAddressConnection extends AbstractResponse<MailingAddressConnection> {
        public MailingAddressConnection() {
        }

        public MailingAddressConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<MailingAddressEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new MailingAddressEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "MailingAddressConnection";
        }

        /**
        * A list of edges.
        */

        public List<MailingAddressEdge> getEdges() {
            return (List<MailingAddressEdge>) get("edges");
        }

        public MailingAddressConnection setEdges(List<MailingAddressEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public MailingAddressConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface MailingAddressEdgeQueryDefinition {
        void define(MailingAddressEdgeQuery _queryBuilder);
    }

    public static class MailingAddressEdgeQuery extends Query<MailingAddressEdgeQuery> {
        MailingAddressEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public MailingAddressEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        public MailingAddressEdgeQuery node(MailingAddressQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class MailingAddressEdge extends AbstractResponse<MailingAddressEdge> {
        public MailingAddressEdge() {
        }

        public MailingAddressEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new MailingAddress(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "MailingAddressEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public MailingAddressEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        public MailingAddress getNode() {
            return (MailingAddress) get("node");
        }

        public MailingAddressEdge setNode(MailingAddress arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public static class MailingAddressInput implements Serializable {
        private String address1;

        private String address2;

        private String city;

        private String company;

        private String country;

        private String firstName;

        private String lastName;

        private String phone;

        private String province;

        private String zip;

        public String getAddress1() {
            return address1;
        }

        public MailingAddressInput setAddress1(String address1) {
            this.address1 = address1;
            return this;
        }

        public String getAddress2() {
            return address2;
        }

        public MailingAddressInput setAddress2(String address2) {
            this.address2 = address2;
            return this;
        }

        public String getCity() {
            return city;
        }

        public MailingAddressInput setCity(String city) {
            this.city = city;
            return this;
        }

        public String getCompany() {
            return company;
        }

        public MailingAddressInput setCompany(String company) {
            this.company = company;
            return this;
        }

        public String getCountry() {
            return country;
        }

        public MailingAddressInput setCountry(String country) {
            this.country = country;
            return this;
        }

        public String getFirstName() {
            return firstName;
        }

        public MailingAddressInput setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public String getLastName() {
            return lastName;
        }

        public MailingAddressInput setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public String getPhone() {
            return phone;
        }

        public MailingAddressInput setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public String getProvince() {
            return province;
        }

        public MailingAddressInput setProvince(String province) {
            this.province = province;
            return this;
        }

        public String getZip() {
            return zip;
        }

        public MailingAddressInput setZip(String zip) {
            this.zip = zip;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            if (address1 != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("address1:");
                Query.appendQuotedString(_queryBuilder, address1.toString());
            }

            if (address2 != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("address2:");
                Query.appendQuotedString(_queryBuilder, address2.toString());
            }

            if (city != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("city:");
                Query.appendQuotedString(_queryBuilder, city.toString());
            }

            if (company != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("company:");
                Query.appendQuotedString(_queryBuilder, company.toString());
            }

            if (country != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("country:");
                Query.appendQuotedString(_queryBuilder, country.toString());
            }

            if (firstName != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("firstName:");
                Query.appendQuotedString(_queryBuilder, firstName.toString());
            }

            if (lastName != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("lastName:");
                Query.appendQuotedString(_queryBuilder, lastName.toString());
            }

            if (phone != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("phone:");
                Query.appendQuotedString(_queryBuilder, phone.toString());
            }

            if (province != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("province:");
                Query.appendQuotedString(_queryBuilder, province.toString());
            }

            if (zip != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("zip:");
                Query.appendQuotedString(_queryBuilder, zip.toString());
            }

            _queryBuilder.append('}');
        }
    }

    public interface MutationQueryDefinition {
        void define(MutationQuery _queryBuilder);
    }

    /**
    * The schema’s entry-point for mutations. This acts as the public, top-level API from which all
    * mutation queries must start.
    */
    public static class MutationQuery extends Query<MutationQuery> {
        MutationQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * Updates the attributes of a checkout.
        */
        public MutationQuery checkoutAttributesUpdate(ID checkoutId, CheckoutAttributesUpdateInput input, CheckoutAttributesUpdatePayloadQueryDefinition queryDef) {
            startField("checkoutAttributesUpdate");

            _queryBuilder.append("(checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(",input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutAttributesUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery checkoutCompleteFree(ID checkoutId, CheckoutCompleteFreePayloadQueryDefinition queryDef) {
            startField("checkoutCompleteFree");

            _queryBuilder.append("(checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutCompleteFreePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Completes a checkout using a credit card token from Shopify's Vault.
        */
        public MutationQuery checkoutCompleteWithCreditCard(ID checkoutId, CreditCardPaymentInput payment, CheckoutCompleteWithCreditCardPayloadQueryDefinition queryDef) {
            startField("checkoutCompleteWithCreditCard");

            _queryBuilder.append("(checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(",payment:");
            payment.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutCompleteWithCreditCardPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Completes a checkout with a tokenized payment.
        */
        public MutationQuery checkoutCompleteWithTokenizedPayment(ID checkoutId, TokenizedPaymentInput payment, CheckoutCompleteWithTokenizedPaymentPayloadQueryDefinition queryDef) {
            startField("checkoutCompleteWithTokenizedPayment");

            _queryBuilder.append("(checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(",payment:");
            payment.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutCompleteWithTokenizedPaymentPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Creates a new checkout.
        */
        public MutationQuery checkoutCreate(CheckoutCreateInput input, CheckoutCreatePayloadQueryDefinition queryDef) {
            startField("checkoutCreate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutCreatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Associates a customer to the checkout.
        */
        public MutationQuery checkoutCustomerAssociate(ID checkoutId, String customerAccessToken, CheckoutCustomerAssociatePayloadQueryDefinition queryDef) {
            startField("checkoutCustomerAssociate");

            _queryBuilder.append("(checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(",customerAccessToken:");
            Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutCustomerAssociatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Disassociates the current checkout customer from the checkout.
        */
        public MutationQuery checkoutCustomerDisassociate(ID checkoutId, CheckoutCustomerDisassociatePayloadQueryDefinition queryDef) {
            startField("checkoutCustomerDisassociate");

            _queryBuilder.append("(checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutCustomerDisassociatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Updates the email on an existing checkout.
        */
        public MutationQuery checkoutEmailUpdate(ID checkoutId, String email, CheckoutEmailUpdatePayloadQueryDefinition queryDef) {
            startField("checkoutEmailUpdate");

            _queryBuilder.append("(checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(",email:");
            Query.appendQuotedString(_queryBuilder, email.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutEmailUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Applies a gift card to an existing checkout using a gift card code.
        */
        public MutationQuery checkoutGiftCardApply(String giftCardCode, ID checkoutId, CheckoutGiftCardApplyPayloadQueryDefinition queryDef) {
            startField("checkoutGiftCardApply");

            _queryBuilder.append("(giftCardCode:");
            Query.appendQuotedString(_queryBuilder, giftCardCode.toString());

            _queryBuilder.append(",checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutGiftCardApplyPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Removes an applied gift card from the checkout.
        */
        public MutationQuery checkoutGiftCardRemove(ID appliedGiftCardId, ID checkoutId, CheckoutGiftCardRemovePayloadQueryDefinition queryDef) {
            startField("checkoutGiftCardRemove");

            _queryBuilder.append("(appliedGiftCardId:");
            Query.appendQuotedString(_queryBuilder, appliedGiftCardId.toString());

            _queryBuilder.append(",checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutGiftCardRemovePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Adds a list of line items to a checkout.
        */
        public MutationQuery checkoutLineItemsAdd(List<CheckoutLineItemInput> lineItems, ID checkoutId, CheckoutLineItemsAddPayloadQueryDefinition queryDef) {
            startField("checkoutLineItemsAdd");

            _queryBuilder.append("(lineItems:");
            _queryBuilder.append('[');

            String listSeperator1 = "";
            for (CheckoutLineItemInput item1 : lineItems) {
                _queryBuilder.append(listSeperator1);
                listSeperator1 = ",";
                item1.appendTo(_queryBuilder);
            }
            _queryBuilder.append(']');

            _queryBuilder.append(",checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutLineItemsAddPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Removes line items from an existing checkout
        */
        public MutationQuery checkoutLineItemsRemove(ID checkoutId, List<ID> lineItemIds, CheckoutLineItemsRemovePayloadQueryDefinition queryDef) {
            startField("checkoutLineItemsRemove");

            _queryBuilder.append("(checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(",lineItemIds:");
            _queryBuilder.append('[');

            String listSeperator1 = "";
            for (ID item1 : lineItemIds) {
                _queryBuilder.append(listSeperator1);
                listSeperator1 = ",";
                Query.appendQuotedString(_queryBuilder, item1.toString());
            }
            _queryBuilder.append(']');

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutLineItemsRemovePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Updates line items on a checkout.
        */
        public MutationQuery checkoutLineItemsUpdate(ID checkoutId, List<CheckoutLineItemUpdateInput> lineItems, CheckoutLineItemsUpdatePayloadQueryDefinition queryDef) {
            startField("checkoutLineItemsUpdate");

            _queryBuilder.append("(checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(",lineItems:");
            _queryBuilder.append('[');

            String listSeperator1 = "";
            for (CheckoutLineItemUpdateInput item1 : lineItems) {
                _queryBuilder.append(listSeperator1);
                listSeperator1 = ",";
                item1.appendTo(_queryBuilder);
            }
            _queryBuilder.append(']');

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutLineItemsUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Updates the shipping address of an existing checkout.
        */
        public MutationQuery checkoutShippingAddressUpdate(MailingAddressInput shippingAddress, ID checkoutId, CheckoutShippingAddressUpdatePayloadQueryDefinition queryDef) {
            startField("checkoutShippingAddressUpdate");

            _queryBuilder.append("(shippingAddress:");
            shippingAddress.appendTo(_queryBuilder);

            _queryBuilder.append(",checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutShippingAddressUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Updates the shipping lines on an existing checkout.
        */
        public MutationQuery checkoutShippingLineUpdate(ID checkoutId, String shippingRateHandle, CheckoutShippingLineUpdatePayloadQueryDefinition queryDef) {
            startField("checkoutShippingLineUpdate");

            _queryBuilder.append("(checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(",shippingRateHandle:");
            Query.appendQuotedString(_queryBuilder, shippingRateHandle.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutShippingLineUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Creates a customer access token. The customer access token is required to modify the customer object
        * in any way. 
        */
        public MutationQuery customerAccessTokenCreate(CustomerAccessTokenCreateInput input, CustomerAccessTokenCreatePayloadQueryDefinition queryDef) {
            startField("customerAccessTokenCreate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenCreatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Permanently destroys a customer access token.
        */
        public MutationQuery customerAccessTokenDelete(String customerAccessToken, CustomerAccessTokenDeletePayloadQueryDefinition queryDef) {
            startField("customerAccessTokenDelete");

            _queryBuilder.append("(customerAccessToken:");
            Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenDeletePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Renews a customer access token.
        */
        public MutationQuery customerAccessTokenRenew(String customerAccessToken, CustomerAccessTokenRenewPayloadQueryDefinition queryDef) {
            startField("customerAccessTokenRenew");

            _queryBuilder.append("(customerAccessToken:");
            Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenRenewPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Activates a customer.
        */
        public MutationQuery customerActivate(ID id, CustomerActivateInput input, CustomerActivatePayloadQueryDefinition queryDef) {
            startField("customerActivate");

            _queryBuilder.append("(id:");
            Query.appendQuotedString(_queryBuilder, id.toString());

            _queryBuilder.append(",input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerActivatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Creates a new address for a customer.
        */
        public MutationQuery customerAddressCreate(String customerAccessToken, MailingAddressInput address, CustomerAddressCreatePayloadQueryDefinition queryDef) {
            startField("customerAddressCreate");

            _queryBuilder.append("(customerAccessToken:");
            Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

            _queryBuilder.append(",address:");
            address.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerAddressCreatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Permanently deletes the address of an existing customer.
        */
        public MutationQuery customerAddressDelete(ID id, String customerAccessToken, CustomerAddressDeletePayloadQueryDefinition queryDef) {
            startField("customerAddressDelete");

            _queryBuilder.append("(id:");
            Query.appendQuotedString(_queryBuilder, id.toString());

            _queryBuilder.append(",customerAccessToken:");
            Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerAddressDeletePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Updates the address of an existing customer.
        */
        public MutationQuery customerAddressUpdate(String customerAccessToken, ID id, MailingAddressInput address, CustomerAddressUpdatePayloadQueryDefinition queryDef) {
            startField("customerAddressUpdate");

            _queryBuilder.append("(customerAccessToken:");
            Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

            _queryBuilder.append(",id:");
            Query.appendQuotedString(_queryBuilder, id.toString());

            _queryBuilder.append(",address:");
            address.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerAddressUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Creates a new customer.
        */
        public MutationQuery customerCreate(CustomerCreateInput input, CustomerCreatePayloadQueryDefinition queryDef) {
            startField("customerCreate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerCreatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Sends a reset password email to the customer, as the first step in the reset password process.
        */
        public MutationQuery customerRecover(String email, CustomerRecoverPayloadQueryDefinition queryDef) {
            startField("customerRecover");

            _queryBuilder.append("(email:");
            Query.appendQuotedString(_queryBuilder, email.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerRecoverPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Resets a customer’s password with a token received from `CustomerRecover`.
        */
        public MutationQuery customerReset(ID id, CustomerResetInput input, CustomerResetPayloadQueryDefinition queryDef) {
            startField("customerReset");

            _queryBuilder.append("(id:");
            Query.appendQuotedString(_queryBuilder, id.toString());

            _queryBuilder.append(",input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerResetPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Updates an existing customer.
        */
        public MutationQuery customerUpdate(String customerAccessToken, CustomerUpdateInput customer, CustomerUpdatePayloadQueryDefinition queryDef) {
            startField("customerUpdate");

            _queryBuilder.append("(customerAccessToken:");
            Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

            _queryBuilder.append(",customer:");
            customer.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public String toString() {
            return _queryBuilder.toString();
        }
    }

    /**
    * The schema’s entry-point for mutations. This acts as the public, top-level API from which all
    * mutation queries must start.
    */
    public static class Mutation extends AbstractResponse<Mutation> {
        public Mutation() {
        }

        public Mutation(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkoutAttributesUpdate": {
                        CheckoutAttributesUpdatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutAttributesUpdatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutCompleteFree": {
                        CheckoutCompleteFreePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutCompleteFreePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutCompleteWithCreditCard": {
                        CheckoutCompleteWithCreditCardPayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutCompleteWithCreditCardPayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutCompleteWithTokenizedPayment": {
                        CheckoutCompleteWithTokenizedPaymentPayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutCompleteWithTokenizedPaymentPayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutCreate": {
                        CheckoutCreatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutCreatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutCustomerAssociate": {
                        CheckoutCustomerAssociatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutCustomerAssociatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutCustomerDisassociate": {
                        CheckoutCustomerDisassociatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutCustomerDisassociatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutEmailUpdate": {
                        CheckoutEmailUpdatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutEmailUpdatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutGiftCardApply": {
                        CheckoutGiftCardApplyPayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutGiftCardApplyPayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutGiftCardRemove": {
                        CheckoutGiftCardRemovePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutGiftCardRemovePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutLineItemsAdd": {
                        CheckoutLineItemsAddPayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutLineItemsAddPayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutLineItemsRemove": {
                        CheckoutLineItemsRemovePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutLineItemsRemovePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutLineItemsUpdate": {
                        CheckoutLineItemsUpdatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutLineItemsUpdatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutShippingAddressUpdate": {
                        CheckoutShippingAddressUpdatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutShippingAddressUpdatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutShippingLineUpdate": {
                        CheckoutShippingLineUpdatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutShippingLineUpdatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerAccessTokenCreate": {
                        CustomerAccessTokenCreatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerAccessTokenCreatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerAccessTokenDelete": {
                        CustomerAccessTokenDeletePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerAccessTokenDeletePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerAccessTokenRenew": {
                        CustomerAccessTokenRenewPayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerAccessTokenRenewPayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerActivate": {
                        CustomerActivatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerActivatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerAddressCreate": {
                        CustomerAddressCreatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerAddressCreatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerAddressDelete": {
                        CustomerAddressDeletePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerAddressDeletePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerAddressUpdate": {
                        CustomerAddressUpdatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerAddressUpdatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerCreate": {
                        CustomerCreatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerCreatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerRecover": {
                        CustomerRecoverPayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerRecoverPayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerReset": {
                        CustomerResetPayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerResetPayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "customerUpdate": {
                        CustomerUpdatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CustomerUpdatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "Mutation";
        }

        /**
        * Updates the attributes of a checkout.
        */

        public CheckoutAttributesUpdatePayload getCheckoutAttributesUpdate() {
            return (CheckoutAttributesUpdatePayload) get("checkoutAttributesUpdate");
        }

        public Mutation setCheckoutAttributesUpdate(CheckoutAttributesUpdatePayload arg) {
            optimisticData.put(getKey("checkoutAttributesUpdate"), arg);
            return this;
        }

        public CheckoutCompleteFreePayload getCheckoutCompleteFree() {
            return (CheckoutCompleteFreePayload) get("checkoutCompleteFree");
        }

        public Mutation setCheckoutCompleteFree(CheckoutCompleteFreePayload arg) {
            optimisticData.put(getKey("checkoutCompleteFree"), arg);
            return this;
        }

        /**
        * Completes a checkout using a credit card token from Shopify's Vault.
        */

        public CheckoutCompleteWithCreditCardPayload getCheckoutCompleteWithCreditCard() {
            return (CheckoutCompleteWithCreditCardPayload) get("checkoutCompleteWithCreditCard");
        }

        public Mutation setCheckoutCompleteWithCreditCard(CheckoutCompleteWithCreditCardPayload arg) {
            optimisticData.put(getKey("checkoutCompleteWithCreditCard"), arg);
            return this;
        }

        /**
        * Completes a checkout with a tokenized payment.
        */

        public CheckoutCompleteWithTokenizedPaymentPayload getCheckoutCompleteWithTokenizedPayment() {
            return (CheckoutCompleteWithTokenizedPaymentPayload) get("checkoutCompleteWithTokenizedPayment");
        }

        public Mutation setCheckoutCompleteWithTokenizedPayment(CheckoutCompleteWithTokenizedPaymentPayload arg) {
            optimisticData.put(getKey("checkoutCompleteWithTokenizedPayment"), arg);
            return this;
        }

        /**
        * Creates a new checkout.
        */

        public CheckoutCreatePayload getCheckoutCreate() {
            return (CheckoutCreatePayload) get("checkoutCreate");
        }

        public Mutation setCheckoutCreate(CheckoutCreatePayload arg) {
            optimisticData.put(getKey("checkoutCreate"), arg);
            return this;
        }

        /**
        * Associates a customer to the checkout.
        */

        public CheckoutCustomerAssociatePayload getCheckoutCustomerAssociate() {
            return (CheckoutCustomerAssociatePayload) get("checkoutCustomerAssociate");
        }

        public Mutation setCheckoutCustomerAssociate(CheckoutCustomerAssociatePayload arg) {
            optimisticData.put(getKey("checkoutCustomerAssociate"), arg);
            return this;
        }

        /**
        * Disassociates the current checkout customer from the checkout.
        */

        public CheckoutCustomerDisassociatePayload getCheckoutCustomerDisassociate() {
            return (CheckoutCustomerDisassociatePayload) get("checkoutCustomerDisassociate");
        }

        public Mutation setCheckoutCustomerDisassociate(CheckoutCustomerDisassociatePayload arg) {
            optimisticData.put(getKey("checkoutCustomerDisassociate"), arg);
            return this;
        }

        /**
        * Updates the email on an existing checkout.
        */

        public CheckoutEmailUpdatePayload getCheckoutEmailUpdate() {
            return (CheckoutEmailUpdatePayload) get("checkoutEmailUpdate");
        }

        public Mutation setCheckoutEmailUpdate(CheckoutEmailUpdatePayload arg) {
            optimisticData.put(getKey("checkoutEmailUpdate"), arg);
            return this;
        }

        /**
        * Applies a gift card to an existing checkout using a gift card code.
        */

        public CheckoutGiftCardApplyPayload getCheckoutGiftCardApply() {
            return (CheckoutGiftCardApplyPayload) get("checkoutGiftCardApply");
        }

        public Mutation setCheckoutGiftCardApply(CheckoutGiftCardApplyPayload arg) {
            optimisticData.put(getKey("checkoutGiftCardApply"), arg);
            return this;
        }

        /**
        * Removes an applied gift card from the checkout.
        */

        public CheckoutGiftCardRemovePayload getCheckoutGiftCardRemove() {
            return (CheckoutGiftCardRemovePayload) get("checkoutGiftCardRemove");
        }

        public Mutation setCheckoutGiftCardRemove(CheckoutGiftCardRemovePayload arg) {
            optimisticData.put(getKey("checkoutGiftCardRemove"), arg);
            return this;
        }

        /**
        * Adds a list of line items to a checkout.
        */

        public CheckoutLineItemsAddPayload getCheckoutLineItemsAdd() {
            return (CheckoutLineItemsAddPayload) get("checkoutLineItemsAdd");
        }

        public Mutation setCheckoutLineItemsAdd(CheckoutLineItemsAddPayload arg) {
            optimisticData.put(getKey("checkoutLineItemsAdd"), arg);
            return this;
        }

        /**
        * Removes line items from an existing checkout
        */

        public CheckoutLineItemsRemovePayload getCheckoutLineItemsRemove() {
            return (CheckoutLineItemsRemovePayload) get("checkoutLineItemsRemove");
        }

        public Mutation setCheckoutLineItemsRemove(CheckoutLineItemsRemovePayload arg) {
            optimisticData.put(getKey("checkoutLineItemsRemove"), arg);
            return this;
        }

        /**
        * Updates line items on a checkout.
        */

        public CheckoutLineItemsUpdatePayload getCheckoutLineItemsUpdate() {
            return (CheckoutLineItemsUpdatePayload) get("checkoutLineItemsUpdate");
        }

        public Mutation setCheckoutLineItemsUpdate(CheckoutLineItemsUpdatePayload arg) {
            optimisticData.put(getKey("checkoutLineItemsUpdate"), arg);
            return this;
        }

        /**
        * Updates the shipping address of an existing checkout.
        */

        public CheckoutShippingAddressUpdatePayload getCheckoutShippingAddressUpdate() {
            return (CheckoutShippingAddressUpdatePayload) get("checkoutShippingAddressUpdate");
        }

        public Mutation setCheckoutShippingAddressUpdate(CheckoutShippingAddressUpdatePayload arg) {
            optimisticData.put(getKey("checkoutShippingAddressUpdate"), arg);
            return this;
        }

        /**
        * Updates the shipping lines on an existing checkout.
        */

        public CheckoutShippingLineUpdatePayload getCheckoutShippingLineUpdate() {
            return (CheckoutShippingLineUpdatePayload) get("checkoutShippingLineUpdate");
        }

        public Mutation setCheckoutShippingLineUpdate(CheckoutShippingLineUpdatePayload arg) {
            optimisticData.put(getKey("checkoutShippingLineUpdate"), arg);
            return this;
        }

        /**
        * Creates a customer access token. The customer access token is required to modify the customer object
        * in any way. 
        */

        public CustomerAccessTokenCreatePayload getCustomerAccessTokenCreate() {
            return (CustomerAccessTokenCreatePayload) get("customerAccessTokenCreate");
        }

        public Mutation setCustomerAccessTokenCreate(CustomerAccessTokenCreatePayload arg) {
            optimisticData.put(getKey("customerAccessTokenCreate"), arg);
            return this;
        }

        /**
        * Permanently destroys a customer access token.
        */

        public CustomerAccessTokenDeletePayload getCustomerAccessTokenDelete() {
            return (CustomerAccessTokenDeletePayload) get("customerAccessTokenDelete");
        }

        public Mutation setCustomerAccessTokenDelete(CustomerAccessTokenDeletePayload arg) {
            optimisticData.put(getKey("customerAccessTokenDelete"), arg);
            return this;
        }

        /**
        * Renews a customer access token.
        */

        public CustomerAccessTokenRenewPayload getCustomerAccessTokenRenew() {
            return (CustomerAccessTokenRenewPayload) get("customerAccessTokenRenew");
        }

        public Mutation setCustomerAccessTokenRenew(CustomerAccessTokenRenewPayload arg) {
            optimisticData.put(getKey("customerAccessTokenRenew"), arg);
            return this;
        }

        /**
        * Activates a customer.
        */

        public CustomerActivatePayload getCustomerActivate() {
            return (CustomerActivatePayload) get("customerActivate");
        }

        public Mutation setCustomerActivate(CustomerActivatePayload arg) {
            optimisticData.put(getKey("customerActivate"), arg);
            return this;
        }

        /**
        * Creates a new address for a customer.
        */

        public CustomerAddressCreatePayload getCustomerAddressCreate() {
            return (CustomerAddressCreatePayload) get("customerAddressCreate");
        }

        public Mutation setCustomerAddressCreate(CustomerAddressCreatePayload arg) {
            optimisticData.put(getKey("customerAddressCreate"), arg);
            return this;
        }

        /**
        * Permanently deletes the address of an existing customer.
        */

        public CustomerAddressDeletePayload getCustomerAddressDelete() {
            return (CustomerAddressDeletePayload) get("customerAddressDelete");
        }

        public Mutation setCustomerAddressDelete(CustomerAddressDeletePayload arg) {
            optimisticData.put(getKey("customerAddressDelete"), arg);
            return this;
        }

        /**
        * Updates the address of an existing customer.
        */

        public CustomerAddressUpdatePayload getCustomerAddressUpdate() {
            return (CustomerAddressUpdatePayload) get("customerAddressUpdate");
        }

        public Mutation setCustomerAddressUpdate(CustomerAddressUpdatePayload arg) {
            optimisticData.put(getKey("customerAddressUpdate"), arg);
            return this;
        }

        /**
        * Creates a new customer.
        */

        public CustomerCreatePayload getCustomerCreate() {
            return (CustomerCreatePayload) get("customerCreate");
        }

        public Mutation setCustomerCreate(CustomerCreatePayload arg) {
            optimisticData.put(getKey("customerCreate"), arg);
            return this;
        }

        /**
        * Sends a reset password email to the customer, as the first step in the reset password process.
        */

        public CustomerRecoverPayload getCustomerRecover() {
            return (CustomerRecoverPayload) get("customerRecover");
        }

        public Mutation setCustomerRecover(CustomerRecoverPayload arg) {
            optimisticData.put(getKey("customerRecover"), arg);
            return this;
        }

        /**
        * Resets a customer’s password with a token received from `CustomerRecover`.
        */

        public CustomerResetPayload getCustomerReset() {
            return (CustomerResetPayload) get("customerReset");
        }

        public Mutation setCustomerReset(CustomerResetPayload arg) {
            optimisticData.put(getKey("customerReset"), arg);
            return this;
        }

        /**
        * Updates an existing customer.
        */

        public CustomerUpdatePayload getCustomerUpdate() {
            return (CustomerUpdatePayload) get("customerUpdate");
        }

        public Mutation setCustomerUpdate(CustomerUpdatePayload arg) {
            optimisticData.put(getKey("customerUpdate"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "checkoutAttributesUpdate": return true;

                case "checkoutCompleteFree": return true;

                case "checkoutCompleteWithCreditCard": return true;

                case "checkoutCompleteWithTokenizedPayment": return true;

                case "checkoutCreate": return true;

                case "checkoutCustomerAssociate": return true;

                case "checkoutCustomerDisassociate": return true;

                case "checkoutEmailUpdate": return true;

                case "checkoutGiftCardApply": return true;

                case "checkoutGiftCardRemove": return true;

                case "checkoutLineItemsAdd": return true;

                case "checkoutLineItemsRemove": return true;

                case "checkoutLineItemsUpdate": return true;

                case "checkoutShippingAddressUpdate": return true;

                case "checkoutShippingLineUpdate": return true;

                case "customerAccessTokenCreate": return true;

                case "customerAccessTokenDelete": return true;

                case "customerAccessTokenRenew": return true;

                case "customerActivate": return true;

                case "customerAddressCreate": return true;

                case "customerAddressDelete": return true;

                case "customerAddressUpdate": return true;

                case "customerCreate": return true;

                case "customerRecover": return true;

                case "customerReset": return true;

                case "customerUpdate": return true;

                default: return false;
            }
        }
    }

    public interface NodeQueryDefinition {
        void define(NodeQuery _queryBuilder);
    }

    /**
    * An object with an ID to support global identification.
    */
    public static class NodeQuery extends Query<NodeQuery> {
        NodeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("__typename");
        }

        /**
        * Globally unique identifier.
        */
        public NodeQuery id() {
            startField("id");

            return this;
        }

        public NodeQuery onAppliedGiftCard(AppliedGiftCardQueryDefinition queryDef) {
            startInlineFragment("AppliedGiftCard");
            queryDef.define(new AppliedGiftCardQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onCheckout(CheckoutQueryDefinition queryDef) {
            startInlineFragment("Checkout");
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onCheckoutLineItem(CheckoutLineItemQueryDefinition queryDef) {
            startInlineFragment("CheckoutLineItem");
            queryDef.define(new CheckoutLineItemQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onCollection(CollectionQueryDefinition queryDef) {
            startInlineFragment("Collection");
            queryDef.define(new CollectionQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onMailingAddress(MailingAddressQueryDefinition queryDef) {
            startInlineFragment("MailingAddress");
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onOrder(OrderQueryDefinition queryDef) {
            startInlineFragment("Order");
            queryDef.define(new OrderQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onPayment(PaymentQueryDefinition queryDef) {
            startInlineFragment("Payment");
            queryDef.define(new PaymentQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onProduct(ProductQueryDefinition queryDef) {
            startInlineFragment("Product");
            queryDef.define(new ProductQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onProductOption(ProductOptionQueryDefinition queryDef) {
            startInlineFragment("ProductOption");
            queryDef.define(new ProductOptionQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onProductVariant(ProductVariantQueryDefinition queryDef) {
            startInlineFragment("ProductVariant");
            queryDef.define(new ProductVariantQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onShopPolicy(ShopPolicyQueryDefinition queryDef) {
            startInlineFragment("ShopPolicy");
            queryDef.define(new ShopPolicyQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }
    }

    public interface Node extends com.shopify.graphql.support.Node {
        String getGraphQlTypeName();

        ID getId();
    }

    /**
    * An object with an ID to support global identification.
    */
    public static class UnknownNode extends AbstractResponse<UnknownNode> implements Node {
        public UnknownNode() {
        }

        public UnknownNode(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public static Node create(JsonObject fields) throws SchemaViolationError {
            String typeName = fields.getAsJsonPrimitive("__typename").getAsString();
            switch (typeName) {
                case "AppliedGiftCard": {
                    return new AppliedGiftCard(fields);
                }

                case "Checkout": {
                    return new Checkout(fields);
                }

                case "CheckoutLineItem": {
                    return new CheckoutLineItem(fields);
                }

                case "Collection": {
                    return new Collection(fields);
                }

                case "MailingAddress": {
                    return new MailingAddress(fields);
                }

                case "Order": {
                    return new Order(fields);
                }

                case "Payment": {
                    return new Payment(fields);
                }

                case "Product": {
                    return new Product(fields);
                }

                case "ProductOption": {
                    return new ProductOption(fields);
                }

                case "ProductVariant": {
                    return new ProductVariant(fields);
                }

                case "ShopPolicy": {
                    return new ShopPolicy(fields);
                }

                default: {
                    return new UnknownNode(fields);
                }
            }
        }

        public String getGraphQlTypeName() {
            return (String) get("__typename");
        }

        /**
        * Globally unique identifier.
        */

        public ID getId() {
            return (ID) get("id");
        }

        public UnknownNode setId(ID arg) {
            optimisticData.put(getKey("id"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "id": return false;

                default: return false;
            }
        }
    }

    public interface OrderQueryDefinition {
        void define(OrderQuery _queryBuilder);
    }

    /**
    * An order is a customer’s completed request to purchase one or more products from a shop. An order is
    * created when a customer completes the checkout process, during which time they provides an email
    * address, billing address and payment information.
    */
    public static class OrderQuery extends Query<OrderQuery> {
        OrderQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        /**
        * The reason why the order was cancelled. If the order was not cancelled, this value is `null`. 
        */
        public OrderQuery cancelReason() {
            startField("cancelReason");

            return this;
        }

        /**
        * The date and time when the order was cancelled. If the order was not cancelled, this value is
        * `null.` 
        */
        public OrderQuery cancelledAt() {
            startField("cancelledAt");

            return this;
        }

        /**
        * The date and time when the order was created.
        */
        public OrderQuery createdAt() {
            startField("createdAt");

            return this;
        }

        /**
        * The code of the currency used for the payment.
        */
        public OrderQuery currencyCode() {
            startField("currencyCode");

            return this;
        }

        /**
        * The order’s URL for a customer.
        */
        public OrderQuery customerUrl() {
            startField("customerUrl");

            return this;
        }

        /**
        * Displays financial status of order payment processing.
        */
        public OrderQuery displayFinancialStatus() {
            startField("displayFinancialStatus");

            return this;
        }

        /**
        * Displays fulfillment status of the order.
        */
        public OrderQuery displayFulfillmentStatus() {
            startField("displayFulfillmentStatus");

            return this;
        }

        /**
        * The customer's email address.
        */
        public OrderQuery email() {
            startField("email");

            return this;
        }

        public class LineItemsArguments extends Arguments {
            LineItemsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, false);
            }

            public LineItemsArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            public LineItemsArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface LineItemsArgumentsDefinition {
            void define(LineItemsArguments args);
        }

        /**
        * List of the order’s line items.
        */
        public OrderQuery lineItems(int first, OrderLineItemConnectionQueryDefinition queryDef) {
            return lineItems(first, args -> {}, queryDef);
        }

        /**
        * List of the order’s line items.
        */
        public OrderQuery lineItems(int first, LineItemsArgumentsDefinition argsDef, OrderLineItemConnectionQueryDefinition queryDef) {
            startField("lineItems");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new LineItemsArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new OrderLineItemConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * A unique numeric identifier for the order for use by shop owner and customer.
        */
        public OrderQuery orderNumber() {
            startField("orderNumber");

            return this;
        }

        /**
        * The customer's phone number.
        */
        public OrderQuery phone() {
            startField("phone");

            return this;
        }

        /**
        * The date and time when the order was imported. This value can be set to dates in the past when
        * importing from other systems. If no value is provided, it will be auto-generated based on current
        * date and time. 
        */
        public OrderQuery processedAt() {
            startField("processedAt");

            return this;
        }

        /**
        * The address to where the order will be shipped.
        */
        public OrderQuery shippingAddress(MailingAddressQueryDefinition queryDef) {
            startField("shippingAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Price of the order before shipping and taxes.
        */
        public OrderQuery subtotalPrice() {
            startField("subtotalPrice");

            return this;
        }

        /**
        * The sum of all the prices of all the items in the order, taxes and discounts included (must be
        * positive).
        */
        public OrderQuery totalPrice() {
            startField("totalPrice");

            return this;
        }

        /**
        * The total amount that has been refunded.
        */
        public OrderQuery totalRefunded() {
            startField("totalRefunded");

            return this;
        }

        /**
        * The total cost of shipping.
        */
        public OrderQuery totalShippingPrice() {
            startField("totalShippingPrice");

            return this;
        }

        /**
        * The total cost of taxes.
        */
        public OrderQuery totalTax() {
            startField("totalTax");

            return this;
        }

        /**
        * The date and time when the order was last modified.
        */
        public OrderQuery updatedAt() {
            startField("updatedAt");

            return this;
        }
    }

    /**
    * An order is a customer’s completed request to purchase one or more products from a shop. An order is
    * created when a customer completes the checkout process, during which time they provides an email
    * address, billing address and payment information.
    */
    public static class Order extends AbstractResponse<Order> implements Node {
        public Order() {
        }

        public Order(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cancelReason": {
                        OrderCancelReason optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = OrderCancelReason.fromGraphQl(jsonAsString(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "cancelledAt": {
                        DateTime optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = Utils.parseDateTime(jsonAsString(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "createdAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "currencyCode": {
                        responseData.put(key, CurrencyCode.fromGraphQl(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "customerUrl": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "displayFinancialStatus": {
                        OrderDisplayFinancialStatus optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = OrderDisplayFinancialStatus.fromGraphQl(jsonAsString(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "displayFulfillmentStatus": {
                        responseData.put(key, OrderDisplayFulfillmentStatus.fromGraphQl(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "email": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "lineItems": {
                        responseData.put(key, new OrderLineItemConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "orderNumber": {
                        responseData.put(key, jsonAsInteger(field.getValue(), key));

                        break;
                    }

                    case "phone": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "processedAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "shippingAddress": {
                        MailingAddress optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new MailingAddress(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "subtotalPrice": {
                        BigDecimal optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new BigDecimal(jsonAsString(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "totalPrice": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "totalRefunded": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "totalShippingPrice": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "totalTax": {
                        BigDecimal optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new BigDecimal(jsonAsString(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "updatedAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public Order(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "Order";
        }

        /**
        * The reason why the order was cancelled. If the order was not cancelled, this value is `null`. 
        */

        public OrderCancelReason getCancelReason() {
            return (OrderCancelReason) get("cancelReason");
        }

        public Order setCancelReason(OrderCancelReason arg) {
            optimisticData.put(getKey("cancelReason"), arg);
            return this;
        }

        /**
        * The date and time when the order was cancelled. If the order was not cancelled, this value is
        * `null.` 
        */

        public DateTime getCancelledAt() {
            return (DateTime) get("cancelledAt");
        }

        public Order setCancelledAt(DateTime arg) {
            optimisticData.put(getKey("cancelledAt"), arg);
            return this;
        }

        /**
        * The date and time when the order was created.
        */

        public DateTime getCreatedAt() {
            return (DateTime) get("createdAt");
        }

        public Order setCreatedAt(DateTime arg) {
            optimisticData.put(getKey("createdAt"), arg);
            return this;
        }

        /**
        * The code of the currency used for the payment.
        */

        public CurrencyCode getCurrencyCode() {
            return (CurrencyCode) get("currencyCode");
        }

        public Order setCurrencyCode(CurrencyCode arg) {
            optimisticData.put(getKey("currencyCode"), arg);
            return this;
        }

        /**
        * The order’s URL for a customer.
        */

        public String getCustomerUrl() {
            return (String) get("customerUrl");
        }

        public Order setCustomerUrl(String arg) {
            optimisticData.put(getKey("customerUrl"), arg);
            return this;
        }

        /**
        * Displays financial status of order payment processing.
        */

        public OrderDisplayFinancialStatus getDisplayFinancialStatus() {
            return (OrderDisplayFinancialStatus) get("displayFinancialStatus");
        }

        public Order setDisplayFinancialStatus(OrderDisplayFinancialStatus arg) {
            optimisticData.put(getKey("displayFinancialStatus"), arg);
            return this;
        }

        /**
        * Displays fulfillment status of the order.
        */

        public OrderDisplayFulfillmentStatus getDisplayFulfillmentStatus() {
            return (OrderDisplayFulfillmentStatus) get("displayFulfillmentStatus");
        }

        public Order setDisplayFulfillmentStatus(OrderDisplayFulfillmentStatus arg) {
            optimisticData.put(getKey("displayFulfillmentStatus"), arg);
            return this;
        }

        /**
        * The customer's email address.
        */

        public String getEmail() {
            return (String) get("email");
        }

        public Order setEmail(String arg) {
            optimisticData.put(getKey("email"), arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * List of the order’s line items.
        */

        public OrderLineItemConnection getLineItems() {
            return (OrderLineItemConnection) get("lineItems");
        }

        public Order setLineItems(OrderLineItemConnection arg) {
            optimisticData.put(getKey("lineItems"), arg);
            return this;
        }

        /**
        * A unique numeric identifier for the order for use by shop owner and customer.
        */

        public Integer getOrderNumber() {
            return (Integer) get("orderNumber");
        }

        public Order setOrderNumber(Integer arg) {
            optimisticData.put(getKey("orderNumber"), arg);
            return this;
        }

        /**
        * The customer's phone number.
        */

        public String getPhone() {
            return (String) get("phone");
        }

        public Order setPhone(String arg) {
            optimisticData.put(getKey("phone"), arg);
            return this;
        }

        /**
        * The date and time when the order was imported. This value can be set to dates in the past when
        * importing from other systems. If no value is provided, it will be auto-generated based on current
        * date and time. 
        */

        public DateTime getProcessedAt() {
            return (DateTime) get("processedAt");
        }

        public Order setProcessedAt(DateTime arg) {
            optimisticData.put(getKey("processedAt"), arg);
            return this;
        }

        /**
        * The address to where the order will be shipped.
        */

        public MailingAddress getShippingAddress() {
            return (MailingAddress) get("shippingAddress");
        }

        public Order setShippingAddress(MailingAddress arg) {
            optimisticData.put(getKey("shippingAddress"), arg);
            return this;
        }

        /**
        * Price of the order before shipping and taxes.
        */

        public BigDecimal getSubtotalPrice() {
            return (BigDecimal) get("subtotalPrice");
        }

        public Order setSubtotalPrice(BigDecimal arg) {
            optimisticData.put(getKey("subtotalPrice"), arg);
            return this;
        }

        /**
        * The sum of all the prices of all the items in the order, taxes and discounts included (must be
        * positive).
        */

        public BigDecimal getTotalPrice() {
            return (BigDecimal) get("totalPrice");
        }

        public Order setTotalPrice(BigDecimal arg) {
            optimisticData.put(getKey("totalPrice"), arg);
            return this;
        }

        /**
        * The total amount that has been refunded.
        */

        public BigDecimal getTotalRefunded() {
            return (BigDecimal) get("totalRefunded");
        }

        public Order setTotalRefunded(BigDecimal arg) {
            optimisticData.put(getKey("totalRefunded"), arg);
            return this;
        }

        /**
        * The total cost of shipping.
        */

        public BigDecimal getTotalShippingPrice() {
            return (BigDecimal) get("totalShippingPrice");
        }

        public Order setTotalShippingPrice(BigDecimal arg) {
            optimisticData.put(getKey("totalShippingPrice"), arg);
            return this;
        }

        /**
        * The total cost of taxes.
        */

        public BigDecimal getTotalTax() {
            return (BigDecimal) get("totalTax");
        }

        public Order setTotalTax(BigDecimal arg) {
            optimisticData.put(getKey("totalTax"), arg);
            return this;
        }

        /**
        * The date and time when the order was last modified.
        */

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Order setUpdatedAt(DateTime arg) {
            optimisticData.put(getKey("updatedAt"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cancelReason": return false;

                case "cancelledAt": return false;

                case "createdAt": return false;

                case "currencyCode": return false;

                case "customerUrl": return false;

                case "displayFinancialStatus": return false;

                case "displayFulfillmentStatus": return false;

                case "email": return false;

                case "id": return false;

                case "lineItems": return true;

                case "orderNumber": return false;

                case "phone": return false;

                case "processedAt": return false;

                case "shippingAddress": return true;

                case "subtotalPrice": return false;

                case "totalPrice": return false;

                case "totalRefunded": return false;

                case "totalShippingPrice": return false;

                case "totalTax": return false;

                case "updatedAt": return false;

                default: return false;
            }
        }
    }

    public enum OrderCancelReason {
        CUSTOMER,

        DECLINED,

        FRAUD,

        INVENTORY,

        OTHER,

        UNKNOWN_VALUE;

        public static OrderCancelReason fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "CUSTOMER": {
                    return CUSTOMER;
                }

                case "DECLINED": {
                    return DECLINED;
                }

                case "FRAUD": {
                    return FRAUD;
                }

                case "INVENTORY": {
                    return INVENTORY;
                }

                case "OTHER": {
                    return OTHER;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case CUSTOMER: {
                    return "CUSTOMER";
                }

                case DECLINED: {
                    return "DECLINED";
                }

                case FRAUD: {
                    return "FRAUD";
                }

                case INVENTORY: {
                    return "INVENTORY";
                }

                case OTHER: {
                    return "OTHER";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface OrderConnectionQueryDefinition {
        void define(OrderConnectionQuery _queryBuilder);
    }

    public static class OrderConnectionQuery extends Query<OrderConnectionQuery> {
        OrderConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public OrderConnectionQuery edges(OrderEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new OrderEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public OrderConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class OrderConnection extends AbstractResponse<OrderConnection> {
        public OrderConnection() {
        }

        public OrderConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<OrderEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new OrderEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "OrderConnection";
        }

        /**
        * A list of edges.
        */

        public List<OrderEdge> getEdges() {
            return (List<OrderEdge>) get("edges");
        }

        public OrderConnection setEdges(List<OrderEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public OrderConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public enum OrderDisplayFinancialStatus {
        AUTHORIZED,

        PAID,

        PARTIALLY_PAID,

        PARTIALLY_REFUNDED,

        PENDING,

        REFUNDED,

        VOIDED,

        UNKNOWN_VALUE;

        public static OrderDisplayFinancialStatus fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "AUTHORIZED": {
                    return AUTHORIZED;
                }

                case "PAID": {
                    return PAID;
                }

                case "PARTIALLY_PAID": {
                    return PARTIALLY_PAID;
                }

                case "PARTIALLY_REFUNDED": {
                    return PARTIALLY_REFUNDED;
                }

                case "PENDING": {
                    return PENDING;
                }

                case "REFUNDED": {
                    return REFUNDED;
                }

                case "VOIDED": {
                    return VOIDED;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case AUTHORIZED: {
                    return "AUTHORIZED";
                }

                case PAID: {
                    return "PAID";
                }

                case PARTIALLY_PAID: {
                    return "PARTIALLY_PAID";
                }

                case PARTIALLY_REFUNDED: {
                    return "PARTIALLY_REFUNDED";
                }

                case PENDING: {
                    return "PENDING";
                }

                case REFUNDED: {
                    return "REFUNDED";
                }

                case VOIDED: {
                    return "VOIDED";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public enum OrderDisplayFulfillmentStatus {
        FULFILLED,

        OPEN,

        PARTIALLY_FULFILLED,

        PENDING_FULFILLMENT,

        RESTOCKED,

        UNFULFILLED,

        UNKNOWN_VALUE;

        public static OrderDisplayFulfillmentStatus fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "FULFILLED": {
                    return FULFILLED;
                }

                case "OPEN": {
                    return OPEN;
                }

                case "PARTIALLY_FULFILLED": {
                    return PARTIALLY_FULFILLED;
                }

                case "PENDING_FULFILLMENT": {
                    return PENDING_FULFILLMENT;
                }

                case "RESTOCKED": {
                    return RESTOCKED;
                }

                case "UNFULFILLED": {
                    return UNFULFILLED;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case FULFILLED: {
                    return "FULFILLED";
                }

                case OPEN: {
                    return "OPEN";
                }

                case PARTIALLY_FULFILLED: {
                    return "PARTIALLY_FULFILLED";
                }

                case PENDING_FULFILLMENT: {
                    return "PENDING_FULFILLMENT";
                }

                case RESTOCKED: {
                    return "RESTOCKED";
                }

                case UNFULFILLED: {
                    return "UNFULFILLED";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface OrderEdgeQueryDefinition {
        void define(OrderEdgeQuery _queryBuilder);
    }

    public static class OrderEdgeQuery extends Query<OrderEdgeQuery> {
        OrderEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public OrderEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        public OrderEdgeQuery node(OrderQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new OrderQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class OrderEdge extends AbstractResponse<OrderEdge> {
        public OrderEdge() {
        }

        public OrderEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new Order(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "OrderEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public OrderEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        public Order getNode() {
            return (Order) get("node");
        }

        public OrderEdge setNode(Order arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public interface OrderLineItemQueryDefinition {
        void define(OrderLineItemQuery _queryBuilder);
    }

    /**
    * Represents a single line in an order. There is one line item for each distinct product variant.
    */
    public static class OrderLineItemQuery extends Query<OrderLineItemQuery> {
        OrderLineItemQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * List of custom attributes associated to the line item.
        */
        public OrderLineItemQuery customAttributes(AttributeQueryDefinition queryDef) {
            startField("customAttributes");

            _queryBuilder.append('{');
            queryDef.define(new AttributeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The number of products variants associated to the line item.
        */
        public OrderLineItemQuery quantity() {
            startField("quantity");

            return this;
        }

        /**
        * The title of the product combined with title of the variant.
        */
        public OrderLineItemQuery title() {
            startField("title");

            return this;
        }

        /**
        * The product variant object associated to the line item.
        */
        public OrderLineItemQuery variant(ProductVariantQueryDefinition queryDef) {
            startField("variant");

            _queryBuilder.append('{');
            queryDef.define(new ProductVariantQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    /**
    * Represents a single line in an order. There is one line item for each distinct product variant.
    */
    public static class OrderLineItem extends AbstractResponse<OrderLineItem> {
        public OrderLineItem() {
        }

        public OrderLineItem(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customAttributes": {
                        List<Attribute> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new Attribute(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "quantity": {
                        responseData.put(key, jsonAsInteger(field.getValue(), key));

                        break;
                    }

                    case "title": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "variant": {
                        ProductVariant optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ProductVariant(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "OrderLineItem";
        }

        /**
        * List of custom attributes associated to the line item.
        */

        public List<Attribute> getCustomAttributes() {
            return (List<Attribute>) get("customAttributes");
        }

        public OrderLineItem setCustomAttributes(List<Attribute> arg) {
            optimisticData.put(getKey("customAttributes"), arg);
            return this;
        }

        /**
        * The number of products variants associated to the line item.
        */

        public Integer getQuantity() {
            return (Integer) get("quantity");
        }

        public OrderLineItem setQuantity(Integer arg) {
            optimisticData.put(getKey("quantity"), arg);
            return this;
        }

        /**
        * The title of the product combined with title of the variant.
        */

        public String getTitle() {
            return (String) get("title");
        }

        public OrderLineItem setTitle(String arg) {
            optimisticData.put(getKey("title"), arg);
            return this;
        }

        /**
        * The product variant object associated to the line item.
        */

        public ProductVariant getVariant() {
            return (ProductVariant) get("variant");
        }

        public OrderLineItem setVariant(ProductVariant arg) {
            optimisticData.put(getKey("variant"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customAttributes": return true;

                case "quantity": return false;

                case "title": return false;

                case "variant": return true;

                default: return false;
            }
        }
    }

    public interface OrderLineItemConnectionQueryDefinition {
        void define(OrderLineItemConnectionQuery _queryBuilder);
    }

    public static class OrderLineItemConnectionQuery extends Query<OrderLineItemConnectionQuery> {
        OrderLineItemConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public OrderLineItemConnectionQuery edges(OrderLineItemEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new OrderLineItemEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public OrderLineItemConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class OrderLineItemConnection extends AbstractResponse<OrderLineItemConnection> {
        public OrderLineItemConnection() {
        }

        public OrderLineItemConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<OrderLineItemEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new OrderLineItemEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "OrderLineItemConnection";
        }

        /**
        * A list of edges.
        */

        public List<OrderLineItemEdge> getEdges() {
            return (List<OrderLineItemEdge>) get("edges");
        }

        public OrderLineItemConnection setEdges(List<OrderLineItemEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public OrderLineItemConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface OrderLineItemEdgeQueryDefinition {
        void define(OrderLineItemEdgeQuery _queryBuilder);
    }

    public static class OrderLineItemEdgeQuery extends Query<OrderLineItemEdgeQuery> {
        OrderLineItemEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public OrderLineItemEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        public OrderLineItemEdgeQuery node(OrderLineItemQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new OrderLineItemQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class OrderLineItemEdge extends AbstractResponse<OrderLineItemEdge> {
        public OrderLineItemEdge() {
        }

        public OrderLineItemEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new OrderLineItem(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "OrderLineItemEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public OrderLineItemEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        public OrderLineItem getNode() {
            return (OrderLineItem) get("node");
        }

        public OrderLineItemEdge setNode(OrderLineItem arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    /**
    * The set of valid sort keys for the orders query.
    */
    public enum OrderSortKeys {
        CREATED_AT,

        CUSTOMER_NAME,

        FINANCIAL_STATUS,

        FULFILLMENT_STATUS,

        ID,

        ORDER_NUMBER,

        PROCESSED_AT,

        RELEVANCE,

        TOTAL_PRICE,

        UPDATED_AT,

        UNKNOWN_VALUE;

        public static OrderSortKeys fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "CREATED_AT": {
                    return CREATED_AT;
                }

                case "CUSTOMER_NAME": {
                    return CUSTOMER_NAME;
                }

                case "FINANCIAL_STATUS": {
                    return FINANCIAL_STATUS;
                }

                case "FULFILLMENT_STATUS": {
                    return FULFILLMENT_STATUS;
                }

                case "ID": {
                    return ID;
                }

                case "ORDER_NUMBER": {
                    return ORDER_NUMBER;
                }

                case "PROCESSED_AT": {
                    return PROCESSED_AT;
                }

                case "RELEVANCE": {
                    return RELEVANCE;
                }

                case "TOTAL_PRICE": {
                    return TOTAL_PRICE;
                }

                case "UPDATED_AT": {
                    return UPDATED_AT;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case CREATED_AT: {
                    return "CREATED_AT";
                }

                case CUSTOMER_NAME: {
                    return "CUSTOMER_NAME";
                }

                case FINANCIAL_STATUS: {
                    return "FINANCIAL_STATUS";
                }

                case FULFILLMENT_STATUS: {
                    return "FULFILLMENT_STATUS";
                }

                case ID: {
                    return "ID";
                }

                case ORDER_NUMBER: {
                    return "ORDER_NUMBER";
                }

                case PROCESSED_AT: {
                    return "PROCESSED_AT";
                }

                case RELEVANCE: {
                    return "RELEVANCE";
                }

                case TOTAL_PRICE: {
                    return "TOTAL_PRICE";
                }

                case UPDATED_AT: {
                    return "UPDATED_AT";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface PageInfoQueryDefinition {
        void define(PageInfoQuery _queryBuilder);
    }

    /**
    * Information about pagination in a connection.
    */
    public static class PageInfoQuery extends Query<PageInfoQuery> {
        PageInfoQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * Indicates if there are more pages to fetch.
        */
        public PageInfoQuery hasNextPage() {
            startField("hasNextPage");

            return this;
        }

        /**
        * Indicates if there are any pages prior to the current page.
        */
        public PageInfoQuery hasPreviousPage() {
            startField("hasPreviousPage");

            return this;
        }
    }

    /**
    * Information about pagination in a connection.
    */
    public static class PageInfo extends AbstractResponse<PageInfo> {
        public PageInfo() {
        }

        public PageInfo(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "hasNextPage": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "hasPreviousPage": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "PageInfo";
        }

        /**
        * Indicates if there are more pages to fetch.
        */

        public Boolean getHasNextPage() {
            return (Boolean) get("hasNextPage");
        }

        public PageInfo setHasNextPage(Boolean arg) {
            optimisticData.put(getKey("hasNextPage"), arg);
            return this;
        }

        /**
        * Indicates if there are any pages prior to the current page.
        */

        public Boolean getHasPreviousPage() {
            return (Boolean) get("hasPreviousPage");
        }

        public PageInfo setHasPreviousPage(Boolean arg) {
            optimisticData.put(getKey("hasPreviousPage"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "hasNextPage": return false;

                case "hasPreviousPage": return false;

                default: return false;
            }
        }
    }

    public interface PaymentQueryDefinition {
        void define(PaymentQuery _queryBuilder);
    }

    /**
    * A payment applied to a checkout.
    */
    public static class PaymentQuery extends Query<PaymentQuery> {
        PaymentQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        /**
        * The amount of the payment.
        */
        public PaymentQuery amount() {
            startField("amount");

            return this;
        }

        /**
        * The billing address for the payment.
        */
        public PaymentQuery billingAddress(MailingAddressQueryDefinition queryDef) {
            startField("billingAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The checkout to which the payment belongs.
        */
        public PaymentQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The credit card used for the payment in the case of direct payments.
        */
        public PaymentQuery creditCard(CreditCardQueryDefinition queryDef) {
            startField("creditCard");

            _queryBuilder.append('{');
            queryDef.define(new CreditCardQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * An message describing a processing error during asynchronous processing.
        */
        public PaymentQuery errorMessage() {
            startField("errorMessage");

            return this;
        }

        /**
        * A client-side generated token to identify a payment and perform idempotent operations.
        */
        public PaymentQuery idempotencyKey() {
            startField("idempotencyKey");

            return this;
        }

        /**
        * Whether or not the payment is still processing asynchronously.
        */
        public PaymentQuery ready() {
            startField("ready");

            return this;
        }

        /**
        * A flag to indicate if the payment is to be done in test mode for gateways that support it.
        */
        public PaymentQuery test() {
            startField("test");

            return this;
        }

        /**
        * The actual transaction recorded by Shopify after having processed the payment with the gateway.
        */
        public PaymentQuery transaction(TransactionQueryDefinition queryDef) {
            startField("transaction");

            _queryBuilder.append('{');
            queryDef.define(new TransactionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    /**
    * A payment applied to a checkout.
    */
    public static class Payment extends AbstractResponse<Payment> implements Node {
        public Payment() {
        }

        public Payment(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "amount": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "billingAddress": {
                        MailingAddress optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new MailingAddress(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkout": {
                        responseData.put(key, new Checkout(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "creditCard": {
                        CreditCard optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CreditCard(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "errorMessage": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "idempotencyKey": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "ready": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "test": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "transaction": {
                        Transaction optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Transaction(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public Payment(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "Payment";
        }

        /**
        * The amount of the payment.
        */

        public BigDecimal getAmount() {
            return (BigDecimal) get("amount");
        }

        public Payment setAmount(BigDecimal arg) {
            optimisticData.put(getKey("amount"), arg);
            return this;
        }

        /**
        * The billing address for the payment.
        */

        public MailingAddress getBillingAddress() {
            return (MailingAddress) get("billingAddress");
        }

        public Payment setBillingAddress(MailingAddress arg) {
            optimisticData.put(getKey("billingAddress"), arg);
            return this;
        }

        /**
        * The checkout to which the payment belongs.
        */

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public Payment setCheckout(Checkout arg) {
            optimisticData.put(getKey("checkout"), arg);
            return this;
        }

        /**
        * The credit card used for the payment in the case of direct payments.
        */

        public CreditCard getCreditCard() {
            return (CreditCard) get("creditCard");
        }

        public Payment setCreditCard(CreditCard arg) {
            optimisticData.put(getKey("creditCard"), arg);
            return this;
        }

        /**
        * An message describing a processing error during asynchronous processing.
        */

        public String getErrorMessage() {
            return (String) get("errorMessage");
        }

        public Payment setErrorMessage(String arg) {
            optimisticData.put(getKey("errorMessage"), arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * A client-side generated token to identify a payment and perform idempotent operations.
        */

        public String getIdempotencyKey() {
            return (String) get("idempotencyKey");
        }

        public Payment setIdempotencyKey(String arg) {
            optimisticData.put(getKey("idempotencyKey"), arg);
            return this;
        }

        /**
        * Whether or not the payment is still processing asynchronously.
        */

        public Boolean getReady() {
            return (Boolean) get("ready");
        }

        public Payment setReady(Boolean arg) {
            optimisticData.put(getKey("ready"), arg);
            return this;
        }

        /**
        * A flag to indicate if the payment is to be done in test mode for gateways that support it.
        */

        public Boolean getTest() {
            return (Boolean) get("test");
        }

        public Payment setTest(Boolean arg) {
            optimisticData.put(getKey("test"), arg);
            return this;
        }

        /**
        * The actual transaction recorded by Shopify after having processed the payment with the gateway.
        */

        public Transaction getTransaction() {
            return (Transaction) get("transaction");
        }

        public Payment setTransaction(Transaction arg) {
            optimisticData.put(getKey("transaction"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "amount": return false;

                case "billingAddress": return true;

                case "checkout": return true;

                case "creditCard": return true;

                case "errorMessage": return false;

                case "id": return false;

                case "idempotencyKey": return false;

                case "ready": return false;

                case "test": return false;

                case "transaction": return true;

                default: return false;
            }
        }
    }

    public interface ProductQueryDefinition {
        void define(ProductQuery _queryBuilder);
    }

    /**
    * A product represents an individual item for sale in a Shopify store. Products are often physical,
    * but they don't have to be. For example, a digital download (such as a movie, music or ebook file)
    * also qualifies as a product, as do services (such as equipment rental, work for hire, customization
    * of another product or an extended warranty).
    */
    public static class ProductQuery extends Query<ProductQuery> {
        ProductQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public class CollectionsArguments extends Arguments {
            CollectionsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, false);
            }

            public CollectionsArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            public CollectionsArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface CollectionsArgumentsDefinition {
            void define(CollectionsArguments args);
        }

        /**
        * List of collections a product belongs to.
        */
        public ProductQuery collections(int first, CollectionConnectionQueryDefinition queryDef) {
            return collections(first, args -> {}, queryDef);
        }

        /**
        * List of collections a product belongs to.
        */
        public ProductQuery collections(int first, CollectionsArgumentsDefinition argsDef, CollectionConnectionQueryDefinition queryDef) {
            startField("collections");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new CollectionsArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CollectionConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The date and time when the product was created.
        */
        public ProductQuery createdAt() {
            startField("createdAt");

            return this;
        }

        public class DescriptionArguments extends Arguments {
            DescriptionArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Truncates string after the given length.
            */
            public DescriptionArguments truncateAt(Integer value) {
                if (value != null) {
                    startArgument("truncateAt");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface DescriptionArgumentsDefinition {
            void define(DescriptionArguments args);
        }

        /**
        * Stripped description of the product, single line with HTML tags removed.
        */
        public ProductQuery description() {
            return description(args -> {});
        }

        /**
        * Stripped description of the product, single line with HTML tags removed.
        */
        public ProductQuery description(DescriptionArgumentsDefinition argsDef) {
            startField("description");

            DescriptionArguments args = new DescriptionArguments(_queryBuilder);
            argsDef.define(args);
            DescriptionArguments.end(args);

            return this;
        }

        /**
        * The description of the product, complete with HTML formatting.
        */
        public ProductQuery descriptionHtml() {
            startField("descriptionHtml");

            return this;
        }

        /**
        * A human-friendly unique string for the Product automatically generated from its title. They are used
        * by the Liquid templating language to refer to objects. 
        */
        public ProductQuery handle() {
            startField("handle");

            return this;
        }

        public class ImagesArguments extends Arguments {
            ImagesArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, false);
            }

            public ImagesArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            public ImagesArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Image width in pixels between 1 and 2048
            */
            public ImagesArguments maxWidth(Integer value) {
                if (value != null) {
                    startArgument("maxWidth");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Image height in pixels between 1 and 2048
            */
            public ImagesArguments maxHeight(Integer value) {
                if (value != null) {
                    startArgument("maxHeight");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * If specified, crop the image keeping the specified region
            */
            public ImagesArguments crop(CropRegion value) {
                if (value != null) {
                    startArgument("crop");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

            /**
            * Image size multiplier retina displays. Must be between 1 and 3
            */
            public ImagesArguments scale(Integer value) {
                if (value != null) {
                    startArgument("scale");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface ImagesArgumentsDefinition {
            void define(ImagesArguments args);
        }

        /**
        * List of images associated with the product.
        */
        public ProductQuery images(int first, ImageConnectionQueryDefinition queryDef) {
            return images(first, args -> {}, queryDef);
        }

        /**
        * List of images associated with the product.
        */
        public ProductQuery images(int first, ImagesArgumentsDefinition argsDef, ImageConnectionQueryDefinition queryDef) {
            startField("images");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new ImagesArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new ImageConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public class OptionsArguments extends Arguments {
            OptionsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Truncate the array result to this size
            */
            public OptionsArguments first(Integer value) {
                if (value != null) {
                    startArgument("first");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface OptionsArgumentsDefinition {
            void define(OptionsArguments args);
        }

        /**
        * Lst of custom product options (maximum of 3 per product).
        */
        public ProductQuery options(ProductOptionQueryDefinition queryDef) {
            return options(args -> {}, queryDef);
        }

        /**
        * Lst of custom product options (maximum of 3 per product).
        */
        public ProductQuery options(OptionsArgumentsDefinition argsDef, ProductOptionQueryDefinition queryDef) {
            startField("options");

            OptionsArguments args = new OptionsArguments(_queryBuilder);
            argsDef.define(args);
            OptionsArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new ProductOptionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * A categorization that a product can be tagged with, commonly used for filtering and searching.
        */
        public ProductQuery productType() {
            startField("productType");

            return this;
        }

        /**
        * The date and time when the product was published to the Online Store channel. A value of `null`
        * indicates that the product is not published to Online Store. 
        */
        public ProductQuery publishedAt() {
            startField("publishedAt");

            return this;
        }

        /**
        * A categorization that a product can be tagged with, commonly used for filtering and searching. Each
        * comma-separated tag has a character limit of 255. 
        */
        public ProductQuery tags() {
            startField("tags");

            return this;
        }

        /**
        * The product’s title.
        */
        public ProductQuery title() {
            startField("title");

            return this;
        }

        /**
        * The date and time when the product was last modified.
        */
        public ProductQuery updatedAt() {
            startField("updatedAt");

            return this;
        }

        /**
        * Find a product’s variant based on its selected options. This is useful for converting a user’s
        * selection of product options into a single matching variant. If there is not a variant for the
        * selected options, `null` will be returned. 
        */
        public ProductQuery variantBySelectedOptions(List<SelectedOptionInput> selectedOptions, ProductVariantQueryDefinition queryDef) {
            startField("variantBySelectedOptions");

            _queryBuilder.append("(selectedOptions:");
            _queryBuilder.append('[');

            String listSeperator1 = "";
            for (SelectedOptionInput item1 : selectedOptions) {
                _queryBuilder.append(listSeperator1);
                listSeperator1 = ",";
                item1.appendTo(_queryBuilder);
            }
            _queryBuilder.append(']');

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new ProductVariantQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public class VariantsArguments extends Arguments {
            VariantsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, false);
            }

            public VariantsArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            public VariantsArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface VariantsArgumentsDefinition {
            void define(VariantsArguments args);
        }

        /**
        * List of the product’s variants.
        */
        public ProductQuery variants(int first, ProductVariantConnectionQueryDefinition queryDef) {
            return variants(first, args -> {}, queryDef);
        }

        /**
        * List of the product’s variants.
        */
        public ProductQuery variants(int first, VariantsArgumentsDefinition argsDef, ProductVariantConnectionQueryDefinition queryDef) {
            startField("variants");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new VariantsArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new ProductVariantConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The product’s vendor name.
        */
        public ProductQuery vendor() {
            startField("vendor");

            return this;
        }
    }

    /**
    * A product represents an individual item for sale in a Shopify store. Products are often physical,
    * but they don't have to be. For example, a digital download (such as a movie, music or ebook file)
    * also qualifies as a product, as do services (such as equipment rental, work for hire, customization
    * of another product or an extended warranty).
    */
    public static class Product extends AbstractResponse<Product> implements Node {
        public Product() {
        }

        public Product(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "collections": {
                        responseData.put(key, new CollectionConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "createdAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "description": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "descriptionHtml": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "handle": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "images": {
                        responseData.put(key, new ImageConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "options": {
                        List<ProductOption> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new ProductOption(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "productType": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "publishedAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "tags": {
                        List<String> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(jsonAsString(element1, key));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "title": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "updatedAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "variantBySelectedOptions": {
                        ProductVariant optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ProductVariant(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "variants": {
                        responseData.put(key, new ProductVariantConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "vendor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public Product(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "Product";
        }

        /**
        * List of collections a product belongs to.
        */

        public CollectionConnection getCollections() {
            return (CollectionConnection) get("collections");
        }

        public Product setCollections(CollectionConnection arg) {
            optimisticData.put(getKey("collections"), arg);
            return this;
        }

        /**
        * The date and time when the product was created.
        */

        public DateTime getCreatedAt() {
            return (DateTime) get("createdAt");
        }

        public Product setCreatedAt(DateTime arg) {
            optimisticData.put(getKey("createdAt"), arg);
            return this;
        }

        /**
        * Stripped description of the product, single line with HTML tags removed.
        */

        public String getDescription() {
            return (String) get("description");
        }

        public Product setDescription(String arg) {
            optimisticData.put(getKey("description"), arg);
            return this;
        }

        /**
        * The description of the product, complete with HTML formatting.
        */

        public String getDescriptionHtml() {
            return (String) get("descriptionHtml");
        }

        public Product setDescriptionHtml(String arg) {
            optimisticData.put(getKey("descriptionHtml"), arg);
            return this;
        }

        /**
        * A human-friendly unique string for the Product automatically generated from its title. They are used
        * by the Liquid templating language to refer to objects. 
        */

        public String getHandle() {
            return (String) get("handle");
        }

        public Product setHandle(String arg) {
            optimisticData.put(getKey("handle"), arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * List of images associated with the product.
        */

        public ImageConnection getImages() {
            return (ImageConnection) get("images");
        }

        public Product setImages(ImageConnection arg) {
            optimisticData.put(getKey("images"), arg);
            return this;
        }

        /**
        * Lst of custom product options (maximum of 3 per product).
        */

        public List<ProductOption> getOptions() {
            return (List<ProductOption>) get("options");
        }

        public Product setOptions(List<ProductOption> arg) {
            optimisticData.put(getKey("options"), arg);
            return this;
        }

        /**
        * A categorization that a product can be tagged with, commonly used for filtering and searching.
        */

        public String getProductType() {
            return (String) get("productType");
        }

        public Product setProductType(String arg) {
            optimisticData.put(getKey("productType"), arg);
            return this;
        }

        /**
        * The date and time when the product was published to the Online Store channel. A value of `null`
        * indicates that the product is not published to Online Store. 
        */

        public DateTime getPublishedAt() {
            return (DateTime) get("publishedAt");
        }

        public Product setPublishedAt(DateTime arg) {
            optimisticData.put(getKey("publishedAt"), arg);
            return this;
        }

        /**
        * A categorization that a product can be tagged with, commonly used for filtering and searching. Each
        * comma-separated tag has a character limit of 255. 
        */

        public List<String> getTags() {
            return (List<String>) get("tags");
        }

        public Product setTags(List<String> arg) {
            optimisticData.put(getKey("tags"), arg);
            return this;
        }

        /**
        * The product’s title.
        */

        public String getTitle() {
            return (String) get("title");
        }

        public Product setTitle(String arg) {
            optimisticData.put(getKey("title"), arg);
            return this;
        }

        /**
        * The date and time when the product was last modified.
        */

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Product setUpdatedAt(DateTime arg) {
            optimisticData.put(getKey("updatedAt"), arg);
            return this;
        }

        /**
        * Find a product’s variant based on its selected options. This is useful for converting a user’s
        * selection of product options into a single matching variant. If there is not a variant for the
        * selected options, `null` will be returned. 
        */

        public ProductVariant getVariantBySelectedOptions() {
            return (ProductVariant) get("variantBySelectedOptions");
        }

        public Product setVariantBySelectedOptions(ProductVariant arg) {
            optimisticData.put(getKey("variantBySelectedOptions"), arg);
            return this;
        }

        /**
        * List of the product’s variants.
        */

        public ProductVariantConnection getVariants() {
            return (ProductVariantConnection) get("variants");
        }

        public Product setVariants(ProductVariantConnection arg) {
            optimisticData.put(getKey("variants"), arg);
            return this;
        }

        /**
        * The product’s vendor name.
        */

        public String getVendor() {
            return (String) get("vendor");
        }

        public Product setVendor(String arg) {
            optimisticData.put(getKey("vendor"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "collections": return true;

                case "createdAt": return false;

                case "description": return false;

                case "descriptionHtml": return false;

                case "handle": return false;

                case "id": return false;

                case "images": return true;

                case "options": return true;

                case "productType": return false;

                case "publishedAt": return false;

                case "tags": return false;

                case "title": return false;

                case "updatedAt": return false;

                case "variantBySelectedOptions": return true;

                case "variants": return true;

                case "vendor": return false;

                default: return false;
            }
        }
    }

    public interface ProductConnectionQueryDefinition {
        void define(ProductConnectionQuery _queryBuilder);
    }

    public static class ProductConnectionQuery extends Query<ProductConnectionQuery> {
        ProductConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public ProductConnectionQuery edges(ProductEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new ProductEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public ProductConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ProductConnection extends AbstractResponse<ProductConnection> {
        public ProductConnection() {
        }

        public ProductConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<ProductEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new ProductEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "ProductConnection";
        }

        /**
        * A list of edges.
        */

        public List<ProductEdge> getEdges() {
            return (List<ProductEdge>) get("edges");
        }

        public ProductConnection setEdges(List<ProductEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public ProductConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface ProductEdgeQueryDefinition {
        void define(ProductEdgeQuery _queryBuilder);
    }

    public static class ProductEdgeQuery extends Query<ProductEdgeQuery> {
        ProductEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public ProductEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        public ProductEdgeQuery node(ProductQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new ProductQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ProductEdge extends AbstractResponse<ProductEdge> {
        public ProductEdge() {
        }

        public ProductEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new Product(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "ProductEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public ProductEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        public Product getNode() {
            return (Product) get("node");
        }

        public ProductEdge setNode(Product arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public interface ProductOptionQueryDefinition {
        void define(ProductOptionQuery _queryBuilder);
    }

    /**
    * Custom product property names like "Size", "Color", and "Material". Products are based on
    * permutations of these options. A product may have a maximum of 3 options. 255 characters limit each.
    */
    public static class ProductOptionQuery extends Query<ProductOptionQuery> {
        ProductOptionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        /**
        * The product option’s name.
        */
        public ProductOptionQuery name() {
            startField("name");

            return this;
        }

        /**
        * The corresponding value to the product option name.
        */
        public ProductOptionQuery values() {
            startField("values");

            return this;
        }
    }

    /**
    * Custom product property names like "Size", "Color", and "Material". Products are based on
    * permutations of these options. A product may have a maximum of 3 options. 255 characters limit each.
    */
    public static class ProductOption extends AbstractResponse<ProductOption> implements Node {
        public ProductOption() {
        }

        public ProductOption(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "name": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "values": {
                        List<String> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(jsonAsString(element1, key));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public ProductOption(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "ProductOption";
        }

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * The product option’s name.
        */

        public String getName() {
            return (String) get("name");
        }

        public ProductOption setName(String arg) {
            optimisticData.put(getKey("name"), arg);
            return this;
        }

        /**
        * The corresponding value to the product option name.
        */

        public List<String> getValues() {
            return (List<String>) get("values");
        }

        public ProductOption setValues(List<String> arg) {
            optimisticData.put(getKey("values"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "id": return false;

                case "name": return false;

                case "values": return false;

                default: return false;
            }
        }
    }

    /**
    * The set of valid sort keys for the products query.
    */
    public enum ProductSortKeys {
        CREATED_AT,

        ID,

        PRODUCT_TYPE,

        RELEVANCE,

        TITLE,

        UPDATED_AT,

        VENDOR,

        UNKNOWN_VALUE;

        public static ProductSortKeys fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "CREATED_AT": {
                    return CREATED_AT;
                }

                case "ID": {
                    return ID;
                }

                case "PRODUCT_TYPE": {
                    return PRODUCT_TYPE;
                }

                case "RELEVANCE": {
                    return RELEVANCE;
                }

                case "TITLE": {
                    return TITLE;
                }

                case "UPDATED_AT": {
                    return UPDATED_AT;
                }

                case "VENDOR": {
                    return VENDOR;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case CREATED_AT: {
                    return "CREATED_AT";
                }

                case ID: {
                    return "ID";
                }

                case PRODUCT_TYPE: {
                    return "PRODUCT_TYPE";
                }

                case RELEVANCE: {
                    return "RELEVANCE";
                }

                case TITLE: {
                    return "TITLE";
                }

                case UPDATED_AT: {
                    return "UPDATED_AT";
                }

                case VENDOR: {
                    return "VENDOR";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface ProductVariantQueryDefinition {
        void define(ProductVariantQuery _queryBuilder);
    }

    /**
    * A product variant represents a different version of a product, such as differing sizes or differing
    * colors.
    */
    public static class ProductVariantQuery extends Query<ProductVariantQuery> {
        ProductVariantQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        /**
        * Indicates if the product variant is in stock.
        */
        public ProductVariantQuery available() {
            startField("available");

            return this;
        }

        public class ImageArguments extends Arguments {
            ImageArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            /**
            * Image width in pixels between 1 and 2048
            */
            public ImageArguments maxWidth(Integer value) {
                if (value != null) {
                    startArgument("maxWidth");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Image height in pixels between 1 and 2048
            */
            public ImageArguments maxHeight(Integer value) {
                if (value != null) {
                    startArgument("maxHeight");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * If specified, crop the image keeping the specified region
            */
            public ImageArguments crop(CropRegion value) {
                if (value != null) {
                    startArgument("crop");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

            /**
            * Image size multiplier retina displays. Must be between 1 and 3
            */
            public ImageArguments scale(Integer value) {
                if (value != null) {
                    startArgument("scale");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface ImageArgumentsDefinition {
            void define(ImageArguments args);
        }

        /**
        * Image associated with the product variant.
        */
        public ProductVariantQuery image(ImageQueryDefinition queryDef) {
            return image(args -> {}, queryDef);
        }

        /**
        * Image associated with the product variant.
        */
        public ProductVariantQuery image(ImageArgumentsDefinition argsDef, ImageQueryDefinition queryDef) {
            startField("image");

            ImageArguments args = new ImageArguments(_queryBuilder);
            argsDef.define(args);
            ImageArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new ImageQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The product variant’s price.
        */
        public ProductVariantQuery price() {
            startField("price");

            return this;
        }

        /**
        * The product object that the product variant belongs to.
        */
        public ProductVariantQuery product(ProductQueryDefinition queryDef) {
            startField("product");

            _queryBuilder.append('{');
            queryDef.define(new ProductQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * List of product options applied to the variant.
        */
        public ProductVariantQuery selectedOptions(SelectedOptionQueryDefinition queryDef) {
            startField("selectedOptions");

            _queryBuilder.append('{');
            queryDef.define(new SelectedOptionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The product variant’s title.
        */
        public ProductVariantQuery title() {
            startField("title");

            return this;
        }

        /**
        * The weight of the product variant in the unit system specified with `weight_unit`.
        */
        public ProductVariantQuery weight() {
            startField("weight");

            return this;
        }

        /**
        * Unit of measurement for weight.
        */
        public ProductVariantQuery weightUnit() {
            startField("weightUnit");

            return this;
        }
    }

    /**
    * A product variant represents a different version of a product, such as differing sizes or differing
    * colors.
    */
    public static class ProductVariant extends AbstractResponse<ProductVariant> implements Node {
        public ProductVariant() {
        }

        public ProductVariant(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "available": {
                        Boolean optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsBoolean(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "image": {
                        Image optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Image(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "price": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "product": {
                        responseData.put(key, new Product(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "selectedOptions": {
                        List<SelectedOption> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new SelectedOption(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "title": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "weight": {
                        Double optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsDouble(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "weightUnit": {
                        responseData.put(key, WeightUnit.fromGraphQl(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public ProductVariant(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "ProductVariant";
        }

        /**
        * Indicates if the product variant is in stock.
        */

        public Boolean getAvailable() {
            return (Boolean) get("available");
        }

        public ProductVariant setAvailable(Boolean arg) {
            optimisticData.put(getKey("available"), arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * Image associated with the product variant.
        */

        public Image getImage() {
            return (Image) get("image");
        }

        public ProductVariant setImage(Image arg) {
            optimisticData.put(getKey("image"), arg);
            return this;
        }

        /**
        * The product variant’s price.
        */

        public BigDecimal getPrice() {
            return (BigDecimal) get("price");
        }

        public ProductVariant setPrice(BigDecimal arg) {
            optimisticData.put(getKey("price"), arg);
            return this;
        }

        /**
        * The product object that the product variant belongs to.
        */

        public Product getProduct() {
            return (Product) get("product");
        }

        public ProductVariant setProduct(Product arg) {
            optimisticData.put(getKey("product"), arg);
            return this;
        }

        /**
        * List of product options applied to the variant.
        */

        public List<SelectedOption> getSelectedOptions() {
            return (List<SelectedOption>) get("selectedOptions");
        }

        public ProductVariant setSelectedOptions(List<SelectedOption> arg) {
            optimisticData.put(getKey("selectedOptions"), arg);
            return this;
        }

        /**
        * The product variant’s title.
        */

        public String getTitle() {
            return (String) get("title");
        }

        public ProductVariant setTitle(String arg) {
            optimisticData.put(getKey("title"), arg);
            return this;
        }

        /**
        * The weight of the product variant in the unit system specified with `weight_unit`.
        */

        public Double getWeight() {
            return (Double) get("weight");
        }

        public ProductVariant setWeight(Double arg) {
            optimisticData.put(getKey("weight"), arg);
            return this;
        }

        /**
        * Unit of measurement for weight.
        */

        public WeightUnit getWeightUnit() {
            return (WeightUnit) get("weightUnit");
        }

        public ProductVariant setWeightUnit(WeightUnit arg) {
            optimisticData.put(getKey("weightUnit"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "available": return false;

                case "id": return false;

                case "image": return true;

                case "price": return false;

                case "product": return true;

                case "selectedOptions": return true;

                case "title": return false;

                case "weight": return false;

                case "weightUnit": return false;

                default: return false;
            }
        }
    }

    public interface ProductVariantConnectionQueryDefinition {
        void define(ProductVariantConnectionQuery _queryBuilder);
    }

    public static class ProductVariantConnectionQuery extends Query<ProductVariantConnectionQuery> {
        ProductVariantConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * A list of edges.
        */
        public ProductVariantConnectionQuery edges(ProductVariantEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new ProductVariantEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * Information to aid in pagination.
        */
        public ProductVariantConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ProductVariantConnection extends AbstractResponse<ProductVariantConnection> {
        public ProductVariantConnection() {
        }

        public ProductVariantConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<ProductVariantEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new ProductVariantEdge(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "pageInfo": {
                        responseData.put(key, new PageInfo(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "ProductVariantConnection";
        }

        /**
        * A list of edges.
        */

        public List<ProductVariantEdge> getEdges() {
            return (List<ProductVariantEdge>) get("edges");
        }

        public ProductVariantConnection setEdges(List<ProductVariantEdge> arg) {
            optimisticData.put(getKey("edges"), arg);
            return this;
        }

        /**
        * Information to aid in pagination.
        */

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public ProductVariantConnection setPageInfo(PageInfo arg) {
            optimisticData.put(getKey("pageInfo"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface ProductVariantEdgeQueryDefinition {
        void define(ProductVariantEdgeQuery _queryBuilder);
    }

    public static class ProductVariantEdgeQuery extends Query<ProductVariantEdgeQuery> {
        ProductVariantEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public ProductVariantEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        public ProductVariantEdgeQuery node(ProductVariantQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new ProductVariantQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ProductVariantEdge extends AbstractResponse<ProductVariantEdge> {
        public ProductVariantEdge() {
        }

        public ProductVariantEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new ProductVariant(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "ProductVariantEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public ProductVariantEdge setCursor(String arg) {
            optimisticData.put(getKey("cursor"), arg);
            return this;
        }

        public ProductVariant getNode() {
            return (ProductVariant) get("node");
        }

        public ProductVariantEdge setNode(ProductVariant arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public interface QueryRootQueryDefinition {
        void define(QueryRootQuery _queryBuilder);
    }

    /**
    * The schema’s entry-point for queries. This acts as the public, top-level API from which all queries
    * must start.
    */
    public static class QueryRootQuery extends Query<QueryRootQuery> {
        QueryRootQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public QueryRootQuery customer(String customerAccessToken, CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append("(customerAccessToken:");
            Query.appendQuotedString(_queryBuilder, customerAccessToken.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public QueryRootQuery node(ID id, NodeQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append("(id:");
            Query.appendQuotedString(_queryBuilder, id.toString());

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new NodeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public QueryRootQuery nodes(List<ID> ids, NodeQueryDefinition queryDef) {
            startField("nodes");

            _queryBuilder.append("(ids:");
            _queryBuilder.append('[');

            String listSeperator1 = "";
            for (ID item1 : ids) {
                _queryBuilder.append(listSeperator1);
                listSeperator1 = ",";
                Query.appendQuotedString(_queryBuilder, item1.toString());
            }
            _queryBuilder.append(']');

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new NodeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public QueryRootQuery shop(ShopQueryDefinition queryDef) {
            startField("shop");

            _queryBuilder.append('{');
            queryDef.define(new ShopQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public String toString() {
            return _queryBuilder.toString();
        }
    }

    /**
    * The schema’s entry-point for queries. This acts as the public, top-level API from which all queries
    * must start.
    */
    public static class QueryRoot extends AbstractResponse<QueryRoot> {
        public QueryRoot() {
        }

        public QueryRoot(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "customer": {
                        Customer optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Customer(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "node": {
                        Node optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = UnknownNode.create(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "nodes": {
                        List<Node> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            Node optional2 = null;
                            if (!element1.isJsonNull()) {
                                optional2 = UnknownNode.create(jsonAsObject(element1, key));
                            }

                            list1.add(optional2);
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "shop": {
                        responseData.put(key, new Shop(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "QueryRoot";
        }

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public QueryRoot setCustomer(Customer arg) {
            optimisticData.put(getKey("customer"), arg);
            return this;
        }

        public Node getNode() {
            return (Node) get("node");
        }

        public QueryRoot setNode(Node arg) {
            optimisticData.put(getKey("node"), arg);
            return this;
        }

        public List<Node> getNodes() {
            return (List<Node>) get("nodes");
        }

        public QueryRoot setNodes(List<Node> arg) {
            optimisticData.put(getKey("nodes"), arg);
            return this;
        }

        public Shop getShop() {
            return (Shop) get("shop");
        }

        public QueryRoot setShop(Shop arg) {
            optimisticData.put(getKey("shop"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "customer": return true;

                case "node": return false;

                case "nodes": return false;

                case "shop": return true;

                default: return false;
            }
        }
    }

    public interface SelectedOptionQueryDefinition {
        void define(SelectedOptionQuery _queryBuilder);
    }

    /**
    * Custom properties that a shop owner can use to define product variants. Multiple options can exist.
    * Options are represented as: 
    * <ul>
    *  option1, option2, option3, etc. 
    * </ul>
    */
    public static class SelectedOptionQuery extends Query<SelectedOptionQuery> {
        SelectedOptionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The product option’s name.
        */
        public SelectedOptionQuery name() {
            startField("name");

            return this;
        }

        /**
        * The product option’s value.
        */
        public SelectedOptionQuery value() {
            startField("value");

            return this;
        }
    }

    /**
    * Custom properties that a shop owner can use to define product variants. Multiple options can exist.
    * Options are represented as: 
    * <ul>
    *  option1, option2, option3, etc. 
    * </ul>
    */
    public static class SelectedOption extends AbstractResponse<SelectedOption> {
        public SelectedOption() {
        }

        public SelectedOption(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "name": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "value": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "SelectedOption";
        }

        /**
        * The product option’s name.
        */

        public String getName() {
            return (String) get("name");
        }

        public SelectedOption setName(String arg) {
            optimisticData.put(getKey("name"), arg);
            return this;
        }

        /**
        * The product option’s value.
        */

        public String getValue() {
            return (String) get("value");
        }

        public SelectedOption setValue(String arg) {
            optimisticData.put(getKey("value"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "name": return false;

                case "value": return false;

                default: return false;
            }
        }
    }

    public static class SelectedOptionInput implements Serializable {
        private String name;

        private String value;

        public SelectedOptionInput(String name, String value) {
            this.name = name;

            this.value = value;
        }

        public String getName() {
            return name;
        }

        public SelectedOptionInput setName(String name) {
            this.name = name;
            return this;
        }

        public String getValue() {
            return value;
        }

        public SelectedOptionInput setValue(String value) {
            this.value = value;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("name:");
            Query.appendQuotedString(_queryBuilder, name.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("value:");
            Query.appendQuotedString(_queryBuilder, value.toString());

            _queryBuilder.append('}');
        }
    }

    public interface ShippingRateQueryDefinition {
        void define(ShippingRateQuery _queryBuilder);
    }

    /**
    * A shipping rate to be applied to a checkout.
    */
    public static class ShippingRateQuery extends Query<ShippingRateQuery> {
        ShippingRateQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * Human-readable unique identifier for this shipping rate.
        */
        public ShippingRateQuery handle() {
            startField("handle");

            return this;
        }

        /**
        * Price of this shipping rate.
        */
        public ShippingRateQuery price() {
            startField("price");

            return this;
        }

        /**
        * Title of this shipping rate.
        */
        public ShippingRateQuery title() {
            startField("title");

            return this;
        }
    }

    /**
    * A shipping rate to be applied to a checkout.
    */
    public static class ShippingRate extends AbstractResponse<ShippingRate> {
        public ShippingRate() {
        }

        public ShippingRate(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "handle": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "price": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "title": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "ShippingRate";
        }

        /**
        * Human-readable unique identifier for this shipping rate.
        */

        public String getHandle() {
            return (String) get("handle");
        }

        public ShippingRate setHandle(String arg) {
            optimisticData.put(getKey("handle"), arg);
            return this;
        }

        /**
        * Price of this shipping rate.
        */

        public BigDecimal getPrice() {
            return (BigDecimal) get("price");
        }

        public ShippingRate setPrice(BigDecimal arg) {
            optimisticData.put(getKey("price"), arg);
            return this;
        }

        /**
        * Title of this shipping rate.
        */

        public String getTitle() {
            return (String) get("title");
        }

        public ShippingRate setTitle(String arg) {
            optimisticData.put(getKey("title"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "handle": return false;

                case "price": return false;

                case "title": return false;

                default: return false;
            }
        }
    }

    public interface ShopQueryDefinition {
        void define(ShopQuery _queryBuilder);
    }

    /**
    * Shop represents a collection of the general settings and information about the shop.
    */
    public static class ShopQuery extends Query<ShopQuery> {
        ShopQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The url pointing to the endpoint to vault credit cards.
        */
        public ShopQuery cardVaultUrl() {
            startField("cardVaultUrl");

            return this;
        }

        public class CollectionsArguments extends Arguments {
            CollectionsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, false);
            }

            public CollectionsArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            public CollectionsArguments sortKey(CollectionSortKeys value) {
                if (value != null) {
                    startArgument("sortKey");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

            public CollectionsArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Supported filter parameters: 
            * <ul>
            * <li>title</li>
            * <li>collection_type</li>
            * <li>updated_at</li>
            * <li>published_status</li>
            * 
            * </ul>
            */
            public CollectionsArguments query(String value) {
                if (value != null) {
                    startArgument("query");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }
        }

        public interface CollectionsArgumentsDefinition {
            void define(CollectionsArguments args);
        }

        /**
        * List of the shop’ collections.
        */
        public ShopQuery collections(int first, CollectionConnectionQueryDefinition queryDef) {
            return collections(first, args -> {}, queryDef);
        }

        /**
        * List of the shop’ collections.
        */
        public ShopQuery collections(int first, CollectionsArgumentsDefinition argsDef, CollectionConnectionQueryDefinition queryDef) {
            startField("collections");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new CollectionsArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CollectionConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The three-letter code for the currency that the shop accepts.
        */
        public ShopQuery currencyCode() {
            startField("currencyCode");

            return this;
        }

        /**
        * A description of the shop.
        */
        public ShopQuery description() {
            startField("description");

            return this;
        }

        /**
        * A string representing the way currency is formatted when the currency isn’t specified.
        */
        public ShopQuery moneyFormat() {
            startField("moneyFormat");

            return this;
        }

        /**
        * The shop’s name.
        */
        public ShopQuery name() {
            startField("name");

            return this;
        }

        /**
        * The shop’s primary domain.
        */
        public ShopQuery primaryDomain(DomainQueryDefinition queryDef) {
            startField("primaryDomain");

            _queryBuilder.append('{');
            queryDef.define(new DomainQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The shop’s privacy policy.
        */
        public ShopQuery privacyPolicy(ShopPolicyQueryDefinition queryDef) {
            startField("privacyPolicy");

            _queryBuilder.append('{');
            queryDef.define(new ShopPolicyQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public class ProductsArguments extends Arguments {
            ProductsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, false);
            }

            public ProductsArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            public ProductsArguments sortKey(ProductSortKeys value) {
                if (value != null) {
                    startArgument("sortKey");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

            public ProductsArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }

            /**
            * Supported filter parameters: 
            * <ul>
            * <li>title</li>
            * <li>product_type</li>
            * <li>vendor</li>
            * <li>created_at</li>
            * <li>updated_at</li>
            * <li>tag</li>
            * 
            * </ul>
            */
            public ProductsArguments query(String value) {
                if (value != null) {
                    startArgument("query");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }
        }

        public interface ProductsArgumentsDefinition {
            void define(ProductsArguments args);
        }

        /**
        * List of the shop’ products.
        */
        public ShopQuery products(int first, ProductConnectionQueryDefinition queryDef) {
            return products(first, args -> {}, queryDef);
        }

        /**
        * List of the shop’ products.
        */
        public ShopQuery products(int first, ProductsArgumentsDefinition argsDef, ProductConnectionQueryDefinition queryDef) {
            startField("products");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new ProductsArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new ProductConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The shop’s refund policy.
        */
        public ShopQuery refundPolicy(ShopPolicyQueryDefinition queryDef) {
            startField("refundPolicy");

            _queryBuilder.append('{');
            queryDef.define(new ShopPolicyQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        /**
        * The shop's Shopify Payments account id.
        */
        public ShopQuery shopifyPaymentsAccountId() {
            startField("shopifyPaymentsAccountId");

            return this;
        }

        /**
        * The shop’s terms of service.
        */
        public ShopQuery termsOfService(ShopPolicyQueryDefinition queryDef) {
            startField("termsOfService");

            _queryBuilder.append('{');
            queryDef.define(new ShopPolicyQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    /**
    * Shop represents a collection of the general settings and information about the shop.
    */
    public static class Shop extends AbstractResponse<Shop> {
        public Shop() {
        }

        public Shop(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cardVaultUrl": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "collections": {
                        responseData.put(key, new CollectionConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "currencyCode": {
                        responseData.put(key, CurrencyCode.fromGraphQl(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "description": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "moneyFormat": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "name": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "primaryDomain": {
                        responseData.put(key, new Domain(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "privacyPolicy": {
                        ShopPolicy optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ShopPolicy(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "products": {
                        responseData.put(key, new ProductConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "refundPolicy": {
                        ShopPolicy optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ShopPolicy(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "shopifyPaymentsAccountId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "termsOfService": {
                        ShopPolicy optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ShopPolicy(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "Shop";
        }

        /**
        * The url pointing to the endpoint to vault credit cards.
        */

        public String getCardVaultUrl() {
            return (String) get("cardVaultUrl");
        }

        public Shop setCardVaultUrl(String arg) {
            optimisticData.put(getKey("cardVaultUrl"), arg);
            return this;
        }

        /**
        * List of the shop’ collections.
        */

        public CollectionConnection getCollections() {
            return (CollectionConnection) get("collections");
        }

        public Shop setCollections(CollectionConnection arg) {
            optimisticData.put(getKey("collections"), arg);
            return this;
        }

        /**
        * The three-letter code for the currency that the shop accepts.
        */

        public CurrencyCode getCurrencyCode() {
            return (CurrencyCode) get("currencyCode");
        }

        public Shop setCurrencyCode(CurrencyCode arg) {
            optimisticData.put(getKey("currencyCode"), arg);
            return this;
        }

        /**
        * A description of the shop.
        */

        public String getDescription() {
            return (String) get("description");
        }

        public Shop setDescription(String arg) {
            optimisticData.put(getKey("description"), arg);
            return this;
        }

        /**
        * A string representing the way currency is formatted when the currency isn’t specified.
        */

        public String getMoneyFormat() {
            return (String) get("moneyFormat");
        }

        public Shop setMoneyFormat(String arg) {
            optimisticData.put(getKey("moneyFormat"), arg);
            return this;
        }

        /**
        * The shop’s name.
        */

        public String getName() {
            return (String) get("name");
        }

        public Shop setName(String arg) {
            optimisticData.put(getKey("name"), arg);
            return this;
        }

        /**
        * The shop’s primary domain.
        */

        public Domain getPrimaryDomain() {
            return (Domain) get("primaryDomain");
        }

        public Shop setPrimaryDomain(Domain arg) {
            optimisticData.put(getKey("primaryDomain"), arg);
            return this;
        }

        /**
        * The shop’s privacy policy.
        */

        public ShopPolicy getPrivacyPolicy() {
            return (ShopPolicy) get("privacyPolicy");
        }

        public Shop setPrivacyPolicy(ShopPolicy arg) {
            optimisticData.put(getKey("privacyPolicy"), arg);
            return this;
        }

        /**
        * List of the shop’ products.
        */

        public ProductConnection getProducts() {
            return (ProductConnection) get("products");
        }

        public Shop setProducts(ProductConnection arg) {
            optimisticData.put(getKey("products"), arg);
            return this;
        }

        /**
        * The shop’s refund policy.
        */

        public ShopPolicy getRefundPolicy() {
            return (ShopPolicy) get("refundPolicy");
        }

        public Shop setRefundPolicy(ShopPolicy arg) {
            optimisticData.put(getKey("refundPolicy"), arg);
            return this;
        }

        /**
        * The shop's Shopify Payments account id.
        */

        public String getShopifyPaymentsAccountId() {
            return (String) get("shopifyPaymentsAccountId");
        }

        public Shop setShopifyPaymentsAccountId(String arg) {
            optimisticData.put(getKey("shopifyPaymentsAccountId"), arg);
            return this;
        }

        /**
        * The shop’s terms of service.
        */

        public ShopPolicy getTermsOfService() {
            return (ShopPolicy) get("termsOfService");
        }

        public Shop setTermsOfService(ShopPolicy arg) {
            optimisticData.put(getKey("termsOfService"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "cardVaultUrl": return false;

                case "collections": return true;

                case "currencyCode": return false;

                case "description": return false;

                case "moneyFormat": return false;

                case "name": return false;

                case "primaryDomain": return true;

                case "privacyPolicy": return true;

                case "products": return true;

                case "refundPolicy": return true;

                case "shopifyPaymentsAccountId": return false;

                case "termsOfService": return true;

                default: return false;
            }
        }
    }

    public interface ShopPolicyQueryDefinition {
        void define(ShopPolicyQuery _queryBuilder);
    }

    /**
    * Policy that a merchant has configured for their store, such as their refund or privacy policy.
    */
    public static class ShopPolicyQuery extends Query<ShopPolicyQuery> {
        ShopPolicyQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        /**
        * Policy text, maximum size of 64kb.
        */
        public ShopPolicyQuery body() {
            startField("body");

            return this;
        }

        /**
        * Policy’s title.
        */
        public ShopPolicyQuery title() {
            startField("title");

            return this;
        }

        /**
        * Public URL to the policy.
        */
        public ShopPolicyQuery url() {
            startField("url");

            return this;
        }
    }

    /**
    * Policy that a merchant has configured for their store, such as their refund or privacy policy.
    */
    public static class ShopPolicy extends AbstractResponse<ShopPolicy> implements Node {
        public ShopPolicy() {
        }

        public ShopPolicy(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "body": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "title": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "url": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public ShopPolicy(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public String getGraphQlTypeName() {
            return "ShopPolicy";
        }

        /**
        * Policy text, maximum size of 64kb.
        */

        public String getBody() {
            return (String) get("body");
        }

        public ShopPolicy setBody(String arg) {
            optimisticData.put(getKey("body"), arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        /**
        * Policy’s title.
        */

        public String getTitle() {
            return (String) get("title");
        }

        public ShopPolicy setTitle(String arg) {
            optimisticData.put(getKey("title"), arg);
            return this;
        }

        /**
        * Public URL to the policy.
        */

        public String getUrl() {
            return (String) get("url");
        }

        public ShopPolicy setUrl(String arg) {
            optimisticData.put(getKey("url"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "body": return false;

                case "id": return false;

                case "title": return false;

                case "url": return false;

                default: return false;
            }
        }
    }

    public static class TokenizedPaymentInput implements Serializable {
        private BigDecimal amount;

        private String idempotencyKey;

        private MailingAddressInput billingAddress;

        private String type;

        private String paymentData;

        private Boolean test;

        private String identifier;

        public TokenizedPaymentInput(BigDecimal amount, String idempotencyKey, MailingAddressInput billingAddress, String type, String paymentData) {
            this.amount = amount;

            this.idempotencyKey = idempotencyKey;

            this.billingAddress = billingAddress;

            this.type = type;

            this.paymentData = paymentData;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public TokenizedPaymentInput setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public String getIdempotencyKey() {
            return idempotencyKey;
        }

        public TokenizedPaymentInput setIdempotencyKey(String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        public MailingAddressInput getBillingAddress() {
            return billingAddress;
        }

        public TokenizedPaymentInput setBillingAddress(MailingAddressInput billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public String getType() {
            return type;
        }

        public TokenizedPaymentInput setType(String type) {
            this.type = type;
            return this;
        }

        public String getPaymentData() {
            return paymentData;
        }

        public TokenizedPaymentInput setPaymentData(String paymentData) {
            this.paymentData = paymentData;
            return this;
        }

        public Boolean getTest() {
            return test;
        }

        public TokenizedPaymentInput setTest(Boolean test) {
            this.test = test;
            return this;
        }

        public String getIdentifier() {
            return identifier;
        }

        public TokenizedPaymentInput setIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("amount:");
            Query.appendQuotedString(_queryBuilder, amount.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("idempotencyKey:");
            Query.appendQuotedString(_queryBuilder, idempotencyKey.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("billingAddress:");
            billingAddress.appendTo(_queryBuilder);

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("type:");
            Query.appendQuotedString(_queryBuilder, type.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("paymentData:");
            Query.appendQuotedString(_queryBuilder, paymentData.toString());

            if (test != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("test:");
                _queryBuilder.append(test);
            }

            if (identifier != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("identifier:");
                Query.appendQuotedString(_queryBuilder, identifier.toString());
            }

            _queryBuilder.append('}');
        }
    }

    public interface TransactionQueryDefinition {
        void define(TransactionQuery _queryBuilder);
    }

    /**
    * An object representing exchange of money for a product or service.
    */
    public static class TransactionQuery extends Query<TransactionQuery> {
        TransactionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * The amount of money that the transaction was for.
        */
        public TransactionQuery amount() {
            startField("amount");

            return this;
        }

        /**
        * The kind of the transaction.
        */
        public TransactionQuery kind() {
            startField("kind");

            return this;
        }

        /**
        * The status of the transaction
        */
        public TransactionQuery status() {
            startField("status");

            return this;
        }

        /**
        * Whether the transaction was done in test mode or not
        */
        public TransactionQuery test() {
            startField("test");

            return this;
        }
    }

    /**
    * An object representing exchange of money for a product or service.
    */
    public static class Transaction extends AbstractResponse<Transaction> {
        public Transaction() {
        }

        public Transaction(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "amount": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "kind": {
                        responseData.put(key, TransactionKind.fromGraphQl(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "status": {
                        responseData.put(key, TransactionStatus.fromGraphQl(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "test": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "Transaction";
        }

        /**
        * The amount of money that the transaction was for.
        */

        public BigDecimal getAmount() {
            return (BigDecimal) get("amount");
        }

        public Transaction setAmount(BigDecimal arg) {
            optimisticData.put(getKey("amount"), arg);
            return this;
        }

        /**
        * The kind of the transaction.
        */

        public TransactionKind getKind() {
            return (TransactionKind) get("kind");
        }

        public Transaction setKind(TransactionKind arg) {
            optimisticData.put(getKey("kind"), arg);
            return this;
        }

        /**
        * The status of the transaction
        */

        public TransactionStatus getStatus() {
            return (TransactionStatus) get("status");
        }

        public Transaction setStatus(TransactionStatus arg) {
            optimisticData.put(getKey("status"), arg);
            return this;
        }

        /**
        * Whether the transaction was done in test mode or not
        */

        public Boolean getTest() {
            return (Boolean) get("test");
        }

        public Transaction setTest(Boolean arg) {
            optimisticData.put(getKey("test"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "amount": return false;

                case "kind": return false;

                case "status": return false;

                case "test": return false;

                default: return false;
            }
        }
    }

    public enum TransactionKind {
        AUTHORIZATION,

        CAPTURE,

        CHANGE,

        EMV_AUTHORIZATION,

        SALE,

        UNKNOWN_VALUE;

        public static TransactionKind fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "AUTHORIZATION": {
                    return AUTHORIZATION;
                }

                case "CAPTURE": {
                    return CAPTURE;
                }

                case "CHANGE": {
                    return CHANGE;
                }

                case "EMV_AUTHORIZATION": {
                    return EMV_AUTHORIZATION;
                }

                case "SALE": {
                    return SALE;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case AUTHORIZATION: {
                    return "AUTHORIZATION";
                }

                case CAPTURE: {
                    return "CAPTURE";
                }

                case CHANGE: {
                    return "CHANGE";
                }

                case EMV_AUTHORIZATION: {
                    return "EMV_AUTHORIZATION";
                }

                case SALE: {
                    return "SALE";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public enum TransactionStatus {
        ERROR,

        FAILURE,

        PENDING,

        SUCCESS,

        UNKNOWN_VALUE;

        public static TransactionStatus fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "ERROR": {
                    return ERROR;
                }

                case "FAILURE": {
                    return FAILURE;
                }

                case "PENDING": {
                    return PENDING;
                }

                case "SUCCESS": {
                    return SUCCESS;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case ERROR: {
                    return "ERROR";
                }

                case FAILURE: {
                    return "FAILURE";
                }

                case PENDING: {
                    return "PENDING";
                }

                case SUCCESS: {
                    return "SUCCESS";
                }

                default: {
                    return "";
                }
            }
        }
    }

    public interface UserErrorQueryDefinition {
        void define(UserErrorQuery _queryBuilder);
    }

    /**
    * Represents an error in the input of a mutation.
    */
    public static class UserErrorQuery extends Query<UserErrorQuery> {
        UserErrorQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        /**
        * Path to input field which caused the error.
        */
        public UserErrorQuery field() {
            startField("field");

            return this;
        }

        /**
        * The error message.
        */
        public UserErrorQuery message() {
            startField("message");

            return this;
        }
    }

    /**
    * Represents an error in the input of a mutation.
    */
    public static class UserError extends AbstractResponse<UserError> {
        public UserError() {
        }

        public UserError(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "field": {
                        List<String> optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            List<String> list1 = new ArrayList<>();
                            for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                                list1.add(jsonAsString(element1, key));
                            }

                            optional1 = list1;
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "message": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "__typename": {
                        responseData.put(key, jsonAsString(field.getValue(), key));
                        break;
                    }
                    default: {
                        throw new SchemaViolationError(this, key, field.getValue());
                    }
                }
            }
        }

        public String getGraphQlTypeName() {
            return "UserError";
        }

        /**
        * Path to input field which caused the error.
        */

        public List<String> getField() {
            return (List<String>) get("field");
        }

        public UserError setField(List<String> arg) {
            optimisticData.put(getKey("field"), arg);
            return this;
        }

        /**
        * The error message.
        */

        public String getMessage() {
            return (String) get("message");
        }

        public UserError setMessage(String arg) {
            optimisticData.put(getKey("message"), arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (getFieldName(key)) {
                case "field": return false;

                case "message": return false;

                default: return false;
            }
        }
    }

    /**
    * Units of measurements for weight.
    */
    public enum WeightUnit {
        GRAMS,

        KILOGRAMS,

        OUNCES,

        POUNDS,

        UNKNOWN_VALUE;

        public static WeightUnit fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "GRAMS": {
                    return GRAMS;
                }

                case "KILOGRAMS": {
                    return KILOGRAMS;
                }

                case "OUNCES": {
                    return OUNCES;
                }

                case "POUNDS": {
                    return POUNDS;
                }

                default: {
                    return UNKNOWN_VALUE;
                }
            }
        }
        public String toString() {
            switch (this) {
                case GRAMS: {
                    return "GRAMS";
                }

                case KILOGRAMS: {
                    return "KILOGRAMS";
                }

                case OUNCES: {
                    return "OUNCES";
                }

                case POUNDS: {
                    return "POUNDS";
                }

                default: {
                    return "";
                }
            }
        }
    }
}
