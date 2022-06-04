package com.huhoot.dto;

import com.huhoot.host.manage.question.QuestionResponse;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetStudentReportsRes {
    private StudentResponse student;
    private int studentRank;
    private double finalScore;
    private PageResponse<QuestionResponse> questions;
    List<StudentAnswerResult> studentAnswerResults;

}
