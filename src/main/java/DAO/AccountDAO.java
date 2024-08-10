package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

/**
 * A DAO is a class that mediates the transformation of data between the format of objects in Java to rows in a
 * database. The methods here are mostly filled out, you will just need to add a SQL statement.
 *
 * We may assume that the database has already created a table named 'account'.
 * It contains similar values as the Account class:
 * account_id, which is of type int and is a primary key,
 * username, which is of type varchar(255) and unique,
 * password, which is of type varchar(255).
 */
public class AccountDAO {
    /**
     * Insert an account to the database.
     * @param account an account object.
     * @return an account with an account_id if it was successfully inserted, null if it was 
     * not successfully inserted (eg if the account prerequisites were not met or username was taken)
     */
    public Account registerAccount(Account account) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account(username, password) VALUES(? , ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()) {
                int newAccountId = pkeyResultSet.getInt("account_id");
                return new Account(newAccountId, account.getUsername(), account.getPassword());
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Verify an account exists in the database.
     * @param account an account object.
     * @return an account with an account_id if the account exists, null if the account does not exists
     */
    public Account loginAccount(Account account) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE username=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                int accountID = resultSet.getInt("account_id");
                return new Account(accountID, account.getUsername(), account.getPassword());
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
