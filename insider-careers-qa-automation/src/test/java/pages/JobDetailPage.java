package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;



public class JobDetailPage extends BasePage {

    private final By leverForm = By.cssSelector("#apply, form[action*='lever'], .posting-apply, .application-form");

    private final By formLocation   = By.xpath("//div[contains(@class,'posting-categories')]//*[contains(@class,'location')]");
    private final By formDepartment = By.xpath("//div[contains(@class,'posting-categories')]//*[contains(@class,'department')]");

    private final By roleTitle = By.cssSelector(
            ".posting-headline, .posting-title, .posting-header h1, .posting-header h2, h1.posting-title, h2.posting-title"
    );

    private final By applyButton = By.xpath(
            "//div[contains(@class,'postings-btn-wrapper')]//a[contains(@class,'postings-btn') and (contains(.,'Apply') or @href='#application')]"
                    + " | //a[contains(@class,'apply') or contains(.,'Apply')]"
                    + " | //button[contains(.,'Apply')]"
    );


    public JobDetailPage(WebDriver driver) { super(driver); }


    public JobDetailPage verifyLeverPage(String expectedLocation, String expectedDepartment) {
        String current = driver.getWindowHandle();
        for (String h : driver.getWindowHandles()) {
            if (!h.equals(current)) {
                driver.switchTo().window(h);
                break;
            }
        }

        waitUntil(
                ExpectedConditions.or(
                        ExpectedConditions.urlContains("lever.co"),
                        ExpectedConditions.presenceOfElementLocated(leverForm)
                ),
                Duration.ofSeconds(15)
        );


        java.util.function.UnaryOperator<String> norm = s -> {
            if (s == null) return "";
            String t = s.replace('\u00A0', ' ');
            t = t.replaceAll("\\s*[/|·•,:;\\-]+\\s*$", "");
            t = t.replaceAll("\\s+", " ").trim();
            return t.toLowerCase(java.util.Locale.ROOT);
        };

        String actualLocation = "";
        try { actualLocation = $(formLocation).getText().trim(); } catch (Exception ignored) {}
        String normActualLoc = norm.apply(actualLocation);
        String normExpectedLoc = norm.apply(expectedLocation);

        org.testng.Assert.assertFalse(normActualLoc.isEmpty(), "Location text not found on page");
        org.testng.Assert.assertEquals(
                normActualLoc, normExpectedLoc,
                "Location mismatch. Actual: '" + actualLocation + "' | Expected: '" + expectedLocation + "'"
        );

        String actualDeptOrRole = "";
        try { actualDeptOrRole = $(formDepartment).getText().trim(); } catch (Exception ignored) {}
        if (actualDeptOrRole.isEmpty()) {
            try { actualDeptOrRole = $(roleTitle).getText().trim(); } catch (Exception ignored) {}
        }
        String normActualDept = norm.apply(actualDeptOrRole);
        String normExpectedDept = norm.apply(expectedDepartment);

        org.testng.Assert.assertFalse(normActualDept.isEmpty(), "Department/Role text not found on page");
        org.testng.Assert.assertEquals(
                normActualDept, normExpectedDept,
                "Department/Role mismatch. Actual: '" + actualDeptOrRole + "' | Expected: '" + expectedDepartment + "'"
        );

        WebElement apply = $(applyButton);
        org.testng.Assert.assertTrue(apply.isDisplayed(), "Apply button is not visible");

        return this;
}
}