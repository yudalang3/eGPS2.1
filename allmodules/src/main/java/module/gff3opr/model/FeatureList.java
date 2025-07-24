package module.gff3opr.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

//List<Feature> allFeatures = new ArrayList<>(1 << 23);
/**
 * 事实证明还是用LinkedList添加比较快
 */
//List<GFF3Feature> allFeatures = new LinkedList<>();
@SuppressWarnings("serial")
public class FeatureList extends LinkedList<GFF3Feature> {

	/**
	 * Construct an empty list.
	 */
	public FeatureList() {

	}

	/**
	 * Construct a new list containing the same features as the specified list.
	 *
	 * @param features An existing list or collection of GFF3Feature objects.
	 */
	public FeatureList(Collection<GFF3Feature> features) {
		this.add(features);
	}

	/**
	 * Add specified feature to the end of the list. Updates the bounding location
	 * of the feature list, if needed.
	 *
	 * @param feature The GFF3Feature object to add.
	 * @return True if the feature was added.
	 */
	@Override
	public boolean add(GFF3Feature feature) {
		return super.add(feature);
	}

	/**
	 * Add all features in the specified list or collection to this list.
	 *
	 * @param list The collection of GFF3Feature objects.
	 */
	public void add(Collection<GFF3Feature> list) {
		for (GFF3Feature f : list) {
			add(f);
		}
	}


	/**
	 * Create a collection of all unique group ids in the list, as defined by the
	 * group() method of the features. For example, if the features are from a GFF1
	 * file, then each group id identifies a particular gene, and this method
	 * returns a collection of all gene ids.
	 *
	 * @return ElegantJTable collection (suitable for iteration using Java's "for" loop) of all
	 *         the group ids found in this list. The order of the values is
	 *         undefined; it will not match the order of features in the list.
	 */
	public Collection<String> groupValues() {
		Set<String> set = new HashSet<>();
		for (GFF3Feature f : this) {
			// enter in a set -- removes duplicates
			set.add(f.group());
		}

		return set;
	}

	/**
	 * Create a collection of the unique values for the specified key. Example: For
	 * GTF files, using the "gene_id" key will give the names of all the genes in
	 * this list.
	 *
	 * @return ElegantJTable collection (suitable for iteration using Java's "for" loop) of all
	 *         the values found for this key. The order of the values is undefined;
	 *         it will not match the order of features in the list.
	 */
	public Collection<String> attributeValues(String key) {
		LinkedHashMap<String, String> hash = new LinkedHashMap<>();
		for (GFF3Feature f : this) {
			// enter as a key -- removes duplicates
			hash.put(f.getAttribute(key), null);
		}

		return Collections.unmodifiableCollection(hash.keySet());
	}

	/**
	 * Create a list of all features that have the specified group id, as defined by
	 * the group() method of the features.
	 *
	 * @param groupid The group to match.
	 * @return ElegantJTable list of features having the specified group id.
	 */
	public FeatureList selectByGroup(String groupid) {
		FeatureList list = new FeatureList();
		for (GFF3Feature f : this) {
			if (f.group().equals(groupid)) {
				list.add(f);
			}
		}

		return list;
	}

	/**
	 * Create a list of all features that are of the specified type, as defined by
	 * the type() method of the features. This might be, for example, "exon" or
	 * "CDS".
	 *
	 * @param type The type to match.
	 * @return ElegantJTable list of features of the specified type.
	 */
	public FeatureList selectByType(String type) {
		FeatureList list = new FeatureList();
		for (GFF3Feature f : this) {
			if (f.type().equals(type)) {
				list.add(f);
			}
		}

		return list;
	}

	/**
	 * Create a list of all features that include the specified attribute key/value
	 * pair. This method now properly supports adding the index before or after
	 * adding the features. Adding features, then then index, then more features is
	 * still not supported.
	 *
	 * @param key   The key to consider.
	 * @param value The value to consider.
	 * @return ElegantJTable list of features that include the key/value pair.
	 */
	public FeatureList selectByAttribute(String key, String value) {
		FeatureList list = new FeatureList();
		for (GFF3Feature f : this) {
			if (f.hasAttribute(key, value)) {
				list.add(f);
			}
		}
		return list;
	}

	/**
	 * Create a list of all features that include the specified attribute key.
	 *
	 * @param key The key to consider.
	 * @return ElegantJTable list of features that include the key.
	 */
	public FeatureList selectByAttribute(String key) {
		FeatureList list = new FeatureList();
		for (GFF3Feature f : this) {
			if (f.hasAttribute(key)) {
				list.add(f);
			}
		}
		return list;
	}

	/**
	 * Create a list of all features that include the specified key/value pair in
	 * their userMap().
	 *
	 * @param key   The key to consider.
	 * @param value The value to consider.
	 * @return ElegantJTable list of features that include the key/value pair.
	 */
	public FeatureList selectByUserData(String key, Object value) {
		FeatureList list = new FeatureList();
		for (GFF3Feature f : this) {
			Object o = f.userData().get(key);
			if (o != null && o.equals(value)) {
				list.add(f);
			}
		}
		return list;
	}

	/**
	 * Create a list of all features that include the specified key in their
	 * userMap().
	 *
	 * @param key The key to consider.
	 * @return ElegantJTable list of features that include the key.
	 */
	public FeatureList selectByUserData(String key) {
		FeatureList list = new FeatureList();
		for (GFF3Feature f : this) {
			if (f.userData().containsKey(key)) {
				list.add(f);
			}
		}
		return list;
	}
	/**
	 * Check if any feature in list has the specified attribute key.
	 *
	 * @param key The attribute key to consider.
	 * @return True if at least one feature has the attribute key.
	 */
	public boolean hasAttribute(String key) {
		for (GFF3Feature f : this) {
			if (f.hasAttribute(key)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check if any feature in list has the specified attribute key/value pair.
	 *
	 * @param key   The attribute key to consider.
	 * @param value The attribute value to consider.
	 * @return True if at least one feature has the key/value pair.
	 */
	public boolean hasAttribute(String key, String value) {
		for (GFF3Feature f : this) {
			if (f.hasAttribute(key, value)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Return a string representation of all features in this list.
	 *
	 * @return ElegantJTable string.
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("FeatureList: >>\n");
		for (GFF3Feature f : this) {
			s.append(f.seqname() + ":" + f.toString() + "\n");
		}

		s.append("\n<<\n");
		return s.toString();
	}

	/**
	 * used by sort routine
	 */
	private class FeatureComparator implements Comparator<GFF3Feature>, Serializable {
		private static final long serialVersionUID = 1;

		@Override
		public int compare(GFF3Feature a, GFF3Feature b) {
			if (a.seqname().equals(b.seqname()) && a.location().isSameStrand(b.location())) {
				return a.location().start - b.location().start; // sort on start
			} else {
				throw new IndexOutOfBoundsException(
						"Cannot compare/sort features whose locations are on opposite strands or with different seqname().\r\n"
								+ a.toString() + "\r\n" + b.toString());
			}
		}
	}

	/**
	 * Create a new list that is ordered by the starting index of the features'
	 * locations. All locations must be on the same strand of the same sequence.
	 *
	 * @return An ordered list.
	 * @throws IndexOutOfBoundsException Cannot compare/sort features whose
	 *                                   locations are on opposite strands, or whose
	 *                                   seqnames differ.
	 */
	public FeatureList sortByStart() {
		GFF3Feature[] array = toArray(new GFF3Feature[1]);

		Arrays.sort(array, new FeatureComparator());

		return new FeatureList(Arrays.asList(array));
	}

	/**
	 * 输入一个List，给出List所有坐标构成的并集的长度。 注意：这里对输入有限制，必须要是没有Overlap的features
	 * 
	 * @param list
	 * @return
	 */
	public int getFeatureListLength() {
		int ret = 0;

		for (GFF3Feature fe : this) {
			ret += fe.location().length();
		}

		return ret;
	}

	/**
	 * Note: the results is excluding the nonsense mediated decay
	 * 
	 * @return
	 */
	public GFF3Feature geneWithLongestCDS() {

		FeatureList selectByType = selectMRNAFeatureList();

		int maxCDSLength = -100000;

		GFF3Feature longestMRNAFeature = null;
		for (GFF3Feature feature : selectByType) {
			String attribute = feature.getAttribute("ID");

			FeatureList selectByAttribute = this.selectByAttribute("Parent", attribute);

			FeatureList cdsFeatureList = selectByAttribute.selectByType("CDS");
			int featureListLength = cdsFeatureList.getFeatureListLength();
			if (featureListLength < 1) {
				throw new InputMismatchException("Please check your gff file, CDS length less 1.");
			}

			if (cdsFeatureList.isEmpty()) {
				throw new InputMismatchException("Please check your gff file, no CDS type records found.");
			}
			GFF3Feature firstCDSFeature = cdsFeatureList.get(0);

			String protName = firstCDSFeature.getAttribute("protein_id");

//			logger.trace("Gene is {}, This is mRNA {}, CDS feature sum is {} , protName is {} ",
//					feature.getAttribute("Name"), attribute, featureListLength, protName);

			if (featureListLength > maxCDSLength) {
				maxCDSLength = featureListLength;
				longestMRNAFeature = feature;
			}
		}

		return longestMRNAFeature;
	}

	/**
	 * Note: the results is excluding the nonsense mediated decay
	 * 
	 * @return
	 */
	public FeatureList selectMRNAFeatureList() {

		FeatureList selectByType = this.selectByType("mRNA");
		Iterator<GFF3Feature> iterator = selectByType.iterator();

		while (iterator.hasNext()) {
			GFF3Feature feature = iterator.next();
			String biotype = feature.getAttribute("biotype");
			if ("nonsense_mediated_decay".equalsIgnoreCase(biotype)) {
				iterator.remove();
			}
		}

		return selectByType;
	}
	
	
	


	/**
	 * <pre>
	 * feature of gene, id is the parameter id.
	 * 输入 gene feature的 id attribute;
	 * 返回 所有这个 gene对应的 feature list
	 * feature就是 gff3文件中的行
	 * </pre>
	 * 
	 * @param id
	 * @return
	 */
	public FeatureList selectFeatureList4gene(String id) {
		FeatureList list = new FeatureList();

		Iterator<GFF3Feature> iterator = this.iterator();
		boolean searched = false;

		while (iterator.hasNext()) {
			GFF3Feature next = iterator.next();

			if (next.hasAttribute("ID", id)) {
				list.add(next);
				searched = true;

				break;
			}
		}

		if (searched) {
			while (iterator.hasNext()) {
				GFF3Feature following = iterator.next();
				String type = following.type();
				if (Objects.equals(type, "biological_region") || Objects.equals(type, "gene")) {
					break;
				}
				list.add(following);
			}
		}

		return list;
	}

}