package com.huhoot.admin.host;

import org.springframework.data.domain.Pageable;

public interface VDataTablePagingConverter {
    Pageable toPageable(VDataTablePagingRequest request);

}
