package com.huhoot.admin.manage.host;

import lombok.Data;

@Data
public class HostAddErrorResponse extends HostAddRequest {

    private String errorMessage;

    public HostAddErrorResponse(HostAddRequest hostAddRequest, String message) {

        this.errorMessage = message;
    }

}
