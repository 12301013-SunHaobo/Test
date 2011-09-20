package modules.at.pattern.bb;

public class BBandMatch {

	//base on approximation, so use this range to define equals
	double range = 0.04;
	
	//trend of current BBand
	enum Trend {
		Up, Flat, Down
	}
	
	enum PointPosition {
		AboveUpper_Up, AboveUpper_Flat, AboveUpper_Down, 
		AtUpper_Up, AtUpper_Flat, AtUpper_Down, 
		BelowUpper_Up, BelowUpper_Flat, BelowUpper_Down,

		AboveMiddle_Up, AboveMiddle_Flat, AboveMiddle_Down, 
		AtMiddle_Up, AtMiddle_Flat, AtMiddle_Down, 
		BelowMiddle_Up, BelowMiddle_Flat, BelowMiddle_Down,

		AboveLower_Up, AboveLower_Flat, AboveLower_Down, 
		AtLower_Up, AtLower_Flat, AtLower_Down, 
		BelowLower_Up, BelowLower_Flat, BelowLower_Down,

		NA
	}
	
	
	
	
	
	
}
