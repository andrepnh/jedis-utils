package com.andrepnh.jedis.utils.fluent;

import java.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class PipelinedTransactedCommands extends BlockBasedCommands<Pipeline> {
    
    PipelinedTransactedCommands(Jedis jedis) {
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
