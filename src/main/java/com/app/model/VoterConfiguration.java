package com.app.model;

import com.app.model.dto.ConstituencyDto;
import lombok.*;
import java.util.List;


@Builder
@Data
public class VoterConfiguration {
    private List<ConstituencyDto> constituenciesDto;
    private List<Integer> voterNumbers;

    public VoterConfiguration() {
    }

    public VoterConfiguration(List<ConstituencyDto> constituenciesDto, List<Integer> voterNumbers) {
        this.constituenciesDto = constituenciesDto;
        this.voterNumbers = voterNumbers;
    }

    public List<ConstituencyDto> getConstituenciesDto() {
        return constituenciesDto;
    }

    public void setConstituenciesDto(List<ConstituencyDto> constituenciesDto) {
        this.constituenciesDto = constituenciesDto;
    }

    public List<Integer> getVoterNumbers() {
        return voterNumbers;
    }

    public void setVoterNumbers(List<Integer> voterNumbers) {
        this.voterNumbers = voterNumbers;
    }
}