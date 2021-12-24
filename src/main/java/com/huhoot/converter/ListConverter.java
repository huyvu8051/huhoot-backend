package com.huhoot.converter;

import com.huhoot.vue.vdatatable.paging.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public interface ListConverter {
    <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> function);

    <T, R> List<R> toListResponse(List<T> list, Function<T, R> function);

    <R> PageResponse<R> toPageResponse(Page<R> page);
}
