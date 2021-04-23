/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Gets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author HP
 */
public class GetRevCenter {
    private final StringProperty RevCenter = new SimpleStringProperty();
    private final StringProperty CenterID = new SimpleStringProperty();

    public String getCenterID() {
        return CenterID.get();
    }

    public StringProperty centerIDProperty() {
        return CenterID;
    }

    public void setCenterID(String centerID) {
        this.CenterID.set(centerID);
    }

    public StringProperty RevCenterProperty() {
        return RevCenter;
    }
    
    public final String getRevCenter(){
        return RevCenterProperty().get();
    }

    public final void setRevCenter(String RevCenter) {
        RevCenterProperty().set(RevCenter);
    }
//    
//    public ObjectProperty displayedViewProperty() {
//        return this.node;
//    }
//    
//    public final void setVBox(VBox node){
//    displayedViewProperty().set(node);
//    }
    
//     public final VBox getVBox(){
//        return displayedViewProperty().get();
//    }
//    

    
        
}
