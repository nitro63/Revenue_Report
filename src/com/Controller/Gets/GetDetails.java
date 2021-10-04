package com.Controller.Gets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GetDetails {
    private StringProperty Center, ID, Category, SubItem;

    public GetDetails(){}

    public GetDetails(String center, String ID, String Category){
        this.Category = new SimpleStringProperty(Category);
        this.ID = new SimpleStringProperty(ID);
        this.Center = new SimpleStringProperty(center);
    }

    public GetDetails(String center,String subItem, String ID, String Category){
        this.Category = new SimpleStringProperty(Category);
        this.SubItem = new SimpleStringProperty(subItem);
        this.ID = new SimpleStringProperty(ID);
        this.Center = new SimpleStringProperty(center);
    }

    public String getSubItem() {
        return SubItem.get();
    }

    public StringProperty subItemProperty() {
        return SubItem;
    }

    public void setSubItem(String subItem) {
        this.SubItem.set(subItem);
    }

    public String getCenter() {
        return Center.get();
    }

    public StringProperty centerProperty() {
        return Center;
    }

    public void setCenter(String center) {
        this.Center.set(center);
    }

    public String getID() {
        return ID.get();
    }

    public StringProperty IDProperty() {
        return ID;
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public String getCategory() {
        return Category.get();
    }

    public StringProperty categoryProperty() {
        return Category;
    }

    public void setCategory(String category) {
        this.Category.set(category);
    }
}
