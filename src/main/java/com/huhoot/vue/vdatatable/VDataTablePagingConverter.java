package com.huhoot.vue.vdatatable;

import org.springframework.data.domain.Pageable;

public interface VDataTablePagingConverter {
    Pageable toPageable(VDataTablePagingRequest request);

}
