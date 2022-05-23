package com.huhoot.organize;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class AnswerResultResponse {
    private int id;
    private int ordinalNumber;
    private String content;

    public AnswerResultResponse(int id, int ordinalNumber, String content) {
        this.id = id;
        this.ordinalNumber = ordinalNumber;
        this.content = content;
    }
}
