package module.remnant.treeoperator.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Hold a collection of numbers which are either int or double type and
 * associate them with weights (often as number of occurrence). The numbers put
 * into the collection are divided into groups each is identified by an integer.
 * The grouping is done by first multiplying the input number by a parameter
 * named Factor (which by default is 10) and then casting the result as integer.
 *
 * In addition to being a standalone class, an object of Distribution can be
 * used as an intermidiate storage, both as event listner and event generator.
 *
 * @author fu
 * @version
 */

// modified by xwu. 02/25/2002, 04/01/02, 04/02/02

public class Distribution extends Object implements PropertyChangeListener, java.io.Serializable {

	protected static final String PROPERTY = "Distribution";
	protected PropertyChangeSupport propertySupport;
	protected int nEntry = 0;
	protected final Map map;
	protected Map treemap = new TreeMap();
	protected int nUpdate = 0;
	protected int updateInterval = 10;
	protected double factor = 10.0; // multiplication factor for converting
	// keys to integers
	protected String name = "";

	protected final Key key = new Key(0);

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getNEntry() {
		return nEntry;
	}

	protected class Count implements java.io.Serializable {
		public double count;

		public Count(double count) {
			this.count = count;
		}

		public String toString() {
			String str = "" + count;
			return str;
		}
	}

	protected class Key implements Comparable, Cloneable, java.io.Serializable {
		protected int value;

		public Key(double value) {
			this.value = (int) (value * factor);
		}

		public int getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = (int) (value * factor);
		}

		public int hashCode() {
			return value;
		}

		public boolean equals(Object a) {
			if (!(a instanceof Key))
				return false;
			if (value != ((Key) a).getValue())
				return false;
			return true;
		}

		public int compareTo(Object a) {
			if (!(a instanceof Key))
				return 1;
			int val = ((Key) a).getValue();
			if (value < val)
				return -1;
			if (value > val)
				return 1;
			return 0;
		}

		public Object clone() {
			try {
				return super.clone();
			} catch (Exception e) {
			}
			return null;
		}
	}

	/** Creates new Distribution */
	public Distribution() {
		// map = new HashMap();
		map = new TreeMap();
		propertySupport = new PropertyChangeSupport(this);
	}

	public Distribution(int iniCapacity) {
		map = new HashMap(iniCapacity);
		propertySupport = new PropertyChangeSupport(this);
	}

	/**
	 * Set the converting factor used for converting input value into a group. Each
	 * value added is put into a group identified by integer (int)(value*factor).
	 * 
	 * Factor thus determines how group is done. If factor=1, the decimal portion of
	 * an input value is ignored. If factor =10, then the first decimal number is
	 * significant in grouping. On the other hand, if factor=0.01, values are
	 * grouped in hundreds.
	 */
	public void setFactor(double x) {
		factor = x;
	}

	public double getFactor() {
		return factor;
	}

	/**
	 * set notification interval. That is, every k calls to put() will cause
	 * notfication to all the listners.
	 */
	public void setNotificationInterval(int k) {
		updateInterval = k;
	}

	public int getNotificationInterval() {
		return updateInterval;
	}

	/**
	 * Return the number of groups in the distribution
	 */
	public int size() {
		return map.size();
	}

	/**
	 * Clear the distribution. That is, removing all the entries and their
	 * frequencies.
	 */
	public void clear() {
		map.clear();
		nUpdate = 0;
		nEntry = 0;
	}

	/**
	 * Put all the entries in a given Distribution into this one.
	 */
	public void putAll(Distribution dis) {
		/*
		 * double yy=0.0e0; double[][] c= getFrequency(); for(int i=c[0].length-1; i>=0;
		 * i--){ yy+= c[1][i]; }
		 */

		double[][] b = dis.getFrequency();
		// double xx=0.0e0;
		for (int i = b[0].length - 1; i >= 0; i--) {
			put(b[0][i], b[1][i]);
			// xx+= b[1][i];
		}

		// System.out.println(xx+ " "+yy);
	}

	/**
	 * Put an array of values into the distribtuion
	 */
	public void put(double[] x, double[] y) {
		for (int i = 0; i < x.length; i++) {
			put(x[i], y[i]);
		}
	}

	/**
	 * Put the number x into the distribtion. If x is already in, then its frequency
	 * is increased by 1.
	 */
	public void put(double x) {
		put(x, 1.0e0);
	}

	/**
	 * Put the number x into the distribtion. If x is not yet in the distribution,
	 * its frequency is set to the value of count, otherwise, its frequency is
	 * increased by count amount.
	 */
	public void put(double x, double count) {
		nEntry++;
		key.setValue(x);
		Count c = (Count) map.get(key);
		if (c == null)
			map.put(key.clone(), new Count(count));
		else
			c.count += count;

		nUpdate = (++nUpdate) % updateInterval;
		if (nUpdate == 0) {
			if (propertySupport.hasListeners(PROPERTY)) {
				propertySupport.firePropertyChange(PROPERTY, null, getFrequency());
			}
			printSummary();
		}
	}

	/**
	 * Return the frequency (weight) of number x
	 */
	public double getFrequency(double x) {
		key.setValue(x);
		Count c = (Count) map.get(key);
		if (c == null)
			return 0.0e0;
		else
			return (double) c.count;
	}

	/**
	 * Return a two dimensional array representing the frequency of all entries. The
	 * returned array (say data[][]) will store the group identification in
	 * data[0],whose length is equal to size(), and their associated frequency(
	 * weights) in data[1];
	 */
	public double[][] getFrequency() {
		if (map instanceof TreeMap)
			treemap = map;
		else {
			treemap.clear();
			treemap.putAll(map);
		}
		double[][] data = new double[2][treemap.size()];
		Iterator b = treemap.entrySet().iterator();
		int i = 0;
		while (b.hasNext()) {
			Map.Entry entry = (Map.Entry) b.next();
			data[0][i] = ((Key) entry.getKey()).getValue() / factor;
			data[1][i] = ((Count) entry.getValue()).count;
			i++;
		}
		return data;
	}

	/**
	 * Return a summary
	 */
	public String toString() {
		double[][] a = getFrequency();
		StringBuffer str = new StringBuffer();

		for (int i = 0, l = a[0].length; i < l; i++) {
			str.append(a[0][i]);
			str.append(" " + a[1][i] + "\n");
		}
		return str.toString();
	}

	/**
	 * Return mean and variance. Here frequency will be normalized so that they are
	 * added to 1.
	 */
	public static double[] summary(double[][] a) {
		double mean = 0.0e0, var = 0.0e0, sum = 0.0e0;
		for (int i = 0, l = a[0].length; i < l; i++) {
			mean += a[0][i] * a[1][i];
			sum += a[1][i];
			var += (a[0][i] * a[0][i]) * a[1][i];
		}
		mean /= sum;
		var /= sum;
		var -= mean * mean;
		double[] summary = new double[3];
		summary[0] = mean;
		summary[1] = var;
		summary[2] = sum;
		// System.out.println("sum="+sum);
		return summary;
	}

	public double[] summary() {
		return summary(getFrequency());
	}

	protected double[][] crits = new double[3][2];

	public void printSummary() {
		/*
		 * double[][] xy=getFrequency();
		 * 
		 * double[] summ = summary(xy);
		 * 
		 * System.out.print( "\nMean="+TextUtility.out(summ[0],7,5)+" var="+
		 * TextUtility.out(summ[1],7,5) + ((name=="")?"":(" Name="+ name))
		 * +". Entries="+nEntry); System.out.println(" Time="+timer.time());
		 * 
		 * System.arraycopy(getCriticalValue(0.010, xy),0,crits[0],0,2);
		 * System.arraycopy(getCriticalValue(0.025, xy),0,crits[1],0,2);
		 * System.arraycopy(getCriticalValue(0.050, xy),0,crits[2],0,2);
		 * 
		 * System.out.println("Lower and upper 1% 2% 5% points ="+
		 * +crits[0][0]+" "+crits[1][0]+" "+crits[2][0]+
		 * "  "+crits[2][1]+" "+crits[1][1]+" "+crits[0][1]);
		 */
	}

	public double[] getSummary() {
		double[][] xy = getFrequency();
		double[] summ = summary(xy);
		return summ;
		/*
		 * StringBuffer ss=new StringBuffer();
		 * 
		 * ss.append( "Mean="+TextUtility.out(summ[0],7,2)+" var="+
		 * TextUtility.out(summ[1],7,2) + ((name=="")?"":(" Name="+ name))
		 * +". Entries="+ TextUtility.out(summ[2],2,0) +"\n");
		 */
		/*
		 * System.arraycopy(getCriticalValue(0.010, xy),0,crits[0],0,2);
		 * System.arraycopy(getCriticalValue(0.025, xy),0,crits[1],0,2);
		 * System.arraycopy(getCriticalValue(0.050, xy),0,crits[2],0,2);
		 * 
		 * ss.append("Lower and upper 1% 2% 5% points ="+
		 * +crits[0][0]+" "+crits[1][0]+" "+crits[2][0]+
		 * "  "+crits[2][1]+" "+crits[1][1]+" "+crits[0][1]);
		 */
		/*
		 * return ss.toString();
		 */
	}

	public double[] getCriticalValue(double perc) {
		return getCriticalValue(perc, getFrequency());
	}

	protected double[] crit = new double[2];

	public double[] getCriticalValue(double perc, double[][] freq) {
		// need to add assertion on 0< perc < 1

		double no, total = 0.0e0;
		for (int i = freq[1].length - 1; i >= 0; i--)
			total += freq[1][i];

		//
		// lower tail
		//
		no = 0.0e0;
		for (int l = freq[1].length, i = 0; i < l; i++) {
			no += freq[1][i];
			if (no / total >= perc) {
				crit[0] = freq[0][i];
				break;
			}
		}
		//
		// up tail
		//
		no = 0.e0;
		for (int i = freq[1].length - 1; i >= 0; i--) {
			no += freq[1][i];
			if (no / total >= perc) {
				crit[1] = freq[0][i];
				break;
			}
		}
		return crit;
	}

	/**
	 * implement the method in interface PropertyChangeListener
	 */
	public void propertyChange(PropertyChangeEvent e) {
		Object a = e.getNewValue();
		if (!(a instanceof Double))
			return;
		put(((Double) a).doubleValue());
	}

	/**
	 * add listeners, so that at a certain interval, listeners will be notified.
	 * 
	 * @see setNotificationInterval(int k)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener);
	}

	/**
	 * Test
	 */
	public static void main(String[] args) {
		double[] a = { 1.0, 1.2, 1.0, 10, 5, 5, 0.5, 0.6, 0.6 };
		Distribution dis = new Distribution();
		dis.setFactor(10);
		dis.setNotificationInterval(100);

		PropertyChangeListener ls = new PropertyChangeListener() {
			int i = 0;

			public void propertyChange(PropertyChangeEvent e) {
				System.out.println("event " + (++i) + " arrived");
				double[][] aa = (double[][]) e.getNewValue();
				for (int i = 0; i < aa[0].length; i++) {
					System.out.println(aa[0][i] + " " + aa[1][i]);
				}
			}
		};
		dis.addPropertyChangeListener(ls);

		for (int k = 0; k < a.length; k++) {
			dis.put(a[k]);
		}
		for (int k = 0; k < 500; k++) {
			dis.put(Math.random());
		}
		for (int i = 0; i < 50; i++)
			dis.put(5);
		System.out.println(dis);
	}
}
