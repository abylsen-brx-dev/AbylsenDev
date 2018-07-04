package util;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.hibernate.Session;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import model.Employee;

public class SecurityUtil {
	public static TokenResponse isTokenValid(Session session, String value, String secretKey) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(value);

			String email = (String) claims.getBody().get("email"); 
			String pwd = (String) claims.getBody().get("pwd");
			String rule = (String) claims.getBody().get("rule");
			
			Employee e = Employee.getEmployeeByEmail(session, email);
			if(e == null)
				return new TokenResponse(null, null, null, null, false);
			
			return new TokenResponse(email, pwd, rule, generateToken(email, pwd, secretKey, rule), e.getPassword().equals(pwd));
		} catch (Exception e) {
			return new TokenResponse(null, null, null, null, false);
		}
	}
	
	public static String generateToken(String email, String pwd, String secretKey, String rule) throws UnsupportedEncodingException {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR_OF_DAY, 20);
		String jwt = Jwts.builder()
				.setExpiration(now.getTime())
				.claim("email", email).claim("pwd", pwd).claim("rule", rule)
				.signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8")).compact();
		return jwt;
	}
	
	public static boolean isPasswordValid(String registered, String request) {
		return registered.equals(request);
	}

	public static boolean isApikeyValid(String apiKey) {
		return true;
	}
}

