package com.lab2.quiz.infrastructure.persistence;

import com.lab2.quiz.application.port.FeedbackRepository;
import com.lab2.quiz.domain.Feedback;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

// Хранилище сообщений в памяти. При переполнении удаляется самое старое сообщение.
public class InMemoryFeedbackRepository implements FeedbackRepository {

    private final Deque<Feedback> storage = new ArrayDeque<>();
    private final int capacity;

    public InMemoryFeedbackRepository(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public synchronized void save(Feedback feedback) {
        storage.addLast(feedback);
        if (storage.size() > capacity) {
            storage.removeFirst();
        }
    }

    @Override
    public synchronized List<Feedback> findAll() {
        return new ArrayList<>(storage);
    }
}
