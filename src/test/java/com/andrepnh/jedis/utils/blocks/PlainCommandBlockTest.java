package com.andrepnh.jedis.utils.blocks;

import com.andrepnh.jedis.utils.blocks.PlainCommandBlock;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;

public class PlainCommandBlockTest extends CommandsTest<PlainCommandBlock> {

    private PlainCommandBlock simpleCommands;
    
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
    public PlainCommandBlock newCommandsUnderTest(Jedis jedisMock) {
        return new PlainCommandBlock(jedisMock);
    }

}