package com.EventApplication.EventApplication.service;


import com.EventApplication.EventApplication.model.Event;
import com.EventApplication.EventApplication.repositry.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;
import java.util.UUID;

@Service
@SessionScope
public class ChatServiceAi {
    private final ChatClient chatClient; // Använder AI's chat client för att integrera med OpenAi
    private final String conversationId;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public ChatServiceAi(ChatModel chatModel, ChatMemory chatMemory, EventRepository eventRepository) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
        this.conversationId = UUID.randomUUID().toString();
        this.eventRepository = eventRepository;
    }

    //Chat metoden tar användarens input och returnerar AI's output
    public String chat(String prompt) throws JsonProcessingException {
        // Hämta alla events från databasen
        List<Event> events = eventRepository.findAll();
        //gör om listan till JSON så AI kan läsa den
        String eventListJson = objectMapper.writeValueAsString(events);



        String conversationKey = conversationId  + "-session";

        String systemPrompt = """
            Du är en hjälpsam event-assistent som ger rekommendationer till användaren
            baserat på deras intressen, humör och preferenser.
            Du får en lista över tillgängliga event från systemet, rekommendera endast dessa.
            Om inget event passar användarens beskrivning, säg det vänligt.
            """;

        String userPrompt = """
        Här är en lista över tillgängliga event:
        %s

        Användarens fråga:
        %s
        """.formatted(eventListJson, prompt);


        System.out.println(" (Key: " + conversationKey + ")");
        System.out.println("ChatService.chat() kallades med prompt: " + prompt);

        String response = chatClient.prompt() //starta en ny prompt (ett samtal)
                .system(systemPrompt)
                .user(userMessage -> userMessage.text(userPrompt)) // lägger till användarens meddelande
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationKey))
                .call() // skickar prompten till den underliggande modellen t.ex. OpenAi
                .content(); // hämtar bara textinnehållet från svaret

        System.out.println(response);
        return response;

    }
}
