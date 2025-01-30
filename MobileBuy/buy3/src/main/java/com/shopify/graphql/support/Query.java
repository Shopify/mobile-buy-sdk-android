package com.shopify.graphql.support;

/**
 * Created by eapache on 2015-11-17.
 */
public abstract class Query<T extends Query> {
    public static final String ALIAS_SUFFIX_SEPARATOR = "__";
    private static final String BAD_ALIAS_SEPARATOR = "-";
    private static final String ALIAS_DELIMITER = ":";
    protected final StringBuilder _queryBuilder;
    private boolean firstSelection = true;
    private String aliasSuffix = null;

    protected Query(StringBuilder queryBuilder) {
        this._queryBuilder = queryBuilder;
    }

    public static void appendQuotedString(StringBuilder query, String string) {
        query.append('"');
        for (char c : string.toCharArray()) {
            switch (c) {
                case '"':
                case '\\':
                    query.append('\\');
                    query.append(c);
                    break;
                case '\r':
                    query.append("\\r");
                    break;
                case '\n':
                    query.append("\\n");
                    break;
                default:
                    if (c < 0x20) {
                        query.append(String.format("\\u%04x", (int) c));
                    } else {
                        query.append(c);
                    }
                    break;
            }
        }
        query.append('"');
    }

    private void startSelection() {
        if (firstSelection) {
            firstSelection = false;
        } else {
            _queryBuilder.append(',');
        }
    }

    protected void startInlineFragment(String typeName) {
        if (aliasSuffix != null) {
            throw new IllegalStateException("An alias cannot be specified on inline fragments");
        }

        startSelection();
        _queryBuilder.append("... on ");
        _queryBuilder.append(typeName);
        _queryBuilder.append('{');
    }

    protected void startField(String fieldName) {
        startSelection();
        _queryBuilder.append(fieldName);
        if (aliasSuffix != null) {
            _queryBuilder.append(ALIAS_SUFFIX_SEPARATOR);
            _queryBuilder.append(aliasSuffix);
            _queryBuilder.append(ALIAS_DELIMITER);
            _queryBuilder.append(fieldName);
            aliasSuffix = null;
        }
    }

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
        if (aliasSuffix.contains(Query.BAD_ALIAS_SEPARATOR)) {
            throw new IllegalArgumentException("Alias must not contain -");
        }
        this.aliasSuffix = aliasSuffix;
        // noinspection unchecked
        return (T) this;
    }
}
