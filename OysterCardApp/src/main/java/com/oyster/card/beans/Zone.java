package com.oyster.card.beans;

/**
 * @author Shahnawaz
 */
public class Zone {
	/**
	 * 
	 */
	private int id;
	/**
	 * @param zoneId
	 */
	public Zone(int zoneId) {
		id = zoneId;
	}
	/**
	 * @return
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 *
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Zone other = (Zone) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Zone [id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
}
