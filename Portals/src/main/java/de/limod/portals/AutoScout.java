package de.limod.portals;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author q381723
 */
public class AutoScout extends Portal {

    private static final String HOST = "http://fahrzeuge.autoscout24.de/";
    private static final String SELECTOR_RESULTS = "div.details-container";
    private static final String SELECTOR_TITLE = "div.title h2";
    private static final String SELECTOR_CREATED = "div.headline-block span.rbt-onlineSince";
    private static final String SELECTOR_PRICE = "div.items p:nth-child(1)";
    private static final String SELECTOR_LINK = "a.detail";
    private static final String URL = "http://ww3.autoscout24.de/classified/%s";

    public AutoScout(int maxHits) {
        super(maxHits);
    }

    public AutoScout(int maxHits, String query) {
//        super.setQuery(query);
        super(maxHits, query);
    }

    @Override
    public List<Car> getCars() {
        Document page = super.getPage("AutoScout", AutoScout.HOST, this.getQuery());

        String content = page.html();

        // parse json object from request
        Pattern pattern = Pattern.compile("articlesFromServer\\s+=\\s+\\Q[{\\E.*\\Q}]\\E");

        Matcher matcher = pattern.matcher(content);
        List<Car> cars = new ArrayList<>();
        // check  occurance
        if (matcher.find()) {
            String json = matcher.group().trim();
            json = json.substring(json.indexOf("["), json.length());
            JSONArray carsArray = new JSONArray(json);

            // iterate json object
            for (Object carEntry : carsArray) {
                JSONObject obj = (JSONObject) carEntry;
                String title = obj.getString("mk") + " " + obj.getString("vr");
                String price = String.valueOf(obj.getDouble("price_raw"));
                String url = String.format(AutoScout.URL, String.valueOf(obj.getInt("ei")));
                String erstzulassung = obj.getString("fr");
                String kilomter = obj.getString("ma");
                String id = String.valueOf(obj.getInt("ei"));
                Car c = new Car(title, "--", price, url, "AutoScout", id);
                c.setFound(new Date());

                cars.add(c);
                if (cars.size() > this.getMaxHits()) {
                    break;
                }
            }
        }

        return cars;
    }

    private String getTitle(Element result) {
        Elements t = result.select(AutoScout.SELECTOR_TITLE);
        String title = t.text();
        return title;
    }

    private String getCreated(Element result) {
        Elements t = result.select(AutoScout.SELECTOR_CREATED);
        return t.text();
    }

    private String getUrl(Element result) {
        Elements t = result.select(AutoScout.SELECTOR_LINK);
        String attr = t.attr("href");
        return attr;
    }

    private String getPrice(Element result) {
        Elements t = result.select(AutoScout.SELECTOR_PRICE);
        return t.text();
    }

}
