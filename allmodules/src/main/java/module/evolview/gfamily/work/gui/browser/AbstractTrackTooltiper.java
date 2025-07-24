package module.evolview.gfamily.work.gui.browser;

public abstract class AbstractTrackTooltiper {
    String templateHeader = "<html><body font size='5'>";
    String templateFooter = "</body></html>";
    protected static StringBuilder sbuilder = new StringBuilder();
    /**
     * The tool tip
     */
    protected String toolTip;

    /**
     * The tooltip text is changeable when you move the cursor.
     */
    public synchronized String getToolTip() {
    	sbuilder.setLength(0);
    	sbuilder.append(templateHeader).append(toolTip)
    	.append(templateFooter);
        return sbuilder.toString();
    }

    /**
     * Tests if the specified coordinates are inside the boundary of the JShape.
     *
     * @param x, y - the specified coordinates
     * @returns true if the specified coordinates are inside the Shape boundary;
     * false otherwise.
     */
    public abstract boolean contains(double x, double y);
}
