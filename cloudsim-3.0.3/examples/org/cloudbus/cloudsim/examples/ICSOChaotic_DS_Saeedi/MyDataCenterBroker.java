/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.examples.ICSOChaotic_DS_Saeedi;

import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.Collections;
import java.util.Timer;
import org.cloudbus.cloudsim.lists.VmList;

/**
 *
 * @author SAEEDI
 */
public class MyDataCenterBroker extends DatacenterBroker implements Parameters {

    private List<Cloudlet> templistcloudlet = new ArrayList<Cloudlet>();
    public List<Cloudlet> tempcloudletsSubmitted = new ArrayList<Cloudlet>();

    private Vector<Chickens> chicken = new Vector<Chickens>();
    private Vector<Chickens> tempchicken = new Vector<Chickens>();
    List<Integer> Bestsol = new ArrayList<Integer>();
    Random generator = new Random();
    int varsize = varsize1;
    private double GlobalBest_Cost = Double.POSITIVE_INFINITY;
    private List<Double> GlobalBest_Position = new ArrayList<Double>();
    private List<Double> FL = new ArrayList<Double>();
    private List<Integer> rank = new ArrayList<Integer>();
    private List<Integer> motherLib = new ArrayList<Integer>();
    private List<Integer> mate = new ArrayList<Integer>();
    private List<Integer> temp = new ArrayList<Integer>();
    private List<Integer> temp2 = new ArrayList<Integer>();

    private List<Integer> index = new ArrayList<Integer>();
    private List<Integer> result = new ArrayList<Integer>();
    private List<Integer> mother = new ArrayList<Integer>();
//    private List<Double> temp_Position = new ArrayList<Double>();
//    private List<Double> temp_Position2 = new ArrayList<Double>();
//    private List<Double> temp_Position3 = new ArrayList<Double>();
    private List<Double> randn = new ArrayList<Double>();
    private List<Double> randn1 = new ArrayList<Double>();
    private List<Double> Best_Map = new ArrayList<Double>();
    private double[] BestCost = new double[MaxIt];
    public double runtime = 0;
    public double Best_runtime = 0;

    public MyDataCenterBroker(String name) throws Exception {
        super(name);
    }

    @Override
    protected void submitCloudlets() {

        Best_runtime = ICSOChaotic_Algorithm();
        int vmIndex = 0;

        double mapping;
        for (int i = 0; i < varsize; i++) {

            Cloudlet cloudlet = getCloudletList().get(i);
            mapping = GlobalBest_Position.get(i);
            Vm vm = vmList.get((int) mapping);
            Log.printLine(CloudSim.clock() + ": " + getName() + ": Sending cloudlet " + cloudlet.getCloudletId() + " to VM #" + vm.getId());
            cloudlet.setVmId(vm.getId());
            sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
            cloudletsSubmitted++;
            vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
            getCloudletSubmittedList().add(cloudlet);
            tempcloudletsSubmitted.add(cloudlet);

        }

        // remove submitted cloudlets from waiting list
        for (Cloudlet cloudlet : getCloudletSubmittedList()) {
            getCloudletList().remove(cloudlet);
        }

    }

    public double evaluate(List<Double> pos) {

        double Texe = 0, a;
        int j = 0, i;

        List<Integer> p = new ArrayList<Integer>();
        p = discretemethod(pos); // Find a permutation according to the updated each particle’s position

        // For each job Calculate the execution time
        for (j = 0; j < varsize; j++) {

            Cloudlet cloudlet = getCloudletList().get(j);
            Vm vm = vmList.get(p.get(j));

            bindCloudletToVm(cloudlet.getCloudletId(), vm.getId());
            Texe += (double) cloudlet.getCloudletLength() / vm.getMips();

        }

        Bestsol = p;
        return Texe;
    }

    public double ICSOChaotic_Algorithm() {

        varsize = varsize1 / 4;
        System.out.println();
        System.out.println("ICSOChaotic Algorithm Starting....." + "\n");
        Initialization();

        int it, i, j, k = 0, n, r, anotherRooster, other, ttnum = 0;
        double tempsigma, s1, s2, a, wmax = 0.9, wmin = 0.4, w;
        double Brt = 0;

        for (i = 0; i < nPop; i++) {

            rank.add(0);
            mate.add(0);
            motherLib.add(0);
            mother.add(0);
        }
        for (it = 0; it < MaxIt; it++) {

            w = wmax * (wmax - wmin) * (MaxIt - it) / MaxIt;

            Timer timer = new Timer();

            MyTimerTask tt = new MyTimerTask(varsize);
            timer.schedule(tt, 500, 2000);
            varsize = tt.event();
            if (varsize > varsize1) {
                timer.cancel();
                varsize = varsize + 0;
                System.out.println(0 + " Task is Arrive..." + "Number of total tasks =" + varsize);
            } else {
                ttnum = tt.getttr();
                System.out.println(ttnum + " Task is Arrive..." + "Number of total tasks =" + varsize);
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
            }
            timer.cancel();

            for (i = 0; i < nPop; i++) {

                FL.add(0.1 * new Random().nextDouble() + 0.4);

            }

            if (it % G == 1 || it == 0) {

//                rank = sort();
                sort();

                //Here motherLib include all the mother hens
                motherLib = randperm(hNum, mNum);
                for (i = 0; i < mNum; i++) {
                    motherLib.add(motherLib.get(i) + rNum);
                }
                mate = randpermF(rNum, hNum);

                // Randomly select cNum chicks' mother hens
                for (i = 0; i < cNum; i++) {
                    mother.add(motherLib.get(new Random().nextInt(mNum - 1) + 0));
                }

            }
            // Update the rNum roosters' values
            for (r = 0; r < rNum; r++) {
                // randomly select another rooster different from the i (th) one
                anotherRooster = randiTabu(1, rNum, r, 1);
                if (chicken.get(rank.get(r)).getcostvalue() <= chicken.get(rank.get(anotherRooster)).getcostvalue()) {
                    tempsigma = 1;
                } else {
                    tempsigma = Math.exp((chicken.get(rank.get(anotherRooster)).getcostvalue()) - chicken.get(rank.get(r)).getcostvalue() / (Math.abs(chicken.get(rank.get(r)).getcostvalue()) + 2.2251e-308));

                }

                List<Double> temp_Position = new ArrayList<Double>();
                List<Double> newtempPosition = new ArrayList<Double>();
                // Update solution
                temp_Position = chicken.get(rank.get(r)).getPosition();
                if (varsize > temp_Position.size()) {
                    for (int d = temp_Position.size(); d < varsize; d++) {
                        temp_Position = convert(temp_Position);
                        GlobalBest_Position = convert(GlobalBest_Position);
                    }
                }

                for (i = 0; i < varsize; i++) {

                    a = temp_Position.get(i) * (new Random().nextGaussian() * tempsigma);
                    a = Math.max(a, varmin);
                    a = Math.min(a, varmax);
                    newtempPosition.add(a);
                }

                // Evaluate
                double Cost = evaluate(newtempPosition);

                // Update Position and Cost
                if (chicken.get(rank.get(r)).getcostvalue() > Cost) {
                    chicken.get(rank.get(r)).setPosition(newtempPosition);
                    chicken.get(rank.get(r)).setcostvalue(Cost);

                }

            }

            // Update the hNum hens' values
            for (i = rNum + 1; i < rNum + hNum; i++) {

                other = randiTabu(1, i, mate.get(i - rNum), 1);

                s1 = Math.exp((chicken.get(rank.get(i)).getcostvalue() + chicken.get(rank.get(mate.get(i - rNum))).getcostvalue())
                        / (Math.abs(chicken.get(rank.get(i)).getcostvalue()) + 2.2251e-308));

                s2 = Math.exp(chicken.get(rank.get(other)).getcostvalue() - chicken.get(rank.get(i)).getcostvalue());

                List<Double> ntemp_Position = new ArrayList<Double>();
                List<Double> ntemp_Position2 = new ArrayList<Double>();
                List<Double> ntemp_Position3 = new ArrayList<Double>();
                List<Double> newtemp_Position = new ArrayList<Double>();

                // Update solution
                ntemp_Position = chicken.get(rank.get(i)).getPosition();
                ntemp_Position2 = chicken.get(rank.get(mate.get(i - rNum))).getPosition();
                ntemp_Position3 = chicken.get(rank.get(other)).getPosition();

                if (varsize > ntemp_Position.size()) {

                    ntemp_Position = convert(ntemp_Position);
                    ntemp_Position2 = convert(ntemp_Position2);
                    ntemp_Position3 = convert(ntemp_Position3);
                    GlobalBest_Position = convert(GlobalBest_Position);

                }

                for (j = 0; j < varsize; j++) {

                    ntemp_Position2 = convert(ntemp_Position2);
                    ntemp_Position = convert(ntemp_Position);
                    ntemp_Position3 = convert(ntemp_Position3);

                    double b = w * ntemp_Position.get(j) + s1 * generator.nextDouble() * (ntemp_Position2.get(j) - ntemp_Position.get(j)) + s2 * generator.nextDouble() * (ntemp_Position3.get(j) - ntemp_Position.get(j));
                    b = Math.max(b, varmin);
                    b = Math.min(b, varmax);

                    newtemp_Position.add(b);
                }

                // Evaluate
                double Cost = evaluate(newtemp_Position);

                // Update Position and Cost
                if (chicken.get(rank.get(i)).getcostvalue() > Cost) {
                    chicken.get(rank.get(i)).setPosition(newtemp_Position);
                    chicken.get(rank.get(i)).setcostvalue(Cost);

                }

            }

            List<Double> temp_Position = new ArrayList<Double>();
            List<Double> temp_Position2 = new ArrayList<Double>();
            List<Double> temp_Position3 = new ArrayList<Double>();
            List<Double> ctemp_Position = new ArrayList<Double>();

            // Update the cNum chicks' values
            for (i = rNum + hNum + 1; i < nPop; i++) {

                temp_Position = chicken.get(rank.get(i)).getPosition();
                temp_Position2 = chicken.get(rank.get(mother.get(i - rNum - hNum))).getPosition();
//                temp_Position3 = chicken.get(rank.get(mother.get(i - rNum - hNum))).getPosition();

                if (varsize > temp_Position.size()) {

                    temp_Position = convert(temp_Position);
                    temp_Position2 = convert(temp_Position2);
                    temp_Position3 = convert(temp_Position3);
                    GlobalBest_Position = convert(GlobalBest_Position);

                }

                for (j = 0; j < varsize; j++) {

                    a = temp_Position.get(j) + FL.get(i) * (temp_Position2.get(j) - temp_Position.get(j));
                    a = Math.max(a, varmin);
                    a = Math.min(a, varmax);
                    ctemp_Position.add(a);

                }

                // Evaluate
                double Cost = evaluate(ctemp_Position);

                // Update Position and Cost
                if (chicken.get(rank.get(i)).getcostvalue() > Cost) {
                    chicken.get(rank.get(i)).setPosition(ctemp_Position);
                    chicken.get(rank.get(i)).setcostvalue(Cost);

                }
            }

            //Update the individual's best fitness vlaue and the global best one
            for (i = 0; i < nPop; i++) {
                if (chicken.get(i).getcostvalue() < chicken.get(i).getBestcostvalue()) {
                    chicken.get(i).setBestcostvalue(chicken.get(i).getcostvalue());
                    chicken.get(i).setBestPosition(chicken.get(i).getPosition());

                }

                if (chicken.get(i).getBestcostvalue() < GlobalBest_Cost) {
                    GlobalBest_Cost = chicken.get(i).getBestcostvalue();
                    GlobalBest_Position = chicken.get(i).getBestPosition();

                }

            }

            BestCost[it] = GlobalBest_Cost;

            System.out.println("ITERATION " + it + ": " + "BestCost=" + BestCost[it] + "\n");

        }

        System.out.println("\nSolution found at iteration " + (it - 1) + ", the solutions is:" + "\n");
        System.out.println("BestCost=" + BestCost[it - 1] + "\n");

        runtime = runtime + BestCost[it - 1];
        System.out.println("(EXECUTION TIME) Bestruntime=" + runtime + "\n");

        return Brt = BestCost[MaxIt - 1];
    }

    public void Initialization() {

        int i, j;
        Chickens C1;
        for (i = 0; i < nPop; i++) {
            C1 = new Chickens();

            //Initialization position
            List<Double> position = new ArrayList<Double>();

            for (j = 0; j < varsize; j++) {

                double a, l;
                a = varmin + new Random().nextDouble() * (varmax - varmin);

                l = 4 * Math.pow(a, 2) - 3 * a;
                a = (1 / 2 * (varmax - varmin)) * l + 1 / 2 * (varmax + varmin);
                a = Math.max(a, varmin);
                a = Math.min(a, varmax);
                position.add(a);

            }

            C1.setPosition(position);

            //Evaluation
            C1.setcostvalue(evaluate(position));

            //Update Personal Best
            C1.setBestPosition(C1.getPosition());
            C1.setBestcostvalue(C1.getcostvalue());

            //Update Global Best
            if (C1.getBestcostvalue() < GlobalBest_Cost) {
                GlobalBest_Cost = C1.getBestcostvalue();
                GlobalBest_Position = C1.getBestPosition();
            }
            chicken.add(C1);

        }

    }

    public List<Integer> discretemethod(List<Double> location) {

        List<Integer> p = new ArrayList<Integer>();

        int i, x;

        for (i = 0; i < varsize; i++) {
            x = (int) Math.min(Math.floor(location.get(i)), varmax);
            p.add(x);
        }

        return p;
    }

    public List<Integer> sort() {

        int i, j, k = 0;
        double cost;
        rank.clear();
        Collections.sort(chicken, new Comparatorcost_chicken());//Sort of Costs

        for (j = 0; j < nPop; j++) {
            cost = chicken.get(j).getcostvalue();
            for (i = 0; i < nPop; i++) {
                if (cost == chicken.get(i).getcostvalue()) {

                    rank.add(k, i);
                }
            }
            k++;
        }
        return rank;
    }

    public List<Integer> randperm(int a, int b) {

        List<Integer> array = new ArrayList<Integer>();
        int i, swap, r;
        for (i = 0; i < b; i++) {
            array.add(new Random().nextInt(a - 1) + 0);
        }

        // shuffle
        for (i = 0; i < b; i++) {
            r = (int) (Math.random() * (i + 1));     // int between 0 and i
            swap = array.get(r);
            array.add(r, array.get(i));
            array.add(i, swap);
        }

        return array;

    }

    public List<Integer> randpermF(int rNum, int hNum) {
        int i, j;
        temp.addAll(randperm(rNum, rNum));
        temp2.addAll(randi(rNum, hNum));
        index.addAll(randperm(hNum, hNum - rNum));
        for (i = 0; i < temp.size(); i++) {
            result.add(temp.get(i));
        }
        j = 0;
        for (i = rNum; i < hNum; i++) {
            result.add(temp2.get(index.get(j)));
            j++;
        }
        return result;

    }

    public List<Integer> randi(int a, int b) {

        List<Integer> temp3 = new ArrayList<Integer>();
        int i;
        for (i = 0; i < b; i++) {
            temp3.add(new Random().nextInt(a - 1) + 0);
        }
        return temp3;
    }

    public int randiTabu(int min, int max, int tabu, int dim) {

        int value, num = 1, temp;
        value = dim * max * 2;

        while (num <= dim || value >= nPop) {
            temp = new Random().nextInt(max) + min;

            if ((value != temp) && (temp != tabu)) {
                value = temp;
            }
            num++;
        }

        return value;
    }

    public double loadCalculator() {

        int Counter = 0, i, j, k;
        double ALF, sum = 0;
        int[] BM = new int[vmList.size()];
        int tasknumber = varsize;

        for (j = 0; j < vmList.size(); j++) {
            Vm vm = (Vm) vmList.get(j);

            for (i = 0; i < Bestsol.size(); i++) {

                if (vm.getId() == Bestsol.get(i)) {
                    Counter++;
                }
            }
            BM[j] = Counter;
            Counter = 0;

        }

        for (k = 0; k < BM.length; k++) {
            sum = sum + BM[k];
        }
        ALF = sum / vmList.size();

        return ALF;

    }

    public List<Double> convert(List<Double> location) {

        if (location.size() > varsize) {

            while (location.size() > varsize) {
                location.remove(location.size() - 1);
            }
        } else if (varsize > location.size()) {
            for (int d = location.size(); d < varsize; d++) {
                double a;
                a = varmin + new Random().nextDouble() * (varmax - varmin);
                a = Math.max(a, varmin);
                a = Math.min(a, varmax);
                location.add(a);
            }
        }
        return location;
    }
}
