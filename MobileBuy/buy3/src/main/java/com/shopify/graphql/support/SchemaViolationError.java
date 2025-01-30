package com.shopify.graphql.support;

import com.google.gson.JsonElement;

/**
 * Created by dylansmith on 2016-08-17.
 */
public class SchemaViolationError extends Exception {
    private final AbstractResponse object;
    private final String field;
    private final JsonElement value;

    public SchemaViolationError(AbstractResponse object, String field, JsonElement value) {
        super("Invalid value " + value.toString() + " for field " + object.getClass().getSimpleName() + "." + field);
        this.object = object;
        this.field = field;
        this.value = value;
    }

    public AbstractResponse getObject() {
        return object;
    }

    public String getField() {
        return field;
    }

    public JsonElement getValue() {
        return value;
    }
}
