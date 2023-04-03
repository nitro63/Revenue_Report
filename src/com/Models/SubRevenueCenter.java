package com.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SubRevenueCenter {
    private StringProperty id = new SimpleStringProperty(), revenueCenterId = new SimpleStringProperty()
            , revenueCenter = new SimpleStringProperty(), subCenter = new SimpleStringProperty()
            , status = new SimpleStringProperty();

    public SubRevenueCenter(String id, String subCenter) {
        setSubCenter(subCenter);
        setId(id);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getRevenueCenterId() {
        return revenueCenterId.get();
    }

    public StringProperty revenueCenterIdProperty() {
        return revenueCenterId;
    }

    public void setRevenueCenterId(String revenueCenterId) {
        this.revenueCenterId.set(revenueCenterId);
    }

    public String getRevenueCenter() {
        return revenueCenter.get();
    }

    public StringProperty revenueCenterProperty() {
        return revenueCenter;
    }

    public void setRevenueCenter(String revenueCenter) {
        this.revenueCenter.set(revenueCenter);
    }

    public String getSubCenter() {
        return subCenter.get();
    }

    public StringProperty subCenterProperty() {
        return subCenter;
    }

    public void setSubCenter(String subCenter) {
        this.subCenter.set(subCenter);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    }
