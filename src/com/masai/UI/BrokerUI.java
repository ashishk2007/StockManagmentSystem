package com.masai.UI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.masai.DAO.BrokerDAO;
import com.masai.DAO.BrokerDAOImpl;
import com.masai.DTO.Customer;
import com.masai.DTO.CustomerImpl;
import com.masai.DTO.Stock;
import com.masai.DTO.StockImpl;
import com.masai.EXCEPTION.NoRecordFoundException;
import com.masai.EXCEPTION.SomethingWentWrongException;

public class BrokerUI {
	static void brokerLogin(Scanner sc) throws NoRecordFoundException {
		
		if(isBrokerLogged.logged) {
			brokerMenu(sc);
		}else {
			System.out.println("Enter Username!");
			String username = sc.next();
			System.out.println("Enter Password!");
			String password = sc.next();
			if(username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
				isBrokerLogged.logged=true;
				brokerMenu(sc);
			}else {
				System.out.println("Wrong Credential");
			}
		}	
	}	
	static void addStock(Scanner sc) {
		System.out.println("Enter Stock name");
		String name = sc.next();
		System.out.println("Enter Stock Quantity");
		int quantity = sc.nextInt();
		System.out.println("Enter Stock Price");
		double price = sc.nextDouble();
		
		Stock stock = new StockImpl(name,quantity,price);
		BrokerDAO daoLayer = new BrokerDAOImpl();
		try {
			daoLayer.addnewStock(name, quantity, price);
			System.out.println("Stock Added Successfully");
		} catch (SomethingWentWrongException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
	}
	static void  viewStock() {
		BrokerDAO daoLayer = new BrokerDAOImpl();
		List<Stock> list = new ArrayList<>();
		try {
			 list = daoLayer.viewAllStocks();
			 
			 for(Stock i : list) {
				 System.out.println("Stock ID: " + i.getId() + ", Name: " 
						 + i.getName() + ", Quantity: " + 
								 i.getQuantity() + ", Price: " + i.getPrice() + ", Is Deleted: " + i.getIsDeleted());
			 }
			
			
		}catch(NoRecordFoundException e) {
			System.out.println(e.getMessage());
		}
	}
		
	static void deleteStock(Scanner sc) {
		System.out.println("Enter Stock ID");
		int id = sc.nextInt();
		
		BrokerDAO daoLayer = new BrokerDAOImpl();
		try {
			if(daoLayer.deleteStock(id)) {
				
				System.out.println("Stock Deleted successfully");				
			}else {
				System.out.println("No stock found");
			}
		}catch(SomethingWentWrongException ms) {
			System.out.println(ms.getMessage());
		}
		
		
	}
	
	static void deleteCustomer(Scanner sc) {
		System.out.println("Enter customer id");
		int id = sc.nextInt();
		BrokerDAO daoLayer = new BrokerDAOImpl();
		try {
			if(daoLayer.deleteCustomer(id)) {
				System.out.println("Customer Deleted Successfully!");				
			}else {
				System.out.println("No customer found!");
			}
		}catch(SomethingWentWrongException e) {
			System.out.println(e.getMessage());
		}
	}
	private static void viewConsolidatedReport(Scanner sc) {
		// TODO Auto-generated method stub
		System.out.println("Enter stock name");
		String name = sc.next();
		BrokerDAO daoLayer = new BrokerDAOImpl();
		try {
			
			int[] arr = daoLayer.viewStockReport(name);

			System.out.println("Consolidated Report for Stock: " + name);
			System.out.println("Total pieces sold: " + arr[0]);
			System.out.println("Total pieces yet to be sold: " + arr[1]);
		}catch(SomethingWentWrongException e) {
			System.out.println(e.getMessage());
		}
		
	}
	static void brokerMenu(Scanner sc) throws NoRecordFoundException {
		System.out.println("Welcome Broker");
		while (isBrokerLogged.logged) {
			try {
				 System.out.println("-------- Broker Menu --------");
		            System.out.println("1. View all customers");
		            System.out.println("2. Add new stocks");
		            System.out.println("3. View all stocks");
		            System.out.println("4. View consolidated report of a stock");
		            System.out.println("5. Delete customer");
		            System.out.println("6. Delete stock");
		            System.out.println("7. Logout");
		            System.out.println("-------------------------------");
		            
		            System.out.print("Enter your choice: ");
		            int choice = sc.nextInt();
		            BrokerDAO daoLayer = new BrokerDAOImpl();
		            
		            switch (choice) {
		            case 1:
		                try {
							List<Customer> list = daoLayer.viewAllCustomers();
							
							for(Customer i: list) {
								System.out.println(
								"CustomerID: "+i.getId()+" FirstName:" + i.getFirstName()+
								" LastName: "+i.getLastName()+" Username: "+i.getUsername()+
								" Email: "+i.getEmail()+" WalletBalance: "+i.getWalletBalance());
							}
						} catch (SQLException e) {
							System.out.println(e.getMessage());
						}
		                break;
		            case 2:
		            	addStock(sc);
		                break;
		            case 3:
		                viewStock();
		                break;
		            case 4:
		                viewConsolidatedReport(sc);
		                break;
		            case 5:
		                deleteCustomer(sc);
		                break;
		            case 6:
		                deleteStock(sc);
		                break;
		            case 7:
		                isBrokerLogged.logged = false;
		                break;
		            default:
		                System.out.println("Invalid choice");
		                break;
		        }
		    }catch(InputMismatchException e) {
		    	System.out.println("Please enter numeric value");
		    	sc.nextLine();
		    }
			}
           
	}
	
}

