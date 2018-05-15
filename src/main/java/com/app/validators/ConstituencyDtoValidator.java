package com.app.validators;

import com.app.model.dto.ConstituencyDto;
import com.app.service.constituency.ConstituencyService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ConstituencyDtoValidator implements Validator {
    private ConstituencyService constituencyService;

    public ConstituencyDtoValidator(ConstituencyService constituencyService) {
        this.constituencyService = constituencyService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(ConstituencyDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ConstituencyDto cDt = (ConstituencyDto)o;

        if (cDt.getConstituencyName() == null || !cDt.getConstituencyName().matches("[A-Za-z- ]+")) {
            errors.rejectValue("constituencyName", "NAZWA OKRĘGU NIE JEST POPRAWNA");
        }
        if(constituencyService.isPresent(cDt.getConstituencyName())){
            errors.rejectValue("constituencyName", "OKRĘG ZNAJDUJE SIĘ JUŻ W BAZIE!");
        }

    }
}

