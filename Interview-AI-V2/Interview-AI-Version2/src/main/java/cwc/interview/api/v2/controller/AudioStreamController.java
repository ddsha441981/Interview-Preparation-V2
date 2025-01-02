package cwc.interview.api.v2.controller;

import cwc.interview.api.v2.service.impl.AudioProcessingService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;

@Controller
public class AudioStreamController {

//    private final AudioProcessingService audioProcessingService;
//    private final SimpMessagingTemplate messagingTemplate;
//
//    public AudioStreamController(AudioProcessingService audioProcessingService, SimpMessagingTemplate messagingTemplate) {
//        this.audioProcessingService = audioProcessingService;
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @MessageMapping("/audio") // STOMP endpoint
//    @SendTo("/topic/transcript") // Broadcasts the result to the /topic/transcript topic
//    public byte[] handleAudio(@RequestBody byte[] audioBytes) throws Exception {
//        System.out.println("******************************************************************************************************");
//        try {
//            // Process audio data and get the transcript
//            System.out.println(Arrays.toString(audioBytes));
//            return audioBytes;
////            return audioProcessingService.transcribeAudio(audioBytes);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error processing audio data".getBytes();
//        }
//    }

//    @MessageMapping("/audio") // STOMP endpoint
//    @SendTo("/topic/transcript") // Broadcasts the result to the /topic/transcript topic
//    public String handleAudio(String base64AudioData) throws Exception {
//        System.out.println("******************************************************************************************************");
//        try {
//            // Decode Base64 string to byte array
//            byte[] audioBytes = Base64.getDecoder().decode(base64AudioData);
//
//            // Process audio data and get the transcript
//            return audioProcessingService.transcribeAudio(audioBytes);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error processing audio data";
//        }
//    }


//    @MessageMapping("/audio") // STOMP endpoint
//    @SendTo("/topic/transcript") // Broadcasts the result to the /topic/transcript topic
//    public String handleAudio(String base64AudioData) throws Exception {
//        System.out.println("******************************************************************************************************");
//        try {
//            // Decode Base64 string to byte array
//            byte[] audioBytes = Base64.getDecoder().decode(base64AudioData);
//
//            // Process audio data and get the transcript
//            return audioProcessingService.transcribeAudio(audioBytes);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error processing audio data";
//        }
//    }

//    @MessageMapping("/audio")
//    @SendTo("/topic/transcript")
//    public String handleAudioMessage(byte[] audioBytes) {
//        try {
//            String transcript = audioProcessingService.transcribeAudio(audioBytes);
//            System.out.println(transcript);
//            return transcript;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error processing audio";
//        }
//    }

//    @MessageMapping("/audio")
//    public void handleAudioMessage(byte[] audioBytes) {
//        try {
//            String transcript = audioProcessingService.transcribeAudio(audioBytes);
//            System.out.println(transcript);
//            messagingTemplate.convertAndSend("/topic/transcript", transcript); // Use messagingTemplate
//        } catch (Exception e) {
//            e.printStackTrace();
//            messagingTemplate.convertAndSend("/topic/transcript", "Error processing audio");
//        }
//    }
}