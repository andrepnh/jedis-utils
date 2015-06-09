package com.andrepnh.jedis.utils.blocks;

import java.util.Optional;

public interface ResultWrapper<T> {
    
    T get();
    
    default Optional<T> asOptional() {
        return Optional.ofNullable(get());
    }

}
