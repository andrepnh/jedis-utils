package com.andrepnh.jedis.utils.blocks;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import static org.mockito.Mockito.inOrder;
import redis.clients.jedis.Jedis;

@Ignore
public abstract class CommandsTest<T extends Commands> {

    @Test(expected = NullPointerException.class)
    public void shouldNotAllowNullJedis() {
        newCommandsUnderTest(null);
    }
    
    abstract T newCommandsUnderTest(Jedis jedisMock);

    protected void verifyJedisClosedAfterAllCommands(Jedis jedisMock) {
        InOrder inOrder = inOrder(jedisMock);
        inOrder.verify(jedisMock).close();
        inOrder.verifyNoMoreInteractions();
    }

}
