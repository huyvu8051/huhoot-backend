package com.huhoot.dto;

import lombok.Data;

@Data
public class HostAddErrorResponse extends HostAddRequest {

    private String errorMessage;

    public HostAddErrorResponse(HostAddRequest hostAddRequest) {
        super(hostAddRequest.getUsername(), hostAddRequest.getPassword());
    }

}
