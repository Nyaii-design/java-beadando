package hu.nje.beadando.forex;

import com.oanda.v20.trade.Trade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/** Forex-Poz menü: nyitott pozíciók táblázatban. */
@Controller
public class ForexPozController {

    private final OandaService oanda;

    public ForexPozController(OandaService oanda) {
        this.oanda = oanda;
    }

    @GetMapping("/forex/poz")
    public String poziciok(Model model) {
        try {
            List<Trade> tradek = oanda.ctx().trade.listOpen(oanda.accountId()).getTrades();
            model.addAttribute("tradek", tradek);
        } catch (Exception e) {
            model.addAttribute("hiba", OandaService.hibauzenet(e));
        }
        return "forex/poz";
    }
}
