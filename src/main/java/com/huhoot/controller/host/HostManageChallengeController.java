package com.huhoot.controller.host;

import com.huhoot.dto.ChallengeAddRequest;
import com.huhoot.dto.ChallengeResponse;
import com.huhoot.dto.ChallengeUpdateRequest;
import com.huhoot.dto.PageResponse;
import com.huhoot.exception.NotYourOwnException;
import com.huhoot.functional.impl.CheckOwnerChallenge;
import com.huhoot.model.Admin;
import com.huhoot.service.HostManageService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("host")
public class HostManageChallengeController {
    private final HostManageService hostService;

    private final CheckOwnerChallenge checkOwnerChallenge;

    public HostManageChallengeController(HostManageService hostService, CheckOwnerChallenge checkOwnerChallenge) {
        this.hostService = hostService;
        this.checkOwnerChallenge = checkOwnerChallenge;
    }

    @GetMapping("/challenge")
    public ResponseEntity<PageResponse<ChallengeResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "12") int size,
                                                                   @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                   @RequestParam(defaultValue = "DESC") String direction) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        return ResponseEntity.ok(hostService.findAllOwnChallenge(userDetails, pageable));
    }

    @GetMapping("/challenge/details")
    public ResponseEntity<ChallengeResponse> getDetails(@RequestParam int id) throws NotYourOwnException, NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(hostService.getOneOwnChallengeDetailsById(userDetails, id, checkOwnerChallenge));

    }

    @GetMapping("/challenge/search")
    public ResponseEntity<PageResponse<ChallengeResponse>> search(@Length(min = 1, max = 15) @RequestParam String title,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "12") int size,
                                                                  @RequestParam(defaultValue = "createdDate") String sortBy,
                                                                  @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(hostService.searchOwnChallengeByTitle(userDetails, title, pageable));

    }

    @PostMapping("/challenge")
    public void add(@Valid @RequestBody ChallengeAddRequest request) throws IOException {

        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        hostService.addOneChallenge(userDetails, request);

    }

    @PutMapping("/challenge")
    public void update(@Valid @RequestBody ChallengeUpdateRequest request) throws NotYourOwnException, NotFoundException {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.updateOneChallenge(userDetails, request, checkOwnerChallenge);

    }

    @DeleteMapping("/challenge")
    public void delete(@RequestBody List<Integer> ids) {
        Admin userDetails = (Admin) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        hostService.deleteManyChallenge(userDetails, ids);

    }

}
