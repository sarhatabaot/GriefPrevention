package me.ryanhamshire.griefprevention.spam;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SpamDetector {
	//last chat message shown and its timestamp, regardless of who sent it
	private String lastChatMessage = "";
	private long lastChatMessageTimestamp = 0;

	//number of identical chat messages in a row
	private int duplicateMessageCount = 0;

	//data for individual chatters
	ConcurrentHashMap<UUID, ChatterData> dataStore = new ConcurrentHashMap<>();

	private ChatterData getChatterData(UUID chatterID) {
		ChatterData data = this.dataStore.get(chatterID);
		if (data == null) {
			data = new ChatterData();
			this.dataStore.put(chatterID, data);
		}

		return data;
	}

	public SpamAnalysisResult analyzeMessage(UUID chatterID, String message, long timestamp) {
		SpamAnalysisResult result = new SpamAnalysisResult();
		result.setFinalMessage(message);

		//remedy any CAPS SPAM, exception for very short messages which could be emoticons like =D or XD
		if (message.length() > 4 && this.stringsAreSimilar(message.toUpperCase(), message)) {
			message = message.toLowerCase();
            result.setFinalMessage(message);
		}

		boolean spam = false;
		ChatterData chatterData = this.getChatterData(chatterID);

		//mute if total volume of text from this player is too high
		if (message.length() > 50 && chatterData.getTotalRecentLength(timestamp) > 200) {
			spam = true;
			result.setMuteReason("too much chat sent in 10 seconds");
			chatterData.setSpamLevel(chatterData.getSpamLevel() + 1);
		}

		//always mute an exact match to the last chat message
		if (result.getFinalMessage().equals(this.lastChatMessage) && timestamp - this.lastChatMessageTimestamp < 2000) {
			chatterData.setSpamLevel(chatterData.getSpamLevel() + ++this.duplicateMessageCount);
			spam = true;
			result.setMuteReason("repeat message");
		} else {
			this.lastChatMessage = message;
			this.lastChatMessageTimestamp = timestamp;
			this.duplicateMessageCount = 0;
		}

		//check message content and timing
		long millisecondsSinceLastMessage = timestamp - chatterData.getLastMessageTimestamp();

		//if the message came too close to the last one
		if (millisecondsSinceLastMessage < 1500) {
			//increment the spam counter
			chatterData.setSpamLevel(chatterData.getSpamLevel() + 1);
			spam = true;
		}

		//if it's exactly the same as the last message from the same player and within 30 seconds
		if (result.getMuteReason() == null && millisecondsSinceLastMessage < 30000 && result.getFinalMessage().equalsIgnoreCase(chatterData.getLastMessage())) {
			chatterData.setSpamLevel(chatterData.getSpamLevel() + 1);
			spam = true;
			result.setMuteReason("repeat message");
		}

		//if it's very similar to the last message from the same player and within 10 seconds of that message
		if (result.getMuteReason() == null && millisecondsSinceLastMessage < 10000 && this.stringsAreSimilar(message.toLowerCase(), chatterData.getLastMessage().toLowerCase())) {
			chatterData.setSpamLevel(chatterData.getSpamLevel() + 1);
			spam = true;
			if (chatterData.getSpamLevel() > 2) {
				result.setMuteReason("similar message");
			}
		}

		//if the message was mostly non-alpha-numerics or doesn't include much whitespace, consider it a spam (probably ansi art or random text gibberish)
		if (result.getMuteReason() == null && message.length() > 5) {
			int symbolsCount = 0;
			int whitespaceCount = 0;
			for (int i = 0; i < message.length(); i++) {
				char character = message.charAt(i);
				if (!(Character.isLetterOrDigit(character))) {
					symbolsCount++;
				}

				if (Character.isWhitespace(character)) {
					whitespaceCount++;
				}
			}

			if (symbolsCount > message.length() / 2 || (message.length() > 15 && whitespaceCount < message.length() / 10)) {
				spam = true;
				if (chatterData.getSpamLevel() > 0) result.setMuteReason("gibberish");
				chatterData.setSpamLevel(chatterData.getSpamLevel() + 1);
			}
		}

		//very short messages close together are spam
		if (result.getMuteReason() == null && message.length() < 5 && millisecondsSinceLastMessage < 3000) {
			spam = true;
			chatterData.setSpamLevel(chatterData.getSpamLevel() + 1);
		}

		//if the message was determined to be a spam, consider taking action
		if (spam) {
			//anything above level 8 for a player which has received a warning...  kick or if enabled, ban
			if (chatterData.getSpamLevel() > 8 && chatterData.isSpamWarned()) {
				result.setShouldBanChatter(true);
			} else if (chatterData.getSpamLevel() >= 4) {
				if (!chatterData.isSpamWarned()) {
					chatterData.setSpamWarned(true);
					result.setShouldWarnChatter(true);
				}

				if (result.getMuteReason() == null) {
					result.setMuteReason("too-frequent text");
				}
			}
		}

		//otherwise if not a spam, reduce the spam level for this player
		else {
			chatterData.setSpamLevel(0);
			chatterData.setSpamWarned(false);
		}

		chatterData.addMessage(message, timestamp);

		return result;
	}

	//if two strings are 75% identical, they're too close to follow each other in the chat
	private boolean stringsAreSimilar(String message, String lastMessage) {
		//ignore differences in only punctuation and whitespace
		message = message.replaceAll("[^\\p{Alpha}]", "");
		lastMessage = lastMessage.replaceAll("[^\\p{Alpha}]", "");

		//determine which is shorter
		String shorterString, longerString;
		if (lastMessage.length() < message.length()) {
			shorterString = lastMessage;
			longerString = message;
		} else {
			shorterString = message;
			longerString = lastMessage;
		}

		if (shorterString.length() <= 5) return shorterString.equals(longerString);

		//set similarity tolerance
		int maxIdenticalCharacters = longerString.length() - longerString.length() / 4;

		//trivial check on length
		if (shorterString.length() < maxIdenticalCharacters) return false;

		//compare forward
		int identicalCount = 0;
		int i;
		for (i = 0; i < shorterString.length(); i++) {
			if (shorterString.charAt(i) == longerString.charAt(i)) identicalCount++;
			if (identicalCount > maxIdenticalCharacters) return true;
		}

		//compare backward
		int j;
		for (j = 0; j < shorterString.length() - i; j++) {
			if (shorterString.charAt(shorterString.length() - j - 1) == longerString.charAt(longerString.length() - j - 1))
				identicalCount++;
			if (identicalCount > maxIdenticalCharacters) return true;
		}

		return false;
	}
}

