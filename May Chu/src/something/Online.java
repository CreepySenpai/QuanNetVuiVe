package something;

public class Online {
	private String computerName;
	
	private String userName;
	
	private int tongSoTienDaNap;
	
	public Online(String computerName, String userName, int tongSoTienDaNap) {
		this.computerName = computerName;
		this.userName = userName;
		this.tongSoTienDaNap = tongSoTienDaNap;
	}

	public int getTongSoTienDaNap() {
		return tongSoTienDaNap;
	}

	public void setTongSoTienDaNap(int tongSoTienDaNap) {
		this.tongSoTienDaNap = tongSoTienDaNap;
	}

	public String getComputerName() {
		return computerName;
	}

	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
