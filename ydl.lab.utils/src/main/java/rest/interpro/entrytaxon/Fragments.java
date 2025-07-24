package rest.interpro.entrytaxon;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * https://blog.csdn.net/quan20111992/article/details/88918585
 * 这里出现了一个问题，就是 JAVA变量名是不支持 - 字符串的，但是JSON里面有，所以就用这个了。
 * @author yudal
 *
 */
public class Fragments {

	int start;
	int end;
	
	@JSONField(name = "dc-status")
	String dc_status;
	boolean representative;
	
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getDc_status() {
		return dc_status;
	}
	public void setDc_status(String dc_status) {
		this.dc_status = dc_status;
	}
	public boolean isRepresentative() {
		return representative;
	}
	public void setRepresentative(boolean representative) {
		this.representative = representative;
	}
	
	
	
}
