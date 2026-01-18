package com.eco.oauth2_login.report;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eco.oauth2_login.MjesecniRacunService;
import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.FakturaRepository;
import com.eco.oauth2_login.databaza.Firma;
import com.eco.oauth2_login.databaza.KorisnikRepository;
import com.eco.oauth2_login.databaza.MjesecniRacun;
import com.eco.oauth2_login.databaza.PutniNalog;
import com.eco.oauth2_login.databaza.Zaposlenik;
import com.eco.oauth2_login.databaza.ZaposlenikDTO;

@Service
public class NoviService {
    private final KorisnikRepository korisnikRepository;
    private final MjesecniRacunService mjesecniRacunService;
    private final JeZaposlenService jeZaposlenService;
    private final FirmaReportService firmaReportService;

    @Autowired
    public NoviService(KorisnikRepository korisnikRepository, MjesecniRacunService mjesecniRacunService
        ,JeZaposlenService jeZaposlenService, FirmaReportService firmaReportService
    ) {
        this.korisnikRepository = korisnikRepository;
        this.mjesecniRacunService =  mjesecniRacunService;
        this.jeZaposlenService = jeZaposlenService;
        this.firmaReportService = firmaReportService;
    }
    

    public List<ZaposlenikDTO> popisZaposlenika(Long klijentID){
        Long firmaId = firmaReportService.vratiFirmu(klijentID).getIdFirma();
        List<Zaposlenik> listaZaposlenika = jeZaposlenService.listaZaposlenikaZaFirmu(firmaId, klijentID);  
        List<ZaposlenikDTO>  listaDTO = new ArrayList<>(); 
        for(Zaposlenik z : listaZaposlenika){
            ZaposlenikDTO zap = new ZaposlenikDTO();
            Long idZaposlenik = z.getIdKorisnika();
            String imeZaposlenika = korisnikRepository.findById(idZaposlenik)
                                                .orElseThrow(() -> new RuntimeException("Korisnik ne postoji"))
                                                .getImeKorisnik();
            zap.setImeZaposlenik(imeZaposlenika);
            zap.setIdKorisnika(idZaposlenik);
            zap.setPlaca(z.getPlaca());
            listaDTO.add(zap);
        }        

        return listaDTO;
    }

    public List<MjesecniRacunDTO> ispisRacuna(Long klijentID){
        List<MjesecniRacun> racuni = mjesecniRacunService.getRacuniZaKlijenta(klijentID);
        List<MjesecniRacunDTO> listaRacuna = new  ArrayList<>();
        for (MjesecniRacun r :racuni){
            listaRacuna.add(new MjesecniRacunDTO(r.getStatusPlacanja(),
                                                r.getIznos(),
                                                r.getDatumGeneriranja())
                                            );
        }
        return listaRacuna;
    }

    public FirmaReportDTO dohvatiPodatke(Long klijentID){
        String imeVlasnika = korisnikRepository.findById(klijentID)
            .orElseThrow(() -> new RuntimeException("Korisnik ne postoji"))
            .getImeKorisnik();
        
        Firma firma = firmaReportService.vratiFirmu(klijentID);
        String imeFirme = firma.getNazivFirme();
        String emailIzvjestaj = firma.getEmailIzvjestaj();
        BigDecimal trenutnoStanjeFirme = firma.getStanjeRacuna();
        
        return new FirmaReportDTO(imeFirme, imeVlasnika, emailIzvjestaj, trenutnoStanjeFirme);
    }


}
