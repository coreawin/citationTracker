/**
 * 
 */
package kr.co.topquadrant.citationTracker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author coreawin
 * @sinse 2013. 2. 25.
 * @version 1.0
 * @history 2013. 2. 25. : 최초 작성 <br>
 * 
 */
public class CSVReader extends Readers {

	/**
	 * @param filePath
	 * @throws Exception
	 */
	public CSVReader(String filePath) throws Exception {
		super(filePath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.topquadrant.citationTracker.Readers#read()
	 */
	@Override
	void read() throws Exception {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(super.file));
			String line = null;
			List<String> datas = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				if (line != null)
					line = line.trim();
				String[] e = line.split(",");
				for (String v : e) {
					if (v == null)
						continue;
					v = v.trim();
					if (checkEID(v)) {
						datas.add(v);
						if (datas.size() == 1000) {
							dataSet.add(datas);
							datas = new ArrayList<String>();
						}
					}
				}
			}
			if (datas.size() > 0) {
				dataSet.add(datas);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (br != null)
				br.close();
		}
	}

}
