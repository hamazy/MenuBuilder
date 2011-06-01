package menubuilder;

public class StringUtil {

	static String stripDoubleQuote(String string) {
		
		int start = string.startsWith("\"") ? 1 : 0; 
		int end = string.endsWith("\"") ? string.length() - 1: string.length();
		return string.substring(start, end);
	}
}
