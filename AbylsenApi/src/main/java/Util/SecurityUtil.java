package Util;

public class SecurityUtil {
	public static boolean isTokenValid(String value) {
		return true;
	}
	
	public static String generateToken(String email, String pwd) {
		return "12345";
	}
	
	public static boolean isPasswordValid(String registered, String request) {
		return registered.equals(request);
	}
	
	public static boolean isApikeyValid(String apiKey) {
		return true;
	}
}
