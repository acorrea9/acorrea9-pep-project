package Service;

import DAO.AccountDAO;
import Model.Account;

/**
 * The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
 * persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
 * SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
 * actions undertaken by the API to a logging file.
 */
public class AccountService {
    private AccountDAO accountDAO;

    /**
     * no-args constructor for creating a new AccountService with a new AccountDAO.
     */
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * Uses the AccountDAO to insert an account to the database.
     * @param account an account object.
     * @return an account with an account_id if it was successfully inserted, null if it was 
     * not successfully inserted (eg if the account prerequisites were not met or username was taken)
     */
    public Account registerAccount(Account account) {
        if((account.getUsername().length() != 0) && (account.getPassword().length() >= 4) && loginAccount(account) == null) {
            return accountDAO.registerAccount(account);
        }
        return null;
    }

    /**
     * Uses the AccountDAO to verify an account exists in the database.
     * @param account an account object.
     * @return an account with an account_id if the account exists, null if the account does not exists
     */
    public Account loginAccount(Account account) {
        return accountDAO.loginAccount(account);
    }
}
