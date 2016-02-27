import java.io.IOException;


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
		for(int i = 0; i < data.length; i++){
			for(int j = 0; j < data[i].length;j++){
				System.out.print(data[i][j]+" ");				 
			}
			System.out.print("\n");
		}

	}

}
