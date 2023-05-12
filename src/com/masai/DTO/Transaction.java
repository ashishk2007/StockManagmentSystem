package com.masai.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Transaction {
	public int getId();

    public void setId(int id);

    public int getCustomerId();

    public void setCustomerId(int customerId);
    public int getStockId();

    public void setStockId(int stockId);

    public String getType();
    public void setType(String type);
    public int getQuantity();

    public void setQuantity(int quantity);

    public double getPrice();

    public void setPrice(double price);
    public double getTotalAmount();

    public void setTotalAmount(double totalAmount);

    public LocalDate getDate();

    public void setDate(LocalDate date);
}
