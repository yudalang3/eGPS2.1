package module.evolview.gfamily.work.beans;

import java.awt.Color;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import utils.string.EGPSStringUtil;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertyElement;
import module.evolview.gfamily.work.gui.browser.draw.DrawingPropertySequenceModel;
import graphic.engine.colors.EGPSColors;

/**
 * 读取domain文件(压缩/非压缩)
 *
 * @Author: mhl
 */
public class ReaderDoMain {

	/**
	 * 读取本地文件,非压缩形式的
	 *
	 * @Author: mhl
	 * @Date Created on: 2020-07-03 10:26
	 */
	public DrawingPropertySequenceModel parseWithLocalFile(File file) throws Exception {
		List<String> domainInforStrings = FileUtils.readLines(file, StandardCharsets.UTF_8);
		List<String> contens = new ArrayList<>();
		for (String string : domainInforStrings) {
			String trim = string.trim();
			if (trim.isEmpty() || trim.charAt(0) == '#') {
				continue;
			}
			contens.add(trim);
		}

		return producePaintingModel(contens);
	}

	public DrawingPropertySequenceModel generateNcov2019GenomeElementModels(String text) throws Exception {
		String[] splits = EGPSStringUtil.split(text, '\t');

		List<String> contens = new ArrayList<>();
		for (String string : splits) {
			String trim = string.trim();
			if (trim.isEmpty() || trim.charAt(0) == '#') {
				continue;
			}
			contens.add(trim);
		}

		return producePaintingModel(contens);
	}

	public DrawingPropertySequenceModel producePaintingModel(List<String> splits) {
		DrawingPropertySequenceModel ncov2019GenomePaintElementModelItem = new DrawingPropertySequenceModel();
		List<DrawingPropertyElement> genomeElementModels = new ArrayList<>();

		Iterator<String> iterator = splits.iterator();
		ncov2019GenomePaintElementModelItem.setHeader(iterator.next());

		while (iterator.hasNext()) {
			String readLine = iterator.next();
			String[] split = readLine.split("\t");
			if (split.length < 5) {
				throw new IllegalArgumentException("Sorry, input line must greater than 4");
			}
			DrawingPropertyElement genomeElementModel = new DrawingPropertyElement();
			genomeElementModel.setName(split[0]);
			genomeElementModel.setGene(split[1]);
			int int1 = Integer.parseInt(split[2]);
			genomeElementModel.setStartPosition(int1);
			int int2 = Integer.parseInt(split[3]);
			genomeElementModel.setEndPositoin(int2);
			
			Color color = EGPSColors.parseColor(split[4]);
			genomeElementModel.setColor(color);

			genomeElementModels.add(genomeElementModel);
		}
		ncov2019GenomePaintElementModelItem.setOrigianlSeqElementModels(genomeElementModels);
		return ncov2019GenomePaintElementModelItem;
	}

}
