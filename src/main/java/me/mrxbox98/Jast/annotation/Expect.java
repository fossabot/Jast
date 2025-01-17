package me.mrxbox98.Jast.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Expect {

    /**
     * Gets the expected value in JSON form
     * @return the expected values
     */
    public String expect() default "";

}
