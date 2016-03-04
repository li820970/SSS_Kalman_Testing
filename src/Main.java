import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.opencsv.CSVWriter;


public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/filter/KalmanFilter.html

		//		A - state transition matrix
		//		B - control input matrix
		//		H - measurement matrix
		//		Q - process noise covariance matrix
		//		R - measurement noise covariance matrix
		//		P - error covariance matrix

		CSV_Handler firstHandler = new CSV_Handler(50, 10, .1,"test");
		firstHandler.generateData();
		firstHandler.generateData();

		String[][] data = firstHandler.getData(1);
//		for(int i = 0; i < data.length; i++){
//			for(int j = 0; j < data[i].length;j++){
//				System.out.print(data[i][j]+" ");				 
//			}
//			System.out.print("\n");
//		}
		
		// A = [ 1 ]
		RealMatrix A = new Array2DRowRealMatrix(new double[] { 1d });
		// B = null
		RealMatrix B = null;
		// H = [ 1 ]
		RealMatrix H = new Array2DRowRealMatrix(new double[] { 1d });
		// x = [ 10 ]
		RealVector x = new ArrayRealVector(new double[] { 10 });
		// Q = [ 1e-5 ]
		RealMatrix Q = new Array2DRowRealMatrix(new double[] { 1e-5d });
		// P = [ 1 ]
		RealMatrix P0 = new Array2DRowRealMatrix(new double[] { 1d });
		// R = [ 0.1 ]
		RealMatrix R = new Array2DRowRealMatrix(new double[] { .1d });
		
		
		ProcessModel pm = new DefaultProcessModel(A, B, Q, x, P0);
		MeasurementModel mm = new DefaultMeasurementModel(H, R);
		KalmanFilter filter = new KalmanFilter(pm, mm);  

		
		RealVector pNoise = new ArrayRealVector(1);
		RealVector mNoise = new ArrayRealVector(1);
		CSVWriter writer = new CSVWriter(new FileWriter(firstHandler.DEFAULTDIR + "aa.csv"));
		String[] header={"x","f(x)"};
		writer.writeNext(header);
		
		for (int i = 1; i <= 50; i++) {
		    filter.predict();

		    // simulate the process
		    pNoise.setEntry(0, Double.parseDouble(data[i][1]) );

		    // x = A * x + p_noise
		    x = A.operate(x).add(pNoise);

		    // simulate the measurement
		    mNoise.setEntry(0, Double.parseDouble(data[i][1]) );

		    // z = H * x + m_noise
		    RealVector z = H.operate(x).add(mNoise);

		    filter.correct(z);

		    String[] result = {Integer.toString(i),Double.toString(filter.getStateEstimation()[0])};
			writer.writeNext(result);
			System.out.print(result[1]);
		}
		writer.close();
	}

}
