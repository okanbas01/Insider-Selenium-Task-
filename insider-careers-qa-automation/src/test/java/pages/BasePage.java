package pages;


import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;



public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);
    private static final Duration DEFAULT_POLLING = Duration.ofMillis(250);

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    protected WebElement $(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> $$(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return driver.findElements(locator);
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void jsClick(By locator) {
        WebElement el = $(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    protected void hover(By locator) {
        new Actions(driver).moveToElement($(locator)).perform();
    }

    protected void open(String url) {
        driver.get(url);
    }

    protected boolean isVisible(By locator) {
        try {
            return $(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void waitForUrlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    protected void scrollByPx(int px) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, arguments[0])", px);
    }

    protected long getScrollY() {
        Object y = ((JavascriptExecutor) driver).executeScript(
                "return window.scrollY || window.pageYOffset || 0;");
        return ((Number) y).longValue();
    }

    protected boolean isInViewport(WebElement el) {
        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "const r=arguments[0].getBoundingClientRect();" +
                        "return r.top>=0 && r.bottom <= (window.innerHeight || document.documentElement.clientHeight);", el);
    }

    protected void stepScrollUntilPresent(By locator, int stepPx, int maxSteps, int pauseMs, int minDeltaPx) {
        long startY = getScrollY();
        for (int i = 0; i < maxSteps; i++) {
            if (isPresent(locator)) {
                WebElement el = driver.findElement(locator);
                if (isInViewport(el)) break;
            }
            scrollByPx(stepPx);
            try { Thread.sleep(pauseMs); } catch (InterruptedException ignored) {}
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        long endY = getScrollY();
        System.out.println("[STEP-SCROLL] " + locator + " scrollY: " + startY + " → " + endY);
        org.testng.Assert.assertTrue(Math.abs(endY - startY) >= minDeltaPx,
                "[STEP-SCROLL] Page did not scroll enough for " + locator);
    }

    protected WebElement centerAndEnsureVisible(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'})", el);
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -120)"); // sticky header payı
        wait.until(ExpectedConditions.visibilityOf(el));
        org.testng.Assert.assertTrue(isInViewport(el), "Element not in viewport: " + locator);
        return el;
    }

    protected void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
    }

    protected <T> T waitUntil(Function<WebDriver, T> condition, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .pollingEvery(DEFAULT_POLLING)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(condition);
    }

    protected void waitSeconds(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected WebElement waitPresent(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected void waitMillis(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    protected void scrollElementBy(WebElement container, int deltaY) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollTop = arguments[0].scrollTop + arguments[1];",
                container, deltaY);
    }

    protected WebElement scrollDropdownUntilOption(
            By panelLocator, String optionText, int stepPx, int maxSteps, long pauseMs) {

        WebElement panel = waitPresent(panelLocator);
        By optionRel = By.xpath(".//*[self::li or self::div or self::span or self::a]" +
                "[contains(normalize-space(.), '"+ optionText +"')]");

        for (int i = 0; i < maxSteps; i++) {
            List<WebElement> hits = panel.findElements(optionRel);
            if (!hits.isEmpty() && hits.get(0).isDisplayed()) {
                return hits.get(0);                // bulundu
            }
            scrollElementBy(panel, stepPx);        // paneli aşağı kaydır
            waitMillis(pauseMs);
        }
        return null; // bulunamadı
    }

    protected void hover(WebElement el) {
        new Actions(driver)
                .moveToElement(el)
                .pause(Duration.ofMillis(150))
                .perform();
    }

    protected void closeCookies() {
        final By reject = By.id("wt-cli-accept-all-btn");
        click(reject);
        waitSeconds(1);
    }
}