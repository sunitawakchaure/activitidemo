package org.nanquan.activiti.examples.bus;

public class BusEvent implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8782442736622388350L;

	private String type;
	
	private Integer level;

	public String getType() {
		return type;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "BusEvent [type=" + type + ", level=" + level + "]";
	}
	
}
