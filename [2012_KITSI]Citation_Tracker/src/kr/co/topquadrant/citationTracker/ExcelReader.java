/**
 * 
 */
package kr.co.topquadrant.citationTracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.examples.CellTypes;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author coreawin
 * @sinse 2012. 11. 30.
 * @version 1.0
 * @history 2012. 11. 30. : 최초 작성 <br>
 * 
 */
public class ExcelReader {

	private File xls = null;
	private List<List<String>> dataSet = new ArrayList<List<String>>();

	public ExcelReader(String excelPath) throws Exception {
		this.xls = new File(excelPath);
		if (!this.xls.isFile()) {
			throw new FileNotFoundException(excelPath + " 파일이 존재하지 않습니다.");
		} else {
			read();
		}
	}

	public List<List<String>> getIDData() {
		return dataSet;
	}

	private void read() throws Exception {
		InputStream inp = null;
		int sheetIdx = 0;
		try {
			inp = new FileInputStream(xls);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(sheetIdx);
			int cnt = 1;
			List<String> datas = new ArrayList<String>();
			for (Row row : sheet) {
				int cellNo = row.getFirstCellNum();
				Cell cell = row.getCell(cellNo);
				if (cell != null) {
					int cellType = cell.getCellType();
					String v = "";
					switch (cellType) {
					case Cell.CELL_TYPE_NUMERIC:
						v = String.valueOf((long)cell.getNumericCellValue());
						break;
					default:
						v = cell.getStringCellValue();
						break;
					}
					if (checkEID(v)) {
						datas.add(v);
						if (datas.size() == 1000) {
							dataSet.add(datas);
							datas = new ArrayList<String>();
						}
						cnt++;
					}
				}
			}
			if (datas.size() > 0) {
				dataSet.add(datas);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (inp != null)
				inp.close();
		}
	}

	private boolean checkEID(String s) {
		if (s != null) {
			s = s.trim();
			try {
				Long.parseLong(s);
			} catch (Exception e) {
				return false;
			}
			if (s.length() > 10 & s.length() < 13) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		String path = "z:\\SCOPUS\\20121130_113407.xlsx";
		System.out.println(new ExcelReader(path).getIDData());
	}
}
