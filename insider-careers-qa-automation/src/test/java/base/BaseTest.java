package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import utils.Driver;


public abstract class BaseTest {
    protected WebDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        driver = Driver.get();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        Driver.quit();
    }
}
