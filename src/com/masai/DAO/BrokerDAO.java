package com.masai.DAO;

import java.sql.SQLException;
import java.util.List;

import com.masai.DTO.Customer;
import com.masai.DTO.Stock;
import com.masai.EXCEPTION.NoRecordFoundException;
import com.masai.EXCEPTION.SomethingWentWrongException;

public interface BrokerDAO {
	public List<Customer> viewAllCustomers() throws SQLException, NoRecordFoundException;
	public void addnewStock(String name, int quantity, double price) throws SomethingWentWrongException;
	public List<Stock> viewAllStocks() throws NoRecordFoundException;
	
	public boolean deleteCustomer(int id) throws SomethingWentWrongException;
	public boolean deleteStock(int id) throws SomethingWentWrongException;
	public int[] viewStockReport(String stockName) throws SomethingWentWrongException;
}
