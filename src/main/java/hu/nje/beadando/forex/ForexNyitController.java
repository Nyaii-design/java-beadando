package hu.nje.beadando.forex;

import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderCreateRequest;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.transaction.OrderFillTransaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Forex-Nyit menü: pozíció nyitása piaci áron (pozitív = Long, negatív = Short). */
@Controller
public class ForexNyitController {

    private final OandaService oanda;

    public ForexNyitController(OandaService oanda) {
        this.oanda = oanda;
    }

    @GetMapping("/forex/nyit")
    public String urlap(Model model) {
        model.addAttribute("instrumentumok", OandaService.INSTRUMENTUMOK);
        model.addAttribute("instrument", "EUR_USD");
        model.addAttribute("mennyiseg", 100);
        return "forex/nyit";
    }

    @PostMapping("/forex/nyit")
    public String nyitas(@RequestParam String instrument, @RequestParam long mennyiseg, Model model) {
        model.addAttribute("instrumentumok", OandaService.INSTRUMENTUMOK);
        model.addAttribute("instrument", instrument);
        model.addAttribute("mennyiseg", mennyiseg);
        if (mennyiseg == 0) {
            model.addAttribute("hiba", "A mennyiség nem lehet 0 (pozitív = Long, negatív = Short).");
            return "forex/nyit";
        }
        try {
            MarketOrderRequest megbizas = new MarketOrderRequest();
            megbizas.setInstrument(new InstrumentName(instrument));
            megbizas.setUnits(mennyiseg);

            OrderCreateRequest request = new OrderCreateRequest(oanda.accountId());
            request.setOrder(megbizas);
            OrderCreateResponse resp = oanda.ctx().order.create(request);

            OrderFillTransaction fill = resp.getOrderFillTransaction();
            if (fill == null) {
                String ok = resp.getOrderCancelTransaction() != null
                        ? String.valueOf(resp.getOrderCancelTransaction().getReason()) : "ismeretlen ok";
                model.addAttribute("hiba", "A megbízást az Oanda nem teljesítette: " + ok);
            } else {
                String tradeId = fill.getTradeOpened() != null
                        ? fill.getTradeOpened().getTradeID().toString() : fill.getId().toString();
                model.addAttribute("tradeId", tradeId);
                model.addAttribute("irany", mennyiseg > 0 ? "Long (vétel)" : "Short (eladás)");
                model.addAttribute("ar", fill.getPrice());
            }
        } catch (Exception e) {
            model.addAttribute("hiba", OandaService.hibauzenet(e));
        }
        return "forex/nyit";
    }
}
