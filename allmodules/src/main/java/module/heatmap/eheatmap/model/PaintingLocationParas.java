package module.heatmap.eheatmap.model;

import java.awt.Cursor;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import egps2.frame.gui.interacive.ComplexRectObj;
import egps2.frame.gui.interacive.RectAdjustMent;
import egps2.frame.gui.interacive.RectObj;
import module.heatmap.EheatmapMain;
import module.heatmap.eheatmap.HeatmapController;

/**
 * This is the object to store the painting parameters!
 * 
 * @author yudalang
 */

public class PaintingLocationParas {
	final public int blankLeft = 10;

	final HeatmapController hController;

	private double height;
	private double width;

	// parameters for painting tree
	private double rowTreeLeftTopX;
	private double rowTreeLeftTopY;
	private double rowTreeWeidth;
	private double rowTreeHeight;

	private double colTreeLeftTopX;
	private double colTreeLeftTopY;
	private double colTreeWeidth;
	private double colTreeHeight;

	// parameters for painting annotation
	private double rowAnnoLeftTopX;
	private double rowAnnoLeftTopY;
	private double rowAnnoWeidth;
	private double rowAnnoHeight;

	private double colAnnoLeftTopX;
	private double colAnnoLeftTopY;
	private double colAnnoWeidth;
	private double colAnnoHeight;

	// parameters for painting names
	private double rowNamesLeftTopX;
	private double rowNamesLeftTopY;
	private double rowNamesWeidth;
	private double rowNamesHeight;

	private double colNamesLeftTopX;
	private double colNamesLeftTopY;
	private double colNamesWeidth;
	private double colNamesHeight;

	// parameters for map legend
	private double mapLegendLeftTopX;
	private double mapLegendLeftTopY;
	private double mapLegendWeidth;
	private double mapLegendHeight;

	// parameters for map annotation legend
	private double annoLegendLeftTopX;
	private double annoLegendLeftTopY;
	private double annoLegendWeidth;
	private double annoLegendHeight;

	// parameters for painting heatmap!
	private double hmapLeftTopX;
	private double hmapLeftTopY;
	private double hmapWeidth;
	private double hmapHeight;

	private int numOfCols;
	private int numOfRows;
	private double cellWidth;
	private double cellHeight;

	private RectAdjustMent[] horizontalRects;
	private RectAdjustMent[] veriticalRects;
	private RectAdjustMent[] legendAdjustRects;

	public PaintingLocationParas(HeatmapController hController) {
		this.hController = hController;
	}

	public double getRowTreeLeftTopX() {
		return rowTreeLeftTopX;
	}

	public void setRowTreeLeftTopX(double rowTreeLeftTopX) {
		this.rowTreeLeftTopX = rowTreeLeftTopX;
	}

	public double getRowTreeLeftTopY() {
		return rowTreeLeftTopY;
	}

	public void setRowTreeLeftTopY(double rowTreeLeftTopY) {
		this.rowTreeLeftTopY = rowTreeLeftTopY;
	}

	public double getRowTreeWeidth() {
		return rowTreeWeidth;
	}

	public void setRowTreeWeidth(double rowTreeWeidth) {
		this.rowTreeWeidth = rowTreeWeidth;
	}

	public double getRowTreeHeight() {
		return rowTreeHeight;
	}

	public void setRowTreeHeight(double rowTreeHeight) {
		this.rowTreeHeight = rowTreeHeight;
	}

	public double getColTreeLeftTopX() {
		return colTreeLeftTopX;
	}

	public void setColTreeLeftTopX(double colTreeLeftTopX) {
		this.colTreeLeftTopX = colTreeLeftTopX;
	}

	public double getColTreeLeftTopY() {
		return colTreeLeftTopY;
	}

	public void setColTreeLeftTopY(double colTreeLeftTopY) {
		this.colTreeLeftTopY = colTreeLeftTopY;
	}

	public double getColTreeWeidth() {
		return colTreeWeidth;
	}

	public void setColTreeWeidth(double colTreeWeidth) {
		this.colTreeWeidth = colTreeWeidth;
	}

	public double getColTreeHeight() {
		return colTreeHeight;
	}

	public void setColTreeHeight(double colTreeHeight) {
		this.colTreeHeight = colTreeHeight;
	}

	public double getRowAnnoLeftTopX() {
		return rowAnnoLeftTopX;
	}

	public void setRowAnnoLeftTopX(double rowAnnoLeftTopX) {
		this.rowAnnoLeftTopX = rowAnnoLeftTopX;
	}

	public double getRowAnnoLeftTopY() {
		return rowAnnoLeftTopY;
	}

	public void setRowAnnoLeftTopY(double rowAnnoLeftTopY) {
		this.rowAnnoLeftTopY = rowAnnoLeftTopY;
	}

	public double getRowAnnoWeidth() {
		return rowAnnoWeidth;
	}

	public void setRowAnnoWeidth(double rowAnnoWeidth) {
		this.rowAnnoWeidth = rowAnnoWeidth;
	}

	public double getRowAnnoHeight() {
		return rowAnnoHeight;
	}

	public void setRowAnnoHeight(double rowAnnoHeight) {
		this.rowAnnoHeight = rowAnnoHeight;
	}

	public double getColAnnoLeftTopX() {
		return colAnnoLeftTopX;
	}

	public void setColAnnoLeftTopX(double colAnnoLeftTopX) {
		this.colAnnoLeftTopX = colAnnoLeftTopX;
	}

	public double getColAnnoLeftTopY() {
		return colAnnoLeftTopY;
	}

	public void setColAnnoLeftTopY(double colAnnoLeftTopY) {
		this.colAnnoLeftTopY = colAnnoLeftTopY;
	}

	public double getColAnnoWeidth() {
		return colAnnoWeidth;
	}

	public void setColAnnoWeidth(double colAnnoWeidth) {
		this.colAnnoWeidth = colAnnoWeidth;
	}

	public double getColAnnoHeight() {
		return colAnnoHeight;
	}

	public void setColAnnoHeight(double colAnnoHeight) {
		this.colAnnoHeight = colAnnoHeight;
	}

	public double getRowNamesLeftTopX() {
		return rowNamesLeftTopX;
	}

	public void setRowNamesLeftTopX(double rowNamesLeftTopX) {
		this.rowNamesLeftTopX = rowNamesLeftTopX;
	}

	public double getRowNamesLeftTopY() {
		return rowNamesLeftTopY;
	}

	public void setRowNamesLeftTopY(double rowNamesLeftTopY) {
		this.rowNamesLeftTopY = rowNamesLeftTopY;
	}

	public double getRowNamesWeidth() {
		return rowNamesWeidth;
	}

	public void setRowNamesWeidth(double rowNamesWeidth) {
		this.rowNamesWeidth = rowNamesWeidth;
	}

	public double getRowNamesHeight() {
		return rowNamesHeight;
	}

	public void setRowNamesHeight(double rowNamesHeight) {
		this.rowNamesHeight = rowNamesHeight;
	}

	public double getColNamesLeftTopX() {
		return colNamesLeftTopX;
	}

	public void setColNamesLeftTopX(double colNamesLeftTopX) {
		this.colNamesLeftTopX = colNamesLeftTopX;
	}

	public double getColNamesLeftTopY() {
		return colNamesLeftTopY;
	}

	public void setColNamesLeftTopY(double colNamesLeftTopY) {
		this.colNamesLeftTopY = colNamesLeftTopY;
	}

	public double getColNamesWeidth() {
		return colNamesWeidth;
	}

	public void setColNamesWeidth(double colNamesWeidth) {
		this.colNamesWeidth = colNamesWeidth;
	}

	public double getColNamesHeight() {
		return colNamesHeight;
	}

	public void setColNamesHeight(double colNamesHeight) {
		this.colNamesHeight = colNamesHeight;
	}

	public double getMapLegendLeftTopX() {
		return mapLegendLeftTopX;
	}

	public void setMapLegendLeftTopX(double mapLegendLeftTopX) {
		this.mapLegendLeftTopX = mapLegendLeftTopX;
	}

	public double getMapLegendLeftTopY() {
		return mapLegendLeftTopY;
	}

	public void setMapLegendLeftTopY(double mapLegendLeftTopY) {
		this.mapLegendLeftTopY = mapLegendLeftTopY;
	}

	public double getMapLegendWeidth() {
		return mapLegendWeidth;
	}

	public void setMapLegendWeidth(double mapLegendWeidth) {
		this.mapLegendWeidth = mapLegendWeidth;
	}

	public double getMapLegendHeight() {
		return mapLegendHeight;
	}

	public void setMapLegendHeight(double mapLegendHeight) {
		this.mapLegendHeight = mapLegendHeight;
	}

	public double getAnnoLegendLeftTopX() {
		return annoLegendLeftTopX;
	}

	public void setAnnoLegendLeftTopX(double annoLegendLeftTopX) {
		this.annoLegendLeftTopX = annoLegendLeftTopX;
	}

	public double getAnnoLegendLeftTopY() {
		return annoLegendLeftTopY;
	}

	public void setAnnoLegendLeftTopY(double annoLegendLeftTopY) {
		this.annoLegendLeftTopY = annoLegendLeftTopY;
	}

	public double getAnnoLegendWeidth() {
		return annoLegendWeidth;
	}

	public void setAnnoLegendWeidth(double annoLegendWeidth) {
		this.annoLegendWeidth = annoLegendWeidth;
	}

	public double getAnnoLegendHeight() {
		return annoLegendHeight;
	}

	public void setAnnoLegendHeight(double annoLegendHeight) {
		this.annoLegendHeight = annoLegendHeight;
	}

	public double getHmapLeftTopX() {
		return hmapLeftTopX;
	}

	public void setHmapLeftTopX(double hmapLeftTopX) {
		this.hmapLeftTopX = hmapLeftTopX;
	}

	public double getHmapLeftTopY() {
		return hmapLeftTopY;
	}

	public void setHmapLeftTopY(double hmapLeftTopY) {
		this.hmapLeftTopY = hmapLeftTopY;
	}

	public double getHmapWeidth() {
		return hmapWeidth;
	}

	public void setHmapWeidth(double hmapWeidth) {
		this.hmapWeidth = hmapWeidth;
	}

	public double getHmapHeight() {
		return hmapHeight;
	}

	public void setHmapHeight(double hmapHeight) {
		this.hmapHeight = hmapHeight;
	}

	public int getNumOfCols() {
		return numOfCols;
	}

	public void setNumOfCols(int numOfCols) {
		this.numOfCols = numOfCols;
	}

	public int getNumOfRows() {
		return numOfRows;
	}

	public void setNumOfRows(int numOfRows) {
		this.numOfRows = numOfRows;
	}

	public double getCellWidth() {
		return cellWidth;
	}

	public void setCellWidth(double cellWidth) {
		this.cellWidth = cellWidth;
	}

	public double getCellHeight() {
		return cellHeight;
	}

	public void setCellHeight(double cellHeight) {
		this.cellHeight = cellHeight;
	}

	public RectAdjustMent[] getHorizontalRects() {
		return horizontalRects;
	}

	public void setHorizontalRects(boolean[] horizontalBooleans) {

		List<RectAdjustMent> tt = new ArrayList<RectAdjustMent>();
		if (horizontalBooleans[0]) {
			// rowTree and rowAnno / map
			RectObj a = new RectObj(rowTreeLeftTopX + rowTreeWeidth - 2, hmapLeftTopY, 4, hmapHeight) {
				@Override
				public void adjustPaintings(double x0, double y0) {
					final int minColTreeWidth = 40;
					final int minColAnnoWidht = 20;
					final int minHeatmapWidth = 150;
					if (horizontalBooleans[1]) {
						// if ((hmapLeftTopX - colAnnoLeftTopY - devi) > 10) {
						if (x0 < hmapLeftTopX - minColAnnoWidht && x0 > blankLeft + minColTreeWidth) {

							rowTreeWeidth = x0 - rowTreeLeftTopX;
							rowAnnoLeftTopX = x0;
							rowAnnoWeidth = hmapLeftTopX - x0;

							this.x = x0 - 2;
							hController.weakestRefreshHeatmapForInteractiveDrag(false, true);
						}
					} else {

						if (x0 < hmapLeftTopX + hmapWeidth - minHeatmapWidth && x0 > blankLeft + minColTreeWidth) {
							rowTreeWeidth = x0 - rowTreeLeftTopX;

							hmapWeidth = hmapLeftTopX + hmapWeidth - x0;
							hmapLeftTopX = x0;

							colTreeLeftTopX = x0;
							colAnnoLeftTopX = x0;
							colNamesLeftTopX = x0;

							ParameterModel paraModel = hController.getParaModel();
							cellWidth = (hmapWeidth - paraModel.getGapSize() * paraModel.gethGapLocations().length)
									/ numOfCols;

							this.x = x0 - 2;
							hController.weakestRefreshHeatmapForInteractiveDrag(true, true);
						}
					}
					reSetParameterInLayoutPanel();
				}
				
				@Override
				public int getCursorType() {
					return Cursor.E_RESIZE_CURSOR;
				}
			};
			tt.add(a);
		}
		if (horizontalBooleans[1]) {
			// rowAnno and map
			RectObj a = new RectObj(hmapLeftTopX - 2, hmapLeftTopY, 4, hmapHeight) {
				@Override
				public void adjustPaintings(double x0, double y0) {
					final int minHeatmapWidth = 150;
					final int minRowAnnoWidth = 20;
					if (x0 < hmapLeftTopX + hmapWeidth - minHeatmapWidth && x0 > rowAnnoLeftTopX + minRowAnnoWidth) {
						rowAnnoWeidth = x0 - rowAnnoLeftTopX;

						hmapWeidth = hmapLeftTopX + hmapWeidth - x0;
						hmapLeftTopX = x0;

						colTreeLeftTopX = x0;
						colAnnoLeftTopX = x0;
						colNamesLeftTopX = x0;

						ParameterModel paraModel = hController.getParaModel();
						cellWidth = (hmapWeidth - paraModel.getGapSize() * paraModel.gethGapLocations().length)
								/ numOfCols;

						this.x = x0 - 2;
						hController.weakestRefreshHeatmapForInteractiveDrag(true, true);
					}
					reSetParameterInLayoutPanel();
				}
				@Override
				public int getCursorType() {
					return Cursor.E_RESIZE_CURSOR;
				}
			};
			tt.add(a);
		}
		if (horizontalBooleans[3]) {
			// map and rowNames
			RectObj a = new RectObj(rowNamesLeftTopX - 2, hmapLeftTopY, 4, hmapHeight) {
				@Override
				public void adjustPaintings(double x0, double y0) {
					final int minHeatmapWidth = 250;
					final int minRowNameWidth = 30;
					if (x0 < rowNamesLeftTopX + rowNamesWeidth - minRowNameWidth
							&& x0 > hmapLeftTopX + minHeatmapWidth) {
						rowNamesWeidth = rowNamesLeftTopX + rowNamesWeidth - x0;
						rowNamesLeftTopX = x0;

						hmapWeidth = x0 - hmapLeftTopX;

						colTreeWeidth = x0 - colTreeLeftTopX;
						colAnnoWeidth = x0 - colAnnoLeftTopX;
						colNamesWeidth = x0 - colNamesLeftTopX;

						ParameterModel paraModel = hController.getParaModel();
						cellWidth = (hmapWeidth - paraModel.getGapSize() * paraModel.gethGapLocations().length)
								/ numOfCols;

						this.x = x0 - 2;
						hController.weakestRefreshHeatmapForInteractiveDrag(true, false);
					}
					reSetParameterInLayoutPanel();
				}
				
				@Override
				public int getCursorType() {
					return Cursor.E_RESIZE_CURSOR;
				}
			};
			tt.add(a);
		}
		if (horizontalBooleans[4]) {
			/**
			 * 注意：这个Map legend 需要添加拖拽的功能！
			 */
			// map / rowNames and map legend
			ParameterModel paraModel = hController.getParaModel();
			int width = paraModel.getMapLegnedWidth();
			int height = paraModel.getMapLegendHeight();
			ComplexRectObj a = new ComplexRectObj(mapLegendLeftTopX, mapLegendLeftTopY, height, height) {
				private int cursorType = Cursor.MOVE_CURSOR;
				
				@Override
				public boolean contains(double x, double y) {
					
					// Judge left
					double x0 = this.x- 2;
					double y0 = this.y;
					boolean leftRet =  (x >= x0 && y >= y0 && x < x0 + 4 && y < y0 + h);
					if (leftRet) {
						allBooleansBeFalse();
						this.ifLeft = true;
						cursorType = Cursor.E_RESIZE_CURSOR;
						return true;
					}
					
					x0 = this.x;
					boolean centerRet =  (x >= x0 && y >= y0 && x < x0 + width+5 && y < y0 + h);
					if (centerRet) {
						allBooleansBeFalse();
						this.ifCenter = true;
						cursorType = Cursor.MOVE_CURSOR;
						return true;
					}
					
					return false;
				}
				
				@Override
				public int getCursorType() {
					return cursorType;
				}
				@Override
				public void adjustLeft(double x0, double y0) {
					final int minRowNameWidth = 30;
					final int minMapLegendWidth = 30;
					final int minHeatmapWidth = 250;

					if (horizontalBooleans[3]) {
						if (x0 < mapLegendLeftTopX + mapLegendWeidth - minMapLegendWidth
								&& x0 > rowNamesLeftTopX + minRowNameWidth) {
							rowNamesWeidth = x0 - rowNamesLeftTopX;

							mapLegendWeidth = mapLegendLeftTopX + mapLegendWeidth - x0;
							mapLegendLeftTopX = x0;
							this.x = x0 - 2;
							hController.weakestRefreshHeatmap();
						}
					} else {
						if (x0 < mapLegendLeftTopX + mapLegendWeidth - minMapLegendWidth
								&& x0 > hmapLeftTopX + minHeatmapWidth) {
							hmapWeidth = x0 - hmapLeftTopX;

							mapLegendWeidth = mapLegendLeftTopX + mapLegendWeidth - x0;
							mapLegendLeftTopX = x0;
							colTreeWeidth = x0 - colTreeLeftTopX;
							colAnnoWeidth = x0 - colAnnoLeftTopX;
							colNamesWeidth = x0 - colNamesLeftTopX;

							ParameterModel paraModel = hController.getParaModel();
							cellWidth = (hmapWeidth - paraModel.getGapSize() * paraModel.gethGapLocations().length)
									/ numOfCols;

							this.x = x0 - 2;
							hController.weakestRefreshHeatmapForInteractiveDrag(true, false);
						}
					}
					reSetParameterInLayoutPanel();
					
				}
				
				@Override
				public void adjustCenter(double x0, double y0) {
					final int lengthOfPoint2leftTop = 50;
					mapLegendLeftTopX = x0 - 0.5 * width;
					mapLegendLeftTopY = y0 - lengthOfPoint2leftTop;
					hController.weakestRefreshHeatmap();
					
					this.x = mapLegendLeftTopX;
					this.y = mapLegendLeftTopY;
					
				}
			};
			tt.add(a);
		}
		if (horizontalBooleans[5]) {
			/**
			 * 注意：这个annotation legend 需要添加拖拽的功能！
			 */
			// map / rowNames / map legend and annotation legend
			ComplexRectObj a = new ComplexRectObj(annoLegendLeftTopX, annoLegendLeftTopY, annoLegendWeidth, annoLegendHeight) {
				private int cursorType = Cursor.MOVE_CURSOR;
				
				@Override
				public boolean contains(double x, double y) {
					// Judge left
					double x0 = this.x- 2;
					double y0 = this.y;
					boolean leftRet =  (x >= x0 && y >= y0 && x < x0 + 4 && y < y0 + h);
					if (leftRet) {
						allBooleansBeFalse();
						this.ifLeft = true;
						cursorType = Cursor.E_RESIZE_CURSOR;
						return true;
					}
					
					x0 = this.x;
					boolean centerRet =  (x >= x0 && y >= y0 && x < x0 + w && y < y0 + h);
					if (centerRet) {
						allBooleansBeFalse();
						this.ifCenter = true;
						cursorType = Cursor.MOVE_CURSOR;
						return true;
					}
					
					return false;
				}
				
				@Override
				public void adjustLeft(double x0, double y0) {
					boolean ifMap = horizontalBooleans[4];
					boolean ifRowName = horizontalBooleans[3];

					final int minAnnoLegendWidth = 80;
					final int minMapLegendWidth = 30;
					final int minHeatmapWidth = 250;
					final int minRowNameWidth = 30;
					if (ifMap) {
						if (x0 < width - blankLeft - minAnnoLegendWidth && x0 > mapLegendLeftTopX + minMapLegendWidth) {
							mapLegendWeidth = x0 - mapLegendLeftTopX;

							annoLegendWeidth = annoLegendLeftTopX + annoLegendWeidth - x0;
							annoLegendLeftTopX = x0;

							this.x = x0 - 2;
							hController.weakestRefreshHeatmap();
						}
					} else {
						if (ifRowName) {
							// not map but row name
							if (x0 < annoLegendLeftTopX + annoLegendWeidth - minAnnoLegendWidth
									&& x0 > rowNamesLeftTopX + minRowNameWidth) {
								rowNamesWeidth = x0 - rowNamesLeftTopX;

								annoLegendWeidth = annoLegendLeftTopX + annoLegendWeidth - x0;
								annoLegendLeftTopX = x0;

								this.x = x0 - 2;
								hController.weakestRefreshHeatmap();
							}
						} else {
							// not all
							if (x0 < annoLegendLeftTopX + annoLegendWeidth - minAnnoLegendWidth
									&& x0 > hmapLeftTopX + minHeatmapWidth) {
								hmapWeidth = x0 - hmapLeftTopX;

								annoLegendWeidth = annoLegendLeftTopX + annoLegendWeidth - x0;
								annoLegendLeftTopX = x0;
								colTreeWeidth = x0 - colTreeLeftTopX;
								colAnnoWeidth = x0 - colAnnoLeftTopX;
								colNamesWeidth = x0 - colNamesLeftTopX;

								ParameterModel paraModel = hController.getParaModel();
								cellWidth = (hmapWeidth - paraModel.getGapSize() * paraModel.gethGapLocations().length)
										/ numOfCols;

								this.x = x0 - 2;
								hController.weakestRefreshHeatmapForInteractiveDrag(true, false);
							}
						}
					}
					reSetParameterInLayoutPanel();
				
				}
				
				@Override
				public void adjustCenter(double x0, double y0) {
					final int lengthOfPoint2leftTop = 50;
						
						annoLegendLeftTopX = x0 - 0.5 * annoLegendWeidth;
						annoLegendLeftTopY = y0 - lengthOfPoint2leftTop;
						hController.weakestRefreshHeatmap();
						
						this.x = annoLegendLeftTopX;
						this.y = annoLegendLeftTopY;
				}

				@Override
				public int getCursorType() {
					return cursorType;
				}
			};
			tt.add(a);
		}

		this.horizontalRects = tt.toArray(new RectAdjustMent[0]);
	}

	public RectAdjustMent[] getVeriticalRects() {
		return veriticalRects;
	}

	public void setVeriticalRects(boolean[] veriticalBooleans) {
		List<RectAdjustMent> tt = new ArrayList<RectAdjustMent>();
		if (veriticalBooleans[0]) {
			// colTree and colAnno / map
			double yy = colTreeLeftTopY + colTreeHeight;
			RectObj a = new RectObj(hmapLeftTopX, yy - 2, hmapWeidth, 4) {
				@Override
				public void adjustPaintings(double x0, double y0) {

					final int minColTreeHeight = 40;
					final int minColAnnoHeight = 20;
					final int minHeatmapHeight = 150;
					if (veriticalBooleans[1]) {
						// if ((hmapLeftTopX - colAnnoLeftTopY - devi) > 10) {
						if (y0 < hmapLeftTopY - minColAnnoHeight && y0 > blankLeft + minColTreeHeight) {

							colTreeHeight = y0 - colTreeLeftTopY;
							colAnnoHeight = hmapLeftTopY - y0;
							colAnnoLeftTopY = y0;

							this.y = y0 - 2;
							hController.weakestRefreshHeatmapForInteractiveDrag(true, false);
						}
					} else {

						if (y0 < hmapLeftTopY + hmapHeight - minHeatmapHeight && y0 > blankLeft + minColTreeHeight) {
							colTreeHeight = y0 - colTreeLeftTopY;
							hmapHeight = hmapLeftTopY + hmapHeight - y0;

							hmapLeftTopY = y0;
							rowTreeLeftTopY = y0;
							rowAnnoLeftTopY = y0;
							rowNamesLeftTopY = y0;

							ParameterModel paraModel = hController.getParaModel();
							cellHeight = (hmapHeight - paraModel.getGapSize() * paraModel.getvGapLocations().length)
									/ numOfRows;

							this.y = y0 - 2;
							hController.weakestRefreshHeatmapForInteractiveDrag(true, true);
						}
					}
					reSetParameterInLayoutPanel();
				}
				@Override
				public int getCursorType() {
					return Cursor.N_RESIZE_CURSOR;
				}
			};
			tt.add(a);
		}
		if (veriticalBooleans[1]) {
			// colAnno and map
			RectObj a = new RectObj(hmapLeftTopX, hmapLeftTopY - 2, hmapWeidth, 4) {
				@Override
				public void adjustPaintings(double x0, double y0) {
					final int minColAnnoHeight = 20;
					final int minHeatmapHeight = 150;

					if (y0 < hmapLeftTopY + hmapHeight - minHeatmapHeight && y0 > colAnnoLeftTopY + minColAnnoHeight) {

						colAnnoHeight = y0 - colAnnoLeftTopY;
						hmapHeight = hmapLeftTopY + hmapHeight - y0;

						hmapLeftTopY = y0;
						rowTreeLeftTopY = y0;
						rowAnnoLeftTopY = y0;
						rowNamesLeftTopY = y0;

						ParameterModel paraModel = hController.getParaModel();
						cellHeight = (hmapHeight - paraModel.getGapSize() * paraModel.getvGapLocations().length)
								/ numOfRows;

						this.y = y0 - 2;
						hController.weakestRefreshHeatmapForInteractiveDrag(true, true);
					}
					reSetParameterInLayoutPanel();
				}
				
				@Override
				public int getCursorType() {
					return Cursor.N_RESIZE_CURSOR;
				}
			};
			tt.add(a);
		}
		if (veriticalBooleans[3]) {
			// map and colName
			RectObj a = new RectObj(hmapLeftTopX, colNamesLeftTopY - 2, hmapWeidth, 4) {
				@Override
				public void adjustPaintings(double x0, double y0) {
					final int minColNameHeight = 50;
					final int minHeatmapHeight = 150;

					if (y0 < height - blankLeft - minColNameHeight
							&& y0 > hmapLeftTopY + hmapHeight - minHeatmapHeight) {
						colNamesHeight = height - blankLeft - y0;
						colNamesLeftTopY = y0;

						hmapHeight = y0 - hmapLeftTopY;

						ParameterModel paraModel = hController.getParaModel();
						cellHeight = (hmapHeight - paraModel.getGapSize() * paraModel.getvGapLocations().length)
								/ numOfRows;

						this.y = y0 - 2;
						hController.weakestRefreshHeatmapForInteractiveDrag(true, true);
					}

					reSetParameterInLayoutPanel();
				}
				@Override
				public int getCursorType() {
					return Cursor.N_RESIZE_CURSOR;
				}
			};
			tt.add(a);
		}
		this.veriticalRects = tt.toArray(new RectAdjustMent[0]);
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	final void reSetParameterInLayoutPanel() {
		EheatmapMain main = (EheatmapMain) hController.getViewPanel();
		main.reInitializeLeftLayoutPanelLayout();
	}


	public RectAdjustMent[] getLegendAdjustRects() {
		return legendAdjustRects;
	}

	Point circularCenter ;
	public Point getCenterPoint() {
		if (circularCenter == null) {
			int centerX0 = (int) (hmapLeftTopX + 0.5 * hmapWeidth);
			int centerY0 = (int) (hmapLeftTopY + 0.5 * hmapHeight);
			
			circularCenter = new Point(centerX0,centerY0);
		}
		
		return circularCenter;
	}

	public double calculateRingThickness(int number, double innerRadius) {
		double min = 0.5 * Math.min(hmapWeidth, hmapHeight);
		return (min - innerRadius) / number;
	}

}
