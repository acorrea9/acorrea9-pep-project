package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

/**
 * A DAO is a class that mediates the transformation of data between the format of objects in Java to rows in a
 * database. The methods here are mostly filled out, you will just need to add a SQL statement.
 *
 * We may assume that the database has already created a table named 'message'.
 * It contains similar values as the Message class:
 * message_id, which is of type int and is a primary key,
 * posted_by, which is of type int and a foreign key that references account(account_id),
 * message_text, which is of type varchar(255),
 * time_posted_epoch, which is of type bigint.
 */
public class MessageDAO {
    /**
     * Insert a message to the database.
     * @param message a message object.
     * @return a message with a message_id if it was successfully inserted, null if it was not successfully inserted
     */
    public Message createMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()) {
                int newMessageId = pkeyResultSet.getInt("message_id");
                return new Message(newMessageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Retrieve a list of all messages.
     * @return all messages
     */
    public List<Message> getAllMessages() {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                list.add(new Message(resultSet.getInt("message_id"), 
                                        resultSet.getInt("posted_by"), 
                                        resultSet.getString("message_text"), 
                                        resultSet.getLong("time_posted_epoch")));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    /**
     * Retrieve a message by its ID from the database.
     * @param id the message_id.
     * @return a message with the specific message_id if successful, null if not successfully found
     */
    public Message getMessageById(int id) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return new Message(resultSet.getInt("message_id"), 
                                    resultSet.getInt("posted_by"), 
                                    resultSet.getString("message_text"), 
                                    resultSet.getLong("time_posted_epoch"));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Delete a message by its ID from the database.
     * @param id the message_id.
     * @return true if deletion of message is successful, false if not successfully deleted
     */
    public boolean deleteMessage(int id) {
        Connection conn = ConnectionUtil.getConnection();
        
        try {
            String sql = "DELETE FROM message WHERE message_id=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows == 1) {
                return true;
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    /**
     * Update a message by its ID from the database.
     * @param id the message_id.
     * @return true if update of message is successful, false if not successfully updated
     */
    public boolean updateMessage(int id, Message message) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            System.out.println(message.getMessage_text());
            String sql = "UPDATE message SET message_text=? WHERE message_id=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, id);

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows == 1) {
                return true;
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Retrieve a list of all messages written by a particular user.
     * @param id the account_id.
     * @return all messages of a particular user
     */
    public List<Message> getAllMessagesByAccountId(int id) {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                list.add(new Message(resultSet.getInt("message_id"), 
                                        resultSet.getInt("posted_by"), 
                                        resultSet.getString("message_text"), 
                                        resultSet.getLong("time_posted_epoch")));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }
}
