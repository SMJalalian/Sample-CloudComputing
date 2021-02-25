import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.text.DecimalFormat;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;


public class App {



    public static void main(String[] args) throws Exception 
    {      
        try{

            List<Vm> vmlist;
            List<Cloudlet> cloudletList;
            Tools myTools = new Tools();

			int numUser = 1;
			Calendar calendar = Calendar.getInstance();
			boolean traceFlag = false;

			CloudSim.init(numUser, calendar, traceFlag);

			Datacenter myDatacentered = myTools.createDatacenter("MyDC");

            Log.printLine(myDatacentered.getName());


			DatacenterBroker broker = myTools.createBroker("MyBroker");
			int brokerId = broker.getId();
			
            Vm vm1 = myTools.createVM(0, 1000, 10000, 512, 1000, 1, "Xen", brokerId);
			Vm vm2 = myTools.createVM(1, 1000, 10000, 512, 1000, 1, "Xen", brokerId);

            
            vmlist = new ArrayList<Vm>();
			vmlist.add(vm1);
			vmlist.add(vm2);

			broker.submitVmList(vmlist);

			cloudletList = new ArrayList<Cloudlet>();

			UtilizationModel utilizationModel = new UtilizationModelFull();
			Cloudlet cloudlet1 = new Cloudlet(0, 40000, 1, 300, 300, utilizationModel, utilizationModel, utilizationModel);
			Cloudlet cloudlet2 = new Cloudlet(1, 400000, 1, 300, 300, utilizationModel, utilizationModel, utilizationModel);

            cloudlet1.setUserId(brokerId);
			cloudlet1.setVmId(0);
			cloudlet2.setUserId(brokerId);
			cloudlet2.setVmId(1);

			cloudletList.add(cloudlet1);
			cloudletList.add(cloudlet2);
			broker.submitCloudletList(cloudletList);

			CloudSim.startSimulation();
			CloudSim.stopSimulation();
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			printCloudletList(newList);
		} 
        catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
    }

    private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "Data center ID" + indent + "VM ID" + indent + "Time" + indent
				+ "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");

				Log.printLine(indent + indent + cloudlet.getResourceId()
						+ indent + indent + indent + cloudlet.getVmId()
						+ indent + indent
						+ dft.format(cloudlet.getActualCPUTime()) + indent
						+ indent + dft.format(cloudlet.getExecStartTime())
						+ indent + indent
						+ dft.format(cloudlet.getFinishTime()));
			}
		}
	}
}
