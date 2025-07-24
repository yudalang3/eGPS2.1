package module.evoldist.operator;

import egps2.panels.dialog.SwingDialog;

public class EvoDistanceUtil {
	
	private final static char[] ACTG_ARRAY = { 'A', 'G', 'C', 'T' };
	private final static int TYPE_OF_NUCL = 4;
	
	/**
	 * @return boolean[] :<br>
	 * First element indicate whether there exist at least one is positive.<br>
	 *                   Second element indicate whether all element are non-NaN value!
	 */
	public final static boolean[] evaluateDistM(double[][] distM) {
		boolean hasElementNotZero = false;
		boolean dontHaveNaN = true;
		
		for (double[] ds : distM) {
			for (double d : ds) {
				if (Double.isNaN(d)) {
					dontHaveNaN = false;
				}else {
					if (d > 0) {
						hasElementNotZero = true;
					}
				}
				
			}
		}
		
		return new boolean[] {hasElementNotZero,dontHaveNaN};
	}
	
	public static void dialogAppear(boolean[] evaluateDistM) throws Exception {
		if (!evaluateDistM[0]) {
			SwingDialog.showErrorMSGDialog("Genetic distance error",
					"All elements in the genetic distance matrix are zero!\n"
							+ "If your parameter to deal with deletion is \"Compelete deletion\",you may using other settings!"
							+ "Else you can change your input string!");
			throw new Exception("Genetic distance error");
		}
		if (!evaluateDistM[1]) {
			SwingDialog.showErrorMSGDialog("Genetic distance error",
					"Some elements in the genetic distance matrix are NaN!\n"
							+ "You may change the genetic disance method in Preference!");
			throw new Exception("Genetic distance error");
		}
	}
	
	public final static int getFirstIndexZeroBased(char c) {
		for (int i = 0; i < TYPE_OF_NUCL; i++) {
			char iterC = ACTG_ARRAY[i];
			if (iterC == c) {
				return i;
			}
		}
		return -1;
	}
	

}
