package com.procesosyoperaciones.bioenergy.data_objects;

import java.io.Serializable;

/**
 * Created by Jonathan on 9/18/2017.
 */

public class Goal implements Serializable {

    private int id;
    private String perspective;
    private String description;
    private String general;
    private String specific;
    private String formula;
    private String unity;
    private int weight;
    private int type;
    private String period;
    private Period[] periods;

    public Goal() {
        this.perspective = "Nuevo Objetivo";
        this.description = "";
        this.general = "";
        this.specific = "";
        this.weight = 0;
        this.type = 0;
        this.periods = null;
    }

    public Goal(int type){
        this.perspective = "Nuevo Objetivo";
        this.description = "";
        this.general = "";
        this.specific = "";
        this.weight = 0;
        this.type = type;
        this.periods = null;
    }

    public Goal(String perspective, int type){
        this.perspective = perspective;
        this.description = "";
        this.general = "";
        this.specific = "";
        this.weight = 0;
        this.type = type;
        this.periods = null;
    }

    public Goal(int id, String perspective, String description, String general, String specific, String formula, String unity, int weight, int type, String period, Period[] periods) {
        this.id = id;
        this.perspective = perspective;
        this.description = description;
        this.general = general;
        this.specific = specific;
        this.formula = formula;
        this.unity = unity;
        this.weight = weight;
        this.type = type;
        this.period = period;
        this.periods = periods;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPerspective() {
        return perspective;
    }

    public void setPerspective(String perspective) {
        this.perspective = perspective;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeneral() {
        return general;
    }

    public void setGeneral(String general) {
        this.general = general;
    }

    public String getSpecific() {
        return specific;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Period[] getPeriods() {
        return periods;
    }

    public void setPeriods(Period[] periods) {
        this.periods = periods;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
