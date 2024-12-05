package com.EmergencyAlertApplication.EAA.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SeleniumLoginTests {
    WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:8081/login");

        WebElement activeNavBarItem = driver.findElement(By.cssSelector("li.active > a.router-link-active"));
        assertEquals("Logi sisse", activeNavBarItem.getText(), "Active navbar item is not 'Logi sisse'");
    }

    @Test
    public void testLogin() {
        WebElement emailInput = driver.findElement(By.id("email"));
        emailInput.sendKeys("jane.doe@example.com");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("password123");

        WebElement loginButton = driver.findElement(By.className("login-button"));
        loginButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe("http://localhost:8081/my-cases"));

        assertEquals("http://localhost:8081/my-cases", driver.getCurrentUrl(), "Did not redirect to 'My Cases' page");
    }

    @Test
    public void testLoginInvalidCredentials() {
        WebElement emailInput = driver.findElement(By.id("email"));
        emailInput.sendKeys("jane.doe@example.com");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("password12345");

        WebElement loginButton = driver.findElement(By.className("login-button"));
        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();
        assertEquals("Kasutajanimi või salasõna on vale!", alert.getText(), "Alert message is not 'Kasutajanimi või salasõna on vale!'");

        alert.accept();
    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }
}
