package com.masai.UI;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.masai.EXCEPTION.NoRecordFoundException;

public class UIMain {
	public static void main(String[] args) throws NoRecordFoundException {
		Scanner scanner = new Scanner(System.in);
		 boolean quit = true;
	        while (quit) {
	            try {
	            System.out.println("Enter your choice:");
	            System.out.println("1. Broker Login");
	            System.out.println("2. Customer Login");
	            System.out.println("3. Exit");
	       
	            int choice = scanner.nextInt();
	            scanner.nextLine(); 
	           
	            	switch (choice) {
	                case 1:
	                    BrokerUI.brokerLogin(scanner);
	                    break;
	                case 2:
	                    CustomerUI.customerLogin(scanner);
	                    break;
	                case 3:
	                	System.out.println("Thanks for using Stock Broker System");
	                    quit = false;
	                    break;
	                default:
	                    System.out.println("Invalid choice");
	                    break;
	            }
	            }catch(InputMismatchException e) {
	            	System.out.println("Input mismatch please enter numeric value");
	            	scanner.nextLine();
	            }
	            
	        }
	        scanner.close();
	    }
	}
		
