package inahiki.guap.diploma.test.xssf;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class XSSFLoader {

    private List<Integer> scoreColumns;
    private String dirPath;
    private String fileName;

    public XSSFLoader() {}

    public void setScoreColumns(int... columns) {
        scoreColumns = Arrays.stream(columns).boxed().collect(Collectors.toList());
    }

    public Map<Integer, List<Double>> load() throws IOException, InvalidFormatException {
        Map<Integer, List<Double>> map = new HashMap<>();
        File file = new File(dirPath, fileName);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        int rowId = 0;
        while (true) {
            Row row = sheet.getRow(rowId++);
            if (row == null) {
                return map;
            }
            List<Double> scores = new ArrayList<>(scoreColumns.size());
            for (int column : scoreColumns) {
                Cell cell = row.getCell(column);
                if (cell == null || cell.getCellType() == CellType.STRING) {
                    return map;
                }
                scores.add(cell.getNumericCellValue());
            }
            map.put(rowId, scores);
        }
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
