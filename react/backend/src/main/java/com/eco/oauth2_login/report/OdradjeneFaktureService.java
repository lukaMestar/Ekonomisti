package com.eco.oauth2_login.report;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
