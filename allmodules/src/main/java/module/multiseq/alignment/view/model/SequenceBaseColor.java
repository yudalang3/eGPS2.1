package module.multiseq.alignment.view.model;

import java.awt.Color;
import java.util.function.Supplier;

public enum SequenceBaseColor {

	A("A", () -> new Color(254, 109, 109)), C("C", () -> new Color(116, 206, 112)),
	D("D", () -> new Color(252, 180, 211)), E("E", () -> new Color(209, 251, 208)),
	F("F", () -> new Color(180, 243, 237)), G("G", () -> new Color(242, 190, 60)),
	H("H", () -> new Color(220, 171, 171)), I("I", () -> new Color(242, 227, 208)),
	K("K", () -> new Color(205, 194, 155)), L("L", () -> new Color(237, 196, 167)),
	M("M", () -> new Color(255, 205, 186)), N("N", () -> new Color(194, 213, 248)),
	P("P", () -> new Color(241, 163, 179)), Q("Q", () -> new Color(155, 255, 255)),
	R("R", () -> new Color(233, 150, 35)), S("S", () -> new Color(254, 252, 180)),
	T("T", () -> new Color(118, 156, 203)), V("V", () -> new Color(248, 206, 248)),
	W("W", () -> new Color(155, 210, 210)), Y("Y", () -> new Color(211, 231, 241)),
	U("U", () -> new Color(60, 136, 238)), STORP("*", () -> new Color(175, 143, 188)),
	OTHES("OTHES", () -> new Color(255, 255, 255));
	private Supplier<Color> baseColor;

	private String message;

	private SequenceBaseColor(String message, Supplier<Color> baseColor) {
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
		case "A":
			return A.getColor();
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
		case "K":
			return K.getColor();
		case "L":
			return L.getColor();
		case "M":
			return M.getColor();
		case "N":
			return N.getColor();
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
		case "Y":
			return Y.getColor();
		case "*":
			return STORP.getColor();
		default:
			return OTHES.getColor();
		}
	}
}
