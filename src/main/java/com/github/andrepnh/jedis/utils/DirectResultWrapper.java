package com.github.andrepnh.jedis.utils;

class DirectResultWrapper<T> implements ResultWrapper<T> {
    
    private final T result;

    DirectResultWrapper(T result) {
        this.result = result;
    }
    
    @Override
    public T get() {
        return result;
    }

}
