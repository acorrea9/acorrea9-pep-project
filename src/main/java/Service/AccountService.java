package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account) {
        if((account.getUsername().length() != 0) && (account.getPassword().length() >= 4) && loginAccount(account) == null) {
            return accountDAO.registerAccount(account);
        }
        return null;
    }

    public Account loginAccount(Account account) {
        return accountDAO.loginAccount(account);
    }
}
