import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.*;
import com.opencsv.*;

public class CSV_Generator {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// https://commons.apache.org/proper/commons-csv/archives/1.2/apidocs/index.html
		long datapoints = 50;
		double deviationScale = 10;//changes the amount of variance in the points. 0 should be none, idk how to make this actual variance/std yet so there's no point of reference
		double timestep = .1;
		String name = "testprint";
		//CSVFormat format = new CSVFormat();
		CSVWriter writer = new CSVWriter(new FileWriter("E:\\EclipseWorkspace\\SSS_Kalman_Testing\\"+ name+".csv"));

		String[] header={"x","f(x)"};
		writer.writeNext(header);

		for (int i = 0; i <datapoints; i++){
			String[] data = {Integer.toString(i),Double.toString((f(i) + Math.random()* deviationScale))};
			writer.writeNext(data);
			System.out.println(data);
		}
		writer.close();
	}

	public static double f(double x){
		return x;
	}
}
