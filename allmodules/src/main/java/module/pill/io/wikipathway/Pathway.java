package module.pill.io.wikipathway;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class Pathway {
    private String id;
    private double height;
    private double width;
    private List<String> name;
	private List<Xref> xref;
    private List<BiopaxRef> biopaxRefs;
	private String organism;
	private String author;
	private List<Comment> comments;
	private List<String> contains;
	private String dataSourceVersion;
	private String displayName;
	private String drawAs;
	private String fill;
	private int fontSize;
	private String fontWeight;
	private String gpmlElementName;
	private double x;
	private double y;
	private int zIndex;
	private String wpType;
	private String xrefDataSource;
	private String xrefIdentifier;
	private String textAlign;
	private String verticalAlign; // 添加 verticalAlign 属性

    @JSONField(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JSONField(name = "height")
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @JSONField(name = "width")
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @JSONField(name = "name")
    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    @JSONField(name = "xref")
	public List<Xref> getXref() {
        return xref;
    }

	public void setXref(List<Xref> xref) {
        this.xref = xref;
    }

    @JSONField(name = "biopaxRefs")
    public List<BiopaxRef> getBiopaxRefs() {
        return biopaxRefs;
    }

    public void setBiopaxRefs(List<BiopaxRef> biopaxRefs) {
        this.biopaxRefs = biopaxRefs;
    }

	@JSONField(name = "organism")
	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	@JSONField(name = "author")
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@JSONField(name = "comments")
	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@JSONField(name = "contains")
	public List<String> getContains() {
		return contains;
	}

	public void setContains(List<String> contains) {
		this.contains = contains;
	}

	@JSONField(name = "dataSourceVersion")
	public String getDataSourceVersion() {
		return dataSourceVersion;
	}

	public void setDataSourceVersion(String dataSourceVersion) {
		this.dataSourceVersion = dataSourceVersion;
	}

	@JSONField(name = "displayName")
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@JSONField(name = "drawAs")
	public String getDrawAs() {
		return drawAs;
	}

	public void setDrawAs(String drawAs) {
		this.drawAs = drawAs;
	}

	@JSONField(name = "fill")
	public String getFill() {
		return fill;
	}

	public void setFill(String fill) {
		this.fill = fill;
	}

	@JSONField(name = "fontSize")
	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	@JSONField(name = "fontWeight")
	public String getFontWeight() {
		return fontWeight;
	}

	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
	}

	@JSONField(name = "gpmlElementName")
	public String getGpmlElementName() {
		return gpmlElementName;
	}

	public void setGpmlElementName(String gpmlElementName) {
		this.gpmlElementName = gpmlElementName;
	}

	@JSONField(name = "x")
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	@JSONField(name = "y")
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@JSONField(name = "zIndex")
	public int getZIndex() {
		return zIndex;
	}

	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
	}

	@JSONField(name = "wpType")
	public String getWpType() {
		return wpType;
	}

	public void setWpType(String wpType) {
		this.wpType = wpType;
	}

	@JSONField(name = "xrefDataSource")
	public String getXrefDataSource() {
		return xrefDataSource;
	}

	public void setXrefDataSource(String xrefDataSource) {
		this.xrefDataSource = xrefDataSource;
	}

	@JSONField(name = "xrefIdentifier")
	public String getXrefIdentifier() {
		return xrefIdentifier;
	}

	public void setXrefIdentifier(String xrefIdentifier) {
		this.xrefIdentifier = xrefIdentifier;
	}

	@JSONField(name = "textAlign")
	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	@JSONField(name = "verticalAlign")
	public String getVerticalAlign() {
		return verticalAlign;
	}

	public void setVerticalAlign(String verticalAlign) {
		this.verticalAlign = verticalAlign;
	}

	public static class Comment {
		private String content;
		private String source;

		@JSONField(name = "content")
		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		@JSONField(name = "source")
		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}
	}

	public static class Xref {
		private String id;
		private String database;

		@JSONField(name = "id")
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@JSONField(name = "database")
		public String getDatabase() {
			return database;
		}

		public void setDatabase(String database) {
			this.database = database;
		}
	}

    public static class BiopaxRef {
        private String id;
        private String type;

        @JSONField(name = "id")
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @JSONField(name = "type")
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
