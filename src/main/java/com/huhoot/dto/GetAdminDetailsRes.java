package com.huhoot.dto;

import com.huhoot.admin.manage.host.HostResponse;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAdminDetailsRes {
    private HostResponse adminDetails;
    private PageResponse<ChallengeResponse> listChallenge;

}
