package com.andrepnh.jedis.utils.blocks;

import java.util.Optional;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.BDDMockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Response;

public class ResultTest {

    private Result<String> result;
    
    @Mock
    private Response<String> response;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        result = new Result<>();
    }

    @Test
    public void shouldGetAnEmptyResultForANewResult() {
        assertEmptyResult(result);
    }
    
    @Test
    public void shouldGetAnEmptyResultWhenTheWrappedCommandOutputIsNull() {
        result.wrapping((String) null);
        assertEmptyResult(result);
    }
    
    @Test
    public void shouldGetAnEmptyResultWhenTheWrappedJedisResponseContainsNull() {
        result.wrapping(response);
        assertEmptyResult(result);
    }
    
    @Test
    public void shouldGetTheWrappedCommandOutput() {
        result.wrapping("foo");
        assertResultContaining("foo", result);
    }
    
    @Test
    public void shouldGetTheWrappedJedisResponseContent() {
        given(response.get()).willReturn("foo");
        result.wrapping(response);
        assertResultContaining("foo", result);
    }
    
    @Test(expected = NullPointerException.class)
    public void shouldNotWrappANullJedisResponse() {
        result.wrapping((Response<String>) null);
    }

    private <T> void assertEmptyResult(Result<T> result) {
        assertNull(result.get());
        assertEquals(Optional.empty(), result.asOptional());
    }

    private <T> void assertResultContaining(T value, Result<T> result) {
        assertEquals(value, result.get());
        assertEquals(Optional.of(value), result.asOptional());
    }

}