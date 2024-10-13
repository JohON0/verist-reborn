/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.functions.api;

import im.expensive.functions.api.Category;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface FunctionRegister {
    public String name();

    public int key() default 0;

    public Category type();
}

