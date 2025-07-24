package module.multiseqview.model.paint;

public class CrossSequenceDirectionPaintObj {
	
	public double xP1;public double yP1;
	public double xP2;public double yP2;
	public double xP3;public double yP3;
	public double xP4;public double yP4;
	
	
	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		
		sBuilder.append(xP1).append(" ").append(yP1).append(":");
		sBuilder.append(xP2).append(" ").append(yP2).append(":");
		sBuilder.append(xP3).append(" ").append(yP3).append(":");
		sBuilder.append(xP4).append(" ").append(yP4).append("\n");
		
		return sBuilder.toString();
	}

}
