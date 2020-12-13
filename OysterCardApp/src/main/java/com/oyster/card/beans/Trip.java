package com.oyster.card.beans;

/**
 * @author Shahnawaz
 */
public class Trip {
	/**
	 * 
	 */
	private Station from;
	/**
	 * 
	 */
    private Station to;
    /**
     * 
     */
    private CommutationMode transporationMode;
    /**
     * 
     */
	private String tripDetails;
	
	/**
	 * @param i_from i_from
	 * @param i_to i_to
	 * @param i_transporationMode i_transporationMode
	 * @param i_tripDetails i_tripDetails
	 */
	public Trip(Station i_from, Station i_to, 
		CommutationMode i_transporationMode, String i_tripDetails) {
		this.from = i_from;
		this.to = i_to;
		this.transporationMode = i_transporationMode;
		if(i_tripDetails == null){
			throw new IllegalArgumentException("Trip details can not be null");
		}
		this.tripDetails = i_tripDetails;
	}
	
	/**
	 * @return
	 */
	public Station getFrom() {
		return from;
	}

	/**
	 * @return
	 */
	public Station getTo() {
		return to;
	}

	/**
	 * @return
	 */
	public CommutationMode getTransporationMode() {
		return transporationMode;
	}

	/**
	 * @return
	 */
	public String getJourneyDetails() {
		return tripDetails;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Trip [from=");
		builder.append(from);
		builder.append(", to=");
		builder.append(to);
		builder.append(", transporationMode=");
		builder.append(transporationMode);
		builder.append(", tripDetails=");
		builder.append(tripDetails);
		builder.append("]");
		return builder.toString();
	}
}
