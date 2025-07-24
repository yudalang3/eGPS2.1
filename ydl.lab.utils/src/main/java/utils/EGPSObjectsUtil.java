package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;

public class EGPSObjectsUtil {

	private EGPSObjectsUtil() {

	}

	/**
	 * 
	 * 一个快速对Java对象的快速持久化保存，未考虑serialVersionUID。只是用来保存一下而已。
	 * 
	 * @title quickSaveAnObject2file
	 * @createdDate 2020-11-03 14:16
	 * @lastModifiedDate 2020-11-03 14:16
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @param obj
	 * @param file
	 * @return void
	 * @throws IOException
	 * @throws
	 */
	public static void quickSaveAnObject2file(Object obj, File file) throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
		os.writeObject(obj);
		os.close();
	}

	/**
	 * 
	 * 
	 * 一个快速对Java对象的快速持久化保存，未考虑serialVersionUID。只是用来保存一下而已。
	 * 
	 * @title quickSaveAnObject2file
	 * @createdDate 2020-11-03 14:16
	 * @lastModifiedDate 2020-11-03 14:16
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @param obj
	 * @param file
	 * @return void
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object quickObtainAnObjectFromFile(File file) throws IOException, ClassNotFoundException {
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
		Object readObject = input.readObject();
		input.close();
		return readObject;
	}

	/**
	 * 通过json文件来保存配置的属性
	 * @param object
	 * @param outFile
	 * @throws IOException
	 */
	public static void persistentSaveJavaBeanByFastaJSON(Object object, File outFile) throws IOException {
		String jsonString = JSON.toJSONString(object);
		FileUtils.writeStringToFile(outFile, jsonString);
		
	}

	/**
	 * 快速通过配置文件生成java类
	 * @param <T>
	 * @param clz
	 * @param outFile
	 * @return
	 * @throws IOException
	 */
	public static <T> T obtainJavaBeanByFastaJSON(Class<T> clz, File outFile) throws IOException {
		String readFileToString = FileUtils.readFileToString(outFile);
		return JSON.parseObject(readFileToString, clz);
	}
}
