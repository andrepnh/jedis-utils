package com.andrepnh.jedis.utils;

public class ConversionException extends RuntimeException {
    
    private static final long serialVersionUID = 1;
    
    private static final String PARAMETERIZED_MESSAGE = "%s is not a valid %s";
    
    private final String value;
    
    private final Class<?> targetType;

    public ConversionException(String value, Class<?> targetType) {
        super(String.format(PARAMETERIZED_MESSAGE, 
                value, targetType.getSimpleName()));
        this.value = value;
        this.targetType = targetType;
    }

    public ConversionException(String value, Class<?> targetType, Throwable cause) {
        super(String.format(PARAMETERIZED_MESSAGE, 
                value, targetType.getSimpleName()), 
                cause);
        this.value = value;
        this.targetType = targetType;
    }
    
    public String getValue() {
        return value;
    }

    public Class<?> getTargetType() {
        return targetType;
    }
    
}
