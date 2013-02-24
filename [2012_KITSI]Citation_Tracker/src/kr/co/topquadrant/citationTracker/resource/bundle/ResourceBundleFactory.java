/**
 * 
 */
package kr.co.topquadrant.citationTracker.resource.bundle;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ��� �޽����� �����ϴ� Ŭ����.
 * 
 * @author coreawin
 * @sinse 2012. 10. 5.
 * @version 1.0
 * @history 2012. 10. 5. : ���� �ۼ� <br>
 * 
 */
public class ResourceBundleFactory {

	public final static String MESSAGE = "kr.co.topquadrant.citationTracker.resource.bundle.propterties.sysmessage";

	public static ResourceBundle create(String baseName) {
		return ResourceBundle.getBundle(baseName);
	}

	public static ResourceBundle create(String baseName, String locale) {
		return ResourceBundle.getBundle(baseName, new Locale(locale));
	}

}
