package de.limod.portals;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author q381723
 */
public class EbayKleinanzeigen extends Portal {

    private static final String HOST = "https://www.ebay-kleinanzeigen.de/";
    private static final String SELECTOR_RESULTS = "li.ad-listitem.lazyload-item";
    private static String SELECTOR_TITLE = "section.aditem-main h2 a";
    private static String SELECTOR_CREATED = "section.aditem-addon";
    private static String SELECTOR_PRICE = "section.aditem-details strong";
    private static String SELECTOR_LINK = "section.aditem-main h2 a";

    public EbayKleinanzeigen(int maxHits) {
        super(maxHits);
    }

    public EbayKleinanzeigen(int maxHits, String query) {
        super(maxHits, query);
    }

    @Override
    public List<Car> getCars() {
        Document page = super.getPage("EbayKleinanzeigen", EbayKleinanzeigen.HOST, this.getQuery());

        Elements results = page.select(EbayKleinanzeigen.SELECTOR_RESULTS);
//
        List<Car> cars = new ArrayList<>();
//
        for (Element result : results) {
            String title = this.getTitle(result);
            String created = this.getCreated(result);
            String price = this.getPrice(result);
            String url = this.getUrl(result);
            String id = this.getID(url);
            Car c = new Car(title, created, price, url, "EbayKleinanzeigen", id);
            c.setFound(new Date());
            cars.add(c);
            if (cars.size() > this.getMaxHits()) {
                break;
            }
        }
        return cars;
    }

    private String getID(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    private String getTitle(Element result) {
        Elements t = result.select(EbayKleinanzeigen.SELECTOR_TITLE);
        String title = t.text();
        return title;
    }

    private String getCreated(Element result) {
        Elements t = result.select(EbayKleinanzeigen.SELECTOR_CREATED);
        return t.text();
    }

    private String getPrice(Element result) {
        Elements t = result.select(EbayKleinanzeigen.SELECTOR_PRICE);
        return t.text();
    }

    private String getUrl(Element result) {
        Elements t = result.select(EbayKleinanzeigen.SELECTOR_LINK);
        String attr = t.attr("href").replaceFirst("/", "");
        return EbayKleinanzeigen.HOST + attr;
    }

}
