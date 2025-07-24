package utils.storage;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 *
 * <h1>目的</h1>
 * <p>
 *
 * 进行快捷持久化存储
 * </p>
 *
 * <h1>输入/输出</h1>
 *
 * <p>
 * Map和存储相互转换
 * </p>
 *
 * <h1>使用方法</h1>
 *
 * <blockquote>
 *
 * <pre>
 * Map<String, Integer> map = Maps.newHashMap();
 * map.put("a", 2);
 * map.put("b", 2);
 * map.put("c", 2);
 *
 * File tempFile = File.createTempFile("abcd", "gg");
 * System.out.println(tempFile);
 * MapPersistentStorer.storeStr2numberMap(map, tempFile.getAbsolutePath());
 * Map<String, Integer> map2 = MapPersistentStorer.getStr2numberMap(tempFile.getAbsolutePath());
 *
 * System.out.println(map);
 * </pre>
 *
 * </blockquote>
 *
 * <h1>注意点</h1>
 * <ol>
 * <li>暂时没有注意点，这个类很好用，要多用</li>
 * </ol>
 *
 * @implSpec 直接调用序列化的类完成这个任务。
 *
 * @author yudal
 *
 */
public class MapPersistence {

    private static final Logger log = LoggerFactory.getLogger(MapPersistence.class);

    /**
     * 将一个 String 到 Integer 的 Map 持久化存储到指定路径。
     * 使用 GZIP 压缩和对象序列化的方式保存数据。
     *
     * @param map   要存储的 Map 数据
     * @param gpath 存储文件的路径
     */
    public static void storeStr2numberMap(Map<String, Integer> map, String gpath) {
        ObjectPersistence.saveObjectByObjectOutputWithGZIP(map, gpath);
    }

    /**
     * 从指定路径加载一个 String 到 Integer 的 Map。
     * 如果文件不存在，则返回一个空的 HashMap。
     *
     * @param path 存储文件的路径
     * @return 加载的 Map 数据，如果文件不存在或读取失败，则返回空的 HashMap
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Integer> getStr2numberMap(String path) {
        File storePathFile = new File(path);
        if (!storePathFile.exists()) {
            return new HashMap<>();
        }

        try {
            GZIPInputStream gis = new GZIPInputStream(new FileInputStream(path));
            ObjectInputStream ois = new ObjectInputStream(gis);
            Object o = ois.readObject();
            ois.close();
            return (Map<String, Integer>) o;
        } catch (Exception ex) {
            log.error("Map persistence", ex);
        }

        return new HashMap<>();
    }

    /**
     * 将一个 String 到 String 的 Map 持久化存储到指定路径。
     * 使用 GZIP 压缩和对象序列化的方式保存数据。
     *
     * @param map   要存储的 Map 数据
     * @param gpath 存储文件的路径
     */
    public static void storeStr2strMap(Map<String, String> map, String gpath) {
        ObjectPersistence.saveObjectByObjectOutputWithGZIP(map, gpath);
    }

    /**
     * 从指定路径加载一个 String 到 String 的 Map。
     * 如果文件不存在，则返回一个空的 HashMap。
     *
     * @param path 存储文件的路径
     * @return 加载的 Map 数据，如果文件不存在或读取失败，则返回空的 HashMap
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getStr2strMap(String path) {
        File storePathFile = new File(path);
        if (!storePathFile.exists()) {
            return new HashMap<>();
        }

        try {
            GZIPInputStream gis = new GZIPInputStream(new FileInputStream(path));
            ObjectInputStream ois = new ObjectInputStream(gis);
            Object o = ois.readObject();
            ois.close();
            return (Map<String, String>) o;
        } catch (Exception ex) {
            log.error("Map persistence", ex);
        }

        return new HashMap<>();
    }

    /**
     * 将一个 String 到 String 的 Map 以 JSON 格式保存到指定路径。
     *
     * @param map   要存储的 Map 数据
     * @param path  存储文件的路径
     * @throws IOException 如果写入文件时发生 I/O 错误
     */
    public static void saveStr2StrMapToJSON(Map<String, String> map, String path) throws IOException {
        String jsonString = JSONObject.toJSONString(map, false);
        FileUtils.writeStringToFile(new File(path), jsonString, StandardCharsets.UTF_8);
    }

    /**
     * 从指定路径加载一个以 JSON 格式存储的 String 到 String 的 Map。
     *
     * @param path 存储文件的路径
     * @return 加载的 Map 数据
     * @throws IOException 如果读取文件时发生 I/O 错误
     */
    public static HashMap<String, String> getStr2StrMapFromJSON(String path) throws IOException {
        String fileToString = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
        @SuppressWarnings("unchecked")
        HashMap<String, String> object = JSONObject.parseObject(fileToString, HashMap.class);
        return object;
    }

}
