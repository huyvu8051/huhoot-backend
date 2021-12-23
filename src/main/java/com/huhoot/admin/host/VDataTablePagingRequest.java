package com.huhoot.admin.host;

import lombok.Data;

import javax.validation.constraints.Min;
import java.util.List;

@Data
public class VDataTablePagingRequest {
    // not support yet
    private List<String> groupBy;
    private List<String> groupDesc;
    //

    @Min(0)
    private int itemsPerPage;

    private boolean multiSort;
    private boolean mustSort;

    @Min(0)
    private int page;

    private List<String> sortBy;
    private List<Boolean> sortDesc;
}
