package com.andrepnh.jedis.utils.fluent;

import static java.util.Objects.*;
import java.util.function.Consumer;
import java.util.function.Function;
import redis.clients.jedis.Jedis;

public class PlainCommands implements Commands {
    
    private final Jedis jedis;

    PlainCommands(Jedis jedis) {
        this.jedis = requireNonNull(jedis);
    }

    public PipelinedCommands pipelined() {
        return new PipelinedCommands(jedis);
    }

    public TransactedCommands transacted() {
        return new TransactedCommands(jedis);
    }
    
    public void consume(Consumer<Jedis> commands) {
        try (Jedis jedis = this.jedis) {
            commands.accept(jedis);
        }
    }
    
    public <T> T call(Function<Jedis, T> commands) {
        try (Jedis jedis = this.jedis) {
            return commands.apply(jedis);
        }
    }
    
    @Override
    public Jedis getJedis() {
        return jedis;
    }
    
}
