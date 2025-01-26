package com.help.help_backend.controller;

import com.help.help_backend.entity.User;
import com.help.help_backend.dto.HelpRequestDto;
import com.help.help_backend.entity.HelpRequest;
import com.help.help_backend.service.HelpRequestService;
import com.help.help_backend.service.HelperService;
import com.help.help_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class HelpRequestController {

    @Autowired
    private HelpRequestService helpRequestService;

    @Autowired
    private HelperService helperService;

    @Autowired
    private AuthService userService;

    @PostMapping("/create")
    public ResponseEntity<HelpRequest> createHelpRequest(@RequestBody HelpRequestDto helpRequestDto, @RequestHeader("Authorization") String token) {
        User user = userService.getUserFromToken(token);  // Extract user from token
        HelpRequest request = helpRequestService.createHelpRequest(user, helpRequestDto.getDescription(), helpRequestDto.getLatitude(), helpRequestDto.getLongitude());
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<User>> getNearbyHelpers(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double radius) {
        List<User> helpers = helperService.findAvailableHelpers(latitude, longitude, radius);  // Pass radius for filtering helpers
        return ResponseEntity.ok(helpers);
    }

    @PostMapping("/assign/{requestId}/{helperId}")
    public ResponseEntity<HelpRequest> assignHelperToRequest(@PathVariable String requestId, @PathVariable String helperId) {
        HelpRequest request = helpRequestService.assignHelperToRequest(requestId, helperId);
        return ResponseEntity.ok(request);
    }
}
