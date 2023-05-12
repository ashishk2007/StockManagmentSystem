package com.masai.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionImpl implements Transaction {
    private int id;
    private int customerId;
    private int stockId;
    private String type;
    private int quantity;
    private double price;
    private double totalAmount;
    private LocalDate date;

    public TransactionImpl( int customerId, int stockId, LocalDate date,String type, int quantity, double price, double totalAmount ) {
        this.customerId = customerId;
        this.stockId = stockId;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
        this.date = date;
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
    public int getCustomerId() {
        return customerId;
    }
    @Override
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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
    public String getType() {
        return type;
    }
    @Override
    public void setType(String type) {
        this.type = type;
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
    public double getPrice() {
        return price;
    }
    @Override
    public void setPrice(double price) {
        this.price = price;
    }
    @Override
    public double getTotalAmount() {
        return totalAmount;
    }
    @Override
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    @Override
    public LocalDate getDate() {
        return date;
    }
    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", stockId=" + stockId +
                ", type='" + type + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalAmount=" + totalAmount +
                ", date=" + date +
                '}';
    }
}

