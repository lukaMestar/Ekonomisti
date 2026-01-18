package com.eco.oauth2_login.report;

import java.math.BigDecimal;

public record  FirmaReportDTO (
    String imeFirme,
    String imeVlasnika,
    String emailIzvjestaj,
    BigDecimal TrenutnoStanjeFirme
){
    
}
