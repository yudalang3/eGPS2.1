package module.evolknow;

import java.awt.Color;

public class EonsTimeScale {
	
	private final TimeScaleEntity[] entities;

	public EonsTimeScale() {
		this.entities = new TimeScaleEntity[]{
				new TimeScaleEntity("Phanerozoic", "显生宙", 541, 0, new Color(154,217,221))
		};
	}
	
	public TimeScaleEntity[] getEntities() {
		return entities;
	}
}
