package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.DTO.MessageDTO;
import tn.esprit.se.pispring.Service.ChatExceptions.ChatAlreadyExistException;
import tn.esprit.se.pispring.Service.ChatExceptions.ChatNotFoundException;
import tn.esprit.se.pispring.Service.ChatExceptions.NoChatExistsInTheRepository;
import tn.esprit.se.pispring.entities.Chat;
import tn.esprit.se.pispring.entities.Message;

import java.util.HashSet;
import java.util.List;

public interface ChatService {

    public Chat addChat(Chat chat) throws ChatAlreadyExistException;

    List<Chat> findallchats() throws NoChatExistsInTheRepository;

    Chat getById(int id)  throws ChatNotFoundException;

    HashSet<Chat> getChatByFirstUserName(String username)  throws ChatNotFoundException;

    HashSet<Chat> getChatBySecondUserName(String username)  throws ChatNotFoundException;

    HashSet<Chat> getChatByFirstUserNameOrSecondUserName(String username)  throws ChatNotFoundException;

    HashSet<Chat> getChatByFirstUserNameAndSecondUserName(String firstUserName, String secondUserName)  throws ChatNotFoundException;

    Chat addMessage(Message add, int chatId)  throws ChatNotFoundException;

    Message addMessage2(MessageDTO message) throws Exception;

    List<Message> getAllMessagesInChat(int chatId) throws NoChatExistsInTheRepository;
}
