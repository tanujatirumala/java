/**********************************************************
 * 														  *
 * CSCI 502     Assignment 7    Summer 2023			      *
 * 														  *
 * Developers: Laxmi Tanuja Thirumala           	      *
 *                                                        *
 * 														  *
 * Due Date: Thursday, August 10, 2023 		11:59 PM      *
 * 														  *
 * Purpose: Program to handle action events when buttons  *
 *          pressed.          			                  *
 * 														  *
 **********************************************************/
package com.example.demojavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.application.Platform;

public class SortAnimationController
{
    @FXML
    private ChoiceBox<String> algorithmOne;    // Choice box to select algorithm for left pane

    @FXML
    private ChoiceBox<String> algorithmTwo;    // choice box to select algorithm for right pane
    @FXML
    private Button populate;                   // Button to populate the area with random values

    @FXML
    private Button Sort;                      // Button to sort the array

    @FXML
    private Pane area1;                       // Left pane area

    @FXML
    private Pane area2;                       // Right pane area

    @FXML
    private ChoiceBox<String> SpeedSelector;  //Choice box to select the speed

    private SortAnimation sortAnimation1 ; // Initialize the instance
    private SortAnimation sortAnimation2 ;  //Initialize  the instance
    private boolean sortInProgress = false;  // flag to check if sorting is inprogress
    private Thread thread1;                 // To declare thread1
    private Thread thread2;                 // To declare thread2

    @FXML
    public void initialize()
    {
        // Initialise  the choice boxes in GUI
        algorithmOne.getItems().addAll("Quick Sort", "Selection Sort", "Insertion Sort");
        algorithmTwo.getItems().addAll("Quick Sort", "Selection Sort", "Insertion Sort");
        SpeedSelector.getItems().addAll("Fast", "Medium", "Slow");
        // Instantiation od sort Animation
        sortAnimation1 = new SortAnimation();
        sortAnimation2 = new SortAnimation();
        Sort.setDisable(true);
    }

    // Method to handle the action event when populate button pressed
    @FXML
    private void onPopulatePressed()
    {
        sortAnimation1.setArea(area1); // Set the Pane area for SortAnimation1
        sortAnimation2.setArea(area2); // Set the pane area for SortAnimation2
        int[] array1 = sortAnimation1.populateArray(area1); // Generate the random array for area1
        int[] array2 = sortAnimation2.populateArray(area2);  // Generate the random array for area2
        sortAnimation1.setArray(array1);   // Set the area for SortAnimation1
        sortAnimation2.setArray(array2);   // Set the area for SortAnimation2
        sortAnimation1.drawVerticalLines(array1, area1); // to draw vertical lines in area1
        sortAnimation2.drawVerticalLines(array2, area2);  // to draw vertical lines in area2
        populate.setDisable(true); // to disable populate button
        Sort.setDisable(false);
    }


    // Method to handle the action event when sort button pressed
    @FXML
    private void onSortButtonPressed()
    {
        if (!sortInProgress)
        {
            // get user input from UI
            String algorithm1 = algorithmOne.getValue();
            String algorithm2 = algorithmTwo.getValue();
            String speed = SpeedSelector.getValue();
            // To set values
            sortAnimation1.setAlgorithm(algorithm1);
            sortAnimation2.setAlgorithm(algorithm2);
            sortAnimation1.setAnimationSpeed(speed);
            sortAnimation2.setAnimationSpeed(speed);
            // To disable choice boxes after setting the values
            algorithmOne.setDisable(true);
            algorithmTwo.setDisable(true);
            SpeedSelector.setDisable(true);
            // To Start sorting in both areas independently based on the chosen algorithms
            thread1 = new Thread(sortAnimation1);
            thread2 = new Thread(sortAnimation2);
            thread1.setDaemon(true);
            thread2.setDaemon(true);
            thread1.start();
            thread2.start();
            sortInProgress = true;
            Sort.setText("Pause");
            new Thread(() -> {
                try {
                    thread1.join();
                    thread2.join();
                    Platform.runLater(() -> {
                        sortInProgress = false;
                        Sort.setText("Sort");
                        // To enable choice boxes and button after sorting process
                        populate.setDisable(false);
                        algorithmOne.setDisable(false);
                        algorithmTwo.setDisable(false);
                        SpeedSelector.setDisable(false);
                        Sort.setDisable(true);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        else
        {
           if (sortAnimation1.isSortingPaused() || sortAnimation2.isSortingPaused())
           {
               Sort.setText("Pause");    // To change button text to pause
               sortAnimation1.resumeSorting();  //resume sorting
               sortAnimation2.resumeSorting();
           }
           else
           {
               Sort.setText("Resume");    // To set button text to Resume
               sortAnimation1.toPauseSorting();   //pause sorting
               sortAnimation2.toPauseSorting();

           }
        }
    }


}


