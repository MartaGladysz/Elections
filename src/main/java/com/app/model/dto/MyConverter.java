package com.app.model.dto;


import com.app.model.Candidate;
import com.app.model.Constituency;
import com.app.model.Voter;
import org.springframework.stereotype.Component;


@Component
public class MyConverter {

    public Constituency fromConstituencyDtoToConstituency(ConstituencyDto cDt){
        Constituency c = new Constituency();
        c.setId(cDt.getConstituencyId());
        c.setName(cDt.getConstituencyName());

        return c;
    }

    public ConstituencyDto fromConstituencyToConstituencyDto(Constituency c){
        ConstituencyDto cDt = new ConstituencyDto();
        cDt.setConstituencyId(c.getId());
        cDt.setConstituencyName(c.getName());
        cDt.setConstituencyVotes(c.getVotes());
        cDt.setConstituencyVotersNumber(c.getVoters().size());

        return cDt;
    }

    public Candidate fromCandidateDtoToCandidate(CandidateDto cDt){
        Candidate c = new Candidate();
        c.setId(cDt.getCandidateId());
        c.setName(cDt.getCandidateName());
        c.setSurname(cDt.getCandidateSurname());
        c.setVotes(cDt.getCandidateVotes());
        c.setActive(cDt.isCandidateActive());
        //c.setConstituency(constituencyDao.findOneById(cDt.getConstituencyId()).get());

        return c;
    }

    public CandidateDto fromCandidateToCandidateDto(Candidate c){
        CandidateDto cDt = new CandidateDto();
        cDt.setCandidateId(c.getId());
        cDt.setCandidateName(c.getName());
        cDt.setCandidateSurname(c.getSurname());
        cDt.setConstituencyId(c.getConstituency().getId());
        cDt.setConstituencyName(c.getConstituency().getName());
        cDt.setCandidateNameAndSurname(c.getName()+" "+c.getSurname());
        cDt.setCandidateVotes(c.getVotes());
        cDt.setCandidateActive(c.isActive());

        return cDt;
    }

    public Voter fromVoterDtoToVoter(VoterDto vDt){
        Voter v = new Voter();
        v.setId(vDt.getVoterId());

        return v;
    }

    public VoterDto fromVoterToVoterDto(Voter v){
        VoterDto vDt = new VoterDto();
        vDt.setVoterId(v.getId());
        vDt.setConstituencyId(v.getConstituency().getId());
        vDt.setConstituencyName(v.getConstituency().getName());

        return vDt;
    }
}
