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
			//*************
			int numUser = 1;
			Calendar calendar = Calendar.getInstance();
			boolean traceFlag = false;
			//*************
			CloudSim.init(numUser, calendar, traceFlag);
			//*************
			DatacenterBroker broker = myTools.createBroker("MyBroker");
			int brokerId = broker.getId();
			//*************
			Datacenter myDatacentered = myTools.createDatacenter("MyDC", 5, 5000, 4096, 1000000, 10000 );
			//*************
			UtilizationModel utilizationModel = new UtilizationModelFull();
			vmlist = new ArrayList<Vm>();
			cloudletList = new ArrayList<Cloudlet>();
			for (int i = 0; i < 5; i++) {
				Vm vm = myTools.createVM(i, 1000, 10000, 512, 1000, 1, "Xen", brokerId);
				Cloudlet cloudlet = new Cloudlet(i, 40000, 1, 300, 300, utilizationModel, utilizationModel, utilizationModel);
				vmlist.add(vm);
				cloudlet.setUserId(brokerId);
				cloudlet.setVmId(i);
				cloudletList.add(cloudlet);
			}
			broker.submitVmList(vmlist);
			broker.submitCloudletList(cloudletList);
			//*************
			CloudSim.startSimulation();
			CloudSim.stopSimulation();
			//*************
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			Log.printLine(myDatacentered.getName());
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
