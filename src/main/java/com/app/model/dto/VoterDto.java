package com.app.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoterDto {
    private Long voterId;
    private Long constituencyId;
    private String constituencyName;
}
