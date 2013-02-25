/**
 * 
 */
package kr.co.topquadrant.citationTracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author coreawin
 * @sinse 2013. 2. 25.
 * @version 1.0
 * @history 2013. 2. 25. : ���� �ۼ� <br>
 * 
 */
public abstract class Readers {

	File file = null;
	List<List<String>> dataSet = new ArrayList<List<String>>();

	protected Readers(String filePath) throws Exception {
		this.file = new File(filePath);
		if (!this.file.isFile()) {
			throw new FileNotFoundException(filePath + " ������ �������� �ʽ��ϴ�.");
		} else {
			read();
		}
	}

	abstract void read() throws Exception;

	public List<List<String>> getIDData() {
		return dataSet;
	}

	protected boolean checkEID(String s) {
		if (s != null) {
			s = s.trim();
			try {
				Long.parseLong(s);
			} catch (Exception e) {
				return false;
			}
			if (s.length() >= 10 & s.length() < 13) {
				return true;
			}
		}
		return false;
	}

}
