package com.student.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOdabirIOdjava {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "http://localhost:5173";

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:/selenium-profile");
        options.addArguments("profile-directory=Default");
        options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
        options.setExperimentalOption("useAutomationExtension", false);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void teardown() {
        if (driver != null) { driver.quit(); }
    }

    @Test
    @DisplayName("Test odjave korisnika - Uloga Radnik")
    void testOdjavaRadnik() {
        // 1. 
    	driver.get(baseUrl);
    	WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));

    	try {
    	    // Provjeri postoji li gumb za prijavu na stranici
    	    WebElement loginButton = shortWait.until(ExpectedConditions.presenceOfElementLocated(
    	            By.xpath("//button[contains(text(), 'Prijavi se s Auth2.0')]")));

    	    System.out.println("Pokrećem prijavu...");
    	    loginButton.click();

    	    wait.until(ExpectedConditions.urlContains("/radnik"));
    	    System.out.println("Prijava uspješna.");
    	    
    	} catch (TimeoutException e) {
    	    // već smo ulogirani
    	    System.out.println("Nastavljam kao već prijavljen korisnik.");
        	driver.get(baseUrl + "/radnik");
    	}

        // 2. ČEKANJE NA NESTANAK LOADING PORUKA
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//p[contains(text(), 'Učitavanje')]")));

        // 3. ODABIR TVRTKE
        try {
            // Provjeravamo postoji li select
            WebElement selectTvrtka = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("select")));
            
            // Odabir prve dostupne tvrtke
            selectTvrtka.click();
            WebElement opcija = driver.findElement(By.xpath("//select/option[2]"));
            opcija.click();
            
            System.out.println("Tvrtka odabrana.");
        } catch (TimeoutException e) {
            System.out.println("Dropdown nije prikazan, vjerojatno je tvrtka već odabrana u sessionu.");
        }

        // 4. klik odjava
        WebElement btnOdjava = wait.until(ExpectedConditions.elementToBeClickable(By.className("logout-button")));
        btnOdjava.click();

        // 5. provjera redirecta
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/"));
        assertEquals(baseUrl + "/", driver.getCurrentUrl(), "Redirect na početnu nije uspio.");
    }
}