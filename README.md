# StockManagmentSystem

This is a Stock Broker System that allows customers to trade and invest in shares. The system consists of two types of users: brokers and customers. Brokers have access to functions such as viewing customers, adding new stocks, viewing all stocks, deleting customers, deleting stocks, and logging out. Customers have access to functions such as signing up, logging in, viewing all stocks, buying and selling stocks, viewing transaction history, adding and withdrawing funds, logging out, and deleting their account.

Database Design: 


![stockER](https://github.com/ashishk2007/StockManagmentSystem/assets/113036805/94adcf84-3941-4539-a524-47f505db820d)




Database Scheama

#Feature

Buy A stock
Sell A stock
See transaction
Manage Wallet
The database consists of the following tables:

Broker - stores information about the broker, including their username and password. Customer - stores information about the customer, including their name, username, password, address, mobile number, and email. Stock - stores information about each stock, including the stock name, the quantity of stock available, and the price of the stock. Wallet - stores information about the customer's wallet, including the balance. Transaction - stores information about each transaction made by a customer, including the stock name, the quantity of stock bought/sold, the price at which the stock was bought/sold, and the timestamp of the transaction. To maintain data integrity and relationships between tables, foreign keys and constraints have been used. Each table has a field called is_deleted, which is set to 0 by default. When a record is deleted, the value of is_deleted is set to 1, which allows the record to remain in the database while being hidden from users and administrators.

How to run the program:

Clone this repository to your local machine. Open the project in your IDE. Make sure the database credentials are correctly set in the properties file. Run the main class to start the program. Follow the instructions displayed on the console to use the program. Exceptions:

The program handles exceptions such as duplicate email, incorrect stock name, batch name, etc. Appropriate error messages are displayed to the user when these exceptions occur.

Dependencies:

The program uses Java and MySQL. The required dependencies are included in the pom.xml file.
