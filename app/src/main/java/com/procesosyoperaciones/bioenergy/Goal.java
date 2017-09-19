package com.procesosyoperaciones.bioenergy;

import java.io.Serializable;

/**
 * Created by Jonathan on 9/18/2017.
 */

public class Goal implements Serializable {

    String id;
    String description;
    String general;
    String specific;
    String formula;
    String unity;
    int localWeight;
    int globalWeight;

    public Goal() {
        this.description = "Nuevo objetivo";
        this.general = "";
        this.specific = "";
        this.localWeight = 0;
        this.globalWeight = 0;
    }

    public Goal(String id, String description, String general, String specific, String formula, String unity, int localWeight, int globalWeight) {
        this.id = id;
        this.description = description;
        this.general = general;
        this.specific = specific;
        this.formula = formula;
        this.unity = unity;
        this.localWeight = localWeight;
        this.globalWeight = globalWeight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getLocalWeight() {
        return localWeight;
    }

    public void setLocalWeight(int localWeight) {
        this.localWeight = localWeight;
    }

    public int getGlobalWeight() {
        return globalWeight;
    }

    public void setGlobalWeight(int globalWeight) {
        this.globalWeight = globalWeight;
    }
}
