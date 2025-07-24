package module.multiseqview.model;

import java.util.List;

import com.google.common.base.Joiner;

/**
 * 一条 Seq has list of StructElement
 * @author yudal
 *
 */
public class Sequence4import {

	private int length;
	private String name;
	
	private List<StructureElement> structureElementLists;
	
	public Sequence4import() {
		super();
	}

	@Override
	public String toString() {
		return Joiner.on(':').join(name, length, structureElementLists.toString());
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the structureElementLists
	 */
	public List<StructureElement> getStructureElementLists() {
		return structureElementLists;
	}

	/**
	 * @param structureElementLists the structureElementLists to set
	 */
	public void setStructureElementLists(List<StructureElement> structureElementLists) {
		this.structureElementLists = structureElementLists;
	}
	
	
	
}
