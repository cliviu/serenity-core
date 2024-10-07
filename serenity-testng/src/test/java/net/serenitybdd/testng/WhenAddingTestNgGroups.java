package net.serenitybdd.testng;


import net.thucydides.model.domain.TestTag;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.testng.Assert.assertTrue;

public class WhenAddingTestNgGroups {

    public class ATestCaseWithTagsOnTheMethods {
        @Test(groups={ "methodleveltag"})
        public void some_test_method() {
        }

        @Test(groups={"methodleveltag1", "methodleveltag2"})
        public void some_test_method_with_many_tags() {
        }
    }

    @Test
    public void singleTag() throws NoSuchMethodException {
        Method testMethod = ATestCaseWithTagsOnTheMethods.class.getMethod("some_test_method");

        assertTrue(TestNGTags.forMethod(testMethod).contains(TestTag.withValue("methodleveltag")));
    }

    @Test
    public void multipleTag() throws NoSuchMethodException {
        Method testMethod = ATestCaseWithTagsOnTheMethods.class.getMethod("some_test_method_with_many_tags");

        assertTrue(TestNGTags.forMethod(testMethod).contains(TestTag.withValue("methodleveltag1")));
        assertTrue(TestNGTags.forMethod(testMethod).contains(TestTag.withValue("methodleveltag2")));
    }
}
