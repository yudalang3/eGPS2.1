package module.evolview.model.enums;

public enum NintyDegreeRotation {

    ZERO, ONCE, TWICE, THRICE;

    public NintyDegreeRotation getNextRotation() {

        if (this == ZERO) {
            return ONCE;
        } else if (this == ONCE) {
            return TWICE;
        } else if (this == TWICE) {
            return THRICE;
        } else {
            return THRICE;
        }

    }
}
