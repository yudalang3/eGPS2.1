package module.evolview.model.tree;

import java.util.Objects;

public class CGBID {

	public final static int DEFAULT_CGBID_INDEX = -100;
	public final static String CGBID_PREFIX = "CGB";

	protected int cgbIDFirst = DEFAULT_CGBID_INDEX;
	protected int cgbIDLast = DEFAULT_CGBID_INDEX;

	public CGBID() {
		
	}
	
	public static CGBID build(String string) {
		int indexOf = string.indexOf('.');
		CGBID cgbid = new CGBID();
		
		
		if (indexOf == -1) {
			if (string.startsWith(CGBID_PREFIX)) {
				int first = Integer.parseInt(string.substring(3));
				cgbid.setFirstID(first);
			}else {
				int first = Integer.parseInt(string);
				cgbid.setFirstID(first);
			}
			
			return cgbid;
		}
		
		
		int firstItemIndex = 0;
		if (string.startsWith(CGBID_PREFIX)) {
			firstItemIndex = 3;
		}		
		int first = Integer.parseInt(string.substring(firstItemIndex, indexOf));
		int last = Integer.parseInt(string.substring(indexOf + 1));
		cgbid.setFirstID(first);
		cgbid.setSecondID(last);
		return cgbid;
	}

	public int getFirstID() {
		return cgbIDFirst;
	}

	public void setFirstID(int id) {
		cgbIDFirst = id;
	}
	
	public void setSecondID(int id) {
		cgbIDLast = id;
	}

	public int getSecondID() {
		return cgbIDLast;
	}

	/**
	 * 
	 * 返回没有CGB的单独的ID字符串，如“1.3”
	 *  
	 * @title getSimpleIDString
	 * @createdDate 2022-10-21 14:52
	 * @lastModifiedDate 2022-10-21 14:52
	 * @author yjn
	 * @since 1.7
	 *   
	 * @return
	 * @return String
	 */
	public String getSimpleIDString() {
		
		String valueOf1 = String.valueOf(cgbIDFirst);
		if (cgbIDLast == DEFAULT_CGBID_INDEX) {
			return valueOf1;
		}else {
			String valueOf2 = String.valueOf(cgbIDLast);
			return valueOf1.concat(".").concat(valueOf2);
		}
	}
	
	@Override
	public String toString() {
		char[] charArray1 = String.valueOf(getFirstID()).toCharArray();
		final int cgbSize = 3;
		int lengthOfArray1 = charArray1.length;

		int size = lengthOfArray1 + cgbSize;

		String ret = null;

		int cgbIDLast = getSecondID();
		if (cgbIDLast == CGBID.DEFAULT_CGBID_INDEX) {
			char[] array = new char[size];
			array[0] = 'C';
			array[1] = 'G';
			array[2] = 'B';

			System.arraycopy(charArray1, 0, array, cgbSize, lengthOfArray1);
			ret = new String(array);
		} else {
			char[] charArray2 = String.valueOf(cgbIDLast).toCharArray();
			size += (charArray2.length + 1);
			char[] array = new char[size];
			array[0] = 'C';
			array[1] = 'G';
			array[2] = 'B';

			System.arraycopy(charArray1, 0, array, cgbSize, lengthOfArray1);
			int middleLen = cgbSize + lengthOfArray1;
			array[middleLen] = '.';
			System.arraycopy(charArray2, 0, array, middleLen + 1, charArray2.length);

			ret = new String(array);
		}

		return ret;
	}



	@Override
	public int hashCode() {
		return Objects.hash(cgbIDFirst, cgbIDLast);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CGBID other = (CGBID) obj;
		return cgbIDFirst == other.cgbIDFirst && cgbIDLast == other.cgbIDLast;
	}
	

}
