package module.evolview.gfamily.work.gui.browser;

/**
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 *
 * @author mhl
 * @Date Created on:2020-04-10 10:23
 */
public class DrawIntermediatedProperties {

    private double oldWidth;

    private double oldHeight;

    private int oldDrawStart;

    private int oldDrawEnd;

    private GeneDrawingLengthCursor drawProperties;

    public DrawIntermediatedProperties(GeneDrawingLengthCursor drawProperties) {

        this.drawProperties = drawProperties;

    }

    public boolean isReCalculator(double width, double height) {

        int drawStart = drawProperties.getDrawStart();

        int drawEnd = drawProperties.getDrawEnd();

        if (oldWidth != width || oldHeight != height || oldDrawStart != drawStart || oldDrawEnd != drawEnd
                || drawProperties.isReCalculator()) {

            oldWidth = width;

            oldHeight = height;

            oldDrawStart = drawProperties.getDrawStart();

            oldDrawEnd = drawProperties.getDrawEnd();

            drawProperties.setReCalculator(false);

            return true;
        }

        return false;
    }

}
