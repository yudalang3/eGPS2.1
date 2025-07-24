package module.evolview.gfamily.work.gui.dialog;

public class FindSearchRecord {
    private short currentHightNodeIndex = 0;
    private String stringToFind;

    public int getCurrentHightNodeIndex() {
        return currentHightNodeIndex;
    }

    public void incrementCurrentHightNodeIndex() {
        currentHightNodeIndex++;
    }
    public void decreaseCurrentHightNodeIndex() {
        currentHightNodeIndex--;
    }

    public void setCurrentHightNodeIndex(short currentHightNodeIndex) {
        this.currentHightNodeIndex = currentHightNodeIndex;
    }

    public String getStringToFind() {
        return stringToFind;
    }

    public void setStringToFind(String stringToFind) {
        this.stringToFind = stringToFind;
    }

    public void resetToZero() {
        this.currentHightNodeIndex = 0;
    }
}
