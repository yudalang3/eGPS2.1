package module.evolview.gfamily.work.gui.browser;

import java.awt.Color;

/**
 * 引物转换工具类
 *
 * @Author: mhl
 * @Date Created on: 2020-07-02 18:13
 */
public class PrimerUtil {
    private static Color[] colors = new Color[40];

    public static void createColors() {
    	colors = generateBeautifulColors(40);
    }
    
    public static Color[] generateBeautifulColors(int numColors) {
        Color[] colors = new Color[numColors];

        float saturation = 0.9f;
        float brightness = 0.9f;
        float hueIncrement = (float) (Math.sqrt(5) - 1) / 2; // 黄金分割

        for (int i = 0; i < numColors; i++) {
            float hue = (i * hueIncrement) % 1.0f;
            colors[i] = Color.getHSBColor(hue, saturation, brightness);
        }

        return colors;
    }
    

    public static Color getColor(int index) {
        if (index >= colors.length) {
            return Color.BLACK;
        }
        return colors[index];

    }


    public static String forwardPrimersToreversePrimers(String base) {
        String result = "";
        switch (base) {
            case "A":
                result = "T";
                break;
            case "T":
                result = "A";
                break;
            case "C":
                result = "G";
                break;
            case "G":
                result = "C";
                break;
            default:
                result = "-";
                break;
        }
        return result;

    }
}
