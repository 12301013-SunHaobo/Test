package modules.at.model.visual;

/**
 *  one dot in chart 
 */
public class VXY {
    private long x;// time as long
    private double y;

    public VXY(long x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
