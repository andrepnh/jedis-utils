package com.github.andrepnh.jedis.utils.blocks;

import java.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class PipelinedTransactedCommandBlock extends MultiKeyCommandBlock<Pipeline> {
    
    PipelinedTransactedCommandBlock(Jedis jedis) {
        super(jedis);
    }
    
    @Override
    Pipeline before(Jedis jedis) {
        Pipeline pipeline = jedis.pipelined();
        pipeline.multi();
        return pipeline;
    }
    
    @Override
    List<Object> after(Pipeline pipeline) {
        Response<List<Object>> txOutput = pipeline.exec();
        pipeline.syncAndReturnAll();
        return txOutput.get();
    }
    
}
