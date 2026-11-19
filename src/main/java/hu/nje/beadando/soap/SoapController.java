package hu.nje.beadando.soap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import soapclient.MNBArfolyamServiceSoap;
import soapclient.MNBArfolyamServiceSoapImpl;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** SOAP menü: MNB devizaárfolyamok lekérdezése és grafikonja. */
@Controller
public class SoapController {

    private static final List<String> ALAP_DEVIZAK =
            List.of("EUR", "USD", "GBP", "CHF", "JPY", "CZK", "PLN", "RON");

    @GetMapping("/soap")
    public String urlap(Model model) {
        SoapUrlap urlap = new SoapUrlap();
        urlap.setDeviza("EUR");
        urlap.setKezdoDatum(LocalDate.now().minusMonths(1).toString());
        urlap.setBefejezoDatum(LocalDate.now().toString());
        model.addAttribute("urlap", urlap);
        model.addAttribute("devizak", devizaLista());
        return "soap";
    }

    @PostMapping("/soap")
    public String lekerdezes(@ModelAttribute("urlap") SoapUrlap urlap, Model model) {
        model.addAttribute("devizak", devizaLista());
        try {
            MNBArfolyamServiceSoap service = new MNBArfolyamServiceSoapImpl().getCustomBindingMNBArfolyamServiceSoap();
            String xml = service.getExchangeRates(urlap.getKezdoDatum(), urlap.getBefejezoDatum(), urlap.getDeviza());

            List<Arfolyam> arfolyamok = new ArrayList<>();
            Document doc = xmlBeolvas(xml);
            NodeList napok = doc.getElementsByTagName("Day");
            for (int i = 0; i < napok.getLength(); i++) {
                Element nap = (Element) napok.item(i);
                NodeList rates = nap.getElementsByTagName("Rate");
                if (rates.getLength() == 0) continue;
                Element rate = (Element) rates.item(0);
                int egyseg = Integer.parseInt(rate.getAttribute("unit"));
                double ertek = Double.parseDouble(rate.getTextContent().trim().replace(',', '.'));
                arfolyamok.add(new Arfolyam(nap.getAttribute("date"), egyseg, ertek));
            }
            Collections.reverse(arfolyamok); // az MNB a legújabbal kezd, a grafikonhoz időrendben kell

            if (arfolyamok.isEmpty()) {
                model.addAttribute("hiba", "Nincs adat erre az időszakra. Próbálj másik dátumot vagy devizát!");
            }
            model.addAttribute("arfolyamok", arfolyamok);
            model.addAttribute("datumok", arfolyamok.stream().map(Arfolyam::datum).toList());
            model.addAttribute("ertekek", arfolyamok.stream().map(Arfolyam::ertek).toList());
        } catch (Exception e) {
            model.addAttribute("hiba", "Hiba az MNB lekérdezésekor: " + e.getMessage());
        }
        return "soap";
    }

    /** A devizák listáját az MNB-től kéri le; ha nem sikerül, egy alaplistát ad. */
    private List<String> devizaLista() {
        try {
            MNBArfolyamServiceSoap service = new MNBArfolyamServiceSoapImpl().getCustomBindingMNBArfolyamServiceSoap();
            NodeList curr = xmlBeolvas(service.getCurrencies()).getElementsByTagName("Curr");
            List<String> lista = new ArrayList<>();
            for (int i = 0; i < curr.getLength(); i++) {
                String d = curr.item(i).getTextContent().trim();
                if (!d.equals("HUF")) lista.add(d);
            }
            return lista.isEmpty() ? ALAP_DEVIZAK : lista;
        } catch (Exception e) {
            return ALAP_DEVIZAK;
        }
    }

    private Document xmlBeolvas(String xml) throws Exception {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    public record Arfolyam(String datum, int egyseg, double ertek) {}
}
