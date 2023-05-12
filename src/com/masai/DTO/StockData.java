package com.masai.DTO;

public interface StockData {
	    public int getStockId();
		public void setStockId(int stockId);
		public String getStockName();
		public void setStockName(String stockName);
		public double getPrice() ;
		public void setPrice(double price);
		public int getQuantity() ;
		public void setQuantity(int quantity) ;
		public int getSharesOwned();
		public void setSharesOwned(int sharesOwned);

}
