package module.chorddiagram.work;

public class BioInteraction {

    private BioEntity source;
    private int sourceStartPosition;
    private int sourceEndPosition;
    private BioEntity target;
    private int targetStartPosition;
    private int targetEndPosition;

	@Override
	public String toString() {
		return String.format("Source: %s [Start: %d, End: %d], Target: %s [Start: %d, End: %d]", source.getName(),
				sourceStartPosition, sourceEndPosition, target.getName(), targetStartPosition, targetEndPosition);
	}


    public int getTargetEndPosition() {
        return targetEndPosition;
    }

    public void setTargetEndPosition(int targetEndPosition) {
        this.targetEndPosition = targetEndPosition;
    }

    public int getTargetStartPosition() {
        return targetStartPosition;
    }

    public void setTargetStartPosition(int targetStartPosition) {
        this.targetStartPosition = targetStartPosition;
    }

    public BioEntity getTarget() {
        return target;
    }

    public void setTarget(BioEntity target) {
        this.target = target;
    }

    public int getSourceEndPosition() {
        return sourceEndPosition;
    }

    public void setSourceEndPosition(int sourceEndPosition) {
        this.sourceEndPosition = sourceEndPosition;
    }

    public int getSourceStartPosition() {
        return sourceStartPosition;
    }

    public void setSourceStartPosition(int sourceStartPosition) {
        this.sourceStartPosition = sourceStartPosition;
    }

    public BioEntity getSource() {
        return source;
    }

    public void setSource(BioEntity source) {
        this.source = source;
    }
}
