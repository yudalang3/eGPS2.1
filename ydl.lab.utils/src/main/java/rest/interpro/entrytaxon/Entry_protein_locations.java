package rest.interpro.entrytaxon;

import java.util.List;

public class Entry_protein_locations {

	String model;
	
	float score;
	
	List<Fragments> fragments;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public List<Fragments> getFragments() {
		return fragments;
	}

	public void setFragments(List<Fragments> fragments) {
		this.fragments = fragments;
	}
	
	
	
	
}
