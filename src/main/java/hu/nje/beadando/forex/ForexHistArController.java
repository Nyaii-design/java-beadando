package hu.nje.beadando.forex;

import com.oanda.v20.instrument.Candlestick;
import com.oanda.v20.instrument.CandlestickGranularity;
import com.oanda.v20.instrument.InstrumentCandlesRequest;
import com.oanda.v20.instrument.InstrumentCandlesResponse;
import com.oanda.v20.primitives.InstrumentName;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/** Forex-HistÁr menü: az utolsó 10 historikus ár. */
@Controller
public class ForexHistArController {

    private final OandaService oanda;

    public ForexHistArController(OandaService oanda) {
        this.oanda = oanda;
    }

    @GetMapping("/forex/histar")
    public String urlap(Model model) {
        alapadatok(model, "EUR_USD", "D");
        return "forex/histar";
    }

    @PostMapping("/forex/histar")
    public String lekerdezes(@RequestParam String instrument, @RequestParam String granularity, Model model) {
        alapadatok(model, instrument, granularity);
        try {
            InstrumentCandlesRequest request = new InstrumentCandlesRequest(new InstrumentName(instrument));
            request.setGranularity(CandlestickGranularity.valueOf(granularity));
            request.setCount(10L);
            InstrumentCandlesResponse resp = oanda.ctx().instrument.candles(request);

            List<Gyertya> gyertyak = new ArrayList<>();
            for (Candlestick c : resp.getCandles()) {
                gyertyak.add(new Gyertya(c.getTime().toString(),
                        c.getMid().getO().toString(), c.getMid().getH().toString(),
                        c.getMid().getL().toString(), c.getMid().getC().toString()));
            }
            model.addAttribute("gyertyak", gyertyak);
        } catch (Exception e) {
            model.addAttribute("hiba", OandaService.hibauzenet(e));
        }
        return "forex/histar";
    }

    private void alapadatok(Model model, String instrument, String granularity) {
        model.addAttribute("instrumentumok", OandaService.INSTRUMENTUMOK);
        model.addAttribute("granularityk", OandaService.GRANULARITY);
        model.addAttribute("instrument", instrument);
        model.addAttribute("granularity", granularity);
    }

    public record Gyertya(String ido, String nyito, String max, String min, String zaro) {}
}
