package module.evoldist.operator.pairdist;

/**
 * 
 * @ClassName AmbiguousBaseCode
 * 
 * @author mhl,ydl
 * 
 * @Date Created on:2019-09-06 17:28
 * 
 */
public enum AmbiguousBaseCode {

	A {
		@Override
		public void simpleSwithToGetParameter(double factor, char base, SimpleObj simpleObj) {
			switch (base) {
			case 'A':
				simpleObj.addOneValidateLength(factor);
				break;
			case 'T':
				simpleObj.addOneValidateLength(factor);
				simpleObj.addDiffInfo(0, 0, factor);
				break;
			case 'C':
				simpleObj.addOneValidateLength(factor);
				simpleObj.addDiffInfo(0, 0, factor);
				break;
			case 'G':
				simpleObj.addOneValidateLength(factor);
				simpleObj.addDiffInfo(factor, 0, 0);
				break;
			default:
				System.err.println("Warning: its impossible!\t" + getClass());
				break;
			}

		}


	},
	T {

		@Override
		public void simpleSwithToGetParameter(double factor,char base, SimpleObj simpleObj) {
			switch (base) {
			case 'A':
				simpleObj.addOneValidateLength(factor);
				simpleObj.addDiffInfo(0, 0, factor);
				break;
			case 'T':
				simpleObj.addOneValidateLength(factor);
				//simpleObj.addDiffInfo(0, 0, factor);
				break;
			case 'C':
				simpleObj.addOneValidateLength(factor);
				simpleObj.addDiffInfo(0, factor, 0);
				break;
			case 'G':
				simpleObj.addOneValidateLength(factor);
				simpleObj.addDiffInfo(0, 0, factor);
				break;
			default:
				System.err.println("Warning: its impossible!\t" + getClass());
				break;
			}
		}

	},
	C {

		@Override
		public void simpleSwithToGetParameter(double factor,char base, SimpleObj simpleObj) {
			switch (base) {
			case 'A':
				simpleObj.addOneValidateLength(factor);
				simpleObj.addDiffInfo(0, 0, factor);
				break;
			case 'T':
				simpleObj.addOneValidateLength(factor);
				simpleObj.addDiffInfo(0, factor, 0);
				break;
			case 'C':
				simpleObj.addOneValidateLength(factor);
				break;
			case 'G':
				simpleObj.addOneValidateLength(factor);
				simpleObj.addDiffInfo(0, 0, factor);
				break;
			default:
				System.err.println("Warning: its impossible!\t" + getClass());
				break;
			}
		}

	},
	G {

		@Override
		public void simpleSwithToGetParameter(double factor,char base, SimpleObj simpleObj) {
			switch (base) {
			case 'A':
				simpleObj.addOneValidateLength(factor);
				simpleObj.addDiffInfo(factor, 0, 0 );
				break;
			case 'T':
				simpleObj.addOneValidateLength(factor);
				simpleObj.addDiffInfo(0, 0, factor);
				break;
			case 'C':
				simpleObj.addOneValidateLength(factor);
				simpleObj.addDiffInfo(0, 0, factor);
				break;
			case 'G':
				simpleObj.addOneValidateLength(factor);
				break;
			default:
				System.err.println("Warning: its impossible!\t" + getClass());
				break;
			}
		}

	} ;
	

	public abstract void simpleSwithToGetParameter(double factor,char base, SimpleObj simpleObj);
	
}
