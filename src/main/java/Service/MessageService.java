package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

/**
 * The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
 * persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
 * SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
 * actions undertaken by the API to a logging file.
 */
public class MessageService {
    private MessageDAO messageDAO;

    /**
     * no-args constructor for creating a new MessageService with a new MessageDAO.
     */
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    /**
     * Uses the MessageDAO to insert a message to the database.
     * @param message a message object.
     * @return a message with a message_id if it was successfully inserted, null if it was not successfully inserted
     */
    public Message createMessage(Message message) {
        if((message.getMessage_text().length() > 0) && (message.getMessage_text().length() <= 255)) {
            return messageDAO.createMessage(message);
        }
        return null;
    }

    /**
     * Uses the MessageDAO to retrieve a list of all messages.
     * @return all messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Uses the MessageDAO to retrieve a message by its ID from the database.
     * @param id the message_id.
     * @return a message with the specific message_id if successful, null if not successfully found
     */
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    /**
     * Uses the MessageDAO to delete a message by its ID from the database.
     * @param id the message_id.
     * @return the deleted message if successful, null if not successfully deleted
     */
    public Message deleteMessage(int id) {
        Message message = getMessageById(id);
        if(message != null) {
            if(messageDAO.deleteMessage(id)) {
                return message;
            }
        }
        return null;
    }

    /**
     * Uses the MessageDAO to update a message by its ID from the database.
     * @param id the message_id.
     * @return the new updated message if successful, null if not successfully updated
     */
    public Message updateMessage(int id, Message message) {
        if((getMessageById(id) != null) && (message.getMessage_text().length() > 0) && (message.getMessage_text().length() <= 255)) {
            if(messageDAO.updateMessage(id, message)) {
                return getMessageById(id);
            }
        }
        return null;
    }

    /**
     * Uses the MessageDAO to retrieve a list of all messages written by a particular user.
     * @param id the account_id.
     * @return all messages of a particular user
     */
    public List<Message> getAllMessagesByAccountId(int id) {
        return messageDAO.getAllMessagesByAccountId(id);
    }
}
