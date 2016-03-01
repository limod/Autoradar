package de.limod.portals;

import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author q381723
 */
public class Car {

    private String title;
    private String created;
    private String price;
    private String url;
    private String provider;
    private String id;
    private Boolean isNew = false;
    private Date found;

    public Car() {
    }

    public Car(String title) {
        this.title = title;
    }

    public Car(String title, String created) {
        this.title = title;
        this.created = created;
    }

    public Car(String title, String created, String price) {
        this.title = title;
        this.created = created;
        this.price = price;
    }

    public Car(String title, String created, String price, String url, String provider, String id) {
        this.title = title;
        this.created = created;
        this.price = price;
        this.url = url;
        this.provider = provider;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Date getFound() {
        return found;
    }

    public void setFound(Date found) {
        this.found = found;
    }
    
    

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        Car c = (Car) o;

        if (!c.getId().equals(this.getId())) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return String.format("Provider: %s, Titel: %s, Online: %s, Price: %s, Url: %s, ID: %s, isNew: %s", this.getProvider(), this.getTitle(), this.getCreated(), this.getPrice(), this.getUrl(), this.getId(), this.getIsNew());
    }

}
