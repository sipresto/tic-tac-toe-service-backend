package be.kdg.ipj3.infrastructure.ai;

import be.kdg.ipj3.infrastructure.ai.dto.AiGameStateDto;
import be.kdg.ipj3.infrastructure.ai.dto.AiResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AiClient {
    private final RestClient restClient;

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    public AiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public AiResponseDto getAiMove(AiGameStateDto gameState) {
        return restClient
                .post()
                .uri(aiServiceUrl)
                .body(gameState)
                .retrieve()
                .body(AiResponseDto.class);
    }
}
