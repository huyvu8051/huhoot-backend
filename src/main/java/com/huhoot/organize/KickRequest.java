package com.huhoot.organize;

import lombok.Data;

import java.util.List;

@Data
public class KickRequest {
    private List<Integer> studentIds;
    private int challengeId;

}
