package com.app.service.candidate;

import com.app.model.dto.CandidateDto;

public interface CandidateService {
    Boolean isPresent(CandidateDto cDt);
    Boolean isConfigurationSet();
}
