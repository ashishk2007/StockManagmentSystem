package com.masai.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.masai.DTO.Customer;
import com.masai.DTO.CustomerImpl;
import com.masai.DTO.Stock;
import com.masai.DTO.StockData;
import com.masai.DTO.StockDataImpl;
import com.masai.DTO.StockImpl;
import com.masai.DTO.Transaction;
import com.masai.DTO.TransactionImpl;
import com.masai.EXCEPTION.InsufficientBalanceException;
import com.masai.EXCEPTION.InsufficientSharesException;
import com.masai.EXCEPTION.InvalidCredentialsException;
import com.masai.EXCEPTION.NoRecordFoundException;
import com.masai.EXCEPTION.SomethingWentWrongException;
import com.mysql.cj.xdevapi.Statement;

public class CustomerDAOImpl implements CustomerDAO{
	static String stockName=null;
	@Override
	public  void addCustomer(Customer customer) throws SomethingWentWrongException {
	    String query = "INSERT INTO customer (first_name, last_name, username, password, address, mobile_number, email, wallet_balance, is_active) VALUES (?,?,?,?,?,?,?,?,?)";
	    Connection conn = null;
	    try{
	    	conn = DBUtils.getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setString(1, customer.getFirstName());
	        pstmt.setString(2, customer.getLastName());
	        pstmt.setString(3, customer.getUsername());
	        pstmt.setString(4, customer.getPassword());
	        pstmt.setString(5, customer.getAddress());
	        pstmt.setString(6, customer.getPhone());
	        pstmt.setString(7, customer.getEmail());
	        pstmt.setDouble(8, customer.getWalletBalance());
	        pstmt.setInt(9, 1);
	        pstmt.executeUpdate();

	    } catch (SQLException | ClassNotFoundException e) {
	        throw new SomethingWentWrongException("Could not able to add customer");
	    }finally {
	    	try {
	    		DBUtils.closeConnection(conn);
	    	}catch(SQLException e) {
	    		
	    	}
	    }
	}
	
	@Override
	public Customer authenticateCustomer(String username, String password) throws InvalidCredentialsException {
	    Connection connection = null;
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    try {
	        connection = DBUtils.getConnection();
	        String query = "SELECT * FROM customer WHERE username = ? AND password = ? AND is_active = 1";
	        statement = connection.prepareStatement(query);
	        statement.setString(1, username);
	        statement.setString(2, password);
	        resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	        	Customer customer = new  CustomerImpl(resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)
	        			, resultSet.getString(6),
	    				resultSet.getString(7), resultSet.getString(8), resultSet.getDouble(9)
	    				, resultSet.getInt(10));
	        	customer.setId(resultSet.getInt(1));
	            return customer;
	        } else {
	            throw new InvalidCredentialsException("Invalid username or password");
	        }
	    } catch (ClassNotFoundException | SQLException e) {
	        throw new InvalidCredentialsException("Invalid username or password");
	    } finally {
	        try {
	            DBUtils.closeConnection(connection);
	            if (statement != null) statement.close();
	            if (resultSet != null) resultSet.close();
	        } catch (SQLException ex) {
	            
	        }
	    }
	}
	
	public boolean buyStocks(int customerId, int stockId, int quantity) throws InsufficientBalanceException, NoRecordFoundException {
	    Connection connection = null;
	    boolean isSuccessful = false;
	    try {
	        connection = DBUtils.getConnection();
	       connection.setAutoCommit(false); // start transaction

	        // fetch customer data
	        double walletBalance = checkWalletBalance(customerId);
	        double stockPrice = fetchStockPrice(stockId);
	        double totalCost = stockPrice * quantity;

	        // check if customer has sufficient balance
	        if (walletBalance < totalCost) {
	            throw new InsufficientBalanceException("Customer with ID " + customerId + " has insufficient balance.");
	        }

	        // update customer wallet balance
	        String updateCustomerQuery = "UPDATE customer SET wallet_balance = ? WHERE customer_id = ? AND is_active = 1";
	        PreparedStatement updateCustomerStatement = connection.prepareStatement(updateCustomerQuery);
	        updateCustomerStatement.setDouble(1, walletBalance - totalCost);
	        updateCustomerStatement.setInt(2, customerId);
	        int updateCustomerResult = updateCustomerStatement.executeUpdate();

	        // check if update was successful
	        if (updateCustomerResult == 0) {
	            throw new NoRecordFoundException("No customer record found with ID: " + customerId);
	        }

	        // update stock quantity
	        String updateStockQuery = "UPDATE stock SET quantity = quantity - ? WHERE stock_id = ? AND is_deleted = 0";
	        PreparedStatement updateStockStatement = connection.prepareStatement(updateStockQuery);
	        updateStockStatement.setInt(1, quantity);
	        updateStockStatement.setInt(2, stockId);
	        int updateStockResult = updateStockStatement.executeUpdate();

	        // check if update was successful
	        if (updateStockResult == 0) {
	            throw new NoRecordFoundException("No stock record found with ID: " + stockId);
	        }

	        // add transaction record
	        String addTransactionQuery = "INSERT INTO transaction (customer_id, stock_id, transaction_type, quantity, price, total_amount) VALUES (?, ?, ?, ?, ?, ?)";
	        PreparedStatement addTransactionStatement = connection.prepareStatement(addTransactionQuery);
	        addTransactionStatement.setInt(1, customerId);
	        addTransactionStatement.setInt(2, stockId);
	        addTransactionStatement.setString(3, "BUY");
	        addTransactionStatement.setInt(4, quantity);
	        addTransactionStatement.setDouble(5, stockPrice);
	        addTransactionStatement.setDouble(6, totalCost);
	        int addTransactionResult = addTransactionStatement.executeUpdate();

	        // check if transaction was added successfully
	        if (addTransactionResult == 0) {
	            throw new SQLException("Failed to add transaction record.");
	        }

	        // get the transaction ID
	        

	        // add stock record to customer_stock table
	        String addCustomerStockQuery = "INSERT INTO customer_stock (customer_id, stock_id, quantity,name,price) VALUES (?, ?, ?,?,?)";
	        PreparedStatement addCustomerStockStatement = connection.prepareStatement(addCustomerStockQuery);
	        addCustomerStockStatement.setInt(1, customerId);
	        addCustomerStockStatement.setInt(2, stockId);
	        addCustomerStockStatement.setInt(3, quantity);
	        addCustomerStockStatement.setString(4, stockName);
	        addCustomerStockStatement.setDouble(5, stockPrice);
	        
	        int addCustomerStockResult = addCustomerStockStatement.executeUpdate();

	        if (addCustomerStockResult == 0) {
	            throw new SQLException("Failed to add stock record to customer_stock table.");
	        }

	        // retrieve generated k
	            
	        } catch (SQLException | ClassNotFoundException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (connection != null) {
	                	connection.setAutoCommit(true);
	                	connection.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        return isSuccessful;
	}


	public double checkWalletBalance(int customerId) throws NoRecordFoundException {
	    Connection connection = null;
	    double walletBalance = 0.0;
	    try {
	        connection = DBUtils.getConnection();
	        String query = "SELECT wallet_balance FROM customer WHERE customer_id = ? AND is_active = 1";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, customerId);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	            walletBalance = resultSet.getDouble("wallet_balance");
	        } else {
	            throw new NoRecordFoundException("No customer record found with ID: " + customerId);
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    } finally {
	        try {
				DBUtils.closeConnection(connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    return walletBalance;
	}

	public double fetchStockPrice(int stockId) throws NoRecordFoundException {
	    Connection connection = null;
	    double stockPrice = 0.0;
	    try {
	        connection = DBUtils.getConnection();
	        String query = "SELECT price, name FROM stock WHERE stock_id = ? AND is_deleted = 0";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, stockId);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	            stockPrice = resultSet.getDouble("price");
	            stockName = resultSet.getString("name");
	        } else {
	            throw new NoRecordFoundException("No stock record found with ID: " + stockId);
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    } finally {
	        try {
				DBUtils.closeConnection(connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    return stockPrice;
	}

	public boolean sellStock(int customerId, int stockId, int quantity) throws InsufficientSharesException, NoRecordFoundException, ClassNotFoundException {
	    Connection connection = null;
	    boolean isSuccessful = false;
	    try {
	        connection = DBUtils.getConnection();
	        connection.setAutoCommit(false); // start transaction

	        // fetch customer data
	        double walletBalance = checkWalletBalance(customerId);
	        int customerShares = fetchCustomerShares(customerId, stockId);
	        double stockPrice = fetchStockPrice(stockId);
	        double totalSaleAmount = stockPrice * quantity;

	        // check if customer has enough shares to sell
	        if (customerShares < quantity) {
	            throw new InsufficientSharesException("Customer with ID " + customerId + " has insufficient shares for sale.");
	        }

	        // update customer wallet balance
	        String updateCustomerQuery = "UPDATE customer SET wallet_balance = ? WHERE customer_id = ? AND is_active = 1";
	        PreparedStatement updateCustomerStatement = connection.prepareStatement(updateCustomerQuery);
	        updateCustomerStatement.setDouble(1, walletBalance + totalSaleAmount);
	        updateCustomerStatement.setInt(2, customerId);
	        int updateCustomerResult = updateCustomerStatement.executeUpdate();

	        // check if update was successful
	        if (updateCustomerResult == 0) {
	            throw new NoRecordFoundException("No customer record found with ID: " + customerId);
	        }

	        // update stock quantity
	        String updateStockQuery = "UPDATE stock SET quantity = quantity + ? WHERE stock_id = ? AND is_deleted = 0";
	        PreparedStatement updateStockStatement = connection.prepareStatement(updateStockQuery);
	        updateStockStatement.setInt(1, quantity);
	        updateStockStatement.setInt(2, stockId);
	        int updateStockResult = updateStockStatement.executeUpdate();

	        // check if update was successful
	        if (updateStockResult == 0) {
	            throw new NoRecordFoundException("No stock record found with ID: " + stockId);
	        }

	        // add transaction record
	        String addTransactionQuery = "INSERT INTO transaction (customer_id, stock_id, transaction_type, quantity, price, total_amount) VALUES (?, ?, ?, ?, ?, ?)";
	        PreparedStatement addTransactionStatement = connection.prepareStatement(addTransactionQuery);
	        addTransactionStatement.setInt(1, customerId);
	        addTransactionStatement.setInt(2, stockId);
	        addTransactionStatement.setString(3, "SELL");
	        addTransactionStatement.setInt(4, quantity);
	        addTransactionStatement.setDouble(5, stockPrice);
	        addTransactionStatement.setDouble(6, totalSaleAmount);
	        int addTransactionResult = addTransactionStatement.executeUpdate();

	        // check if transaction was added successfully
	        if (addTransactionResult == 0) {
	            throw new SQLException("Failed to add transaction record.");
	        }
	     // remove stock record to customer_stock table	        
	        int stockQuantity = getStockQuantity(stockId);
	        String addCustomerStockQuery = "UPDATE customer_stock SET quantity= ? WHERE stock_id=?";
	        PreparedStatement addCustomerStockStatement = connection.prepareStatement(addCustomerStockQuery);        
	        addCustomerStockStatement.setInt(1, stockQuantity-quantity);
	        addCustomerStockStatement.setInt(2, stockId);
	        int addCustomerStockResult = addCustomerStockStatement.executeUpdate();
	        
	        String deleteQuery = "DELETE FROM customer_stock  WHERE quantity = 0";
	        PreparedStatement dq = connection.prepareStatement(deleteQuery);
	        
	        
	        if (addCustomerStockResult == 0) {
	            throw new SQLException("Failed to sell stock record to customer_stock table.");
	        }
	        dq.executeUpdate();

	        // commit transaction
	        connection.commit();
	        isSuccessful = true;
	    } catch (SQLException e) {
	        System.err.println("An error occurred: " + e.getMessage());
	        try {
	            connection.rollback(); // rollback transaction
	        } catch (SQLException ex) {
	            System.err.println("An error occurred: " + ex.getMessage());
	        }
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        // close connection
	        if (connection != null) {
	            try {
	                connection.setAutoCommit(true);
	                connection.close();
	            } catch (SQLException e) {
	                System.err.println("An error occurred: " + e.getMessage());
	            }
	        }
	    }
	    return isSuccessful;
	}

	public int fetchCustomerShares(int customerId, int stockId) throws Exception {
	    Connection connection = null;
	    int shares = 0;
	    try {
	        connection = DBUtils.getConnection();
	        
	        String query = "SELECT quantity FROM transaction WHERE customer_id = ? AND stock_id = ? AND transaction_type = ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, customerId);
	        statement.setInt(2, stockId);
	        statement.setString(3, "BUY");
	        ResultSet resultSet = statement.executeQuery();
	        
	        while (resultSet.next()) {
	            shares += resultSet.getInt("quantity");
	        }
	        
	        resultSet.close();
	        statement.close();
	    } catch (SQLException | ClassNotFoundException e) {
	        throw e;
	    } finally {
	        if (connection != null) {
	            connection.close();
	        }
	    }
	    
	    if (shares == 0) {
	        throw new NoRecordFoundException("No transaction record found for customer with ID: " + customerId + " and stock with ID: " + stockId);
	    }
	    
	    return shares;
	}
	public int getStockQuantity(int stockId) throws Exception {
	    Connection connection = null;
	    int shares = 0;
	    try {
	        connection = DBUtils.getConnection();
	        
	        String query = "SELECT quantity FROM customer_stock WHERE stock_id = ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, stockId);
	        ResultSet resultSet = statement.executeQuery();
	        
	        while (resultSet.next()) {
	            shares += resultSet.getInt("quantity");
	        }
	        
	        resultSet.close();
	        statement.close();
	    } catch (SQLException | ClassNotFoundException e) {
	        throw e;
	    } finally {
	        if (connection != null) {
	            connection.close();
	        }
	    }
	    
	    if (shares == 0) {
	        throw new NoRecordFoundException("No transaction record found for customer with ID: " + stockId);
	    }
	    
	    return shares;
	}
	
	public List<StockData> getAllStockData(int id) throws NoRecordFoundException {
	    Connection connection = null;
	    List<StockData> stockDataList = new ArrayList<>();

	    try {
	        connection = DBUtils.getConnection();
	        String query = "SELECT * from customer_stock WHERE quantity > 0 AND customer_id=?";
			        PreparedStatement statement = connection.prepareStatement(query);
			        statement.setInt(1, id);
			        ResultSet resultSet = statement.executeQuery();
			        
			if(DBUtils.isResulSetEmpty(resultSet)) {
				return null;
			}else {
				while (resultSet.next()) {
		            int stockId = resultSet.getInt("stock_id");
		            String stockName = resultSet.getString("name");
		            double price = resultSet.getDouble("price");
		            int quantity = resultSet.getInt("quantity");
		            int customerId = resultSet.getInt("customer_id");

		            StockData stockData = new StockDataImpl(stockName, price, quantity, customerId);
		            stockData.setStockId(stockId);
		            stockDataList.add(stockData);
			}
	        
	        }
	    } catch (SQLException | ClassNotFoundException ex) {
	        ex.printStackTrace();
	    } finally {
	    	try {
	    		DBUtils.closeConnection(connection);
	    		
	    	}catch(SQLException e) {
	    		
	    	}
	    }

	    return stockDataList;
	}
	public List<Transaction> viewTransactionHistory(int id) throws NoRecordFoundException{
		List<Transaction> list = new ArrayList<>();;
		Connection con = null;
		try {
			con = DBUtils.getConnection();
			String query = "SELECT * FROM transaction WHERE customer_id = ?";
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			if(DBUtils.isResulSetEmpty(rs)) {
				return null;
			}else {
				while(rs.next()) {
					
					Transaction transaction =new TransactionImpl(rs.getInt(2),rs.getInt(3),rs.getDate(5).toLocalDate(), 
							rs.getString(4)  ,rs.getInt(6),rs.getDouble(7),rs.getDouble(8));
					list.add(transaction);
				}
			}
			
		}catch(SQLException | ClassNotFoundException e) {
			throw new NoRecordFoundException("no Transaction found");
		}finally {
			try {
				DBUtils.closeConnection(con);
			}catch(SQLException e) {
				
			}
		}
		
		
		
		
		
		return list;
	}

	@Override
	public void addMoney(int id, double amount) throws SomethingWentWrongException {
		// TODO Auto-generated method stub
		Connection con = null;
		try {
			con = DBUtils.getConnection();
			double wallet_balance = checkWalletBalance(id);
			String query = "UPDATE customer SET wallet_balance = ? + ? WHERE customer_id=?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setDouble(1, wallet_balance);
			ps.setDouble(2, amount);
			ps.setInt(3, id);
			
			ps.executeUpdate();
			
		}catch(SQLException | ClassNotFoundException | NoRecordFoundException e) {
			throw new SomethingWentWrongException("something went wrong please retry");
		}finally {
			try {
				DBUtils.closeConnection(con);
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void withdrawMoney(int id, double amount) throws SomethingWentWrongException, InsufficientBalanceException {
	    Connection con = null;
	    try {
	        con = DBUtils.getConnection();
	        double wallet_balance = checkWalletBalance(id);

	        if (wallet_balance < amount) {
	            throw new InsufficientBalanceException("You don't have enough balance in your wallet.");
	        }

	        String query = "UPDATE customer SET wallet_balance = ? - ? WHERE customer_id=?";
	        PreparedStatement ps = con.prepareStatement(query);
	        ps.setDouble(1, wallet_balance);
	        ps.setDouble(2, amount);
	        ps.setInt(3, id);

	        ps.executeUpdate();

	    } catch (SQLException | ClassNotFoundException | NoRecordFoundException e) {
	        throw new SomethingWentWrongException("Something went wrong. Please try again.");
	    } finally {
	        try {
	            DBUtils.closeConnection(con);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

}