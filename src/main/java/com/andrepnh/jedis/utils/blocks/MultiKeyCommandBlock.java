package com.andrepnh.jedis.utils.blocks;

import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.function.Consumer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.MultiKeyPipelineBase;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

abstract class MultiKeyCommandBlock<T extends MultiKeyPipelineBase> 
        implements Commands {
    
    private final Jedis jedis;

    protected MultiKeyCommandBlock(Jedis jedis) {
        this.jedis = requireNonNull(jedis);
    }

    abstract T before(Jedis jedis);
    
    /**
     * Executes {@code commands} inside a transaction, pipeline or both, 
     * depending on how the current command block was built.
     * 
     * @return bulk response from redis. In the case of pipelined transactions, 
     * only the output of {@link Pipeline#exec()} is returned.
     * @see Result used to wrap specific responses, like in {@link Response}
     */
    public List<Object> consume(Consumer<T> commands) {
        try (Jedis jedis = this.jedis) {
            T block = before(jedis);
            commands.accept(block);
            return after(block);
        }
    }
    
    abstract List<Object> after(T block);
    
    protected final Jedis getJedis() {
        return jedis;
    }
    
}
