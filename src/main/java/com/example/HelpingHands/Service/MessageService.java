package com.example.HelpingHands.Service;

import com.example.HelpingHands.DTO.ConversationSummary;
import com.example.HelpingHands.DTO.MessageRequest;
import com.example.HelpingHands.DTO.MessageResponse;

import java.util.List;

public interface MessageService {

    MessageResponse sendMessage(String senderEmail, MessageRequest request);

    List<MessageResponse> getConversation(String userEmail, Long otherUserId);

    List<ConversationSummary> getConversations(String userEmail);
}
