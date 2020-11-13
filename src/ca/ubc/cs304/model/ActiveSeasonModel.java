package ca.ubc.cs304.model;
import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single program
 */
public class ActiveSeasonModel {
    private final String type;
    private final boolean isOutdoor;
    private final char activeSeason
	
	public ActiveSeasonModel(boolean isOutdoor, String type, char activeSeason) {
        this.type = type;
        this.isOutdoor = isOutdoor;
        this.activeSeason = activeSeason;
        
	}

	public String getIsOutdoor() {
		return this.isOutdoor;
	}

	public String getType() {
		return this.type;
	}

    public String getActiveSeason() {
		return this.activeSeason;
    }

    
