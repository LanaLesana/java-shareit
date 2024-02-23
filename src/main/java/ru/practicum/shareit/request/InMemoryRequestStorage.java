package ru.practicum.shareit.request;

import org.apache.coyote.Request;

import java.util.HashMap;

public class InMemoryRequestStorage {
    private HashMap<Integer, Request> requestHashMap = new HashMap<>();
    private Integer generatedRequestId;
}
