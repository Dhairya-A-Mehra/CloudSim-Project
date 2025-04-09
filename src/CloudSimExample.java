package cloudsim;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;

import java.util.*;

public class CloudSimExample {
    private static final int INITIAL_VM_COUNT = 2;
    private static final int CLOUDLET_COUNT = 10;
    private static final int VM_THRESHOLD = 3;

    public static void main(String[] args) {
        try {
            int numUsers = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;

            CloudSim.init(numUsers, calendar, traceFlag);

            Datacenter datacenter0 = createDatacenter("Datacenter_0");

            DatacenterBroker broker = new DatacenterBroker("Broker");
            int brokerId = broker.getId();

            List<Vm> vmlist = createVms(brokerId, INITIAL_VM_COUNT);
            broker.submitVmList(vmlist);

            List<Cloudlet> cloudletList = createCloudlets(brokerId, CLOUDLET_COUNT, vmlist);
            broker.submitCloudletList(cloudletList);

            CloudSim.startSimulation();

            List<Cloudlet> newList = broker.getCloudletReceivedList();

            printCloudletList(newList); 

            CloudSim.stopSimulation();

            autoScaleIfNeeded(cloudletList, vmlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Datacenter createDatacenter(String name) throws Exception {
        List<Host> hostList = new ArrayList<>();

        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(1000)));

        Host host = new Host(
                0,
                new RamProvisionerSimple(2048),
                new BwProvisionerSimple(10000),
                1000000,
                peList,
                new VmSchedulerTimeShared(peList)
        );

        hostList.add(host);

        return new Datacenter(
                name,
                new DatacenterCharacteristics(
                        "x86", "Linux", "Xen", hostList,
                        10.0, 3.0, 0.05, 0.1, 0.1),
                new VmAllocationPolicySimple(hostList),
                new LinkedList<>(),
                10
        );
    }

    private static List<Vm> createVms(int brokerId, int count) {
        List<Vm> vmlist = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Vm vm = new Vm(i, brokerId, 1000, 1, 2048, 1000, 10000, "Xen", new CloudletSchedulerTimeShared());
            vmlist.add(vm);
        }
        return vmlist;
    }

    private static List<Cloudlet> createCloudlets(int brokerId, int count, List<Vm> vmlist) {
        List<Cloudlet> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Cloudlet cloudlet = new Cloudlet(i, 40000, 1, 300, 300,
                    new UtilizationModelFull(),
                    new UtilizationModelFull(),
                    new UtilizationModelFull());
            cloudlet.setUserId(brokerId);
            cloudlet.setVmId(vmlist.get(i % vmlist.size()).getId());
            list.add(cloudlet);
        }
        return list;
    }

    private static void printCloudletList(List<Cloudlet> list) {
        System.out.println("========== OUTPUT ==========");
        System.out.println("Cloudlet ID\tSTATUS\t\tData center ID\tVM ID\tTime\tStart Time\tFinish Time");

        for (Cloudlet cloudlet : list) {
            System.out.print(cloudlet.getCloudletId() + "\t\t");

            if (cloudlet.getStatus() == Cloudlet.SUCCESS) {
                System.out.print("SUCCESS");

                System.out.println("\t\t" +
                        cloudlet.getResourceId() + "\t\t" +
                        cloudlet.getVmId() + "\t\t" +
                        cloudlet.getActualCPUTime() + "\t" +
                        cloudlet.getExecStartTime() + "\t\t" +
                        cloudlet.getFinishTime());
            }
        }
    }

    private static void autoScaleIfNeeded(List<Cloudlet> cloudletList, List<Vm> vmlist) {
        int avgLoad = cloudletList.size() / vmlist.size();

        System.out.println("\n========= AUTO-SCALING =========");
        if (avgLoad > VM_THRESHOLD) {
            System.out.println("⚠️  High average load per VM (" + avgLoad + "). Scaling out by adding 1 VM...");
        } else if (avgLoad < (VM_THRESHOLD / 2)) {
            System.out.println("ℹ️  Load is low (" + avgLoad + "). Consider scaling in...");
        } else {
            System.out.println("✅ Load is optimal. No scaling needed.");
        }
    }
}
