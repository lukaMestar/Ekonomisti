package com.student.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TestGeneriranjeIzvjestaja {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "http://localhost:5173";

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:/selenium-profile");
        options.addArguments("profile-directory=Default");
        options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
        options.setExperimentalOption("useAutomationExtension", false);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testGenerirajIzvjestaj() {
        // 1. Navigacija
        driver.get(baseUrl + "/racunklijenti");
        if (driver.getCurrentUrl().contains("login") || driver.getCurrentUrl().contains("unauthorized")) {
            System.out.println("Redirectan na login/unauthorized, pokušavam ponovno...");
            driver.get(baseUrl + "/racunklijenti");
        }

        // 2. Čekamo da se učita stranica
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(), 'Računovodstveni izvještaj')]")));

        // 3. Odabir Klijenta
        WebElement klijentSelectElem = driver.findElement(By.xpath("//label[contains(text(), 'Klijent')]//select"));
        Select klijentSelect = new Select(klijentSelectElem);
        
        // Pokušaj odabrati Ivana, ako ne postoji, uzmi drugu opciju
        try {
            klijentSelect.selectByVisibleText("Ivan Ivic");
        } catch (Exception e) {
            klijentSelect.selectByIndex(1); // Prvi klijent u listi
        }

        // 4. Odabir mjeseca (kolovoz)
        WebElement mjesecSelectElem = driver.findElement(By.xpath("//label[contains(text(), 'Mjesec')]//select"));
        Select mjesecSelect = new Select(mjesecSelectElem);
        mjesecSelect.selectByValue("8");

        // 5. klik na gumb
        WebElement btnPrikazi = driver.findElement(By.xpath("//button[contains(text(), 'Prikaži izvještaj')]"));
        btnPrikazi.click();

        // 6. Provjera je li prebacio na novu rutu
        wait.until(ExpectedConditions.urlContains("/izvjestaj/"));
        
        String url = driver.getCurrentUrl();
        assertTrue(url.contains("/izvjestaj/"), "Nije navigirao na stranicu izvještaja!");
        System.out.println("Test uspješan. Trenutni URL: " + url);
    }
}