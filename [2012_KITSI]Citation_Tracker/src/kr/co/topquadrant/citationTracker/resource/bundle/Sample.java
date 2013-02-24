/**
 * 
 */
package kr.co.topquadrant.citationTracker.resource.bundle;

/**
 * 문자열 포맷은
 * http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Formatter.html#syntax
 * 참고 할것 <br>
 * 
 * @author coreawin
 * @sinse 2012. 10. 5.
 * @version 1.0
 * @history 2012. 10. 5. : 최초 작성 <br>
 * 
 */
public class Sample {
	
	public Sample(){
		ToolResource resource = SystemResourceBundle.getInstance().getResourceBundle();
		String a = resource.getMessage("test");
		String b = resource.getMessage("ttl");
		
		System.out.println(String.format(a, "입력값"));
		System.out.println(String.format(b, "ab.xlsx"));
	}
	
	public static void main(String[] args){
		new Sample();
	}

}
