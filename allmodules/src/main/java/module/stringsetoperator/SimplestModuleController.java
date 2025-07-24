package module.stringsetoperator;

import egps2.Authors;

public class SimplestModuleController {

	public String[] getTeamAndAuthors() {
		String[] info = new String[3];
		info[0] = Authors.LAB;
		info[1] = Authors.YUDALANG +  "," + Authors.LIHAIPENG;
		info[2] = Authors.WEB_SITE;
		return info;
	}

}
