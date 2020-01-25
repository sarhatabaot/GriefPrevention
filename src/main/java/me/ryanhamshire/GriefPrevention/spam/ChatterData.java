package me.ryanhamshire.GriefPrevention.spam;

import java.util.concurrent.ConcurrentLinkedQueue;


public class ChatterData {
	private String lastMessage = "";                 //the player's last chat message, or slash command complete with parameters
	private long lastMessageTimestamp;               //last time the player sent a chat message or used a monitored slash command
	private int spamLevel = 0;                       //number of consecutive "spams"
	private boolean spamWarned = false;              //whether the player has received a warning recently

	//all recent message lengths and their total
	private ConcurrentLinkedQueue<LengthTimestampPair> recentMessageLengths = new ConcurrentLinkedQueue<>();
	private int recentTotalLength = 0;

	public void addMessage(String message, long timestamp) {
		int length = message.length();
		this.recentMessageLengths.add(new LengthTimestampPair(length, timestamp));
		this.recentTotalLength += length;

		this.setLastMessage(message);
		this.setLastMessageTimestamp(timestamp);
	}

	public int getTotalRecentLength(long timestamp) {
		LengthTimestampPair oldestPair = this.recentMessageLengths.peek();
		while (oldestPair != null && timestamp - oldestPair.getTimestamp() > 10000) {
			this.recentMessageLengths.poll();
			this.recentTotalLength -= oldestPair.getLength();
			oldestPair = this.recentMessageLengths.peek();
		}

		return this.recentTotalLength;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public long getLastMessageTimestamp() {
		return lastMessageTimestamp;
	}

	public void setLastMessageTimestamp(long lastMessageTimestamp) {
		this.lastMessageTimestamp = lastMessageTimestamp;
	}

	public int getSpamLevel() {
		return spamLevel;
	}

	public void setSpamLevel(int spamLevel) {
		this.spamLevel = spamLevel;
	}

	public boolean isSpamWarned() {
		return spamWarned;
	}

	public void setSpamWarned(boolean spamWarned) {
		this.spamWarned = spamWarned;
	}
}
