package com.arya.bot.repository;

import com.arya.bot.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatRepository extends JpaRepository<ChatMessage ,Long> {

    List<ChatMessage> findAllByOrderByTimestampDesc();
}
