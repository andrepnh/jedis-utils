package com.andrepnh.jedis.utils;

import java.util.function.Function;

public class BooleanResult extends ForwardConvertableResult<Boolean> {

    public BooleanResult() {
        super(Boolean.class);
    }

    @Override
    protected Function<String, Boolean> getConverter() {
        return Converters.BOOLEAN;
    }
    
}
