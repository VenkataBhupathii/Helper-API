package com.help.help_backend.service;

import com.help.help_backend.entity.HelpRequest;
import com.help.help_backend.entity.User;
import com.help.help_backend.repository.HelpRequestRepository;
import com.help.help_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelpRequestService {

    @Autowired
    private HelpRequestRepository helpRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HelperService helperService;

    // Create HelpRequest with status 'PENDING'
    public HelpRequest createHelpRequest(User user, String description, double latitude, double longitude) {
        HelpRequest request = new HelpRequest();
        request.setDescription(description);
        request.setLatitude(latitude);
        request.setLongitude(longitude);
        request.setUser(user);
        request.setStatus(HelpRequest.HelpRequestStatus.PENDING); // Set status to PENDING when created

        helpRequestRepository.save(request);

        return request;
    }

    // Get all HelpRequests
    public List<HelpRequest> getAllHelpRequests() {
        return helpRequestRepository.findAll();
    }

    // Assign Helper to the HelpRequest
    public HelpRequest assignHelperToRequest(String requestId, String helperId) {
        HelpRequest request = helpRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Help request not found"));

        User helper = userRepository.findById(helperId)
                .orElseThrow(() -> new RuntimeException("Helper not found"));

        request.setHelper(helper);
        request.setStatus(HelpRequest.HelpRequestStatus.ACCEPTED); // Set status to ACCEPTED when helper is assigned
        helpRequestRepository.save(request);

        return request;
    }
}
