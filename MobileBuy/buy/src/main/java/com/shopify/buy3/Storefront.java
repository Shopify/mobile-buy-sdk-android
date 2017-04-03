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

    public interface AttributeQueryDefinition {
        void define(AttributeQuery _queryBuilder);
    }

    public static class AttributeQuery extends Query<AttributeQuery> {
        AttributeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public AttributeQuery key() {
            startField("key");

            return this;
        }

        public AttributeQuery value() {
            startField("value");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            return children;
        }

        public String getGraphQlTypeName() {
            return "Attribute";
        }

        public String getKey() {
            return (String) get("key");
        }

        public Attribute setKey(String arg) {
            optimisticData.put("key", arg);
            return this;
        }

        public String getValue() {
            return (String) get("value");
        }

        public Attribute setValue(String arg) {
            optimisticData.put("value", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

    public interface CheckoutQueryDefinition {
        void define(CheckoutQuery _queryBuilder);
    }

    public static class CheckoutQuery extends Query<CheckoutQuery> {
        CheckoutQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public CheckoutQuery completedAt() {
            startField("completedAt");

            return this;
        }

        public CheckoutQuery createdAt() {
            startField("createdAt");

            return this;
        }

        public CheckoutQuery currencyCode() {
            startField("currencyCode");

            return this;
        }

        public CheckoutQuery customAttributes(AttributeQueryDefinition queryDef) {
            startField("customAttributes");

            _queryBuilder.append('{');
            queryDef.define(new AttributeQuery(_queryBuilder));
            _queryBuilder.append('}');

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

        public CheckoutQuery lineItems(int first, LineItemConnectionQueryDefinition queryDef) {
            return lineItems(first, args -> {}, queryDef);
        }

        public CheckoutQuery lineItems(int first, LineItemsArgumentsDefinition argsDef, LineItemConnectionQueryDefinition queryDef) {
            startField("lineItems");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new LineItemsArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new LineItemConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutQuery note() {
            startField("note");

            return this;
        }

        public CheckoutQuery order(OrderQueryDefinition queryDef) {
            startField("order");

            _queryBuilder.append('{');
            queryDef.define(new OrderQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutQuery orderStatusUrl() {
            startField("orderStatusUrl");

            return this;
        }

        public CheckoutQuery paymentDue() {
            startField("paymentDue");

            return this;
        }

        public CheckoutQuery ready() {
            startField("ready");

            return this;
        }

        public CheckoutQuery requiresShipping() {
            startField("requiresShipping");

            return this;
        }

        public CheckoutQuery shippingAddress(MailingAddressQueryDefinition queryDef) {
            startField("shippingAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutQuery shippingLine(ShippingRateQueryDefinition queryDef) {
            startField("shippingLine");

            _queryBuilder.append('{');
            queryDef.define(new ShippingRateQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutQuery subtotalPrice() {
            startField("subtotalPrice");

            return this;
        }

        public CheckoutQuery taxExempt() {
            startField("taxExempt");

            return this;
        }

        public CheckoutQuery taxesIncluded() {
            startField("taxesIncluded");

            return this;
        }

        public CheckoutQuery totalPrice() {
            startField("totalPrice");

            return this;
        }

        public CheckoutQuery totalTax() {
            startField("totalTax");

            return this;
        }

        public CheckoutQuery updatedAt() {
            startField("updatedAt");

            return this;
        }

        public CheckoutQuery webUrl() {
            startField("webUrl");

            return this;
        }
    }

    public static class Checkout extends AbstractResponse<Checkout> implements Node {
        public Checkout() {
        }

        public Checkout(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
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

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "lineItems": {
                        responseData.put(key, new LineItemConnection(jsonAsObject(field.getValue(), key)));

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            if (getCustomAttributes() != null) {
                for (Attribute elem: getCustomAttributes()) {
                    children.addAll(elem.getNodes());
                }
            }

            if (getLineItems() != null) {
                children.addAll(getLineItems().getNodes());
            }

            if (getOrder() != null) {
                children.addAll(getOrder().getNodes());
            }

            if (getShippingAddress() != null) {
                children.addAll(getShippingAddress().getNodes());
            }

            if (getShippingLine() != null) {
                children.addAll(getShippingLine().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "Checkout";
        }

        public DateTime getCompletedAt() {
            return (DateTime) get("completedAt");
        }

        public Checkout setCompletedAt(DateTime arg) {
            optimisticData.put("completedAt", arg);
            return this;
        }

        public DateTime getCreatedAt() {
            return (DateTime) get("createdAt");
        }

        public Checkout setCreatedAt(DateTime arg) {
            optimisticData.put("createdAt", arg);
            return this;
        }

        public CurrencyCode getCurrencyCode() {
            return (CurrencyCode) get("currencyCode");
        }

        public Checkout setCurrencyCode(CurrencyCode arg) {
            optimisticData.put("currencyCode", arg);
            return this;
        }

        public List<Attribute> getCustomAttributes() {
            return (List<Attribute>) get("customAttributes");
        }

        public Checkout setCustomAttributes(List<Attribute> arg) {
            optimisticData.put("customAttributes", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public LineItemConnection getLineItems() {
            return (LineItemConnection) get("lineItems");
        }

        public Checkout setLineItems(LineItemConnection arg) {
            optimisticData.put("lineItems", arg);
            return this;
        }

        public String getNote() {
            return (String) get("note");
        }

        public Checkout setNote(String arg) {
            optimisticData.put("note", arg);
            return this;
        }

        public Order getOrder() {
            return (Order) get("order");
        }

        public Checkout setOrder(Order arg) {
            optimisticData.put("order", arg);
            return this;
        }

        public String getOrderStatusUrl() {
            return (String) get("orderStatusUrl");
        }

        public Checkout setOrderStatusUrl(String arg) {
            optimisticData.put("orderStatusUrl", arg);
            return this;
        }

        public BigDecimal getPaymentDue() {
            return (BigDecimal) get("paymentDue");
        }

        public Checkout setPaymentDue(BigDecimal arg) {
            optimisticData.put("paymentDue", arg);
            return this;
        }

        public Boolean getReady() {
            return (Boolean) get("ready");
        }

        public Checkout setReady(Boolean arg) {
            optimisticData.put("ready", arg);
            return this;
        }

        public Boolean getRequiresShipping() {
            return (Boolean) get("requiresShipping");
        }

        public Checkout setRequiresShipping(Boolean arg) {
            optimisticData.put("requiresShipping", arg);
            return this;
        }

        public MailingAddress getShippingAddress() {
            return (MailingAddress) get("shippingAddress");
        }

        public Checkout setShippingAddress(MailingAddress arg) {
            optimisticData.put("shippingAddress", arg);
            return this;
        }

        public ShippingRate getShippingLine() {
            return (ShippingRate) get("shippingLine");
        }

        public Checkout setShippingLine(ShippingRate arg) {
            optimisticData.put("shippingLine", arg);
            return this;
        }

        public BigDecimal getSubtotalPrice() {
            return (BigDecimal) get("subtotalPrice");
        }

        public Checkout setSubtotalPrice(BigDecimal arg) {
            optimisticData.put("subtotalPrice", arg);
            return this;
        }

        public Boolean getTaxExempt() {
            return (Boolean) get("taxExempt");
        }

        public Checkout setTaxExempt(Boolean arg) {
            optimisticData.put("taxExempt", arg);
            return this;
        }

        public Boolean getTaxesIncluded() {
            return (Boolean) get("taxesIncluded");
        }

        public Checkout setTaxesIncluded(Boolean arg) {
            optimisticData.put("taxesIncluded", arg);
            return this;
        }

        public BigDecimal getTotalPrice() {
            return (BigDecimal) get("totalPrice");
        }

        public Checkout setTotalPrice(BigDecimal arg) {
            optimisticData.put("totalPrice", arg);
            return this;
        }

        public BigDecimal getTotalTax() {
            return (BigDecimal) get("totalTax");
        }

        public Checkout setTotalTax(BigDecimal arg) {
            optimisticData.put("totalTax", arg);
            return this;
        }

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Checkout setUpdatedAt(DateTime arg) {
            optimisticData.put("updatedAt", arg);
            return this;
        }

        public String getWebUrl() {
            return (String) get("webUrl");
        }

        public Checkout setWebUrl(String arg) {
            optimisticData.put("webUrl", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "completedAt": return false;

                case "createdAt": return false;

                case "currencyCode": return false;

                case "customAttributes": return true;

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

    public static class CheckoutAddLineItemsInput implements Serializable {
        private ID checkoutId;

        private String clientMutationId;

        private List<LineItemInput> lineItems;

        public CheckoutAddLineItemsInput(ID checkoutId) {
            this.checkoutId = checkoutId;
        }

        public ID getCheckoutId() {
            return checkoutId;
        }

        public CheckoutAddLineItemsInput setCheckoutId(ID checkoutId) {
            this.checkoutId = checkoutId;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CheckoutAddLineItemsInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public List<LineItemInput> getLineItems() {
            return lineItems;
        }

        public CheckoutAddLineItemsInput setLineItems(List<LineItemInput> lineItems) {
            this.lineItems = lineItems;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            if (lineItems != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("lineItems:");
                _queryBuilder.append('[');

                String listSeperator1 = "";
                for (LineItemInput item1 : lineItems) {
                    _queryBuilder.append(listSeperator1);
                    listSeperator1 = ",";
                    item1.appendTo(_queryBuilder);
                }
                _queryBuilder.append(']');
            }

            _queryBuilder.append('}');
        }
    }

    public interface CheckoutAddLineItemsPayloadQueryDefinition {
        void define(CheckoutAddLineItemsPayloadQuery _queryBuilder);
    }

    public static class CheckoutAddLineItemsPayloadQuery extends Query<CheckoutAddLineItemsPayloadQuery> {
        CheckoutAddLineItemsPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CheckoutAddLineItemsPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutAddLineItemsPayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CheckoutAddLineItemsPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class CheckoutAddLineItemsPayload extends AbstractResponse<CheckoutAddLineItemsPayload> {
        public CheckoutAddLineItemsPayload() {
        }

        public CheckoutAddLineItemsPayload(JsonObject fields) throws SchemaViolationError {
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

                    case "clientMutationId": {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCheckout() != null) {
                children.addAll(getCheckout().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CheckoutAddLineItemsPayload";
        }

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutAddLineItemsPayload setCheckout(Checkout arg) {
            optimisticData.put("checkout", arg);
            return this;
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CheckoutAddLineItemsPayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutAddLineItemsPayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "checkout": return true;

                case "clientMutationId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CheckoutAttributesUpdateInput implements Serializable {
        private ID checkoutId;

        private String clientMutationId;

        private List<AttributeInput> customAttributes;

        private String note;

        public CheckoutAttributesUpdateInput(ID checkoutId) {
            this.checkoutId = checkoutId;
        }

        public ID getCheckoutId() {
            return checkoutId;
        }

        public CheckoutAttributesUpdateInput setCheckoutId(ID checkoutId) {
            this.checkoutId = checkoutId;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CheckoutAttributesUpdateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public List<AttributeInput> getCustomAttributes() {
            return customAttributes;
        }

        public CheckoutAttributesUpdateInput setCustomAttributes(List<AttributeInput> customAttributes) {
            this.customAttributes = customAttributes;
            return this;
        }

        public String getNote() {
            return note;
        }

        public CheckoutAttributesUpdateInput setNote(String note) {
            this.note = note;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
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

            if (note != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("note:");
                Query.appendQuotedString(_queryBuilder, note.toString());
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

        public CheckoutAttributesUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutAttributesUpdatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

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

                    case "clientMutationId": {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCheckout() != null) {
                children.addAll(getCheckout().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CheckoutAttributesUpdatePayload";
        }

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutAttributesUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put("checkout", arg);
            return this;
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CheckoutAttributesUpdatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutAttributesUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "checkout": return true;

                case "clientMutationId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CheckoutCompleteWithCreditCardInput implements Serializable {
        private BigDecimal amount;

        private MailingAddressInput billingAddress;

        private ID checkoutId;

        private String idempotencyKey;

        private String vaultId;

        private String clientMutationId;

        private Boolean test;

        public CheckoutCompleteWithCreditCardInput(BigDecimal amount, MailingAddressInput billingAddress, ID checkoutId, String idempotencyKey, String vaultId) {
            this.amount = amount;

            this.billingAddress = billingAddress;

            this.checkoutId = checkoutId;

            this.idempotencyKey = idempotencyKey;

            this.vaultId = vaultId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public CheckoutCompleteWithCreditCardInput setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public MailingAddressInput getBillingAddress() {
            return billingAddress;
        }

        public CheckoutCompleteWithCreditCardInput setBillingAddress(MailingAddressInput billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public ID getCheckoutId() {
            return checkoutId;
        }

        public CheckoutCompleteWithCreditCardInput setCheckoutId(ID checkoutId) {
            this.checkoutId = checkoutId;
            return this;
        }

        public String getIdempotencyKey() {
            return idempotencyKey;
        }

        public CheckoutCompleteWithCreditCardInput setIdempotencyKey(String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        public String getVaultId() {
            return vaultId;
        }

        public CheckoutCompleteWithCreditCardInput setVaultId(String vaultId) {
            this.vaultId = vaultId;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CheckoutCompleteWithCreditCardInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public Boolean getTest() {
            return test;
        }

        public CheckoutCompleteWithCreditCardInput setTest(Boolean test) {
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
            _queryBuilder.append("billingAddress:");
            billingAddress.appendTo(_queryBuilder);

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("idempotencyKey:");
            Query.appendQuotedString(_queryBuilder, idempotencyKey.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("vaultId:");
            Query.appendQuotedString(_queryBuilder, vaultId.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            if (test != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("test:");
                _queryBuilder.append(test);
            }

            _queryBuilder.append('}');
        }
    }

    public interface CheckoutCompleteWithCreditCardPayloadQueryDefinition {
        void define(CheckoutCompleteWithCreditCardPayloadQuery _queryBuilder);
    }

    public static class CheckoutCompleteWithCreditCardPayloadQuery extends Query<CheckoutCompleteWithCreditCardPayloadQuery> {
        CheckoutCompleteWithCreditCardPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CheckoutCompleteWithCreditCardPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutCompleteWithCreditCardPayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CheckoutCompleteWithCreditCardPayloadQuery payment(PaymentQueryDefinition queryDef) {
            startField("payment");

            _queryBuilder.append('{');
            queryDef.define(new PaymentQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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

                    case "clientMutationId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCheckout() != null) {
                children.addAll(getCheckout().getNodes());
            }

            if (getPayment() != null) {
                children.addAll(getPayment().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CheckoutCompleteWithCreditCardPayload";
        }

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCompleteWithCreditCardPayload setCheckout(Checkout arg) {
            optimisticData.put("checkout", arg);
            return this;
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CheckoutCompleteWithCreditCardPayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public Payment getPayment() {
            return (Payment) get("payment");
        }

        public CheckoutCompleteWithCreditCardPayload setPayment(Payment arg) {
            optimisticData.put("payment", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCompleteWithCreditCardPayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "checkout": return true;

                case "clientMutationId": return false;

                case "payment": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CheckoutCompleteWithTokenizedPaymentInput implements Serializable {
        private BigDecimal amount;

        private MailingAddressInput billingAddress;

        private ID checkoutId;

        private String idempotencyKey;

        private String paymentData;

        private String type;

        private String clientMutationId;

        private String identifier;

        private Boolean test;

        public CheckoutCompleteWithTokenizedPaymentInput(BigDecimal amount, MailingAddressInput billingAddress, ID checkoutId, String idempotencyKey, String paymentData, String type) {
            this.amount = amount;

            this.billingAddress = billingAddress;

            this.checkoutId = checkoutId;

            this.idempotencyKey = idempotencyKey;

            this.paymentData = paymentData;

            this.type = type;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public CheckoutCompleteWithTokenizedPaymentInput setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public MailingAddressInput getBillingAddress() {
            return billingAddress;
        }

        public CheckoutCompleteWithTokenizedPaymentInput setBillingAddress(MailingAddressInput billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public ID getCheckoutId() {
            return checkoutId;
        }

        public CheckoutCompleteWithTokenizedPaymentInput setCheckoutId(ID checkoutId) {
            this.checkoutId = checkoutId;
            return this;
        }

        public String getIdempotencyKey() {
            return idempotencyKey;
        }

        public CheckoutCompleteWithTokenizedPaymentInput setIdempotencyKey(String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        public String getPaymentData() {
            return paymentData;
        }

        public CheckoutCompleteWithTokenizedPaymentInput setPaymentData(String paymentData) {
            this.paymentData = paymentData;
            return this;
        }

        public String getType() {
            return type;
        }

        public CheckoutCompleteWithTokenizedPaymentInput setType(String type) {
            this.type = type;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CheckoutCompleteWithTokenizedPaymentInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public String getIdentifier() {
            return identifier;
        }

        public CheckoutCompleteWithTokenizedPaymentInput setIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Boolean getTest() {
            return test;
        }

        public CheckoutCompleteWithTokenizedPaymentInput setTest(Boolean test) {
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
            _queryBuilder.append("billingAddress:");
            billingAddress.appendTo(_queryBuilder);

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("idempotencyKey:");
            Query.appendQuotedString(_queryBuilder, idempotencyKey.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("paymentData:");
            Query.appendQuotedString(_queryBuilder, paymentData.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("type:");
            Query.appendQuotedString(_queryBuilder, type.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            if (identifier != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("identifier:");
                Query.appendQuotedString(_queryBuilder, identifier.toString());
            }

            if (test != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("test:");
                _queryBuilder.append(test);
            }

            _queryBuilder.append('}');
        }
    }

    public interface CheckoutCompleteWithTokenizedPaymentPayloadQueryDefinition {
        void define(CheckoutCompleteWithTokenizedPaymentPayloadQuery _queryBuilder);
    }

    public static class CheckoutCompleteWithTokenizedPaymentPayloadQuery extends Query<CheckoutCompleteWithTokenizedPaymentPayloadQuery> {
        CheckoutCompleteWithTokenizedPaymentPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CheckoutCompleteWithTokenizedPaymentPayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutCompleteWithTokenizedPaymentPayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CheckoutCompleteWithTokenizedPaymentPayloadQuery payment(PaymentQueryDefinition queryDef) {
            startField("payment");

            _queryBuilder.append('{');
            queryDef.define(new PaymentQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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

                    case "clientMutationId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCheckout() != null) {
                children.addAll(getCheckout().getNodes());
            }

            if (getPayment() != null) {
                children.addAll(getPayment().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CheckoutCompleteWithTokenizedPaymentPayload";
        }

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCompleteWithTokenizedPaymentPayload setCheckout(Checkout arg) {
            optimisticData.put("checkout", arg);
            return this;
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CheckoutCompleteWithTokenizedPaymentPayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public Payment getPayment() {
            return (Payment) get("payment");
        }

        public CheckoutCompleteWithTokenizedPaymentPayload setPayment(Payment arg) {
            optimisticData.put("payment", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCompleteWithTokenizedPaymentPayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "checkout": return true;

                case "clientMutationId": return false;

                case "payment": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CheckoutCreateInput implements Serializable {
        private String clientMutationId;

        private List<AttributeInput> customAttributes;

        private String email;

        private List<LineItemInput> lineItems;

        private String note;

        private MailingAddressInput shippingAddress;

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CheckoutCreateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public List<AttributeInput> getCustomAttributes() {
            return customAttributes;
        }

        public CheckoutCreateInput setCustomAttributes(List<AttributeInput> customAttributes) {
            this.customAttributes = customAttributes;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public CheckoutCreateInput setEmail(String email) {
            this.email = email;
            return this;
        }

        public List<LineItemInput> getLineItems() {
            return lineItems;
        }

        public CheckoutCreateInput setLineItems(List<LineItemInput> lineItems) {
            this.lineItems = lineItems;
            return this;
        }

        public String getNote() {
            return note;
        }

        public CheckoutCreateInput setNote(String note) {
            this.note = note;
            return this;
        }

        public MailingAddressInput getShippingAddress() {
            return shippingAddress;
        }

        public CheckoutCreateInput setShippingAddress(MailingAddressInput shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
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
                for (LineItemInput item1 : lineItems) {
                    _queryBuilder.append(listSeperator1);
                    listSeperator1 = ",";
                    item1.appendTo(_queryBuilder);
                }
                _queryBuilder.append(']');
            }

            if (note != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("note:");
                Query.appendQuotedString(_queryBuilder, note.toString());
            }

            if (shippingAddress != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("shippingAddress:");
                shippingAddress.appendTo(_queryBuilder);
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

        public CheckoutCreatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutCreatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

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

                    case "clientMutationId": {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCheckout() != null) {
                children.addAll(getCheckout().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CheckoutCreatePayload";
        }

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutCreatePayload setCheckout(Checkout arg) {
            optimisticData.put("checkout", arg);
            return this;
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CheckoutCreatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "checkout": return true;

                case "clientMutationId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CheckoutShippingAddressUpdateInput implements Serializable {
        private ID checkoutId;

        private MailingAddressInput shippingAddress;

        private String clientMutationId;

        public CheckoutShippingAddressUpdateInput(ID checkoutId, MailingAddressInput shippingAddress) {
            this.checkoutId = checkoutId;

            this.shippingAddress = shippingAddress;
        }

        public ID getCheckoutId() {
            return checkoutId;
        }

        public CheckoutShippingAddressUpdateInput setCheckoutId(ID checkoutId) {
            this.checkoutId = checkoutId;
            return this;
        }

        public MailingAddressInput getShippingAddress() {
            return shippingAddress;
        }

        public CheckoutShippingAddressUpdateInput setShippingAddress(MailingAddressInput shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CheckoutShippingAddressUpdateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("shippingAddress:");
            shippingAddress.appendTo(_queryBuilder);

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            _queryBuilder.append('}');
        }
    }

    public interface CheckoutShippingAddressUpdatePayloadQueryDefinition {
        void define(CheckoutShippingAddressUpdatePayloadQuery _queryBuilder);
    }

    public static class CheckoutShippingAddressUpdatePayloadQuery extends Query<CheckoutShippingAddressUpdatePayloadQuery> {
        CheckoutShippingAddressUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CheckoutShippingAddressUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutShippingAddressUpdatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

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

                    case "clientMutationId": {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCheckout() != null) {
                children.addAll(getCheckout().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CheckoutShippingAddressUpdatePayload";
        }

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutShippingAddressUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put("checkout", arg);
            return this;
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CheckoutShippingAddressUpdatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutShippingAddressUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "checkout": return true;

                case "clientMutationId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CheckoutShippingLineUpdateInput implements Serializable {
        private ID checkoutId;

        private String shippingRateHandle;

        private String clientMutationId;

        public CheckoutShippingLineUpdateInput(ID checkoutId, String shippingRateHandle) {
            this.checkoutId = checkoutId;

            this.shippingRateHandle = shippingRateHandle;
        }

        public ID getCheckoutId() {
            return checkoutId;
        }

        public CheckoutShippingLineUpdateInput setCheckoutId(ID checkoutId) {
            this.checkoutId = checkoutId;
            return this;
        }

        public String getShippingRateHandle() {
            return shippingRateHandle;
        }

        public CheckoutShippingLineUpdateInput setShippingRateHandle(String shippingRateHandle) {
            this.shippingRateHandle = shippingRateHandle;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CheckoutShippingLineUpdateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("checkoutId:");
            Query.appendQuotedString(_queryBuilder, checkoutId.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("shippingRateHandle:");
            Query.appendQuotedString(_queryBuilder, shippingRateHandle.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            _queryBuilder.append('}');
        }
    }

    public interface CheckoutShippingLineUpdatePayloadQueryDefinition {
        void define(CheckoutShippingLineUpdatePayloadQuery _queryBuilder);
    }

    public static class CheckoutShippingLineUpdatePayloadQuery extends Query<CheckoutShippingLineUpdatePayloadQuery> {
        CheckoutShippingLineUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CheckoutShippingLineUpdatePayloadQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CheckoutShippingLineUpdatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

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

                    case "clientMutationId": {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCheckout() != null) {
                children.addAll(getCheckout().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CheckoutShippingLineUpdatePayload";
        }

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public CheckoutShippingLineUpdatePayload setCheckout(Checkout arg) {
            optimisticData.put("checkout", arg);
            return this;
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CheckoutShippingLineUpdatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CheckoutShippingLineUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "checkout": return true;

                case "clientMutationId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface CollectionQueryDefinition {
        void define(CollectionQuery _queryBuilder);
    }

    public static class CollectionQuery extends Query<CollectionQuery> {
        CollectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public CollectionQuery descriptionHtml() {
            startField("descriptionHtml");

            return this;
        }

        public CollectionQuery descriptionPlainSummary() {
            startField("descriptionPlainSummary");

            return this;
        }

        public CollectionQuery handle() {
            startField("handle");

            return this;
        }

        public class ImageArguments extends Arguments {
            ImageArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            public ImageArguments maxWidth(Integer value) {
                if (value != null) {
                    startArgument("maxWidth");
                    _queryBuilder.append(value);
                }
                return this;
            }

            public ImageArguments maxHeight(Integer value) {
                if (value != null) {
                    startArgument("maxHeight");
                    _queryBuilder.append(value);
                }
                return this;
            }

            public ImageArguments crop(CropRegion value) {
                if (value != null) {
                    startArgument("crop");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

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

        public CollectionQuery image(ImageQueryDefinition queryDef) {
            return image(args -> {}, queryDef);
        }

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

        public CollectionQuery products(int first, ProductConnectionQueryDefinition queryDef) {
            return products(first, args -> {}, queryDef);
        }

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

        public CollectionQuery title() {
            startField("title");

            return this;
        }

        public CollectionQuery updatedAt() {
            startField("updatedAt");

            return this;
        }
    }

    public static class Collection extends AbstractResponse<Collection> implements Node {
        public Collection() {
        }

        public Collection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "descriptionHtml": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "descriptionPlainSummary": {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            if (getImage() != null) {
                children.addAll(getImage().getNodes());
            }

            if (getProducts() != null) {
                children.addAll(getProducts().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "Collection";
        }

        public String getDescriptionHtml() {
            return (String) get("descriptionHtml");
        }

        public Collection setDescriptionHtml(String arg) {
            optimisticData.put("descriptionHtml", arg);
            return this;
        }

        public String getDescriptionPlainSummary() {
            return (String) get("descriptionPlainSummary");
        }

        public Collection setDescriptionPlainSummary(String arg) {
            optimisticData.put("descriptionPlainSummary", arg);
            return this;
        }

        public String getHandle() {
            return (String) get("handle");
        }

        public Collection setHandle(String arg) {
            optimisticData.put("handle", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public Image getImage() {
            return (Image) get("image");
        }

        public Collection setImage(Image arg) {
            optimisticData.put("image", arg);
            return this;
        }

        public ProductConnection getProducts() {
            return (ProductConnection) get("products");
        }

        public Collection setProducts(ProductConnection arg) {
            optimisticData.put("products", arg);
            return this;
        }

        public String getTitle() {
            return (String) get("title");
        }

        public Collection setTitle(String arg) {
            optimisticData.put("title", arg);
            return this;
        }

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Collection setUpdatedAt(DateTime arg) {
            optimisticData.put("updatedAt", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "descriptionHtml": return false;

                case "descriptionPlainSummary": return false;

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

        public CollectionConnectionQuery edges(CollectionEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new CollectionEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getEdges() != null) {
                for (CollectionEdge elem: getEdges()) {
                    children.addAll(elem.getNodes());
                }
            }

            if (getPageInfo() != null) {
                children.addAll(getPageInfo().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CollectionConnection";
        }

        public List<CollectionEdge> getEdges() {
            return (List<CollectionEdge>) get("edges");
        }

        public CollectionConnection setEdges(List<CollectionEdge> arg) {
            optimisticData.put("edges", arg);
            return this;
        }

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public CollectionConnection setPageInfo(PageInfo arg) {
            optimisticData.put("pageInfo", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getNode() != null) {
                children.addAll(getNode().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CollectionEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public CollectionEdge setCursor(String arg) {
            optimisticData.put("cursor", arg);
            return this;
        }

        public Collection getNode() {
            return (Collection) get("node");
        }

        public CollectionEdge setNode(Collection arg) {
            optimisticData.put("node", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

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

        public CreditCardQuery maskedNumber() {
            startField("maskedNumber");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            return children;
        }

        public String getGraphQlTypeName() {
            return "CreditCard";
        }

        public String getBrand() {
            return (String) get("brand");
        }

        public CreditCard setBrand(String arg) {
            optimisticData.put("brand", arg);
            return this;
        }

        public Integer getExpiryMonth() {
            return (Integer) get("expiryMonth");
        }

        public CreditCard setExpiryMonth(Integer arg) {
            optimisticData.put("expiryMonth", arg);
            return this;
        }

        public Integer getExpiryYear() {
            return (Integer) get("expiryYear");
        }

        public CreditCard setExpiryYear(Integer arg) {
            optimisticData.put("expiryYear", arg);
            return this;
        }

        public String getFirstDigits() {
            return (String) get("firstDigits");
        }

        public CreditCard setFirstDigits(String arg) {
            optimisticData.put("firstDigits", arg);
            return this;
        }

        public String getFirstName() {
            return (String) get("firstName");
        }

        public CreditCard setFirstName(String arg) {
            optimisticData.put("firstName", arg);
            return this;
        }

        public String getLastDigits() {
            return (String) get("lastDigits");
        }

        public CreditCard setLastDigits(String arg) {
            optimisticData.put("lastDigits", arg);
            return this;
        }

        public String getLastName() {
            return (String) get("lastName");
        }

        public CreditCard setLastName(String arg) {
            optimisticData.put("lastName", arg);
            return this;
        }

        public String getMaskedNumber() {
            return (String) get("maskedNumber");
        }

        public CreditCard setMaskedNumber(String arg) {
            optimisticData.put("maskedNumber", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

    public enum CropRegion {
        BOTTOM,

        CENTER,

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

                case TOP: {
                    return "TOP";
                }

                default: {
                    return "";
                }
            }
        }
    }

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

    public static class CustomerQuery extends Query<CustomerQuery> {
        CustomerQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

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

        public CustomerQuery addresses(int first, MailingAddressConnectionQueryDefinition queryDef) {
            return addresses(first, args -> {}, queryDef);
        }

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

        public CustomerQuery createdAt() {
            startField("createdAt");

            return this;
        }

        public CustomerQuery defaultAddress(MailingAddressQueryDefinition queryDef) {
            startField("defaultAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CustomerQuery displayName() {
            startField("displayName");

            return this;
        }

        public CustomerQuery email() {
            startField("email");

            return this;
        }

        public CustomerQuery firstName() {
            startField("firstName");

            return this;
        }

        public CustomerQuery id() {
            startField("id");

            return this;
        }

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

        public CustomerQuery orders(int first, OrderConnectionQueryDefinition queryDef) {
            return orders(first, args -> {}, queryDef);
        }

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

        public CustomerQuery updatedAt() {
            startField("updatedAt");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getAddresses() != null) {
                children.addAll(getAddresses().getNodes());
            }

            if (getDefaultAddress() != null) {
                children.addAll(getDefaultAddress().getNodes());
            }

            if (getOrders() != null) {
                children.addAll(getOrders().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "Customer";
        }

        public Boolean getAcceptsMarketing() {
            return (Boolean) get("acceptsMarketing");
        }

        public Customer setAcceptsMarketing(Boolean arg) {
            optimisticData.put("acceptsMarketing", arg);
            return this;
        }

        public MailingAddressConnection getAddresses() {
            return (MailingAddressConnection) get("addresses");
        }

        public Customer setAddresses(MailingAddressConnection arg) {
            optimisticData.put("addresses", arg);
            return this;
        }

        public DateTime getCreatedAt() {
            return (DateTime) get("createdAt");
        }

        public Customer setCreatedAt(DateTime arg) {
            optimisticData.put("createdAt", arg);
            return this;
        }

        public MailingAddress getDefaultAddress() {
            return (MailingAddress) get("defaultAddress");
        }

        public Customer setDefaultAddress(MailingAddress arg) {
            optimisticData.put("defaultAddress", arg);
            return this;
        }

        public String getDisplayName() {
            return (String) get("displayName");
        }

        public Customer setDisplayName(String arg) {
            optimisticData.put("displayName", arg);
            return this;
        }

        public String getEmail() {
            return (String) get("email");
        }

        public Customer setEmail(String arg) {
            optimisticData.put("email", arg);
            return this;
        }

        public String getFirstName() {
            return (String) get("firstName");
        }

        public Customer setFirstName(String arg) {
            optimisticData.put("firstName", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public Customer setId(ID arg) {
            optimisticData.put("id", arg);
            return this;
        }

        public String getLastName() {
            return (String) get("lastName");
        }

        public Customer setLastName(String arg) {
            optimisticData.put("lastName", arg);
            return this;
        }

        public OrderConnection getOrders() {
            return (OrderConnection) get("orders");
        }

        public Customer setOrders(OrderConnection arg) {
            optimisticData.put("orders", arg);
            return this;
        }

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Customer setUpdatedAt(DateTime arg) {
            optimisticData.put("updatedAt", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

                case "updatedAt": return false;

                default: return false;
            }
        }
    }

    public interface CustomerAccessTokenQueryDefinition {
        void define(CustomerAccessTokenQuery _queryBuilder);
    }

    public static class CustomerAccessTokenQuery extends Query<CustomerAccessTokenQuery> {
        CustomerAccessTokenQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CustomerAccessTokenQuery accessToken() {
            startField("accessToken");

            return this;
        }

        public CustomerAccessTokenQuery expiresAt() {
            startField("expiresAt");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            return children;
        }

        public String getGraphQlTypeName() {
            return "CustomerAccessToken";
        }

        public String getAccessToken() {
            return (String) get("accessToken");
        }

        public CustomerAccessToken setAccessToken(String arg) {
            optimisticData.put("accessToken", arg);
            return this;
        }

        public DateTime getExpiresAt() {
            return (DateTime) get("expiresAt");
        }

        public CustomerAccessToken setExpiresAt(DateTime arg) {
            optimisticData.put("expiresAt", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "accessToken": return false;

                case "expiresAt": return false;

                default: return false;
            }
        }
    }

    public static class CustomerAccessTokenCreateInput implements Serializable {
        private String email;

        private String password;

        private String clientMutationId;

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

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CustomerAccessTokenCreateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
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

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

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

        public CustomerAccessTokenCreatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CustomerAccessTokenCreatePayloadQuery customerAccessToken(CustomerAccessTokenQueryDefinition queryDef) {
            startField("customerAccessToken");

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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
                    case "clientMutationId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCustomerAccessToken() != null) {
                children.addAll(getCustomerAccessToken().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CustomerAccessTokenCreatePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CustomerAccessTokenCreatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public CustomerAccessToken getCustomerAccessToken() {
            return (CustomerAccessToken) get("customerAccessToken");
        }

        public CustomerAccessTokenCreatePayload setCustomerAccessToken(CustomerAccessToken arg) {
            optimisticData.put("customerAccessToken", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAccessTokenCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "customerAccessToken": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerAccessTokenDeleteInput implements Serializable {
        private String accessToken;

        private String clientMutationId;

        public CustomerAccessTokenDeleteInput(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public CustomerAccessTokenDeleteInput setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CustomerAccessTokenDeleteInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("accessToken:");
            Query.appendQuotedString(_queryBuilder, accessToken.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            _queryBuilder.append('}');
        }
    }

    public interface CustomerAccessTokenDeletePayloadQueryDefinition {
        void define(CustomerAccessTokenDeletePayloadQuery _queryBuilder);
    }

    public static class CustomerAccessTokenDeletePayloadQuery extends Query<CustomerAccessTokenDeletePayloadQuery> {
        CustomerAccessTokenDeletePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CustomerAccessTokenDeletePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CustomerAccessTokenDeletePayloadQuery deletedAccessToken() {
            startField("deletedAccessToken");

            return this;
        }

        public CustomerAccessTokenDeletePayloadQuery deletedCustomerAccessTokenId() {
            startField("deletedCustomerAccessTokenId");

            return this;
        }

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
                    case "clientMutationId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CustomerAccessTokenDeletePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CustomerAccessTokenDeletePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public String getDeletedAccessToken() {
            return (String) get("deletedAccessToken");
        }

        public CustomerAccessTokenDeletePayload setDeletedAccessToken(String arg) {
            optimisticData.put("deletedAccessToken", arg);
            return this;
        }

        public String getDeletedCustomerAccessTokenId() {
            return (String) get("deletedCustomerAccessTokenId");
        }

        public CustomerAccessTokenDeletePayload setDeletedCustomerAccessTokenId(String arg) {
            optimisticData.put("deletedCustomerAccessTokenId", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAccessTokenDeletePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "deletedAccessToken": return false;

                case "deletedCustomerAccessTokenId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerAccessTokenRenewInput implements Serializable {
        private String accessToken;

        private String clientMutationId;

        public CustomerAccessTokenRenewInput(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public CustomerAccessTokenRenewInput setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CustomerAccessTokenRenewInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("accessToken:");
            Query.appendQuotedString(_queryBuilder, accessToken.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            _queryBuilder.append('}');
        }
    }

    public interface CustomerAccessTokenRenewPayloadQueryDefinition {
        void define(CustomerAccessTokenRenewPayloadQuery _queryBuilder);
    }

    public static class CustomerAccessTokenRenewPayloadQuery extends Query<CustomerAccessTokenRenewPayloadQuery> {
        CustomerAccessTokenRenewPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CustomerAccessTokenRenewPayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CustomerAccessTokenRenewPayloadQuery customerAccessToken(CustomerAccessTokenQueryDefinition queryDef) {
            startField("customerAccessToken");

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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
                    case "clientMutationId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCustomerAccessToken() != null) {
                children.addAll(getCustomerAccessToken().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CustomerAccessTokenRenewPayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CustomerAccessTokenRenewPayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public CustomerAccessToken getCustomerAccessToken() {
            return (CustomerAccessToken) get("customerAccessToken");
        }

        public CustomerAccessTokenRenewPayload setCustomerAccessToken(CustomerAccessToken arg) {
            optimisticData.put("customerAccessToken", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAccessTokenRenewPayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "customerAccessToken": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerActivateInput implements Serializable {
        private ID id;

        private String password;

        private String resetToken;

        private String clientMutationId;

        public CustomerActivateInput(ID id, String password, String resetToken) {
            this.id = id;

            this.password = password;

            this.resetToken = resetToken;
        }

        public ID getId() {
            return id;
        }

        public CustomerActivateInput setId(ID id) {
            this.id = id;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public CustomerActivateInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getResetToken() {
            return resetToken;
        }

        public CustomerActivateInput setResetToken(String resetToken) {
            this.resetToken = resetToken;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CustomerActivateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("id:");
            Query.appendQuotedString(_queryBuilder, id.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("password:");
            Query.appendQuotedString(_queryBuilder, password.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("resetToken:");
            Query.appendQuotedString(_queryBuilder, resetToken.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

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

        public CustomerActivatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CustomerActivatePayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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
                    case "clientMutationId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCustomer() != null) {
                children.addAll(getCustomer().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CustomerActivatePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CustomerActivatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerActivatePayload setCustomer(Customer arg) {
            optimisticData.put("customer", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerActivatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "customer": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerAddressCreateInput implements Serializable {
        private String accessToken;

        private MailingAddressInput address;

        private String clientMutationId;

        public CustomerAddressCreateInput(String accessToken, MailingAddressInput address) {
            this.accessToken = accessToken;

            this.address = address;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public CustomerAddressCreateInput setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public MailingAddressInput getAddress() {
            return address;
        }

        public CustomerAddressCreateInput setAddress(MailingAddressInput address) {
            this.address = address;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CustomerAddressCreateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("accessToken:");
            Query.appendQuotedString(_queryBuilder, accessToken.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("address:");
            address.appendTo(_queryBuilder);

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            _queryBuilder.append('}');
        }
    }

    public interface CustomerAddressCreatePayloadQueryDefinition {
        void define(CustomerAddressCreatePayloadQuery _queryBuilder);
    }

    public static class CustomerAddressCreatePayloadQuery extends Query<CustomerAddressCreatePayloadQuery> {
        CustomerAddressCreatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CustomerAddressCreatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CustomerAddressCreatePayloadQuery customerAddress(MailingAddressQueryDefinition queryDef) {
            startField("customerAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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
                    case "clientMutationId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCustomerAddress() != null) {
                children.addAll(getCustomerAddress().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CustomerAddressCreatePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CustomerAddressCreatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public MailingAddress getCustomerAddress() {
            return (MailingAddress) get("customerAddress");
        }

        public CustomerAddressCreatePayload setCustomerAddress(MailingAddress arg) {
            optimisticData.put("customerAddress", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAddressCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "customerAddress": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerAddressDeleteInput implements Serializable {
        private String accessToken;

        private ID id;

        private String clientMutationId;

        public CustomerAddressDeleteInput(String accessToken, ID id) {
            this.accessToken = accessToken;

            this.id = id;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public CustomerAddressDeleteInput setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public ID getId() {
            return id;
        }

        public CustomerAddressDeleteInput setId(ID id) {
            this.id = id;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CustomerAddressDeleteInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("accessToken:");
            Query.appendQuotedString(_queryBuilder, accessToken.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("id:");
            Query.appendQuotedString(_queryBuilder, id.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            _queryBuilder.append('}');
        }
    }

    public interface CustomerAddressDeletePayloadQueryDefinition {
        void define(CustomerAddressDeletePayloadQuery _queryBuilder);
    }

    public static class CustomerAddressDeletePayloadQuery extends Query<CustomerAddressDeletePayloadQuery> {
        CustomerAddressDeletePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CustomerAddressDeletePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CustomerAddressDeletePayloadQuery deletedCustomerAddressId() {
            startField("deletedCustomerAddressId");

            return this;
        }

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
                    case "clientMutationId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CustomerAddressDeletePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CustomerAddressDeletePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public String getDeletedCustomerAddressId() {
            return (String) get("deletedCustomerAddressId");
        }

        public CustomerAddressDeletePayload setDeletedCustomerAddressId(String arg) {
            optimisticData.put("deletedCustomerAddressId", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAddressDeletePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "deletedCustomerAddressId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerAddressUpdateInput implements Serializable {
        private String accessToken;

        private MailingAddressInput address;

        private ID id;

        private String clientMutationId;

        public CustomerAddressUpdateInput(String accessToken, MailingAddressInput address, ID id) {
            this.accessToken = accessToken;

            this.address = address;

            this.id = id;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public CustomerAddressUpdateInput setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public MailingAddressInput getAddress() {
            return address;
        }

        public CustomerAddressUpdateInput setAddress(MailingAddressInput address) {
            this.address = address;
            return this;
        }

        public ID getId() {
            return id;
        }

        public CustomerAddressUpdateInput setId(ID id) {
            this.id = id;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CustomerAddressUpdateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("accessToken:");
            Query.appendQuotedString(_queryBuilder, accessToken.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("address:");
            address.appendTo(_queryBuilder);

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("id:");
            Query.appendQuotedString(_queryBuilder, id.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            _queryBuilder.append('}');
        }
    }

    public interface CustomerAddressUpdatePayloadQueryDefinition {
        void define(CustomerAddressUpdatePayloadQuery _queryBuilder);
    }

    public static class CustomerAddressUpdatePayloadQuery extends Query<CustomerAddressUpdatePayloadQuery> {
        CustomerAddressUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CustomerAddressUpdatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CustomerAddressUpdatePayloadQuery customerAddress(MailingAddressQueryDefinition queryDef) {
            startField("customerAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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
                    case "clientMutationId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCustomerAddress() != null) {
                children.addAll(getCustomerAddress().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CustomerAddressUpdatePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CustomerAddressUpdatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public MailingAddress getCustomerAddress() {
            return (MailingAddress) get("customerAddress");
        }

        public CustomerAddressUpdatePayload setCustomerAddress(MailingAddress arg) {
            optimisticData.put("customerAddress", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerAddressUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "customerAddress": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerCreateInput implements Serializable {
        private String email;

        private String password;

        private Boolean acceptsMarketing;

        private String clientMutationId;

        private String firstName;

        private String lastName;

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

        public Boolean getAcceptsMarketing() {
            return acceptsMarketing;
        }

        public CustomerCreateInput setAcceptsMarketing(Boolean acceptsMarketing) {
            this.acceptsMarketing = acceptsMarketing;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CustomerCreateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
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

            if (acceptsMarketing != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("acceptsMarketing:");
                _queryBuilder.append(acceptsMarketing);
            }

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
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

        public CustomerCreatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CustomerCreatePayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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
                    case "clientMutationId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCustomer() != null) {
                children.addAll(getCustomer().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CustomerCreatePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CustomerCreatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerCreatePayload setCustomer(Customer arg) {
            optimisticData.put("customer", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "customer": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerRecoverInput implements Serializable {
        private String email;

        private String clientMutationId;

        public CustomerRecoverInput(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public CustomerRecoverInput setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CustomerRecoverInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("email:");
            Query.appendQuotedString(_queryBuilder, email.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            _queryBuilder.append('}');
        }
    }

    public interface CustomerRecoverPayloadQueryDefinition {
        void define(CustomerRecoverPayloadQuery _queryBuilder);
    }

    public static class CustomerRecoverPayloadQuery extends Query<CustomerRecoverPayloadQuery> {
        CustomerRecoverPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public CustomerRecoverPayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

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
                    case "clientMutationId": {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CustomerRecoverPayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CustomerRecoverPayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerRecoverPayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerResetInput implements Serializable {
        private ID id;

        private String password;

        private String resetToken;

        private String clientMutationId;

        public CustomerResetInput(ID id, String password, String resetToken) {
            this.id = id;

            this.password = password;

            this.resetToken = resetToken;
        }

        public ID getId() {
            return id;
        }

        public CustomerResetInput setId(ID id) {
            this.id = id;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public CustomerResetInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getResetToken() {
            return resetToken;
        }

        public CustomerResetInput setResetToken(String resetToken) {
            this.resetToken = resetToken;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CustomerResetInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("id:");
            Query.appendQuotedString(_queryBuilder, id.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("password:");
            Query.appendQuotedString(_queryBuilder, password.toString());

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("resetToken:");
            Query.appendQuotedString(_queryBuilder, resetToken.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

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

        public CustomerResetPayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CustomerResetPayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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
                    case "clientMutationId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCustomer() != null) {
                children.addAll(getCustomer().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CustomerResetPayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CustomerResetPayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerResetPayload setCustomer(Customer arg) {
            optimisticData.put("customer", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerResetPayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "customer": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class CustomerUpdateInput implements Serializable {
        private String accessToken;

        private Boolean acceptsMarketing;

        private String clientMutationId;

        private String email;

        private String firstName;

        private String lastName;

        private String password;

        public CustomerUpdateInput(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public CustomerUpdateInput setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Boolean getAcceptsMarketing() {
            return acceptsMarketing;
        }

        public CustomerUpdateInput setAcceptsMarketing(Boolean acceptsMarketing) {
            this.acceptsMarketing = acceptsMarketing;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CustomerUpdateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public CustomerUpdateInput setEmail(String email) {
            this.email = email;
            return this;
        }

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

        public String getPassword() {
            return password;
        }

        public CustomerUpdateInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("accessToken:");
            Query.appendQuotedString(_queryBuilder, accessToken.toString());

            if (acceptsMarketing != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("acceptsMarketing:");
                _queryBuilder.append(acceptsMarketing);
            }

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            if (email != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("email:");
                Query.appendQuotedString(_queryBuilder, email.toString());
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

            if (password != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("password:");
                Query.appendQuotedString(_queryBuilder, password.toString());
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

        public CustomerUpdatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public CustomerUpdatePayloadQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append('{');
            queryDef.define(new CustomerQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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
                    case "clientMutationId": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCustomer() != null) {
                children.addAll(getCustomer().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CustomerUpdatePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public CustomerUpdatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public CustomerUpdatePayload setCustomer(Customer arg) {
            optimisticData.put("customer", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public CustomerUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "customer": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public interface DomainQueryDefinition {
        void define(DomainQuery _queryBuilder);
    }

    public static class DomainQuery extends Query<DomainQuery> {
        DomainQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public DomainQuery host() {
            startField("host");

            return this;
        }

        public DomainQuery sslEnabled() {
            startField("sslEnabled");

            return this;
        }

        public DomainQuery url() {
            startField("url");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            return children;
        }

        public String getGraphQlTypeName() {
            return "Domain";
        }

        public String getHost() {
            return (String) get("host");
        }

        public Domain setHost(String arg) {
            optimisticData.put("host", arg);
            return this;
        }

        public Boolean getSslEnabled() {
            return (Boolean) get("sslEnabled");
        }

        public Domain setSslEnabled(Boolean arg) {
            optimisticData.put("sslEnabled", arg);
            return this;
        }

        public String getUrl() {
            return (String) get("url");
        }

        public Domain setUrl(String arg) {
            optimisticData.put("url", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

    public static class ImageQuery extends Query<ImageQuery> {
        ImageQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public ImageQuery altText() {
            startField("altText");

            return this;
        }

        public ImageQuery id() {
            startField("id");

            return this;
        }

        public ImageQuery src() {
            startField("src");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            return children;
        }

        public String getGraphQlTypeName() {
            return "Image";
        }

        public String getAltText() {
            return (String) get("altText");
        }

        public Image setAltText(String arg) {
            optimisticData.put("altText", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public Image setId(ID arg) {
            optimisticData.put("id", arg);
            return this;
        }

        public String getSrc() {
            return (String) get("src");
        }

        public Image setSrc(String arg) {
            optimisticData.put("src", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

        public ImageConnectionQuery edges(ImageEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new ImageEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getEdges() != null) {
                for (ImageEdge elem: getEdges()) {
                    children.addAll(elem.getNodes());
                }
            }

            if (getPageInfo() != null) {
                children.addAll(getPageInfo().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "ImageConnection";
        }

        public List<ImageEdge> getEdges() {
            return (List<ImageEdge>) get("edges");
        }

        public ImageConnection setEdges(List<ImageEdge> arg) {
            optimisticData.put("edges", arg);
            return this;
        }

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public ImageConnection setPageInfo(PageInfo arg) {
            optimisticData.put("pageInfo", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getNode() != null) {
                children.addAll(getNode().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "ImageEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public ImageEdge setCursor(String arg) {
            optimisticData.put("cursor", arg);
            return this;
        }

        public Image getNode() {
            return (Image) get("node");
        }

        public ImageEdge setNode(Image arg) {
            optimisticData.put("node", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public interface LineItemQueryDefinition {
        void define(LineItemQuery _queryBuilder);
    }

    public static class LineItemQuery extends Query<LineItemQuery> {
        LineItemQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public LineItemQuery customAttributes(AttributeQueryDefinition queryDef) {
            startField("customAttributes");

            _queryBuilder.append('{');
            queryDef.define(new AttributeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public LineItemQuery quantity() {
            startField("quantity");

            return this;
        }

        public LineItemQuery title() {
            startField("title");

            return this;
        }

        public LineItemQuery variant(ProductVariantQueryDefinition queryDef) {
            startField("variant");

            _queryBuilder.append('{');
            queryDef.define(new ProductVariantQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class LineItem extends AbstractResponse<LineItem> {
        public LineItem() {
        }

        public LineItem(JsonObject fields) throws SchemaViolationError {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCustomAttributes() != null) {
                for (Attribute elem: getCustomAttributes()) {
                    children.addAll(elem.getNodes());
                }
            }

            if (getVariant() != null) {
                children.addAll(getVariant().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "LineItem";
        }

        public List<Attribute> getCustomAttributes() {
            return (List<Attribute>) get("customAttributes");
        }

        public LineItem setCustomAttributes(List<Attribute> arg) {
            optimisticData.put("customAttributes", arg);
            return this;
        }

        public Integer getQuantity() {
            return (Integer) get("quantity");
        }

        public LineItem setQuantity(Integer arg) {
            optimisticData.put("quantity", arg);
            return this;
        }

        public String getTitle() {
            return (String) get("title");
        }

        public LineItem setTitle(String arg) {
            optimisticData.put("title", arg);
            return this;
        }

        public ProductVariant getVariant() {
            return (ProductVariant) get("variant");
        }

        public LineItem setVariant(ProductVariant arg) {
            optimisticData.put("variant", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "customAttributes": return true;

                case "quantity": return false;

                case "title": return false;

                case "variant": return true;

                default: return false;
            }
        }
    }

    public interface LineItemConnectionQueryDefinition {
        void define(LineItemConnectionQuery _queryBuilder);
    }

    public static class LineItemConnectionQuery extends Query<LineItemConnectionQuery> {
        LineItemConnectionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public LineItemConnectionQuery edges(LineItemEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new LineItemEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public LineItemConnectionQuery pageInfo(PageInfoQueryDefinition queryDef) {
            startField("pageInfo");

            _queryBuilder.append('{');
            queryDef.define(new PageInfoQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class LineItemConnection extends AbstractResponse<LineItemConnection> {
        public LineItemConnection() {
        }

        public LineItemConnection(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "edges": {
                        List<LineItemEdge> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new LineItemEdge(jsonAsObject(element1, key)));
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getEdges() != null) {
                for (LineItemEdge elem: getEdges()) {
                    children.addAll(elem.getNodes());
                }
            }

            if (getPageInfo() != null) {
                children.addAll(getPageInfo().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "LineItemConnection";
        }

        public List<LineItemEdge> getEdges() {
            return (List<LineItemEdge>) get("edges");
        }

        public LineItemConnection setEdges(List<LineItemEdge> arg) {
            optimisticData.put("edges", arg);
            return this;
        }

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public LineItemConnection setPageInfo(PageInfo arg) {
            optimisticData.put("pageInfo", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "edges": return true;

                case "pageInfo": return true;

                default: return false;
            }
        }
    }

    public interface LineItemEdgeQueryDefinition {
        void define(LineItemEdgeQuery _queryBuilder);
    }

    public static class LineItemEdgeQuery extends Query<LineItemEdgeQuery> {
        LineItemEdgeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public LineItemEdgeQuery cursor() {
            startField("cursor");

            return this;
        }

        public LineItemEdgeQuery node(LineItemQueryDefinition queryDef) {
            startField("node");

            _queryBuilder.append('{');
            queryDef.define(new LineItemQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class LineItemEdge extends AbstractResponse<LineItemEdge> {
        public LineItemEdge() {
        }

        public LineItemEdge(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "cursor": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "node": {
                        responseData.put(key, new LineItem(jsonAsObject(field.getValue(), key)));

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getNode() != null) {
                children.addAll(getNode().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "LineItemEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public LineItemEdge setCursor(String arg) {
            optimisticData.put("cursor", arg);
            return this;
        }

        public LineItem getNode() {
            return (LineItem) get("node");
        }

        public LineItemEdge setNode(LineItem arg) {
            optimisticData.put("node", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public static class LineItemInput implements Serializable {
        private int quantity;

        private ID variantId;

        private List<AttributeInput> customAttributes;

        public LineItemInput(int quantity, ID variantId) {
            this.quantity = quantity;

            this.variantId = variantId;
        }

        public int getQuantity() {
            return quantity;
        }

        public LineItemInput setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public ID getVariantId() {
            return variantId;
        }

        public LineItemInput setVariantId(ID variantId) {
            this.variantId = variantId;
            return this;
        }

        public List<AttributeInput> getCustomAttributes() {
            return customAttributes;
        }

        public LineItemInput setCustomAttributes(List<AttributeInput> customAttributes) {
            this.customAttributes = customAttributes;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("quantity:");
            _queryBuilder.append(quantity);

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("variantId:");
            Query.appendQuotedString(_queryBuilder, variantId.toString());

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

    public interface MailingAddressQueryDefinition {
        void define(MailingAddressQuery _queryBuilder);
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            return children;
        }

        public String getGraphQlTypeName() {
            return "MailingAddress";
        }

        public String getAddress1() {
            return (String) get("address1");
        }

        public MailingAddress setAddress1(String arg) {
            optimisticData.put("address1", arg);
            return this;
        }

        public String getAddress2() {
            return (String) get("address2");
        }

        public MailingAddress setAddress2(String arg) {
            optimisticData.put("address2", arg);
            return this;
        }

        public String getCity() {
            return (String) get("city");
        }

        public MailingAddress setCity(String arg) {
            optimisticData.put("city", arg);
            return this;
        }

        public String getCompany() {
            return (String) get("company");
        }

        public MailingAddress setCompany(String arg) {
            optimisticData.put("company", arg);
            return this;
        }

        public String getCountry() {
            return (String) get("country");
        }

        public MailingAddress setCountry(String arg) {
            optimisticData.put("country", arg);
            return this;
        }

        public String getCountryCode() {
            return (String) get("countryCode");
        }

        public MailingAddress setCountryCode(String arg) {
            optimisticData.put("countryCode", arg);
            return this;
        }

        public String getFirstName() {
            return (String) get("firstName");
        }

        public MailingAddress setFirstName(String arg) {
            optimisticData.put("firstName", arg);
            return this;
        }

        public List<String> getFormatted() {
            return (List<String>) get("formatted");
        }

        public MailingAddress setFormatted(List<String> arg) {
            optimisticData.put("formatted", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public String getLastName() {
            return (String) get("lastName");
        }

        public MailingAddress setLastName(String arg) {
            optimisticData.put("lastName", arg);
            return this;
        }

        public Double getLatitude() {
            return (Double) get("latitude");
        }

        public MailingAddress setLatitude(Double arg) {
            optimisticData.put("latitude", arg);
            return this;
        }

        public Double getLongitude() {
            return (Double) get("longitude");
        }

        public MailingAddress setLongitude(Double arg) {
            optimisticData.put("longitude", arg);
            return this;
        }

        public String getName() {
            return (String) get("name");
        }

        public MailingAddress setName(String arg) {
            optimisticData.put("name", arg);
            return this;
        }

        public String getPhone() {
            return (String) get("phone");
        }

        public MailingAddress setPhone(String arg) {
            optimisticData.put("phone", arg);
            return this;
        }

        public String getProvince() {
            return (String) get("province");
        }

        public MailingAddress setProvince(String arg) {
            optimisticData.put("province", arg);
            return this;
        }

        public String getProvinceCode() {
            return (String) get("provinceCode");
        }

        public MailingAddress setProvinceCode(String arg) {
            optimisticData.put("provinceCode", arg);
            return this;
        }

        public String getZip() {
            return (String) get("zip");
        }

        public MailingAddress setZip(String arg) {
            optimisticData.put("zip", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

        public MailingAddressConnectionQuery edges(MailingAddressEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getEdges() != null) {
                for (MailingAddressEdge elem: getEdges()) {
                    children.addAll(elem.getNodes());
                }
            }

            if (getPageInfo() != null) {
                children.addAll(getPageInfo().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "MailingAddressConnection";
        }

        public List<MailingAddressEdge> getEdges() {
            return (List<MailingAddressEdge>) get("edges");
        }

        public MailingAddressConnection setEdges(List<MailingAddressEdge> arg) {
            optimisticData.put("edges", arg);
            return this;
        }

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public MailingAddressConnection setPageInfo(PageInfo arg) {
            optimisticData.put("pageInfo", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getNode() != null) {
                children.addAll(getNode().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "MailingAddressEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public MailingAddressEdge setCursor(String arg) {
            optimisticData.put("cursor", arg);
            return this;
        }

        public MailingAddress getNode() {
            return (MailingAddress) get("node");
        }

        public MailingAddressEdge setNode(MailingAddress arg) {
            optimisticData.put("node", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

    public static class MutationQuery extends Query<MutationQuery> {
        MutationQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public MutationQuery checkoutAddLineItems(CheckoutAddLineItemsInput input, CheckoutAddLineItemsPayloadQueryDefinition queryDef) {
            startField("checkoutAddLineItems");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutAddLineItemsPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery checkoutAttributesUpdate(CheckoutAttributesUpdateInput input, CheckoutAttributesUpdatePayloadQueryDefinition queryDef) {
            startField("checkoutAttributesUpdate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutAttributesUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery checkoutCompleteWithCreditCard(CheckoutCompleteWithCreditCardInput input, CheckoutCompleteWithCreditCardPayloadQueryDefinition queryDef) {
            startField("checkoutCompleteWithCreditCard");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutCompleteWithCreditCardPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery checkoutCompleteWithTokenizedPayment(CheckoutCompleteWithTokenizedPaymentInput input, CheckoutCompleteWithTokenizedPaymentPayloadQueryDefinition queryDef) {
            startField("checkoutCompleteWithTokenizedPayment");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutCompleteWithTokenizedPaymentPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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

        public MutationQuery checkoutShippingAddressUpdate(CheckoutShippingAddressUpdateInput input, CheckoutShippingAddressUpdatePayloadQueryDefinition queryDef) {
            startField("checkoutShippingAddressUpdate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutShippingAddressUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery checkoutShippingLineUpdate(CheckoutShippingLineUpdateInput input, CheckoutShippingLineUpdatePayloadQueryDefinition queryDef) {
            startField("checkoutShippingLineUpdate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CheckoutShippingLineUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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

        public MutationQuery customerAccessTokenDelete(CustomerAccessTokenDeleteInput input, CustomerAccessTokenDeletePayloadQueryDefinition queryDef) {
            startField("customerAccessTokenDelete");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenDeletePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery customerAccessTokenRenew(CustomerAccessTokenRenewInput input, CustomerAccessTokenRenewPayloadQueryDefinition queryDef) {
            startField("customerAccessTokenRenew");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerAccessTokenRenewPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery customerActivate(CustomerActivateInput input, CustomerActivatePayloadQueryDefinition queryDef) {
            startField("customerActivate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerActivatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery customerAddressCreate(CustomerAddressCreateInput input, CustomerAddressCreatePayloadQueryDefinition queryDef) {
            startField("customerAddressCreate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerAddressCreatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery customerAddressDelete(CustomerAddressDeleteInput input, CustomerAddressDeletePayloadQueryDefinition queryDef) {
            startField("customerAddressDelete");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerAddressDeletePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery customerAddressUpdate(CustomerAddressUpdateInput input, CustomerAddressUpdatePayloadQueryDefinition queryDef) {
            startField("customerAddressUpdate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerAddressUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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

        public MutationQuery customerRecover(CustomerRecoverInput input, CustomerRecoverPayloadQueryDefinition queryDef) {
            startField("customerRecover");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerRecoverPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery customerReset(CustomerResetInput input, CustomerResetPayloadQueryDefinition queryDef) {
            startField("customerReset");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerResetPayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery customerUpdate(CustomerUpdateInput input, CustomerUpdatePayloadQueryDefinition queryDef) {
            startField("customerUpdate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

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

    public static class Mutation extends AbstractResponse<Mutation> {
        public Mutation() {
        }

        public Mutation(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "checkoutAddLineItems": {
                        CheckoutAddLineItemsPayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutAddLineItemsPayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "checkoutAttributesUpdate": {
                        CheckoutAttributesUpdatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new CheckoutAttributesUpdatePayload(jsonAsObject(field.getValue(), key));
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCheckoutAddLineItems() != null) {
                children.addAll(getCheckoutAddLineItems().getNodes());
            }

            if (getCheckoutAttributesUpdate() != null) {
                children.addAll(getCheckoutAttributesUpdate().getNodes());
            }

            if (getCheckoutCompleteWithCreditCard() != null) {
                children.addAll(getCheckoutCompleteWithCreditCard().getNodes());
            }

            if (getCheckoutCompleteWithTokenizedPayment() != null) {
                children.addAll(getCheckoutCompleteWithTokenizedPayment().getNodes());
            }

            if (getCheckoutCreate() != null) {
                children.addAll(getCheckoutCreate().getNodes());
            }

            if (getCheckoutShippingAddressUpdate() != null) {
                children.addAll(getCheckoutShippingAddressUpdate().getNodes());
            }

            if (getCheckoutShippingLineUpdate() != null) {
                children.addAll(getCheckoutShippingLineUpdate().getNodes());
            }

            if (getCustomerAccessTokenCreate() != null) {
                children.addAll(getCustomerAccessTokenCreate().getNodes());
            }

            if (getCustomerAccessTokenDelete() != null) {
                children.addAll(getCustomerAccessTokenDelete().getNodes());
            }

            if (getCustomerAccessTokenRenew() != null) {
                children.addAll(getCustomerAccessTokenRenew().getNodes());
            }

            if (getCustomerActivate() != null) {
                children.addAll(getCustomerActivate().getNodes());
            }

            if (getCustomerAddressCreate() != null) {
                children.addAll(getCustomerAddressCreate().getNodes());
            }

            if (getCustomerAddressDelete() != null) {
                children.addAll(getCustomerAddressDelete().getNodes());
            }

            if (getCustomerAddressUpdate() != null) {
                children.addAll(getCustomerAddressUpdate().getNodes());
            }

            if (getCustomerCreate() != null) {
                children.addAll(getCustomerCreate().getNodes());
            }

            if (getCustomerRecover() != null) {
                children.addAll(getCustomerRecover().getNodes());
            }

            if (getCustomerReset() != null) {
                children.addAll(getCustomerReset().getNodes());
            }

            if (getCustomerUpdate() != null) {
                children.addAll(getCustomerUpdate().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "Mutation";
        }

        public CheckoutAddLineItemsPayload getCheckoutAddLineItems() {
            return (CheckoutAddLineItemsPayload) get("checkoutAddLineItems");
        }

        public Mutation setCheckoutAddLineItems(CheckoutAddLineItemsPayload arg) {
            optimisticData.put("checkoutAddLineItems", arg);
            return this;
        }

        public CheckoutAttributesUpdatePayload getCheckoutAttributesUpdate() {
            return (CheckoutAttributesUpdatePayload) get("checkoutAttributesUpdate");
        }

        public Mutation setCheckoutAttributesUpdate(CheckoutAttributesUpdatePayload arg) {
            optimisticData.put("checkoutAttributesUpdate", arg);
            return this;
        }

        public CheckoutCompleteWithCreditCardPayload getCheckoutCompleteWithCreditCard() {
            return (CheckoutCompleteWithCreditCardPayload) get("checkoutCompleteWithCreditCard");
        }

        public Mutation setCheckoutCompleteWithCreditCard(CheckoutCompleteWithCreditCardPayload arg) {
            optimisticData.put("checkoutCompleteWithCreditCard", arg);
            return this;
        }

        public CheckoutCompleteWithTokenizedPaymentPayload getCheckoutCompleteWithTokenizedPayment() {
            return (CheckoutCompleteWithTokenizedPaymentPayload) get("checkoutCompleteWithTokenizedPayment");
        }

        public Mutation setCheckoutCompleteWithTokenizedPayment(CheckoutCompleteWithTokenizedPaymentPayload arg) {
            optimisticData.put("checkoutCompleteWithTokenizedPayment", arg);
            return this;
        }

        public CheckoutCreatePayload getCheckoutCreate() {
            return (CheckoutCreatePayload) get("checkoutCreate");
        }

        public Mutation setCheckoutCreate(CheckoutCreatePayload arg) {
            optimisticData.put("checkoutCreate", arg);
            return this;
        }

        public CheckoutShippingAddressUpdatePayload getCheckoutShippingAddressUpdate() {
            return (CheckoutShippingAddressUpdatePayload) get("checkoutShippingAddressUpdate");
        }

        public Mutation setCheckoutShippingAddressUpdate(CheckoutShippingAddressUpdatePayload arg) {
            optimisticData.put("checkoutShippingAddressUpdate", arg);
            return this;
        }

        public CheckoutShippingLineUpdatePayload getCheckoutShippingLineUpdate() {
            return (CheckoutShippingLineUpdatePayload) get("checkoutShippingLineUpdate");
        }

        public Mutation setCheckoutShippingLineUpdate(CheckoutShippingLineUpdatePayload arg) {
            optimisticData.put("checkoutShippingLineUpdate", arg);
            return this;
        }

        public CustomerAccessTokenCreatePayload getCustomerAccessTokenCreate() {
            return (CustomerAccessTokenCreatePayload) get("customerAccessTokenCreate");
        }

        public Mutation setCustomerAccessTokenCreate(CustomerAccessTokenCreatePayload arg) {
            optimisticData.put("customerAccessTokenCreate", arg);
            return this;
        }

        public CustomerAccessTokenDeletePayload getCustomerAccessTokenDelete() {
            return (CustomerAccessTokenDeletePayload) get("customerAccessTokenDelete");
        }

        public Mutation setCustomerAccessTokenDelete(CustomerAccessTokenDeletePayload arg) {
            optimisticData.put("customerAccessTokenDelete", arg);
            return this;
        }

        public CustomerAccessTokenRenewPayload getCustomerAccessTokenRenew() {
            return (CustomerAccessTokenRenewPayload) get("customerAccessTokenRenew");
        }

        public Mutation setCustomerAccessTokenRenew(CustomerAccessTokenRenewPayload arg) {
            optimisticData.put("customerAccessTokenRenew", arg);
            return this;
        }

        public CustomerActivatePayload getCustomerActivate() {
            return (CustomerActivatePayload) get("customerActivate");
        }

        public Mutation setCustomerActivate(CustomerActivatePayload arg) {
            optimisticData.put("customerActivate", arg);
            return this;
        }

        public CustomerAddressCreatePayload getCustomerAddressCreate() {
            return (CustomerAddressCreatePayload) get("customerAddressCreate");
        }

        public Mutation setCustomerAddressCreate(CustomerAddressCreatePayload arg) {
            optimisticData.put("customerAddressCreate", arg);
            return this;
        }

        public CustomerAddressDeletePayload getCustomerAddressDelete() {
            return (CustomerAddressDeletePayload) get("customerAddressDelete");
        }

        public Mutation setCustomerAddressDelete(CustomerAddressDeletePayload arg) {
            optimisticData.put("customerAddressDelete", arg);
            return this;
        }

        public CustomerAddressUpdatePayload getCustomerAddressUpdate() {
            return (CustomerAddressUpdatePayload) get("customerAddressUpdate");
        }

        public Mutation setCustomerAddressUpdate(CustomerAddressUpdatePayload arg) {
            optimisticData.put("customerAddressUpdate", arg);
            return this;
        }

        public CustomerCreatePayload getCustomerCreate() {
            return (CustomerCreatePayload) get("customerCreate");
        }

        public Mutation setCustomerCreate(CustomerCreatePayload arg) {
            optimisticData.put("customerCreate", arg);
            return this;
        }

        public CustomerRecoverPayload getCustomerRecover() {
            return (CustomerRecoverPayload) get("customerRecover");
        }

        public Mutation setCustomerRecover(CustomerRecoverPayload arg) {
            optimisticData.put("customerRecover", arg);
            return this;
        }

        public CustomerResetPayload getCustomerReset() {
            return (CustomerResetPayload) get("customerReset");
        }

        public Mutation setCustomerReset(CustomerResetPayload arg) {
            optimisticData.put("customerReset", arg);
            return this;
        }

        public CustomerUpdatePayload getCustomerUpdate() {
            return (CustomerUpdatePayload) get("customerUpdate");
        }

        public Mutation setCustomerUpdate(CustomerUpdatePayload arg) {
            optimisticData.put("customerUpdate", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "checkoutAddLineItems": return true;

                case "checkoutAttributesUpdate": return true;

                case "checkoutCompleteWithCreditCard": return true;

                case "checkoutCompleteWithTokenizedPayment": return true;

                case "checkoutCreate": return true;

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

    public static class NodeQuery extends Query<NodeQuery> {
        NodeQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("__typename");
        }

        public NodeQuery id() {
            startField("id");

            return this;
        }

        public NodeQuery onCheckout(CheckoutQueryDefinition queryDef) {
            startInlineFragment("Checkout");
            queryDef.define(new CheckoutQuery(_queryBuilder));
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

    public interface Node {
        String getGraphQlTypeName();

        ID getId();
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            return children;
        }

        public static Node create(JsonObject fields) throws SchemaViolationError {
            String typeName = fields.getAsJsonPrimitive("__typename").getAsString();
            switch (typeName) {
                case "Checkout": {
                    return new Checkout(fields);
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

        public ID getId() {
            return (ID) get("id");
        }

        public UnknownNode setId(ID arg) {
            optimisticData.put("id", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "id": return false;

                default: return false;
            }
        }
    }

    public interface OrderQueryDefinition {
        void define(OrderQuery _queryBuilder);
    }

    public static class OrderQuery extends Query<OrderQuery> {
        OrderQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public OrderQuery cancelReason() {
            startField("cancelReason");

            return this;
        }

        public OrderQuery cancelledAt() {
            startField("cancelledAt");

            return this;
        }

        public OrderQuery createdAt() {
            startField("createdAt");

            return this;
        }

        public OrderQuery currencyCode() {
            startField("currencyCode");

            return this;
        }

        public OrderQuery customerUrl() {
            startField("customerUrl");

            return this;
        }

        public OrderQuery displayFinancialStatus() {
            startField("displayFinancialStatus");

            return this;
        }

        public OrderQuery displayFulfillmentStatus() {
            startField("displayFulfillmentStatus");

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

        public OrderQuery lineItems(int first, LineItemConnectionQueryDefinition queryDef) {
            return lineItems(first, args -> {}, queryDef);
        }

        public OrderQuery lineItems(int first, LineItemsArgumentsDefinition argsDef, LineItemConnectionQueryDefinition queryDef) {
            startField("lineItems");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new LineItemsArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new LineItemConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public OrderQuery orderNumber() {
            startField("orderNumber");

            return this;
        }

        public OrderQuery processedAt() {
            startField("processedAt");

            return this;
        }

        public OrderQuery shippingAddress(MailingAddressQueryDefinition queryDef) {
            startField("shippingAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public OrderQuery subtotalPrice() {
            startField("subtotalPrice");

            return this;
        }

        public OrderQuery totalPrice() {
            startField("totalPrice");

            return this;
        }

        public OrderQuery totalRefunded() {
            startField("totalRefunded");

            return this;
        }

        public OrderQuery totalShippingPrice() {
            startField("totalShippingPrice");

            return this;
        }

        public OrderQuery totalTax() {
            startField("totalTax");

            return this;
        }

        public OrderQuery updatedAt() {
            startField("updatedAt");

            return this;
        }
    }

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

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "lineItems": {
                        responseData.put(key, new LineItemConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "orderNumber": {
                        responseData.put(key, jsonAsInteger(field.getValue(), key));

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            if (getLineItems() != null) {
                children.addAll(getLineItems().getNodes());
            }

            if (getShippingAddress() != null) {
                children.addAll(getShippingAddress().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "Order";
        }

        public OrderCancelReason getCancelReason() {
            return (OrderCancelReason) get("cancelReason");
        }

        public Order setCancelReason(OrderCancelReason arg) {
            optimisticData.put("cancelReason", arg);
            return this;
        }

        public DateTime getCancelledAt() {
            return (DateTime) get("cancelledAt");
        }

        public Order setCancelledAt(DateTime arg) {
            optimisticData.put("cancelledAt", arg);
            return this;
        }

        public DateTime getCreatedAt() {
            return (DateTime) get("createdAt");
        }

        public Order setCreatedAt(DateTime arg) {
            optimisticData.put("createdAt", arg);
            return this;
        }

        public CurrencyCode getCurrencyCode() {
            return (CurrencyCode) get("currencyCode");
        }

        public Order setCurrencyCode(CurrencyCode arg) {
            optimisticData.put("currencyCode", arg);
            return this;
        }

        public String getCustomerUrl() {
            return (String) get("customerUrl");
        }

        public Order setCustomerUrl(String arg) {
            optimisticData.put("customerUrl", arg);
            return this;
        }

        public OrderDisplayFinancialStatus getDisplayFinancialStatus() {
            return (OrderDisplayFinancialStatus) get("displayFinancialStatus");
        }

        public Order setDisplayFinancialStatus(OrderDisplayFinancialStatus arg) {
            optimisticData.put("displayFinancialStatus", arg);
            return this;
        }

        public OrderDisplayFulfillmentStatus getDisplayFulfillmentStatus() {
            return (OrderDisplayFulfillmentStatus) get("displayFulfillmentStatus");
        }

        public Order setDisplayFulfillmentStatus(OrderDisplayFulfillmentStatus arg) {
            optimisticData.put("displayFulfillmentStatus", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public LineItemConnection getLineItems() {
            return (LineItemConnection) get("lineItems");
        }

        public Order setLineItems(LineItemConnection arg) {
            optimisticData.put("lineItems", arg);
            return this;
        }

        public Integer getOrderNumber() {
            return (Integer) get("orderNumber");
        }

        public Order setOrderNumber(Integer arg) {
            optimisticData.put("orderNumber", arg);
            return this;
        }

        public DateTime getProcessedAt() {
            return (DateTime) get("processedAt");
        }

        public Order setProcessedAt(DateTime arg) {
            optimisticData.put("processedAt", arg);
            return this;
        }

        public MailingAddress getShippingAddress() {
            return (MailingAddress) get("shippingAddress");
        }

        public Order setShippingAddress(MailingAddress arg) {
            optimisticData.put("shippingAddress", arg);
            return this;
        }

        public BigDecimal getSubtotalPrice() {
            return (BigDecimal) get("subtotalPrice");
        }

        public Order setSubtotalPrice(BigDecimal arg) {
            optimisticData.put("subtotalPrice", arg);
            return this;
        }

        public BigDecimal getTotalPrice() {
            return (BigDecimal) get("totalPrice");
        }

        public Order setTotalPrice(BigDecimal arg) {
            optimisticData.put("totalPrice", arg);
            return this;
        }

        public BigDecimal getTotalRefunded() {
            return (BigDecimal) get("totalRefunded");
        }

        public Order setTotalRefunded(BigDecimal arg) {
            optimisticData.put("totalRefunded", arg);
            return this;
        }

        public BigDecimal getTotalShippingPrice() {
            return (BigDecimal) get("totalShippingPrice");
        }

        public Order setTotalShippingPrice(BigDecimal arg) {
            optimisticData.put("totalShippingPrice", arg);
            return this;
        }

        public BigDecimal getTotalTax() {
            return (BigDecimal) get("totalTax");
        }

        public Order setTotalTax(BigDecimal arg) {
            optimisticData.put("totalTax", arg);
            return this;
        }

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Order setUpdatedAt(DateTime arg) {
            optimisticData.put("updatedAt", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "cancelReason": return false;

                case "cancelledAt": return false;

                case "createdAt": return false;

                case "currencyCode": return false;

                case "customerUrl": return false;

                case "displayFinancialStatus": return false;

                case "displayFulfillmentStatus": return false;

                case "id": return false;

                case "lineItems": return true;

                case "orderNumber": return false;

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

        public OrderConnectionQuery edges(OrderEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new OrderEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getEdges() != null) {
                for (OrderEdge elem: getEdges()) {
                    children.addAll(elem.getNodes());
                }
            }

            if (getPageInfo() != null) {
                children.addAll(getPageInfo().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "OrderConnection";
        }

        public List<OrderEdge> getEdges() {
            return (List<OrderEdge>) get("edges");
        }

        public OrderConnection setEdges(List<OrderEdge> arg) {
            optimisticData.put("edges", arg);
            return this;
        }

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public OrderConnection setPageInfo(PageInfo arg) {
            optimisticData.put("pageInfo", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getNode() != null) {
                children.addAll(getNode().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "OrderEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public OrderEdge setCursor(String arg) {
            optimisticData.put("cursor", arg);
            return this;
        }

        public Order getNode() {
            return (Order) get("node");
        }

        public OrderEdge setNode(Order arg) {
            optimisticData.put("node", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

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

    public static class PageInfoQuery extends Query<PageInfoQuery> {
        PageInfoQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public PageInfoQuery hasNextPage() {
            startField("hasNextPage");

            return this;
        }

        public PageInfoQuery hasPreviousPage() {
            startField("hasPreviousPage");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            return children;
        }

        public String getGraphQlTypeName() {
            return "PageInfo";
        }

        public Boolean getHasNextPage() {
            return (Boolean) get("hasNextPage");
        }

        public PageInfo setHasNextPage(Boolean arg) {
            optimisticData.put("hasNextPage", arg);
            return this;
        }

        public Boolean getHasPreviousPage() {
            return (Boolean) get("hasPreviousPage");
        }

        public PageInfo setHasPreviousPage(Boolean arg) {
            optimisticData.put("hasPreviousPage", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "hasNextPage": return false;

                case "hasPreviousPage": return false;

                default: return false;
            }
        }
    }

    public interface PaymentQueryDefinition {
        void define(PaymentQuery _queryBuilder);
    }

    public static class PaymentQuery extends Query<PaymentQuery> {
        PaymentQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public PaymentQuery amount() {
            startField("amount");

            return this;
        }

        public PaymentQuery billingAddress(MailingAddressQueryDefinition queryDef) {
            startField("billingAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public PaymentQuery checkout(CheckoutQueryDefinition queryDef) {
            startField("checkout");

            _queryBuilder.append('{');
            queryDef.define(new CheckoutQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public PaymentQuery creditCard(CreditCardQueryDefinition queryDef) {
            startField("creditCard");

            _queryBuilder.append('{');
            queryDef.define(new CreditCardQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public PaymentQuery errorMessage() {
            startField("errorMessage");

            return this;
        }

        public PaymentQuery idempotencyKey() {
            startField("idempotencyKey");

            return this;
        }

        public PaymentQuery ready() {
            startField("ready");

            return this;
        }

        public PaymentQuery test() {
            startField("test");

            return this;
        }

        public PaymentQuery transaction(TransactionQueryDefinition queryDef) {
            startField("transaction");

            _queryBuilder.append('{');
            queryDef.define(new TransactionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            if (getBillingAddress() != null) {
                children.addAll(getBillingAddress().getNodes());
            }

            if (getCheckout() != null) {
                children.addAll(getCheckout().getNodes());
            }

            if (getCreditCard() != null) {
                children.addAll(getCreditCard().getNodes());
            }

            if (getTransaction() != null) {
                children.addAll(getTransaction().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "Payment";
        }

        public BigDecimal getAmount() {
            return (BigDecimal) get("amount");
        }

        public Payment setAmount(BigDecimal arg) {
            optimisticData.put("amount", arg);
            return this;
        }

        public MailingAddress getBillingAddress() {
            return (MailingAddress) get("billingAddress");
        }

        public Payment setBillingAddress(MailingAddress arg) {
            optimisticData.put("billingAddress", arg);
            return this;
        }

        public Checkout getCheckout() {
            return (Checkout) get("checkout");
        }

        public Payment setCheckout(Checkout arg) {
            optimisticData.put("checkout", arg);
            return this;
        }

        public CreditCard getCreditCard() {
            return (CreditCard) get("creditCard");
        }

        public Payment setCreditCard(CreditCard arg) {
            optimisticData.put("creditCard", arg);
            return this;
        }

        public String getErrorMessage() {
            return (String) get("errorMessage");
        }

        public Payment setErrorMessage(String arg) {
            optimisticData.put("errorMessage", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public String getIdempotencyKey() {
            return (String) get("idempotencyKey");
        }

        public Payment setIdempotencyKey(String arg) {
            optimisticData.put("idempotencyKey", arg);
            return this;
        }

        public Boolean getReady() {
            return (Boolean) get("ready");
        }

        public Payment setReady(Boolean arg) {
            optimisticData.put("ready", arg);
            return this;
        }

        public Boolean getTest() {
            return (Boolean) get("test");
        }

        public Payment setTest(Boolean arg) {
            optimisticData.put("test", arg);
            return this;
        }

        public Transaction getTransaction() {
            return (Transaction) get("transaction");
        }

        public Payment setTransaction(Transaction arg) {
            optimisticData.put("transaction", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

        public ProductQuery collections(int first, CollectionConnectionQueryDefinition queryDef) {
            return collections(first, args -> {}, queryDef);
        }

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

        public ProductQuery createdAt() {
            startField("createdAt");

            return this;
        }

        public ProductQuery descriptionHtml() {
            startField("descriptionHtml");

            return this;
        }

        public ProductQuery descriptionPlainSummary() {
            startField("descriptionPlainSummary");

            return this;
        }

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

            public ImagesArguments maxWidth(Integer value) {
                if (value != null) {
                    startArgument("maxWidth");
                    _queryBuilder.append(value);
                }
                return this;
            }

            public ImagesArguments maxHeight(Integer value) {
                if (value != null) {
                    startArgument("maxHeight");
                    _queryBuilder.append(value);
                }
                return this;
            }

            public ImagesArguments crop(CropRegion value) {
                if (value != null) {
                    startArgument("crop");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

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

        public ProductQuery images(int first, ImageConnectionQueryDefinition queryDef) {
            return images(first, args -> {}, queryDef);
        }

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

        public ProductQuery options(ProductOptionQueryDefinition queryDef) {
            return options(args -> {}, queryDef);
        }

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

        public ProductQuery productType() {
            startField("productType");

            return this;
        }

        public ProductQuery publishedAt() {
            startField("publishedAt");

            return this;
        }

        public ProductQuery tags() {
            startField("tags");

            return this;
        }

        public ProductQuery title() {
            startField("title");

            return this;
        }

        public ProductQuery updatedAt() {
            startField("updatedAt");

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

        public ProductQuery variants(int first, ProductVariantConnectionQueryDefinition queryDef) {
            return variants(first, args -> {}, queryDef);
        }

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

        public ProductQuery vendor() {
            startField("vendor");

            return this;
        }
    }

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

                    case "descriptionHtml": {
                        responseData.put(key, jsonAsString(field.getValue(), key));

                        break;
                    }

                    case "descriptionPlainSummary": {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            if (getCollections() != null) {
                children.addAll(getCollections().getNodes());
            }

            if (getImages() != null) {
                children.addAll(getImages().getNodes());
            }

            if (getOptions() != null) {
                for (ProductOption elem: getOptions()) {
                    children.addAll(elem.getNodes());
                }
            }

            if (getVariants() != null) {
                children.addAll(getVariants().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "Product";
        }

        public CollectionConnection getCollections() {
            return (CollectionConnection) get("collections");
        }

        public Product setCollections(CollectionConnection arg) {
            optimisticData.put("collections", arg);
            return this;
        }

        public DateTime getCreatedAt() {
            return (DateTime) get("createdAt");
        }

        public Product setCreatedAt(DateTime arg) {
            optimisticData.put("createdAt", arg);
            return this;
        }

        public String getDescriptionHtml() {
            return (String) get("descriptionHtml");
        }

        public Product setDescriptionHtml(String arg) {
            optimisticData.put("descriptionHtml", arg);
            return this;
        }

        public String getDescriptionPlainSummary() {
            return (String) get("descriptionPlainSummary");
        }

        public Product setDescriptionPlainSummary(String arg) {
            optimisticData.put("descriptionPlainSummary", arg);
            return this;
        }

        public String getHandle() {
            return (String) get("handle");
        }

        public Product setHandle(String arg) {
            optimisticData.put("handle", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public ImageConnection getImages() {
            return (ImageConnection) get("images");
        }

        public Product setImages(ImageConnection arg) {
            optimisticData.put("images", arg);
            return this;
        }

        public List<ProductOption> getOptions() {
            return (List<ProductOption>) get("options");
        }

        public Product setOptions(List<ProductOption> arg) {
            optimisticData.put("options", arg);
            return this;
        }

        public String getProductType() {
            return (String) get("productType");
        }

        public Product setProductType(String arg) {
            optimisticData.put("productType", arg);
            return this;
        }

        public DateTime getPublishedAt() {
            return (DateTime) get("publishedAt");
        }

        public Product setPublishedAt(DateTime arg) {
            optimisticData.put("publishedAt", arg);
            return this;
        }

        public List<String> getTags() {
            return (List<String>) get("tags");
        }

        public Product setTags(List<String> arg) {
            optimisticData.put("tags", arg);
            return this;
        }

        public String getTitle() {
            return (String) get("title");
        }

        public Product setTitle(String arg) {
            optimisticData.put("title", arg);
            return this;
        }

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public Product setUpdatedAt(DateTime arg) {
            optimisticData.put("updatedAt", arg);
            return this;
        }

        public ProductVariantConnection getVariants() {
            return (ProductVariantConnection) get("variants");
        }

        public Product setVariants(ProductVariantConnection arg) {
            optimisticData.put("variants", arg);
            return this;
        }

        public String getVendor() {
            return (String) get("vendor");
        }

        public Product setVendor(String arg) {
            optimisticData.put("vendor", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "collections": return true;

                case "createdAt": return false;

                case "descriptionHtml": return false;

                case "descriptionPlainSummary": return false;

                case "handle": return false;

                case "id": return false;

                case "images": return true;

                case "options": return true;

                case "productType": return false;

                case "publishedAt": return false;

                case "tags": return false;

                case "title": return false;

                case "updatedAt": return false;

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

        public ProductConnectionQuery edges(ProductEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new ProductEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getEdges() != null) {
                for (ProductEdge elem: getEdges()) {
                    children.addAll(elem.getNodes());
                }
            }

            if (getPageInfo() != null) {
                children.addAll(getPageInfo().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "ProductConnection";
        }

        public List<ProductEdge> getEdges() {
            return (List<ProductEdge>) get("edges");
        }

        public ProductConnection setEdges(List<ProductEdge> arg) {
            optimisticData.put("edges", arg);
            return this;
        }

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public ProductConnection setPageInfo(PageInfo arg) {
            optimisticData.put("pageInfo", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getNode() != null) {
                children.addAll(getNode().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "ProductEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public ProductEdge setCursor(String arg) {
            optimisticData.put("cursor", arg);
            return this;
        }

        public Product getNode() {
            return (Product) get("node");
        }

        public ProductEdge setNode(Product arg) {
            optimisticData.put("node", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public interface ProductOptionQueryDefinition {
        void define(ProductOptionQuery _queryBuilder);
    }

    public static class ProductOptionQuery extends Query<ProductOptionQuery> {
        ProductOptionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public ProductOptionQuery name() {
            startField("name");

            return this;
        }

        public ProductOptionQuery values() {
            startField("values");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            return children;
        }

        public String getGraphQlTypeName() {
            return "ProductOption";
        }

        public ID getId() {
            return (ID) get("id");
        }

        public String getName() {
            return (String) get("name");
        }

        public ProductOption setName(String arg) {
            optimisticData.put("name", arg);
            return this;
        }

        public List<String> getValues() {
            return (List<String>) get("values");
        }

        public ProductOption setValues(List<String> arg) {
            optimisticData.put("values", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "id": return false;

                case "name": return false;

                case "values": return false;

                default: return false;
            }
        }
    }

    public enum ProductSortKeys {
        CREATED_AT,

        ID,

        INVENTORY_TOTAL,

        PRODUCT_TYPE,

        PUBLISHED_AT,

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

                case "INVENTORY_TOTAL": {
                    return INVENTORY_TOTAL;
                }

                case "PRODUCT_TYPE": {
                    return PRODUCT_TYPE;
                }

                case "PUBLISHED_AT": {
                    return PUBLISHED_AT;
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

                case INVENTORY_TOTAL: {
                    return "INVENTORY_TOTAL";
                }

                case PRODUCT_TYPE: {
                    return "PRODUCT_TYPE";
                }

                case PUBLISHED_AT: {
                    return "PUBLISHED_AT";
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

    public static class ProductVariantQuery extends Query<ProductVariantQuery> {
        ProductVariantQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public ProductVariantQuery available() {
            startField("available");

            return this;
        }

        public class ImageArguments extends Arguments {
            ImageArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            public ImageArguments maxWidth(Integer value) {
                if (value != null) {
                    startArgument("maxWidth");
                    _queryBuilder.append(value);
                }
                return this;
            }

            public ImageArguments maxHeight(Integer value) {
                if (value != null) {
                    startArgument("maxHeight");
                    _queryBuilder.append(value);
                }
                return this;
            }

            public ImageArguments crop(CropRegion value) {
                if (value != null) {
                    startArgument("crop");
                    _queryBuilder.append(value.toString());
                }
                return this;
            }

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

        public ProductVariantQuery image(ImageQueryDefinition queryDef) {
            return image(args -> {}, queryDef);
        }

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

        public ProductVariantQuery price() {
            startField("price");

            return this;
        }

        public ProductVariantQuery product(ProductQueryDefinition queryDef) {
            startField("product");

            _queryBuilder.append('{');
            queryDef.define(new ProductQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public ProductVariantQuery selectedOptions(SelectedOptionQueryDefinition queryDef) {
            startField("selectedOptions");

            _queryBuilder.append('{');
            queryDef.define(new SelectedOptionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public ProductVariantQuery title() {
            startField("title");

            return this;
        }

        public ProductVariantQuery weight() {
            startField("weight");

            return this;
        }

        public ProductVariantQuery weightUnit() {
            startField("weightUnit");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            if (getImage() != null) {
                children.addAll(getImage().getNodes());
            }

            if (getProduct() != null) {
                children.addAll(getProduct().getNodes());
            }

            if (getSelectedOptions() != null) {
                for (SelectedOption elem: getSelectedOptions()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "ProductVariant";
        }

        public Boolean getAvailable() {
            return (Boolean) get("available");
        }

        public ProductVariant setAvailable(Boolean arg) {
            optimisticData.put("available", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public Image getImage() {
            return (Image) get("image");
        }

        public ProductVariant setImage(Image arg) {
            optimisticData.put("image", arg);
            return this;
        }

        public BigDecimal getPrice() {
            return (BigDecimal) get("price");
        }

        public ProductVariant setPrice(BigDecimal arg) {
            optimisticData.put("price", arg);
            return this;
        }

        public Product getProduct() {
            return (Product) get("product");
        }

        public ProductVariant setProduct(Product arg) {
            optimisticData.put("product", arg);
            return this;
        }

        public List<SelectedOption> getSelectedOptions() {
            return (List<SelectedOption>) get("selectedOptions");
        }

        public ProductVariant setSelectedOptions(List<SelectedOption> arg) {
            optimisticData.put("selectedOptions", arg);
            return this;
        }

        public String getTitle() {
            return (String) get("title");
        }

        public ProductVariant setTitle(String arg) {
            optimisticData.put("title", arg);
            return this;
        }

        public Double getWeight() {
            return (Double) get("weight");
        }

        public ProductVariant setWeight(Double arg) {
            optimisticData.put("weight", arg);
            return this;
        }

        public WeightUnit getWeightUnit() {
            return (WeightUnit) get("weightUnit");
        }

        public ProductVariant setWeightUnit(WeightUnit arg) {
            optimisticData.put("weightUnit", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

        public ProductVariantConnectionQuery edges(ProductVariantEdgeQueryDefinition queryDef) {
            startField("edges");

            _queryBuilder.append('{');
            queryDef.define(new ProductVariantEdgeQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getEdges() != null) {
                for (ProductVariantEdge elem: getEdges()) {
                    children.addAll(elem.getNodes());
                }
            }

            if (getPageInfo() != null) {
                children.addAll(getPageInfo().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "ProductVariantConnection";
        }

        public List<ProductVariantEdge> getEdges() {
            return (List<ProductVariantEdge>) get("edges");
        }

        public ProductVariantConnection setEdges(List<ProductVariantEdge> arg) {
            optimisticData.put("edges", arg);
            return this;
        }

        public PageInfo getPageInfo() {
            return (PageInfo) get("pageInfo");
        }

        public ProductVariantConnection setPageInfo(PageInfo arg) {
            optimisticData.put("pageInfo", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getNode() != null) {
                children.addAll(getNode().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "ProductVariantEdge";
        }

        public String getCursor() {
            return (String) get("cursor");
        }

        public ProductVariantEdge setCursor(String arg) {
            optimisticData.put("cursor", arg);
            return this;
        }

        public ProductVariant getNode() {
            return (ProductVariant) get("node");
        }

        public ProductVariantEdge setNode(ProductVariant arg) {
            optimisticData.put("node", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "cursor": return false;

                case "node": return true;

                default: return false;
            }
        }
    }

    public interface QueryRootQueryDefinition {
        void define(QueryRootQuery _queryBuilder);
    }

    public static class QueryRootQuery extends Query<QueryRootQuery> {
        QueryRootQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public QueryRootQuery customer(String accessToken, CustomerQueryDefinition queryDef) {
            startField("customer");

            _queryBuilder.append("(accessToken:");
            Query.appendQuotedString(_queryBuilder, accessToken.toString());

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getCustomer() != null) {
                children.addAll(getCustomer().getNodes());
            }

            if (getShop() != null) {
                children.addAll(getShop().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "QueryRoot";
        }

        public Customer getCustomer() {
            return (Customer) get("customer");
        }

        public QueryRoot setCustomer(Customer arg) {
            optimisticData.put("customer", arg);
            return this;
        }

        public Node getNode() {
            return (Node) get("node");
        }

        public QueryRoot setNode(Node arg) {
            optimisticData.put("node", arg);
            return this;
        }

        public Shop getShop() {
            return (Shop) get("shop");
        }

        public QueryRoot setShop(Shop arg) {
            optimisticData.put("shop", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "customer": return true;

                case "node": return false;

                case "shop": return true;

                default: return false;
            }
        }
    }

    public interface SelectedOptionQueryDefinition {
        void define(SelectedOptionQuery _queryBuilder);
    }

    public static class SelectedOptionQuery extends Query<SelectedOptionQuery> {
        SelectedOptionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public SelectedOptionQuery name() {
            startField("name");

            return this;
        }

        public SelectedOptionQuery value() {
            startField("value");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            return children;
        }

        public String getGraphQlTypeName() {
            return "SelectedOption";
        }

        public String getName() {
            return (String) get("name");
        }

        public SelectedOption setName(String arg) {
            optimisticData.put("name", arg);
            return this;
        }

        public String getValue() {
            return (String) get("value");
        }

        public SelectedOption setValue(String arg) {
            optimisticData.put("value", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "name": return false;

                case "value": return false;

                default: return false;
            }
        }
    }

    public interface ShippingRateQueryDefinition {
        void define(ShippingRateQuery _queryBuilder);
    }

    public static class ShippingRateQuery extends Query<ShippingRateQuery> {
        ShippingRateQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public ShippingRateQuery handle() {
            startField("handle");

            return this;
        }

        public ShippingRateQuery price() {
            startField("price");

            return this;
        }

        public ShippingRateQuery title() {
            startField("title");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            return children;
        }

        public String getGraphQlTypeName() {
            return "ShippingRate";
        }

        public String getHandle() {
            return (String) get("handle");
        }

        public ShippingRate setHandle(String arg) {
            optimisticData.put("handle", arg);
            return this;
        }

        public BigDecimal getPrice() {
            return (BigDecimal) get("price");
        }

        public ShippingRate setPrice(BigDecimal arg) {
            optimisticData.put("price", arg);
            return this;
        }

        public String getTitle() {
            return (String) get("title");
        }

        public ShippingRate setTitle(String arg) {
            optimisticData.put("title", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

    public static class ShopQuery extends Query<ShopQuery> {
        ShopQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public ShopQuery billingAddress(MailingAddressQueryDefinition queryDef) {
            startField("billingAddress");

            _queryBuilder.append('{');
            queryDef.define(new MailingAddressQuery(_queryBuilder));
            _queryBuilder.append('}');

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

        public ShopQuery collections(int first, CollectionConnectionQueryDefinition queryDef) {
            return collections(first, args -> {}, queryDef);
        }

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

        public ShopQuery currencyCode() {
            startField("currencyCode");

            return this;
        }

        public ShopQuery description() {
            startField("description");

            return this;
        }

        public ShopQuery moneyFormat() {
            startField("moneyFormat");

            return this;
        }

        public ShopQuery name() {
            startField("name");

            return this;
        }

        public ShopQuery primaryDomain(DomainQueryDefinition queryDef) {
            startField("primaryDomain");

            _queryBuilder.append('{');
            queryDef.define(new DomainQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

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

        public ShopQuery products(int first, ProductConnectionQueryDefinition queryDef) {
            return products(first, args -> {}, queryDef);
        }

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

        public ShopQuery refundPolicy(ShopPolicyQueryDefinition queryDef) {
            startField("refundPolicy");

            _queryBuilder.append('{');
            queryDef.define(new ShopPolicyQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public ShopQuery termsOfService(ShopPolicyQueryDefinition queryDef) {
            startField("termsOfService");

            _queryBuilder.append('{');
            queryDef.define(new ShopPolicyQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class Shop extends AbstractResponse<Shop> {
        public Shop() {
        }

        public Shop(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "billingAddress": {
                        responseData.put(key, new MailingAddress(jsonAsObject(field.getValue(), key)));

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            if (getBillingAddress() != null) {
                children.addAll(getBillingAddress().getNodes());
            }

            if (getCollections() != null) {
                children.addAll(getCollections().getNodes());
            }

            if (getPrimaryDomain() != null) {
                children.addAll(getPrimaryDomain().getNodes());
            }

            if (getPrivacyPolicy() != null) {
                children.addAll(getPrivacyPolicy().getNodes());
            }

            if (getProducts() != null) {
                children.addAll(getProducts().getNodes());
            }

            if (getRefundPolicy() != null) {
                children.addAll(getRefundPolicy().getNodes());
            }

            if (getTermsOfService() != null) {
                children.addAll(getTermsOfService().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "Shop";
        }

        public MailingAddress getBillingAddress() {
            return (MailingAddress) get("billingAddress");
        }

        public Shop setBillingAddress(MailingAddress arg) {
            optimisticData.put("billingAddress", arg);
            return this;
        }

        public CollectionConnection getCollections() {
            return (CollectionConnection) get("collections");
        }

        public Shop setCollections(CollectionConnection arg) {
            optimisticData.put("collections", arg);
            return this;
        }

        public CurrencyCode getCurrencyCode() {
            return (CurrencyCode) get("currencyCode");
        }

        public Shop setCurrencyCode(CurrencyCode arg) {
            optimisticData.put("currencyCode", arg);
            return this;
        }

        public String getDescription() {
            return (String) get("description");
        }

        public Shop setDescription(String arg) {
            optimisticData.put("description", arg);
            return this;
        }

        public String getMoneyFormat() {
            return (String) get("moneyFormat");
        }

        public Shop setMoneyFormat(String arg) {
            optimisticData.put("moneyFormat", arg);
            return this;
        }

        public String getName() {
            return (String) get("name");
        }

        public Shop setName(String arg) {
            optimisticData.put("name", arg);
            return this;
        }

        public Domain getPrimaryDomain() {
            return (Domain) get("primaryDomain");
        }

        public Shop setPrimaryDomain(Domain arg) {
            optimisticData.put("primaryDomain", arg);
            return this;
        }

        public ShopPolicy getPrivacyPolicy() {
            return (ShopPolicy) get("privacyPolicy");
        }

        public Shop setPrivacyPolicy(ShopPolicy arg) {
            optimisticData.put("privacyPolicy", arg);
            return this;
        }

        public ProductConnection getProducts() {
            return (ProductConnection) get("products");
        }

        public Shop setProducts(ProductConnection arg) {
            optimisticData.put("products", arg);
            return this;
        }

        public ShopPolicy getRefundPolicy() {
            return (ShopPolicy) get("refundPolicy");
        }

        public Shop setRefundPolicy(ShopPolicy arg) {
            optimisticData.put("refundPolicy", arg);
            return this;
        }

        public ShopPolicy getTermsOfService() {
            return (ShopPolicy) get("termsOfService");
        }

        public Shop setTermsOfService(ShopPolicy arg) {
            optimisticData.put("termsOfService", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "billingAddress": return true;

                case "collections": return true;

                case "currencyCode": return false;

                case "description": return false;

                case "moneyFormat": return false;

                case "name": return false;

                case "primaryDomain": return true;

                case "privacyPolicy": return true;

                case "products": return true;

                case "refundPolicy": return true;

                case "termsOfService": return true;

                default: return false;
            }
        }
    }

    public interface ShopPolicyQueryDefinition {
        void define(ShopPolicyQuery _queryBuilder);
    }

    public static class ShopPolicyQuery extends Query<ShopPolicyQuery> {
        ShopPolicyQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public ShopPolicyQuery body() {
            startField("body");

            return this;
        }

        public ShopPolicyQuery title() {
            startField("title");

            return this;
        }

        public ShopPolicyQuery url() {
            startField("url");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            return children;
        }

        public String getGraphQlTypeName() {
            return "ShopPolicy";
        }

        public String getBody() {
            return (String) get("body");
        }

        public ShopPolicy setBody(String arg) {
            optimisticData.put("body", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public String getTitle() {
            return (String) get("title");
        }

        public ShopPolicy setTitle(String arg) {
            optimisticData.put("title", arg);
            return this;
        }

        public String getUrl() {
            return (String) get("url");
        }

        public ShopPolicy setUrl(String arg) {
            optimisticData.put("url", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "body": return false;

                case "id": return false;

                case "title": return false;

                case "url": return false;

                default: return false;
            }
        }
    }

    public interface TransactionQueryDefinition {
        void define(TransactionQuery _queryBuilder);
    }

    public static class TransactionQuery extends Query<TransactionQuery> {
        TransactionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public TransactionQuery amount() {
            startField("amount");

            return this;
        }

        public TransactionQuery kind() {
            startField("kind");

            return this;
        }

        public TransactionQuery status() {
            startField("status");

            return this;
        }

        public TransactionQuery test() {
            startField("test");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            return children;
        }

        public String getGraphQlTypeName() {
            return "Transaction";
        }

        public BigDecimal getAmount() {
            return (BigDecimal) get("amount");
        }

        public Transaction setAmount(BigDecimal arg) {
            optimisticData.put("amount", arg);
            return this;
        }

        public TransactionKind getKind() {
            return (TransactionKind) get("kind");
        }

        public Transaction setKind(TransactionKind arg) {
            optimisticData.put("kind", arg);
            return this;
        }

        public TransactionStatus getStatus() {
            return (TransactionStatus) get("status");
        }

        public Transaction setStatus(TransactionStatus arg) {
            optimisticData.put("status", arg);
            return this;
        }

        public Boolean getTest() {
            return (Boolean) get("test");
        }

        public Transaction setTest(Boolean arg) {
            optimisticData.put("test", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
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

    public static class UserErrorQuery extends Query<UserErrorQuery> {
        UserErrorQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public UserErrorQuery field() {
            startField("field");

            return this;
        }

        public UserErrorQuery message() {
            startField("message");

            return this;
        }
    }

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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            return children;
        }

        public String getGraphQlTypeName() {
            return "UserError";
        }

        public List<String> getField() {
            return (List<String>) get("field");
        }

        public UserError setField(List<String> arg) {
            optimisticData.put("field", arg);
            return this;
        }

        public String getMessage() {
            return (String) get("message");
        }

        public UserError setMessage(String arg) {
            optimisticData.put("message", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "field": return false;

                case "message": return false;

                default: return false;
            }
        }
    }

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
