package com.huhoot.converter;

import com.huhoot.vue.vdatatable.paging.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class ListConverterImpl implements ListConverter {
    public <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> function) {
        PageResponse<R> result = new PageResponse<>();

        for (T entity : page) {
            result.getList()
                    .add(function.apply(entity));
        }

        result.setTotalElements(page.getTotalElements());
        return result;
    }


    public <T, R> List<R> toListResponse(List<T> list, Function<T, R> function) {
        List<R> result = new ArrayList<>();
        for (T entity : list) {
            result.add(function.apply(entity));
        }

        return result;
    }


    public <R> PageResponse<R> toPageResponse(Page<R> page) {
        PageResponse<R> result = new PageResponse<>();
        result.setList(page.toList());
        result.setTotalElements(page.getTotalElements());
        return result;

    }
}
