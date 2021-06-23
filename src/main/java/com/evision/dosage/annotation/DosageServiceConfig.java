package com.evision.dosage.annotation;

import java.lang.annotation.*;

/**
 * @author Xue Bing
 * @version 1.0
 * @date 2020-02-21 13:42
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DosageServiceConfig {
    String value();
}
