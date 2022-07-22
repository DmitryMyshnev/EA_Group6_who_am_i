package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.TimeoutException;
import com.eleks.academy.whoami.model.request.PlayersAnswer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class PersistentPlayer implements SynchronousPlayer {

    private final String id;
    private String name;
    @Getter
    private String character;
    @JsonIgnore
    private boolean guessing;
    @JsonIgnore
    private boolean isLeft;
    @JsonIgnore
    private final Queue<String> answerQueue = new ConcurrentLinkedQueue<>();

    public PersistentPlayer(String name, String id) {
        this.name =name;
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public SynchronousPlayer setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public void setCharacter(String character) {
        this.character = character;
    }

    @Override
    public boolean isGuessing() {
        return this.guessing;
    }

    @Override
    public void setAnswer(String answer, boolean guessing) {
        answerQueue.add(answer);
        this.guessing = guessing;
    }

    @Override
    public void markAsLeft() {
        this.isLeft = true;
    }

    @Override
    @JsonIgnore
    public CompletableFuture<String> getCurrentQuestion(long limit, TimeUnit unit) {
        return CompletableFuture
                .supplyAsync(() -> waitAnswer(limit, unit));
    }

    @Override
    @JsonIgnore
    public CompletableFuture<PlayersAnswer> getCurrentAnswer(long limit, TimeUnit unit) {
        return CompletableFuture
                .supplyAsync(() -> {
                    var answer = waitAnswer(limit, unit);
                    return PlayersAnswer.valueOf(answer);
                });
    }

    private String waitAnswer(long limit, TimeUnit unit) throws TimeoutException {
        var currentTime = System.currentTimeMillis();
        while (answerQueue.isEmpty()) {
            if (System.currentTimeMillis() - currentTime >= unit.toMillis(limit)) {
                throw new TimeoutException();
            }
            if (isLeft) {
                return GameLoop.MISSING_QUESTION;
            }
        }
        return answerQueue.poll();
    }
}
