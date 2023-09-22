package utilities.excelutil;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Reads Excel file data to be used in test scripts. See example file to view expected format.
 */

public class ExcelDataReader {
    private ExcelFile excelFile;
    private Map<String, String> keywordData;

    /**
     * Constructs a new ExcelDataReader object.
     *
     * @param excelFile the ExcelFile object representing the Excel file to read data from
     * @param keyword the keyword to search for in the Excel file
     */
    public ExcelDataReader(ExcelFile excelFile, String keyword){
        this.excelFile = excelFile;
        this.keywordData = getKeywordData(keyword);
    }

    /**
     * Retrieves the data from a specific cell in the Excel file.
     *
     * @param rowNum the row number of the cell
     * @param colNum the column number of the cell
     * @return the data stored in the specified cell as a String
     */
    public String getCellData(int rowNum, int colNum) {
        XSSFCell cell = excelFile.getExcelSheet().getRow(rowNum).getCell(colNum);
        return cell.getStringCellValue();
    }

    /**
     * Checks if a given keyword exists in the Excel sheet.
     *
     * @param keyword the keyword to search for
     * @return true if the keyword exists in the sheet, false otherwise
     */
    private boolean keywordExistsInSheet(String keyword) {
        DataFormatter df = new DataFormatter();
        Iterator rowIterator = excelFile.getExcelSheet().rowIterator();
        String cellValue = "";
        boolean keywordExists = false;

        while (rowIterator.hasNext()) {
            XSSFRow row = (XSSFRow) rowIterator.next();
            XSSFCell cell = row.getCell(0);
            cellValue = df.formatCellValue(cell);

            if (!cellValue.equals("END")) {
                if (cellValue.equals(keyword)) {
                    keywordExists = true;
                    break;
                }
            }
            else {
                keywordExists = false;
            }

        }
        return keywordExists;
    }

    /**
     * Returns the index of the row where a given keyword exists in the Excel sheet.
     * If the keyword does not exist, returns -1.
     *
     * @param keyword the keyword to search for
     * @return the index of the row where the keyword exists, -1 if the keyword does not exist
     */
    private int getRowIndexOfKeyword(String keyword) {

        if(keywordExistsInSheet(keyword)){
            Iterator rowIterator = excelFile.getExcelSheet().rowIterator();
            int rowIndex = 0;
            String cellValue = "";
            DataFormatter df = new DataFormatter();

            while (rowIterator.hasNext()) {
                XSSFRow row = (XSSFRow) rowIterator.next();
                XSSFCell cell = row.getCell(0);
                cellValue = df.formatCellValue(cell);

                if (cellValue.equals(keyword)) {
                    rowIndex = cell.getRowIndex();
                    break;
                }
            }

            return rowIndex;
        }
        else{
            return - 1;
        }
    }

    /**
     * Returns a map containing the keyword data for a given keyword.
     * If the keyword does not exist, prints "Keyword doesn't exist" and returns an empty map.
     *
     * @param keyword the keyword to search for
     * @return a map containing the keyword data, an empty map if the keyword does not exist
     */
    private Map<String, String> getKeywordData(String keyword) {
        int keywordRowIndex;
        XSSFCell parameterCell = null;
        XSSFCell parameterValueCell = null;
        DataFormatter df = new DataFormatter();
        String parameter = "";
        String parameterValue = "";

        if (keywordExistsInSheet(keyword)) {
            keywordRowIndex = getRowIndexOfKeyword(keyword);
            keywordData = new HashMap<String,String>();

            boolean isCellEmpty = false;
            int i = 1;

            while(!isCellEmpty){
                parameterCell = excelFile.getExcelSheet().getRow(keywordRowIndex).getCell(i);
                parameterValueCell = excelFile.getExcelSheet().getRow(keywordRowIndex + 1).getCell(i);
                if(!df.formatCellValue(parameterCell).isEmpty()){
                    parameter = df.formatCellValue(parameterCell);
                    parameterValue = df.formatCellValue(parameterValueCell);
                    keywordData.put(parameter,parameterValue);
                    i++;
                }
                else{
                    isCellEmpty = true;
                }
            }

        }
        else{
            System.out.println("Keyword doesn't exist");
        }

        return keywordData;

    }

    /**
     * Returns the value of a specific parameter from the keyword data.
     * If the parameter does not exist, returns null.
     *
     * @param parameter the parameter to retrieve the value for
     * @return the value of the parameter, or null if the parameter does not exist
     */
    public String getParameterValue(String parameter){
        return this.keywordData.get(parameter).toString().trim();
    }


}
