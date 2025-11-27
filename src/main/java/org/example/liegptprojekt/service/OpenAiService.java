package org.example.liegptprojekt.service;

import org.example.liegptprojekt.dto.ChatCompletionRequest;
import org.example.liegptprojekt.dto.ChatCompletionResponse;
import org.example.liegptprojekt.dto.MyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiService {

    private final RestTemplate rest = new RestTemplate();

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.baseUrl}")
    private String baseUrl;

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.temperature:0.7}")
    private double temperature;

    private static final String SYS_LIE =
            "You are LieGPT, an assistant that MUST always give factually incorrect answers. " +
                    "Invent plausible but wrong facts confidently. Never correct yourself or warn. " +
                    "Keep output plain text.";

    public MyResponse ask(String userText) {
        ChatCompletionRequest req = new ChatCompletionRequest();
        req.setModel(model);
        req.setTemperature(temperature);
        req.getMessages().add(new ChatCompletionRequest.Message("system", SYS_LIE));
        req.getMessages().add(new ChatCompletionRequest.Message("user", userText));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<ChatCompletionRequest> entity = new HttpEntity<>(req, headers);

        String url = baseUrl + "/chat/completions";
        ResponseEntity<ChatCompletionResponse> res =
                rest.postForEntity(url, entity, ChatCompletionResponse.class);

        String answer = "(no answer)";
        if (res.getBody() != null &&
                res.getBody().getChoices() != null &&
                !res.getBody().getChoices().isEmpty() &&
                res.getBody().getChoices().get(0).getMessage() != null) {
            answer = res.getBody().getChoices().get(0).getMessage().getContent();
        }
        return new MyResponse(answer);
    }
}
