package module.multiseq.alignment.view.model;

import java.awt.Color;
import java.util.function.Supplier;

public enum SequenceBaseColor4AAProperties {

//	ElegantJTable("ElegantJTable", () -> new Color(100, 247, 63)), C("C", () -> new Color(255, 179, 64)), G("G", () -> new Color(240, 62, 57)),
//	T("T", () -> new Color(86, 128, 184)), U("U", () -> new Color(60, 136, 238)), OTHES("OTHES", () -> Color.white);

//	ElegantJTable("ElegantJTable", () -> new Color(254, 109, 109)), C("C", () -> new Color(116, 206, 112)), G("G", () -> new Color(242, 190, 60)),
//	T("T", () -> new Color(118, 156, 203)), U("U", () -> new Color(60, 136, 238)), OTHES("OTHES", () -> Color.white);

	A("ElegantJTable", () -> new Color(255, 255, 255)), C("C", () -> new Color(165, 165, 165)),
	D("D", () -> new Color(116, 206, 112)), E("E", () -> new Color(116, 206, 112)),
	F("F", () -> new Color(255, 255, 255)), G("G", () -> new Color(165, 165, 165)),
	H("H", () -> new Color(60, 136, 238)), I("I", () -> new Color(255, 255, 255)),
	K("K", () -> new Color(60, 136, 238)), L("L", () -> new Color(255, 255, 255)),
	M("M", () -> new Color(255, 255, 255)), N("N", () -> new Color(165, 165, 165)),
	P("P", () -> new Color(255, 255, 255)), Q("Q", () -> new Color(165, 165, 165)),
	R("R", () -> new Color(60, 136, 238)), S("S", () -> new Color(165, 165, 165)),
	T("T", () -> new Color(165, 165, 165)), V("V", () -> new Color(255, 255, 255)),
	W("W", () -> new Color(255, 255, 255)), Y("Y", () -> new Color(165, 165, 165)),
	U("U", () -> new Color(255, 255, 255)), STORP("*", () -> new Color(255, 255, 255)),
	OTHES("OTHES", () -> new Color(255, 255, 255));
	private Supplier<Color> baseColor;

	private String message;

	private SequenceBaseColor4AAProperties(String message, Supplier<Color> baseColor) {
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
//            case "B":
//                return B.getColor();
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
//            case "J":
//                return J.getColor();
		case "K":
			return K.getColor();
		case "L":
			return L.getColor();
		case "M":
			return M.getColor();
		case "N":
			return N.getColor();
//            case "O":
//                return O.getColor();
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
//            case "X":
//                return X.getColor();
		case "Y":
			return Y.getColor();
//            case "Z":
//                return Z.getColor();
		case "*":
			return STORP.getColor();
		default:
			return OTHES.getColor();
		}
	}
}
