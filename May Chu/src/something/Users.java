package something;

public class Users {

	private String userName;
	
	private String password;
	
	private int money;
	
	public Users() {}
	
	public Users(String userName, int money) {
		this.userName = userName;
		this.money = money;
	}
	
	public Users(String userName, String password, int money) {
		this.userName = userName;
		this.password = password;
		this.money = money;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
