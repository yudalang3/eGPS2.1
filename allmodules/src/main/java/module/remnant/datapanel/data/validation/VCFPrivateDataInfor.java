package module.remnant.datapanel.data.validation;

import java.util.Arrays;

import msaoperator.DataForamtPrivateInfor;
import org.apache.commons.lang3.tuple.Pair;

public class VCFPrivateDataInfor implements DataForamtPrivateInfor {
	
		String[] individualNames;
		
		public VCFPrivateDataInfor(String[] individualNames) {
			this.individualNames = individualNames;
		}

		@Override
		public Pair<Boolean, String> isCompatiable(DataForamtPrivateInfor anotherInfor) {
			if (anotherInfor instanceof VCFPrivateDataInfor) {
				VCFPrivateDataInfor other = (VCFPrivateDataInfor) anotherInfor;
				// 下面这个判定是讲究顺序的，但是VCF一般顺序是一样的！
				if (Arrays.equals(this.individualNames, other.individualNames)) {
					return Pair.of(true, "") ;
				}else {
					return Pair.of(false,"The input VCF file Not contains the same individuals information with first accept VCF file");
				}
				
			}else {
				return Pair.of(false, "Not the same private information, this is "+getFormatName() +"while another is "+anotherInfor.getFormatName());
			}
		}
		
		@Override
		public String getFormatName() {
			return "VCF individuals information";
		}
		
}
