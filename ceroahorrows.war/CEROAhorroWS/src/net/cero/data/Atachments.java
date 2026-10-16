package net.cero.data;


public class Atachments {

	byte[] theAttachmentBytes;
	String theFilename;
	String theContentType;
	
	public byte[] getTheAttachmentBytes() {
		return theAttachmentBytes;
	}
	public void setTheAttachmentBytes(byte[] theAttachmentBytes) {
		this.theAttachmentBytes = theAttachmentBytes;
	}
	public String getTheFilename() {
		return theFilename;
	}
	public void setTheFilename(String theFilename) {
		this.theFilename = theFilename;
	}
	public String getTheContentType() {
		return theContentType;
	}
	public void setTheContentType(String theContentType) {
		this.theContentType = theContentType;
	}	
}
