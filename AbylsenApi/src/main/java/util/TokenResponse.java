package util;

public class TokenResponse {
	public String email;
	public String pwd;
	public String rule;
	public String newToken;
	public boolean isValid;
	
	public TokenResponse() {
		
	}
	
	public TokenResponse(String email, String pwd, String rule, String newToken, boolean isValid) {
		this.email = email;
		this.pwd = pwd;
		this.rule = rule;
		this.newToken = newToken;
		this.isValid = isValid;
	}
}
