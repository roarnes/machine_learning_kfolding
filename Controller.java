/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iris;

import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.lang.Math;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;

/**
 *
 * @author arnesrespati
 */
public class Controller {
//    private ArrayList<Double> sepal_length;
//    private ArrayList<Double> sepal_width;
//    private ArrayList<Double> petal_length;
//    private ArrayList<Double> petal_width;

    private Double learningRate;

    private Data data1 = new Data();
    private Data data2 = new Data();
    private Iris main;

    private TrainingData trainingData;

    private Double[] train_acc, train_err1, train_err2;
    private Double[] val_acc, val_err1, val_err2;
    private Double[] k_err, k_acc;
    private int [] indices = new int[120];
    private int [] test_indices = new int[120];


    public Controller(Iris main) {
        this.main = main;
    }

    public void setTrainingData(TrainingData trainingData) {
        this.trainingData = trainingData;
    }

    public Double[] getTrain_acc() {
        return this.train_acc;
    }

    public Double[] getTrain_err1() {
        return train_err1;
    }

    public Double[] getTrain_err2() {
        return train_err2;
    }

    public Double[] getVal_acc() {
        return this.val_acc;
    }

    public Double[] getVal_err1() {
        return val_err1;
    }

    public Double[] getVal_err2() {
        return val_err2;
    }

    public Double[] getKAcc() {
        return this.k_acc;
    }

    public Double[] getKError() {
        return this.k_err;
    }

    public void classify(int epoch, Double learningRate, int kf, int valdat) {
        this.learningRate = learningRate;

        train_acc = new Double[epoch];
        train_err1 = new Double[epoch];
        train_err2 = new Double[epoch];

        k_err = new Double[epoch];
        k_acc = new Double[epoch];

        val_acc = new Double[epoch];
        val_err1 = new Double[epoch];
        val_err2 = new Double[epoch];


        //give random values for the first iteration to theta and bias
        randomize();

        //CATEGORY
        //00 -> setosa
        //01 -> versicolor
        //10 -> virginica
        Double[] cat = {0.0, 0.0, 1.0}, cate = {0.0, 0.1, 0.0};
        data1.setCategory(cat);
        data2.setCategory(cate);

        int tot = trainingData.getNoTrainingData() - valdat;

        //k-iteration
        for (int k = 0; k < kf; k++) {
            switch (k){
                case 0:
                    assignTrainingDataIndices(0);
                    break;
                case 1:
                    assignTrainingDataIndices(30);
                    break;
                case 2:
                    assignTrainingDataIndices(60);
                    break;
                case 3:
                    assignTrainingDataIndices(90);
                    break;
                case 4:
                    assignTrainingDataIndices(120);
                    break;
            }

            //epoch iteration for training
            TrainData(epoch, tot, valdat);
//
//            //calculating error
//            k_err[k] = val_err1[k] + val_err2[k];
//
//            //error normalization
//            k_err[k] = Math.log10(k_err[k]);
//            System.out.println("Fold " + (k+1) + " error : " + k_err[k]);
//
//            //calculating accuracy
//            k_acc[k] = val_acc[k];
//
//            //accuracy normalization
//            k_acc[k] = Math.log10(k_acc[k]);
//            System.out.println("Fold " + (k+1) + " accuracy: " + k_acc[k]);

            //normalize data
            //training
//            NormalizeResult(train_acc);
            NormalizeResult(train_err1); NormalizeResult(train_err2);
            //validation
//            NormalizeResult(val_acc);
            NormalizeResult(val_err1); NormalizeResult(val_err2);

            //making chart
            initAccuracyChart(k);
            initErrorChart(k);
        }
    }

    public void initAccuracyChart(int fold){
        Stage stage = new Stage();
        stage.setTitle("Accuracy Chart Fold " + (fold+1));
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Epoch");
        yAxis.setLabel("Accuracy");

        final LineChart<String,Number> lineChart =
                new LineChart<String,Number>(xAxis,yAxis);

        lineChart.setTitle("Accuracy Chart (in %)");

        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();

        series1.setName("Training Accuracy");
        series2.setName("Cross-Validation Accuracy");

        for (int i = 0; i < 100 ; i++) {
            Double trainAc = train_acc[i] * 100;
            Double valAc = val_acc[i] * 100;

            series1.getData().add(new XYChart.Data(String.valueOf(i+1), trainAc));
            series2.getData().add(new XYChart.Data(String.valueOf(i+1), valAc));

            System.out.println("SERIES 2: " + series2.getData());
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().addAll(series1, series2);

        stage.setScene(scene);
        stage.show();
    }

    public void initErrorChart(int fold){
        Stage stage = new Stage();
        stage.setTitle("Error Chart Fold " + (fold+1));
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Epoch");
        yAxis.setLabel("Error");

        final LineChart<String,Number> lineChart =
                new LineChart<String,Number>(xAxis,yAxis);

        lineChart.setTitle("Error Chart");

        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();

        series1.setName("Training Error");
        series2.setName("Cross-Validation Error");

        for (int i = 0; i < 100 ; i++) {
            Double trainErr = getTrain_err1()[i] + getTrain_err2()[i];
            Double valErr = getVal_err1()[i] + getVal_err2()[i];

            series1.getData().add(new XYChart.Data(String.valueOf(i+1), trainErr));
            series2.getData().add(new XYChart.Data(String.valueOf(i+1), valErr));
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().addAll(series1, series2);

        stage.setScene(scene);
        stage.show();
    }

    public void assignTrainingDataIndices(int start){
        for (int i = 0; i < 120 ; i++) {
            indices[i] = start + i;
            if (indices[i] >= 150) {
                indices[i] -= 150;
            }
        }
        assignTestDataIndices(indices);
    }

    public void assignTestDataIndices (int [] trainIndices){
        for (int i = 0; i < 5; i++){
            if (i != trainIndices[i]){
                test_indices[i] = i;
            }
        }
    }

    public void randomize() {
        Double[] theta1 = new Double[4];
        Double[] theta2 = new Double[4];

        for (int i = 0; i < 4; i++) {
            theta1[i] = Math.random();
            theta2[i] = Math.random();
            System.out.println(theta1[i]);
            System.out.println(theta2[i]);
        }
        data1.setTheta(theta1);
        data2.setTheta(theta2);
        data1.setBias(Math.random());
        data2.setBias(Math.random());
    }

    public Double findTarget(Double[] theta, Double bias, int lineIndex) {
        Double target = 0.0;
        target = bias;

        target += (theta[0] * trainingData.getSepal_length().get(lineIndex)) + (theta[1] * trainingData.getSepal_width().get(lineIndex))
                + (theta[2] * trainingData.getPetal_length().get(lineIndex)) + (theta[3] * trainingData.getPetal_width().get(lineIndex));

        return target;
    }

    public Double findSigmoid(Double target) {
        return (1 / (1 + Math.exp(-target)));
    }

    public Double findPrediction(Double sigmoid) {
        Double prediction;
        if (sigmoid < 0.5) {
            prediction = 0.0;
        } else prediction = 1.0;

        return prediction;
    }

    //category-sigmoid

    public Double findError(int index, Double[] cat, Double sigmoid) {
        Double error = 0.0;

        if (index < 50) {
            error = Math.pow(Math.abs(cat[0] - sigmoid), 2);
        } else if (index > 100) {
            error = Math.pow(Math.abs(cat[2] - sigmoid), 2);
        } else {
            error = Math.pow(Math.abs(cat[1] - sigmoid), 2);
        }
        return error/2; //divided by two to ease calculation for derivation
    }

    public Double[] updateTheta(Double[] thetaOld, Double[] d_theta) {
        Double[] thetaNew = new Double[4];
        for (int i = 0; i < 4; i++) {
            thetaNew[i] = thetaOld[i] - (learningRate * d_theta[i]);
        }

        return thetaNew;
    }

    public Double updateBias(Double biasOld, Double d_bias) {
        Double biasNew = 0.0;

        biasNew = biasOld - (learningRate * d_bias);

        return biasNew;
    }

    public Double[] findDTheta(int lineIndex, Double sigmoid, Double[] cat) {
        Double[] d_theta = new Double[4];
        Double category = 0.0;

        if (trainingData.getIris_class().get(lineIndex).equals("Iris-setosa")) {
            category = cat[0];
        }
        if (trainingData.getIris_class().get(lineIndex).equals("Iris-versicolor")) {
            category = cat[1];
        }
        if (trainingData.getIris_class().get(lineIndex).equals("Iris-virginica")) {
            category = cat[2];
        }

        d_theta[0] = 2 * (sigmoid - category) * (1 - sigmoid) * sigmoid * trainingData.getSepal_length().get(lineIndex);
        d_theta[1] = 2 * (sigmoid - category) * (1 - sigmoid) * sigmoid * trainingData.getSepal_width().get(lineIndex);
        d_theta[2] = 2 * (sigmoid - category) * (1 - sigmoid) * sigmoid * trainingData.getPetal_length().get(lineIndex);
        d_theta[3] = 2 * (sigmoid - category) * (1 - sigmoid) * sigmoid * trainingData.getPetal_width().get(lineIndex);

        return d_theta;
    }

    public Double findDBias(int lineIndex, Double sigmoid, Double[] cat) {
        Double d_bias;

        Double category = 0.0;

        if (trainingData.getIris_class().get(lineIndex).equals("Iris-setosa")) {
            category = cat[0];
        }
        if (trainingData.getIris_class().get(lineIndex).equals("Iris-versicolor")) {
            category = cat[1];
        }
        if (trainingData.getIris_class().get(lineIndex).equals("Iris-virginica")) {
            category = cat[2];
        }

        d_bias = 2 * (sigmoid - category) * (1 - sigmoid) * sigmoid;

        return d_bias;
    }

    //PREDICTION/150
    public int findAccuracy(int lineIndex, Double[] cat1, Double[] cat2, Double prediction1, Double prediction2) {
        int counter = 0;

        if (lineIndex < 50) {
            if (Objects.equals(cat1[0], prediction1) && Objects.equals(cat2[0], prediction2)) {
                counter = 1;
            }
        } else if (lineIndex > 100) {
            if (Objects.equals(cat1[2], prediction1) && Objects.equals(cat2[0], prediction2)) {
                counter = 1;
            }
        } else {
            if (Objects.equals(cat1[1], prediction1) && Objects.equals(cat2[0], prediction2)) {
                counter = 1;
            }
        }
        return counter;
    }


    public void TrainData(int epoch, int tot, int valdat){
        System.out.println("---TRAINING---");
        for (int i = 0; i < epoch; i++) {
            System.out.println("EPOCH: " + (i + 1));
            //read per line
            //save the last value of theta and bias

            for (int j = 0; j < tot; j++) {


                //FINDING TARGET
                data1.setTarget(findTarget(data1.getTheta(), data1.getBias(), indices[j]));
                data2.setTarget(findTarget(data2.getTheta(), data2.getBias(), indices[j]));

                //FINDING SIGMOID
                data1.setSigmoid(findSigmoid(data1.getTarget()));
                data2.setSigmoid(findSigmoid(data2.getTarget()));

                //FIND D THETA
                data1.setD_theta(findDTheta(indices[j], data1.getSigmoid(), data1.getCategory()));
                data2.setD_theta(findDTheta(indices[j], data2.getSigmoid(), data2.getCategory()));

                //FINDING D BIAS
                data1.setD_bias(findDBias(indices[j], data1.getSigmoid(), data1.getCategory()));
                data2.setD_bias(findDBias(indices[j], data2.getSigmoid(), data2.getCategory()));

                //update theta for next iteration
                data1.setTheta(updateTheta(data1.getTheta(), data1.getD_theta()));
                data2.setTheta(updateTheta(data2.getTheta(), data2.getD_theta()));

                //update bias for next iteration
                data1.setBias(updateBias(data1.getBias(), data1.getD_bias()));
                data2.setBias(updateBias(data2.getBias(), data2.getD_bias()));

                //FINDING PREDICTION
                data1.setPrediction(findPrediction(data1.getSigmoid()));
                data2.setPrediction(findPrediction(data2.getSigmoid()));

                //CHECK WHETHER PREDICTION IS CORRECT
                data1.setAccuracy(findAccuracy(indices[j], data1.getCategory(), data2.getCategory(), data1.getPrediction(), data2.getPrediction()));
                data2.setAccuracy(findAccuracy(indices[j], data1.getCategory(), data2.getCategory(), data1.getPrediction(), data2.getPrediction()));


                //FINDING ERROR
                Double error1 = findError(indices[j], data1.getCategory(), data1.getSigmoid());
                Double error2 = findError(indices[j], data2.getCategory(), data2.getSigmoid());
                data1.setTotalError(error1);
                data2.setTotalError(error2);
            }
            //SAVING AND PRINTING ERROR OF EACH EPOCH
            train_err1[i] = data1.getError() / tot;
            train_err2[i] = data2.getError() / tot;

            System.out.println("ERROR 1: " + (train_err1[i]));
            System.out.println("ERROR 2: " + (train_err2[i]));

            //SAVING AND PRINTING ACCURACY OF EACH EPOCH
            train_acc[i] = data1.getAccuracy() / tot;

            System.out.println("ACCURACY : " + (train_acc[i]));
            System.out.println("_____________________________________________");

            data1.resetError();
            data2.resetError();
            data1.resetCorrectPrediction();
            data2.resetCorrectPrediction();

            ValidationData(valdat, i);
        }
    }

    public void ValidationData(int valdat, int epoch){
        System.out.println("---VALIDATING---");
        Double target1, target2;
        Double sigmoid1, sigmoid2;
        Double prediction1, prediction2;
        Double error1 = 0.0, error2 = 0.0;
        Double accuracy = 0.0;

        for (int j = 0; j < valdat; j++) {
            //FINDING TARGET -> not updating theta and bias
            target1 = findTarget(data1.getTheta(), data1.getBias(), test_indices[j]);
            target2 = findTarget(data2.getTheta(), data2.getBias(), test_indices[j]);

            //FINDING SIGMOID
            sigmoid1 = findSigmoid(target1);
            sigmoid2 = findSigmoid(target2);

            //FINDING PREDICTION
            prediction1 = findPrediction(sigmoid1);
            prediction2 = findPrediction(sigmoid2);

            //CHECK WHETHER PREDICTION IS CORRECT
            accuracy += findAccuracy(test_indices[j], data1.getCategory(), data2.getCategory(), prediction1, prediction2);

            //FINDING ERROR
            error1 += findError(test_indices[j], data1.getCategory(), sigmoid1);
            error2 += findError(test_indices[j], data2.getCategory(), sigmoid2);
        }
        //SAVING AND PRINTING ERROR OF EACH EPOCH
        val_err1[epoch] = error1/valdat;
        val_err2[epoch] = error2/valdat;

        System.out.println("ERROR 1: " + val_err1[epoch]);
        System.out.println("ERROR 2: " + val_err2[epoch]);

        //SAVING AND PRINTING ACCURACY OF EACH EPOCH
        val_acc[epoch] = accuracy/valdat;

        System.out.println("ACCURACY : " + val_acc[epoch]);
        System.out.println("_____________________________________________");

    }

    //MEAN NORMALIZATION
    public void NormalizeResult(Double[] data){
        Double min = Collections.min(Arrays.asList(data));
        Double max = Collections.max(Arrays.asList(data));

        Double sum = 0.0;

        for (Double i : data){
            sum += i;
        }

        Double avg = sum/(data.length);

        for (int i = 0; i < data.length ; i++) {
            data[i] = (data[i] - min) / (max - min);
        }
    }

//    public Double[] ConfusionMatrix(Double prediction, Double [] target) {
//        Double [] confusionMatrix = new Double [4];
//
//        //confusionMatrix indices:
//        //0 -> true positive
//        //1 -> false positive
//        //2 -> false negative
//        //3 -> true negative
//
//        if (prediction == 1.0 && target == 1.0){
//            confusionMatrix[0] += 1;
//        }
//        if (prediction == 1.0 && target == 0.0){
//            confusionMatrix[1] += 1;
//        }
//        if (prediction == 0.0 && target == 1.0){
//            confusionMatrix[2] += 1;
//        }
//        if (prediction == 0.0 && target == 0.0){
//            confusionMatrix[3] += 1;
//        }
//        return confusionMatrix;
//    }


//    public Double findAccuracy (Double prediction, Double [] target, int lineIndex){
//        Double[] cm = ConfusionMatrix(prediction, target);
//        Double acc = 0.0;
//        }
//

}