package module.remnant.datapanel.data;

/**
 * 
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 * 
 * @ClassName: .java
 * 
 * @Package: egps.data
 * 
 * @author mhl
 * 
 * @version V1.0
 * 
 * @Date Created on: 2019-01-16 11:02
 * 
 */
public abstract class DataAlignmentFormat {

	public static final String A = "a"; // Adenine
	public static final String C = "c"; // Cytosine
	public static final String G = "g"; // Guanine
	public static final String T = "t"; // Thymine
	public static final String U = "u"; // Uracil
	public static final String R = "r"; // Guanine/Adenine(purine)
	public static final String Y = "y"; // Cytosine/Thymine(pyrimidine)
	public static final String K = "k"; // Guanine/Thymine
	public static final String M = "m"; // Adenine/Cytosine
	public static final String S = "s"; // Guanine/Cytosine
	public static final String W = "w"; // Adenine/Thymine
	public static final String B = "b"; // Guanine/Thymine/Cytosine
	public static final String D = "d"; // Guanine/Adenine/Thymine
	public static final String H = "h"; // Adenine/Cytosine/Thymine
	public static final String V = "v"; // Guanine/Cyto , sine/Thymine
	public static final String N = "n"; // Adenine/Guanine/Cytosine/Thymine
	public static final String X = "x"; // Masked
	public static final String isEmp = "-"; // By default, the default is -

	public static boolean ifAlignmentFormat(String singleChar) {
		if (!A.equalsIgnoreCase(singleChar)
				&& !C.equalsIgnoreCase(singleChar)
				&& !G.equalsIgnoreCase(singleChar)
				&& !T.equalsIgnoreCase(singleChar)
				&& !isEmp.equalsIgnoreCase(singleChar)
				&& !U.equalsIgnoreCase(singleChar)
				&& !R.equalsIgnoreCase(singleChar)
				&& !Y.equalsIgnoreCase(singleChar)
				&& !K.equalsIgnoreCase(singleChar)
				&& !M.equalsIgnoreCase(singleChar)
				&& !S.equalsIgnoreCase(singleChar)
				&& !W.equalsIgnoreCase(singleChar)
				&& !B.equalsIgnoreCase(singleChar)
				&& !D.equalsIgnoreCase(singleChar)
				&& !H.equalsIgnoreCase(singleChar)
				&& !V.equalsIgnoreCase(singleChar)
				&& !X.equalsIgnoreCase(singleChar)
				&& !N.equalsIgnoreCase(singleChar)) {
			return false;
		} else {
			return true;
		}
	}
}
