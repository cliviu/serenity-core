package net.serenitybdd.junit.extensions;

import com.google.common.base.*;
import net.serenitybdd.junit.runners.*;
import net.thucydides.core.annotations.*;
import org.junit.runners.model.*;

import java.lang.reflect.*;

public class JUnit5TestMethodAnnotations {

    private final Method method;

    private JUnit5TestMethodAnnotations(final Method method) {
        this.method = method;
    }

    public static JUnit5TestMethodAnnotations forTest(final Method method) {
        return new JUnit5TestMethodAnnotations(method);
    }

    public boolean isDriverSpecified() {
        return (method.getAnnotation(WithDriver.class) != null);
    }

    public String specifiedDriver() {
        Preconditions.checkArgument(isDriverSpecified() == true);
        return (method.getAnnotation(WithDriver.class).value());
    }

}
