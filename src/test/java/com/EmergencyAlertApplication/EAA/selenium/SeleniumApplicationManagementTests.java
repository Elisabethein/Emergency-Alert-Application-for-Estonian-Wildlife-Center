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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Selenium tests for the application submission functionality
 * 1) Submitting an application
 * 2) Checking the application on the applications management page
 * 3) Deleting the application on the application management page
 * 4) Accepting the application on the application management page
 * 5) Checking the user on the user management page
 * 6) Deleting the user on the user management page
 */
@SpringBootTest
public class SeleniumApplicationManagementTests {

    WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:8081/register");

        WebElement activeNavBarItem = driver.findElement(By.cssSelector("li.active > a.router-link-active"));
        assertEquals("Registreeru", activeNavBarItem.getText(), "Active navbar item is not 'Registreeru'");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".volunteer-form")));


        // Fill out the application form
        WebElement firstNameInput = driver.findElement(By.id("name"));
        firstNameInput.sendKeys("Test");

        WebElement lastNameInput = driver.findElement(By.id("lastName"));
        lastNameInput.sendKeys("User");

        WebElement phoneNumberInput = driver.findElement(By.id("phone"));
        phoneNumberInput.sendKeys("123");

        WebElement emailInput = driver.findElement(By.id("email"));
        emailInput.sendKeys("test@gmailtest.com");

        WebElement dateOfBirthInput = driver.findElement(By.id("dob"));
        dateOfBirthInput.sendKeys("12.12.2002");

        WebElement streetNameInput = driver.findElement(By.id("street"));
        streetNameInput.sendKeys("test");

        WebElement streetNumberInput = driver.findElement(By.id("streetNr"));
        streetNumberInput.sendKeys("123");

        WebElement cityInput = driver.findElement(By.id("city"));
        cityInput.sendKeys("test");

        WebElement regionSelect = driver.findElement(By.id("region"));
        wait.until(ExpectedConditions.elementToBeClickable(regionSelect));
        Select regionDropdown = new Select(regionSelect);
        regionDropdown.selectByVisibleText("Harjumaa");

        WebElement postalCodeInput = driver.findElement(By.id("postalCode"));
        postalCodeInput.sendKeys("123");

        WebElement reasonInput = driver.findElement(By.id("reason"));
        reasonInput.sendKeys("test");

        List<WebElement> checkboxes = driver.findElements(By.cssSelector(".checkbox-container"));
        for (WebElement checkbox : checkboxes) {
            String checkboxText = checkbox.findElement(By.tagName("label")).getText();
            if (checkboxText.equals("Hoiukodu") || checkboxText.equals("Transport")) {
                WebElement inputCheckbox = checkbox.findElement(By.tagName("input"));

                if (!inputCheckbox.isSelected()) {
                    inputCheckbox.click();
                }
            }
        }

        WebElement experienceInput = driver.findElement(By.id("experience"));
        experienceInput.sendKeys("test");

        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("test");

        WebElement shareDataCheckbox = driver.findElement(By.id("shareData"));
        if (!shareDataCheckbox.isSelected()) {
            shareDataCheckbox.click();
        }

        WebElement allowVisitsCheckbox = driver.findElement(By.id("allowControlVisits"));
        if (!allowVisitsCheckbox.isSelected()) {
            allowVisitsCheckbox.click();
        }

        // Submit the form
        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();


        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal")));

        // Check the modal content
        WebElement modalHeader = modal.findElement(By.tagName("h2"));
        WebElement modalMessage = modal.findElement(By.tagName("p"));

        assertEquals("Taotlus saadetud!", modalHeader.getText(), "Modal header does not match");
        assertEquals("Teie taotlus on edukalt saadetud. Täname, et soovite Eesti Metsloomaühingu vabatahtlikuks tulla!",
                modalMessage.getText(), "Modal message does not match");

        // Log in as admin
        driver.get("http://localhost:8081/login");

        WebElement loginEmailInput = driver.findElement(By.id("email"));
        loginEmailInput.sendKeys("jane.doe@example.com");

        WebElement loginPasswordInput = driver.findElement(By.id("password"));
        loginPasswordInput.sendKeys("password123");

        WebElement loginButton = driver.findElement(By.className("login-button"));
        loginButton.click();

        // Navigate to the application management page
        wait.until(ExpectedConditions.urlToBe("http://localhost:8081/my-cases"));

        WebElement userLink = driver.findElement(By.cssSelector("a[href='/application-management']"));
        userLink.click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:8081/application-management"));

        WebElement activeNavBarItem2 = driver.findElement(By.cssSelector("li.active > a.router-link-active.router-link-exact-active"));
        assertEquals("Uued kasutajad", activeNavBarItem2.getText(), "Active navbar item is not 'Uued kasutajad'");
    }

    /**
     * Test application submit functionality by filling out the form and checking its submission on the applications page
     * and then deleting the application on the application management page
     */
    @Test
    public void testApplicationSubmitWithApplicationDelete() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".user-list-container"))));
        WebElement userListContainer = driver.findElement(By.cssSelector(".user-list-container"));
        List<WebElement> userListItems = userListContainer.findElements(By.cssSelector(".user-list"));

        // Check if the application is in the list and delete it
        boolean userFound = false;
        for (WebElement userListItem : userListItems) {
            WebElement userNameElement = userListItem.findElement(By.cssSelector(".user-info"));
            if (userNameElement.getText().equals("Test User")) {
                userFound = true;

                WebElement deleteButton = userListItem.findElement(By.cssSelector(".delete-button"));
                deleteButton.click();

                Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());
                confirmAlert.accept();

                Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
                String alertMessage = successAlert.getText();
                successAlert.accept();

                assertEquals("Test User on kustutatud", alertMessage);

                break;
            }
        }
        assertTrue(userFound, "Test User was not found in the user list");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".user-list-container")));
        WebElement updatedUserListContainer = driver.findElement(By.cssSelector(".user-list-container"));
        List<WebElement> updatedUserListItems = updatedUserListContainer.findElements(By.cssSelector(".user-list"));
        boolean isTestUserDeleted = true;
        for (WebElement updatedUser : updatedUserListItems) {
            WebElement updatedUserName = updatedUser.findElement(By.cssSelector(".user-info"));
            if (updatedUserName.getText().equals("Test User")) {
                isTestUserDeleted = false;
                break;
            }
        }
        assertTrue(isTestUserDeleted, "Test User was not deleted successfully");
    }

    @Test
    public void testApplicationSubmitWithUserDelete() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".user-list-container")));

        WebElement userListContainer = driver.findElement(By.cssSelector(".user-list-container"));
        List<WebElement> userListItems = userListContainer.findElements(By.cssSelector(".user-list"));

        // Check if the application is in the list and accept it
        boolean userFound = false;
        for (WebElement userListItem : userListItems) {
            WebElement userNameElement = userListItem.findElement(By.cssSelector(".user-info"));
            if (userNameElement.getText().equals("Test User")) {
                userFound = true;

                WebElement addButton = userListItem.findElement(By.cssSelector(".add-button"));
                addButton.click();

                Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());
                confirmAlert.accept();

                Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
                String alertMessage = successAlert.getText();
                successAlert.accept();

                assertEquals("Test User on lisatud", alertMessage);

                break;
            }
        }
        assertTrue(userFound, "Test User was not found in the user list");

        // Navigate to the user management page
        driver.get("http://localhost:8081/user-management");

        wait.until(ExpectedConditions.urlToBe("http://localhost:8081/user-management"));
        WebElement activeNavBarItem2 = driver.findElement(By.cssSelector("li.active > a.router-link-active.router-link-exact-active"));
        assertEquals("Olemasolevad kasutajad", activeNavBarItem2.getText(), "Active navbar item is not 'Olemasolevad kasutajad'");


        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".user-list"))));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Test User')]")));

        List<WebElement> userListItems2 = driver.findElements(By.cssSelector(".user-list .user-card"));

        boolean userFound2 = false;
        for (WebElement userListItem : userListItems2) {
            WebElement userNameElement = userListItem.findElement(By.cssSelector(".user-name"));

            if (userNameElement.getText().equals("Test User")) {
                userFound2 = true;

                WebElement muudaButton = userListItem.findElement(By.cssSelector(".edit-link a"));
                muudaButton.click();

                WebElement button = driver.findElement(By.cssSelector(".delete-button"));
                button.click();

                Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());
                confirmAlert.accept();

                // Optionally, handle success alert after user is deleted
                Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
                String alertMessage = successAlert.getText();
                successAlert.accept();

                assertEquals("Kasutaja Test User on edukalt kustutatud!", alertMessage); // Assert that the user was deleted

                break;
            }
        }
        assertTrue(userFound2, "Test User was not found in the user list");
    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }
}
