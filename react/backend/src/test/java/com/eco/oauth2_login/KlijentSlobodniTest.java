package com.eco.oauth2_login;

import com.eco.oauth2_login.databaza.KlijentRepository;
import com.eco.oauth2_login.dto.KlijentDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KlijentSlobodniTest {

    @Mock
    private KlijentRepository klijentRepository;

    @Test
    @DisplayName("Redovni slučaj: Dohvat liste klijenata koji nemaju računovođu")
    void testFindSlobodniKlijenti() {
        // 1. PRIPREMA
        KlijentDTO k1 = new KlijentDTO(1L, "Ivan", "Ivić");
        KlijentDTO k2 = new KlijentDTO(2L, "Marija", "Marić");
        
        when(klijentRepository.findSlobodniKlijenti()).thenReturn(List.of(k1, k2));

        // 2. IZVRŠAVANJE
        List<KlijentDTO> rezultat = klijentRepository.findSlobodniKlijenti();

        // 3. PROVJERA
        assertFalse(rezultat.isEmpty(), "Lista ne smije biti prazna");
        assertEquals(2, rezultat.size(), "Trebala bi biti 2 slobodna klijenta");
        assertEquals("Ivan", rezultat.get(0).getIme());
    }
}