package com.app.model;


import com.app.model.dto.CandidateDto;
import com.app.model.dto.VoterDto;
import lombok.*;
import java.util.List;

@Data
@Builder

public class ElectionConfiguration {
    private VoterDto voterDto;
    private List<CandidateDto> votersCandidatesDto;
    private Integer n;
    private Long chosenCandidateId;


    public ElectionConfiguration() {
    }

    public ElectionConfiguration(VoterDto voterDto, List<CandidateDto> votersCandidatesDto, Integer n, Long chosenCandidateId) {
        this.voterDto = voterDto;
        this.votersCandidatesDto = votersCandidatesDto;
        this.n = n;
        this.chosenCandidateId = chosenCandidateId;
    }

    public VoterDto getVoterDto() {
        return voterDto;
    }

    public void setVoterDto(VoterDto voterDto) {
        this.voterDto = voterDto;
    }

    public List<CandidateDto> getVotersCandidatesDto() {
        return votersCandidatesDto;
    }

    public void setVotersCandidatesDto(List<CandidateDto> votersCandidatesDto) {
        this.votersCandidatesDto = votersCandidatesDto;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Long getChosenCandidateId() {
        return chosenCandidateId;
    }

    public void setCandidateId(Long chosenCandidateId) {
        this.chosenCandidateId = chosenCandidateId;
    }

    @Override
    public String toString() {
        return "ElectionConfiguration{" +
                "voterDto=" + voterDto +
                ", votersCandidatesDto=" + votersCandidatesDto +
                ", n=" + n +
                ", chosenCandidateId=" + chosenCandidateId +
                '}';
    }
}



