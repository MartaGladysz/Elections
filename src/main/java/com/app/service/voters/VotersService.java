package com.app.service.voters;

import com.app.model.Voter;

public interface VotersService {
    Voter findNthVoter(Integer n);
    Integer getVotersNumber();
}
