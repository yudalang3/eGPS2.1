package module.chorddiagram.work;

import java.util.List;

public class ChordDiagram4WntBean {

	private String name = "DKK-LRP6-KREMEN triple complex";
    private List<BioEntity> entities;
    private List<BioInteraction> interactionList;

    public ChordDiagram4WntBean(List<BioEntity> entities, List<BioInteraction> interactionList) {
        this.entities = entities;
        this.interactionList = interactionList;
    }

    public List<BioEntity> getEntities() {
        return entities;
    }
    public List<BioInteraction> getInteractionList() {
        return interactionList;
    }

    public void setEntities(List<BioEntity> entities) {
            this.entities = entities;
    }

    public void setInteractionList(List<BioInteraction> interactionList) {
        this.interactionList = interactionList;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
