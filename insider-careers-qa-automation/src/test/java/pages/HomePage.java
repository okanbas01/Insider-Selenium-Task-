package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public class HomePage extends BasePage {
    private static final String URL = "https://useinsider.com/";


    private final By logo = By.cssSelector("img[alt='insider_logo']");
    private final By companyMenu = By.xpath("//nav//a[contains(translate(., 'COMPANY', 'company'),'company') or contains(@href,'company')]");
    private final By careersLink = By.xpath("//nav//a[contains(translate(., 'CAREERS','careers'),'careers') or contains(@href,'careers')]");


    public HomePage(WebDriver driver) { super(driver); }


    public HomePage open() {
        open(URL);
        closeCookies();
        return this;
    }

    public HomePage verifyHomePage() {
        Assert.assertTrue(isVisible(logo), "Home page logo is not visible");
        return this;
    }

    public CareersPage navigateToCareersViaMenu() {
        try { hover(companyMenu); } catch (Exception ignored) {}
        click(careersLink);
        return new CareersPage(driver);
    }
}