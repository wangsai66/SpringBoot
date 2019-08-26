package com.base.base.param;

import java.lang.annotation.*;
import java.util.Map;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataType {

    Class<?> value() default Void.class;
}
