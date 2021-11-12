package com.huhoot.controller;

import com.huhoot.dto.*;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.model.Admin;
import com.huhoot.service.HostService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("host")
public class HostManageAnswerController {

    @Autowired
    private HostService hostService;

    @GetMapping("/answer")
    public ResponseEntity<List<AnswerResponse>> findAll(@RequestParam int questionId){

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostService.findAllAnswerByQuestionId(userDetails, questionId));

    }
    @GetMapping("/answer/details")
    public ResponseEntity<AnswerResponse> getDetails(@RequestParam int answerId) throws NotYourOwnException, NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(hostService.getOneAnswerDetailsById(userDetails, answerId));

    }
    @PostMapping("/answer")
    public ResponseEntity<?> add(@Valid @RequestBody AnswerAddRequest request) throws IOException, NotFoundException {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        hostService.addOneAnswer(userDetails, request);

        return ResponseEntity.ok(null);

    }

    @PutMapping("/answer")
    public ResponseEntity<?> update(@Valid @RequestBody AnswerUpdateRequest request) throws NotYourOwnException, NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.updateOneAnswer(userDetails, request);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/answer")
    public ResponseEntity<?> delete(@RequestBody List<Integer> ids) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.deleteManyAnswer(userDetails, ids);

        return ResponseEntity.ok(null);

    }
}
