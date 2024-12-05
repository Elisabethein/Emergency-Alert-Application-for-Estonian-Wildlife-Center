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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Selenium tests for user management
 * 1) Test editing a user and then reverting the changes
 * 2) Test deleting a user -> implemented in SeleniumApplicationManagementTests
 */
@SpringBootTest
public class SeleniumUserManagementTests {
    WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:8081/login");

        WebElement emailInput = driver.findElement(By.id("email"));
        emailInput.sendKeys("jane.doe@example.com");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("password123");

        WebElement loginButton = driver.findElement(By.className("login-button"));
        loginButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe("http://localhost:8081/my-cases"));

        WebElement userLink = driver.findElement(By.cssSelector("a[href='/user-management']"));
        userLink.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe("http://localhost:8081/user-management"));

        WebElement activeNavBarItem2 = driver.findElement(By.cssSelector("li.active > a.router-link-active.router-link-exact-active"));
        assertEquals("Olemasolevad kasutajad", activeNavBarItem2.getText(), "Active navbar item is not 'Olemasolevad kasutajad'");

    }

    @Test
    public void testEditUser() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".user-list")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Jane Doe')]")));
        List<WebElement> userListItems = driver.findElements(By.cssSelector(".user-list .user-card"));

        for (WebElement userListItem : userListItems) {
            WebElement userNameElement = userListItem.findElement(By.cssSelector(".user-name"));

            if (userNameElement.getText().equals("Jane Doe")) {

                WebElement muudaButton = userListItem.findElement(By.cssSelector(".edit-link a"));
                muudaButton.click();

                WebElement firstNameInput = driver.findElement(By.id("firstName"));
                firstNameInput.clear();
                firstNameInput.sendKeys("Jane_updated");

                WebElement lastNameInput = driver.findElement(By.id("lastName"));
                lastNameInput.clear();
                lastNameInput.sendKeys("Doe_updated");

                WebElement saveButton = driver.findElement(By.xpath("//button[text()='Salvesta kasutaja']"));
                saveButton.click();

                Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
                String alertMessage = successAlert.getText();
                successAlert.accept();

                assertEquals("Kasutaja Jane_updated Doe_updated on edukalt muudetud!", alertMessage);

                break;
            }
        }

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".user-list"))));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Jane_updated Doe_updated')]")));
        userListItems = driver.findElements(By.cssSelector(".user-list .user-card"));

        boolean newUserFound = false;
        for (WebElement userListItem : userListItems) {
            WebElement userNameElement = userListItem.findElement(By.cssSelector(".user-name"));

            if (userNameElement.getText().equals("Jane_updated Doe_updated")) {
                newUserFound = true;

                WebElement muudaButton = userListItem.findElement(By.cssSelector(".edit-link a"));
                muudaButton.click();

                WebElement firstNameInput = driver.findElement(By.id("firstName"));
                firstNameInput.clear();
                firstNameInput.sendKeys("Jane");

                WebElement lastNameInput = driver.findElement(By.id("lastName"));
                lastNameInput.clear();
                lastNameInput.sendKeys("Doe");

                WebElement saveButton = driver.findElement(By.xpath("//button[text()='Salvesta kasutaja']"));
                saveButton.click();

                Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
                String alertMessage = successAlert.getText();
                successAlert.accept();

                assertEquals("Kasutaja Jane Doe on edukalt muudetud!", alertMessage);

                break;
            }
        }
        assertTrue(newUserFound, "User 'Jane Doe' was not updated to 'Jane_updated Doe_updated'");
    }

    @AfterEach
    public void close() {
        driver.quit();
    }
}
