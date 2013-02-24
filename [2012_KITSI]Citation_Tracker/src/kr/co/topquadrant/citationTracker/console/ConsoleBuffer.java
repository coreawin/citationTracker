package kr.co.topquadrant.citationTracker.console;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author coreawin
 * 
 */
public class ConsoleBuffer {

	/**
	 * 콘솔의 버퍼 크기. 최대 40kb
	 */
	final int bufferSize = 1024 * 10;

	private StringBuilder builder = new StringBuilder(bufferSize);

	private final String ENTER = "\n";
	private final String TAB = "\t";

	public void setText(String t) {
		builder.insert(0, t);
		checkBuilderSize();
	}

	private StringBuffer tmpBuffer = new StringBuffer();

	public void setTextLine(ConsoleOut.LEVEL level, String line) {
		tmpBuffer.setLength(0);
		tmpBuffer.append(level);
		tmpBuffer.append(TAB);
		tmpBuffer.append(currentDate());
		tmpBuffer.append(TAB);
		tmpBuffer.append(line);
		tmpBuffer.append(ENTER);
		builder.insert(0, tmpBuffer);
		checkBuilderSize();
	}

	final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static String currentDate() {
		return format.format(new Date());
	}

	/**
	 * 문자열은 항상 최대 버퍼 사이즈 만큼만 유지한다.<br>
	 */
	private void checkBuilderSize() {
		if (builder.length() > bufferSize) {
			builder.delete(bufferSize, builder.length());
			builder.deleteCharAt(builder.length() - 1);
			if (builder.lastIndexOf(ENTER) != -1) {
				builder.delete(builder.lastIndexOf(ENTER), builder.length());
			}
		}
	}

	public String getText() {
		return builder.toString();
	}

	public String toString() {
		return builder.toString();
	}

	public void clearBuffer() {
		builder.delete(0, builder.length() - 1);
	}
}
