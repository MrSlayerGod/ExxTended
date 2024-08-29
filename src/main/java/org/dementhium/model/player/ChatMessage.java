package org.dementhium.model.player;

public class ChatMessage {
	
	private short effects;
	private short numChars;
	private String chatText;
	
	public ChatMessage(int effects, int numChars, String chatText) {
		this.effects = (short) effects;
		this.numChars = (short) numChars;
		this.chatText = chatText;
	}
	
	public short getEffects() {
		return effects;
	}
	
	public short getNumChars() {
		return numChars;
	}
	
	public String getChatText() {
		return chatText;
	}

}
