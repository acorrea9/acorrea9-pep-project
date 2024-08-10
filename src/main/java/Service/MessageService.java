package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message) {
        if((message.getMessage_text().length() > 0) && (message.getMessage_text().length() <= 255)) {
            return messageDAO.createMessage(message);
        }
        return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessage(int id) {
        Message message = getMessageById(id);
        if(message != null) {
            if(messageDAO.deleteMessage(id)) {
                return message;
            }
        }
        return null;
    }

    public Message updateMessage(int id, Message message) {
        if((getMessageById(id) != null) && (message.getMessage_text().length() > 0) && (message.getMessage_text().length() <= 255)) {
            if(messageDAO.updateMessage(id, message)) {
                return getMessageById(id);
            }
        }
        return null;
    }
}
