/**********************************************************
 * 														  *
 * CSCI 502     Assignment 7    Summer 2023			      *
 * 														  *
 * Developers: Laxmi Tanuja Thirumala           	      *
 *                                                        *
 * 														  *
 * Due Date: Thursday, August 10, 2023 		11:59 PM      *
 * 														  *
 * Purpose: Program having methods to sort array and draw *
 *          Animation.          			              *
 * 														  *
 **********************************************************/
package com.example.demojavafx;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.application.Platform;
import java.util.Random;

public class SortAnimation implements Runnable
{
    private String algorithm;     // To store selected algorithm from UI
    private int[] array;          //To store the populated array
    private Pane area;            //Area in the GUI
    private int animationDelay = 100;  // Set default animation speed
    private boolean pauseSorting = false;   // flag to set if animation is paused or not
    private final Object lock = new Object(); //lock object to resume and pause animation

    // constructor with no arguments
    public SortAnimation() {
    }

    // Method to set algorithm
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    // Method to set area
    public void setArea(Pane area) {
        this.area = area;
    }

    // Method to set populated array
    public void setArray(int[] array) {
        this.array = array;
    }

    // Method to check if sorting is paused or not
    public boolean isSortingPaused()
    {
        return pauseSorting;
    }

    // Method to set the user selected animation speed
    public void setAnimationSpeed(String speed)
    {
        switch (speed) {
            case "Fast":
                animationDelay = 5;
                break;
            case "Medium":
                animationDelay = 50;
                break;
            case "Slow":
                animationDelay = 150;
                break;
            default:
                break;
        }
    }
    @Override
    public void run()
    {
        toSortArray();
    }

    // Method to call user selected sorting algorithm
    private void toSortArray()
    {
        switch (algorithm)
        {
            case "Quick Sort":
                quickSort(array, 0, array.length - 1, area);
                break;
            case "Insertion Sort":
                insertionSort(array, area);
                break;
            case "Selection Sort":
                selectionSort(array, area);
                break;
            default:
                break;

        }
    }

    // Method to populate the random generated array
    public int[] populateArray(Pane area){
        double width = area.getPrefWidth();  // To get the width of the area in UI
        double height = area.getPrefHeight(); // To get the height of the area in UI
        int[] array = new int[(int) width];
        // Create a random number generator
        Random random = new Random();
        //  To fill the array with randomly generated values in the range of 0 to animation Area's Height
        for (int i = 0; i < array.length; i++)
        {
            array[i] = random.nextInt((int) height + 1);
        }
        return array;   // return the populated array
    }

    // Method to draw the vertical lines in the animation area
    public void drawVerticalLines(int[] values, Pane pane)
    {
        Platform.runLater(() ->
        {
            // Clear the previous lines in the pane area
            pane.getChildren().clear();

            // To Calculate the distance between each vertical line
            double distanceBetweenLines = pane.getPrefWidth() / values.length;

            // Draw the vertical lines using the values in the array
            for (int i = 0; i < values.length; i++) {
                double x = i * distanceBetweenLines;
                double y1 = pane.getPrefHeight();
                double y2 = pane.getPrefHeight() - values[i];

                Line line = new Line(x, y1, x, y2);
                line.setStroke(Color.BLUE);
                pane.getChildren().add(line);
            }
        });
        // Delay to visualize the sorting process
        try
        {
            Thread.sleep(animationDelay);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    // Method to implement insertion sort
    private void insertionSort(int[] array, Pane pane)
    {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;

            // Move elements greater than key to one position ahead of their current position
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
                pauseIfNeeded();  // check if pause button is presses and pause the animation
                drawVerticalLines(array, pane);  // to draw current state of the array
            }
            array[j + 1] = key;
        }
    }

    //Method to implement quick sort
    private void quickSort(int[] array, int low, int high, Pane pane) {
        if (low < high) {
            int partitionIndex = partition(array, low, high, pane);
            quickSort(array, low, partitionIndex - 1, pane);
            quickSort(array, partitionIndex + 1, high, pane);
        }
    }

    private  int partition(int[] array, int low, int high, Pane pane) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                swap(array, i, j, pane);
            }
        }

        swap(array, i + 1, high, pane);
        return i + 1;
    }

    private void swap(int[] array, int i, int j, Pane pane)
    {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        pauseIfNeeded();    // check if pause button is presses and pause the animation
        drawVerticalLines(array, pane);   // To draw current state of the array
    }

    // Method to implement selection sort
    private void selectionSort(int[] array, Pane pane)
    {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }

            // Swap the elements
            int temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
            pauseIfNeeded();  // check if pause button is presses and pause the animation
            drawVerticalLines(array, pane); // To draw vertical lines
        }
    }


    // Method to pause the animation
    private void pauseIfNeeded() {
        while (pauseSorting) {
            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to set pause sorting variable
    public void toPauseSorting()
    {
        pauseSorting = true;
    }

    // Method for resume sorting
    public void resumeSorting()
    {
        synchronized (lock)
        {
            pauseSorting = false;    // set variable to false
            lock.notifyAll();        // notify threads
        }
    }

}
