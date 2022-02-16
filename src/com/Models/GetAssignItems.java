package com.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GetAssignItems {
    private StringProperty  ID, Category, SubItem;

    public GetAssignItems(){}

    public GetAssignItems(String subItem, String ID, String Category){
        this.Category = new SimpleStringProperty(Category);
        this.ID = new SimpleStringProperty(ID);
        this.SubItem = new SimpleStringProperty(subItem);
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
