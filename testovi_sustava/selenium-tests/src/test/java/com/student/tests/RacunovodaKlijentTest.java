package com.student.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RacunovodaKlijentTest {

	private WebDriver driver;
	private WebDriverWait wait;
	private final String baseUrl = "http://localhost:5173";

	@BeforeEach
	void setup() {
		ChromeOptions options = new ChromeOptions();

		// Putanja do profila - bitno je da putanja bude precizna
		options.addArguments("user-data-dir=C:/selenium-profile");
		options.addArguments("profile-directory=Default");

		options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
		options.setExperimentalOption("useAutomationExtension", false);

		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@AfterEach
	void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	@DisplayName("Test dodavanja klijenta s nula vrijednostima")
	void testDodajKlijentaSaNulaVrijednostima() {
		// 1. Idi na stranicu s klijentima
		driver.get(baseUrl + "/racunklijenti");

		driver.get(baseUrl + "/racunklijenti");
		System.out.println("Trenutni URL: " + driver.getCurrentUrl());

		// 2. Klikni na gumb "Dodaj klijenta"
		WebElement btnDodaj = wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Dodaj klijenta')]")));
		btnDodaj.click();

		// 3. Provjeri jesmo li na pravoj ruti
		wait.until(ExpectedConditions.urlContains("/dodajklijenta"));

		// 4. Popuni obavezna polja
		driver.findElement(By.name("ime")).sendKeys("Test");
		driver.findElement(By.name("prezime")).sendKeys("Klijent");
		driver.findElement(By.name("email")).sendKeys("test.klijent@example.com");
		driver.findElement(By.name("nazivFirme")).sendKeys("Test Firma d.o.o.");

		// 5. Unesi 0 u polja za iznose (prema tvom kodu, ovo bi trebalo raditi)
		driver.findElement(By.name("pocetnoStanje")).sendKeys("0");
		driver.findElement(By.name("mjesecniTrosakUsluge")).sendKeys("0");

		// 6. Klikni na gumb za spremanje
		WebElement btnSpremi = driver.findElement(By.xpath("//button[text()='Dodaj klijenta']"));
		btnSpremi.click();

		// 7. Klijent uspješno dodan!
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		String alertText = alert.getText();

		assertEquals("Klijent uspješno dodan!", alertText);

		alert.accept();

		String imeValue = driver.findElement(By.name("ime")).getAttribute("value");
		assertEquals("", imeValue, "Forma bi se trebala isprazniti nakon uspješnog slanja.");
	}
}
