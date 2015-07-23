package com.github.andrepnh.jedis.utils.blocks;

import com.github.andrepnh.jedis.utils.blocks.PipelinedTransactedCommandBlock;
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
import redis.clients.jedis.Response;

public class PipelinedTransactedCommandBlockTest 
        extends CommandsTest<PipelinedTransactedCommandBlock> {

    private PipelinedTransactedCommandBlock commands;
    
    @Mock
    private Jedis jedis;
    
    @Mock
    private Pipeline pipeline;
    
    @Mock
    private Response<List<Object>> txResponse;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(jedis.pipelined()).thenReturn(pipeline);
        when(pipeline.exec()).thenReturn(txResponse);
        commands = newCommandsUnderTest(jedis);
    }
    
    @Test
    public void shouldWrapCommandsInPipelinedTransaction() {
        commands.consume(pipeline -> pipeline.get("foo"));
        InOrder inOrder = inOrder(jedis, pipeline);
        inOrder.verify(jedis).pipelined();
        inOrder.verify(pipeline).multi();
        inOrder.verify(pipeline).get("foo");
        inOrder.verify(pipeline).exec();
        inOrder.verify(pipeline).syncAndReturnAll();
    }
    
    @Test
    public void shouldCloseJedisAfterAllCommands() {
        commands.consume(pipeline -> pipeline.get("foo"));
        verifyJedisClosedAfterAllCommands(jedis);
    }
    
    @Test
    public void shouldReturnPipelineExecOutput() {
        given(txResponse.get()).willReturn(Collections.nCopies(3, "pipsqueak"));
        assertEquals(txResponse.get(), 
                commands.consume(pipeline -> pipeline.get("1")));
    }

    @Override
    PipelinedTransactedCommandBlock newCommandsUnderTest(Jedis jedisMock) {
        return new PipelinedTransactedCommandBlock(jedisMock);
    }

}