/**
 * 
 */
package kr.co.topquadrant.citationTracker.resource.bundle;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

/**
 * @author coreawin
 * @sinse 2012. 10. 5. 
 * @version 1.0
 * @history 2012. 10. 5. : 최초 작성 <br>
 *
 */
public class ToolResource {
	ResourceBundle rb = null;

	ToolResource(ResourceBundle _rb) {
		this.rb = _rb;
	}

	public String getMessage(String key) {
		if (key == null || rb == null)
			return null;
		String value = null;
		try {
			if (rb.getObject(key) == null) {
				return null;
			}
			value = "[" + key +"]";
			value = new String(rb.getString(key).getBytes("8859_1"), "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
		}
		return value;
	}
}
