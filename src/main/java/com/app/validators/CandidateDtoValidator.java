package com.app.validators;

import com.app.model.dto.CandidateDto;
import com.app.service.candidate.CandidateService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CandidateDtoValidator implements Validator {
    private CandidateService candidateService;

    public CandidateDtoValidator(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @Override
    public boolean supports(Class<?> aClass) {

        return aClass.equals(CandidateDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CandidateDto cDt = (CandidateDto) o;

        if(cDt.getCandidateName() == null || !cDt.getCandidateName().matches("[A-Za-z- ]+")){
            errors.rejectValue("candidateName", "NIEPRAWIDŁOWY FORMAT NAPISU!");
        }

        if(cDt.getCandidateSurname() == null || !cDt.getCandidateSurname().matches("[A-Za-z- ]+")){
            errors.rejectValue("candidateSurname", "NIEPRAWIDŁOWY FORMAT NAPISU!");
        }

        if(candidateService.isPresent(cDt)){
            errors.rejectValue("candidateId", "KANDYDAT ZNAJDUJE SIĘ JUŻ W BAZIE!");
        }

    }
}
