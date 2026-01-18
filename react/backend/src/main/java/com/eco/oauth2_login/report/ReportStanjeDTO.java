package com.eco.oauth2_login.report;

import java.math.BigDecimal;
import java.util.List;

import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.PutniNalog;

public record ReportStanjeDTO(
    List<Faktura> listaFaktura,
    List<PutniNalog> listaPutnihNaloga,
    Double PDV
) {
}  