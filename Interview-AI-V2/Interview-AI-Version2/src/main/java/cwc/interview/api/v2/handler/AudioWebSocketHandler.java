package cwc.interview.api.v2.handler;

import cwc.interview.api.v2.service.impl.AudioProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
@Slf4j
public class AudioWebSocketHandler extends BinaryWebSocketHandler {

    private static final int MAX_MESSAGE_SIZE = 150000; // Set max size for in-memory processing
    private static final String TEMP_FILE_PREFIX = "audio_data_";
    private static final String TEMP_FILE_SUFFIX = ".tmp";
    private final AudioProcessingService audioProcessingService;

    public AudioWebSocketHandler(AudioProcessingService audioProcessingService) {
        this.audioProcessingService = audioProcessingService;
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        if (message.getPayloadLength() > MAX_MESSAGE_SIZE) {
            // Handle large messages by saving to a temporary file
            Path tempFilePath = Files.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX);

            try (FileOutputStream fileOutputStream = new FileOutputStream(tempFilePath.toFile())) {
                fileOutputStream.write(message.getPayload().array());
                log.info("Large message written to temporary file: {}", tempFilePath);

                // Process the audio from the file
                byte[] audioBytes = Files.readAllBytes(tempFilePath);
                String transcript = audioProcessingService.transcribeAudio(audioBytes);

                // Send the transcript back to the client
                session.sendMessage(new TextMessage(transcript));
            } catch (IOException e) {
                log.error("Failed to process large message", e);
                session.close(CloseStatus.SERVER_ERROR);
            } finally {
                // Clean up the temporary file
                Files.deleteIfExists(tempFilePath);
            }
        } else {
            // Process small messages in-memory
            byte[] audioBytes = message.getPayload().array();
            String transcript = audioProcessingService.transcribeAudio(audioBytes);

            // Send the transcript back to the client
            session.sendMessage(new TextMessage(transcript));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.setBinaryMessageSizeLimit(MAX_MESSAGE_SIZE); // Set buffer size
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error", exception);
        session.close(CloseStatus.SERVER_ERROR);
    }

//    @Override
//    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
//        System.out.println("From Handler -----------------------------------------------------");
//
//        if (message.getPayloadLength() > MAX_MESSAGE_SIZE) {
//            // Handle large messages by saving to a temporary file
//            String tempFileName = TEMP_FILE_PREFIX + session.getId() + TEMP_FILE_SUFFIX;
//            Path tempFilePath = Paths.get(tempFileName);
//
//            try (FileOutputStream fileOutputStream = new FileOutputStream(tempFilePath.toFile())) {
//                fileOutputStream.write(message.getPayload().array());
//                log.info("Large message written to temporary file: {}", tempFilePath);
//
//                // Process the audio from the file
//                byte[] audioBytes = Files.readAllBytes(tempFilePath);
//                String transcript = audioProcessingService.transcribeAudio(audioBytes);
//
//                // Send the transcript back to the client
//                session.sendMessage(new TextMessage(transcript));
//            } catch (IOException e) {
//                log.error("Failed to process large message", e);
//                session.close(CloseStatus.SERVER_ERROR);
//            } finally {
//                // Clean up the temporary file
//                Files.deleteIfExists(tempFilePath);
//            }
//        } else {
//            // Process small messages in-memory
//            byte[] audioBytes = message.getPayload().array();
//            System.out.println("From Handler " + Arrays.toString(audioBytes));
//
//            // Transcribe the audio data
//            String transcript = audioProcessingService.transcribeAudio(audioBytes);
//
//            // Send the transcript back to the client
//            session.sendMessage(new TextMessage(transcript));
//        }
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        session.setBinaryMessageSizeLimit(MAX_MESSAGE_SIZE); // Set buffer size
//        super.afterConnectionEstablished(session);
//    }
//
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        log.error("WebSocket transport error", exception);
//        session.close(CloseStatus.SERVER_ERROR);
//    }

//    @Override
//    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
//        System.out.println("From Handler -----------------------------------------------------");
//        byte[] audioBytes = message.getPayload().array();
//        System.out.println("From Handler " + Arrays.toString(audioBytes));
//        // Transcribe the audio data (this should be implemented in your service)
//        String transcript = audioProcessingService.transcribeAudio(audioBytes);
//
//        // Send the transcript back to the client
//        session.sendMessage(new TextMessage(transcript));
//    }
}

