package com.andrepnh.jedis.utils;

import java.util.Optional;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.BDDMockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;

public class TypedJedisTest {
    
    private static final double DOUBLE_COMPARISON_TOLERANCE = 1e-15;

    private TypedJedis typedJedis;
    
    @Mock
    private Jedis jedis;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        typedJedis = new TypedJedis(jedis);
    }

    // GET tests --------------------------------------------------------------
    
    @Test
    public void shouldIgnoreWhenGetReturnsBooleanWithDifferentCase() {
        given(jedis.get("trueKey")).willReturn("True");
        given(jedis.get("falseKey")).willReturn("FALSE");
        
        assertTrue(typedJedis.getBoolean("trueKey").get());
        assertFalse(typedJedis.getBoolean("falseKey").get());
    }
    
    @Test(expected = ConversionException.class)
    public void shouldThrowConversionExceptionWhenGetReturnsInvalidBoolean() {
        given(jedis.get("foo")).willReturn("a");
        
        typedJedis.getBoolean("foo");
    }
    
    @Test
    public void shouldReturnEmptyBooleanOptionalWhenGetReturnsNull() {
        assertEquals(Optional.empty(), typedJedis.getBoolean("a"));
    }
    
    @Test
    public void shouldConvertWhenGetReturnsLong() {
        given(jedis.get("foo")).willReturn(String.valueOf(Long.MIN_VALUE));
        
        assertEquals(Long.MIN_VALUE, (long) typedJedis.getLong("foo").get());
    }
    
    @Test(expected = ConversionException.class)
    public void shouldThrowConversionExceptionWhenGetReturnsInvalidLong() {
        given(jedis.get("foo")).willReturn("1.1");
        
        typedJedis.getLong("foo");
    }
    
    @Test
    public void shouldReturnEmptyLongOptionalWhenGetReturnsNull() {
        assertEquals(Optional.empty(), typedJedis.getLong("a"));
    }
    
    @Test
    public void shouldConvertWhenGetReturnsInteger() {
        given(jedis.get("foo")).willReturn(String.valueOf(Integer.MIN_VALUE));
        
        assertEquals(Integer.MIN_VALUE, (int) typedJedis.getInteger("foo").get());
    }
    
    @Test(expected = ConversionException.class)
    public void shouldThrowConversionExceptionWhenGetReturnsInvalidInteger() {
        given(jedis.get("foo")).willReturn("1.1");
        
        typedJedis.getInteger("foo");
    }
    
    @Test
    public void shouldReturnEmptyIntegerOptionalWhenGetReturnsNull() {
        assertEquals(Optional.empty(), typedJedis.getInteger("a"));
    }
    
    @Test
    public void shouldConvertWhenGetReturnsDouble() {
        double value = 0.1;
        given(jedis.get("foo")).willReturn(String.valueOf(value));
        assertEquals(value, typedJedis.getDouble("foo").get(), 
                DOUBLE_COMPARISON_TOLERANCE);
    }
    
    @Test(expected = ConversionException.class)
    public void shouldThrowConversionExceptionWhenGetReturnsInvalidDouble() {
        given(jedis.get("foo")).willReturn("a");
        
        typedJedis.getDouble("foo");
    }
    
    @Test
    public void shouldReturnEmptyDoubleOptionalWhenGetReturnsNull() {
        assertEquals(Optional.empty(), typedJedis.getDouble("a"));
    }

    // GETSET tests -----------------------------------------------------------
    
    @Test
    public void shouldIgnoreWhenGetSetReturnsBooleanWithDifferentCase() {
        given(jedis.getSet("trueKey", "true")).willReturn("True");
        given(jedis.getSet("falseKey", "false")).willReturn("FALSE");
        
        assertTrue(typedJedis.getSetBoolean("trueKey", true).get());
        assertFalse(typedJedis.getSetBoolean("falseKey", false).get());
    }
    
    @Test(expected = ConversionException.class)
    public void shouldThrowConversionExceptionWhenGetSetReturnsInvalidBoolean() {
        given(jedis.getSet("foo", "true")).willReturn("a");
        
        typedJedis.getSetBoolean("foo", true);
    }
    
    @Test
    public void shouldReturnEmptyBooleanOptionalWhenGetSetReturnsNull() {
        assertEquals(Optional.empty(), typedJedis.getSetBoolean("a", true));
    }
    
    @Test
    public void shouldConvertWhenGetSetReturnsLong() {
        given(jedis.getSet("foo", String.valueOf(Long.MAX_VALUE)))
                .willReturn(String.valueOf(Long.MIN_VALUE));
        
        assertEquals(Long.MIN_VALUE, (long) typedJedis
                .getSetLong("foo", Long.MAX_VALUE).get());
    }
    
    @Test(expected = ConversionException.class)
    public void shouldThrowConversionExceptionWhenGetSetReturnsInvalidLong() {
        given(jedis.getSet("foo", "1")).willReturn("1.1");
        
        typedJedis.getSetLong("foo", 1);
    }
    
    @Test
    public void shouldReturnEmptyLongOptionalWhenGetSetReturnsNull() {
        assertEquals(Optional.empty(), typedJedis.getSetLong("a", 1));
    }
    
    @Test
    public void shouldConvertWhenGetSetReturnsInteger() {
        given(jedis.getSet("foo", String.valueOf(Integer.MAX_VALUE)))
                .willReturn(String.valueOf(Integer.MIN_VALUE));
        
        assertEquals(Integer.MIN_VALUE, (int) typedJedis
                .getSetInteger("foo", Integer.MAX_VALUE).get());
    }
    
    @Test(expected = ConversionException.class)
    public void shouldThrowConversionExceptionWhenGetSetReturnsInvalidInteger() {
        given(jedis.getSet("foo", "1")).willReturn("1.1");
        
        typedJedis.getSetInteger("foo", 1);
    }
    
    @Test
    public void shouldReturnEmptyIntegerOptionalWhenGetSetReturnsNull() {
        assertEquals(Optional.empty(), typedJedis.getSetInteger("a", 1));
    }
    
    @Test
    public void shouldConvertWhenGetSetReturnsDouble() {
        double newValue = 2;
        given(jedis.getSet("foo", String.valueOf(newValue)))
                .willReturn(String.valueOf(0.1));
        
        assertEquals(0.1, typedJedis.getSetDouble("foo", newValue).get(), 
                DOUBLE_COMPARISON_TOLERANCE);
    }
    
    @Test(expected = ConversionException.class)
    public void shouldThrowConversionExceptionWhenGetSetReturnsInvalidDouble() {
        double newValue = 1;
        given(jedis.getSet("foo", String.valueOf(newValue))).willReturn("a");
        
        typedJedis.getSetDouble("foo", newValue);
    }
    
    @Test
    public void shouldReturnEmptyDoubleOptionalWhenGetSetReturnsNull() {
        assertEquals(Optional.empty(), typedJedis.getSetDouble("a", 1));
    }
    
    // HGET tests -------------------------------------------------------------
    
    @Test
    public void shouldIgnoreWhenHGetReturnsBooleanWithDifferentCase() {
        given(jedis.hget("trueKey", "field")).willReturn("True");
        given(jedis.hget("falseKey", "field")).willReturn("FALSE");
        
        assertTrue(typedJedis.hgetBoolean("trueKey", "field").get());
        assertFalse(typedJedis.hgetBoolean("falseKey", "field").get());
    }
    
    @Test(expected = ConversionException.class)
    public void shouldThrowConversionExceptionWhenHGetReturnsInvalidBoolean() {
        given(jedis.hget("foo", "field")).willReturn("a");
        
        typedJedis.hgetBoolean("foo", "field");
    }
    
    @Test
    public void shouldReturnEmptyBooleanOptionalWhenHGetReturnsNull() {
        assertEquals(Optional.empty(), typedJedis.hgetBoolean("a", "field"));
    }
    
    @Test
    public void shouldConvertWhenHGetReturnsLong() {
        given(jedis.hget("foo", "field"))
                .willReturn(String.valueOf(Long.MIN_VALUE));
        
        assertEquals(Long.MIN_VALUE, 
                (long) typedJedis.hgetLong("foo", "field").get());
    }
    
    @Test(expected = ConversionException.class)
    public void shouldThrowConversionExceptionWhenHGetReturnsInvalidLong() {
        given(jedis.hget("foo", "field")).willReturn("1.1");
        
        typedJedis.hgetLong("foo", "field");
    }
    
    @Test
    public void shouldReturnEmptyLongOptionalWhenHGetReturnsNull() {
        assertEquals(Optional.empty(), typedJedis.hgetLong("a", "field"));
    }
    
    @Test
    public void shouldConvertWhenHGetReturnsInteger() {
        given(jedis.hget("foo", "field"))
                .willReturn(String.valueOf(Integer.MIN_VALUE));
        
        assertEquals(Integer.MIN_VALUE, 
                (int) typedJedis.hgetInteger("foo", "field").get());
    }
    
    @Test(expected = ConversionException.class)
    public void shouldThrowConversionExceptionWhenHGetReturnsInvalidInteger() {
        given(jedis.hget("foo", "field")).willReturn("1.1");
        
        typedJedis.hgetInteger("foo", "field");
    }
    
    @Test
    public void shouldReturnEmptyIntegerOptionalWhenHGetReturnsNull() {
        assertEquals(Optional.empty(), typedJedis.hgetInteger("a", "foo"));
    }
    
    @Test
    public void shouldConvertWhenHGetReturnsDouble() {
        double value = 0.1;
        given(jedis.hget("foo", "field")).willReturn(String.valueOf(value));
        assertEquals(value, typedJedis.hgetDouble("foo", "field").get(), 
                DOUBLE_COMPARISON_TOLERANCE);
    }
    
    @Test(expected = ConversionException.class)
    public void shouldThrowConversionExceptionWhenHGetReturnsInvalidDouble() {
        given(jedis.hget("foo", "field")).willReturn("a");
        
        typedJedis.hgetDouble("foo", "field");
    }
    
    @Test
    public void shouldReturnEmptyDoubleOptionalWhenHGetReturnsNull() {
        assertEquals(Optional.empty(), typedJedis.hgetDouble("a", "field"));
    }
}