/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.examples.CSO_Dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SAEEDI
 */
public class Chickens implements Parameters {

    private List<Double> Position = new ArrayList<Double>();
    private List<Double> Best_Position = new ArrayList<Double>();
    private double Cost;
    public double Bestcost;

    public Chickens() {
        super();
    }

    public Chickens(double cost, List<Double> position, List<Double> Bestposition, double Bestcost) {
        super();
        this.Cost = cost;
        this.Position = position;
        this.Bestcost = Bestcost;
        this.Best_Position = Bestposition;

    }

    public List<Double> getPosition() {
        return this.Position;
    }

    public void setPosition(List<Double> Location) {
        this.Position = Location;
    }

    public List<Double> getBestPosition() {
        return this.Best_Position;
    }

    public void setBestPosition(List<Double> Location) {
        this.Best_Position = Location;
    }

    public double getcostvalue() {
        return Cost;
    }

    public void setcostvalue(double cost) {
        this.Cost = cost;
    }

    public double getBestcostvalue() {
        return Bestcost;
    }

    public void setBestcostvalue(double Bestcost) {
        this.Bestcost = Bestcost;
    }

}
