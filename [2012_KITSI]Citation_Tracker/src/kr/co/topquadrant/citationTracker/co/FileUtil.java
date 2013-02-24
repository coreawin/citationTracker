package kr.co.topquadrant.citationTracker.co;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * File�� ���� ��ƿ��Ƽ.
 * 
 * @author user
 */
public class FileUtil {
	/**
	 * �־��� path�� ������ ���丮�� �����Ѵ�.
	 * 
	 * @param path
	 *            ������ ���丮 �н�
	 * @return �־��� path�� ������ ���丮 ���� ��ü�� �����ؼ� �����Ѵ�.
	 */
	public static final File mkdirs(String path) {
		File dirs = new File(path);
		dirs.mkdirs();
		return dirs;
	}

	/**
	 * �־��� ���� ��ü�� ���丮�� ��쿡 ������ ���丮�� ������ ���ϸ� �����Ѵ�.
	 * 
	 * @param file
	 *            File ��ü(���丮�� ��Ÿ���� File ��ü)
	 * @return �־��� ���� ��ü�� ���丮�� ��쿡 ������ ���丮�� ������ ���ϸ� �����Ѵ�. �������� �ʰų� ���丮�� �ƴ�
	 *         ��쿡�� �ƹ��ϵ� �Ͼ�� �ʴ´�.
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
	 * �־��� file ��ü�� �����Ѵ�.
	 * 
	 * @param file
	 *            ������ ���� ��ü
	 * @return �־��� file�� �����Ѵ�. ���丮�� ��쿡�� �ڽ��� �����Ͽ� ��� ���� ���ϵ��� �����Ѵ�.
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
	 * ������ �ٸ� ���Ϸ� �����Ѵ�.<br>
	 * �־��� ���� ��ü�� ���丮�� ��쿡�� �ƹ��ϵ� �Ͼ�� �ʴ´�.
	 * 
	 * @param in
	 *            ������ File ��ü(������ ��Ÿ���� File ��ü)
	 * @param out
	 *            ������ File ��ü(������ ��Ÿ���� File ��ü)
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

			// �پ��� OS�� ����� �ִ� ���� ����Ʈ ũ��
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
	 * �ڽ��� �������� ���� ��� ���� ���ϵ���(���丮 ����) �ٸ� ���丮�� �����Ѵ�.<br>
	 * ������ ���� ��ü�� ������ ���� �ƹ��ϵ� �Ͼ�� ������, ���� ��� ���丮�� �������� �ʴ´ٸ� ���� �����Ѵ�.
	 * 
	 * @param in
	 *            ������ File ��ü(���丮�� ��Ÿ���� File ��ü)
	 * @param out
	 *            ������ File ��ü(���丮�� ��Ÿ���� File ��ü)
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
