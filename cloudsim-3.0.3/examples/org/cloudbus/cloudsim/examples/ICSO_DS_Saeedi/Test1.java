/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cloudbus.cloudsim.examples.ICSO_DS_Saeedi;

import org.cloudbus.cloudsim.examples.ICSODE_DS_Saeedi.MyDataCenterBroker;
import org.cloudbus.cloudsim.examples.ICSODE_DS_Saeedi.Parameters;
import java.text.DecimalFormat;
import java.util.*;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.lists.HostList;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.util.WorkloadFileReader;

/**
 *
 * @author SAEEDI
 */
public class Test1 implements Parameters {

    private static List<Cloudlet> cloudletList;
    private static List<Vm> vmlist;
    public static double cost = 0;
    public static double overhead = 0;
    public static double cloudletnum = 0;
    public static long elapsedTime = 0;
    public static ArrayList<Double> submittedTimes;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Log.printLine("Task Scheduling Optimization Based on ICSO Algorithm" + "\n");
        Log.printLine("Starting time : " + System.currentTimeMillis() + "\n");

        try {
            // First step: Initialize the CloudSim package. It should be called
            // before creating any entities.
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);

            // Second step: Create Datacenters
            //Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
            @SuppressWarnings("unused")
            Datacenter datacenter0 = createDatacenter("Datacenter_0");
            @SuppressWarnings("unused")
            Datacenter datacenter1 = createDatacenter("Datacenter_1");

            //Third step: Create Broker
            MyDataCenterBroker broker = createBroker();
            int brokerId = broker.getId();

            WorkloadFileReader workloadFileReader = new WorkloadFileReader("c:\\cloudsim-3.0.3\\NASA-iPSC-1993-3.swf", 1);
            cloudletList = workloadFileReader.generateWorkload().subList(0, varsize1);
//            submittedTimes = workloadFileReader.getSubmitTimes();
            for (Cloudlet cloudlet : cloudletList) {
                cloudlet.setUserId(brokerId);

            }
            //Fourth step: Create VMs and Cloudlets and send them to broker
            vmlist = createVM(brokerId, VM_SIZE);
            int cldnum = 0;
            cldnum = cloudletList.size();
            cloudletnum = cldnum;
            broker.submitVmList(vmlist);
            broker.submitCloudletList(cloudletList);

            long startTime = System.nanoTime();

            // Fifth step: Starts the simulation
            CloudSim.startSimulation();

            // Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            CloudSim.stopSimulation();
            printCloudletList(newList);

            long stopTime = System.nanoTime();
            elapsedTime = stopTime - startTime;

            Log.printLine("finished time: " + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
    }

    private static List<Vm> createVM(int userId, int vms) {

        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<Vm> list = new LinkedList<Vm>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 1000;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name
        Random r = new Random();
        double cost = 0;
        //create VMs
        Vm[] vm = new Vm[vms];

        for (int i = 0; i < vms; i++) {

            if (i % 2 == 0) {
                vm[i] = new Vm(i, userId, 500, pesNumber, 512, 10000, 9000, vmm, new CloudletSchedulerTimeShared());
            } else if (i % 5 == 0) {
                vm[i] = new Vm(i, userId, 1000, pesNumber + 2, 256, 9000, 9000, vmm, new CloudletSchedulerTimeShared());
            } else {
                vm[i] = new Vm(i, userId, 800, pesNumber, 512, 9000, 9000, vmm, new CloudletSchedulerTimeShared());
            }
            list.add(vm[i]);
        }

        return list;
    }

    private static Datacenter createDatacenter(String name) {

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more
        //    Machines
        List<Host> hostList = new ArrayList<Host>();

        // 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
        //    create a list to store these PEs before creating
        //    a Machine.
        List<Pe> peList1 = new ArrayList<Pe>();

        int mips = 100000;

        // 3. Create PEs and add these into the list.
        //for a quad-core machine, a list of 4 PEs is required:
        // need to store Pe id and MIPS Rating
        peList1.add(new Pe(0, new PeProvisionerSimple(mips)));
        peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
        peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
        peList1.add(new Pe(3, new PeProvisionerSimple(mips)));

        //4. Create Hosts with its id and list of PEs and add them to the list of machines
        int hosts = 400;
        int ram = 32768; //host memory (MB)
        long storage = 1000000; //host storage
        int bw = 100000;

        Host[] host = new Host[hosts];
        for (int i = 0; i < hosts; i++) {
            host[i] = new Host(i, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList1, new VmSchedulerSpaceShared(peList1));
            hostList.add(host[i]);
        }

        // 5. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;		// the cost of using memory in this resource
        double costPerStorage = 0.1;	// the cost of using storage in this resource
        double costPerBw = 0.1;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        // 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics,
                    new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    private static MyDataCenterBroker createBroker() {

        MyDataCenterBroker broker = null;
        try {
            broker = new MyDataCenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;
        double executiontime = 0;
        double responseTime = 0;
        List<Double> finishtime = new ArrayList<Double>();
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {

                finishtime.add(cloudlet.getFinishTime());
                responseTime += cloudlet.getFinishTime() - cloudlet.getSubmissionTime();
            }
        }

        Log.printLine("(THROUGH PUT) Makespan: " + Collections.max(finishtime));
        Log.printLine("(RESPONSE TIME) ResponseTime: " + responseTime);

        
//        Log.printLine("Makespan: " + Collections.max(finishtime));
//        Log.printLine("ResponseTime: " + responseTime);
    }
}
