package com.shopify.graphql.support;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dylansmith on 2015-11-20.
 */
public abstract class Arguments {
    private final StringBuilder query;
    private final Set<String> arguments = new HashSet<>();
    private boolean firstArgument;

    protected Arguments(StringBuilder query, boolean firstArgument) {
        this.query = query;
        this.firstArgument = firstArgument;
    }

    public static void end(Arguments arguments) {
        if (!arguments.firstArgument) {
            arguments.query.append(')');
        }
    }

    protected void startArgument(String name) {
        if (!arguments.add(name)) {
            throw new RuntimeException("Already specified argument " + name);
        }
        if (firstArgument) {
            firstArgument = false;
            query.append('(');
        } else {
            query.append(',');
        }
        query.append(name);
        query.append(':');
    }
}
