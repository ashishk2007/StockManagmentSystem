package com.masai.DTO;

import java.util.Objects;

import com.masai.DAO.BrokerDAO;

public class CustomerImpl implements Customer {
	 	private int id;
	    private String firstName;
	    private String lastName;
	    private String username;
	    private String phone;
	    private String address;
	    private String email;
	    private String password;
	    private double walletBalance;
	    private int isActive;
		
		
		public CustomerImpl(String firstName, String lastName, String username, String phone, String address,
				String email, String password, double walletBalance, int isActive) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.username = username;
			this.phone = phone;
			this.address = address;
			this.email = email;
			this.password = password;
			this.walletBalance = walletBalance;
			this.isActive = isActive;
		}
		@Override
		public int getId() {
			return id;
		}
		@Override
		public void setId(int id) {
			this.id = id;
			
		}
		@Override
		public String getFirstName() {
			return firstName;
		}

		@Override
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		@Override
		public String getLastName() {
			return lastName;
		}

		@Override
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		@Override
		public String getUsername() {
			return username;
		}
		@Override
		public void setUsername(String username) {
			this.username = username;
		}

		@Override
		public String getPhone() {
			return phone;
		}

		@Override
		public void setPhone(String phone) {
			this.phone = phone;
		}

		@Override
		public String getAddress() {
			return address;
		}

		@Override
		public void setAddress(String address) {
			this.address = address;
		}

		@Override
		public String getEmail() {
			return email;
		}

		@Override
		public void setEmail(String email) {
			this.email = email;
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public void setPassword(String password) {
			this.password = password;
		}

		@Override
		public double getWalletBalance() {
			return walletBalance;
		}

		@Override
		public void setWalletBalance(double walletBalance) {
			this.walletBalance = walletBalance;
		}

		@Override
		public int getIsActive() {
			return isActive;
		}

		@Override
		public void setIsActive(int isActive) {
			this.isActive = isActive;
		}
		
		
		
	    
}
