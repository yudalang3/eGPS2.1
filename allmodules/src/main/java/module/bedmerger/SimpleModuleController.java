package module.bedmerger;

public class SimpleModuleController{

	private SimpleModuleMain main;

	public SimpleModuleController(SimpleModuleMain simpleModuleMain) {
		this.main = simpleModuleMain;
	}

	public SimpleModuleMain getMain() {
		return main;
	}

}
