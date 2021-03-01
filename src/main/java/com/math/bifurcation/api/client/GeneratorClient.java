package com.math.bifurcation.api.client;

import lombok.Getter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

/**
 * @author Leonid Cheremshantsev
 */
public class GeneratorClient {

    public static final String API_URL = "wss://echo.websocket.org";

    @Getter
    private final WebSocketSession clientSession;

    public GeneratorClient() throws ExecutionException, InterruptedException {
        this.clientSession = buildSession();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        GeneratorClient generatorClient = new GeneratorClient();
        generatorClient.clientSession.sendMessage(new TextMessage("Hello!"));
        Thread.sleep(5000);
    }

    private WebSocketSession buildSession() throws ExecutionException, InterruptedException {
        ListenableFuture<WebSocketSession> futureSession = new StandardWebSocketClient().doHandshake(
                new GeneratorHandler(),
                new WebSocketHttpHeaders(),
                URI.create(API_URL)
        );
        return futureSession.get();
    }

    /**
     * @author Leonid Cheremshantsev
     */
    private static class GeneratorHandler extends TextWebSocketHandler {

        @SuppressWarnings("NullableProblems")
        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) {
            System.out.println(message.getPayload());
        }
    }
}
