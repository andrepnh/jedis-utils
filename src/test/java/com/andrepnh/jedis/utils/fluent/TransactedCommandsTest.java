package com.andrepnh.jedis.utils.fluent;

import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.BDDMockito.given;
import org.mockito.InOrder;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TransactedCommandsTest extends CommandsTest<TransactedCommands> {

    private TransactedCommands commands;
    
    @Mock
    private Jedis jedis;
    
    @Mock
    private Transaction tx;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(jedis.multi()).thenReturn(tx);
        commands = newCommandsUnderTest(jedis);
    }
    
    @Test
    public void shouldWrapCommandsInTransaction() {
        commands.call(tx -> tx.get("foo"));
        InOrder inOrder = inOrder(jedis, tx);
        inOrder.verify(jedis).multi();
        inOrder.verify(tx).get("foo");
        inOrder.verify(tx).exec();
    }
    
    @Test
    public void shouldCloseJedisAfterAllCommands() {
        commands.call(tx -> tx.get("foo"));
        verifyJedisClosedAfterAllCommands(jedis);
    }
    
    @Test
    public void shouldReturnTransactionExecOutput() {
        List<Object> execOutput = Collections.nCopies(3, "quetzalcoatl");
        given(tx.exec()).willReturn(execOutput);
        assertEquals(execOutput, commands.call(tx -> tx.get("1")));
    }

    @Override
    TransactedCommands newCommandsUnderTest(Jedis jedisMock) {
        return new TransactedCommands(jedisMock);
    }
    
}