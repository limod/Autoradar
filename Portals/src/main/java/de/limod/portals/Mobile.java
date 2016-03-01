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
public class Mobile extends Portal {

    private static String HOST = "http://suchen.mobile.de/fahrzeuge/search.html";
    private static String SELECTOR_RESULTS = "div.cBox-body.cBox-body--resultitem";
    private static String SELECTOR_TITLE = "div.headline-block span.h3";
    private static String SELECTOR_CREATED = "div.headline-block span.rbt-onlineSince";
    private static String SELECTOR_PRICE = "div.price-block span.h3";
    private static String SELECTOR_LINK = "a.link--muted.no--text--decoration";
    private static String SELECTOR_ID = "div.parking-block";

    public Mobile(String query) {
        super.setQuery(query);
    }

    @Override
    public List<Car> getCars() {
        Document page = super.getPage("Mobile", Mobile.HOST, this.getQuery());

        Elements results = page.select(Mobile.SELECTOR_RESULTS);

        List<Car> cars = new ArrayList<>();

        for (Element result : results) {
            String title = this.getTitle(result);
            String created = this.getCreated(result);
            String price = this.getPrice(result);
            String url = this.getUrl(result);
            String id = this.getID(result);
            Car c = new Car(title, created, price, url, "Mobile", id);
            c.setFound(new Date());
            cars.add(c);
            if(cars.size() > 4){
                break;
            }
        }
        return cars;
    }

    private String getID(Element result) {
        Elements t = result.select(Mobile.SELECTOR_ID);
        String id = t.attr("data-parking");
        return id;
    }

    private String getTitle(Element result) {
        Elements t = result.select(Mobile.SELECTOR_TITLE);
        String title = t.text();
        return title;
    }

    private String getCreated(Element result) {
        Elements t = result.select(Mobile.SELECTOR_CREATED);
        return t.text().substring(20,t.text().length());
    }

    private String getPrice(Element result) {
        Elements t = result.select(Mobile.SELECTOR_PRICE);
        return t.text();
    }

    private String getUrl(Element result) {
        Elements t = result.select(Mobile.SELECTOR_LINK);
        String attr = t.attr("href");
        return attr;
    }
}
