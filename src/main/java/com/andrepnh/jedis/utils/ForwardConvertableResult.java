package com.andrepnh.jedis.utils;

import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.function.Function;
import redis.clients.jedis.Response;

public abstract class ForwardConvertableResult<T> implements ResultWrapper<T> {
    
    private final Result<String> delegate = new Result<>();
    
    private final Class<T> clazz;

    protected ForwardConvertableResult(Class<T> clazz) {
        this.clazz = requireNonNull(clazz);
    }

    public void wrapping(Response<String> jedisResponse) {
        delegate.wrapping(jedisResponse);
    }

    public void wrapping(String commandResult) {
        delegate.wrapping(commandResult);
        // This will trigger a conversion and throw an exception if the result
        // is not valid
        get();
    }
    
    @Override
    public T get() {
        return asOptional().orElse(null);
    }

    @Override
    public Optional<T> asOptional() {
        return Converters.convert(delegate.get(), clazz, getConverter());
    }
    
    protected abstract Function<String, T> getConverter();
}
