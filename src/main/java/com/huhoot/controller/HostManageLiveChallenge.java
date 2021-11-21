package com.huhoot.controller;

import com.huhoot.dto.StudentInChallengeResponse;
import com.huhoot.model.Admin;
import com.huhoot.service.HostService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("host")
public class HostManageLiveChallenge {

private final HostService hostService;

    public HostManageLiveChallenge(HostService hostService) {
        this.hostService = hostService;
    }


    @GetMapping("challenge/live")
    public ResponseEntity<List<StudentInChallengeResponse>> openChallenge(@RequestParam int id) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostService.openChallenge(userDetails, id));
    }

}
