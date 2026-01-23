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
import org.springframework.web.bind.annotation.*;
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

    public ReportController(ReportService reportService, UserRepository userRepository,
            FirmaReportService firmaReportService,
            NoviService noviService, OdradjeneFaktureService odradjeneFaktureService,
            OdradjeniPutniNaloziService odradjeniPutniNaloziService, FakturaReportService fakturaReportService,
            PutniNalogReportService putniNalogReportService) {
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
    public ResponseEntity<List<ZaposlenikDTO>> dohvatiZaposlenike(@AuthenticationPrincipal OAuth2User principal,
            HttpServletRequest request) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String email = principal.getAttribute("email");
        List<ZaposlenikDTO> lista = new ArrayList<>();
        if (email != null) {
            Optional<Korisnik> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                Korisnik user = userOptional.get();
                if (user.getIdUloge() == 3) {
                    Firma firma = firmaReportService.vratiFirmu(user.getIdKorisnika());
                    if (firma != null) {
                        lista = noviService.popisZaposlenika(user.getIdKorisnika(), firma.getIdFirma());
                    }
                }
            }
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/dohvacanjeRacuna")
    public ResponseEntity<List<MjesecniRacunDTO>> dohvatiRacune(@AuthenticationPrincipal OAuth2User principal,
            HttpServletRequest request) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String email = principal.getAttribute("email");
        List<MjesecniRacunDTO> lista = new ArrayList<>();
        if (email != null) {
            Optional<Korisnik> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent() && userOptional.get().getIdUloge() == 3) {
                lista = noviService.ispisRacuna(userOptional.get().getIdKorisnika());
            }
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/listaPN")
    public ResponseEntity<Map<String, List<PutniNalog>>> dohvatiPN(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestParam(required = false) Long odabranaFirmaId) {

        if (principal == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        String email = principal.getAttribute("email");
        Map<String, List<PutniNalog>> rezultat = new HashMap<>();

        if (email != null) {
            Optional<Korisnik> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                Korisnik user = userOptional.get();
                Long userId = user.getIdKorisnika();
                Integer idUloge = user.getIdUloge();

                Long firmaid = odabranaFirmaId;

                if (idUloge == 3) {
                    if (firmaid == null) {
                        Firma firma = firmaReportService.vratiFirmu(userId);
                        if (firma == null)
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                        firmaid = firma.getIdFirma();
                    }
                    List<PutniNalog> odradjeni = odradjeniPutniNaloziService.odrPN(firmaid, userId);
                    List<PutniNalog> sviFirma = putniNalogReportService.getSvePutneNaloge(firmaid);

                    Set<Long> odradjeniIds = odradjeni.stream().map(PutniNalog::getIdPutniNalog)
                            .collect(Collectors.toSet());
                    List<PutniNalog> neodradjeni = sviFirma.stream()
                            .filter(pn -> !odradjeniIds.contains(pn.getIdPutniNalog())).toList();

                    rezultat.put("odradjeni", odradjeni);
                    rezultat.put("neodradjeni", neodradjeni);
                    return ResponseEntity.ok(rezultat);
                }

                if (idUloge == 4) {
                    if (firmaid == null) {
                        Firma firma = firmaReportService.vratiFrimuRadnik(userId);
                        if (firma == null)
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                        firmaid = firma.getIdFirma();
                    }
                    List<PutniNalog> sviRadnikPN = putniNalogReportService.getRadnikPN(firmaid, userId);
                    rezultat.put("svi", sviRadnikPN);
                    return ResponseEntity.ok(rezultat);
                }
            }
        }
        return ResponseEntity.ok(rezultat);
    }

    @GetMapping("/listaF")
    public ResponseEntity<Map<String, List<Faktura>>> dohvatiF(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String email = principal.getAttribute("email");
        List<Faktura> odradjeneFakture = new ArrayList<>();
        List<Faktura> neodradjeneFakture = new ArrayList<>();
        if (email != null) {
            Optional<Korisnik> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent() && userOptional.get().getIdUloge() == 3) {
                Firma firma = firmaReportService.vratiFirmu(userOptional.get().getIdKorisnika());
                if (firma != null) {
                    odradjeneFakture = odradjeneFaktureService.odrF(firma.getIdFirma());
                    neodradjeneFakture = fakturaReportService.getNeodradjeneFakture(firma.getIdFirma());
                }
            }
        }
        Map<String, List<Faktura>> rezultat = new HashMap<>();
        rezultat.put("odradjeni", odradjeneFakture);
        rezultat.put("neodradjeni", neodradjeneFakture);
        return ResponseEntity.ok(rezultat);
    }
}