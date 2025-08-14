package com.smooth.usage_example.bedrock_example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.List;
import java.util.Map;

@Service
public class BedrockService {

    private final BedrockRuntimeClient bedrockClient;
    private final ObjectMapper objectMapper;

    public BedrockService() {
        this.bedrockClient = BedrockRuntimeClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(ProfileCredentialsProvider.create("smooth"))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public String callClaude(String message) throws Exception {
        // Claude 요청 구조
        Map<String, Object> requestBody = Map.of(
                "anthropic_version", "bedrock-2023-05-31",
                "messages", List.of(Map.of(
                        "role", "user",
                        "content", List.of(Map.of(
                                "type", "text",
                                "text", message
                        ))
                )),
                "max_tokens", 100,
                "temperature", 0.7
        );

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        InvokeModelRequest request = InvokeModelRequest.builder()
                .modelId("anthropic.claude-3-haiku-20240307-v1:0")
                .body(SdkBytes.fromUtf8String(jsonBody))
                .build();

        InvokeModelResponse response = bedrockClient.invokeModel(request);

        String responseBody = response.body().asUtf8String();
        JsonNode jsonResponse = objectMapper.readTree(responseBody);

        return jsonResponse.get("content").get(0).get("text").asText();
    }
}
