package com.andrepnh.jedis.utils.fluent;

import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.BDDMockito.given;
import org.mockito.InOrder;
import org.mockito.Mock;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class PipelinedCommandsTest extends CommandsTest<PipelinedCommands> {

    private PipelinedCommands commands;
    
    @Mock
    private Jedis jedis;
    
    @Mock
    private Pipeline pipeline;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(jedis.pipelined()).thenReturn(pipeline);
        commands = newCommandsUnderTest(jedis);
    }
    
    @Test
    public void shouldWrapCommandsInPipeline() {
        commands.call(pipeline -> pipeline.get("foo"));
        InOrder inOrder = inOrder(jedis, pipeline);
        inOrder.verify(jedis).pipelined();
        inOrder.verify(pipeline).get("foo");
        inOrder.verify(pipeline).syncAndReturnAll();
    }
    
    @Test
    public void shouldCloseJedisAfterAllCommands() {
        commands.call(pipeline -> pipeline.get("foo"));
        verifyJedisClosedAfterAllCommands(jedis);
    }
    
    @Test
    public void shouldReturnPipelineSyncAndReturnAllOutput() {
        List<Object> syncAndReturnAllOutput = Collections.nCopies(3, "pipsqueak");
        given(pipeline.syncAndReturnAll()).willReturn(syncAndReturnAllOutput);
        assertEquals(syncAndReturnAllOutput, 
                commands.call(pipeline -> pipeline.get("1")));
    }

    @Override
    PipelinedCommands newCommandsUnderTest(Jedis jedisMock) {
        return new PipelinedCommands(jedisMock);
    }

}