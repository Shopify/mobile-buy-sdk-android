package com.shopify.graphql.support;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by eapache on 2015-11-17.
 */
public class Error implements Serializable {
    private final String message;
    private final int line;
    private final int column;


    public Error(String message) {
        this.message = message;
        line = 0;
        column = 0;
    }

    public Error(JsonObject fields) {
        JsonElement message = fields.get("message");
        if (message != null && message.isJsonPrimitive() && message.getAsJsonPrimitive().isString()) {
            this.message = message.getAsString();
        } else {
            this.message = "Unknown error";
        }

        JsonElement line = fields.get("line");
        if (line != null && line.isJsonPrimitive() && line.getAsJsonPrimitive().isNumber()) {
            this.line = line.getAsInt();
        } else {
            this.line = 0;
        }

        JsonElement column = fields.get("column");
        if (column != null && column.isJsonPrimitive() && column.getAsJsonPrimitive().isNumber()) {
            this.column = column.getAsInt();
        } else {
            this.column = 0;
        }
    }

    @Override
    public String toString() {
        return message();
    }

    public String message() {
        return message;
    }

    public int line() {
        return line;
    }

    public int column() {
        return column;
    }
}
