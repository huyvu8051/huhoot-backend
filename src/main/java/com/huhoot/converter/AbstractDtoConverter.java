package com.huhoot.converter;

import com.huhoot.dto.PageResponse;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AbstractDtoConverter {
    public static <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> function) {
        PageResponse<R> result = new PageResponse<>();

        for (T entity : page) {
            result.getList()
                    .add(function.apply(entity));
        }

        result.setTotalElements(page.getTotalElements());
        return result;
    }

    public static <T, R> List<R> toListResponse(List<T> list, Function<T, R> function) {
        List<R> result = new ArrayList<>();
        for (T entity : list) {
            result.add(function.apply(entity));
        }

        return result;
    }


}
