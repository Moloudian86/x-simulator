package LogicController;

import model.ChatMessage;
import model.MessageStatus;
import model.User;
import repository.MessageRepository;

import java.util.List;

public class MessageService {
    private static final MessageService instance = new MessageService();
    private final MessageRepository messageRepository = new MessageRepository();

    public static MessageService getInstance() {
        return instance;
    }

    public ChatMessage sendMessage(User sender, User receiver, String content) {
        ChatMessage message = new ChatMessage(sender, receiver, content);

        boolean result = messageRepository.add(message);
        if(result){
            return messageRepository.findById(message.getId());
        }
        return null;
    }


    public List<ChatMessage> getConversation(User user1, User user2){
        return messageRepository.getConversation(user1.getId(), user2.getId());
    }


    public void markMessagesAsSeen(User currentUser, User otherUser){
        List<ChatMessage> messages = messageRepository.getConversation(currentUser.getId(), otherUser.getId());
        for(ChatMessage message : messages){
            if(message.getReceiver().getId() == currentUser.getId()){
                if(message.getStatus() != MessageStatus.SEEN){
                    message.setStatus(MessageStatus.SEEN);
                    messageRepository.update(message.getId(), message);
                }
            }
        }
    }

    public void deleteMessage(int messageId){
        messageRepository.remove(messageId);
    }
}