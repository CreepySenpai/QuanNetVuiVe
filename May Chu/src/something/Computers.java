package something;

public class Computers {

	private String computerName;
	
	private String computerType;
	
	private String tinhTrang;
	
	private int moneyPerHour;
	
	public Computers() {};
	
	public Computers(String computerName, String computerType,int moneyPerHour) {
		this.computerName = computerName;
		this.computerType = computerType;
		this.moneyPerHour = moneyPerHour;
	}
	
	public Computers(String computerName, String tinhTrang, String computerType, int moneyPerHour) {
		this.computerName = computerName;
		this.computerType = computerType;
		this.tinhTrang = tinhTrang;
		this.moneyPerHour = moneyPerHour;
	}

	public String getTinhTrang() {
		return tinhTrang;
	}

	public void setTinhTrang(String tinhTrang) {
		this.tinhTrang = tinhTrang;
	}

	public String getComputerName() {
		return computerName;
	}

	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	public String getComputerType() {
		return computerType;
	}

	public void setComputerType(String computerType) {
		this.computerType = computerType;
	}

	public int getMoneyPerHour() {
		return moneyPerHour;
	}

	public void setMoneyPerHour(int moneyPerHour) {
		this.moneyPerHour = moneyPerHour;
	}
}
