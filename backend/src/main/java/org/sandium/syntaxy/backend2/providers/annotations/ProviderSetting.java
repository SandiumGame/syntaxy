package org.sandium.syntaxy.backend2.providers.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME) @Target({METHOD})
public @interface ProviderSetting {

    String value();

    boolean passwordField() default false;

}
