package com.eco.oauth2_login.report;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eco.oauth2_login.databaza.UserRepository;
import com.eco.oauth2_login.databaza.ZaposlenikDTO;
import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.Firma;
import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.PutniNalog;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/izvjestaj")

public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;
    private final FirmaReportService firmaReportService;
    private final NoviService noviService;
    private final OdradjeneFaktureService odradjeneFaktureService;
    private final OdradjeniPutniNaloziService odradjeniPutniNaloziService;
    private final FakturaReportService fakturaReportService;
    private final PutniNalogReportService putniNalogReportService;

    public ReportController(ReportService reportService, UserRepository userRepository, FirmaReportService firmaReportService,
        NoviService noviService, OdradjeneFaktureService odradjeneFaktureService, OdradjeniPutniNaloziService odradjeniPutniNaloziService
        , FakturaReportService fakturaReportService, PutniNalogReportService putniNalogReportService) {
        this.reportService = reportService;
        this.userRepository = userRepository;
        this.firmaReportService = firmaReportService;
        this.noviService = noviService;
        this.odradjeniPutniNaloziService = odradjeniPutniNaloziService;
        this.odradjeneFaktureService = odradjeneFaktureService;
        this.fakturaReportService = fakturaReportService;
        this.putniNalogReportService = putniNalogReportService;
    }

    @GetMapping("/{klijentId}/{mjesec}")
    public IzvjestajDTO pdf(@PathVariable Long klijentId, @PathVariable String mjesec) {
        YearMonth ym = YearMonth.parse(mjesec);
        return reportService.generirajMjesecniIzvjestajJson(klijentId, ym);
    }


    @GetMapping("/dohvacanjeZaposlenika")
    public ResponseEntity<List<ZaposlenikDTO>> dohvatiZaposlenike(@AuthenticationPrincipal OAuth2User principal, HttpServletRequest request) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String email = principal.getAttribute("email");

        Long userId = 0L;
        Long firmaId = null; 

        List<ZaposlenikDTO> lista = new ArrayList<>();
        
        if (email != null) {
            Optional<Korisnik> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                Korisnik user = userOptional.get();
                Integer idUloge = user.getIdUloge();
                userId = user.getIdKorisnika();
                if (idUloge == 3) { 
                    Firma firma = firmaReportService.vratiFirmu(userId);
                    if (firma == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }
                    // id firme i idkorisnika userid mi je idkorisnik
                    firmaId = firma.getIdFirma();
                    lista = noviService.popisZaposlenika(userId, firmaId);
                }
            }
        }
        return ResponseEntity.ok(lista);
    }



    @GetMapping("/dohvacanjeRacuna")
    public ResponseEntity<List<MjesecniRacunDTO>> dohvatiRacune(@AuthenticationPrincipal OAuth2User principal, HttpServletRequest request) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String email = principal.getAttribute("email");
        Long userId = 0L;
        List<MjesecniRacunDTO> lista = new ArrayList<>();
        
        if (email != null) {
            Optional<Korisnik> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                Korisnik user = userOptional.get();
                Integer idUloge = user.getIdUloge();
                userId = user.getIdKorisnika();
                if (idUloge == 3) { 
                    Firma firma = firmaReportService.vratiFirmu(userId);
                    if (firma == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }
                    lista = noviService.ispisRacuna(userId);


                }
            }
        }
        return ResponseEntity.ok(lista);

    }


    @GetMapping("/listaPN")
    public ResponseEntity<Map<String, List<PutniNalog>>> dohvatiPN(@AuthenticationPrincipal OAuth2User principal, HttpServletRequest request) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String email = principal.getAttribute("email");

        Long userId = 0L;

        List<PutniNalog> listaOdradjenihPN = new ArrayList<>();
        List<PutniNalog> listaNeodredjenihPN = new ArrayList<>();
        
        if (email != null) {
            Optional<Korisnik> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                Korisnik user = userOptional.get();
                Integer idUloge = user.getIdUloge();
                userId = user.getIdKorisnika();
                if (idUloge == 3) { 
                    Firma firma = firmaReportService.vratiFirmu(userId);
                    if (firma == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }

                    Long firmaid = firma.getIdFirma();
                    listaOdradjenihPN = odradjeniPutniNaloziService.odrPN(firmaid, userId);
                    listaNeodredjenihPN = putniNalogReportService.getSvePutneNaloge(firmaid);

                    Set<Long> odradjeniIds = listaOdradjenihPN.stream().map(PutniNalog::getIdPutniNalog)
                        .collect(Collectors.toSet());
                    listaNeodredjenihPN = listaNeodredjenihPN.stream()
                        .filter(pn -> !odradjeniIds.contains(pn.getIdPutniNalog()))
                        .toList();
                }

            if (idUloge == 4) { 
                    Firma firma = firmaReportService.vratiFrimuRadnik(userId);
                    if (firma == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }

                    Long firmaid = firma.getIdFirma();
                    listaOdradjenihPN = odradjeniPutniNaloziService.odrPNRadnik(firmaid, userId);
                    System.out.println(firmaid + "hooooooooooooooooooooooooooooooooooooooooooooooooooo");
                    listaNeodredjenihPN = putniNalogReportService.getRadnikPN(firmaid,userId);

                    Set<Long> odradjeniIds = listaOdradjenihPN.stream().map(PutniNalog::getIdPutniNalog)
                        .collect(Collectors.toSet());
                    listaNeodredjenihPN = listaNeodredjenihPN.stream()
                        .filter(pn -> !odradjeniIds.contains(pn.getIdPutniNalog()))
                        .toList();
                }
            }
        }
        Map<String, List<PutniNalog>> rezultat = new HashMap<>();
        rezultat.put("odradjeni", listaOdradjenihPN);
        rezultat.put("neodradjeni", listaNeodredjenihPN);
        return ResponseEntity.ok(rezultat);

    }

    @GetMapping("/listaF")
    public ResponseEntity<Map<String, List<Faktura>>> dohvatiF(@AuthenticationPrincipal OAuth2User principal, HttpServletRequest request) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String email = principal.getAttribute("email");

        Long userId = 0L;

        List<Faktura> odradjeneFakture = new ArrayList<>();
         List<Faktura> neodradjeneFakture = new ArrayList<>();
        
        if (email != null) {
            Optional<Korisnik> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                Korisnik user = userOptional.get();
                Integer idUloge = user.getIdUloge();
                userId = user.getIdKorisnika();
                if (idUloge == 3) { 
                    Firma firma = firmaReportService.vratiFirmu(userId);
                    if (firma == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }

                    Long firmaid = firma.getIdFirma();
                    odradjeneFakture = odradjeneFaktureService.odrF(firmaid);
                    neodradjeneFakture = fakturaReportService.getNeodradjeneFakture(firmaid);
                }
            }
        }

        Map<String, List<Faktura>> rezultat = new HashMap<>();
        rezultat.put("odradjeni", odradjeneFakture);
        rezultat.put("neodradjeni", neodradjeneFakture);

        return ResponseEntity.ok(rezultat);

    }

}

