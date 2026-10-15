package com.taskbox;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskboxFrontendSeleniumTest {
    private WebDriver driver;

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void landingPageShowsTaskboxAndAuthenticationButtons() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox",
                "--disable-dev-shm-usage", "--disable-gpu", "--window-size=1365,900");
        driver = new ChromeDriver(options);

        Path page = Path.of("..", "frontend", "index.html").toAbsolutePath().normalize();
        driver.get(page.toUri().toString());

        assertTrue(driver.getPageSource().contains("TASKBOX"),
                "Landing page should contain the TASKBOX brand");
        assertTrue(driver.findElement(By.xpath("//button[contains(., 'Login')]")).isDisplayed(),
                "Login button should be visible");
        assertTrue(driver.findElement(By.xpath("//button[contains(., 'Sign Up')]")).isDisplayed(),
                "Sign Up button should be visible");
    }
}
