package io.github.soundbar91.aipipeline.domain.term.controller;

import io.github.soundbar91.aipipeline.domain.term.dto.response.TermResponse;
import io.github.soundbar91.aipipeline.domain.term.service.TermService;
import io.github.soundbar91.aipipeline.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/terms")
public class TermController {

    private final TermService termService;

    public TermController(TermService termService) {
        this.termService = termService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TermResponse>>> getTerms() {
        return ResponseEntity.ok(ApiResponse.ok(termService.getActiveTerms()));
    }
}
