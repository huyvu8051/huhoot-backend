package com.huhoot.vue.vdatatable.paging;

import lombok.Data;

import java.util.List;

@Data
public class PagingDto {
    private List<String> groupBy;
    private List<String> groupDesc;
    private int itemsPerPage;
    private boolean multiSort;
    private int page;
    private String[] sortBy = {"createdDate"};
    private boolean[] sortDesc = {true};
}
