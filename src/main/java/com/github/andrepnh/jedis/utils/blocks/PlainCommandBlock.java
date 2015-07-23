package com.github.andrepnh.jedis.utils.blocks;

import static java.util.Objects.*;
import java.util.function.Consumer;
import java.util.function.Function;
import redis.clients.jedis.Jedis;

public class PlainCommandBlock implements Commands {
    
    private final Jedis jedis;

    PlainCommandBlock(Jedis jedis) {
        this.jedis = requireNonNull(jedis);
    }

    /**
     * @return the command block, now using a pipeline managed internally
     */
    public PipelinedCommandBlock pipelined() {
        return new PipelinedCommandBlock(jedis);
    }

    /**
     * @return the command block, now using a transaction managed internally
     */
    public TransactedCommandBlock transacted() {
        return new TransactedCommandBlock(jedis);
    }
    
    /**
     * Executes {@code commands} without transactions or pipelines.
     */
    public void consume(Consumer<Jedis> commands) {
        try (Jedis jedis = this.jedis) {
            commands.accept(jedis);
        }
    }
    
    /**
     * Executes {@code commands} without transactions or pipelines, returning 
     * the result of the lambda expression.
     */
    public <T> T call(Function<Jedis, T> commands) {
        try (Jedis jedis = this.jedis) {
            return commands.apply(jedis);
        }
    }
    
}
