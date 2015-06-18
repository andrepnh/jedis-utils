package com.andrepnh.jedis.utils;

import redis.clients.jedis.Response;

/**
 * Used to wrap a {@link Response} return by a jedis command inside a transaction
 * and pipeline; or to just wrap a plain command output. This class is not thread
 * safe.
 */
public class Result<T> implements ResultWrapper<T> {
    
    private ResultWrapper<T> delegate = new DirectResultWrapper<>(null);

    public void wrapping(Response<T> jedisResponse) {
        delegate = new ResponseBasedResultWrapper<>(jedisResponse);
    }
    
    public void wrapping(T commandResult) {
        delegate = new DirectResultWrapper<>(commandResult);
    }
    
    /**
     * Returns the wrapped command output, which may be {@code null} if that's 
     * jedis answer. Like {@link Response}, it will also return {@code null} 
     * if it was used inside a transaction or pipeline, but this method was 
     * called before it finished.
     * <p>
     * Prefer {@link #asOptional()}.
     */
    @Override
    public T get() {
        return delegate.get();
    }
    
}
