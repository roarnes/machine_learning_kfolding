/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iris;

import java.util.ArrayList;

/**
 *
 * @author arnesrespati
 */


public class TrainingData {

    public ArrayList<Double> sepal_length = new ArrayList<>();
    public ArrayList<Double> sepal_width = new ArrayList<>();
    public ArrayList<Double> petal_length = new ArrayList<>();
    public ArrayList<Double> petal_width = new ArrayList<>();
    public ArrayList<String> iris_class = new ArrayList<>();
    
  
    public ArrayList<String> getIris_class() {
        return iris_class;
    }

    public void setIris_class(ArrayList<String> iris_class) {
        this.iris_class = iris_class;
    }


    public ArrayList<Double> getSepal_length() {
        return sepal_length;
    }
    
    public int getNoTrainingData(){
        return sepal_length.size();
    }

    public void setSepal_length(ArrayList<Double> sepal_length) {
        this.sepal_length = sepal_length;
    }

    public ArrayList<Double> getSepal_width() {
        return sepal_width;
    }

    public void setSepal_width(ArrayList<Double> sepal_width) {
        this.sepal_width = sepal_width;
    }

    public ArrayList<Double> getPetal_length() {
        return petal_length;
    }

    public void setPetal_length(ArrayList<Double> petal_length) {
        this.petal_length = petal_length;
    }

    public ArrayList<Double> getPetal_width() {
        return petal_width;
    }

    public void setPetal_width(ArrayList<Double> petal_width) {
        this.petal_width = petal_width;
    }
    
    
}
