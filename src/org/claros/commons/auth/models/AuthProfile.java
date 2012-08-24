package org.claros.commons.auth.models;

/**
 * @author Umut Gokbayrak
 *
 */
public class AuthProfile {
    private String username;
    private String password;
    private String emailId;
    private boolean deleteAfterFetched=false;
    private String signature; 

    public AuthProfile() {
        super();
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean isDeleteAfterFetched() {
		return deleteAfterFetched;
	}

	public void setDeleteAfterFetched(boolean deleteAfterFetched) {
		this.deleteAfterFetched = deleteAfterFetched;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
