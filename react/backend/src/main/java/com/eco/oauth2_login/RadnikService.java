package com.eco.oauth2_login;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eco.oauth2_login.databaza.FirmaDTO;
import com.eco.oauth2_login.databaza.JeZaposlenRepository;

@Service
public class RadnikService {

    private final JeZaposlenRepository jeZaposlenRepository;

    public RadnikService(JeZaposlenRepository repo) {
        this.jeZaposlenRepository = repo;
    }

    public List<FirmaDTO> getFirmeZaRadnika(Long idZaposlenik) {
        return jeZaposlenRepository
            .findFirmeZaZaposlenika(idZaposlenik)
            .stream()
            .map(f -> new FirmaDTO(
                f.getIdFirma(),
                f.getIdKlijent(),
                f.getNazivFirme()
            ))
            .toList();
    }
}
