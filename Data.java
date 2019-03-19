/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iris;

/**
 *
 * @author arnesrespati
 */
public class Data {
    private Double[] theta = new Double[4];
    private Double[] d_theta = new Double[4];

    private Double bias;
    private Double d_bias;

    private Double error = 0.0;
    private Double target;
    private Double sigmoid;
    private Double [] category;

    private Double prediction;

    private Double accuracy = 0.0;

    public Double getK_error() {
        return k_error;
    }

    public void setK_error(Double k_error) {
        this.k_error = k_error;
    }

    public Double getK_acc() {
        return k_acc;
    }

    public void setK_acc(Double k_acc) {
        this.k_acc = k_acc;
    }

    private Double k_error = 0.0;
    private Double k_acc = 0.0;

    public void resetKError(){
        this.k_error = 0.0;
    }

    public void resetKAcc(){
        this.k_acc = 0.0;
    }


    public void setAccuracy(int accuracy) {
        this.accuracy += accuracy;
    }

    public void resetError(){
        this.error = 0.0;
    }

    public void resetCorrectPrediction(){
        this.accuracy = 0.0;
    }

    public void setD_bias(Double d_bias) {
        this.d_bias = d_bias;
    }

    public void setD_theta(Double[] d_theta) {
        this.d_theta = d_theta;
    }

    public void setCategory(Double[] category) {
        this.category = category;
    }

    public void setPrediction(Double prediction) {
        this.prediction = prediction;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public void setSigmoid(Double sigmoid) {
        this.sigmoid = sigmoid;
    }

    public void setTotalError(Double error){
        this.error += error;
    }

    public void setTheta (Double[] theta){
        this.theta = theta;
    }

    public void setBias (Double bias){
        this.bias = bias;
    }

    public Double[] getTheta(){
        return this.theta;
    }

    public Double getError() {
        return this.error;
    }

    public Double getBias() {
        return bias;
    }

    public Double getTarget() {
        return target;
    }

    public Double [] getCategory (){
        return this.category;
    }

    public Double getSigmoid() {
        return sigmoid;
    }

    public Double getD_bias() {
        return d_bias;
    }

    public Double[] getD_theta() {
        return d_theta;
    }

    public Double getPrediction(){
        return this.prediction;
    }

    public Double getAccuracy() {
        return this.accuracy;
    }
   
}
