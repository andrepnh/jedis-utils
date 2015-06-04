package com.andrepnh.jedis.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.function.Function;
import redis.clients.jedis.Jedis;

public class TypedJedis {

    private static final Function<String, Boolean> BOOLEAN_CONVERTER = value -> {
        if (value.equalsIgnoreCase("true")) {
            return true;
        } else if (value.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new IllegalArgumentException(value + " is not true or false");
        }
    };

    private final Jedis jedis;

    public TypedJedis(Jedis jedis) {
        this.jedis = requireNonNull(jedis);
    }

    public Optional<Boolean> getBoolean(String key) {
        return convert(jedis.get(key), Boolean.class, BOOLEAN_CONVERTER);
    }

    public Optional<Long> getLong(String key) {
        return convert(jedis.get(key), Long.class, Long::parseLong);
    }

    public Optional<Integer> getInteger(String key) {
        return convert(jedis.get(key), Integer.class, Integer::parseInt);
    }

    public Optional<BigInteger> getBigInteger(String key) {
        return convert(jedis.get(key), BigInteger.class, BigInteger::new);
    }

    public Optional<Double> getDouble(String key) {
        return convert(jedis.get(key), Double.class, Double::parseDouble);
    }

    public Optional<BigDecimal> getBigDecimal(String key) {
        return convert(jedis.get(key), BigDecimal.class, BigDecimal::new);
    }
    
    public Optional<Boolean> getSetBoolean(String key, boolean value) {
        return convert(
                jedis.getSet(key, String.valueOf(value)), 
                Boolean.class, 
                BOOLEAN_CONVERTER);
    }

    public Optional<Long> getSetLong(String key, long value) {
        return convert(
                jedis.getSet(key, String.valueOf(value)), 
                Long.class, 
                Long::parseLong);
    }

    public Optional<Integer> getSetInteger(String key, int value) {
        return convert(
                jedis.getSet(key, String.valueOf(value)), 
                Integer.class, 
                Integer::parseInt);
    }

    public Optional<BigInteger> getSetBigInteger(String key, BigInteger value) {
        return convert(
                jedis.getSet(key, value.toString()), 
                BigInteger.class, 
                BigInteger::new);
    }

    public Optional<Double> getSetDouble(String key, double value) {
        return convert(
                jedis.getSet(key, String.valueOf(value)), 
                Double.class, 
                Double::parseDouble);
    }

    public Optional<BigDecimal> getSetBigDecimal(String key, BigDecimal value) {
        return convert(
                jedis.getSet(key, value.toPlainString()), 
                BigDecimal.class, 
                BigDecimal::new);
    }
    
    public Optional<Boolean> hgetBoolean(String key, String field) {
        return convert(
                jedis.hget(key, field),
                Boolean.class,
                BOOLEAN_CONVERTER);
    }
    
    public Optional<Long> hgetLong(String key, String field) {
        return convert(
                jedis.hget(key, field),
                Long.class,
                Long::parseLong);
    }
    
    public Optional<Integer> hgetInteger(String key, String field) {
        return convert(
                jedis.hget(key, field),
                Integer.class,
                Integer::parseInt);
    }
    
    public Optional<BigInteger> hgetBigInteger(String key, String field) {
        return convert(
                jedis.hget(key, field),
                BigInteger.class,
                BigInteger::new);
    }
    
    public Optional<Double> hgetDouble(String key, String field) {
        return convert(
                jedis.hget(key, field),
                Double.class,
                Double::parseDouble);
    }
    
    public Optional<BigDecimal> hgetBigDecimal(String key, String field) {
        return convert(
                jedis.hget(key, field),
                BigDecimal.class,
                BigDecimal::new);
    }

    private <T> Optional<T> convert(String value, Class<T> targetType, 
            Function<String, T> converter) {
        try {
            return Optional.ofNullable(value).map(converter);
        } catch (RuntimeException e) {
            throw new ConversionException(value, targetType, e);
        }
    }
}
