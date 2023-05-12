package com.masai.DTO;

public class StockImpl implements Stock{
	private int id;
    private String name;
    private int quantity;
    private double price;
    private int isDeleted;


	public StockImpl(String name, int quantity, double price) {
		// TODO Auto-generated constructor stub
		 	this.name = name;
	        this.quantity = quantity;
	        this.price = price;
	}


	@Override
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int getId() {
		return id;
	}

    public String getName() { return name; }
    @Override
    public void setName(String name) { this.name = name; }
    @Override
    public int getQuantity() { return quantity; }
    @Override
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    @Override
    public double getPrice() { return price; }
    @Override
    public void setPrice(double price) { this.price = price; }
    @Override
    public int getIsDeleted() { return isDeleted; }
    @Override
    public void setIsDeleted(int isDeleted) { this.isDeleted = isDeleted; }

    // other methods
    @Override
    public void display() {
        System.out.println("Stock Name: " + name);
        System.out.println("Stock Quantity: " + quantity);
        System.out.println("Stock Price: " + price);
        System.out.println("Stock isDeleted: " + isDeleted);
    }
}
