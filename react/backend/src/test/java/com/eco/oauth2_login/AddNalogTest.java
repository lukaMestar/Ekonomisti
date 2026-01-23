package com.eco.oauth2_login;

import com.eco.oauth2_login.databaza.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddNalogTest {

    @Mock
    private PutniNalogRepository nalogRepository;
    @Mock
    private FirmaRepository firmaRepository;
    @Mock
    private ZaposlenikRepository zaposlenikRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AddNalogService addNalogService;

    @Test
    @DisplayName("Rubni uvjet: Dodavanje putnog naloga s troškom od 0 EUR")
    public void testAddNalogSaNulaTroskova() {
        // 1. PRIPREMA
        Long idFirma = 100L;
        Long idKlijent = 1L;
        Long idZaposlenik = 50L;

        Firma mockFirma = new Firma();
        mockFirma.setIdFirma(idFirma);
        mockFirma.setIdKlijent(idKlijent);

        Zaposlenik mockZaposlenik = new Zaposlenik();
        mockZaposlenik.setIdKorisnika(idZaposlenik);

        PutniNalog nalog = new PutniNalog();
        nalog.setFirma(mockFirma);
        nalog.setZaposlenik(mockZaposlenik);
        nalog.setTrosak(BigDecimal.ZERO);

        when(firmaRepository.findByIdFirmaAndIdKlijent(idFirma, idKlijent))
                .thenReturn(Optional.of(mockFirma));
        when(zaposlenikRepository.findByIdKorisnika(idZaposlenik))
                .thenReturn(Optional.of(mockZaposlenik));

        // 2. IZVRŠAVANJE
        assertDoesNotThrow(() -> addNalogService.addNalog(nalog));

        // 3. PROVJERA
        verify(nalogRepository, times(1)).save(nalog);
        assertEquals(BigDecimal.ZERO, nalog.getTrosak());
        verify(eventPublisher, times(1)).publishEvent(any(PutniNalogCreatedEvent.class));
    }

    @Test
    @DisplayName("Izazivanje pogreške: pokušaj dodavanja naloga za nepostojeću firmu")
    public void testAddNalogFirmaNePostoji() {
        // 1. PRIPREMA
        Long nepostojeciIdFirme = 500L;
        Long idKlijent = 1L;

        PutniNalog nalog = new PutniNalog();
        Firma firma = new Firma();
        firma.setIdFirma(nepostojeciIdFirme);
        firma.setIdKlijent(idKlijent);
        
        Zaposlenik zaps = new Zaposlenik();
        zaps.setIdKorisnika(10L);
        
        nalog.setFirma(firma);
        nalog.setZaposlenik(zaps);

        // Postavljamo mock da vrati prazno
        when(firmaRepository.findByIdFirmaAndIdKlijent(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        // 2. IZVRŠAVANJE I PROVJERA
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            addNalogService.addNalog(nalog);
        });

        // 3. PROVJERA
        assertEquals("Firma not found", exception.getMessage());
        verify(nalogRepository, never()).save(any());
    }
}