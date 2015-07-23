package com.github.andrepnh.jedis.utils.blocks;

import java.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class PipelinedCommandBlock extends MultiKeyCommandBlock<Pipeline> {

    PipelinedCommandBlock(Jedis jedis) {
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

    /**
     * @return a command block that runs inside a pipeline and transaction.
     * Both are managed internally.
     */
    public PipelinedTransactedCommandBlock transacted() {
        return new PipelinedTransactedCommandBlock(getJedis());
    }
    
}
