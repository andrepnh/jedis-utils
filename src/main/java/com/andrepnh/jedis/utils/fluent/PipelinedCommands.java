package com.andrepnh.jedis.utils.fluent;

import java.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class PipelinedCommands extends BlockBasedCommands<Pipeline> {

    PipelinedCommands(Jedis jedis) {
        super(jedis);
    }
    
    @Override
    Pipeline before(Jedis jedis) {
        return jedis.pipelined();
    }
    
    @Override
    List<Object> after(Pipeline pipeline) {
        return pipeline.syncAndReturnAll();
    }

    public PipelinedTransactedCommands transacted() {
        return new PipelinedTransactedCommands(getJedis());
    }
    
}
