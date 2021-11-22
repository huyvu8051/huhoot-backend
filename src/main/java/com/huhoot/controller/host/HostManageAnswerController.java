package com.huhoot.controller.host;

import com.huhoot.dto.AnswerAddRequest;
import com.huhoot.dto.AnswerResponse;
import com.huhoot.dto.AnswerUpdateRequest;
import com.huhoot.model.Admin;
import com.huhoot.service.HostManageService;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("host")
public class HostManageAnswerController {

    private final HostManageService hostService;

    public HostManageAnswerController(HostManageService hostService) {
        this.hostService = hostService;
    }

    @GetMapping("/answer")
    public ResponseEntity<List<AnswerResponse>> findAll(@RequestParam int questionId) {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(hostService.findAllAnswerByQuestionId(userDetails, questionId));

    }

    @GetMapping("/answer/details")
    public ResponseEntity<AnswerResponse> getDetails(@RequestParam int answerId) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(hostService.getOneAnswerDetailsById(userDetails, answerId));

    }

    @PostMapping("/answer")
    public void add(@Valid @RequestBody AnswerAddRequest request) throws Exception {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        hostService.addOneAnswer(userDetails, request);

    }

    @PutMapping("/answer")
    public void update(@Valid @RequestBody AnswerUpdateRequest request) throws NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.updateOneAnswer(userDetails, request);
    }

    @DeleteMapping("/answer")
    public void delete(@RequestBody List<Integer> ids) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.deleteManyAnswer(userDetails, ids);

    }
}
