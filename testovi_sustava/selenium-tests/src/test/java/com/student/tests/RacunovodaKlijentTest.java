package com.student.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RacunovodaKlijentTest {

	private WebDriver driver;
	private WebDriverWait wait;
	private final String baseUrl = "http://localhost:5173";

	@BeforeEach
	void setup() {
		ChromeOptions options = new ChromeOptions();

		// Putanja do profila
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
	void testDodajKlijentaSNulama() {
		// 1. Idi na stranicu s klijentima
		driver.get(baseUrl + "/racunklijenti");

		driver.get(baseUrl + "/racunklijenti");
		System.out.println("Trenutni URL: " + driver.getCurrentUrl());

		// 2. Klik "Dodaj klijenta"
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
		driver.findElement(By.name("emailIzvjestaj")).sendKeys("izvjestaj@test.com");
		// 5. Unesi 0 u polja za iznose
		driver.findElement(By.name("pocetnoStanje")).sendKeys("0");
		driver.findElement(By.name("mjesecniTrosakUsluge")).sendKeys("0");

		// 6. gumb za spremanje
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

	@Test
	@Order(2)
	@DisplayName("Izazivanje pogreške: Slanje forme bez obaveznih podataka")
	void testDodajKlijentaGreska() {
		driver.get(baseUrl + "/racunklijenti");

		driver.get(baseUrl + "/racunklijenti");
		System.out.println("Trenutni URL: " + driver.getCurrentUrl());

		WebElement btnDodaj = wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Dodaj klijenta')]")));
		btnDodaj.click();

		// 1. otvori formu
		WebElement btnOtvoriFormu = wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Dodaj klijenta')]")));
		btnOtvoriFormu.click();

		// 2. Čekaj da se forma učita (provjeri npr. naslov "Novi klijent")
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Novi klijent']")));

		// 3. Klikni na gumb "Dodaj klijenta" unutar forme bez unosa podataka
		WebElement btnSpremi = driver.findElement(By.xpath("//form//button"));
		btnSpremi.click();

		// 4. Provjera
		WebElement greskaPoruka = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(), 'Sva obavezna polja')]")));

		assertTrue(greskaPoruka.isDisplayed(),
				"Poruka o grešci bi trebala biti vidljiva nakon pokušaja slanja prazne forme.");

		// Dodatna provjera teksta poruke
		assertEquals("Sva obavezna polja moraju biti popunjena.", greskaPoruka.getText());
	}

	@Test
	@Order(3)
	@DisplayName("Nepostojeća funkcionalnost: Pristup neimplementiranoj ruti /arhiva")
	void testPristupNepostojecojRuti() {
	    // 1. pokušaj pristupa
	    driver.get(baseUrl + "/arhiva");

	    // 2. Čekaj kratko
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

	    // 3. Provjera: je li se učitao naslov forme (ne bi trebao)
	    boolean naslovPrisutan = driver.findElements(By.xpath("//h1")).size() > 0;
	    
	    // Provjera teksta na stranici
	    String content = driver.findElement(By.tagName("body")).getText();

	    assertTrue(!naslovPrisutan || content.length() < 100, 
	               "Sustav ne bi trebao prikazati funkcionalan sadržaj na ruti /arhiva.");
	}
}
