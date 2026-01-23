package com.eco.oauth2_login;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

// pomoćna klasa
class ValutaService {
	public BigDecimal convertCurrency(BigDecimal iznos, String izValute, String uValutu) {
		throw new UnsupportedOperationException("Currency conversion logic is not yet implemented.");
	}
}

public class ValutaTest {

	private final ValutaService valutaService = new ValutaService();

	@Test
	@DisplayName("Nepostojeća funkcionalnost: Testiranje poziva neimplementirane funkcije")
	void testConvertCurrencyThrowsException() {
		// 1. PRIPREMA
		BigDecimal iznos = new BigDecimal("100.00");
		String izValute = "USD";
		String uValutu = "EUR";

		// 2. IZVRŠAVANJE I PROVJERA
		// baca li sustav ispravnu iznimku
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
				() -> valutaService.convertCurrency(iznos, izValute, uValutu),
				"Očekivano: UnsupportedOperationException");

		// Provjera poruke
		assertEquals("Currency conversion logic is not yet implemented.", exception.getMessage());
	}
}