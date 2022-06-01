package com.huhoot.details;

import com.huhoot.admin.manage.host.HostResponse;
import com.huhoot.admin.manage.host.ManageHostService;
import com.huhoot.admin.manage.student.ManageStudentService;
import com.huhoot.dto.*;
import com.huhoot.host.manage.challenge.ManageChallengeService;
import com.huhoot.host.manage.studentInChallenge.ManageStudentInChallengeService;
import com.huhoot.host.manage.studentInChallenge.StudentInChallengeResponse;
import com.huhoot.organize.OrganizeService;
import com.huhoot.organize.StudentScoreResponse;
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

    private final ManageChallengeService challengeService;


    private final ManageHostService manageHostService;


    @PostMapping("/student")
    public ResponseEntity<GetStudentDetailsRes> getStudentDetails(@RequestBody GetStudentDetailsReq req) {
        StudentResponse student = manageStudentService.findOneByUsername(req.getUsername());
        Pageable pageable1 = vDataTablePagingConverter.toPageable(req);
        PageResponse<ChallengeResponse> listChallenge = manageStudentService.findAllChallengeParticipateIn(req.getUsername(), pageable1);
        return ResponseEntity.ok(GetStudentDetailsRes.builder()
                        .studentDetails(student)
                        .listChallenge(listChallenge)
                .build());

    }

    @PostMapping("/admin")
    public ResponseEntity<GetAdminDetailsRes> getAdminDetails(@RequestBody GetAdminDetailsReq req) {
        HostResponse host = manageHostService.findOneHostResponseByUsername(req.getUsername());
        Pageable pageable1 = vDataTablePagingConverter.toPageable(req);
        PageResponse<ChallengeResponse> listChallenge = challengeService.findAllOwnChallenge(host.getId(), pageable1);

        return ResponseEntity.ok(GetAdminDetailsRes.builder()
                        .adminDetails(host)
                        .listChallenge(listChallenge)
                .build());

    }



    @PostMapping("/participants")
    public ResponseEntity<GetAllParticipantsRes> getAllParticipants(@RequestBody GetAllParticipantsReq req) {

        Pageable pageable1 = vDataTablePagingConverter.toPageable(req);

        ChallengeResponse challengeResponse = challengeService.findChallengeResponse(req.getChallengeId());

        PageResponse<StudentInChallengeResponse> allParticipants = msicSer.findAllParticipants(req.getChallengeId(), pageable1);

        return ResponseEntity.ok(GetAllParticipantsRes.builder()
                .participants(allParticipants)
                .challengeResponse(challengeResponse)
                .build());

    }

    /**
     * @param challengeId  {@link com.huhoot.model.Challenge} id
     * @param itemsPerPage size
     * @return List of {@link StudentScoreResponse}
     */
    @GetMapping("/challenge-report")
    public ResponseEntity<GetTopStudentRes> getTopStudent(@RequestParam int challengeId,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "20") int itemsPerPage) {

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);

        PageResponse<StudentScoreResponse> topStudent = organizeService.getTopStudent(challengeId, pageable);

        ChallengeResponse challengeResponse = challengeService.findChallengeResponse(challengeId);

        return ResponseEntity.ok(GetTopStudentRes.builder()
                .challengeResponse(challengeResponse)
                .topStudents(topStudent)
                .build());
    }

}
