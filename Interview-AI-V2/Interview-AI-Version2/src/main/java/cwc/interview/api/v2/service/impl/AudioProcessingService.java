package cwc.interview.api.v2.service.impl;


import lombok.RequiredArgsConstructor;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AudioProcessingService {
    private final RestTemplate restTemplate;

    public String transcribeAudio(byte[] audioBytes) {
        System.out.println("From Service Process: " + Arrays.toString(audioBytes));

        try {
            // Create WebSocket client
            WebSocketClient client = new WebSocketClient(new URI("ws://localhost:5000/audio")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("WebSocket opened: " + handshakedata);
                    // Send audio bytes in chunks to the Python WebSocket server
                    sendAudioInChunks(this, audioBytes, 1024 * 100); // 100 KB chunk size
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("Received message: " + message);
                    // Handle the response message from the Python server
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("WebSocket closed with exit code " + code + " additional info: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.err.println("WebSocket error: " + ex.getMessage());
                }
            };

            // Connect to the WebSocket server
            client.connectBlocking();

            // Wait for the response or close the connection after a certain time
            client.closeBlocking();

        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }

        // Return a placeholder transcription result
        return "Transcription result";
    }

    private void sendAudioInChunks(WebSocketClient client, byte[] audioBytes, int chunkSize) {
        int offset = 0;
        while (offset < audioBytes.length) {
            int end = Math.min(offset + chunkSize, audioBytes.length);
            byte[] chunk = Arrays.copyOfRange(audioBytes, offset, end);
            client.send(ByteBuffer.wrap(chunk));
            offset += chunkSize;
        }
        System.out.println("All audio chunks sent.");
    }

//    public String transcribeAudio(byte[] audioBytes) {
//        System.out.println("From Service Process: " + Arrays.toString(audioBytes));
//
//        try {
//            // Create WebSocket client
//            WebSocketClient client = new WebSocketClient(new URI("ws://localhost:5000/audio")) {
//                @Override
//                public void onOpen(ServerHandshake handshakedata) {
//                    System.out.println("WebSocket opened: " + handshakedata);
//                    // Send audio bytes to the Python WebSocket server
//                    this.send(ByteBuffer.wrap(audioBytes));
//                }
//
//                @Override
//                public void onMessage(String message) {
//                    System.out.println("Received message: " + message);
//                    // Handle the response message from the Python server
//                    // For example, you can log it or return it from this method
//                }
//
//                @Override
//                public void onClose(int code, String reason, boolean remote) {
//                    System.out.println("WebSocket closed with exit code " + code + " additional info: " + reason);
//                }
//
//                @Override
//                public void onError(Exception ex) {
//                    System.err.println("WebSocket error: " + ex.getMessage());
//                }
//            };
//
//            // Connect to the WebSocket server
//            client.connectBlocking();
//
//            // Wait for the response or close the connection after a certain time
//            client.closeBlocking();
//
//        } catch (URISyntaxException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Return a placeholder transcription result
//        return "Transcription result";
//    }

//    public String transcribeAudio(byte[] audioBytes) {
//        System.out.println("From Service Process" + Arrays.toString(audioBytes));
//        // Implement your transcription logic here
//        // For example, use a third-party service or library to process the audioBytes
//        return "Transcription result";
//    }


//    public String transcribeAudio(byte[] audioBytes) {
//        // Encode the audio data in Base64
//        String base64Audio = Base64.getEncoder().encodeToString(audioBytes);
//
//        // Prepare the JSON payload
//        Map<String, String> requestPayload = new HashMap<>();
//        requestPayload.put("audio_data", base64Audio);
//
//        String pythonServiceUrl = "http://localhost:5000/transcribe";
//
//        // Prepare the request
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestPayload, headers);
//
//        try {
//            // Send the request and get the response
//            ResponseEntity<String> response = restTemplate.exchange(
//                    pythonServiceUrl,
//                    HttpMethod.POST,
//                    requestEntity,
//                    String.class
//            );
//
//            // Handle the response
//            if (response.getStatusCode() == HttpStatus.OK) {
//                String transcribedText = response.getBody();
//                System.out.println("Transcribed text: " + transcribedText);
//                return transcribedText;
//            } else {
//                // Handle non-200 responses
//                System.err.println("Error: Received HTTP status code " + response.getStatusCode());
//                return "Error: " + response.getStatusCode();
//            }
//        } catch (Exception e) {
//            // Handle exceptions
//            System.err.println("Exception occurred while sending request: " + e.getMessage());
//            return "Error: Exception occurred";
//        }
//    }
}

//    public String transcribeAudio(byte[] audioBytes) {
//        // Encode the audio data in Base64
//        String base64Audio = Base64.getEncoder().encodeToString(audioBytes);
//
//        // Prepare the JSON payload
//        Map<String, String> requestPayload = new HashMap<>();
//        requestPayload.put("audio_data", base64Audio);
//
//        String pythonServiceUrl = "http://localhost:5000/transcribe";
//
//        // Prepare the request
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestPayload, headers);
//
//        try {
//            // Send the request and get the response
//            ResponseEntity<String> response = restTemplate.exchange(
//                    pythonServiceUrl,
//                    HttpMethod.POST,
//                    requestEntity,
//                    String.class
//            );
//
//            // Handle the response
//            if (response.getStatusCode() == HttpStatus.OK) {
//                String transcribedText = response.getBody();
//                System.out.println("Transcribed text: " + transcribedText);
//                return transcribedText;
//            } else {
//                // Handle non-200 responses
//                System.err.println("Error: Received HTTP status code " + response.getStatusCode());
//                return "Error: " + response.getStatusCode();
//            }
//        } catch (Exception e) {
//            // Handle exceptions
//            System.err.println("Exception occurred while sending request: " + e.getMessage());
//            return "Error: Exception occurred";
//        }
//    }

//    public String transcribeAudio(byte[] audioBytes) {
//        // Encode the audio data in Base64
//        String base64Audio = Base64.getEncoder().encodeToString(audioBytes);
//
//        // Prepare the JSON payload
//        Map<String, String> requestPayload = new HashMap<>();
//        requestPayload.put("audio_data", base64Audio);
//
//        String pythonServiceUrl = "http://localhost:5000/transcribe";
//
//        // Prepare the request
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestPayload, headers);
//
//        // Send the request and get the response
//        ResponseEntity<String> response = restTemplate.exchange(
//                pythonServiceUrl,
//                HttpMethod.POST,
//                requestEntity,
//                String.class
//        );
//
//        // Handle the response
//        if (response.getStatusCode() == HttpStatus.OK) {
//            String transcribeAudio = response.getBody();
//            System.out.println(transcribeAudio +";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
//            return transcribeAudio;
//        } else {
//            // Handle non-200 responses
//            return "Error: " + response.getStatusCode();
//        }
//    }

//    public String transcribeAudio(byte[] audioBytes) {
//        // Logging the received audio byte array
//        System.out.println("*******____________________**************___________******" + Arrays.toString(audioBytes));
//
//        String pythonServiceUrl = "http://localhost:5000/transcribe";
//
//        // Prepare the request
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        HttpEntity<byte[]> requestEntity = new HttpEntity<>(audioBytes, headers);
//
//        // Send the request and get the response
//        ResponseEntity<String> response = restTemplate.exchange(
//                pythonServiceUrl,
//                HttpMethod.POST,
//                requestEntity,
//                String.class
//        );
//
//        // Handle the response
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return response.getBody();
//        } else {
//            // Handle non-200 responses
//            return "Error: " + response.getStatusCode();
//        }
//    }


//        String url = "http://localhost:5000/receive-variable";
//
//        // Set headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        String variable = "Deendayal";
//        // Create the request body
//        String requestBody = "{\"variable\":\"" + variable + "\"}";
//
//        // Create the request entity
//        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
//
//        // Send the request
//        ResponseEntity<String> response = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                entity,
//                String.class
//        );
//
//        // Return the response body
//        String responseBody = response.getBody();
//        System.out.println(responseBody +"8888888888888888888888888888888888888");
//        return responseBody;
//    }
//
//    }
//        String pythonServiceUrl = "http://192.168.0.101:5000/stream-transcribe";
//




//    public String transcribeAudio(byte[] audioBytes) {
//        // Simulate audio transcription
//        System.out.println(audioBytes + "---------------------------------------------------------------");
//        String pythonServiceUrl = "http://localhost:5000/stream-transcribe";
//        //        // Make sure to set proper content type for binary data
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        HttpEntity<byte[]> requestEntity = new HttpEntity<>(audioBytes, headers);
//        ResponseEntity<String> response = restTemplate.exchange(pythonServiceUrl, HttpMethod.POST, requestEntity, String.class);
//        System.out.println(response.getBody());
////        return response.getBody();
//        return "Transcribed text from audio";
//    }


//    public String transcribeAudio(byte[] audioBytes) {
//        String pythonServiceUrl = "http://localhost:5000/stream-transcribe";
//        // Make sure to set proper content type for binary data
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        HttpEntity<byte[]> requestEntity = new HttpEntity<>(audioBytes, headers);
//        ResponseEntity<String> response = restTemplate.exchange(pythonServiceUrl, HttpMethod.POST, requestEntity, String.class);
//        return response.getBody();
//    }

//    public String transcribeAudio(byte[] audioBytes) {
//        String pythonServiceUrl = "http://localhost:5000/stream-transcribe";
//        // Make sure to set proper content type for binary data
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        HttpEntity<byte[]> requestEntity = new HttpEntity<>(audioBytes, headers);
//        ResponseEntity<String> response = restTemplate.exchange(pythonServiceUrl, HttpMethod.POST, requestEntity, String.class);
//        return response.getBody();
//    }



