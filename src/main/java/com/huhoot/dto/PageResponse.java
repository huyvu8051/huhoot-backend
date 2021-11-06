package com.huhoot.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> list;
    private long totalElements;


    public PageResponse() {
        this.list = new ArrayList<>();
    }
}
