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

public class APISchema {
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

    public interface AddressQueryDefinition {
        void define(AddressQuery _queryBuilder);
    }

    public static class AddressQuery extends Query<AddressQuery> {
        AddressQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public AddressQuery address1() {
            startField("address1");

            return this;
        }

        public AddressQuery address2() {
            startField("address2");

            return this;
        }

        public AddressQuery city() {
            startField("city");

            return this;
        }

        public AddressQuery company() {
            startField("company");

            return this;
        }

        public AddressQuery country() {
            startField("country");

            return this;
        }

        public AddressQuery countryCode() {
            startField("countryCode");

            return this;
        }

        public AddressQuery firstName() {
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

        public AddressQuery formatted() {
            return formatted(args -> {});
        }

        public AddressQuery formatted(FormattedArgumentsDefinition argsDef) {
            startField("formatted");

            FormattedArguments args = new FormattedArguments(_queryBuilder);
            argsDef.define(args);
            FormattedArguments.end(args);

            return this;
        }

        public AddressQuery lastName() {
            startField("lastName");

            return this;
        }

        public AddressQuery latitude() {
            startField("latitude");

            return this;
        }

        public AddressQuery longitude() {
            startField("longitude");

            return this;
        }

        public AddressQuery name() {
            startField("name");

            return this;
        }

        public AddressQuery phone() {
            startField("phone");

            return this;
        }

        public AddressQuery province() {
            startField("province");

            return this;
        }

        public AddressQuery provinceCode() {
            startField("provinceCode");

            return this;
        }

        public AddressQuery zip() {
            startField("zip");

            return this;
        }
    }

    public static class Address extends AbstractResponse<Address> {
        public Address() {
        }

        public Address(JsonObject fields) throws SchemaViolationError {
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

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            return children;
        }

        public String getGraphQlTypeName() {
            return "Address";
        }

        public String getAddress1() {
            return (String) get("address1");
        }

        public Address setAddress1(String arg) {
            optimisticData.put("address1", arg);
            return this;
        }

        public String getAddress2() {
            return (String) get("address2");
        }

        public Address setAddress2(String arg) {
            optimisticData.put("address2", arg);
            return this;
        }

        public String getCity() {
            return (String) get("city");
        }

        public Address setCity(String arg) {
            optimisticData.put("city", arg);
            return this;
        }

        public String getCompany() {
            return (String) get("company");
        }

        public Address setCompany(String arg) {
            optimisticData.put("company", arg);
            return this;
        }

        public String getCountry() {
            return (String) get("country");
        }

        public Address setCountry(String arg) {
            optimisticData.put("country", arg);
            return this;
        }

        public String getCountryCode() {
            return (String) get("countryCode");
        }

        public Address setCountryCode(String arg) {
            optimisticData.put("countryCode", arg);
            return this;
        }

        public String getFirstName() {
            return (String) get("firstName");
        }

        public Address setFirstName(String arg) {
            optimisticData.put("firstName", arg);
            return this;
        }

        public List<String> getFormatted() {
            return (List<String>) get("formatted");
        }

        public Address setFormatted(List<String> arg) {
            optimisticData.put("formatted", arg);
            return this;
        }

        public String getLastName() {
            return (String) get("lastName");
        }

        public Address setLastName(String arg) {
            optimisticData.put("lastName", arg);
            return this;
        }

        public Double getLatitude() {
            return (Double) get("latitude");
        }

        public Address setLatitude(Double arg) {
            optimisticData.put("latitude", arg);
            return this;
        }

        public Double getLongitude() {
            return (Double) get("longitude");
        }

        public Address setLongitude(Double arg) {
            optimisticData.put("longitude", arg);
            return this;
        }

        public String getName() {
            return (String) get("name");
        }

        public Address setName(String arg) {
            optimisticData.put("name", arg);
            return this;
        }

        public String getPhone() {
            return (String) get("phone");
        }

        public Address setPhone(String arg) {
            optimisticData.put("phone", arg);
            return this;
        }

        public String getProvince() {
            return (String) get("province");
        }

        public Address setProvince(String arg) {
            optimisticData.put("province", arg);
            return this;
        }

        public String getProvinceCode() {
            return (String) get("provinceCode");
        }

        public Address setProvinceCode(String arg) {
            optimisticData.put("provinceCode", arg);
            return this;
        }

        public String getZip() {
            return (String) get("zip");
        }

        public Address setZip(String arg) {
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

    public static class AddressInput implements Serializable {
        private String address1;

        private String address2;

        private String city;

        private String company;

        private String country;

        private String firstName;

        private ID id;

        private String lastName;

        private String phone;

        private String province;

        private String zip;

        public String getAddress1() {
            return address1;
        }

        public AddressInput setAddress1(String address1) {
            this.address1 = address1;
            return this;
        }

        public String getAddress2() {
            return address2;
        }

        public AddressInput setAddress2(String address2) {
            this.address2 = address2;
            return this;
        }

        public String getCity() {
            return city;
        }

        public AddressInput setCity(String city) {
            this.city = city;
            return this;
        }

        public String getCompany() {
            return company;
        }

        public AddressInput setCompany(String company) {
            this.company = company;
            return this;
        }

        public String getCountry() {
            return country;
        }

        public AddressInput setCountry(String country) {
            this.country = country;
            return this;
        }

        public String getFirstName() {
            return firstName;
        }

        public AddressInput setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ID getId() {
            return id;
        }

        public AddressInput setId(ID id) {
            this.id = id;
            return this;
        }

        public String getLastName() {
            return lastName;
        }

        public AddressInput setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public String getPhone() {
            return phone;
        }

        public AddressInput setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public String getProvince() {
            return province;
        }

        public AddressInput setProvince(String province) {
            this.province = province;
            return this;
        }

        public String getZip() {
            return zip;
        }

        public AddressInput setZip(String zip) {
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

            if (id != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("id:");
                Query.appendQuotedString(_queryBuilder, id.toString());
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

    public interface ApiCustomerAccessTokenQueryDefinition {
        void define(ApiCustomerAccessTokenQuery _queryBuilder);
    }

    public static class ApiCustomerAccessTokenQuery extends Query<ApiCustomerAccessTokenQuery> {
        ApiCustomerAccessTokenQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public ApiCustomerAccessTokenQuery accessToken() {
            startField("accessToken");

            return this;
        }

        public ApiCustomerAccessTokenQuery expiresAt() {
            startField("expiresAt");

            return this;
        }
    }

    public static class ApiCustomerAccessToken extends AbstractResponse<ApiCustomerAccessToken> implements Node {
        public ApiCustomerAccessToken() {
        }

        public ApiCustomerAccessToken(JsonObject fields) throws SchemaViolationError {
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

        public ApiCustomerAccessToken(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            return children;
        }

        public String getGraphQlTypeName() {
            return "ApiCustomerAccessToken";
        }

        public String getAccessToken() {
            return (String) get("accessToken");
        }

        public ApiCustomerAccessToken setAccessToken(String arg) {
            optimisticData.put("accessToken", arg);
            return this;
        }

        public DateTime getExpiresAt() {
            return (DateTime) get("expiresAt");
        }

        public ApiCustomerAccessToken setExpiresAt(DateTime arg) {
            optimisticData.put("expiresAt", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "accessToken": return false;

                case "expiresAt": return false;

                case "id": return false;

                default: return false;
            }
        }
    }

    public static class ApiCustomerAccessTokenCreateInput implements Serializable {
        private String email;

        private String password;

        private String clientMutationId;

        public ApiCustomerAccessTokenCreateInput(String email, String password) {
            this.email = email;

            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public ApiCustomerAccessTokenCreateInput setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public ApiCustomerAccessTokenCreateInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public ApiCustomerAccessTokenCreateInput setClientMutationId(String clientMutationId) {
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

    public interface ApiCustomerAccessTokenCreatePayloadQueryDefinition {
        void define(ApiCustomerAccessTokenCreatePayloadQuery _queryBuilder);
    }

    public static class ApiCustomerAccessTokenCreatePayloadQuery extends Query<ApiCustomerAccessTokenCreatePayloadQuery> {
        ApiCustomerAccessTokenCreatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public ApiCustomerAccessTokenCreatePayloadQuery apiCustomerAccessToken(ApiCustomerAccessTokenQueryDefinition queryDef) {
            startField("apiCustomerAccessToken");

            _queryBuilder.append('{');
            queryDef.define(new ApiCustomerAccessTokenQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public ApiCustomerAccessTokenCreatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public ApiCustomerAccessTokenCreatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ApiCustomerAccessTokenCreatePayload extends AbstractResponse<ApiCustomerAccessTokenCreatePayload> {
        public ApiCustomerAccessTokenCreatePayload() {
        }

        public ApiCustomerAccessTokenCreatePayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "apiCustomerAccessToken": {
                        ApiCustomerAccessToken optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ApiCustomerAccessToken(jsonAsObject(field.getValue(), key));
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

            if (getApiCustomerAccessToken() != null) {
                children.addAll(getApiCustomerAccessToken().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "ApiCustomerAccessTokenCreatePayload";
        }

        public ApiCustomerAccessToken getApiCustomerAccessToken() {
            return (ApiCustomerAccessToken) get("apiCustomerAccessToken");
        }

        public ApiCustomerAccessTokenCreatePayload setApiCustomerAccessToken(ApiCustomerAccessToken arg) {
            optimisticData.put("apiCustomerAccessToken", arg);
            return this;
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public ApiCustomerAccessTokenCreatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public ApiCustomerAccessTokenCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "apiCustomerAccessToken": return true;

                case "clientMutationId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class ApiCustomerAccessTokenDeleteInput implements Serializable {
        private String clientMutationId;

        public String getClientMutationId() {
            return clientMutationId;
        }

        public ApiCustomerAccessTokenDeleteInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
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

            _queryBuilder.append('}');
        }
    }

    public interface ApiCustomerAccessTokenDeletePayloadQueryDefinition {
        void define(ApiCustomerAccessTokenDeletePayloadQuery _queryBuilder);
    }

    public static class ApiCustomerAccessTokenDeletePayloadQuery extends Query<ApiCustomerAccessTokenDeletePayloadQuery> {
        ApiCustomerAccessTokenDeletePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public ApiCustomerAccessTokenDeletePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public ApiCustomerAccessTokenDeletePayloadQuery deletedAccessToken() {
            startField("deletedAccessToken");

            return this;
        }

        public ApiCustomerAccessTokenDeletePayloadQuery deletedApiCustomerAccessTokenId() {
            startField("deletedApiCustomerAccessTokenId");

            return this;
        }

        public ApiCustomerAccessTokenDeletePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ApiCustomerAccessTokenDeletePayload extends AbstractResponse<ApiCustomerAccessTokenDeletePayload> {
        public ApiCustomerAccessTokenDeletePayload() {
        }

        public ApiCustomerAccessTokenDeletePayload(JsonObject fields) throws SchemaViolationError {
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

                    case "deletedApiCustomerAccessTokenId": {
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
            return "ApiCustomerAccessTokenDeletePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public ApiCustomerAccessTokenDeletePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public String getDeletedAccessToken() {
            return (String) get("deletedAccessToken");
        }

        public ApiCustomerAccessTokenDeletePayload setDeletedAccessToken(String arg) {
            optimisticData.put("deletedAccessToken", arg);
            return this;
        }

        public String getDeletedApiCustomerAccessTokenId() {
            return (String) get("deletedApiCustomerAccessTokenId");
        }

        public ApiCustomerAccessTokenDeletePayload setDeletedApiCustomerAccessTokenId(String arg) {
            optimisticData.put("deletedApiCustomerAccessTokenId", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public ApiCustomerAccessTokenDeletePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "deletedAccessToken": return false;

                case "deletedApiCustomerAccessTokenId": return false;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class ApiCustomerAccessTokenRenewInput implements Serializable {
        private String clientMutationId;

        public String getClientMutationId() {
            return clientMutationId;
        }

        public ApiCustomerAccessTokenRenewInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
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

            _queryBuilder.append('}');
        }
    }

    public interface ApiCustomerAccessTokenRenewPayloadQueryDefinition {
        void define(ApiCustomerAccessTokenRenewPayloadQuery _queryBuilder);
    }

    public static class ApiCustomerAccessTokenRenewPayloadQuery extends Query<ApiCustomerAccessTokenRenewPayloadQuery> {
        ApiCustomerAccessTokenRenewPayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public ApiCustomerAccessTokenRenewPayloadQuery apiCustomerAccessToken(ApiCustomerAccessTokenQueryDefinition queryDef) {
            startField("apiCustomerAccessToken");

            _queryBuilder.append('{');
            queryDef.define(new ApiCustomerAccessTokenQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public ApiCustomerAccessTokenRenewPayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public ApiCustomerAccessTokenRenewPayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ApiCustomerAccessTokenRenewPayload extends AbstractResponse<ApiCustomerAccessTokenRenewPayload> {
        public ApiCustomerAccessTokenRenewPayload() {
        }

        public ApiCustomerAccessTokenRenewPayload(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "apiCustomerAccessToken": {
                        ApiCustomerAccessToken optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ApiCustomerAccessToken(jsonAsObject(field.getValue(), key));
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

            if (getApiCustomerAccessToken() != null) {
                children.addAll(getApiCustomerAccessToken().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "ApiCustomerAccessTokenRenewPayload";
        }

        public ApiCustomerAccessToken getApiCustomerAccessToken() {
            return (ApiCustomerAccessToken) get("apiCustomerAccessToken");
        }

        public ApiCustomerAccessTokenRenewPayload setApiCustomerAccessToken(ApiCustomerAccessToken arg) {
            optimisticData.put("apiCustomerAccessToken", arg);
            return this;
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public ApiCustomerAccessTokenRenewPayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public ApiCustomerAccessTokenRenewPayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "apiCustomerAccessToken": return true;

                case "clientMutationId": return false;

                case "userErrors": return true;

                default: return false;
            }
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

    public interface CreditCardPaymentRequestQueryDefinition {
        void define(CreditCardPaymentRequestQuery _queryBuilder);
    }

    public static class CreditCardPaymentRequestQuery extends Query<CreditCardPaymentRequestQuery> {
        CreditCardPaymentRequestQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public CreditCardPaymentRequestQuery amount() {
            startField("amount");

            return this;
        }

        public CreditCardPaymentRequestQuery creditCard(CreditCardQueryDefinition queryDef) {
            startField("creditCard");

            _queryBuilder.append('{');
            queryDef.define(new CreditCardQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CreditCardPaymentRequestQuery paymentProcessingErrorMessage() {
            startField("paymentProcessingErrorMessage");

            return this;
        }

        public CreditCardPaymentRequestQuery purchaseSession(PurchaseSessionQueryDefinition queryDef) {
            startField("purchaseSession");

            _queryBuilder.append('{');
            queryDef.define(new PurchaseSessionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CreditCardPaymentRequestQuery test() {
            startField("test");

            return this;
        }

        public CreditCardPaymentRequestQuery transaction(TransactionQueryDefinition queryDef) {
            startField("transaction");

            _queryBuilder.append('{');
            queryDef.define(new TransactionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CreditCardPaymentRequestQuery uniqueToken() {
            startField("uniqueToken");

            return this;
        }
    }

    public static class CreditCardPaymentRequest extends AbstractResponse<CreditCardPaymentRequest> implements Node, PaymentRequest {
        public CreditCardPaymentRequest() {
        }

        public CreditCardPaymentRequest(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "amount": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "creditCard": {
                        responseData.put(key, new CreditCard(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "paymentProcessingErrorMessage": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "purchaseSession": {
                        responseData.put(key, new PurchaseSession(jsonAsObject(field.getValue(), key)));

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

                    case "uniqueToken": {
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

        public CreditCardPaymentRequest(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            if (getCreditCard() != null) {
                children.addAll(getCreditCard().getNodes());
            }

            if (getPurchaseSession() != null) {
                children.addAll(getPurchaseSession().getNodes());
            }

            if (getTransaction() != null) {
                children.addAll(getTransaction().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "CreditCardPaymentRequest";
        }

        public BigDecimal getAmount() {
            return (BigDecimal) get("amount");
        }

        public CreditCardPaymentRequest setAmount(BigDecimal arg) {
            optimisticData.put("amount", arg);
            return this;
        }

        public CreditCard getCreditCard() {
            return (CreditCard) get("creditCard");
        }

        public CreditCardPaymentRequest setCreditCard(CreditCard arg) {
            optimisticData.put("creditCard", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public String getPaymentProcessingErrorMessage() {
            return (String) get("paymentProcessingErrorMessage");
        }

        public CreditCardPaymentRequest setPaymentProcessingErrorMessage(String arg) {
            optimisticData.put("paymentProcessingErrorMessage", arg);
            return this;
        }

        public PurchaseSession getPurchaseSession() {
            return (PurchaseSession) get("purchaseSession");
        }

        public CreditCardPaymentRequest setPurchaseSession(PurchaseSession arg) {
            optimisticData.put("purchaseSession", arg);
            return this;
        }

        public Boolean getTest() {
            return (Boolean) get("test");
        }

        public CreditCardPaymentRequest setTest(Boolean arg) {
            optimisticData.put("test", arg);
            return this;
        }

        public Transaction getTransaction() {
            return (Transaction) get("transaction");
        }

        public CreditCardPaymentRequest setTransaction(Transaction arg) {
            optimisticData.put("transaction", arg);
            return this;
        }

        public String getUniqueToken() {
            return (String) get("uniqueToken");
        }

        public CreditCardPaymentRequest setUniqueToken(String arg) {
            optimisticData.put("uniqueToken", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "amount": return false;

                case "creditCard": return true;

                case "id": return false;

                case "paymentProcessingErrorMessage": return false;

                case "purchaseSession": return true;

                case "test": return false;

                case "transaction": return true;

                case "uniqueToken": return false;

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
                super(_queryBuilder, true);
            }

            public AddressesArguments first(Integer value) {
                if (value != null) {
                    startArgument("first");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface AddressesArgumentsDefinition {
            void define(AddressesArguments args);
        }

        public CustomerQuery addresses(AddressQueryDefinition queryDef) {
            return addresses(args -> {}, queryDef);
        }

        public CustomerQuery addresses(AddressesArgumentsDefinition argsDef, AddressQueryDefinition queryDef) {
            startField("addresses");

            AddressesArguments args = new AddressesArguments(_queryBuilder);
            argsDef.define(args);
            AddressesArguments.end(args);

            _queryBuilder.append('{');
            queryDef.define(new AddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public CustomerQuery createdAt() {
            startField("createdAt");

            return this;
        }

        public CustomerQuery defaultAddress(AddressQueryDefinition queryDef) {
            startField("defaultAddress");

            _queryBuilder.append('{');
            queryDef.define(new AddressQuery(_queryBuilder));
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
                        List<Address> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new Address(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

                        break;
                    }

                    case "createdAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "defaultAddress": {
                        Address optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Address(jsonAsObject(field.getValue(), key));
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
                for (Address elem: getAddresses()) {
                    children.addAll(elem.getNodes());
                }
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

        public List<Address> getAddresses() {
            return (List<Address>) get("addresses");
        }

        public Customer setAddresses(List<Address> arg) {
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

        public Address getDefaultAddress() {
            return (Address) get("defaultAddress");
        }

        public Customer setDefaultAddress(Address arg) {
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

    public static class CustomerActivateInput implements Serializable {
        private ID id;

        private String password;

        private String token;

        private String clientMutationId;

        public CustomerActivateInput(ID id, String password, String token) {
            this.id = id;

            this.password = password;

            this.token = token;
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

        public String getToken() {
            return token;
        }

        public CustomerActivateInput setToken(String token) {
            this.token = token;
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
            _queryBuilder.append("token:");
            Query.appendQuotedString(_queryBuilder, token.toString());

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

    public static class CustomerInput implements Serializable {
        private Boolean acceptsMarketing;

        private String clientMutationId;

        private String email;

        private String firstName;

        private String lastName;

        private String password;

        public Boolean getAcceptsMarketing() {
            return acceptsMarketing;
        }

        public CustomerInput setAcceptsMarketing(Boolean acceptsMarketing) {
            this.acceptsMarketing = acceptsMarketing;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public CustomerInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public CustomerInput setEmail(String email) {
            this.email = email;
            return this;
        }

        public String getFirstName() {
            return firstName;
        }

        public CustomerInput setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public String getLastName() {
            return lastName;
        }

        public CustomerInput setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public CustomerInput setPassword(String password) {
            this.password = password;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

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

        private String token;

        private String clientMutationId;

        public CustomerResetInput(ID id, String password, String token) {
            this.id = id;

            this.password = password;

            this.token = token;
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

        public String getToken() {
            return token;
        }

        public CustomerResetInput setToken(String token) {
            this.token = token;
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
            _queryBuilder.append("token:");
            Query.appendQuotedString(_queryBuilder, token.toString());

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

    public interface MutationQueryDefinition {
        void define(MutationQuery _queryBuilder);
    }

    public static class MutationQuery extends Query<MutationQuery> {
        MutationQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public MutationQuery apiCustomerAccessTokenCreate(ApiCustomerAccessTokenCreateInput input, ApiCustomerAccessTokenCreatePayloadQueryDefinition queryDef) {
            startField("apiCustomerAccessTokenCreate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new ApiCustomerAccessTokenCreatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery apiCustomerAccessTokenDelete(ApiCustomerAccessTokenDeleteInput input, ApiCustomerAccessTokenDeletePayloadQueryDefinition queryDef) {
            startField("apiCustomerAccessTokenDelete");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new ApiCustomerAccessTokenDeletePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery apiCustomerAccessTokenRenew(ApiCustomerAccessTokenRenewInput input, ApiCustomerAccessTokenRenewPayloadQueryDefinition queryDef) {
            startField("apiCustomerAccessTokenRenew");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new ApiCustomerAccessTokenRenewPayloadQuery(_queryBuilder));
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

        public MutationQuery customerCreate(CustomerInput input, CustomerCreatePayloadQueryDefinition queryDef) {
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

        public MutationQuery customerUpdate(CustomerInput input, CustomerUpdatePayloadQueryDefinition queryDef) {
            startField("customerUpdate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new CustomerUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery purchaseSessionCreate(PurchaseSessionCreateInput input, PurchaseSessionCreatePayloadQueryDefinition queryDef) {
            startField("purchaseSessionCreate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new PurchaseSessionCreatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery purchaseSessionShippingRateUpdate(PurchaseSessionShippingRateUpdateInput input, PurchaseSessionShippingRateUpdatePayloadQueryDefinition queryDef) {
            startField("purchaseSessionShippingRateUpdate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new PurchaseSessionShippingRateUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery purchaseSessionUpdate(PurchaseSessionUpdateInput input, PurchaseSessionUpdatePayloadQueryDefinition queryDef) {
            startField("purchaseSessionUpdate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new PurchaseSessionUpdatePayloadQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public MutationQuery shippingRatesRequestCreate(ShippingRatesRequestCreateInput input, ShippingRatesRequestCreatePayloadQueryDefinition queryDef) {
            startField("shippingRatesRequestCreate");

            _queryBuilder.append("(input:");
            input.appendTo(_queryBuilder);

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new ShippingRatesRequestCreatePayloadQuery(_queryBuilder));
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
                    case "apiCustomerAccessTokenCreate": {
                        ApiCustomerAccessTokenCreatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ApiCustomerAccessTokenCreatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "apiCustomerAccessTokenDelete": {
                        ApiCustomerAccessTokenDeletePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ApiCustomerAccessTokenDeletePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "apiCustomerAccessTokenRenew": {
                        ApiCustomerAccessTokenRenewPayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ApiCustomerAccessTokenRenewPayload(jsonAsObject(field.getValue(), key));
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

                    case "purchaseSessionCreate": {
                        PurchaseSessionCreatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new PurchaseSessionCreatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "purchaseSessionShippingRateUpdate": {
                        PurchaseSessionShippingRateUpdatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new PurchaseSessionShippingRateUpdatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "purchaseSessionUpdate": {
                        PurchaseSessionUpdatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new PurchaseSessionUpdatePayload(jsonAsObject(field.getValue(), key));
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "shippingRatesRequestCreate": {
                        ShippingRatesRequestCreatePayload optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ShippingRatesRequestCreatePayload(jsonAsObject(field.getValue(), key));
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

            if (getApiCustomerAccessTokenCreate() != null) {
                children.addAll(getApiCustomerAccessTokenCreate().getNodes());
            }

            if (getApiCustomerAccessTokenDelete() != null) {
                children.addAll(getApiCustomerAccessTokenDelete().getNodes());
            }

            if (getApiCustomerAccessTokenRenew() != null) {
                children.addAll(getApiCustomerAccessTokenRenew().getNodes());
            }

            if (getCustomerActivate() != null) {
                children.addAll(getCustomerActivate().getNodes());
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

            if (getPurchaseSessionCreate() != null) {
                children.addAll(getPurchaseSessionCreate().getNodes());
            }

            if (getPurchaseSessionShippingRateUpdate() != null) {
                children.addAll(getPurchaseSessionShippingRateUpdate().getNodes());
            }

            if (getPurchaseSessionUpdate() != null) {
                children.addAll(getPurchaseSessionUpdate().getNodes());
            }

            if (getShippingRatesRequestCreate() != null) {
                children.addAll(getShippingRatesRequestCreate().getNodes());
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "Mutation";
        }

        public ApiCustomerAccessTokenCreatePayload getApiCustomerAccessTokenCreate() {
            return (ApiCustomerAccessTokenCreatePayload) get("apiCustomerAccessTokenCreate");
        }

        public Mutation setApiCustomerAccessTokenCreate(ApiCustomerAccessTokenCreatePayload arg) {
            optimisticData.put("apiCustomerAccessTokenCreate", arg);
            return this;
        }

        public ApiCustomerAccessTokenDeletePayload getApiCustomerAccessTokenDelete() {
            return (ApiCustomerAccessTokenDeletePayload) get("apiCustomerAccessTokenDelete");
        }

        public Mutation setApiCustomerAccessTokenDelete(ApiCustomerAccessTokenDeletePayload arg) {
            optimisticData.put("apiCustomerAccessTokenDelete", arg);
            return this;
        }

        public ApiCustomerAccessTokenRenewPayload getApiCustomerAccessTokenRenew() {
            return (ApiCustomerAccessTokenRenewPayload) get("apiCustomerAccessTokenRenew");
        }

        public Mutation setApiCustomerAccessTokenRenew(ApiCustomerAccessTokenRenewPayload arg) {
            optimisticData.put("apiCustomerAccessTokenRenew", arg);
            return this;
        }

        public CustomerActivatePayload getCustomerActivate() {
            return (CustomerActivatePayload) get("customerActivate");
        }

        public Mutation setCustomerActivate(CustomerActivatePayload arg) {
            optimisticData.put("customerActivate", arg);
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

        public PurchaseSessionCreatePayload getPurchaseSessionCreate() {
            return (PurchaseSessionCreatePayload) get("purchaseSessionCreate");
        }

        public Mutation setPurchaseSessionCreate(PurchaseSessionCreatePayload arg) {
            optimisticData.put("purchaseSessionCreate", arg);
            return this;
        }

        public PurchaseSessionShippingRateUpdatePayload getPurchaseSessionShippingRateUpdate() {
            return (PurchaseSessionShippingRateUpdatePayload) get("purchaseSessionShippingRateUpdate");
        }

        public Mutation setPurchaseSessionShippingRateUpdate(PurchaseSessionShippingRateUpdatePayload arg) {
            optimisticData.put("purchaseSessionShippingRateUpdate", arg);
            return this;
        }

        public PurchaseSessionUpdatePayload getPurchaseSessionUpdate() {
            return (PurchaseSessionUpdatePayload) get("purchaseSessionUpdate");
        }

        public Mutation setPurchaseSessionUpdate(PurchaseSessionUpdatePayload arg) {
            optimisticData.put("purchaseSessionUpdate", arg);
            return this;
        }

        public ShippingRatesRequestCreatePayload getShippingRatesRequestCreate() {
            return (ShippingRatesRequestCreatePayload) get("shippingRatesRequestCreate");
        }

        public Mutation setShippingRatesRequestCreate(ShippingRatesRequestCreatePayload arg) {
            optimisticData.put("shippingRatesRequestCreate", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "apiCustomerAccessTokenCreate": return true;

                case "apiCustomerAccessTokenDelete": return true;

                case "apiCustomerAccessTokenRenew": return true;

                case "customerActivate": return true;

                case "customerCreate": return true;

                case "customerRecover": return true;

                case "customerReset": return true;

                case "customerUpdate": return true;

                case "purchaseSessionCreate": return true;

                case "purchaseSessionShippingRateUpdate": return true;

                case "purchaseSessionUpdate": return true;

                case "shippingRatesRequestCreate": return true;

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

        public NodeQuery onApiCustomerAccessToken(ApiCustomerAccessTokenQueryDefinition queryDef) {
            startInlineFragment("ApiCustomerAccessToken");
            queryDef.define(new ApiCustomerAccessTokenQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onCollection(CollectionQueryDefinition queryDef) {
            startInlineFragment("Collection");
            queryDef.define(new CollectionQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onCreditCardPaymentRequest(CreditCardPaymentRequestQueryDefinition queryDef) {
            startInlineFragment("CreditCardPaymentRequest");
            queryDef.define(new CreditCardPaymentRequestQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onOrder(OrderQueryDefinition queryDef) {
            startInlineFragment("Order");
            queryDef.define(new OrderQuery(_queryBuilder));
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

        public NodeQuery onPurchaseSession(PurchaseSessionQueryDefinition queryDef) {
            startInlineFragment("PurchaseSession");
            queryDef.define(new PurchaseSessionQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }

        public NodeQuery onShippingRatesRequest(ShippingRatesRequestQueryDefinition queryDef) {
            startInlineFragment("ShippingRatesRequest");
            queryDef.define(new ShippingRatesRequestQuery(_queryBuilder));
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
                case "ApiCustomerAccessToken": {
                    return new ApiCustomerAccessToken(fields);
                }

                case "Collection": {
                    return new Collection(fields);
                }

                case "CreditCardPaymentRequest": {
                    return new CreditCardPaymentRequest(fields);
                }

                case "Order": {
                    return new Order(fields);
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

                case "PurchaseSession": {
                    return new PurchaseSession(fields);
                }

                case "ShippingRatesRequest": {
                    return new ShippingRatesRequest(fields);
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

        public OrderQuery shippingAddress(AddressQueryDefinition queryDef) {
            startField("shippingAddress");

            _queryBuilder.append('{');
            queryDef.define(new AddressQuery(_queryBuilder));
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
                        Address optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Address(jsonAsObject(field.getValue(), key));
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

        public Address getShippingAddress() {
            return (Address) get("shippingAddress");
        }

        public Order setShippingAddress(Address arg) {
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

    public interface PaymentRequestQueryDefinition {
        void define(PaymentRequestQuery _queryBuilder);
    }

    public static class PaymentRequestQuery extends Query<PaymentRequestQuery> {
        PaymentRequestQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("__typename");
        }

        public PaymentRequestQuery amount() {
            startField("amount");

            return this;
        }

        public PaymentRequestQuery id() {
            startField("id");

            return this;
        }

        public PaymentRequestQuery paymentProcessingErrorMessage() {
            startField("paymentProcessingErrorMessage");

            return this;
        }

        public PaymentRequestQuery purchaseSession(PurchaseSessionQueryDefinition queryDef) {
            startField("purchaseSession");

            _queryBuilder.append('{');
            queryDef.define(new PurchaseSessionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public PaymentRequestQuery test() {
            startField("test");

            return this;
        }

        public PaymentRequestQuery transaction(TransactionQueryDefinition queryDef) {
            startField("transaction");

            _queryBuilder.append('{');
            queryDef.define(new TransactionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public PaymentRequestQuery uniqueToken() {
            startField("uniqueToken");

            return this;
        }

        public PaymentRequestQuery onCreditCardPaymentRequest(CreditCardPaymentRequestQueryDefinition queryDef) {
            startInlineFragment("CreditCardPaymentRequest");
            queryDef.define(new CreditCardPaymentRequestQuery(_queryBuilder));
            _queryBuilder.append('}');
            return this;
        }
    }

    public interface PaymentRequest {
        String getGraphQlTypeName();

        BigDecimal getAmount();

        ID getId();

        String getPaymentProcessingErrorMessage();

        PurchaseSession getPurchaseSession();

        Boolean getTest();

        Transaction getTransaction();

        String getUniqueToken();
    }

    public static class UnknownPaymentRequest extends AbstractResponse<UnknownPaymentRequest> implements PaymentRequest {
        public UnknownPaymentRequest() {
        }

        public UnknownPaymentRequest(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "amount": {
                        responseData.put(key, new BigDecimal(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "paymentProcessingErrorMessage": {
                        String optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = jsonAsString(field.getValue(), key);
                        }

                        responseData.put(key, optional1);

                        break;
                    }

                    case "purchaseSession": {
                        responseData.put(key, new PurchaseSession(jsonAsObject(field.getValue(), key)));

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

                    case "uniqueToken": {
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

            if (getPurchaseSession() != null) {
                children.addAll(getPurchaseSession().getNodes());
            }

            if (getTransaction() != null) {
                children.addAll(getTransaction().getNodes());
            }

            return children;
        }

        public static PaymentRequest create(JsonObject fields) throws SchemaViolationError {
            String typeName = fields.getAsJsonPrimitive("__typename").getAsString();
            switch (typeName) {
                case "CreditCardPaymentRequest": {
                    return new CreditCardPaymentRequest(fields);
                }

                default: {
                    return new UnknownPaymentRequest(fields);
                }
            }
        }

        public String getGraphQlTypeName() {
            return (String) get("__typename");
        }

        public BigDecimal getAmount() {
            return (BigDecimal) get("amount");
        }

        public UnknownPaymentRequest setAmount(BigDecimal arg) {
            optimisticData.put("amount", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public UnknownPaymentRequest setId(ID arg) {
            optimisticData.put("id", arg);
            return this;
        }

        public String getPaymentProcessingErrorMessage() {
            return (String) get("paymentProcessingErrorMessage");
        }

        public UnknownPaymentRequest setPaymentProcessingErrorMessage(String arg) {
            optimisticData.put("paymentProcessingErrorMessage", arg);
            return this;
        }

        public PurchaseSession getPurchaseSession() {
            return (PurchaseSession) get("purchaseSession");
        }

        public UnknownPaymentRequest setPurchaseSession(PurchaseSession arg) {
            optimisticData.put("purchaseSession", arg);
            return this;
        }

        public Boolean getTest() {
            return (Boolean) get("test");
        }

        public UnknownPaymentRequest setTest(Boolean arg) {
            optimisticData.put("test", arg);
            return this;
        }

        public Transaction getTransaction() {
            return (Transaction) get("transaction");
        }

        public UnknownPaymentRequest setTransaction(Transaction arg) {
            optimisticData.put("transaction", arg);
            return this;
        }

        public String getUniqueToken() {
            return (String) get("uniqueToken");
        }

        public UnknownPaymentRequest setUniqueToken(String arg) {
            optimisticData.put("uniqueToken", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "amount": return false;

                case "id": return false;

                case "paymentProcessingErrorMessage": return false;

                case "purchaseSession": return true;

                case "test": return false;

                case "transaction": return true;

                case "uniqueToken": return false;

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

        public class ImagesArguments extends Arguments {
            ImagesArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, true);
            }

            public ImagesArguments first(Integer value) {
                if (value != null) {
                    startArgument("first");
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

        public ProductVariantQuery images(ImageQueryDefinition queryDef) {
            return images(args -> {}, queryDef);
        }

        public ProductVariantQuery images(ImagesArgumentsDefinition argsDef, ImageQueryDefinition queryDef) {
            startField("images");

            ImagesArguments args = new ImagesArguments(_queryBuilder);
            argsDef.define(args);
            ImagesArguments.end(args);

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

                    case "images": {
                        List<Image> list1 = new ArrayList<>();
                        for (JsonElement element1 : jsonAsArray(field.getValue(), key)) {
                            list1.add(new Image(jsonAsObject(element1, key)));
                        }

                        responseData.put(key, list1);

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

            if (getImages() != null) {
                for (Image elem: getImages()) {
                    children.addAll(elem.getNodes());
                }
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

        public List<Image> getImages() {
            return (List<Image>) get("images");
        }

        public ProductVariant setImages(List<Image> arg) {
            optimisticData.put("images", arg);
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

                case "images": return true;

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

    public interface PurchaseSessionQueryDefinition {
        void define(PurchaseSessionQuery _queryBuilder);
    }

    public static class PurchaseSessionQuery extends Query<PurchaseSessionQuery> {
        PurchaseSessionQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public PurchaseSessionQuery createdAt() {
            startField("createdAt");

            return this;
        }

        public class ItemsArguments extends Arguments {
            ItemsArguments(StringBuilder _queryBuilder) {
                super(_queryBuilder, false);
            }

            public ItemsArguments after(String value) {
                if (value != null) {
                    startArgument("after");
                    Query.appendQuotedString(_queryBuilder, value.toString());
                }
                return this;
            }

            public ItemsArguments reverse(Boolean value) {
                if (value != null) {
                    startArgument("reverse");
                    _queryBuilder.append(value);
                }
                return this;
            }
        }

        public interface ItemsArgumentsDefinition {
            void define(ItemsArguments args);
        }

        public PurchaseSessionQuery items(int first, LineItemConnectionQueryDefinition queryDef) {
            return items(first, args -> {}, queryDef);
        }

        public PurchaseSessionQuery items(int first, ItemsArgumentsDefinition argsDef, LineItemConnectionQueryDefinition queryDef) {
            startField("items");

            _queryBuilder.append("(first:");
            _queryBuilder.append(first);

            argsDef.define(new ItemsArguments(_queryBuilder));

            _queryBuilder.append(')');

            _queryBuilder.append('{');
            queryDef.define(new LineItemConnectionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public PurchaseSessionQuery requiresShipping() {
            startField("requiresShipping");

            return this;
        }

        public PurchaseSessionQuery shippingAddress(AddressQueryDefinition queryDef) {
            startField("shippingAddress");

            _queryBuilder.append('{');
            queryDef.define(new AddressQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public PurchaseSessionQuery shippingLine(ShippingRateQueryDefinition queryDef) {
            startField("shippingLine");

            _queryBuilder.append('{');
            queryDef.define(new ShippingRateQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public PurchaseSessionQuery updatedAt() {
            startField("updatedAt");

            return this;
        }
    }

    public static class PurchaseSession extends AbstractResponse<PurchaseSession> implements Node {
        public PurchaseSession() {
        }

        public PurchaseSession(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
                    case "createdAt": {
                        responseData.put(key, Utils.parseDateTime(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "id": {
                        responseData.put(key, new ID(jsonAsString(field.getValue(), key)));

                        break;
                    }

                    case "items": {
                        responseData.put(key, new LineItemConnection(jsonAsObject(field.getValue(), key)));

                        break;
                    }

                    case "requiresShipping": {
                        responseData.put(key, jsonAsBoolean(field.getValue(), key));

                        break;
                    }

                    case "shippingAddress": {
                        Address optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new Address(jsonAsObject(field.getValue(), key));
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

        public PurchaseSession(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            if (getItems() != null) {
                children.addAll(getItems().getNodes());
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
            return "PurchaseSession";
        }

        public DateTime getCreatedAt() {
            return (DateTime) get("createdAt");
        }

        public PurchaseSession setCreatedAt(DateTime arg) {
            optimisticData.put("createdAt", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public LineItemConnection getItems() {
            return (LineItemConnection) get("items");
        }

        public PurchaseSession setItems(LineItemConnection arg) {
            optimisticData.put("items", arg);
            return this;
        }

        public Boolean getRequiresShipping() {
            return (Boolean) get("requiresShipping");
        }

        public PurchaseSession setRequiresShipping(Boolean arg) {
            optimisticData.put("requiresShipping", arg);
            return this;
        }

        public Address getShippingAddress() {
            return (Address) get("shippingAddress");
        }

        public PurchaseSession setShippingAddress(Address arg) {
            optimisticData.put("shippingAddress", arg);
            return this;
        }

        public ShippingRate getShippingLine() {
            return (ShippingRate) get("shippingLine");
        }

        public PurchaseSession setShippingLine(ShippingRate arg) {
            optimisticData.put("shippingLine", arg);
            return this;
        }

        public DateTime getUpdatedAt() {
            return (DateTime) get("updatedAt");
        }

        public PurchaseSession setUpdatedAt(DateTime arg) {
            optimisticData.put("updatedAt", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "createdAt": return false;

                case "id": return false;

                case "items": return true;

                case "requiresShipping": return false;

                case "shippingAddress": return true;

                case "shippingLine": return true;

                case "updatedAt": return false;

                default: return false;
            }
        }
    }

    public static class PurchaseSessionCreateInput implements Serializable {
        private String clientMutationId;

        private String email;

        private List<LineItemInput> items;

        private AddressInput shippingAddress;

        public String getClientMutationId() {
            return clientMutationId;
        }

        public PurchaseSessionCreateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public PurchaseSessionCreateInput setEmail(String email) {
            this.email = email;
            return this;
        }

        public List<LineItemInput> getItems() {
            return items;
        }

        public PurchaseSessionCreateInput setItems(List<LineItemInput> items) {
            this.items = items;
            return this;
        }

        public AddressInput getShippingAddress() {
            return shippingAddress;
        }

        public PurchaseSessionCreateInput setShippingAddress(AddressInput shippingAddress) {
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

            if (email != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("email:");
                Query.appendQuotedString(_queryBuilder, email.toString());
            }

            if (items != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("items:");
                _queryBuilder.append('[');

                String listSeperator1 = "";
                for (LineItemInput item1 : items) {
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

            _queryBuilder.append('}');
        }
    }

    public interface PurchaseSessionCreatePayloadQueryDefinition {
        void define(PurchaseSessionCreatePayloadQuery _queryBuilder);
    }

    public static class PurchaseSessionCreatePayloadQuery extends Query<PurchaseSessionCreatePayloadQuery> {
        PurchaseSessionCreatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public PurchaseSessionCreatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public PurchaseSessionCreatePayloadQuery purchaseSession(PurchaseSessionQueryDefinition queryDef) {
            startField("purchaseSession");

            _queryBuilder.append('{');
            queryDef.define(new PurchaseSessionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public PurchaseSessionCreatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class PurchaseSessionCreatePayload extends AbstractResponse<PurchaseSessionCreatePayload> {
        public PurchaseSessionCreatePayload() {
        }

        public PurchaseSessionCreatePayload(JsonObject fields) throws SchemaViolationError {
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

                    case "purchaseSession": {
                        PurchaseSession optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new PurchaseSession(jsonAsObject(field.getValue(), key));
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

            if (getPurchaseSession() != null) {
                children.addAll(getPurchaseSession().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "PurchaseSessionCreatePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public PurchaseSessionCreatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public PurchaseSession getPurchaseSession() {
            return (PurchaseSession) get("purchaseSession");
        }

        public PurchaseSessionCreatePayload setPurchaseSession(PurchaseSession arg) {
            optimisticData.put("purchaseSession", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public PurchaseSessionCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "purchaseSession": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class PurchaseSessionShippingRateUpdateInput implements Serializable {
        private ID purchaseSessionId;

        private String shippingRateHandle;

        private String clientMutationId;

        public PurchaseSessionShippingRateUpdateInput(ID purchaseSessionId, String shippingRateHandle) {
            this.purchaseSessionId = purchaseSessionId;

            this.shippingRateHandle = shippingRateHandle;
        }

        public ID getPurchaseSessionId() {
            return purchaseSessionId;
        }

        public PurchaseSessionShippingRateUpdateInput setPurchaseSessionId(ID purchaseSessionId) {
            this.purchaseSessionId = purchaseSessionId;
            return this;
        }

        public String getShippingRateHandle() {
            return shippingRateHandle;
        }

        public PurchaseSessionShippingRateUpdateInput setShippingRateHandle(String shippingRateHandle) {
            this.shippingRateHandle = shippingRateHandle;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public PurchaseSessionShippingRateUpdateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("purchaseSessionId:");
            Query.appendQuotedString(_queryBuilder, purchaseSessionId.toString());

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

    public interface PurchaseSessionShippingRateUpdatePayloadQueryDefinition {
        void define(PurchaseSessionShippingRateUpdatePayloadQuery _queryBuilder);
    }

    public static class PurchaseSessionShippingRateUpdatePayloadQuery extends Query<PurchaseSessionShippingRateUpdatePayloadQuery> {
        PurchaseSessionShippingRateUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public PurchaseSessionShippingRateUpdatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public PurchaseSessionShippingRateUpdatePayloadQuery purchaseSession(PurchaseSessionQueryDefinition queryDef) {
            startField("purchaseSession");

            _queryBuilder.append('{');
            queryDef.define(new PurchaseSessionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public PurchaseSessionShippingRateUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class PurchaseSessionShippingRateUpdatePayload extends AbstractResponse<PurchaseSessionShippingRateUpdatePayload> {
        public PurchaseSessionShippingRateUpdatePayload() {
        }

        public PurchaseSessionShippingRateUpdatePayload(JsonObject fields) throws SchemaViolationError {
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

                    case "purchaseSession": {
                        PurchaseSession optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new PurchaseSession(jsonAsObject(field.getValue(), key));
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

            if (getPurchaseSession() != null) {
                children.addAll(getPurchaseSession().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "PurchaseSessionShippingRateUpdatePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public PurchaseSessionShippingRateUpdatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public PurchaseSession getPurchaseSession() {
            return (PurchaseSession) get("purchaseSession");
        }

        public PurchaseSessionShippingRateUpdatePayload setPurchaseSession(PurchaseSession arg) {
            optimisticData.put("purchaseSession", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public PurchaseSessionShippingRateUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "purchaseSession": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public static class PurchaseSessionUpdateInput implements Serializable {
        private ID id;

        private String clientMutationId;

        private String email;

        private List<LineItemInput> items;

        private AddressInput shippingAddress;

        public PurchaseSessionUpdateInput(ID id) {
            this.id = id;
        }

        public ID getId() {
            return id;
        }

        public PurchaseSessionUpdateInput setId(ID id) {
            this.id = id;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public PurchaseSessionUpdateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public String getEmail() {
            return email;
        }

        public PurchaseSessionUpdateInput setEmail(String email) {
            this.email = email;
            return this;
        }

        public List<LineItemInput> getItems() {
            return items;
        }

        public PurchaseSessionUpdateInput setItems(List<LineItemInput> items) {
            this.items = items;
            return this;
        }

        public AddressInput getShippingAddress() {
            return shippingAddress;
        }

        public PurchaseSessionUpdateInput setShippingAddress(AddressInput shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

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

            if (email != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("email:");
                Query.appendQuotedString(_queryBuilder, email.toString());
            }

            if (items != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("items:");
                _queryBuilder.append('[');

                String listSeperator1 = "";
                for (LineItemInput item1 : items) {
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

            _queryBuilder.append('}');
        }
    }

    public interface PurchaseSessionUpdatePayloadQueryDefinition {
        void define(PurchaseSessionUpdatePayloadQuery _queryBuilder);
    }

    public static class PurchaseSessionUpdatePayloadQuery extends Query<PurchaseSessionUpdatePayloadQuery> {
        PurchaseSessionUpdatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public PurchaseSessionUpdatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public PurchaseSessionUpdatePayloadQuery purchaseSession(PurchaseSessionQueryDefinition queryDef) {
            startField("purchaseSession");

            _queryBuilder.append('{');
            queryDef.define(new PurchaseSessionQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public PurchaseSessionUpdatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class PurchaseSessionUpdatePayload extends AbstractResponse<PurchaseSessionUpdatePayload> {
        public PurchaseSessionUpdatePayload() {
        }

        public PurchaseSessionUpdatePayload(JsonObject fields) throws SchemaViolationError {
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

                    case "purchaseSession": {
                        PurchaseSession optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new PurchaseSession(jsonAsObject(field.getValue(), key));
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

            if (getPurchaseSession() != null) {
                children.addAll(getPurchaseSession().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "PurchaseSessionUpdatePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public PurchaseSessionUpdatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public PurchaseSession getPurchaseSession() {
            return (PurchaseSession) get("purchaseSession");
        }

        public PurchaseSessionUpdatePayload setPurchaseSession(PurchaseSession arg) {
            optimisticData.put("purchaseSession", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public PurchaseSessionUpdatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "purchaseSession": return true;

                case "userErrors": return true;

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

        public QueryRootQuery customer(CustomerQueryDefinition queryDef) {
            startField("customer");

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

    public interface ShippingRatesRequestQueryDefinition {
        void define(ShippingRatesRequestQuery _queryBuilder);
    }

    public static class ShippingRatesRequestQuery extends Query<ShippingRatesRequestQuery> {
        ShippingRatesRequestQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);

            startField("id");
        }

        public ShippingRatesRequestQuery errorMessage() {
            startField("errorMessage");

            return this;
        }

        public ShippingRatesRequestQuery shippingRates(ShippingRateQueryDefinition queryDef) {
            startField("shippingRates");

            _queryBuilder.append('{');
            queryDef.define(new ShippingRateQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public ShippingRatesRequestQuery state() {
            startField("state");

            return this;
        }
    }

    public static class ShippingRatesRequest extends AbstractResponse<ShippingRatesRequest> implements Node {
        public ShippingRatesRequest() {
        }

        public ShippingRatesRequest(JsonObject fields) throws SchemaViolationError {
            for (Map.Entry<String, JsonElement> field : fields.entrySet()) {
                String key = field.getKey();
                String fieldName = getFieldName(key);
                switch (fieldName) {
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

                    case "state": {
                        responseData.put(key, ShippingRatesRequestState.fromGraphQl(jsonAsString(field.getValue(), key)));

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

        public ShippingRatesRequest(ID id) {
            this();
            optimisticData.put("id", id);
        }

        public List<Node> getNodes() {
            List<Node> children = new ArrayList<>();

            children.add(this);

            if (getShippingRates() != null) {
                for (ShippingRate elem: getShippingRates()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "ShippingRatesRequest";
        }

        public String getErrorMessage() {
            return (String) get("errorMessage");
        }

        public ShippingRatesRequest setErrorMessage(String arg) {
            optimisticData.put("errorMessage", arg);
            return this;
        }

        public ID getId() {
            return (ID) get("id");
        }

        public List<ShippingRate> getShippingRates() {
            return (List<ShippingRate>) get("shippingRates");
        }

        public ShippingRatesRequest setShippingRates(List<ShippingRate> arg) {
            optimisticData.put("shippingRates", arg);
            return this;
        }

        public ShippingRatesRequestState getState() {
            return (ShippingRatesRequestState) get("state");
        }

        public ShippingRatesRequest setState(ShippingRatesRequestState arg) {
            optimisticData.put("state", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "errorMessage": return false;

                case "id": return false;

                case "shippingRates": return true;

                case "state": return false;

                default: return false;
            }
        }
    }

    public static class ShippingRatesRequestCreateInput implements Serializable {
        private ID purchaseSessionId;

        private String clientMutationId;

        public ShippingRatesRequestCreateInput(ID purchaseSessionId) {
            this.purchaseSessionId = purchaseSessionId;
        }

        public ID getPurchaseSessionId() {
            return purchaseSessionId;
        }

        public ShippingRatesRequestCreateInput setPurchaseSessionId(ID purchaseSessionId) {
            this.purchaseSessionId = purchaseSessionId;
            return this;
        }

        public String getClientMutationId() {
            return clientMutationId;
        }

        public ShippingRatesRequestCreateInput setClientMutationId(String clientMutationId) {
            this.clientMutationId = clientMutationId;
            return this;
        }

        public void appendTo(StringBuilder _queryBuilder) {
            String separator = "";
            _queryBuilder.append('{');

            _queryBuilder.append(separator);
            separator = ",";
            _queryBuilder.append("purchaseSessionId:");
            Query.appendQuotedString(_queryBuilder, purchaseSessionId.toString());

            if (clientMutationId != null) {
                _queryBuilder.append(separator);
                separator = ",";
                _queryBuilder.append("clientMutationId:");
                Query.appendQuotedString(_queryBuilder, clientMutationId.toString());
            }

            _queryBuilder.append('}');
        }
    }

    public interface ShippingRatesRequestCreatePayloadQueryDefinition {
        void define(ShippingRatesRequestCreatePayloadQuery _queryBuilder);
    }

    public static class ShippingRatesRequestCreatePayloadQuery extends Query<ShippingRatesRequestCreatePayloadQuery> {
        ShippingRatesRequestCreatePayloadQuery(StringBuilder _queryBuilder) {
            super(_queryBuilder);
        }

        public ShippingRatesRequestCreatePayloadQuery clientMutationId() {
            startField("clientMutationId");

            return this;
        }

        public ShippingRatesRequestCreatePayloadQuery shippingRatesRequest(ShippingRatesRequestQueryDefinition queryDef) {
            startField("shippingRatesRequest");

            _queryBuilder.append('{');
            queryDef.define(new ShippingRatesRequestQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }

        public ShippingRatesRequestCreatePayloadQuery userErrors(UserErrorQueryDefinition queryDef) {
            startField("userErrors");

            _queryBuilder.append('{');
            queryDef.define(new UserErrorQuery(_queryBuilder));
            _queryBuilder.append('}');

            return this;
        }
    }

    public static class ShippingRatesRequestCreatePayload extends AbstractResponse<ShippingRatesRequestCreatePayload> {
        public ShippingRatesRequestCreatePayload() {
        }

        public ShippingRatesRequestCreatePayload(JsonObject fields) throws SchemaViolationError {
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

                    case "shippingRatesRequest": {
                        ShippingRatesRequest optional1 = null;
                        if (!field.getValue().isJsonNull()) {
                            optional1 = new ShippingRatesRequest(jsonAsObject(field.getValue(), key));
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

            if (getShippingRatesRequest() != null) {
                children.addAll(getShippingRatesRequest().getNodes());
            }

            if (getUserErrors() != null) {
                for (UserError elem: getUserErrors()) {
                    children.addAll(elem.getNodes());
                }
            }

            return children;
        }

        public String getGraphQlTypeName() {
            return "ShippingRatesRequestCreatePayload";
        }

        public String getClientMutationId() {
            return (String) get("clientMutationId");
        }

        public ShippingRatesRequestCreatePayload setClientMutationId(String arg) {
            optimisticData.put("clientMutationId", arg);
            return this;
        }

        public ShippingRatesRequest getShippingRatesRequest() {
            return (ShippingRatesRequest) get("shippingRatesRequest");
        }

        public ShippingRatesRequestCreatePayload setShippingRatesRequest(ShippingRatesRequest arg) {
            optimisticData.put("shippingRatesRequest", arg);
            return this;
        }

        public List<UserError> getUserErrors() {
            return (List<UserError>) get("userErrors");
        }

        public ShippingRatesRequestCreatePayload setUserErrors(List<UserError> arg) {
            optimisticData.put("userErrors", arg);
            return this;
        }

        public boolean unwrapsToObject(String key) {
            switch (key) {
                case "clientMutationId": return false;

                case "shippingRatesRequest": return true;

                case "userErrors": return true;

                default: return false;
            }
        }
    }

    public enum ShippingRatesRequestState {
        ERROR,

        PENDING,

        READY,

        UNKNOWN_VALUE;

        public static ShippingRatesRequestState fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
                case "ERROR": {
                    return ERROR;
                }

                case "PENDING": {
                    return PENDING;
                }

                case "READY": {
                    return READY;
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

                case PENDING: {
                    return "PENDING";
                }

                case READY: {
                    return "READY";
                }

                default: {
                    return "";
                }
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

        public ShopQuery billingAddress(AddressQueryDefinition queryDef) {
            startField("billingAddress");

            _queryBuilder.append('{');
            queryDef.define(new AddressQuery(_queryBuilder));
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
                        responseData.put(key, new Address(jsonAsObject(field.getValue(), key)));

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

        public Address getBillingAddress() {
            return (Address) get("billingAddress");
        }

        public Shop setBillingAddress(Address arg) {
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
        FAILURE,

        PENDING,

        SUCCESS,

        UNKNOWN_VALUE;

        public static TransactionStatus fromGraphQl(String value) {
            if (value == null) {
                return null;
            }

            switch (value) {
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
