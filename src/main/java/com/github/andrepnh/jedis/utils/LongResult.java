package com.github.andrepnh.jedis.utils;

import java.util.function.Function;

public class LongResult extends ForwardConvertableResult<Long> {

    public LongResult() {
        super(Long.class);
    }

    @Override
    protected Function<String, Long> getConverter() {
        return Converters.LONG;
    }
    
}
