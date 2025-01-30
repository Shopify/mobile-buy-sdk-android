package com.shopify.graphql.support;

public abstract class Directive {
    private final String name;

    protected Directive(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
