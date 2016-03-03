package de.limod.portals;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author q381723
 */
public abstract class Portal {

    private String query;
    private int maxHits = 10;

    public Portal(int maxHits) {
        this(maxHits, "");
    }

    public Portal(int maxHits, String query) {
        this.query = query;
        this.maxHits = maxHits;
    }

    public abstract List<Car> getCars();

    protected Document getPage(String provider, String host, String query) {
        Document document = null;
        try {

//        Authenticator authenticator = new Authenticator() {//
//            public PasswordAuthentication getPasswordAuthentication() {
//                return (new PasswordAuthentication("q381723",
//                        "1qay2wsx".toCharArray()));
//            }
//        };
//        Authenticator.setDefault(authenticator);
            URL url = new URL(host + query);
//            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.109.190.88", 8080)); // or whatever your proxy is
//            HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();

            String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0"; //"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20120427 Firefox/15.0a1";
            uc.setRequestMethod("GET");
            uc.setRequestProperty("User-Agent", userAgent);
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            uc.connect();

            String line = null;
            StringBuffer tmp = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            PrintWriter writer = new PrintWriter(provider + ".html", "UTF-8");
            while ((line = in.readLine()) != null) {
                tmp.append(line);
                writer.println(line);
            }

            writer.close();

            document = Jsoup.parse(String.valueOf(tmp));
            return document;

//            File input = new File(provider+".html");
//            System.out.println("read file");
//            Document doc = Jsoup.parse(input, "UTF-8", host);
//            return doc;
        } catch (MalformedURLException ex) {
            Logger.getLogger(Portal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Portal.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getMaxHits() {
        return maxHits;
    }

    public void setMaxHits(int maxHits) {
        this.maxHits = maxHits;
    }

}
