package org.example.liegptprojekt.controller;

import org.example.liegptprojekt.dto.MyResponse;
import org.example.liegptprojekt.service.OpenAiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lie")
@CrossOrigin(origins = "*")
public class LieController {

    private final OpenAiService service;

    public LieController(OpenAiService service) {
        this.service = service;
    }

    public record AskRequest(String text) {}

    @PostMapping("/ask")
    public MyResponse ask(@RequestBody AskRequest body) {
        return service.ask(body.text());
    }

    @GetMapping("/ping")
    public String ping() { return "ok"; }
}
