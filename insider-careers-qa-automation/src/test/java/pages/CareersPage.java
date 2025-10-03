package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public class CareersPage extends BasePage {


    private final By pageHero = By.cssSelector(".btn.btn-info.rounded.mr-0.mr-md-4.py-3");
    private final By teamsBlock = By.xpath("//section[@id='career-find-our-calling']//div[@class='row']");
    private final By locationsBlock = By.id("career-our-location");
    private final By lifeAtInsiderBlock = By.xpath("//div[@class='swiper-slide swiper-slide-active']//div[@role='img']");


    public CareersPage(WebDriver driver) { super(driver); }


    public CareersPage verifyCareerPage() {
        waitForUrlContains("careers");
        Assert.assertTrue(isVisible(pageHero), "Careers page hero not visible");
        return this;
    }


    public CareersPage verifyBlocks() {

        scrollToTop();

        // Teams
        stepScrollUntilPresent(teamsBlock, 600, 12, 200, 300);
        centerAndEnsureVisible(teamsBlock);
        Assert.assertTrue(isVisible(teamsBlock), "Teams block not visible");

        // Locations
        stepScrollUntilPresent(locationsBlock, 600, 12, 200, 300);
        centerAndEnsureVisible(locationsBlock);
        Assert.assertTrue(isVisible(locationsBlock), "Locations block not visible");

        // Life at Insider
        stepScrollUntilPresent(lifeAtInsiderBlock, 600, 12, 200, 300);
        centerAndEnsureVisible(lifeAtInsiderBlock);
        Assert.assertTrue(isVisible(lifeAtInsiderBlock), "Life at Insider block not visible");

        return this;
    }
}