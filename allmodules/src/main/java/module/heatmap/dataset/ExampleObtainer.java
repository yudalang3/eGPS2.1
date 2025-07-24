package module.heatmap.dataset;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import utils.EGPSFileUtil;

/**
 * 这个包里面的文本存放可以作为示例的数据
 */
public class ExampleObtainer {

	public int getExampleCount() {
		return 6;
	}

	public List<String> getExampleContent(int index) {

		index = index % 6;
		InputStream inputSteam = null;

		List<String> ret = null;

		if (index == 0) {
			inputSteam = this.getClass().getResourceAsStream("testData_withParameters1.eheatmap");
		} else if (index == 1) {
			inputSteam = this.getClass().getResourceAsStream("testData_withParameters2.eheatmap");
		} else if (index == 2) {
			inputSteam = this.getClass().getResourceAsStream("smalMatrix_needToDoDataTransformation.txt");
		} else if (index == 3) {
			inputSteam = this.getClass().getResourceAsStream("testData_noParameters_20rows.txt");
		} else if (index == 4) {
			inputSteam = this.getClass().getResourceAsStream("testData_noParameters_10rows.txt");
		} else {
			ret = Arrays.asList(",sample1,sample2,sample3,sample4,sample5", "Gene1,10,20,14,13,46",
					"Gene2,20,30,70,80,4", "Gene3,13,64,37,9,68", "Gene4,44,23,90,43,8", "Gene5,23,54,78,65,1",
					"Gene6,44,19,64,22,41");
		}

		if (ret == null) {
			try {
				ret = EGPSFileUtil.getContentFromInputStreamAsLines(inputSteam);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return ret;
	}
}
