package com.shopify.graphql.support;

/**
 * Created by dylansmith on 2015-11-24.
 */
public class InvalidGraphQLException extends Exception {
    public InvalidGraphQLException(String message) {
        super(message);
    }
}
