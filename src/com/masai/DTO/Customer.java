package com.masai.DTO;

public interface Customer {
	public int getId();
	public void setId(int id);
	public String getFirstName();
	public void setFirstName(String firstName);

	public String getLastName();

	public void setLastName(String lastName);
	public String getUsername();


	public void setUsername(String username);

	public String getPhone();

	public void setPhone(String phone);

	public String getAddress();


	public void setAddress(String address);

	public String getEmail();


	public void setEmail(String email);

	public String getPassword();


	public void setPassword(String password);

	public double getWalletBalance();

	public void setWalletBalance(double walletBalance);

	public int getIsActive();


	public void setIsActive(int isActive);
}
