import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.*;

public class CSV_Handler {
	public final String DEFAULTDIR = "E:\\EclipseWorkspace\\SSS_Kalman_Testing\\";
	private long datapoints;
	private double deviationScale;
	private double timeStep;
	private String fileName;
	private int runCount = 0; //this is incremented before the first file is written
								//there is no run 0
	
	CSV_Handler(){}

	CSV_Handler(long ndatapoints, double ndeviationScale, double ntimeStep, String nfileName) throws IOException{
		datapoints = ndatapoints;
		deviationScale = ndeviationScale;
		timeStep = ntimeStep;
		fileName = nfileName;


	}

	public void generateData() throws IOException{
		runCount++;

		CSVWriter writer = new CSVWriter(new FileWriter(this.getPath(runCount)));

		String[] header={"x","f(x)"};
		writer.writeNext(header);

		for (int i = 0; i <datapoints; i++){
			String[] data = {Double.toString(timeStep*i),Double.toString((f(i) + Math.random()* deviationScale))};
			writer.writeNext(data);
//			System.out.println(data);
		}

		writer.close();

		runCount++;

	}

	public String[][] getData(int runNo) throws IOException{
		if (invalidRunNo(runNo)){System.out.println("getData: invalid run number");return null;}
		 CSVReader reader = new CSVReader(new FileReader(this.getPath(runNo)));
		 List<String[]> temp = reader.readAll();
		 reader.close();
		 int width = 2;//the 2 is hardcoded for now
		 String[][] returnArray = new String[temp.size()][width];
//		 returnArray = temp.toArray(returnArray);
		 for(int i = 0; i < temp.size(); i++){
			 for(int j = 0; j < width;j++){
				 returnArray[i][j] = temp.get(i)[j];
				 
			 }
			 
		 }
		 
		 return returnArray.clone();
	}
	
	public String getPath(int runNo){
		if (invalidRunNo(runNo)){System.out.println("getPath: invalid run number");return null;}
		return "E:\\EclipseWorkspace\\SSS_Kalman_Testing\\"+ fileName+" - " + runNo + ".csv";
	}

	private boolean invalidRunNo(int runNo){return (runNo > runCount || runCount == 0 || runNo <= 0)?true:false;}
	
	public static double f(double x){
		return x;
	}

	public long getDatapoints() {
		return datapoints;
	}

	public void setDatapoints(long datapoints) {
		this.datapoints = datapoints;
	}

	public double getDeviationScale() {
		return deviationScale;
	}

	public void setDeviationScale(double deviationScale) {
		this.deviationScale = deviationScale;
	}

	public double getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(double timeStep) {
		this.timeStep = timeStep;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
