package com.github.andrepnh.jedis.utils;

import java.util.Optional;
import java.util.function.Function;

final class Converters {

    private Converters() {
        throw new IllegalStateException();
    }

    public static final Function<String, Integer> INTEGER = Integer::parseInt;
    
    public static final Function<String, Long> LONG = Long::parseLong;
    
    public static final Function<String, Double> DOUBLE = Double::parseDouble;
    
    public static final Function<String, Boolean> BOOLEAN = value -> {
        if (value.equalsIgnoreCase("true")) {
            return true;
        } else if (value.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new IllegalArgumentException(value + " is not true or false");
        }
    };
    
    static <T> Optional<T> convert(String value, Class<T> targetType, 
            Function<String, T> converter) {
        try {
            return Optional.ofNullable(value).map(converter);
        } catch (RuntimeException e) {
            throw new ConversionException(value, targetType, e);
        }
    }
    
}
