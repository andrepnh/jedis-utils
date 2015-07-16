package com.andrepnh.jedis.utils;

import java.util.Optional;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.BDDMockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Response;

public class ForwardConvertableResultTest {
    
    private ForwardConvertableResult<Long> convertableResult;
    
    @Mock
    private Response<String> responseMock;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        convertableResult = new LongResult();
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotWrapNullJedisResponse() {
        convertableResult.wrapping((Response<String>) null);
    }
    
    @Test
    public void shouldReturnNullAfterWrappingNullCommandResult() {
        // when
        convertableResult.wrapping((String) null);
        
        // then
        assertNull(convertableResult.get());
    }
    
    @Test
    public void shouldReturnEmptyOptionalAfterWrappingNullCommandResult() {
        // when
        convertableResult.wrapping((String) null);
        
        // then
        assertEquals(Optional.empty(), convertableResult.asOptional());
    }
    
    @Test
    public void shouldReturnNullAfterWrappingEmptyJedisResponse() {
        // when
        convertableResult.wrapping(responseMock);
        
        // then
        assertNull(convertableResult.get());
    }
    
    @Test
    public void shouldReturnEmptyOptionalAfterWrappingEmptyJedisResponse() {
        // when
        convertableResult.wrapping(responseMock);
        
        // then
        assertEquals(Optional.empty(), convertableResult.asOptional());
    }
    
    @Test
    public void shouldFailOnlyOnRetrievalWhenTheWrappedJedisResponseCannotBeConverted() {
        // given
        given(responseMock.get()).willReturn("a");
        
        // when
        convertableResult.wrapping(responseMock);
        
        // then
        try {
            convertableResult.asOptional();
            fail("Expecting " + ConversionException.class.getSimpleName());
        } catch (ConversionException e) {
            // noop
        }
    }
    
    @Test(expected = ConversionException.class)
    public void shouldFailImmediatelyWhenCommandResultCannotBeConverted() {
        convertableResult.wrapping("a");
    }

}