package com.shopify.graphql.support;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dylansmith on 2015-11-23.
 */
public abstract class AbstractResponse<T extends AbstractResponse> implements Serializable {
    public final HashMap<String, Object> responseData = new HashMap<>();
    public final HashMap<String, Object> optimisticData = new HashMap<>();
    private String aliasSuffix = null;

    public T withAlias(String aliasSuffix) {
        if (this.aliasSuffix != null) {
            throw new IllegalStateException("Can only define a single alias for a field");
        }
        if (aliasSuffix == null || aliasSuffix.isEmpty()) {
            throw new IllegalArgumentException("Can't specify an empty alias");
        }
        if (aliasSuffix.contains(Query.ALIAS_SUFFIX_SEPARATOR)) {
            throw new IllegalArgumentException("Alias must not contain __");
        }

        this.aliasSuffix = aliasSuffix;
        // noinspection unchecked
        return (T) this;
    }

    public Object get(String field) {
        String key = getKey(field);
        if (optimisticData.containsKey(key)) {
            return optimisticData.get(key);
        }
        return responseData.get(key);
    }

    protected String getFieldName(String key) {
        int i = key.lastIndexOf(Query.ALIAS_SUFFIX_SEPARATOR);
        if (i > 1) {
            key = key.substring(0, i);
        }
        return key;
    }

    protected String getKey(String field) {
        if (aliasSuffix != null) {
            field += Query.ALIAS_SUFFIX_SEPARATOR + aliasSuffix;
            aliasSuffix = null;
        }
        return field;
    }

    protected String jsonAsString(JsonElement element, String field) throws SchemaViolationError {
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            throw new SchemaViolationError(this, field, element);
        }
        return element.getAsJsonPrimitive().getAsString();
    }

    protected Integer jsonAsInteger(JsonElement element, String field) throws SchemaViolationError {
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber()) {
            throw new SchemaViolationError(this, field, element);
        }
        try {
            return element.getAsJsonPrimitive().getAsInt();
        } catch (NumberFormatException exc) {
            throw new SchemaViolationError(this, field, element);
        }
    }

    protected Double jsonAsDouble(JsonElement element, String field) throws SchemaViolationError {
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isNumber()) {
            throw new SchemaViolationError(this, field, element);
        }
        return element.getAsJsonPrimitive().getAsDouble();
    }

    protected Boolean jsonAsBoolean(JsonElement element, String field) throws SchemaViolationError {
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isBoolean()) {
            throw new SchemaViolationError(this, field, element);
        }
        return element.getAsJsonPrimitive().getAsBoolean();
    }

    protected JsonObject jsonAsObject(JsonElement element, String field) throws SchemaViolationError {
        if (!element.isJsonObject()) {
            throw new SchemaViolationError(this, field, element);
        }
        return element.getAsJsonObject();
    }

    protected JsonArray jsonAsArray(JsonElement element, String field) throws SchemaViolationError {
        if (!element.isJsonArray()) {
            throw new SchemaViolationError(this, field, element);
        }
        return element.getAsJsonArray();
    }

    public List<Node> collectNodes() {
        final ArrayList<Node> children = new ArrayList<>();

        collectNodes(this, children);

        return children;
    }

    private static void collectNodes(Object o, List<Node> collection) {
        if (o instanceof AbstractResponse) {
            final AbstractResponse response = (AbstractResponse) o;

            if (response instanceof Node) {
                collection.add((Node) response);
            }

            for (Object key : response.responseData.keySet()) {
                collectNodes(response.get((String) key), collection);
            }
        } else if (o instanceof List) {
            for (Object element : (List) o) {
                collectNodes(element, collection);
            }
        }
    }

    public abstract boolean unwrapsToObject(String key);
}
