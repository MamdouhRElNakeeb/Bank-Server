
package com.bankserver;

import com.bankserver.controller.BankController;
import com.bankserver.controller.CustomerController;
import com.bankserver.controller.OperationController;
import com.bankserver.model.Bank;
import com.bankserver.model.Customer;
import com.bankserver.model.Operation;
import com.bankserver.util.Encryption;
import com.bankserver.util.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

/**
Executes Simple Bank Access Protocol commands
from a socket.
*/
public class BankService implements Runnable{

    private Socket s;
	private Scanner in;
	private PrintWriter out;
	private Bank bank;
	private boolean authenticated;

	CustomerController customerController;
	BankController bankController;
	OperationController operationController;

	/**
	Constructs a service object that processes commands
	from a socket for a bank.
	param aSocket the socket
	param aBank the bank
	*/
	public BankService(Socket aSocket) {

		s = aSocket;
		customerController = CustomerController.getInstance();
		bankController = BankController.getInstance();
		operationController = OperationController.getInstance();

		authenticated = false;
	}
	
	public void run() {

		try {
			try {
				in = new Scanner(s.getInputStream());
				out = new PrintWriter(s.getOutputStream());

				doService();
			}
			finally {
				s.close();
			}
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	/**
	Executes all commands until the QUIT command or the
	end of input.
	*/
	
	public void doService() throws IOException {

		while (true) {
			if (!in.hasNext()) { return; }
			String command = in.next();
			if (command.equals("QUIT")) { return; }
			else executeCommand(command);
		}
	}
	
	/**
	Executes a single command.
	param command the command to execute
	 * @throws IOException 
	 * @throws UnknownHostException 
	*/
	
	public void executeCommand(String command) throws UnknownHostException, IOException {

		if (command.equals("BANKS")){
			sendBanks();
		}
		else if (command.equals("REGISTER")){

			String bankID = in.next();
			String name = in.next();
			String amount = in.next();
			String pass = in.next();

			int accNo = (int) (Math.random() * 9999 + 1000);


			Customer customer = new Customer();
			customer.setAccountNumber(String.valueOf(accNo));
			customer.setName(name);
			customer.setPassword(Encryption.hash(pass));
			customer.setBankID(Integer.parseInt(bankID));
			customerController.store(customer);

			boolean success = operationController.makeDeposit(accNo, Double.parseDouble(amount), 0);

			if (success)
				out.println("Customer created successfully\nAccount Number: " + accNo);
			else
				out.println("Failure\nError registering the account");

			out.flush();
			return;

		}
		else if (command.equals("LOGIN")){

			String accNo = in.next();
			String pass = in.next();

			if (customerController.login(accNo, pass)){
				authenticated = true;

				Customer customer = customerController.searchCustomer(Integer.parseInt(accNo));
				Session.setAccountNumber(accNo);
				Session.setName(customer.getName());
				Session.setBankID(customer.getBankID());

				Bank bank = bankController.getBank(customer.getBankID());

				System.out.println(bank.getName());

				out.println("Success\nLogged in successfully");
				out.println(customer.getBankID() + " " + bank.getName() + " " + bank.getPort() + " " + customer.getName());
			}
			else {
				out.println("Failure\nAccount number or password are wrong!, Tru again");
			}

			out.flush();
			return;
		}
		else if(command.equals("CUSTOMER")) {

			Customer customer;
			int accNo = in.nextInt();
			int bankID = in.nextInt();
			int sameFlag = in.nextInt();

			if (sameFlag == 1) {
				customer = customerController.searchCustomer(accNo, bankID);
			}
			else {
				customer = customerController.searchCustomer(accNo);

				// Check if account is in the same bank not others
				if (customer != null && customer.getBankID() == bankID)
					customer = null;
			}

			if (customer != null){
				out.println("Success\n" + customer.getName());
			}
			else {
				out.println("Failure\n Account not found!");
			}

			System.out.println(accNo);

			out.flush();
			return;
		}

		else if (command.equals("DEPOSIT")) {

			double amount = in.nextDouble();
			int type = in.nextInt();
			int accNo = in.nextInt();

			System.out.println(accNo);

			if (operationController.makeDeposit(accNo, amount, type)){
				out.println("Success\nAmount is deposited successfully");
			}
			else {
				out.println("Failure\nAn error is occurred in deposit");
			}

			out.flush();
			return;

		}
		else if (command.equals("WITHDRAW")) {

			double amount = in.nextDouble();
			int type = in.nextInt();

			if (operationController.makeWithdraw(Integer.parseInt(Session.accountNumber()), amount, type)){
				out.println("Success\nAmount is withdrawn successfully");
			}
			else {
				out.println("Failure\nThere's no enough amount!");
			}

			out.flush();
			return;

		}
		else if (command.equals("BALANCE")) {
			sendBalance(Integer.parseInt(Session.accountNumber()));
			return;
		}

		else if (command.equals("TRANSFER")) {

			double amount = in.nextDouble();
			int accountNum2 = in.nextInt();
			String place = in.next();  // the same bank  or another bank

			Customer customer = customerController.searchCustomer(accountNum2);
			if (customer == null){

				out.println("Failure\nAccount is not found");
				return;
			}

			if(place.equals("SAME")) {

				boolean success  = operationController.makeWithdraw(Integer.parseInt(Session.accountNumber()), amount, 3);

				if (success){
					success = operationController.makeDeposit(accountNum2, amount, 2);

					if (success){
						out.println("Success\nTransfer " + amount + " to " + accountNum2 + " is successful");
						out.flush();
					}

				}
				else {

					out.println("Failure\nThere is no enough amount");
					out.flush();
				}

				return;

			}
			else if(place.equals("DIFFERENT")) {

				Bank bank = bankController.getBank(customer.getBankID());

				boolean success  = operationController.makeWithdraw(Integer.parseInt(Session.accountNumber()), amount, 3);

				if (success){

					Socket otherSocket;

					otherSocket = new Socket("localhost", bank.getPort());

					InputStream instream2 = otherSocket.getInputStream();
					OutputStream outstream2 = otherSocket.getOutputStream();

					Scanner in2 = new Scanner(instream2);
					PrintWriter out2 = new PrintWriter(outstream2);

					out2.println("DEPOSIT " + amount + " 2 " + accountNum2);
					out2.flush();

					String responseMsg = in2.nextLine() + "\n" + in2.nextLine();

					out.println(responseMsg);
					out.flush();

				}
				else {

					out.println("Failure\nThere is no enough amount");
					out.flush();
				}

			}

		}
		else if (command.equals("HISTORY")) {
			sendHistory(Integer.parseInt(Session.accountNumber()));
		}
	}

	private void sendBanks(){

		ArrayList<Bank> banks = bankController.getBanks();
		out.println(banks.size());
		out.flush();

		for(int i = 0; i < banks.size(); i++) {
			out.println(banks.get(i).getId() + " " + banks.get(i).getName() + " " + banks.get(i).getPort());
			out.flush();
		}
	}

	private void sendBalance(int accNo){
		out.println(operationController.getBalance(accNo));
		out.flush();
	}

	private void sendHistory(int accNo){

		ArrayList<Operation> operations = operationController.getAllOperations(accNo);
		out.println(operations.size());
		out.flush();

		for(int i = 0; i < operations.size(); i++) {
			out.println(operations.get(i).getType() + " " + operations.get(i).getAmount() + " " + operations.get(i).getDate());
			out.flush();
		}

	}
}
