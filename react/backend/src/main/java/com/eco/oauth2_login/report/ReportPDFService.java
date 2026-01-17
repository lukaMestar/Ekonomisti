package com.eco.oauth2_login.report;

import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.PutniNalog;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ReportPDFService {
    public byte[] generirajIzvjestaj(ReportDTO report) {
        BigDecimal mjesecniTrosakUsluge =
            report.getMjesecniTrosakUsluge() != null
                ? report.getMjesecniTrosakUsluge()
                : BigDecimal.ZERO;

        List<Faktura> listaFaktura =
            report.getListaFaktura() != null
                ? report.getListaFaktura()
                : List.of();

        List<PutniNalog> listaPutniNalog =
            report.getListaPutnihNaloga() != null
                ? report.getListaPutnihNaloga()
                : List.of();

        BigDecimal ukupanIznosFaktura = listaFaktura.stream()
                                                    .map(Faktura::getIznos)
                                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal ukupanIznosPutnihNaloga = listaPutniNalog.stream()
                                                    .map(PutniNalog::getTrosak)
                                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ukupanIznosMjesecnogTroska = BigDecimal.ZERO;

        ukupanIznosMjesecnogTroska = ukupanIznosMjesecnogTroska.add(mjesecniTrosakUsluge)
                                                                .add(ukupanIznosPutnihNaloga);


        BigDecimal iznos = ukupanIznosFaktura.subtract(ukupanIznosMjesecnogTroska);
        iznos = iznos.abs();
        BigDecimal pdv = iznos.multiply(new BigDecimal("0.25"));
        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            Font naslov = new Font(Font.TIMES_ROMAN, 20, Font.BOLD);
            Font sporedniNaslov = new Font(Font.TIMES_ROMAN, 16);

            Font tekst = new Font(Font.TIMES_ROMAN, 14);

            Paragraph title = new Paragraph("Mjesečni izvještaj", naslov);
            title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Pregled svih faktura", sporedniNaslov));
            document.add(new Paragraph(" "));
            com.lowagie.text.List lista = new com.lowagie.text.List(com.lowagie.text.List.ORDERED);
            
            listaFaktura.forEach(f -> {
                lista.add(
                    new ListItem(
                        "ID fakture: " + f.getIdFaktura()
                        + " | opis: " + f.getOpis()
                        + " | iznos: EUR " + f.getIznos()
                        , tekst
                    )
                );
            });
            
            document.add(lista);

            document.add(new Paragraph("Pregled svih putnih naloga", sporedniNaslov));
            document.add(new Paragraph(" "));
            com.lowagie.text.List listaPN = new com.lowagie.text.List(com.lowagie.text.List.ORDERED);

            listaPutniNalog.forEach(pn -> {
                listaPN.add(
                    new ListItem(
                        "ID putni nalog: " + pn.getIdPutniNalog()
                        + " | datum poslaska: " + pn.getDatumPolaska()
                        + " | datum povratka: " + pn.getDatumPovratka()
                        + " | iznos: EUR " + pn.getTrosak()
                        , tekst
                    )
                );
            });

            document.add(listaPN);

            document.add(new Paragraph("Iznos troškova prema računovođi: EUR "  + mjesecniTrosakUsluge, tekst));
            document.add(new Paragraph(" ")); 

            document.add(new Paragraph("Ukupan mjesecni trosak firme: EUR " + ukupanIznosMjesecnogTroska , tekst));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Ukupan mjesecni prihod firme: EUR " + ukupanIznosFaktura , tekst));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Ukupan PDV: EUR " + pdv , tekst));
            document.add(new Paragraph(" "));

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Greška kod generiranja PDF-a", e);
        }
    }

    public byte[] generirajRacun(ReportDTO report){
            BigDecimal mjesecniTrosakUsluge =
            report.getMjesecniTrosakUsluge() != null
                ? report.getMjesecniTrosakUsluge()
                : BigDecimal.ZERO;


            try {
                Document document = new Document(PageSize.A6);
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                PdfWriter.getInstance(document, out);
                document.open();

                Font naslov = new Font(Font.TIMES_ROMAN, 20, Font.BOLD);
               

                Font tekst = new Font(Font.TIMES_ROMAN, 14);

                Paragraph title = new Paragraph("Račun za podmirivanje troškova računovodstvenih usluga", naslov);
                title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph(" "));

                document.add(new Paragraph("Iznos troškova prema računovođi: EUR "  + mjesecniTrosakUsluge, tekst));
                document.add(new Paragraph(" "));

                document.close();
                return out.toByteArray();


            } catch (Exception e) {
                throw new RuntimeException("Greška kod generiranja PDF-a", e);
            }

        }




    
}
