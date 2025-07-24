package module.heatmap.eheatmap.tree;

import evoltree.struct.EvolNode;

/*
 * TreeCoder.java
 *
 * (((T1,T4),(T17,(T16,(T15,(T18,T20))))),((T19,(((T11,(T14,(T2,T13,(T21,T22)))),(T5,T9)),(T7,T8,T10))),(T6,(T3,T12))));
 *
 * (((a:8.5,b:8.5):2.5,e:11.0):5.5,(c:14.0,d:14.0):2.5):0.0;
 * 
 * Created on February 11, 2004, 4:46 PM
 */



/**
 * @author  haipeng
 */
public class TreeCoder {
    
    
    public static String code(EvolNode root) {
    	return codeForInternalUse(root) + ";";
    }
    
	private static String codeForInternalUse(EvolNode root) {
		if (root.getChildCount() == 0) {
			String name = root.getName();
			if (name == null)
				name = "NoName";
			String str = name + ":"+ root.getLength();
			return str;
		}

		String code = "(";
		for (int i = 0; i < root.getChildCount(); i++) {
			code += codeForInternalUse(root.getChildAt(i));
			if (i < root.getChildCount() - 1)
				code += ",";
		}
		code += "):";
		/**
		 * leave for bootstrap !
		 */
		code += root.getLength();
		return code;
	}
}
