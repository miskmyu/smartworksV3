
package ifez.framework.session;

import java.io.Serializable;

/**
 * 세션 VO 클래스
 *
 * @author : jihhong
 * @version $Revision$  $Date$
 * @see
 */

public class UserProgramAuthInfoVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String programId;    // 프로그램 Id
    private String programUrl;   // 프로그램 Url
    
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getProgramUrl() {
		return programUrl;
	}
	public void setProgramUrl(String programUrl) {
		this.programUrl = programUrl;
	}


}