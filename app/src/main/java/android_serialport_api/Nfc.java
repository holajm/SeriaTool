package android_serialport_api;

public class Nfc {
	private String frameHeader;
    private String CardType;
    private String CardNumber;
    private String checkStr;
    
	public String getFrameHeader() {
		return frameHeader;
	}
	public void setFrameHeader(String frameHeader) {
		this.frameHeader = frameHeader;
	}
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String cardType) {
		CardType = cardType;
	}
	public String getCardNumber() {
		return CardNumber;
	}
	public void setCardNumber(String cardNumber) {
		CardNumber = cardNumber;
	}
	public String getCheckStr() {
		return checkStr;
	}
	public void setCheckStr(String checkStr) {
		this.checkStr = checkStr;
	}

    

    
    
	
}
