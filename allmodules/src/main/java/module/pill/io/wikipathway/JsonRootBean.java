package module.pill.io.wikipathway;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

public class JsonRootBean {
    private Map<String, Entity> entitiesById;
    private Pathway pathway;

    @JSONField(name = "entitiesById")
    public Map<String, Entity> getEntitiesById() {
        return entitiesById;
    }

    public void setEntitiesById(Map<String, Entity> entitiesById) {
        this.entitiesById = entitiesById;
    }

    @JSONField(name = "pathway")
    public Pathway getPathway() {
        return pathway;
    }

    public void setPathway(Pathway pathway) {
        this.pathway = pathway;
    }
}
