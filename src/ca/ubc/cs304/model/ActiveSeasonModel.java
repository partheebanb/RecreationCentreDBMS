package ca.ubc.cs304.model;
import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single program
 */
public class ActiveSeasonModel {
	private final String type;
	private final boolean isOutdoor;
	private final char activeSeason;

	public ActiveSeasonModel(String type, boolean isOutdoor, char activeSeason) {
		this.type = type;
		this.isOutdoor = isOutdoor;
		this.activeSeason = activeSeason;
	}

	public String getType() {
		return type;
	}

	public boolean isOutdoor() {
		return isOutdoor;
	}

	public char getActiveSeason() {
		return activeSeason;
	}
}
