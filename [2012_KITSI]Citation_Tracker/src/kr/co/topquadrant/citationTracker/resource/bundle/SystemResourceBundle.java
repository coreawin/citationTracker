package kr.co.topquadrant.citationTracker.resource.bundle;

import java.util.Locale;

/**
 * �ý��� ������ ������ �д´�.
 * <P>
 * 
 * @author neon
 * @since 2007. 11. 29
 */
public class SystemResourceBundle {

	private static SystemResourceBundle instance = new SystemResourceBundle();

	private ToolResource resourceBundle = null;

	/**
	 * �ý��� �޽��� �ν��Ͻ��� �����ϰ� �����Ѵ�.
	 */
	private SystemResourceBundle() {
		resourceBundle = new ToolResource(ResourceBundleFactory.create(ResourceBundleFactory.MESSAGE,
				Locale.KOREA.getLanguage()));
	}

	/**
	 * �ν��Ͻ��� ��´�.
	 * 
	 * @return
	 */
	public static SystemResourceBundle getInstance() {
		return instance;
	}

	/**
	 * �����ӿ�ũ ���� ���� <br>
	 * 
	 * @return
	 */
	public ToolResource getResourceBundle() {
		return resourceBundle;
	}

}
