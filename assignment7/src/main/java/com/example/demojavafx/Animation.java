/**********************************************************
 * 														  *
 * CSCI 502     Assignment 7    Summer 2023			      *
 * 														  *
 * Developers: Laxmi Tanuja Thirumala - Z1947594	      *
 *                                                        *
 * 														  *
 * Due Date: Thursday, August 10, 2023 		11:59 PM      *
 * 														  *
 * Purpose: Program to create GUI application for Sorting *
 *          Animation.          			              *
 * 														  *
 **********************************************************/
package com.example.demojavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Animation extends Application{
    public static void main(String[] args)
    {
        // create a object and call its start method
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root =
                FXMLLoader.load(getClass().getResource("/com/example/demojavafx/SortAnimation.fxml"));
        Scene scene = new Scene(root); // attach scene graph to scene
        stage.setTitle("Sorting Animation"); //displayed in window's title
        stage.setScene(scene); // attach scene to stage
        stage.show(); // display the stage
    }
}

