package com.naturegecko.application.documents.sub;

public class StatusEnum {

	public enum COUNTRY_STATUS {
		PENDING("Pending"), // Is waiting to be checked
		ACTIVE("Active"), // Is active and can be player.
		ELIMINATED("Eliminated"), // Is deleted from the galaxy map by other empires.
		BANNED("Banned"), // Is banned from the game.
		REJECTED("Rejected"); // Does not meet criteria.

		private final String status;

		private COUNTRY_STATUS(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}
	}

	public enum TECH_STATUS {
		PENDING, ACTIVE, LOST, REJECTED, BANNED;
	}

	public enum ACTION_STATUS {
		PENDING, PASS, REJECTED, CANCELLED;
	}
}
