package others.e.model;


/**
 * xr manually keyed in
 * @author rong
 *
 */
public class Xr {

	//static members
	public static final String PHONE_XR = "R";

	
	
	//instance members
	private String sentences;
	
	//indicate if local has mp3
	private boolean localXrHasMp3 = true;
	
	
	
	public String getSentences() {
		return sentences;
	}


	public void setSentences(String sentences) {
		this.sentences = sentences;
	}
	
	public boolean isLocalXrHasMp3() {
		return localXrHasMp3;
	}

	public void setLocalXrHasMp3(boolean localXrHasMp3) {
		this.localXrHasMp3 = localXrHasMp3;
	}


	// for testing
	public static void main(String args[]){

	}
}
