package com.EmergencyAlertApplication.EAA.selenium;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

/**
 * Selenium tests for the application submission functionality
 * 1) Submitting an alert
 * 2) Checking the alert on the ticket management page
 * 3) Viewing the alert on the ticket management page
 * 4) Deleting the alert on the ticket management page
 * 5) Editing the alert on the ticket management page
 *      5.1) add species, upperspecies, region and volunteer -> status: Hoolekodus
 *              5.1.1) add history in my-cases page
 *              5.1.2) change status to resolved and add resolution
 *      5.2) add species, upperspecies, region -> status: Avatud
 *              5.2.1) assign the ticket via assign-button to the current volunteer
 */

@SpringBootTest
@Transactional
public class SeleniumTicketManagementTests {

    WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:8081/alert");

        WebElement activeNavBarItem = driver.findElement(By.cssSelector("li.active > a.router-link-active"));
        assertEquals("Teavita juhtunust", activeNavBarItem.getText(), "Active navbar item is not 'Teavita juhtunust'");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".volunteer-form")));

        // Fill out the alert form
        driver.findElement(By.id("reason")).sendKeys("Animal trapped in a fence");
        driver.findElement(By.id("animals")).sendKeys("Deer");
        // Leia ja vajuta nupule "Sisesta asukoht käsitsi"
        WebElement manualInputButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Sisesta asukoht käsitsi')]")));
        manualInputButton.click();
        WebElement locationInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("location")));
        String testLocation = "Tallinn-Tartu road 100km";
        locationInput.sendKeys(testLocation);
        assertEquals(testLocation, locationInput.getAttribute("value"), "Käsitsi sisestatud asukoha väärtus ei ühti");

        driver.findElement(By.id("directions")).sendKeys("Near the forest entrance");

        driver.findElement(By.id("name")).sendKeys("John");
        driver.findElement(By.id("lastName")).sendKeys("Forest");
        driver.findElement(By.id("phone")).sendKeys("1234567890");
        driver.findElement(By.id("email")).sendKeys("john.forest@example.com");

        // Toggle 'Can Wait' option
        driver.findElement(By.xpath("//button[contains(text(),'Jah')]")).click();

        // Submit the form
        WebElement submitButton = driver.findElement(By.className("submit-button"));
        submitButton.click();

        // Wait for the modal to appear
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal")));
        WebElement modalHeader = modal.findElement(By.tagName("h2"));
        WebElement modalMessage = modal.findElement(By.tagName("p"));

        // Verify modal content
        assertEquals("Teavitus saadetud!", modalHeader.getText());
        assertEquals("Teie teavitus on edukalt saadetud.", modalMessage.getText(), "Modal message does not match");

        // Navigate to the login page and log in
        driver.get("http://localhost:8081/login");
        driver.findElement(By.id("email")).sendKeys("jane.doe@example.com");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.className("login-button")).click();

        // Ensure login is successful
        wait.until(ExpectedConditions.urlToBe("http://localhost:8081/my-cases"));

        WebElement userLink = driver.findElement(By.cssSelector("a[href='/ticket-management']"));
        userLink.click();

        wait.until(ExpectedConditions.urlToBe("http://localhost:8081/ticket-management"));

        WebElement activeNavBarItem2 = driver.findElement(By.cssSelector("li.active > a.router-link-active.router-link-exact-active"));
        assertEquals("Juhtumid", activeNavBarItem2.getText(), "Active navbar item is not 'Juhtumid'");
    }

    @Test
    @Transactional
    public void testViewTicket() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Step 1: Navigate to the Ticket Management page
        driver.get("http://localhost:8081/ticket-management");

        // Step 2: Wait for the page to load and verify we are on the correct page
        wait.until(ExpectedConditions.urlToBe("http://localhost:8081/ticket-management"));
        WebElement activeNavBarItem = driver.findElement(By.cssSelector("li.active > a.router-link-active.router-link-exact-active"));
        assertEquals("Juhtumid", activeNavBarItem.getText(), "Active navbar item is not 'Juhtumid'");

        // Step 3: Wait for the ticket list to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ticket-list")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), '#') and contains(text(), 'Tuvastamata')]")));

        List<WebElement> ticketListItems = driver.findElements(By.cssSelector(".ticket-list .ticket-card"));

        boolean ticketFound = false;
        for (WebElement ticketListItem : ticketListItems) {
            WebElement ticketNameElement = ticketListItem.findElement(By.cssSelector(".ticket-animal .animal-name"));

            if (ticketNameElement.getText().matches("#\\d+ - Tuvastamata")) {
                ticketFound = true;

                // Step 4: Click on the 'vaata' (view) button to go to ticket details
                WebElement viewButton = ticketListItem.findElement(By.cssSelector(".edit-link .view-button"));
                viewButton.click();

                WebElement ticketSpecies = driver.findElement(By.cssSelector(".view-ticket-popup .form-group span"));
                assertNotNull("Ticket species should be visible", ticketSpecies);

                WebElement ticketLocation = driver.findElement(By.cssSelector(".view-ticket-popup .form-group span"));
                assertNotNull("Ticket location should be visible", ticketLocation);

                WebElement ticketStatus = driver.findElement(By.cssSelector(".view-ticket-popup .form-group span"));
                assertNotNull("Ticket status should be visible", ticketStatus);

                WebElement volunteersList = driver.findElement(By.cssSelector(".view-ticket-popup .volunteer-information"));
                assertNotNull("Volunteers list should be visible", volunteersList);

                WebElement closeButton = driver.findElement(By.cssSelector(".view-ticket-popup .close-button"));
                closeButton.click();
            }
        }
    }

    @Test
    @Transactional
    public void testDeleteTicket() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Step 1: Navigate to the Ticket Management page
        driver.get("http://localhost:8081/ticket-management");

        // Step 2: Wait for the page to load and verify we are on the correct page
        wait.until(ExpectedConditions.urlToBe("http://localhost:8081/ticket-management"));
        WebElement activeNavBarItem = driver.findElement(By.cssSelector("li.active > a.router-link-active.router-link-exact-active"));
        assertEquals("Juhtumid", activeNavBarItem.getText(), "Active navbar item is not 'Juhtumid'");

        // Step 3: Wait for the ticket list to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ticket-list")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), '#') and contains(text(), 'Tuvastamata')]")));

        List<WebElement> ticketListItems = driver.findElements(By.cssSelector(".ticket-list .ticket-card"));

        boolean ticketFound = false;
        for (WebElement ticketListItem : ticketListItems) {
            WebElement ticketNameElement = ticketListItem.findElement(By.cssSelector(".ticket-animal .animal-name"));

            if (ticketNameElement.getText().matches("#\\d+ - Tuvastamata")) {
                ticketFound = true;

                // Step 4: Click on the 'Muuda' (Edit) button to go to ticket details
                WebElement editButton = ticketListItem.findElement(By.cssSelector(".edit-link .edit-button"));
                editButton.click();

                // Step 5: Wait for the ticket details page/modal to load
                WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".delete-button")));
                deleteButton.click();

                // Step 6: Confirm the deletion in the alert
                Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());
                confirmAlert.accept();

                // Optionally, handle success alert after ticket is deleted
                Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
                String alertMessage = successAlert.getText();
                successAlert.accept();

                // Step 7: Assert that the deletion success message is correct
                assertEquals("Teade on edukalt kustutatud!", alertMessage);

                break;
            }
        }

        // Step 8: Assert that the ticket was found and deleted
        assertTrue(ticketFound, "Test Ticket was not found in the ticket list");
    }

    @Test
    @Transactional
    public void testEditTicketAddVolunteer() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Step 1: Navigate to the Ticket Management page
        driver.get("http://localhost:8081/ticket-management");

        // Step 2: Wait for the page to load and verify we are on the correct page
        wait.until(ExpectedConditions.urlToBe("http://localhost:8081/ticket-management"));
        WebElement activeNavBarItem = driver.findElement(By.cssSelector("li.active > a.router-link-active.router-link-exact-active"));
        assertEquals("Juhtumid", activeNavBarItem.getText(), "Active navbar item is not 'Juhtumid'");

        // Step 3: Wait for the ticket list to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ticket-list")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), '#') and contains(text(), 'Tuvastamata')]")));

        List<WebElement> ticketListItems = driver.findElements(By.cssSelector(".ticket-list .ticket-card"));

        boolean ticketFound = false;
        for (WebElement ticketListItem : ticketListItems) {
            WebElement ticketNameElement = ticketListItem.findElement(By.cssSelector(".ticket-animal .animal-name"));

            if (ticketNameElement.getText().matches("#\\d+ - Tuvastamata")){
                ticketFound = true;

                // Step 4: Click on the 'Muuda' (Edit) button to go to ticket details
                WebElement editButton = ticketListItem.findElement(By.cssSelector(".edit-link .edit-button"));
                editButton.click();

                // Step 5: Wait for the Edit Ticket Popup to appear
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".edit-ticket-popup")));

                // Wait for the "Liigigrupp" dropdown to be clickable
                WebElement TypeSelect = wait.until(ExpectedConditions.elementToBeClickable(By.id("animalType")));
                Select typeDropdown = new Select(TypeSelect);
                typeDropdown.selectByVisibleText("Suuruluk");

                // Wait for the "Looma liik" dropdown to be clickable
                WebElement nameSelect = wait.until(ExpectedConditions.elementToBeClickable(By.id("animalName")));
                Select nameDropdown = new Select(nameSelect);
                wait.until(ExpectedConditions.textToBePresentInElement(nameSelect, "Punahirv"));
                nameDropdown.selectByVisibleText("Punahirv");

                // Wait for the Region dropdown to be clickable
                WebElement regionSelect = wait.until(ExpectedConditions.elementToBeClickable(By.id("county")));
                Select regionDropdown = new Select(regionSelect);
                regionDropdown.selectByVisibleText("Harjumaa");

                // Step 9: Fill in Volunteer information (Jane Doe)
                WebElement volunteerDropdown = driver.findElement(By.cssSelector("div.volunteer-information .multiselect"));
                volunteerDropdown.click();  // Trigger click to open the dropdown
                // Wait for the options to appear
                List<WebElement> volunteerOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//div[contains(@class, 'multiselect__content')]//span[contains(text(), 'Jane Doe')]")));

                boolean found = false;
                for (WebElement option : volunteerOptions) {
                    // Check if the option's text is exactly "Jane Doe"
                    if (option.getText().trim().equals("Jane Doe")) {
                        option.click();  // Select the option
                        found = true;
                        break;
                    }
                }

                // Step 10: Save the changes
                WebElement saveButton = driver.findElement(By.xpath("//button[text()='Salvesta']"));
                saveButton.click();

                Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());
                confirmAlert.accept();
            }
        }
        // mark ticket as resolved
        // Navigate to "My Cases" page
        driver.get("http://localhost:8081/my-cases");
        wait.until(ExpectedConditions.urlToBe("http://localhost:8081/my-cases"));

        // Locate the specific ticket in "My Cases"
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".ticket-list .ticket-card")));
        List<WebElement> myCasesTickets = driver.findElements(By.cssSelector(".ticket-list .ticket-card"));
        System.out.println("cases size: " + myCasesTickets.size());
        for (WebElement ticket : myCasesTickets) {
            WebElement ticketNameElement = ticket.findElement(By.cssSelector(".ticket-info .ticket-animal .animal-name"));
            System.out.println("Found ticket name: " + ticketNameElement.getText());

            if (ticketNameElement.getText().matches("#\\d+ - Punahirv")) {
                WebElement editButton = ticket.findElement(By.cssSelector(".edit-link .edit-button"));
                editButton.click();

                // Wait for the Edit Ticket Popup
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".edit-ticket-popup")));

                // Update history
                WebElement historyTextarea = driver.findElement(By.cssSelector(".history textarea"));
                historyTextarea.clear();
                historyTextarea.sendKeys("Loom vabastati metsa tagasi.");

                // Update resolution to "Vabastatud"
                WebElement resolutionDropdown = driver.findElement(By.id("resolution"));
                Select resolutionSelect = new Select(resolutionDropdown);
                resolutionSelect.selectByVisibleText("Vabastatud");

                // Update status to "Lõpetatud"
                WebElement statusDropdown = driver.findElement(By.id("status"));
                Select statusSelect = new Select(statusDropdown);
                statusSelect.selectByVisibleText("Lõpetatud");

                // Save the changes
                WebElement saveButton = driver.findElement(By.xpath("//button[text()='Salvesta']"));
                saveButton.click();

                // Confirm the alert
                Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());
                confirmAlert.accept();

                break;
            }
        }
    }

    @Test
    @Transactional
    public void testEditTicketWithoutVolunteer() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Step 1: Navigate to the Ticket Management page
        driver.get("http://localhost:8081/ticket-management");

        // Step 2: Wait for the page to load and verify we are on the correct page
        wait.until(ExpectedConditions.urlToBe("http://localhost:8081/ticket-management"));
        WebElement activeNavBarItem = driver.findElement(By.cssSelector("li.active > a.router-link-active.router-link-exact-active"));
        assertEquals("Juhtumid", activeNavBarItem.getText(), "Active navbar item is not 'Juhtumid'");

        // Step 3: Wait for the ticket list to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ticket-list")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), '#') and contains(text(), 'Tuvastamata')]")));

        List<WebElement> ticketListItems = driver.findElements(By.cssSelector(".ticket-list .ticket-card"));

        boolean ticketFound = false;
        for (WebElement ticketListItem : ticketListItems) {
            WebElement ticketNameElement = ticketListItem.findElement(By.cssSelector(".ticket-animal .animal-name"));

            if (ticketNameElement.getText().matches("#\\d+ - Tuvastamata")){
                ticketFound = true;

                // Step 4: Click on the 'Muuda' (Edit) button to go to ticket details
                WebElement editButton = ticketListItem.findElement(By.cssSelector(".edit-link .edit-button"));
                editButton.click();

                // Step 5: Wait for the Edit Ticket Popup to appear
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".edit-ticket-popup")));

                // Wait for the "Liigigrupp" dropdown to be clickable
                WebElement TypeSelect = wait.until(ExpectedConditions.elementToBeClickable(By.id("animalType")));
                Select typeDropdown = new Select(TypeSelect);
                typeDropdown.selectByVisibleText("Suuruluk");

                // Wait for the "Looma liik" dropdown to be clickable
                WebElement nameSelect = wait.until(ExpectedConditions.elementToBeClickable(By.id("animalName")));
                Select nameDropdown = new Select(nameSelect);
                wait.until(ExpectedConditions.textToBePresentInElement(nameSelect, "Punahirv"));
                nameDropdown.selectByVisibleText("Punahirv");

                // Wait for the Region dropdown to be clickable
                WebElement regionSelect = wait.until(ExpectedConditions.elementToBeClickable(By.id("county")));
                Select regionDropdown = new Select(regionSelect);
                regionDropdown.selectByVisibleText("Harjumaa");

                // Step 10: Save the changes
                WebElement saveButton = driver.findElement(By.xpath("//button[text()='Salvesta']"));
                saveButton.click();

                Alert confirmAlert = wait.until(ExpectedConditions.alertIsPresent());
                confirmAlert.accept();

                // Step 11: Assign the ticket to yourself
                WebElement assignButton = ticketListItem.findElement(By.cssSelector(".assign-button"));
                assignButton.click();

                // Step 12: Confirm the assiging
                confirmAlert.accept();
                Alert successAlert = wait.until(ExpectedConditions.alertIsPresent());
                String alertMessage = successAlert.getText();
                successAlert.accept();
            }
        }
    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }
}
