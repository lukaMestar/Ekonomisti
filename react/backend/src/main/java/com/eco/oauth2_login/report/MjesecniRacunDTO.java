package com.eco.oauth2_login.report;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MjesecniRacunDTO(
    String statusPlacanja, // neplaceno , placeno , refundirano, otkazano
    BigDecimal iznos,
    LocalDate datumGeneriranja,
    Long idRacuna

) {
    
}
