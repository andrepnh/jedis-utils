package com.andrepnh.jedis.utils.fluent;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;

public class PlainCommandsTest extends CommandsTest<PlainCommands> {

    private PlainCommands simpleCommands;
    
    @Mock
    private Jedis jedis;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        simpleCommands = newCommandsUnderTest(jedis);
    }
    
    @Test
    public void shouldCloseJedisAfterConsumingCommands() {
        simpleCommands.consume(jedis -> jedis.get("foo"));
        verifyJedisClosedAfterAllCommands(jedis);
    }
    
    @Test
    public void shouldCloseJedisAfterReturningCommands() {
        simpleCommands.call(jedis -> jedis.get("foo"));
        verifyJedisClosedAfterAllCommands(jedis);
    }

    @Override
    public PlainCommands newCommandsUnderTest(Jedis jedisMock) {
        return new PlainCommands(jedisMock);
    }

}