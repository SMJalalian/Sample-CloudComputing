import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import org.cloudbus.cloudsim.Storage;

import org.cloudbus.cloudsim.provisioners.*;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;


public class Tools {

	//Constructor
    public Tools() {
        super();
    }


    public Vm createVM(int vmid, int mips, long size, int ram, long bw, int pesNumber, String vmm, int brokerId) {

        Vm virtualMachine = null;
        virtualMachine = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared()); 
        return virtualMachine;
    }

    public DatacenterBroker createBroker(String name) {

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
 
    public Datacenter createDatacenter(String name,  int hostNumbers, int mips , int ram, long storage, int bw) {

		List<Host> hostList = new ArrayList<Host>();
		List<Pe> peList = new ArrayList<Pe>();

		peList.add(new Pe(0, new PeProvisionerSimple(mips)));
		for (int i = 0; i < hostNumbers; i++) {
			int hostId = i;

			hostList.add(
				new Host(
					hostId,
					new RamProvisionerSimple(ram),
					new BwProvisionerSimple(bw),
					storage,
					peList,
					new VmSchedulerTimeShared(peList)
				)
			);
		}

		String arch = "x86"; 
		String os = "Linux";
		String vmm = "Xen";
		double timeZone = 10.0; 
		double cost = 3.0; 
		double costPerMem = 0.05; 
		double costPerStorage = 0.001; 
		double costPerBw = 0.0; 
		LinkedList<Storage> storageList = new LinkedList<Storage>();

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, timeZone, cost, costPerMem,
				costPerStorage, costPerBw);

		Datacenter datacenter = null;
		try 
		{
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return datacenter;
	}
}
