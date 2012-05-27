package demo;

public class Order implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2298576800634415932L;

	private String type;
	
	private Integer status;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Order [type=" + type + ", status=" + status + "]";
	}
	
}
