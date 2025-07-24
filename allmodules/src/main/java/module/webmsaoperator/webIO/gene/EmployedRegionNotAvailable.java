package module.webmsaoperator.webIO.gene;

public class EmployedRegionNotAvailable extends Exception {
	
	protected String title;
	
	protected String information;

	public EmployedRegionNotAvailable(String title, String information) {
		this.title = title;
		this.information = information;
	}
	
	public String getInformation() {
		return information;
	}
	
	public String getTitle() {
		return title;
	}

}
