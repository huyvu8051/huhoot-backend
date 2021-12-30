package com.huhoot.host.manage.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuestionOrdinalUpdateRequest {
        private List<QuestionOrdinal> list;
}
