package com.andrepnh.jedis.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;

/**
 *
 * @author André Pinheiro de Melo
 */
public class TypedJedisTest {
    
    private static final String BIG_DECIMAL_TEST_VALUE = "0.123456789123456789";
    
    private static final String BIG_INTEGER_TEST_VALUE 
            = String.valueOf(Long.MAX_VALUE) + "1";
    
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
    public void getBooleanIgnoresCase() {
        when(jedis.get("trueKey")).thenReturn("True");
        when(jedis.get("falseKey")).thenReturn("FALSE");
        
        assertTrue(typedJedis.getBoolean("trueKey").get());
        assertFalse(typedJedis.getBoolean("falseKey").get());
    }
    
    @Test(expected = ConversionException.class)
    public void getBooleanFailsForInvalidStrings() {
        when(jedis.get("foo")).thenReturn("a");
        
        typedJedis.getBoolean("foo");
    }
    
    @Test
    public void getBooleanReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.getBoolean("a"));
    }
    
    @Test
    public void getLongConvertsCorrectly() {
        when(jedis.get("foo")).thenReturn(String.valueOf(Long.MIN_VALUE));
        
        assertEquals(Long.MIN_VALUE, (long) typedJedis.getLong("foo").get());
    }
    
    @Test(expected = ConversionException.class)
    public void getLongFailsOnInvalidNumbers() {
        when(jedis.get("foo")).thenReturn("1.1");
        
        typedJedis.getLong("foo");
    }
    
    @Test
    public void getLongReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.getLong("a"));
    }
    
    @Test
    public void getIntegerConvertsCorrectly() {
        when(jedis.get("foo")).thenReturn(String.valueOf(Integer.MIN_VALUE));
        
        assertEquals(Integer.MIN_VALUE, (int) typedJedis.getInteger("foo").get());
    }
    
    @Test(expected = ConversionException.class)
    public void getIntegerFailsOnInvalidNumbers() {
        when(jedis.get("foo")).thenReturn("1.1");
        
        typedJedis.getInteger("foo");
    }
    
    @Test
    public void getIntegerReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.getInteger("a"));
    }
    
    @Test
    public void getBigIntegerConvertsCorrectly() {
        when(jedis.get("foo")).thenReturn(BIG_INTEGER_TEST_VALUE);
        
        assertEquals(new BigInteger(BIG_INTEGER_TEST_VALUE), 
                typedJedis.getBigInteger("foo").get());
    }
    
    @Test(expected = ConversionException.class)
    public void getBigIntegerFailsOnInvalidNumbers() {
        when(jedis.get("foo")).thenReturn("1.1");
        
        typedJedis.getBigInteger("foo");
    }
    
    @Test
    public void getBigIntegerReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.getBigInteger("a"));
    }
    
    @Test
    public void getDoubleConvertsCorrectly() {
        double value = 0.1;
        when(jedis.get("foo")).thenReturn(String.valueOf(value));
        assertEquals(value, typedJedis.getDouble("foo").get(), 
                DOUBLE_COMPARISON_TOLERANCE);
    }
    
    @Test(expected = ConversionException.class)
    public void getDoubleFailsOnInvalidNumbers() {
        when(jedis.get("foo")).thenReturn("a");
        
        typedJedis.getDouble("foo");
    }
    
    @Test
    public void getDoubleReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.getDouble("a"));
    }
    
    @Test
    public void getBigDecimalConvertsCorrectly() {
        when(jedis.get("foo")).thenReturn(BIG_DECIMAL_TEST_VALUE);
        
        assertEquals(new BigDecimal(BIG_DECIMAL_TEST_VALUE), 
                typedJedis.getBigDecimal("foo").get());
    }
    
    @Test(expected = ConversionException.class)
    public void getBigDecimalFailsOnInvalidNumbers() {
        when(jedis.get("foo")).thenReturn("a");
        
        typedJedis.getBigDecimal("foo");
    }
    
    @Test
    public void getBigDecimalReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.getBigDecimal("a"));
    }

    // GETSET tests -----------------------------------------------------------
    
    @Test
    public void getSetBooleanIgnoresCase() {
        when(jedis.getSet("trueKey", "true")).thenReturn("True");
        when(jedis.getSet("falseKey", "false")).thenReturn("FALSE");
        
        assertTrue(typedJedis.getSetBoolean("trueKey", true).get());
        assertFalse(typedJedis.getSetBoolean("falseKey", false).get());
    }
    
    @Test(expected = ConversionException.class)
    public void getSetBooleanFailsForInvalidStrings() {
        when(jedis.getSet("foo", "true")).thenReturn("a");
        
        typedJedis.getSetBoolean("foo", true);
    }
    
    @Test
    public void getSetBooleanReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.getSetBoolean("a", true));
    }
    
    @Test
    public void getSetLongConvertsCorrectly() {
        when(jedis.getSet("foo", String.valueOf(Long.MAX_VALUE)))
                .thenReturn(String.valueOf(Long.MIN_VALUE));
        
        assertEquals(Long.MIN_VALUE, (long) typedJedis
                .getSetLong("foo", Long.MAX_VALUE).get());
    }
    
    @Test(expected = ConversionException.class)
    public void getSetLongFailsOnInvalidNumbers() {
        when(jedis.getSet("foo", "1")).thenReturn("1.1");
        
        typedJedis.getSetLong("foo", 1);
    }
    
    @Test
    public void getSetLongReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.getSetLong("a", 1));
    }
    
    @Test
    public void getSetIntegerConvertsCorrectly() {
        when(jedis.getSet("foo", String.valueOf(Integer.MAX_VALUE)))
                .thenReturn(String.valueOf(Integer.MIN_VALUE));
        
        assertEquals(Integer.MIN_VALUE, (int) typedJedis
                .getSetInteger("foo", Integer.MAX_VALUE).get());
    }
    
    @Test(expected = ConversionException.class)
    public void getSetIntegerFailsOnInvalidNumbers() {
        when(jedis.getSet("foo", "1")).thenReturn("1.1");
        
        typedJedis.getSetInteger("foo", 1);
    }
    
    @Test
    public void getSetIntegerReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.getSetInteger("a", 1));
    }
    
    @Test
    public void getSetBigIntegerConvertsCorrectly() {
        when(jedis.getSet("foo", "1")).thenReturn(BIG_INTEGER_TEST_VALUE);
        
        assertEquals(new BigInteger(BIG_INTEGER_TEST_VALUE), 
                typedJedis.getSetBigInteger("foo", BigInteger.ONE).get());
    }
    
    @Test(expected = ConversionException.class)
    public void getSetBigIntegerFailsOnInvalidNumbers() {
        when(jedis.getSet("foo", "1")).thenReturn("1.1");
        
        typedJedis.getSetBigInteger("foo", BigInteger.ONE);
    }
    
    @Test
    public void getSetBigIntegerReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), 
                typedJedis.getSetBigInteger("a", BigInteger.ONE));
    }
    
    @Test
    public void getSetDoubleConvertsCorrectly() {
        double newValue = 2;
        when(jedis.getSet("foo", String.valueOf(newValue)))
                .thenReturn(String.valueOf(0.1));
        
        assertEquals(0.1, typedJedis.getSetDouble("foo", newValue).get(), 
                DOUBLE_COMPARISON_TOLERANCE);
    }
    
    @Test(expected = ConversionException.class)
    public void getSetDoubleFailsOnInvalidNumbers() {
        double newValue = 1;
        when(jedis.getSet("foo", String.valueOf(newValue))).thenReturn("a");
        
        typedJedis.getSetDouble("foo", newValue);
    }
    
    @Test
    public void getSetDoubleReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.getSetDouble("a", 1));
    }
    
    @Test
    public void getSetBigDecimalConvertsCorrectly() {
        when(jedis.getSet("foo", "1")).thenReturn(BIG_DECIMAL_TEST_VALUE);
        
        assertEquals(new BigDecimal(BIG_DECIMAL_TEST_VALUE), 
                typedJedis.getSetBigDecimal("foo", BigDecimal.ONE).get());
    }
    
    @Test(expected = ConversionException.class)
    public void getSetBigDecimalFailsOnInvalidNumbers() {
        when(jedis.getSet("foo", "1")).thenReturn("a");
        
        typedJedis.getSetBigDecimal("foo", BigDecimal.ONE);
    }
    
    @Test
    public void getSetBigBigDecimalReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), 
                typedJedis.getSetBigDecimal("a", BigDecimal.ONE));
    }
    
    // HGET tests -------------------------------------------------------------
    
    @Test
    public void hgetBooleanIgnoresCase() {
        when(jedis.hget("trueKey", "field")).thenReturn("True");
        when(jedis.hget("falseKey", "field")).thenReturn("FALSE");
        
        assertTrue(typedJedis.hgetBoolean("trueKey", "field").get());
        assertFalse(typedJedis.hgetBoolean("falseKey", "field").get());
    }
    
    @Test(expected = ConversionException.class)
    public void hgetBooleanFailsForInvalidStrings() {
        when(jedis.hget("foo", "field")).thenReturn("a");
        
        typedJedis.hgetBoolean("foo", "field");
    }
    
    @Test
    public void hgetBooleanReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.hgetBoolean("a", "field"));
    }
    
    @Test
    public void hgetLongConvertsCorrectly() {
        when(jedis.hget("foo", "field"))
                .thenReturn(String.valueOf(Long.MIN_VALUE));
        
        assertEquals(Long.MIN_VALUE, 
                (long) typedJedis.hgetLong("foo", "field").get());
    }
    
    @Test(expected = ConversionException.class)
    public void hgetLongFailsOnInvalidNumbers() {
        when(jedis.hget("foo", "field")).thenReturn("1.1");
        
        typedJedis.hgetLong("foo", "field");
    }
    
    @Test
    public void hgetLongReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.hgetLong("a", "field"));
    }
    
    @Test
    public void hgetIntegerConvertsCorrectly() {
        when(jedis.hget("foo", "field"))
                .thenReturn(String.valueOf(Integer.MIN_VALUE));
        
        assertEquals(Integer.MIN_VALUE, 
                (int) typedJedis.hgetInteger("foo", "field").get());
    }
    
    @Test(expected = ConversionException.class)
    public void hgetIntegerFailsOnInvalidNumbers() {
        when(jedis.hget("foo", "field")).thenReturn("1.1");
        
        typedJedis.hgetInteger("foo", "field");
    }
    
    @Test
    public void hgetIntegerReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.hgetInteger("a", "foo"));
    }
    
    @Test
    public void hgetBigIntegerConvertsCorrectly() {
        when(jedis.hget("foo", "field")).thenReturn(BIG_INTEGER_TEST_VALUE);
        
        assertEquals(new BigInteger(BIG_INTEGER_TEST_VALUE), 
                typedJedis.hgetBigInteger("foo", "field").get());
    }
    
    @Test(expected = ConversionException.class)
    public void hgetBigIntegerFailsOnInvalidNumbers() {
        when(jedis.hget("foo", "field")).thenReturn("1.1");
        
        typedJedis.hgetBigInteger("foo", "field");
    }
    
    @Test
    public void hgetBigIntegerReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.hgetBigInteger("a", "field"));
    }
    
    @Test
    public void hgetDoubleConvertsCorrectly() {
        double value = 0.1;
        when(jedis.hget("foo", "field")).thenReturn(String.valueOf(value));
        assertEquals(value, typedJedis.hgetDouble("foo", "field").get(), 
                DOUBLE_COMPARISON_TOLERANCE);
    }
    
    @Test(expected = ConversionException.class)
    public void hgetDoubleFailsOnInvalidNumbers() {
        when(jedis.hget("foo", "field")).thenReturn("a");
        
        typedJedis.hgetDouble("foo", "field");
    }
    
    @Test
    public void hgetDoubleReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.hgetDouble("a", "field"));
    }
    
    @Test
    public void hgetBigDecimalConvertsCorrectly() {
        when(jedis.hget("foo", "field")).thenReturn(BIG_DECIMAL_TEST_VALUE);
        
        assertEquals(new BigDecimal(BIG_DECIMAL_TEST_VALUE), 
                typedJedis.hgetBigDecimal("foo", "field").get());
    }
    
    @Test(expected = ConversionException.class)
    public void hgetBigDecimalFailsOnInvalidNumbers() {
        when(jedis.hget("foo", "field")).thenReturn("a");
        
        typedJedis.hgetBigDecimal("foo", "field");
    }
    
    @Test
    public void hgetBigDecimalReturnsEmptyOptionalOnKeyNotFound() {
        assertEquals(Optional.empty(), typedJedis.hgetBigDecimal("a", "field"));
    }
}