package com.laptopfriend.ai.controller;

import com.laptopfriend.ai.entity.VoiceInteraction;
import com.laptopfriend.ai.repository.VoiceInteractionRepository;
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

    @PostMapping("/command")
    @Operation(summary = "Process voice command" , description = "Store the voice Interaction + execute command")
    public ResponseEntity<Map<String , Object>>processVoiceCommand(@RequestBody Map<String , String> request){
        String originalText = request.get("originalVoiceText");
        String translatedText = request.get("translatedText");
        String language = request.get("language");
        // TODO : Real AI + Laptop Control here
        String aiResponse = "Understood '"+translatedText+"' ! Executing .....(demo)";
        String commandExecuted = "demo_"+translatedText;
        VoiceInteraction interaction = new VoiceInteraction(originalText , translatedText , aiResponse , commandExecuted , language);
        repository.save(interaction);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", aiResponse,
                "interactionId", interaction.getId(),
                "storedCount", repository.count()
        ));
    }

    @GetMapping("/history")
    @Operation(summary = "Get recent voice history")
    public List<VoiceInteraction> getRecentHistory(){
        return repository.findTop10ByOrderByTimestampDesc();
    }



}
