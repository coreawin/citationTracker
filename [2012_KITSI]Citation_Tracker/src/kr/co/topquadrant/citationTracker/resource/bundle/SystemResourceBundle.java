package kr.co.topquadrant.citationTracker.resource.bundle;

import java.util.Locale;

/**
 * 시스템 메지시 정보를 읽는다.
 * <P>
 * 
 * @author neon
 * @since 2007. 11. 29
 */
public class SystemResourceBundle {

	private static SystemResourceBundle instance = new SystemResourceBundle();

	private ToolResource resourceBundle = null;

	/**
	 * 시스템 메시지 인스턴스를 설정하고 생성한다.
	 */
	private SystemResourceBundle() {
		resourceBundle = new ToolResource(ResourceBundleFactory.create(ResourceBundleFactory.MESSAGE,
				Locale.KOREA.getLanguage()));
	}

	/**
	 * 인스턴스를 얻는다.
	 * 
	 * @return
	 */
	public static SystemResourceBundle getInstance() {
		return instance;
	}

	/**
	 * 프레임워크 관련 번들 <br>
	 * 
	 * @return
	 */
	public ToolResource getResourceBundle() {
		return resourceBundle;
	}

}
