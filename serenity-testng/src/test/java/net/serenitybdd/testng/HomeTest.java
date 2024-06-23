package net.serenitybdd.testng;


import org.testng.Assert;
import org.testng.annotations.*;

@Listeners({SerenityTestNGExecutionListener.class})
public class HomeTest {

  public HomeTest(String name){}

  @BeforeTest
  //@Parameters({"url"})
  public void setup(/*String url*/) {
    System.out.println("Setup called " /*+ url*/);
  }

  @AfterTest
  public void afterTest() {

  }

  @Test
  public void verifyTitle () {
    Assert.assertEquals("QualityWorks Consulting Group", "QualityWorks Consulting Group", "Testing if title is QualityWorks Consulting Group");
  }

}
