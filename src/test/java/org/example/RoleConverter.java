package org.example;

import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleConverter extends SimpleArgumentConverter{


    @Override
    protected Object convert(Object source, Class<?> target)  {
        assertEquals(String.class, target);
        return String.valueOf(source).toUpperCase();
    }
}
