package hu.nje.beadando.forex;

import com.oanda.v20.trade.TradeCloseRequest;
import com.oanda.v20.trade.TradeCloseResponse;
import com.oanda.v20.trade.TradeSpecifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Forex-Zár menü: pozíció zárása piaci áron tradeId alapján. */
@Controller
public class ForexZarController {

    private final OandaService oanda;

    public ForexZarController(OandaService oanda) {
        this.oanda = oanda;
    }

    @GetMapping("/forex/zar")
    public String urlap() {
        return "forex/zar";
    }

    @PostMapping("/forex/zar")
    public String zaras(@RequestParam long tradeId, Model model) {
        model.addAttribute("tradeId", tradeId);
        try {
            TradeCloseRequest request = new TradeCloseRequest(oanda.accountId(), new TradeSpecifier(String.valueOf(tradeId)));
            TradeCloseResponse resp = oanda.ctx().trade.close(request);
            model.addAttribute("siker", true);
            if (resp.getOrderFillTransaction() != null) {
                model.addAttribute("ar", resp.getOrderFillTransaction().getPrice());
                model.addAttribute("pl", resp.getOrderFillTransaction().getPl());
            }
        } catch (Exception e) {
            model.addAttribute("hiba", OandaService.hibauzenet(e));
        }
        return "forex/zar";
    }
}
