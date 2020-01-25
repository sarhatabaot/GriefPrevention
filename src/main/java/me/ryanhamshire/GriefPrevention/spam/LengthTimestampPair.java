package me.ryanhamshire.GriefPrevention.spam;

/**
 * @author sarhatabaot
 */
class LengthTimestampPair {
	private long timestamp;
	private int length;

	public LengthTimestampPair(int length, long timestamp) {
		this.setLength(length);
		this.setTimestamp(timestamp);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
