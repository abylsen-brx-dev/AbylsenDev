package util;

public class TokenResponse {
	public String email;
	public String pwd;
	public String newToken;
	public boolean isValid;
	
	public TokenResponse() {
		
	}
	
	public TokenResponse(String email, String pwd, String newToken, boolean isValid) {
		this.email = email;
		this.pwd = pwd;
		this.newToken = newToken;
		this.isValid = isValid;
	}
}
