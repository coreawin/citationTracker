/**
 * 
 */
package kr.co.topquadrant.citationTracker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author coreawin
 * @sinse 2012. 11. 30.
 * @version 1.0
 * @history 2012. 11. 30. : 최초 작성 <br>
 * 
 */
public class ResultWriter {

	public static final char COMMA = ',';
	private static final char ENTER = '\n';

	BufferedWriter bw = null;

	public ResultWriter(String name) throws IOException {
		bw = new BufferedWriter(new FileWriter(name));
	}

	public void print(String s) throws IOException {
		bw.write(s);
	}

	public void println(String s) throws IOException {
		bw.write(s);
		bw.write(ENTER);
	}

	public void flush() throws IOException {
		if (bw != null) {
			bw.flush();
		}
	}

	public void close() throws IOException {
		flush();
		if (bw != null) {
			bw.close();
		}
	}

}
