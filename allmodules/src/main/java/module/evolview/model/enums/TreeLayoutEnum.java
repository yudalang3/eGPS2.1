package module.evolview.model.enums;

public enum TreeLayoutEnum {

	SLOPE_LAYOUT("slope"), 
	RECTANGULAR_LAYOUT("rectangular"), 
	CIRCULAR_LAYOUT("circular"),
	RADICAL_LAYOUT("radical"),
	SPRIAL_LAYOUT("sprial");

	private String name;

	TreeLayoutEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
