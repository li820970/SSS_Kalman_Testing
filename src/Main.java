import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class Main {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/filter/KalmanFilter.html

		// A - state transition matrix
		// B - control input matrix
		// H - measurement matrix
		// Q - process noise covariance matrix
		// R - measurement noise covariance matrix
		// P - error covariance matrix

		CSV_Handler firstHandler = new CSV_Handler(50, 10, .1, "test");
		firstHandler.generateData();

		// String[][] data = firstHandler.getData(1);

		// time, ac x, y, z gyro x, y, z, mag x, y, z, light
		// for(int i = 0; i < data.length; i++){
		// for(int j = 0; j < data[i].length;j++){
		// System.out.print(data[i][j]+" ");
		// }
		// System.out.print("\n");
		// }

		CSVReader reader = new CSVReader(new FileReader(firstHandler.DEFAULTDIR
				+ "data/walking_30_6.csv"));
		List<String[]> temp = reader.readAll();
		// String[][] data =
		reader.close();

		String[][] data = new String[temp.size()][1];
		// returnArray = temp.toArray(returnArray);
		int k = 0;
		for (int i = 0; i < temp.size(); i++) {
			if (Double.parseDouble(temp.get(i)[1]) != -100) {

				double dist = 0;

				for (int j = 1; j < 4; j++) {

					dist += Double.parseDouble(temp.get(i)[j])
							* Double.parseDouble(temp.get(i)[j]);

				}
				dist = Math.sqrt(dist);
				data[k][0] = Double.toString(dist);
				k++;
			}
		}

		CSVWriter writer0 = new CSVWriter(new FileWriter(
				firstHandler.DEFAULTDIR + "data.csv"));
		for (int i = 0; i < k; i++) {
			String[] aa = { Integer.toString(i), data[i][0] };
			writer0.writeNext(aa);

		}

		writer0.close();
		// A - state transition matrix
		// B - control input matrix
		// H - measurement matrix
		// Q - process noise covariance matrix
		// R - measurement noise covariance matrix
		// P - error covariance matrix
		// A = [ 1 ]
		RealMatrix A = new Array2DRowRealMatrix(new double[] { 0 });
		// B = null no control input
		RealMatrix B = null;
		// H = [ 1 ]
		RealMatrix H = new Array2DRowRealMatrix(new double[] { 1d });
		// x = [ 10 ]
		RealVector x = new ArrayRealVector(new double[] { 1d }); // constant voltage
		// Q = [ 1e-5 ]
		RealMatrix Q = new Array2DRowRealMatrix(new double[] { 1e-3d });//process noise
		// P = [ 1 ]
		RealMatrix P0 = new Array2DRowRealMatrix(new double[] { 1d });
		// R = [ 0.1 ]
		RealMatrix R = new Array2DRowRealMatrix(new double[] { .1d });//measurement noise

		ProcessModel pm = new DefaultProcessModel(A, B, Q, x, P0);
		MeasurementModel mm = new DefaultMeasurementModel(H, R);
		KalmanFilter filter = new KalmanFilter(pm, mm);

		RealVector pNoise = new ArrayRealVector(1);
		RealVector mNoise = new ArrayRealVector(1);
		CSVWriter writer = new CSVWriter(new FileWriter(firstHandler.DEFAULTDIR
				+ "filtered.csv"));
		String[] header = { "x", "f(x)" };
		writer.writeNext(header);

		for (int i = 0; i < k; i++) {
			filter.predict();

			// simulate the process
			// System.out.println(i + "/" + k + " = ");

			//pNoise.setEntry(0, Double.parseDouble(data[i][0]));
			pNoise.setEntry(0, 0);

			// x = A * x + p_noise
			x = A.operate(x).add(pNoise);

			// simulate the measurement
			mNoise.setEntry(0, Double.parseDouble(data[i][0]));

			// z = H * x + m_noise
			RealVector z = H.operate(x).add(mNoise);

			filter.correct(z);

			String[] result = { Integer.toString(i),
					Double.toString(filter.getStateEstimation()[0]) };
			writer.writeNext(result);
//			System.out.print(result[1]);
		}
		writer.close();
	}
}
