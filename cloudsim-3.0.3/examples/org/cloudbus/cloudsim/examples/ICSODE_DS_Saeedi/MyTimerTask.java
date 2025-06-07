/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.examples.ICSODE_DS_Saeedi;

import java.util.Random;
import java.util.TimerTask;

/**each 500 ms(landa) activate the thread to check that if new task or cloud let arrived
 * thread
 * @author SAEEDI
 */
public class MyTimerTask extends TimerTask implements Parameters {

    private int varsize;
    private int taskarr;

    public MyTimerTask(int vars) {
        this.varsize = vars;

    }

    public void run() {

        event();
    }

    public int event() {

//        taskarr = new Random().nextInt(11 - 1) + 1;
        taskarr = 0;
        for (int i = 1; i <= (varsize / 2); i++) {
            if (varsize % i == 0) {
                taskarr++;
            }
        }
        varsize = varsize + taskarr;
        if (varsize >= varsize1) {
            varsize = varsize1;
            taskarr = 0;
        }

        return this.varsize;
    }

    public int getvar() {

        return this.varsize;
    }

    public int getttr() {

        return this.taskarr;

    }
}
