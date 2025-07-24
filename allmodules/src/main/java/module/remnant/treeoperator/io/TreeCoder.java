package module.remnant.treeoperator.io;

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

import module.remnant.treeoperator.NodeEGPSv1;

/**
 *
 * @author  haipeng
 */
public class TreeCoder {
	public static DecimalFormat decimal = null;
    
    /** Creates a new instance of TreeCoder */
    public TreeCoder() {
    }
    
//    public static String code(Node root) {
//        if (root.getChildCount() == 0) {
//            String name = root.getLeafName();
//            if (name == null) name = "NoName";
//            String str = name + ":";
//            if (decimal == null) {
//                str += root.getBranch().getLength();
//            }
//            else {
//                str += decimal.format(root.getBranch().getLength());
//            }
//            return str;
//        }
//        
//        String code = "(";
//        for (int i = 0; i < root.getChildCount(); i ++) {
//            code += code(root.getChildAt(i));
//            if (i < root.getChildCount() - 1) code += ",";
//        }
//        // Edited by GF
//        if(root.getBs()!=0) {
//        	code = code +")";
//        	code = code+ root.getBs();
//        	code = code +":";
//        } else {
//        	code = code +"):";
//        }
//        
//        if (decimal == null) {
//            code += root.getBranch().getLength();
//        }
//        else {
//            code += decimal.format(root.getBranch().getLength());
//        }
//        return code+";";
//    }
    
    public static String code(NodeEGPSv1 root) {
    	return codeForInternalUse(root) + ";";
    }
    
    private static String codeForInternalUse(NodeEGPSv1 root) {
        if (root.getChildCount() == 0) {
            String name = root.getLeafName();
            if (name == null) name = "NoName";
            String str = name + ":";
            if (decimal == null) {
                str += root.getBranch().getLength();
            }
            else {
                str += decimal.format(root.getBranch().getLength());
            }
            return str;
        }
        
        String code = "(";
        for (int i = 0; i < root.getChildCount(); i ++) {
            code += codeForInternalUse(root.getChildAt(i));
            if (i < root.getChildCount() - 1) code += ",";
        }
        // Edited by GF
        if(root.getBs()!=0) {
        	code = code +")";
        	code = code+ root.getBs();
        	code = code +":";
        } else {
        	code = code +"):";
        }
        
        if (decimal == null) {
            code += root.getBranch().getLength();
        }
        else {
            code += decimal.format(root.getBranch().getLength());
        }
        return code;
    }
}
