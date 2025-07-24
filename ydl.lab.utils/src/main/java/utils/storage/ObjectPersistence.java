package utils.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectPersistence {

	private static final Logger log = LoggerFactory.getLogger(ObjectPersistence.class);

	/**
	 * File usually end with .ser
	 * @param o
	 * @param file
	 * @return
	 */
	public static boolean saveObjectByObjectOutput(Object o, String file) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
			objectOutputStream.writeObject(o);
			objectOutputStream.close();
		} catch (IOException e) {
			log.error("Map persistence", e);
			return false;
		}

		return true;
	}

	public static boolean saveObjectByObjectOutputWithGZIP(Object o, String file) {
		try {

			File file2 = new File(file);
			if (!file2.exists()) {
				FileUtils.createParentDirectories(file2);
			}

			FileOutputStream fos = new FileOutputStream(file2);
			GZIPOutputStream gz = new GZIPOutputStream(fos);

			ObjectOutputStream oos = new ObjectOutputStream(gz);

			oos.writeObject(o);
			oos.close();
		} catch (IOException ex) {
			log.error("Map persistence", ex);
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getObjectByObjectInput(String file) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			Object o = ois.readObject();
			return (T) o; // 使用强制类型转换
		} catch (Exception ex) {
			log.error("Map persistence", ex);
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public static <T> T getObjectByObjectInputWithGZIP(String file) {
		try (GZIPInputStream gis = new GZIPInputStream(new FileInputStream(file));
				ObjectInputStream ois = new ObjectInputStream(gis)) {
			Object o = ois.readObject();
			return (T) o; // 使用强制类型转换
		} catch (Exception ex) {
			log.error("Map persistence", ex);
		}

		return null;
	}

}
