package com.huhoot.converter;

import com.huhoot.dto.PageResponse;
import org.springframework.data.domain.Page;

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
}
