package com.andrepnh.jedis.utils;

import java.util.function.Function;

public class DoubleResult extends ForwardConvertableResult<Double> {

    public DoubleResult() {
        super(Double.class);
    }

    @Override
    protected Function<String, Double> getConverter() {
        return Converters.DOUBLE;
    }
    
}
