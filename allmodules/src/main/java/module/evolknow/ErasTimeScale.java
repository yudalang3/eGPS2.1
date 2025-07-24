package module.evolknow;

import java.awt.Color;

public class ErasTimeScale {
	private final TimeScaleEntity[] entities;

	
	public ErasTimeScale() {
		this.entities = new TimeScaleEntity[]{
				new TimeScaleEntity("Paleozoic", "古生代", 541, 252, new Color(153,192,141)),
				new TimeScaleEntity("Mesozoic", "中生代", 252, 66, new Color(103,197,202)),
				new TimeScaleEntity("Cenozoic", "新生代", 66, 0, new Color(242,249,29))
		};
	}
	public TimeScaleEntity[] getEntities() {
		return entities;
	}
}
