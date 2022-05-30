package com.huhoot.details;

import com.huhoot.admin.manage.student.ManageStudentService;
import com.huhoot.dto.GetAllParticipantsReq;
import com.huhoot.dto.GetChallengeParticipateInReq;
import com.huhoot.dto.StudentResponse;
import com.huhoot.dto.ChallengeResponse;
import com.huhoot.host.manage.challenge.ManageChallengeService;
import com.huhoot.host.manage.studentInChallenge.ManageStudentInChallengeService;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.organize.OrganizeService;
import com.huhoot.organize.StudentScoreResponse;
import com.huhoot.repository.StudentInChallengeRepository;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingConverter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("details")
public class DetailsController {
    private final ManageStudentService manageStudentService;

    private final OrganizeService organizeService;

    private final ManageStudentInChallengeService msicSer;

    private final VDataTablePagingConverter vDataTablePagingConverter;

    @GetMapping("/student")
    public ResponseEntity<StudentResponse> getStudentDetails(@RequestParam String username){
        return ResponseEntity.ok(manageStudentService.findOneByUsername(username)) ;

    }


    @PostMapping("/student/challenge-participate-in")
    public ResponseEntity<PageResponse<ChallengeResponse>> getChallengeParticipateIn(@RequestBody GetChallengeParticipateInReq req){

        Pageable pageable1 = vDataTablePagingConverter.toPageable(req);

        return ResponseEntity.ok(manageStudentService.findAllChallengeParticipateIn(req.getUsername(), pageable1)) ;

    }

    @PostMapping("/participants")
    public ResponseEntity<PageResponse<StudentInChallengeResponse>> getAllParticipants(@RequestBody GetAllParticipantsReq req){

        Pageable pageable1 = vDataTablePagingConverter.toPageable(req);

        return ResponseEntity.ok(msicSer.findAllParticipants(req.getChallengeId(), pageable1)) ;

    }
    /**
     * @param challengeId  {@link com.huhoot.model.Challenge} id
     * @param itemsPerPage size
     * @return List of {@link StudentScoreResponse}
     */
    @GetMapping("/challenge-report")
    public ResponseEntity<PageResponse<StudentScoreResponse>> getTopStudent(@RequestParam int challengeId,
                                                                            @RequestParam(defaultValue = "1") int page,
                                                                            @RequestParam(defaultValue = "20") int itemsPerPage) {

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        return ResponseEntity.ok(organizeService.getTopStudent(challengeId, pageable));
    }

}
