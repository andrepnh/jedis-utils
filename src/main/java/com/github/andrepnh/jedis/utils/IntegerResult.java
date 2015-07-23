package com.github.andrepnh.jedis.utils;

import java.util.function.Function;

public class IntegerResult extends ForwardConvertableResult<Integer> {

    public IntegerResult() {
        super(Integer.class);
    }

    @Override
    protected Function<String, Integer> getConverter() {
        return Converters.INTEGER;
    }
    
}
