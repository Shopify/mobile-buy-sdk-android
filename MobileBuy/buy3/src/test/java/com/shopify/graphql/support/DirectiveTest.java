package com.shopify.graphql.support;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class DirectiveTest {
    @Test
    public void testName() {
        Generated.SampleDirective directive = new Generated.SampleDirective();
        assertEquals("@sample", directive.toString());
    }
}
