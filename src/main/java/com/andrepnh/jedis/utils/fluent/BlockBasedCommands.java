package com.andrepnh.jedis.utils.fluent;

import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.function.Consumer;
import redis.clients.jedis.Jedis;

abstract class BlockBasedCommands<T> implements Commands {
    
    private final Jedis jedis;

    protected BlockBasedCommands(Jedis jedis) {
        this.jedis = requireNonNull(jedis);
    }

    abstract T before(Jedis jedis);
    
    public List<Object> call(Consumer<T> commands) {
        try (Jedis jedis = this.jedis) {
            T block = before(jedis);
            commands.accept(block);
            return after(block);
        }
    }
    
    abstract List<Object> after(T block);
    
    @Override
    public Jedis getJedis() {
        return jedis;
    }
    
}
