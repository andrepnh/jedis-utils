package com.andrepnh.jedis.utils;

import static java.util.Objects.requireNonNull;
import redis.clients.jedis.Response;

class ResponseBasedResultWrapper<T> implements ResultWrapper<T> {
    
    private final Response<T> response;

    public ResponseBasedResultWrapper(Response<T> response) {
        this.response = requireNonNull(response);
    }

    @Override
    public T get() {
        return response.get();
    }

}
