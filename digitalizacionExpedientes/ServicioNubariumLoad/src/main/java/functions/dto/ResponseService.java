package functions.dto;

public class ResponseService {

	private int code;
	private String message;
	private String data;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMenssage() {
		return message;
	}
	public void setMenssage(String message) {
		this.message = message;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResponseService{" +
				"code=" + code +
				", message='" + message + '\'' +
				", data='" + data + '\'' +
				'}';
	}
}
