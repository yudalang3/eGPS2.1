package module.fastatools;

public enum FastaToolsNameEnum {
	AMAZINGFASTAEXRACOR("Sequence Exractor"), 
	FASTAIDRENAMER("Sequence Renamer");
	
//	QUACKFASTAEXRACTORFILTER("Quack Exractor or Filter"),
//	FASTASUBSEQ("Subseq"), 
//	FASTAIDSIMPLIFER("ID Simplifer"), 
//	FASTATABLECONVERT("Table Convert"), 
//	FASTAMERGEANDSPLIT("Merge and Split"),
//	FASTASEQUENCEMANIPULAOR("Sequence Manipulaor"),
//	FASTALONGESTREPRESENTATIVE("Longest Representative"),
//	FASTAIDAPPENDER("ID Appender"), 
//	FASTASEQUENCESPATTERNLOCATION("Sequences Pattern Location");

	private String tableName;

	private FastaToolsNameEnum(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}
}
