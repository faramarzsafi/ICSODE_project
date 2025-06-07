/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.examples.ICSOChaotic_DS_Saeedi;

import java.util.Comparator;

/**
 *
 * @author SAEEDI
 */
public class Comparatorcost_chicken implements Comparator<Chickens> {

    @Override
    public int compare(Chickens arg1, Chickens arg2) {
        int rest = 0;

        if (arg1.getcostvalue() < arg2.getcostvalue()) {
            rest = -1;
        }
        if (arg2.getcostvalue() < arg1.getcostvalue()) {
            rest = 1;
        }

        return rest;
    }

}
