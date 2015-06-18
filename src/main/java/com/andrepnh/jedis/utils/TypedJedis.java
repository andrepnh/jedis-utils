package com.andrepnh.jedis.utils;

import static com.andrepnh.jedis.utils.Converters.*;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import redis.clients.jedis.Jedis;

public class TypedJedis {
    
    private final Jedis jedis;

    public TypedJedis(Jedis jedis) {
        this.jedis = requireNonNull(jedis);
    }

    /**
     * @throws ConversionException if the string returned by redis isn't 
     * {@code true} or {@code false} (case ignored)
     */
    public Optional<Boolean> getBoolean(String key) {
        return convert(jedis.get(key), Boolean.class, Converters.BOOLEAN);
    }

    /**
     * @throws ConversionException if the string returned by redis isn't a valid
     * long
     */
    public Optional<Long> getLong(String key) {
        return convert(jedis.get(key), Long.class, Converters.LONG);
    }

    /**
     * @throws ConversionException if the string returned by redis isn't a valid
     * integer
     */
    public Optional<Integer> getInteger(String key) {
        return convert(jedis.get(key), Integer.class, Converters.INTEGER);
    }

    /**
     * @throws ConversionException if the string returned by redis isn't a valid
     * double
     */
    public Optional<Double> getDouble(String key) {
        return convert(jedis.get(key), Double.class, Converters.DOUBLE);
    }
    
    /**
     * @throws ConversionException if the string returned by redis isn't 
     * {@code true} or {@code false} (case ignored)
     */
    public Optional<Boolean> getSetBoolean(String key, boolean value) {
        return convert(
                jedis.getSet(key, String.valueOf(value)), 
                Boolean.class, 
                Converters.BOOLEAN);
    }

    /**
     * @throws ConversionException if the string returned by redis isn't a valid
     * long
     */
    public Optional<Long> getSetLong(String key, long value) {
        return convert(
                jedis.getSet(key, String.valueOf(value)), 
                Long.class, 
                Converters.LONG);
    }

    /**
     * @throws ConversionException if the string returned by redis isn't a valid
     * integer
     */
    public Optional<Integer> getSetInteger(String key, int value) {
        return convert(
                jedis.getSet(key, String.valueOf(value)), 
                Integer.class, 
                Converters.INTEGER);
    }

    /**
     * @throws ConversionException if the string returned by redis isn't a valid
     * double
     */
    public Optional<Double> getSetDouble(String key, double value) {
        return convert(
                jedis.getSet(key, String.valueOf(value)), 
                Double.class, 
                Converters.DOUBLE);
    }
    
    /**
     * @throws ConversionException if the string returned by redis isn't 
     * {@code true} or {@code false} (case ignored)
     */
    public Optional<Boolean> hgetBoolean(String key, String field) {
        return convert(
                jedis.hget(key, field),
                Boolean.class,
                Converters.BOOLEAN);
    }
    
    /**
     * @throws ConversionException if the string returned by redis isn't a valid
     * long
     */
    public Optional<Long> hgetLong(String key, String field) {
        return convert(
                jedis.hget(key, field),
                Long.class,
                Converters.LONG);
    }
    
    /**
     * @throws ConversionException if the string returned by redis isn't a valid
     * integer
     */
    public Optional<Integer> hgetInteger(String key, String field) {
        return convert(
                jedis.hget(key, field),
                Integer.class,
                Converters.INTEGER);
    }
    
    /**
     * @throws ConversionException if the string returned by redis isn't a valid
     * double
     */
    public Optional<Double> hgetDouble(String key, String field) {
        return convert(
                jedis.hget(key, field),
                Double.class,
                Converters.DOUBLE);
    }

}
