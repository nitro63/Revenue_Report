/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revenue_report;

/**
 *
 * @author NiTrO
 */

    import javafx.application.Application;
import javafx.stage.Stage;

public interface ApplicationRoot {
    void start(Stage stage) throws Exception;
    void stop();
    Application getApplication();
}

