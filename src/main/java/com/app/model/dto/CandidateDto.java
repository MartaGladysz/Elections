package com.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateDto {
    private Long candidateId;
    private String candidateName;
    private String candidateSurname;
    private int candidateVotes;
    private Long constituencyId;
    private String constituencyName;
    private String candidateNameAndSurname;
    private boolean candidateActive;

}

