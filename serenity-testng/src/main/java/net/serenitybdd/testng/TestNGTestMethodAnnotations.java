package net.serenitybdd.testng;

import com.google.common.base.Preconditions;
import net.serenitybdd.annotations.DriverOptions;
import net.serenitybdd.annotations.WithDriver;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

public class TestNGTestMethodAnnotations
{

    private final Method method;

    private TestNGTestMethodAnnotations(final Method method) {
        this.method = method;
    }

    public static TestNGTestMethodAnnotations forTest(final Method method) {
        return new TestNGTestMethodAnnotations(method);
    }

    public boolean isDriverSpecified() {
        return (method.getAnnotation(WithDriver.class) != null);
    }

    public Optional<String> getDisplayName(){
        Test displayNameAnnotation = method.getAnnotation(Test.class);
        if( displayNameAnnotation != null && !Objects.isNull(displayNameAnnotation.testName()) && !displayNameAnnotation.testName().isEmpty()){
            return Optional.of(displayNameAnnotation.testName());
        }
        return Optional.empty();
    }

    public String specifiedDriver() {
        Preconditions.checkArgument(isDriverSpecified());
        return (method.getAnnotation(WithDriver.class).value());
    }

    public String driverOptions() {
        Preconditions.checkArgument(isDriverSpecified());
        return (method.getAnnotation(DriverOptions.class).value());
    }

}
