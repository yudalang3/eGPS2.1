package module.remnant.treeoperator;
/*
 * TreeCoder.java
 *
 * (((T1,T4),(T17,(T16,(T15,(T18,T20))))),((T19,(((T11,(T14,(T2,T13,(T21,T22)))),(T5,T9)),(T7,T8,T10))),(T6,(T3,T12))));
 *
 * (((a:8.5,b:8.5):2.5,e:11.0):5.5,(c:14.0,d:14.0):2.5):0.0;
 * 
 * Created on February 11, 2004, 4:46 PM
 */

import java.text.DecimalFormat;

/**
 * @author yudalang
 */
public class ETreeCoder {
	public DecimalFormat decimal = null;

	/** Creates a new instance of TreeCoder */
	public ETreeCoder() {
	}

	public ETreeCoder(DecimalFormat decimal) {
		this.decimal = decimal;
	}

	public String code(GNode root) {
		return codeForInternalUse(root) + ";";
	}

	private String codeForInternalUse(GNode root) {
		if (root.getChildCount() == 0) {
			String name = root.getLeafName();
			if (name == null)
				name = "NoName";
			String str = name + ":";
			if (decimal == null) {
				str += root.getBranch(0).getLength();
			} else {
				str += decimal.format(root.getBranch(0).getLength());
			}
			return str;
		}

		String code = "(";
		for (int i = 0; i < root.getChildCount(); i++) {
			code += codeForInternalUse(root.getChildAt(i));
			if (i < root.getChildCount() - 1)
				code += ",";
		}
		// Edited by GF
		if (root.getBootstrapValue() != 0) {
			code = code + ")";
			code = code + root.getBootstrapValue();
			code = code + ":";
		} else {
			code = code + "):";
		}

		if (decimal == null) {
			code += root.getBranch(0).getLength();
		} else {
			code += decimal.format(root.getBranch(0).getLength());
		}
		return code;
	}
}
