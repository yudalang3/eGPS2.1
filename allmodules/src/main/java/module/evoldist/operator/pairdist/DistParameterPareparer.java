package module.evoldist.operator.pairdist;

public interface DistParameterPareparer {
	
	DistParameterLevel1 getLeve1Para(String seq1, String seq2);
	
	DistParameterLevel2 getLeve2Para(String seq1, String seq2);

	DistParameterLevel3 getLeve3Para(String seq1, String seq2);
	
	DistParameterLevel41 getLeve41Para(String seq1, String seq2);
	
	DistParameterLevel42 getLeve42Para(String seq1, String seq2);
	
	DistParameterLevel51 getLeve51Para(String seq1, String seq2);

}
