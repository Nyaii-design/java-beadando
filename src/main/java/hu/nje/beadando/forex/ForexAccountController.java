package hu.nje.beadando.forex;

import com.oanda.v20.account.AccountSummary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.Map;

/** Forex-account menü: számlainformációk kiírása. */
@Controller
public class ForexAccountController {

    private final OandaService oanda;

    public ForexAccountController(OandaService oanda) {
        this.oanda = oanda;
    }

    @GetMapping("/forex/account")
    public String account(Model model) {
        try {
            AccountSummary s = oanda.ctx().account.summary(oanda.accountId()).getAccount();
            Map<String, Object> adatok = new LinkedHashMap<>();
            adatok.put("Számlaazonosító", s.getId());
            adatok.put("Alias", s.getAlias());
            adatok.put("Pénznem", s.getCurrency());
            adatok.put("Egyenleg (Balance)", s.getBalance());
            adatok.put("Nettó eszközérték (NAV)", s.getNAV());
            adatok.put("Nem realizált P/L", s.getUnrealizedPL());
            adatok.put("Realizált P/L", s.getPl());
            adatok.put("Felhasznált fedezet", s.getMarginUsed());
            adatok.put("Elérhető fedezet", s.getMarginAvailable());
            adatok.put("Nyitott trade-ek száma", s.getOpenTradeCount());
            adatok.put("Nyitott pozíciók száma", s.getOpenPositionCount());
            adatok.put("Függő megbízások száma", s.getPendingOrderCount());
            adatok.put("Tőkeáttétel (margin rate)", s.getMarginRate());
            adatok.put("Létrehozva", s.getCreatedTime());
            model.addAttribute("adatok", adatok);
        } catch (Exception e) {
            model.addAttribute("hiba", OandaService.hibauzenet(e));
        }
        return "forex/account";
    }
}
