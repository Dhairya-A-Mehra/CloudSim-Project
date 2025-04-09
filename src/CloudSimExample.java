package cloudsim;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;

import java.util.*;

public class CloudSimExample {
    public static void main(String[] args) {
        try {
            // Initialize the CloudSim library
            int numUsers = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;

            CloudSim.init(numUsers, calendar, traceFlag);

            // Create Datacenter
            Datacenter datacenter0 = createDatacenter("Datacenter_0");

            // Create Broker
            DatacenterBroker broker = new DatacenterBroker("Broker");
            int brokerId = broker.getId();

            // Create VMs
            List<Vm> vmlist = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                Vm vm = new Vm(i, brokerId, 1000, 1, 2048, 10000, 1000, "Xen", new CloudletSchedulerTimeShared());
                vmlist.add(vm);
            }
            broker.submitVmList(vmlist);

            // Create Cloudlets
            List<Cloudlet> cloudletList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Cloudlet cloudlet = new Cloudlet(i, 40000, 1, 300, 300, new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull());
                cloudlet.setUserId(brokerId);
                cloudlet.setVmId(vmlist.get(i % vmlist.size()).getId());
                cloudletList.add(cloudlet);
            }
            broker.submitCloudletList(cloudletList);

            // Start simulation
            CloudSim.startSimulation();

            List<Cloudlet> newList = broker.getCloudletReceivedList();
            CloudSim.stopSimulation();

            // Output
            System.out.println("=== Output ===");
            for (Cloudlet cloudlet : newList) {
                System.out.println("Cloudlet ID: " + cloudlet.getCloudletId() + " Status: " + cloudlet.getStatus() +
                        " VM ID: " + cloudlet.getVmId() + " Time: " + cloudlet.getActualCPUTime());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Datacenter createDatacenter(String name) throws Exception {
        List<Host> hostList = new ArrayList<>();

        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(1000)));

        int hostId = 0;
        int ram = 8192;
        long storage = 1000000;
        int bw = 10000;

        hostList.add(new Host(
                hostId,
                new RamProvisionerSimple(ram),
                new BwProvisionerSimple(bw),
                storage,
                peList,
                new VmSchedulerTimeShared(peList)
        ));

        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double time_zone = 10.0;
        double cost = 3.0;
        double costPerMem = 0.05;
        double costPerStorage = 0.001;
        double costPerBw = 0.0;

        LinkedList<Storage> storageList = new LinkedList<>();

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        return new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
    }
}
