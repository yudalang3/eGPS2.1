package api.rpython;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvolTreeManipulatorTest {

    @Test
    void getNodeNames() {
    }

    public static void main(String[] args) throws Exception {
        EvolTreeManipulator evolTreeManipulator = new EvolTreeManipulator();

        String treePath = "C:\\Users\\yudal\\Documents\\project\\WntEvolution\\Archieve\\Main_Vertebrate_WntComp_formation_enrichment\\stage4\\referenced_tree\\SpeciesTree_with_human_atBottom.nwk - 快捷方式.lnk";
        String[] names = evolTreeManipulator.getNodeNames(treePath, null, true, true);

        System.out.println(names);
    }
}