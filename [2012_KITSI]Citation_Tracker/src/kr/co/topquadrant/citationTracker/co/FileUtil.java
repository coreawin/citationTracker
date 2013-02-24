package kr.co.topquadrant.citationTracker.co;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * File에 대한 유틸리티.
 * 
 * @author user
 */
public class FileUtil {
	/**
	 * 주어진 path를 가지는 디렉토리를 생성한다.
	 * 
	 * @param path
	 *            생성할 디렉토리 패스
	 * @return 주어진 path를 가지는 디렉토리 파일 객체를 생성해서 리턴한다.
	 */
	public static final File mkdirs(String path) {
		File dirs = new File(path);
		dirs.mkdirs();
		return dirs;
	}

	/**
	 * 주어진 파일 객체가 디렉토리인 경우에 내부의 디렉토리를 제외한 파일만 삭제한다.
	 * 
	 * @param file
	 *            File 객체(디렉토리를 나타내는 File 객체)
	 * @return 주어진 파일 객체가 디렉토리인 경우에 내부의 디렉토리를 제외한 파일만 삭제한다. 존재하지 않거나 디렉토리가 아닌
	 *         경우에는 아무일도 일어나지 않는다.
	 */
	public static final boolean deleteFiles(File file) {
		if (file.exists() == false)
			return true;

		if (file.isDirectory()) {
			String[] children = file.list();
			for (int i = 0; i < children.length; i++) {
				File child = new File(file, children[i]);
				if (child.isDirectory())
					continue;
				if (child.delete() == false)
					return false;
			}
		}

		return true;
	}

	/**
	 * 주어진 file 객체를 삭제한다.
	 * 
	 * @param file
	 *            삭제할 파일 객체
	 * @return 주어진 file을 삭제한다. 디렉토리인 경우에는 자신을 포함하여 모든 하위 파일들을 삭제한다.
	 */
	public static final boolean delete(File file) {
		if (file.exists() == false)
			return true;

		if (file.isDirectory()) {
			String[] children = file.list();
			for (int i = 0; i < children.length; i++) {
				if (delete(new File(file, children[i])) == false) {
					return false;
				}
			}
		}
		return file.delete();
	}

	/**
	 * 파일을 다른 파일로 복사한다.<br>
	 * 주어진 파일 객체가 디렉토리인 경우에는 아무일도 일어나지 않는다.
	 * 
	 * @param in
	 *            복사할 File 객체(파일을 나타내는 File 객체)
	 * @param out
	 *            복사대상 File 객체(파일을 나타내는 File 객체)
	 * @throws IOException
	 */
	public static final void copyFile(File in, File out) throws IOException {
		if (in.isDirectory() == true || out.isDirectory() == true)
			return;

		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			inChannel = new FileInputStream(in).getChannel();
			outChannel = new FileOutputStream(out).getChannel();

			// 다양한 OS를 고려한 최대 전송 바이트 크기
			int maxCount = (2 ^ 31 - 1) * 1024 * 1024;
			long size = inChannel.size();
			long pos = 0;
			while (pos < size) {
				pos += inChannel.transferTo(pos, maxCount, outChannel);
			}
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}

	/**
	 * 자신을 포함하지 않은 모든 하위 파일들을(디렉토리 포함) 다른 디렉토리로 복사한다.<br>
	 * 복사할 파일 객체가 파일인 경우는 아무일도 일어나지 않으며, 복사 대상 디렉토리가 존재하지 않는다면 새로 생성한다.
	 * 
	 * @param in
	 *            복사할 File 객체(디렉토리를 나타내는 File 객체)
	 * @param out
	 *            복사대상 File 객체(디렉토리를 나타내는 File 객체)
	 * @throws IOException
	 */
	public static final void copyDir(File in, File out) throws IOException {
		if (in.isDirectory() == false)
			return;

		out.mkdirs();

		File[] files = in.listFiles();
		for (int i = 0; i < files.length; i++) {
			File inFile = files[i];
			File outFile = new File(out.getAbsolutePath(), inFile.getName());

			if (inFile.isDirectory())
				copyDir(inFile, outFile);
			else
				copyFile(inFile, outFile);
		}
	}
}
