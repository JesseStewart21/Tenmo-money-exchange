package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AccountService accountService = new AccountService();

    private TransferService transferService = new TransferService();
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            accountService.setAuthToken(currentUser.getToken());
            transferService.setAuthToken(currentUser.getToken());
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        int userId = currentUser.getUser().getId();
        Account account = accountService.findAccountByUserId(userId);
        BigDecimal balance= account.getBalance();
        System.out.println("Your current balance is $" + balance);

	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub

        int userId = currentUser.getUser().getId();
        Account account = accountService.findAccountByUserId(userId);
        int accountId = account.getAccountId();
        List<Transfer> transfers = transferService.findAllById(accountId);

        System.out.println("This is your transaction history");
        System.out.println("______________________________");

        for(Transfer currentTransfer : transfers){
            int transferId = currentTransfer.getTransferId();
            BigDecimal amount = currentTransfer.getAmount();
            int requestType = currentTransfer.getTransferTypeId();
            int accountToId = currentTransfer.getAccountTo();
            int accountFromId = currentTransfer.getAccountFrom();
            int transferStatus = currentTransfer.getTransferStatusId();

            String requestTypeStr = "";
            String status = "";

            if(requestType == 1) {
                requestTypeStr = "sent a transfer to";
            }else if (requestType == 2){
                requestTypeStr = "requested a transfer from";
            }

            if (transferStatus == 1) {
                status = "Pending";
            }else if (transferStatus == 2) {
                status = "Approved";
            }else if (transferStatus == 3) {
                status = "Rejected";
            }

           //we know you wouldn't pass back someone else's account number, but we did not write the methods to support
            //pulling another user's name.  Sorry!

            System.out.println("Transfer #" + transferId);
            System.out.println("Amount: $" + amount);
            System.out.println(accountFromId +" "+ requestTypeStr +" "+ accountToId);
            System.out.println("Transfer Status: " + status);
            System.out.println("______________________________");
        }
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub

        int userId = currentUser.getUser().getId();
        Account account = accountService.findAccountByUserId(userId);
        int accountId = account.getAccountId();
        List<Transfer> transfers = transferService.findAllById(accountId);

        System.out.println("This is your transaction history");
        System.out.println("______________________________");

        for(Transfer currentTransfer : transfers) {
            int transferId = currentTransfer.getTransferId();
            BigDecimal amount = currentTransfer.getAmount();
            int requestType = currentTransfer.getTransferTypeId();
            int accountToId = currentTransfer.getAccountTo();
            int accountFromId = currentTransfer.getAccountFrom();
            int transferStatus = currentTransfer.getTransferStatusId();

            String requestTypeStr = "";
            String status = "";

            if (requestType == 1) {
                requestTypeStr = "sent a transfer to";
            } else if (requestType == 2) {
                requestTypeStr = "requested a transfer from";
            }

            if (transferStatus == 1) {
                status = "Pending";
                //we know you wouldn't pass back someone else's account number, but we did not write the methods to support
                //pulling another user's name.  Sorry!

                System.out.println("Transfer #" + transferId);
                System.out.println("Amount: $" + amount);
                System.out.println(accountFromId + " " + requestTypeStr + " " + accountToId);
                System.out.println("Transfer Status: " + status);
                System.out.println("______________________________");
            }

    }
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
       String prompt = "What is the account number you will be sending to?";
        int accountToId = consoleService.promptForInt(prompt);
        String prompt2 = "How much are you sending?";
        BigDecimal amount = consoleService.promptForBigDecimal(prompt2);

        Transfer newTransfer = new Transfer();
        int userId = currentUser.getUser().getId();
        Account account = accountService.findAccountByUserId(userId);
        int accountFromId = account.getAccountId();
        int transferTypeId = 2;
        int transferStatusId = 3;

        newTransfer.setAccountTo(accountToId);
        newTransfer.setAccountFrom(accountFromId);
        newTransfer.setAmount(amount);
        newTransfer.setTransferStatusId(transferStatusId);
        newTransfer.setTransferTypeId(transferTypeId);

        int transferId = transferService.createTransfer(newTransfer);

        System.out.println("Your transfer Id is: " + transferId);
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
        String prompt = "What is the account number you will be requesting from?";
        int accountToId = consoleService.promptForInt(prompt);
        String prompt2 = "How much are you requesting?";
        BigDecimal amount = consoleService.promptForBigDecimal(prompt2);

        Transfer newTransfer = new Transfer();
        int userId = currentUser.getUser().getId();
        Account account = accountService.findAccountByUserId(userId);
        int accountFromId = account.getAccountId();
        int transferTypeId = 1;
        int transferStatusId = 1;

        newTransfer.setAccountTo(accountToId);
        newTransfer.setAccountFrom(accountFromId);
        newTransfer.setAmount(amount);
        newTransfer.setTransferStatusId(transferStatusId);
        newTransfer.setTransferTypeId(transferTypeId);

        int transferId = transferService.createTransfer(newTransfer);

        System.out.println("Your transfer Id is: " + transferId);
    }
	}


