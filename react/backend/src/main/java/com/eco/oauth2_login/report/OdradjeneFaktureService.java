package com.eco.oauth2_login.report;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.OdradjeneFakture;
import com.eco.oauth2_login.databaza.OdradjeneFaktureRepository;



@Service
public class OdradjeneFaktureService {

    private final OdradjeneFaktureRepository odradjeneRepo;

    public OdradjeneFaktureService(OdradjeneFaktureRepository odradjeneRepo) {
        this.odradjeneRepo = odradjeneRepo;
    }

    @Transactional
    public void oznaciFakturuOdradjenom(Long idFaktura, Long idFirma, Long idKlijent) {

        if (odradjeneRepo.existsByIdFakturaAndIdFirmaAndIdKlijent(
                idFaktura, idFirma, idKlijent)) {
            return; 
        }
        OdradjeneFakture of = new OdradjeneFakture(idFaktura, idFirma, idKlijent);
        odradjeneRepo.save(of);
    }

    public List<Faktura> odrF(Long klijentid){
        return odradjeneRepo.OdradjeneFakture(klijentid);
    }
}
