package com.example.piandroid;

public class Requests {
    String id;
    String title;
    String type;
    String price;
    String sender;
    String receiver;
    String etat;
    String titlechange;


    public Requests() {
    }

    public Requests(String id, String title, String type, String sender, String receiver, String price, String etat,String titlechange) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.price = price;
        this.sender = sender;
        this.receiver = receiver;
        this.etat = etat;
        this.titlechange= titlechange;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getTitleechange() {
        return titlechange;
    }

    public void setTitleechange(String titleechange) {
        this.titlechange = titleechange;
    }
}
