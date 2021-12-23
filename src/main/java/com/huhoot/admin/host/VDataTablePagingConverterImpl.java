package com.huhoot.admin.host;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Not support groupBy yet
 * Paging converter for v-data-table option v2.1.6
 */
@Component
public class VDataTablePagingConverterImpl implements VDataTablePagingConverter {
    @Override
    public Pageable toPageable(VDataTablePagingRequest request) {


        List<String> sortBy = request.getSortBy();
        List<Boolean> sortDesc = request.getSortDesc();

        List<Sort.Order> orders = new ArrayList<>();

        for (int i = 0; i < sortBy.size(); i++) {
            Boolean isDesc = sortDesc.get(i);
            String property = sortBy.get(i);

            Sort.Order order = isDesc ? Sort.Order.desc(property) : Sort.Order.asc(property);

            orders.add(order);

        }

        Sort sort = Sort.by(orders);

        Pageable result = PageRequest.of(request.getPage() - 1, request.getItemsPerPage(), sort);


        return result;
    }
}
