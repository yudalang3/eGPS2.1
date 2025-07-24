package primary.struct.display.data;

import java.awt.*;

public class DomainV2 {
    private String name;
    private int start;
    private int end;
    private Color color;

    public DomainV2(String domain1, int i, int i1, Color red) {
        this.name = domain1;
        this.start = i;
        this.end = i1;
        this.color = red;
    }
    public DomainV2() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
