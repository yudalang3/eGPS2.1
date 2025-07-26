package module.pptxio;

import com.google.common.base.Stopwatch;
import egps2.utils.common.util.poi.pptx.Decoder4pptx;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

class Decoder4pptxTest {


	private static final Logger log = LoggerFactory.getLogger(Decoder4pptxTest.class);

	public static void main(String[] args) throws Exception {
		Stopwatch stopwatch = Stopwatch.createStarted();

		File file = new File("C:\\Users\\yudal\\.egps2\\config\\bioData\\pathwayBrowser\\wnt_pathway_short.pptx");
		Decoder4pptx decoder4pptx = new Decoder4pptx();
		decoder4pptx.decodeFile(file);

		List<XSLFShape> listOfShapes = decoder4pptx.getListOfShapes();
		log.info("Number of shapes is {}", listOfShapes.size());
		for (XSLFShape shape : listOfShapes) {
			log.info("Shape name is {}", shape.getShapeName());
		}

		stopwatch.stop();
		System.out.println(stopwatch);
	}


	private static void test1() {
		// 输入的PPT文件路径
		String inputFile = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\工作汇报\\博士后主线\\TheEvolutionOfWntSignalingPathway.pptx";
		// 输出的PPT文件路径
		String outputFile = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\工作汇报\\博士后主线\\test1.pptx";

		try (FileInputStream fis = new FileInputStream(inputFile);
			 XMLSlideShow ppt = new XMLSlideShow(fis)) {
			XMLSlideShow output = new XMLSlideShow();
			XSLFSlide slide = output.createSlide();

			// 获取PPT的第一页幻灯片
			XSLFSlide firstSlide = ppt.getSlides().get(0);

			System.out.println("PPT page size: " + ppt.getPageSize());
			// 遍历幻灯片中的所有形状（如文本框、图片等）
			for (XSLFShape shape : firstSlide.getShapes()) {
				// 打印每个元素的类型和其他相关信息
				System.out.print("Anchor: " + shape.getAnchor() + "\t");
				System.out.println("Shape type: " + shape.getClass().getSimpleName());

				// 检查是否为文本框类型并打印文本内容
				if (shape instanceof org.apache.poi.xslf.usermodel.XSLFTextShape) {
					org.apache.poi.xslf.usermodel.XSLFTextShape textShape = (org.apache.poi.xslf.usermodel.XSLFTextShape) shape;
//                    System.out.println("Text content: " + textShape.getText());
				}

				// 你可以根据需要扩展，处理不同的元素（如图形、图片等）

//                slide.draw();
			}


//            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
//                output.write(fos);
//                System.out.println("PPT已成功保存到: " + outputFile);
//            }

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void test2(String[] args) {
		String userHomeDir = System.getProperty("user.home");
		System.out.println(userHomeDir);
	}
}
