package module.evolknow;

import java.awt.Color;

public class PeriodsTimeScale {
	private final TimeScaleEntity[] entities;

	public PeriodsTimeScale() {
		this.entities = new TimeScaleEntity[] {
				new TimeScaleEntity("Cambrian", "寒武纪", 541, 485,      new Color(127, 160, 86)),
				new TimeScaleEntity("Ordovician", "奥陶纪", 485, 444,    new Color(0,146,112)),
				new TimeScaleEntity("Silurian", "志留纪", 444, 419,      new Color(179,225,182)),
				new TimeScaleEntity("Devonian", "泥盆纪", 419, 359,      new Color(203,140,55)),
				new TimeScaleEntity("Carboniferous", "石炭纪", 359, 299, new Color(103,165,153)),
				new TimeScaleEntity("Permian", "二叠纪", 299, 252,       new Color(240,64,40)),
				new TimeScaleEntity("Triassic", "三叠纪", 252, 201,      new Color(129,43,146)),
				new TimeScaleEntity("Jurassic", "侏罗纪", 201, 145,      new Color(52,178,201)),
				new TimeScaleEntity("Cretaceous", "白垩纪", 145, 66,     new Color(127,198,78)),
				new TimeScaleEntity("Paleogene", "古近纪", 66, 23.03f,   new Color(253,154,82)),
				new TimeScaleEntity("Neogene", "新近纪", 23.03f, 2.58f,  new Color(255,230,25)),
				new TimeScaleEntity("Quaternary", "第四纪", 2.58f, 0,    new Color(249,249,127)) };
	}

	public TimeScaleEntity[] getEntities() {
		return entities;
	}
}
