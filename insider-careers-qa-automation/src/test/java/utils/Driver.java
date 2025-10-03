package utils;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class Driver {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();


    public static WebDriver get() {
        if (driver.get() == null) {
            String headless = System.getProperty("headless", "false");
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if ("true".equalsIgnoreCase(headless)) {
                options.addArguments("--headless=new");
            }
            options.addArguments("--start-maximized");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            driver.set(new ChromeDriver(options));
        }
        return driver.get();
    }


    public static void quit() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}