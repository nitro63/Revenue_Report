package com.Models;

import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class ActiveRate {

    String id, rateNameId;
    LocalDateTime dateCreated, activationDate, expireDate;
    double rateValue;

    public String getId(){
        return this.id;
    }

    public void setId(String Id){
        this.id = Id;
    }
}
