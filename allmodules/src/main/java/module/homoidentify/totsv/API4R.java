package module.homoidentify.totsv;

public class API4R {

	public void domtbl_output_to_tsv(String input, String output) throws Exception {
		Domtbl2Table domtbl2Table = new Domtbl2Table();
		domtbl2Table.setInputPath(input);
		domtbl2Table.setOutputPath(output);

		domtbl2Table.doIt();
	}
}
