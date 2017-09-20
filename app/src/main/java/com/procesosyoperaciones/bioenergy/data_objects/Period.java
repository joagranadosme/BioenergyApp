package com.procesosyoperaciones.bioenergy.data_objects;

import java.io.Serializable;

/**
 * Created by Jonathan on 9/19/2017.
 */

public class Period implements Serializable {

    private int id;
    private int proposed;
    private int reached;
    private String compromise;

    public Period(){
        this.proposed = 0;
        this.reached = 0;
    }

    public Period(int proposed){
        this.proposed = proposed;
    }

    public Period(int id, int proposed, int reached, String compromise) {
        this.id = id;
        this.proposed = proposed;
        this.reached = reached;
        this.compromise = compromise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProposed() {
        return proposed;
    }

    public void setProposed(int proposed) {
        this.proposed = proposed;
    }

    public int getReached() {
        return reached;
    }

    public void setReached(int reached) {
        this.reached = reached;
    }

    public String getCompromise() {
        return compromise;
    }

    public void setCompromise(String compromise) {
        this.compromise = compromise;
    }
}
