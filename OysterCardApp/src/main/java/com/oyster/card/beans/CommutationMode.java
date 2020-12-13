package com.oyster.card.beans;

/**
 * @author Shahnawaz
 */
public enum CommutationMode {	
	/**
	 * 
	 */
	BUS, TUBE;	
	/**
	* @param mode mode
	* @return CommutationMode
	*/
	public static CommutationMode getValueOf(String mode){		
		for(CommutationMode val: CommutationMode.values()) {
		if(mode != null && val.name().equalsIgnoreCase(mode)){
			return val;
		}
		}
		return null;
	}

}
