package utilities.excelutil;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class ExcelFile {

    private File excelFile;
    static String systemDelimeter = File.separator;
//    private final String FILE_PATH_EXCLUDING_NAME = System.getProperty("user.dir").concat("\\src\\test\\resources\\dataexcelfiles\\");
    private String FILE_PATH_EXCLUDING_NAME ="";

    {
        try {
            FILE_PATH_EXCLUDING_NAME = ClassLoader.getSystemClassLoader().getResource("").toURI().getPath()+"dataexcelfiles"+systemDelimeter;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private String fileName;
    private String sheetName;
    private XSSFWorkbook excelWorkbook;
    private XSSFSheet excelSheet;

    /**
     * Constructs a new ExcelFile object.
     *
     * @param fileName the name of the Excel file to be opened
     * @param sheetName the name of the sheet to be accessed within the Excel file
     * @throws FileNotFoundException if the specified Excel file does not exist
     * @throws Exception if an error occurs while accessing the Excel file
     */
    public ExcelFile(String fileName,String sheetName){
        try{
            if(doesFileExist(fileName)){
                this.fileName = fileName;
                this.sheetName = sheetName;
                this.excelWorkbook = new XSSFWorkbook(FILE_PATH_EXCLUDING_NAME.concat(fileName).concat(".xlsx"));
                this.excelSheet = excelWorkbook.getSheet(sheetName);

            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    /**
     * Checks if the specified file exists.
     *
     * @param fileName the name of the file to check existence
     * @return true if the file exists, false otherwise
     */
    private boolean doesFileExist(String fileName){
        excelFile = new File(FILE_PATH_EXCLUDING_NAME.concat(fileName).concat(".xlsx"));
        return excelFile.exists();
    }

    /**
     * Returns the Excel workbook.
     *
     * @return the Excel workbook object
     */
    public XSSFWorkbook getExcelWorkbook(){
        return this.excelWorkbook;
    }

    /**
     * Returns the Excel sheet.
     *
     * @return the Excel sheet object
     */
    public XSSFSheet getExcelSheet(){
        return this.excelSheet;
    }

}
