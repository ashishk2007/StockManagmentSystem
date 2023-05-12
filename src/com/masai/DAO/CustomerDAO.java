package com.masai.DAO;

import java.util.List;

import com.masai.DTO.Customer;
import com.masai.DTO.StockData;
import com.masai.DTO.Transaction;
import com.masai.EXCEPTION.InsufficientBalanceException;
import com.masai.EXCEPTION.InsufficientSharesException;
import com.masai.EXCEPTION.InvalidCredentialsException;
import com.masai.EXCEPTION.NoRecordFoundException;
import com.masai.EXCEPTION.SomethingWentWrongException;

public interface CustomerDAO<Throws> {
	public  void addCustomer(Customer customer) throws SomethingWentWrongException;
	
	public Customer authenticateCustomer(String username, String password) throws InvalidCredentialsException;
	public boolean buyStocks(int customerId, int stockId, int quantity) throws InsufficientBalanceException, NoRecordFoundException;
	public boolean sellStock(int customerId, int stockId, int quantity) throws InsufficientSharesException, NoRecordFoundException, ClassNotFoundException;
	public List<StockData> getAllStockData(int id) throws NoRecordFoundException;
	
	public List<Transaction> viewTransactionHistory(int id) throws NoRecordFoundException;

	public void addMoney(int id, double amount) throws SomethingWentWrongException;
	public void withdrawMoney(int id, double amount) throws SomethingWentWrongException, InsufficientBalanceException;
}
