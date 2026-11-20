package hu.nje.beadando.forex;

import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.pricing.PricingGetResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/** Forex-AktÁr menü: a kiválasztott instrumentum aktuális ára. */
@Controller
public class ForexAktArController {

    private final OandaService oanda;

    public ForexAktArController(OandaService oanda) {
        this.oanda = oanda;
    }

    @GetMapping("/forex/aktar")
    public String urlap(Model model) {
        model.addAttribute("instrumentumok", OandaService.INSTRUMENTUMOK);
        model.addAttribute("instrument", "EUR_USD");
        return "forex/aktar";
    }

    @PostMapping("/forex/aktar")
    public String lekerdezes(@RequestParam String instrument, Model model) {
        model.addAttribute("instrumentumok", OandaService.INSTRUMENTUMOK);
        model.addAttribute("instrument", instrument);
        try {
            PricingGetRequest request = new PricingGetRequest(oanda.accountId(), List.of(instrument));
            PricingGetResponse resp = oanda.ctx().pricing.get(request);
            ClientPrice ar = resp.getPrices().get(0);
            model.addAttribute("ido", ar.getTime());
            model.addAttribute("bid", ar.getBids().get(0).getPrice());
            model.addAttribute("ask", ar.getAsks().get(0).getPrice());
            model.addAttribute("kereskedheto", Boolean.TRUE.equals(ar.getTradeable()) ? "igen" : "nem (zárva a piac)");
            model.addAttribute("van", true);
        } catch (Exception e) {
            model.addAttribute("hiba", OandaService.hibauzenet(e));
        }
        return "forex/aktar";
    }
}
