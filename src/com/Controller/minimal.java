///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.Controller;
//
///**
// *
// * The main java class
// */
//```
//public class main extends Application {
//    @Override
//    public void start(Stage stage) throws Exception{
//        //this is to load the parent (borderpane)
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/fxml/main.fxml"));
//        
//        //this is to create a controller for the parent view
//        loader.setController(new appController());
//        
//        Parent root = loader.load();
//        
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show;
//        
//        /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        launch(args);
//    }
//    
//    }
//    
//}
//```
//
//```
//public class mainController implements Initializable {
//    
//    @FXML
//    protected BorderPane border_pane;
//    // This is a VBox occupying the Centerpane of the Borderpane
//    @FXML
//    protected VBox centerPane;
//
//public mainController(){         
//         }
//     
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        
//                }    
//
//// This loads the layout1 into the left pane of the borderpane from button handler inside the Top pane
//    @FXML
//    private void showLayout1(ActionEvent event) throws IOException {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource( "/Views/fxml/Layout1.fxml"));
//        loader.setController(new Controller1());
//        leftPane.getChildren().clear();
//        leftPane.getChildren().add(loader.load());
//     }
//    
//}
//```
//
//```
//public class Controller1 implements Initializable{
//    private Button btnShow_layout2;
//    private mainController app;
//    
//    @Override
//    public void initialize(URL url, ResourceBundle rb){        
//    }
//    
//    @FXML
//    private void ShowLayout2(ActionEvent event) throws IOException{
//        FXMLLoader load = new FXMLLoader();
//        load.setLocation(getClass().getResource("/Views/fxml/Layout2.fxml"));
//       loads.setController(new Controller2());
//
//// This is returning a null causing a java.lang.NullPointerException
//           app.centerPane.getChildren().clear();      
//           app.centerPane.getChildren().add(load.load());  
//
//        }
//    }
//}
//```
//
//Now when I access the `VBox`, `centerPane` in `mainController` from `Controller1` it gives me a nullpointer error
//So please any help on how to load `layout2` into the `centerPane` from `Controller1`