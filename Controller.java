/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iris;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Objects;

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

    private Double[] acc1, err1, err2;
    private Double[] k_err1, k_err2, k_acc1, k_acc2;
    int [] indices = new int[120];


    public Controller(Iris main) {
        this.main = main;
    }

    public void setTrainingData(TrainingData trainingData) {
        this.trainingData = trainingData;
    }

    public void classify(int epoch, Double learningRate, int kf, int valdat) {
        this.learningRate = learningRate;

        acc1 = new Double[epoch];
        err1 = new Double[epoch];
        err2 = new Double[epoch];

        k_err1 = new Double[kf];
        k_err2 = new Double[kf];
        k_acc1 = new Double[kf];
        k_acc2= new Double[kf];



        //give random values for the first iteration to theta and bias
        randomize();

        //Manual theta and bias input for testing
//        Double[] theta1 = {0.8478181837, 0.9697483372, 0.1190197063, 0.6618576636};
//        Double[] theta2 = {0.03877398397, 0.392117899, 0.1377062965, 0.7458124254};
//        Double bias1 = 0.5931691469;
//        Double bias2 = 0.1100806592;

//        data1.setTheta(theta1);
//        data2.setTheta(theta2);
//        data1.setBias(bias1);
//        data2.setBias(bias2);

        //CATEGORY
        //00 -> setosa
        //01 -> versicolor
        //10 -> virginica
        Double[] cat = {0.0, 0.0, 1.0}, cate = {0.0, 0.1, 0.0};
        data1.setCategory(cat);
        data2.setCategory(cate);

        int tot = trainingData.getNoTrainingData() - valdat;

        //k iteration
        for (int k = 0; k < kf; k++) {
            switch (k){
                case 0:
                    ind(0);
                    break;
                case 1:
                    ind(30);
                    break;
                case 2:
                    ind(60);
                    break;
                case 3:
                    ind(90);
                    break;
                case 4:
                    ind(120);
                    break;
            }

            //epoch iteration
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
                err1[i] = data1.getError() / tot;
                err2[i] = data2.getError() / tot;
                System.out.println("ERROR 1: " + (err1[i]));
                System.out.println("ERROR 2: " + (err2[i]));

                //SAVING AND PRINTING ACCURACY OF EACH EPOCH
                acc1[i] = data1.getAccuracy() / tot;
//              acc2[i] = data2.getAccuracy()/sepal_width.size();
                System.out.println("ACCURACY : " + (acc1[i]));
                System.out.println("_____________________________________________");

                data1.resetError();
                data2.resetError();
                data1.resetCorrectPrediction();
                data2.resetCorrectPrediction();
            }
            k_err1[k] = err1[epoch-1];
            k_err2[k] = err2[epoch-1];

            System.out.println("K " + k + " error 1: " + k_err1[k]);
            System.out.println("K " + k + " error 2: " + k_err2[k]);

            k_acc1[k] = acc1[epoch-1];

            System.out.println("K " + k + " accuracy: " + k_acc1[k]);


        }
    }

    public void ind(int start){
        for (int i = 0; i < 120 ; i++) {
            indices[i] = start + i;
            if (indices[i] >= 150) {
                indices[i] -= 150;
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
//            System.out.println("in if: " + 1);
        } else if (index > 100) {
            error = Math.pow(Math.abs(cat[2] - sigmoid), 2);
//            System.out.println("in if: " + 2);
        } else {
            error = Math.pow(Math.abs(cat[1] - sigmoid), 2);
//            System.out.println("in if: " + 3);
        }
        return error;
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

        if (trainingData.getIris_class().get(lineIndex) == "Iris-setosa") {
            category = cat[0];
        }
        if (trainingData.getIris_class().get(lineIndex) == "Iris-versicolor") {
            category = cat[1];
        }
        if (trainingData.getIris_class().get(lineIndex) == "Iris-virginica") {
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

    public Double[] getAcc1() {
        return this.acc1;
    }

    public Double[] getErr1() {
        return err1;
    }


    public Double[] getErr2() {
        return err2;
    }

    public Double[] getKAcc1() {
        return this.k_acc1;
    }

    public Double[] getKErr1() {
        return k_err1;
    }


    public Double[] getKErr2() {
        return k_err2;
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