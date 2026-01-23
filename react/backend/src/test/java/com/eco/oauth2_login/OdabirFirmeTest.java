package com.eco.oauth2_login;

import com.eco.oauth2_login.databaza.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OdabirFirmeTest {

	@Mock
	private JeZaposlenRepository jeZaposlenRepository;

	@InjectMocks
	private RadnikService radnikService;

	@Test
	@DisplayName("Redovni slučaj: Dohvat i mapiranje firmi u DTO za radnika")
	public void testGetFirmeZaRadnika() {
		// 1. PRIPREMA
		Long idRadnika = 10L;

		// Simuliramo podatke iz baze
		Firma f1 = new Firma();
		f1.setIdFirma(100L);
		f1.setIdKlijent(1L);
		f1.setNazivFirme("Eco Tech d.o.o.");

		Firma f2 = new Firma();
		f2.setIdFirma(101L);
		f2.setIdKlijent(2L);
		f2.setNazivFirme("Solaris j.d.o.o.");

		List<Firma> firmeIzBaze = Arrays.asList(f1, f2);

		// Kad servis pita repozitorij, vrati listu entiteta
		when(jeZaposlenRepository.findFirmeZaZaposlenika(idRadnika)).thenReturn(firmeIzBaze);

		// 2. IZVRŠAVANJE
		List<FirmaDTO> rezultat = radnikService.getFirmeZaRadnika(idRadnika);

		// 3. PROVJERA
		assertNotNull(rezultat);
		assertEquals(2, rezultat.size(), "Treba vratiti 2 DTO objekta");

		FirmaDTO dto1 = rezultat.get(0);
		assertEquals(100L, dto1.idFirma());
		assertEquals("Eco Tech d.o.o.", dto1.naziv());

		// je li repozitorij pozvan
		verify(jeZaposlenRepository, times(1)).findFirmeZaZaposlenika(idRadnika);
	}

	@Test
	@DisplayName("Rubni slučaj: Radnik nije zaposlen nigdje")
	public void testGetFirmeZaRadnikaPrazno() {
		// 1. PRIPREMA
		Long idNezaposlenog = 99L;
		when(jeZaposlenRepository.findFirmeZaZaposlenika(idNezaposlenog)).thenReturn(Collections.emptyList());

		// 2. IZVRŠAVANJE
		List<FirmaDTO> rezultat = radnikService.getFirmeZaRadnika(idNezaposlenog);

		// 3. PROVJERA
		assertTrue(rezultat.isEmpty(), "Lista DTO-a mora biti prazna");
	}
}