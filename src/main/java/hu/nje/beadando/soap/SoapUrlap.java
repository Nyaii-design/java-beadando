package hu.nje.beadando.soap;

/** A SOAP menü űrlapjának adatai. */
public class SoapUrlap {
    private String deviza;
    private String kezdoDatum;
    private String befejezoDatum;

    public String getDeviza() { return deviza; }
    public void setDeviza(String deviza) { this.deviza = deviza; }

    public String getKezdoDatum() { return kezdoDatum; }
    public void setKezdoDatum(String kezdoDatum) { this.kezdoDatum = kezdoDatum; }

    public String getBefejezoDatum() { return befejezoDatum; }
    public void setBefejezoDatum(String befejezoDatum) { this.befejezoDatum = befejezoDatum; }
}
