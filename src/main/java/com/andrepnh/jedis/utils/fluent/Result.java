package com.andrepnh.jedis.utils.fluent;

import redis.clients.jedis.Response;

public class Result<T> implements ResultWrapper<T> {
    
    private ResultWrapper<T> delegate = new DirectResultWrapper<>(null);

    public void wrapping(Response<T> jedisResponse) {
        delegate = new ResponseBasedResultWrapper<>(jedisResponse);
    }
    
    public void wrapping(T commandResult) {
        delegate = new DirectResultWrapper<>(commandResult);
    }
    
    @Override
    public T get() {
        return delegate.get();
    }
    
}
