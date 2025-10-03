package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Locale;



public class QAJobsPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(QAJobsPage.class);

    private static final String QA_URL = "https://useinsider.com/careers/quality-assurance/";

    private final By seeAllQAJobsBtn = By.xpath("//a[.//span[contains(.,'See all QA jobs')] or contains(.,'See all QA jobs')]");

    private final By departmentFilter = By.id("select2-filter-by-department-container");
    private final By locationFilter = By.id("select2-filter-by-location-container");
    private final By locationResults = By.id("select2-filter-by-location-results");
    private final By departmentResults = By.id("select2-filter-by-department-results");



    private final By jobCards = By.cssSelector("a[href*='jobs'][href*='lever'] , a[href*='lever.co'][target='_blank'], div[class*='position-list'] article, div[class*='jobs-list'] article");
    private final By positionInCard = By.xpath("//p[@class='position-title font-weight-bold']");
    private final By departmentInCard = By.xpath("//span[@class='position-department text-large font-weight-600 text-primary']");
    private final By locationInCard = By.xpath("//div[@class='position-location text-large'] ");
    private final By viewRoleButton = By.xpath("//a[normalize-space()='View Role']");

    public QAJobsPage(WebDriver driver) { super(driver); }


    public QAJobsPage openQAJobsLanding() {
        open(QA_URL);
        closeCookies();
        return this;
    }


    public QAJobsPage clickSeeAllQAJobs() {
        click(seeAllQAJobsBtn);
        waitSeconds(10);
        return this;
    }

    public QAJobsPage filterJobs(String location, String department) {
        try {
            click(departmentFilter);
            waitSeconds(3);
            WebElement opt = scrollDropdownUntilOption(
                    departmentResults,                  // panel
                    department,                         // aranan metin (örn. "Turkey")
                    120,                              // her adımda 300px aşağı
                    4,                               // en fazla 15 adım
                    200                               // her adım arası 200ms
            );
            waitSeconds(3);

            if (opt == null) {
                throw new AssertionError("Location option not found in dropdown: " + location);
            }
            opt.click();
            log.info("Location selected: {}", location);

        } catch (Exception e) {
            throw new AssertionError("Failed to select location via dropdown scroll: " + e.getMessage(), e);
        }

        try {
            click(locationFilter);
            waitSeconds(3);
            WebElement opt = scrollDropdownUntilOption(
                    locationResults,                  // panel
                    location,                         // aranan metin (örn. "Turkey")
                    120,                              // her adımda 300px aşağı
                    2,                               // en fazla 15 adım
                    200                               // her adım arası 200ms
            );
            waitSeconds(3);

            if (opt == null) {
                throw new AssertionError("Location option not found in dropdown: " + location);
            }
            opt.click();
            log.info("Location selected: {}", location);

        } catch (Exception e) {
            throw new AssertionError("Failed to select location via dropdown scroll: " + e.getMessage(), e);
        }
        return this;
    }


    public QAJobsPage verifyJobsListPresent() {
        List<WebElement> cards = $$(jobCards);
        Assert.assertTrue(cards.size() > 0, "Jobs list is empty after filtering");
        return this;
    }


    public QAJobsPage verifyEachJobContent(String requiredPosition, String requiredDepartment, String requiredLocation) {
        waitSeconds(3);
        List<WebElement> cards = $$(jobCards);
        for (WebElement c : cards) {
            String position = safeText(c, positionInCard);
            String department = safeText(c, departmentInCard);
            String location = safeText(c, locationInCard);

            String actual   = position == null ? "" : position.trim().toLowerCase(Locale.ROOT);
            String expected = requiredPosition.trim().toLowerCase(Locale.ROOT);


            Assert.assertTrue(
                    actual.contains(expected),
                    String.format("Position beklenen metni içermiyor. Beklenen: '%s' | Gelen: '%s'", requiredPosition, position)
            );
            Assert.assertTrue(department.toLowerCase().contains(requiredDepartment.toLowerCase()),
                    "Department mismatch: " + department);
            Assert.assertTrue(location.toLowerCase().contains(requiredLocation.toLowerCase()),
                    "Location mismatch: " + location);
        }
        return this;
    }


    private String safeText(WebElement root, By child) {
        try { return root.findElement(child).getText().trim(); } catch (Exception e) { return ""; }
    }


    public JobDetailPage openFirstJob() {
        List<WebElement> cards = $$(jobCards);
        Assert.assertTrue(cards.size() > 0, "No job cards to open");
        WebElement card = cards.get(0);
        hover(card);
        waitSeconds(1);
        driver.findElement(viewRoleButton).click();
        return new JobDetailPage(driver);
    }
    }
