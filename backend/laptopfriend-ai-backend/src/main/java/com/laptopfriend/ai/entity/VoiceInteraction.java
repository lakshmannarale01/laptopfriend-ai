package com.laptopfriend.ai.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "voice_interactions")
public class VoiceInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 5000)
    private String originalVoiceText;   // raw voice input (English / marathi / hindi)

    @Column(nullable = false , length = 5000)
    private String translatedText;     // English Translation

    @Column(nullable = false , length = 5000)
    private String aiResponse;         //AI's Friend like  reply

    @Column(nullable = false)
    private String commandExecuted;    // (Laptop command (e.g "open-notpad")

    @Column(nullable = false)
    private LocalDateTime timestamp;          // Timestamp of interaction

    @Column(nullable = false)
    private String languageDetected;           // Language of the voice input (English / marathi / hindi)

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }


    public VoiceInteraction( String originalVoiceText, String translatedText,
                             String aiResponse, String commandExecuted,
                              String languageDetected) {
        this.originalVoiceText = originalVoiceText;
        this.translatedText = translatedText;
        this.aiResponse = aiResponse;
        this.commandExecuted = commandExecuted;
        this.languageDetected = languageDetected;
    }
}
