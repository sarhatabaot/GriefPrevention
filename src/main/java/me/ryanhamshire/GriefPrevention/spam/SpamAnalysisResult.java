package me.ryanhamshire.GriefPrevention.spam;

public class SpamAnalysisResult {
	private String finalMessage;
	private boolean shouldWarnChatter = false;
	private boolean shouldBanChatter = false;
	private String muteReason;

	public String getFinalMessage() {
		return finalMessage;
	}

	public void setFinalMessage(final String finalMessage) {
		this.finalMessage = finalMessage;
	}

	public boolean isShouldWarnChatter() {
		return shouldWarnChatter;
	}

	public void setShouldWarnChatter(final boolean shouldWarnChatter) {
		this.shouldWarnChatter = shouldWarnChatter;
	}

	public boolean isShouldBanChatter() {
		return shouldBanChatter;
	}

	public void setShouldBanChatter(final boolean shouldBanChatter) {
		this.shouldBanChatter = shouldBanChatter;
	}

	public String getMuteReason() {
		return muteReason;
	}

	public void setMuteReason(final String muteReason) {
		this.muteReason = muteReason;
	}
}
