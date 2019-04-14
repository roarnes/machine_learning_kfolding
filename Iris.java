/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iris;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author arnesrespati
 */
public class Iris extends Application {
    
    public ArrayList<Double> sepal_length = new ArrayList<>();
    public ArrayList<Double> sepal_width = new ArrayList<>();
    public ArrayList<Double> petal_length = new ArrayList<>();
    public ArrayList<Double> petal_width = new ArrayList<>();
    public ArrayList<String> iris_class = new ArrayList<>();

    public Controller controller;
    
    public TrainingData trainingData;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Iris main = new Iris();

        String filename = "/Users/arnes/Desktop/0 - SEMESTER 6/Machine Learning/Tugas 4/iris_backprop/src/iris/dataset/iris_random.csv";
        main.readCSV(filename);
        trainingData = new TrainingData();

        trainingData.setSepal_length(main.sepal_length);
        trainingData.setSepal_width(main.sepal_width);
        trainingData.setPetal_width(main.petal_width);
        trainingData.setPetal_length(main.petal_length);
        trainingData.setIris_class(main.iris_class);

        controller = new Controller(main);
        controller.setTrainingData(trainingData);
        controller.classify(100, 0.1, 5, 30);


    }

    private void readCSV(String file){

        //adapted from post by App Shah on July 16th 2017
        // https://crunchify.com/how-to-read-convert-csv-comma-separated-values-file-to-arraylist-in-java-using-split-operation/
        //Retrieved February 28th 2019

        BufferedReader bufferedReader = null;

        try {
            String line;
            bufferedReader = new BufferedReader(new FileReader(file));

            //Read file in java line by line
            while ((line = bufferedReader.readLine()) != null) {
                String [] splitLine = line.split(",");

                sepal_length.add(Double.valueOf(splitLine[0]));
                sepal_width.add(Double.valueOf(splitLine[1]));
                petal_length.add(Double.valueOf(splitLine[2]));
                petal_width.add(Double.valueOf(splitLine[3]));
                iris_class.add((splitLine[4]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException crunchifyException) {
                crunchifyException.printStackTrace();
            }
        }
    }
    
}
