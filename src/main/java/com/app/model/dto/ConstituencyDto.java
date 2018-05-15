package com.app.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConstituencyDto {
    private Long constituencyId;
    private String constituencyName;
    private Integer constituencyVotersNumber;
    private Integer constituencyVotes;

    @Override
    public String toString() {
        return constituencyName;
    }
}

