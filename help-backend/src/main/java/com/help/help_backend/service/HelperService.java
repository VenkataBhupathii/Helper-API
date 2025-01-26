package com.help.help_backend.service;

import com.help.help_backend.repository.UserRepository;
import com.help.help_backend.entity.User;
import com.help.help_backend.util.DistanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HelperService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService userService;  // Access UserService for distance calculation


    public List<User> findAvailableHelpers(double latitude, double longitude, double radius) {
        List<User> allHelpers = userRepository.findByRolesContaining(User.Role.HELPER);
        List<User> nearbyHelpers = new ArrayList<>();

        for (User helper : allHelpers) {
            double distance = DistanceCalculator.calculateDistance(latitude, longitude, helper.getLatitude(), helper.getLongitude());
            if (distance <= radius && helper.isAvailable()) {
                nearbyHelpers.add(helper);
            }
        }

        return nearbyHelpers;
    }
}
