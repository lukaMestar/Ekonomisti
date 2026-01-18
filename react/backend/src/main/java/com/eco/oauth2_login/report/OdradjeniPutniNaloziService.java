package com.eco.oauth2_login.report;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eco.oauth2_login.databaza.OdradjeniPutniNalozi;
import com.eco.oauth2_login.databaza.OdradjeniPutniNaloziRepository;


@Service
public class OdradjeniPutniNaloziService {

    private final OdradjeniPutniNaloziRepository odradjeniRepo;

    public OdradjeniPutniNaloziService(
        OdradjeniPutniNaloziRepository odradjeniRepo) {
        this.odradjeniRepo = odradjeniRepo;
    }

    @Transactional
    public void oznaciPutniNalogOdradjenim(Long idPutniNalog, Long idFirma, Long idKlijent
    ) {
        if (odradjeniRepo.existsByIdPutniNalogAndIdFirmaAndIdKlijent( idPutniNalog, idFirma, idKlijent)) {
            return;
        }

        OdradjeniPutniNalozi opn =
            new OdradjeniPutniNalozi(idPutniNalog, idFirma, idKlijent);

        odradjeniRepo.save(opn);
    }
}
