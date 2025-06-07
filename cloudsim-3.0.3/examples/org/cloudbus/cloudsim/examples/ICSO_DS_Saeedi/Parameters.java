/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.examples.ICSO_DS_Saeedi;

/**
 *
 * @author SAEEDI
 */
public interface Parameters {

   // Problem Definition
    int varsize1 = 750;              // Number of Problem Dimasions and number of tasks(cloudlets)
    int VM_SIZE = 100;                // Number of virtual machines
    double varmax = VM_SIZE - 1;            // Lower Bound of Variables
    double varmin = 0;           // Upper Bound of Variables
    int nPop = 100;                  // Number of foraging ravens
    int MaxIt = 100;
    // *************** CSO *************//
    // Chicken Swarm Optimization Parameters
    public static final int G = 10;               // How often the chicken swarm can be updated
    public static final double rPercent = 0.15;  // The population size of roosters accounts for "rPercent"
    public static final double hPercent = 0.7;  // The population size of hens accounts for "hPercent" percent of the total population size
    public static final double mPercent = 0.5; // The population size of mother hens accounts for "mPercent" percent of the population size of hens

    public static final int rNum = (int) Math.round(nPop * rPercent);    // The population size of roosters
    public static final int hNum = (int) Math.round(nPop * hPercent);    // The population size of hens
    public static final int cNum = nPop - rNum - hNum;                 // The population size of chicks
    public static final int mNum = (int) Math.round(hNum * mPercent);   // The population size of mother hens

}
