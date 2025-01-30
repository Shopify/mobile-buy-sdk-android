package com.shopify.graphql.support;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.shopify.graphql.support.Generated;

public class AnnotationTest {
    @Test
    public void testAppliesAnnotation() throws Exception {
      Class<Generated> obj = Generated.class;
      boolean foundNullable = false;
      for (Class klass: obj.getDeclaredClasses()) {
        for (Method method : klass.getDeclaredMethods()) {
          if (method.isAnnotationPresent(Nullable.class)) {
            foundNullable = true;
            break;
          }
        }
      }
      assertTrue("Should have found a class with @Nullable annotation in Generated.java", foundNullable);
    }
}
