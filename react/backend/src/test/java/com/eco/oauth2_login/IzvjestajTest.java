package com.eco.oauth2_login;

import com.eco.oauth2_login.databaza.*;
import com.eco.oauth2_login.report.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IzvjestajTest {

    @Mock
    private FakturaReportService fakturaReportService;
    @Mock
    private PutniNalogReportService putniNalogReportService;
    @Mock
    private RacunovodaKlijentReportService racunovodaKlijentReportService;
    @Mock
    private FirmaRepository firmaRepository;
    @Mock
    private PlacaRepository placaRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    @DisplayName("Testiranje izvještaja")
    public void testMjesecniIzvjestaj() {
        Long klijentId = 1L;
        Long firmaId = 10L;
        YearMonth mjesec = YearMonth.of(2023, 8);

        // Simuliramo pronalazak firme
        Firma mockFirma = new Firma();
        mockFirma.setIdFirma(firmaId);
        mockFirma.setIdKlijent(klijentId);
        when(firmaRepository.findByIdKlijent(klijentId)).thenReturn(Optional.of(mockFirma));

        // Simuliramo fakture
        Faktura f1 = new Faktura();
        f1.setIznos(new BigDecimal("1000.00"));
        when(fakturaReportService.getFaktureZaMjesec(klijentId, firmaId, mjesec))
                .thenReturn(Collections.singletonList(f1));

        // Simuliramo putne naloge
        PutniNalog pn = new PutniNalog();
        pn.setTrosak(new BigDecimal("100.00"));
        when(putniNalogReportService.getPutniNaloziZaTekuciMjesec(firmaId, mjesec))
                .thenReturn(Collections.singletonList(pn));

        // Simuliramo trošak usluge računovođe (Rashodi: 50.00)
        when(racunovodaKlijentReportService.getMjesecniTrosakUsluge(klijentId))
                .thenReturn(new BigDecimal("50.00"));

        // Simuliramo plaće zaposlenika
        Placa p = new Placa();
        p.setIznosPlace(new BigDecimal("350.00"));
        when(placaRepository.findByIdFirma(firmaId)).thenReturn(Collections.singletonList(p));

        IzvjestajDTO rezultat = reportService.generirajMjesecniIzvjestajJson(klijentId, mjesec);

        assertEquals(1000.00, rezultat.getPrihodi());

        // Rashodi = 100 (putni) + 50 (usluga) + 350 (placa) = 500.00
        assertEquals(500.00, rezultat.getRashodi());

        // PDV = 1000 * 0.25 = 250.00
        assertEquals(250.00, rezultat.getPdv());

        // Dobitak = 1000 (prihodi) - 500 (rashodi) - 250 (pdv) = 250.00
        assertEquals(250.00, rezultat.getDobitak());
    }
}