package module.batchexcom;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TsvPrettyPrinter {

    public static void main(String[] args) {
        if (args.length < 1) {
			System.err.println("Please provides the tsv file path.");
            return;
        }
        
        String filePath = args[0];
        List<String[]> rows = new ArrayList<>();
        int[] columnWidths = null;

        // 读取并解析文件
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                rows.add(fields);
                
                // 初始化或更新列宽
                if (columnWidths == null) {
                    columnWidths = new int[fields.length];
                }
                for (int i = 0; i < fields.length; i++) {
                    columnWidths[i] = Math.max(columnWidths[i], fields[i].length());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 打印表格
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                System.out.printf("%-" + (columnWidths[i] + 1) + "s", row[i]);
            }
            System.out.println();
        }
    }
}
