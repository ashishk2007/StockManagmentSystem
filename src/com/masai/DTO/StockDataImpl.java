package com.masai.DTO;

public class StockDataImpl implements StockData {
	private int stockId;
    private String stockName;
    private double price;
    private int quantity;
    private int sharesOwned;

     public StockDataImpl( String stockName, double price, int quantity, int sharesOwned) {
        this.stockName = stockName;
        this.price = price;
        this.quantity = quantity;
        this.sharesOwned = sharesOwned;
    }
    
    @Override
    public int getStockId() {
		return stockId;
	}
    @Override
	public void setStockId(int stockId) {
		this.stockId = stockId;
	}
    @Override
	public String getStockName() {
		return stockName;
	}
    @Override
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
    @Override
	public double getPrice() {
		return price;
	}
    @Override
	public void setPrice(double price) {
		this.price = price;
	}
    @Override
	public int getQuantity() {
		return quantity;
	}
    @Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    @Override
	public int getSharesOwned() {
		return sharesOwned;
	}
    @Override
	public void setSharesOwned(int sharesOwned) {
		this.sharesOwned = sharesOwned;
	}

	@Override
    public String toString() {
        return "StockData{" +
                "stockId=" + stockId +
                ", stockName='" + stockName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", sharesOwned=" + sharesOwned +
                '}';
    }

}
