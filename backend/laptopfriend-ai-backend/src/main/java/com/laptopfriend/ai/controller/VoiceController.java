package com.laptopfriend.ai.controller;

import com.laptopfriend.ai.entity.VoiceInteraction;
import com.laptopfriend.ai.repository.VoiceInteractionRepository;
import com.laptopfriend.ai.service.CommandExecutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/voice")
@Tag(name = "Voice AI Controller", description = "Laptop control via voice")
@CrossOrigin(origins = "http://localhost:3000")  // React frontend
public class VoiceController {
    private final VoiceInteractionRepository repository;
    private final CommandExecutorService commandExecutor;

    @PostMapping("/command")
    @Operation(summary = "Process voice command" , description = "Store the voice Interaction + execute command")
    public ResponseEntity<Map<String , Object>>processVoiceCommand(@RequestBody Map<String , String> request){
        String originalText = request.get("originalVoiceText");
        String translatedText = request.get("translatedText");
        String language = request.get("language");
        // TODO : Real Treanslation and AI here
        String aiResponse = "ठीक आहे! मी '" + translatedText + "' करतोय...";  // Hindi response
        String commandExecuted = "demo_"+translatedText;
        VoiceInteraction interaction = new VoiceInteraction(originalText , translatedText , aiResponse , null , language);
       // Execute command
        String commandResult = commandExecutor.executeCommand(translatedText,interaction);
        interaction.setAiResponse(aiResponse+"\n"+commandResult);
        repository.save(interaction);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "aiResponse", interaction.getAiResponse(),
                "commandExecuted", interaction.getCommandExecuted(),
                "interactionId", interaction.getId()
        ));
    }

    @GetMapping("/history")
    @Operation(summary = "Get recent voice history")
    public List<VoiceInteraction> getRecentHistory(){
        return repository.findTop10ByOrderByTimestampDesc();
    }



}
