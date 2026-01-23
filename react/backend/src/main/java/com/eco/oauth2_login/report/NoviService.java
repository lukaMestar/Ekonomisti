package com.eco.oauth2_login.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eco.oauth2_login.MjesecniRacunService;
import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.KorisnikRepository;
import com.eco.oauth2_login.databaza.MjesecniRacun;
import com.eco.oauth2_login.databaza.Placa;
import com.eco.oauth2_login.databaza.PlacaRepository;
import com.eco.oauth2_login.databaza.Zaposlenik;
import com.eco.oauth2_login.databaza.ZaposlenikDTO;

@Service
public class NoviService {
    private final KorisnikRepository korisnikRepository;
    private final MjesecniRacunService mjesecniRacunService;
    private final JeZaposlenService jeZaposlenService;
    private final PlacaRepository placaRepository;

    @Autowired
    public NoviService(KorisnikRepository korisnikRepository, MjesecniRacunService mjesecniRacunService,
            JeZaposlenService jeZaposlenService, PlacaRepository placaRepository) {
        this.korisnikRepository = korisnikRepository;
        this.mjesecniRacunService = mjesecniRacunService;
        this.jeZaposlenService = jeZaposlenService;
        this.placaRepository = placaRepository;
    }

    public List<ZaposlenikDTO> popisZaposlenika(Long klijentID, Long firmaId) {
        List<Zaposlenik> listaZaposlenika = jeZaposlenService.listaZaposlenikaZaFirmu(firmaId, klijentID);
        List<ZaposlenikDTO> listaDTO = new ArrayList<>();
        for (Zaposlenik z : listaZaposlenika) {
            ZaposlenikDTO zap = new ZaposlenikDTO();
            Long idZap = z.getIdKorisnika();
            Korisnik kor = korisnikRepository.findById(idZap)
                    .orElseThrow(() -> new RuntimeException("Korisnik ne postoji"));

            Placa zapPlaca = placaRepository.findPlace(idZap, firmaId);

            zap.setImeZaposlenik(kor.getImeKorisnik() + " " + kor.getPrezimeKorisnik());
            zap.setIdKorisnika(idZap);
            zap.setPlaca(zapPlaca.getIznosPlace());
            listaDTO.add(zap);
        }
        return listaDTO;
    }

    public List<MjesecniRacunDTO> ispisRacuna(Long klijentID) {
        List<MjesecniRacun> racuni = mjesecniRacunService.getRacuniZaKlijenta(klijentID);
        List<MjesecniRacunDTO> listaRacuna = new ArrayList<>();
        for (MjesecniRacun r : racuni) {
            listaRacuna.add(
                    new MjesecniRacunDTO(r.getStatusPlacanja(), r.getIznos(), r.getDatumGeneriranja(), r.getIdRacun()));
        }
        return listaRacuna;
    }
}