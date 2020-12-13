package com.oyster.card.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Shahnawaz
 */
public class Station implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8452078452137914031L;
	/**
	 * 
	 */
	private String name;	
	/**
	 * 
	 */
	private Set<Zone> zones = new HashSet<>();
	
	/**
	 * @param name name
	 * @param zones zones
	 */
	public Station(String station_name, Set<Zone> st_zones) {
		this.name = station_name;
		this.zones = st_zones;
	}
	
	/**
	 * @return Set<Zone>
	 */
	public Set<Zone> getZones() {
		return zones;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 *
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Station other = (Station) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Station [name=");
		builder.append(name);
		builder.append(", zones=");
		builder.append(zones);
		builder.append("]");
		return builder.toString();
	}
}
