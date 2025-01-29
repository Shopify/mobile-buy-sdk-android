package com.shopify.graphql.support;

import java.io.Serializable;

/**
 * Created by dylansmith on 2016-11-01.
 */

public class ID implements Serializable {
    protected final String id;

    public ID(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        }
        return o instanceof ID && id.equals(((ID) o).id);
    }

    // like `a.equals(b)` except handles nulls
    public static boolean equals(ID a, ID b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
