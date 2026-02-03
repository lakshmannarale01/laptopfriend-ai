package com.laptopfriend.ai.repository;

import com.laptopfriend.ai.entity.VoiceInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoiceInteractionRepository extends JpaRepository<VoiceInteraction, Long> {

    // Find recent Interaction for AI context
    List<VoiceInteraction>findTop10ByOrderByTimestampDesc();

    // Search By voice text(for learning)
    List<VoiceInteraction> findByOriginalVoiceTextContainingIgnoreCase(String text);
}
