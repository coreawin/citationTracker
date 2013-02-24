/**
 * 
 */
package kr.co.topquadrant.citationTracker.resource.bundle;

/**
 * ���ڿ� ������
 * http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Formatter.html#syntax
 * ���� �Ұ� <br>
 * 
 * @author coreawin
 * @sinse 2012. 10. 5.
 * @version 1.0
 * @history 2012. 10. 5. : ���� �ۼ� <br>
 * 
 */
public class Sample {
	
	public Sample(){
		ToolResource resource = SystemResourceBundle.getInstance().getResourceBundle();
		String a = resource.getMessage("test");
		String b = resource.getMessage("ttl");
		
		System.out.println(String.format(a, "�Է°�"));
		System.out.println(String.format(b, "ab.xlsx"));
	}
	
	public static void main(String[] args){
		new Sample();
	}

}
