package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
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
}
