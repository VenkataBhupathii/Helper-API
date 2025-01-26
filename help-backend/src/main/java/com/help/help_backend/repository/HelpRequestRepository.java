package com.help.help_backend.repository;

import com.help.help_backend.entity.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpRequestRepository extends JpaRepository<HelpRequest,String> {

}
