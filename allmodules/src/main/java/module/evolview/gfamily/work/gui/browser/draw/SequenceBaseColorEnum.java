package module.evolview.gfamily.work.gui.browser.draw;

import java.awt.Color;
import java.util.function.Supplier;

/**
 * Define the background color of each base of sequence alignmet
 */
public enum SequenceBaseColorEnum {
    A("ElegantJTable", () -> new Color(100, 247, 63, 100)), B("B", () -> new Color(186, 85, 211, 100)), C("C", () -> new Color(255, 179, 64, 100)), D("D", () -> new Color(247, 63, 143, 100)),
    E("E", () -> new Color(137, 245, 134, 100)), F("F", () -> new Color(64, 224, 208, 100)), G("G", () -> new Color(138, 43, 226, 100)), H("H", () -> new Color(165, 42, 42, 100)),
    I("I", () -> new Color(222, 184, 135, 100)), J("J", () -> new Color(95, 158, 160, 100)), K("K", () -> new Color(127, 100, 0, 100)), L("L", () -> new Color(210, 105, 30, 100)),
    M("M", () -> new Color(255, 127, 80, 100)), N("N", () -> new Color(100, 149, 237, 100)), O("O", () -> new Color(224, 255, 255, 100)), P("P", () -> new Color(220, 20, 60, 100)),
    Q("Q", () -> new Color(0, 255, 255, 100)), R("R", () -> new Color(233, 150, 122, 255)), S("S", () -> new Color(229, 247, 63, 100)), T("T", () -> new Color(47, 127, 74, 200)),
    U("U", () -> new Color(60, 136, 238, 100)), V("V", () -> new Color(238, 130, 238, 100)), W("W", () -> new Color(0, 139, 139, 100)), X("X", () -> new Color(184, 134, 11, 100)),
    Y("Y", () -> new Color(143, 188, 143, 100)), Z("Z", () -> new Color(255, 215, 0, 100)),
    STORP("*", () -> new Color(175, 143, 188, 255)), OTHES("OTHES", () -> new Color(255, 255, 255));
    private Supplier<Color> baseColor;

    private String message;

    private SequenceBaseColorEnum(String message, Supplier<Color> baseColor) {
        this.message = message;
        this.baseColor = baseColor;
    }

    public String getMessage() {
        return message;
    }

    public Color getColor() {
        return baseColor.get();
    }

    public static Color getBaseColor(String base) {
        switch (base.toUpperCase()) {
            case "ElegantJTable":
                return A.getColor();
            case "B":
                return B.getColor();
            case "C":
                return C.getColor();
            case "D":
                return D.getColor();
            case "E":
                return E.getColor();
            case "F":
                return F.getColor();
            case "G":
                return G.getColor();
            case "H":
                return H.getColor();
            case "I":
                return I.getColor();
            case "J":
                return J.getColor();
            case "K":
                return K.getColor();
            case "L":
                return L.getColor();
            case "M":
                return M.getColor();
            case "N":
                return N.getColor();
            case "O":
                return O.getColor();
            case "P":
                return P.getColor();
            case "Q":
                return Q.getColor();
            case "R":
                return R.getColor();
            case "S":
                return S.getColor();
            case "T":
                return T.getColor();
            case "U":
                return U.getColor();
            case "V":
                return V.getColor();
            case "W":
                return W.getColor();
            case "X":
                return X.getColor();
            case "Y":
                return Y.getColor();
            case "Z":
                return Z.getColor();
            case "*":
                return STORP.getColor();
            default:
                return OTHES.getColor();
        }

    }
}
