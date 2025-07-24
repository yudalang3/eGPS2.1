package rest.ensembl.ensembrest;

import com.google.common.base.Joiner;

/**
 * <pre>
 *      {
 *     "seq_region_name": "ENSP00000363827",
 *     "interpro": "IPR000742",
 *     "translation_id": 1169849,
 *     "type": "Pfam",
 *     "cigar_string": "",
 *     "hseqname": "PF00008",
 *     "end": 4138,
 *     "start": 4108,
 *     "id": "PF00008",
 *     "hit_end": 31,
 *     "feature_type": "protein_feature",
 *     "align_type": null,
 *     "description": "EGF-like domain",
 *     "hit_start": 1,
 *     "Parent": "ENST00000374695"
 *   },
 * </pre>
 */
public class OverlapTransElementBean {

	private String seq_region_name;
	private String id;
	private String hseqname;
	private String description;
	private String type;
	private String interpro;
	private String hit_start;
	private String hit_end;
	private String feature_type;
	private String start;
	private String end;
	private String align_type;
	private String cigar_string;
	private String translation_id;
	private String Parent;

	
	@Override
	public String toString() {
		//输出你自己想要的信息
		String join = Joiner.on('\t').join(id, start, end, type, feature_type, description );
		
		return join;

	}
	
	public String getHit_start() {
		return hit_start;
	}
	public void setHit_start(String hit_start) {
		this.hit_start = hit_start;
	}
	public String getFeature_type() {
		return feature_type;
	}
	public void setFeature_type(String feature_type) {
		this.feature_type = feature_type;
	}
	public String getHit_end() {
		return hit_end;
	}
	public void setHit_end(String hit_end) {
		this.hit_end = hit_end;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAlign_type() {
		return align_type;
	}
	public void setAlign_type(String align_type) {
		this.align_type = align_type;
	}
	public String getCigar_string() {
		return cigar_string;
	}
	public void setCigar_string(String cigar_string) {
		this.cigar_string = cigar_string;
	}
	public String getTranslation_id() {
		return translation_id;
	}
	public void setTranslation_id(String translation_id) {
		this.translation_id = translation_id;
	}
	public String getInterpro() {
		return interpro;
	}
	public void setInterpro(String interpro) {
		this.interpro = interpro;
	}
	public String getParent() {
		return Parent;
	}
	public void setParent(String parent) {
		Parent = parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSeq_region_name() {
		return seq_region_name;
	}
	public void setSeq_region_name(String seq_region_name) {
		this.seq_region_name = seq_region_name;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getHseqname() {
		return hseqname;
	}
	public void setHseqname(String hseqname) {
		this.hseqname = hseqname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
