package com.masai.DTO;

public interface Stock {
	 // getters and setters
    public String getName();
    public void setName(String name);

    public int getId();
    public int getQuantity();
    public void setQuantity(int quantity);

    public double getPrice();
    public void setPrice(double price);

    public int getIsDeleted();
    public void display();
	public void setId(int stockId);
	public void setIsDeleted(int isDeleted);

}
