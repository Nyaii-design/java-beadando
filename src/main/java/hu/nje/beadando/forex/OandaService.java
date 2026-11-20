package hu.nje.beadando.forex;

import com.oanda.v20.Context;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountID;
import com.oanda.v20.account.AccountProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/** Közös Oanda-kapcsolat az összes Forex menühöz. */
@Service
public class OandaService {

    /** A lenyíló listákban választható instrumentumok. */
    public static final List<String> INSTRUMENTUMOK = List.of(
            "EUR_USD", "GBP_USD", "USD_JPY", "USD_CHF", "AUD_USD",
            "USD_CAD", "NZD_USD", "EUR_GBP", "EUR_JPY", "EUR_HUF", "USD_HUF");

    /** A HistÁr menü granularity-értékei. */
    public static final List<String> GRANULARITY = List.of(
            "M1", "M5", "M15", "M30", "H1", "H4", "D", "W", "M");

    private final String url;
    private final String token;
    private final String beallitottSzamla;

    private Context ctx;
    private AccountID accountId;

    public OandaService(@Value("${oanda.url}") String url,
                        @Value("${oanda.token}") String token,
                        @Value("${oanda.account-id}") String accountId) {
        this.url = url;
        this.token = token == null ? "" : token.trim();
        this.beallitottSzamla = accountId == null ? "" : accountId.trim();
    }

    public boolean vanToken() {
        return !token.isEmpty();
    }

    public synchronized Context ctx() {
        if (!vanToken()) {
            throw new IllegalStateException(
                    "Nincs beállítva Oanda-token. Tedd a jar mellé az oanda.properties fájlt (lásd README).");
        }
        if (ctx == null) {
            ctx = new Context(url, token);
        }
        return ctx;
    }

    /** A számlaazonosító: a beállított, vagy ha nincs, a tokenhez tartozó első számla. */
    public synchronized AccountID accountId() throws Exception {
        if (accountId == null) {
            if (!beallitottSzamla.isEmpty()) {
                accountId = new AccountID(beallitottSzamla);
            } else {
                List<AccountProperties> szamlak = ctx().account.list().getAccounts();
                if (szamlak.isEmpty()) throw new IllegalStateException("A tokenhez nem tartozik számla.");
                accountId = szamlak.get(0).getId();
            }
        }
        return accountId;
    }

    /** Érthető hibaüzenet az Oanda-hibákból. */
    public static String hibauzenet(Exception e) {
        if (e instanceof RequestException re) {
            return "Oanda hiba (" + re.getStatus() + "): " + re.getErrorMessage();
        }
        return e.getMessage() != null ? e.getMessage() : e.toString();
    }
}
