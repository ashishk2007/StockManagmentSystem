package com.masai.UI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.masai.DAO.BrokerDAO;
import com.masai.DAO.BrokerDAOImpl;
import com.masai.DAO.CustomerDAO;
import com.masai.DAO.CustomerDAOImpl;
import com.masai.DTO.Customer;
import com.masai.DTO.CustomerImpl;
import com.masai.DTO.Stock;
import com.masai.DTO.StockData;
import com.masai.DTO.Transaction;
import com.masai.EXCEPTION.InsufficientBalanceException;
import com.masai.EXCEPTION.InsufficientSharesException;
import com.masai.EXCEPTION.InvalidCredentialsException;
import com.masai.EXCEPTION.NoRecordFoundException;
import com.masai.EXCEPTION.SomethingWentWrongException;

public class CustomerUI {
	static Customer customer;
	public static void customerLogin(Scanner scanner) {

	    if (isCustomerLogged.isLogged) {
	        try {
	            showUserMenu(scanner);
	        } catch (NoRecordFoundException e) {
	            System.out.println(e.getMessage());
	        }
	    } else {
	        int choice;

	        try {
	            do {
	                System.out.println("Main Menu");
	                System.out.println("1. Sign Up");
	                System.out.println("2. Login");
	                System.out.println("3. Exit");
	                System.out.print("Enter your choice: ");
	                choice = scanner.nextInt();
	                scanner.nextLine(); // clear input buffer

	                switch (choice) {
	                    case 1:
	                        signUp(scanner);
	                        break;
	                    case 2:
	                        login(scanner);
	                        break;
	                    case 3:
	                        System.out.println("Goodbye!");
	                        break;
	                    default:
	                        System.out.println("Invalid choice!");
	                }
	            } while (choice != 3);
	        } catch (InputMismatchException e) {
	            System.out.println("Please enter numeric value in the list");
	            scanner.nextLine(); // clear input buffer
	        }

	    }

	}

	
    private static void login(Scanner scanner) {
        System.out.println("Enter username");
        String username = scanner.next();
        System.out.println("Enter Password");
        String password = scanner.next();
        CustomerDAO<?> daoLayer = new CustomerDAOImpl();
       try {
		customer =  daoLayer.authenticateCustomer(username, password);
		isCustomerLogged.isLogged=true;
		System.out.println("You're logged in!!!");
		try {
			showUserMenu(scanner);
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		} catch (InvalidCredentialsException e) {
			System.out.println(e.getMessage());
		}
    }
    private static void signUp(Scanner scanner) {
    	  System.out.println("Enter first name: ");
    	  String firstName = scanner.next();
    	  System.out.println("Enter last name: ");
    	  String lastName = scanner.next();
    	  System.out.println("Enter username: ");
    	  String username = scanner.next();
    	  System.out.println("Enter password: ");
    	  String password = scanner.next();
    	  System.out.println("Enter address: ");
    	  String address = scanner.next();
    	  System.out.println("Enter mobile number: ");
    	  String mobileNumber = scanner.next();
    	  System.out.println("Enter email: ");
    	  String email = scanner.next();
    	  System.out.println("Enter wallet balance: ");
    	  double walletBalance = scanner.nextDouble();
    	  System.out.println("Is active (1/0): ");
    	  int isActive = scanner.nextInt();
    	  Customer customer = new CustomerImpl( firstName,  lastName,  username,  mobileNumber,  address,
  				 email,  password,  walletBalance,  isActive);
    	  CustomerDAO<?> daoLayer = new CustomerDAOImpl();
    	  try {
    		  daoLayer.addCustomer(customer);
    		  System.out.println("Customer Added Successfully!!");
    	  }catch(SomethingWentWrongException e) {
    		  System.out.println(e.getMessage());
    	  }
    }
    private static void logout() {
    	isCustomerLogged.isLogged= false;
    	System.out.println("You're Logged Out!!!!!");
    }
    
    static boolean showStock(int id) {
    	CustomerDAO<?> daoLayer = new CustomerDAOImpl();
    	List<StockData> list = new ArrayList<>();
    	try {
    		 list = daoLayer.getAllStockData(id);
    		 if (list != null && !list.isEmpty()) {
    			    // code to display the list of stocks
    			 for(StockData i : list) {
    				 System.out.println("Stock ID: " + i.getStockId() + ", Name: " 
    						 + i.getStockName() + ", Quantity: " + 
    						 i.getQuantity() + ", Price: " + i.getPrice());
    			 }
    			} else {
    			    System.out.println("no stock in your portfolio");
    			    return false;
    			}
    		
    		
    	}catch(NoRecordFoundException e) {
    		System.out.println(e.getMessage());
    	}
		return true;
    }
    
    private static void buySellStocks(Scanner scanner) {
    	if(!isCustomerLogged.isLogged) {
    		System.out.println("You're not login please login first");
    		return;
    	}else {
    		System.out.println("What do you want Buy Or Sell\nPlease entry 1 for Buy and 0 for sell");
    		int choice = scanner.nextInt();
    		CustomerDAO<?> daoLayer = new CustomerDAOImpl();
    			if(choice == 1) {
//    				int customerId, int stockId, int quantity
    				BrokerUI viewStock = new BrokerUI();
                	viewStock.viewStock();
                	System.out.println("Enter Stock Id from the list");
    				int stockId= scanner.nextInt();
    				System.out.println("Enter Quantity");
    				int quantity = scanner.nextInt();
    				
    				try {
						daoLayer.buyStocks(customer.getId(), stockId, quantity);
					} catch (InsufficientBalanceException | NoRecordFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				System.out.println("Congratulation your stock order has been completed");
    			}else {
    				if(showStock(customer.getId())) {;
    				System.out.println("Enter Stock Id from the list");
    				BrokerUI viewStock = new BrokerUI();
    				int stockId= scanner.nextInt();
    				System.out.println("Enter Quantity");
    				int quantity = scanner.nextInt();
    				
    				try {
						daoLayer.sellStock(customer.getId(), stockId, quantity);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InsufficientSharesException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoRecordFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				System.out.println("Congratulation your stock order has been completed");
    				}
    				
    			}
    	}
		
		
	}
    
    static void viewTransactionHistory() {
    	CustomerDAO<?> daoLayer  = new CustomerDAOImpl();
    	try {
			List<Transaction> list = daoLayer.viewTransactionHistory(customer.getId()); 
			list.forEach(System.out::println);
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
    }
    private static void addWithdrawFunds(Scanner scanner) {
		// TODO Auto-generated method stub
    	boolean flag = true;
    	while(flag) {
    		System.out.println("Enter 1 for Add Amount 0 for Withdraw Amount\n3.Exit");
    		int choice = scanner.nextInt();
    		switch(choice) {
    			case 1:
    				addMoney(scanner);
    				break;
    			case 0:
    				withdrawMoney(scanner);
    				break;
    			case 3:
    				flag=false;
    			default:
    				System.out.println("Invalid choice");
    		}
    	}
		
	}
    private static void withdrawMoney(Scanner scanner) {
    	CustomerDAO daoLayer  = new CustomerDAOImpl();
    	System.out.println("Enter Amount");
    	double amount = scanner.nextDouble();
    	try {
    		daoLayer.withdrawMoney(customer.getId(), amount);
    		System.out.println("Amount Withdrawn successfully!");
    		
    	}catch(SomethingWentWrongException | InsufficientBalanceException e) {
    		System.out.println(e.getMessage());
    	}
    	
		
	}


	private static  void addMoney(Scanner sc) {
    	CustomerDAO daoLayer  = new CustomerDAOImpl();
    	System.out.println("Enter Amount");
    	double amount = sc.nextDouble();
    	try {
    		daoLayer.addMoney(customer.getId(), amount);
    		System.out.println("Amount added successfully!");
    		
    	}catch(SomethingWentWrongException e) {
    		System.out.println(e.getMessage());
    	}
    	
    }
	 private static void showUserMenu(Scanner scanner) throws NoRecordFoundException {
	        int choice;

	        do {
	            System.out.println("User Menu");
	            System.out.println("1. View Stocks");
	            System.out.println("2. Buy/Sell Stocks");
	            System.out.println("3. View Transaction History");
	            System.out.println("4. Add/Withdraw Funds");
	            System.out.println("5. Logout");
	            System.out.print("Enter your choice: ");
	            choice = scanner.nextInt();

	            switch (choice) {
	                case 1:
	                	BrokerUI viewStock = new BrokerUI();
	                	BrokerUI.viewStock();
	                    break;
	                case 2:
	                    buySellStocks(scanner);
	                    break;
	                case 3:
	                   viewTransactionHistory();
	                    break;
	                case 4:
	                    addWithdrawFunds(scanner);
	                    break;
	                case 5:
	                    logout();
	                    break;
	                default:
	                    System.out.println("Invalid choice!");
	            }
	        } while (choice != 5);

	    }


	

	
}
