package com.andrepnh.jedis.utils.blocks;

import com.andrepnh.jedis.utils.TypedJedis;
import static java.util.Objects.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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
     * 
     * @see #consume(java.util.function.BiConsumer) for a variation that also
     * exposes a {@link TypedJedis}
     */
    public void consume(Consumer<Jedis> commands) {
        try (Jedis jedis = this.jedis) {
            commands.accept(jedis);
        }
    }
    
    /**
     * Like {@link #consume(java.util.function.Consumer)}, but a {@link TypedJedis}
     * is also available for usage inside the lambda expression
     */
    public void consume(BiConsumer<Jedis, TypedJedis> commands) {
        try (Jedis jedis = this.jedis) {
            TypedJedis typedJedis = new TypedJedis(jedis);
            commands.accept(jedis, typedJedis);
        }
    }
    
    /**
     * Executes {@code commands} without transactions or pipelines, returning 
     * the result of the lambda expression.
     * 
     * @see #call(java.util.function.BiFunction) for a variation that also
     * exposes a {@link TypedJedis}
     */
    public <T> T call(Function<Jedis, T> commands) {
        try (Jedis jedis = this.jedis) {
            return commands.apply(jedis);
        }
    }
    
    /**
     * Like {@link #call(java.util.function.Function)}, but a {@link TypedJedis}
     * is also available for usage inside the lambda expression
     */
    public <T> T call(BiFunction<Jedis, TypedJedis, T> commands) {
        try (Jedis jedis = this.jedis) {
            TypedJedis typedJedis = new TypedJedis(jedis);
            return commands.apply(jedis, typedJedis);
        }
    }
    
}
