package com.app.service.candidate;

import com.app.dao.CandidateDao;
import com.app.model.dto.CandidateDto;
import com.app.service.candidate.CandidateService;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {
    private CandidateDao candidateDao;

    public CandidateServiceImpl(CandidateDao candidateDao) {
        this.candidateDao = candidateDao;
    }

    @Override
    public Boolean isPresent(CandidateDto cDt) {
        return candidateDao.findAll().stream()
                .anyMatch(c -> (c.getName().equalsIgnoreCase(cDt.getCandidateName())
                        && (c.getSurname().equalsIgnoreCase(cDt.getCandidateSurname()))
                        && (c.getConstituency().getId().equals(cDt.getConstituencyId()))));
    }

    @Override
    public Boolean isConfigurationSet() {
        return (!candidateDao.findAll().isEmpty());
    }
}
