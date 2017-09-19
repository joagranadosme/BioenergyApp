package com.procesosyoperaciones.bioenergy;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Jonathan on 9/16/2017.
 */

public class Boss implements Serializable {

    private String id;
    private String name;
    private String title;
    private String company;
    private int image;

    public Boss(String id, String name, String title, String company, int image){
        this.id = id;
        this.name = name;
        this.title = title;
        this.company = company;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String toString(){
        return  "Boss{" +
                "Id='" + id + '\'' +
                ", Company='" + company + '\'' +
                ", Name='" + name + '\'' +
                ", Title='" + title + '\'' +
                '}';
    }

}
