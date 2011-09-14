package others.e.model;

public class Word {

	private int inputIndex;
	private String name;
	
	private Iciba iciba;
	private Wwo wwo;
	private Mw mw;
	private Xr xr;
	
	
	
	
	public Word(){
		super();
		this.iciba = new Iciba();
		this.wwo = new Wwo();
		this.mw = new Mw();
		this.xr = new Xr();
	}
			
	public Word(int inputIndex, String name) {
		super();
		this.inputIndex = inputIndex;
		this.name = name;
		this.iciba = new Iciba();
		this.wwo = new Wwo();
		this.mw = new Mw();
		this.xr = new Xr();
	}
	public int getInputIndex() {
		return inputIndex;
	}
	public void setInputIndex(int inputIndex) {
		this.inputIndex = inputIndex;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Iciba getIciba() {
		return iciba;
	}
	public void setIciba(Iciba iciba) {
		this.iciba = iciba;
	}
	public Wwo getWwo() {
		return wwo;
	}
	public void setWwo(Wwo wwo) {
		this.wwo = wwo;
	}
	public Mw getMw() {
		return mw;
	}
	public void setMw(Mw mw) {
		this.mw = mw;
	}
	public Xr getXr() {
		return xr;
	}
	public void setXr(Xr xr) {
		this.xr = xr;
	}
	
	
	
	
	
	
	
	
}
