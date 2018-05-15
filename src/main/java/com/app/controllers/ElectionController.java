package com.app.controllers;

import com.app.dao.CandidateDao;
import com.app.dao.ConstituencyDao;
import com.app.dao.VoterDao;
import com.app.model.*;
import com.app.model.dto.CandidateDto;
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.MyConverter;
import com.app.model.dto.VoterDto;
import com.app.service.election.ElectionService;
import com.app.service.voters.VotersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/election")
public class ElectionController {
    private ConstituencyDao constituencyDao;
    private VoterDao voterDao;
    private MyConverter myConverter;
    private CandidateDao candidateDao;
    private VotersService votersService;
    private ElectionService electionService;


    public ElectionController(ConstituencyDao constituencyDao, VoterDao voterDao, MyConverter myConverter,
                              CandidateDao candidateDao, VotersService votersService, ElectionService electionService) {
        this.constituencyDao = constituencyDao;
        this.voterDao = voterDao;
        this.myConverter = myConverter;
        this.candidateDao = candidateDao;
        this.votersService = votersService;
        this.electionService = electionService;
    }

    @GetMapping("/poll/{n}")
    public String pollGet(Model model, @PathVariable Integer n) {
        if(!electionService.isConfigurationSet()){
            model.addAttribute("isConfigurationSet", electionService.isConfigurationSet());
            model.addAttribute("electionConfiguration", new ElectionConfiguration());

            return "election/poll";
        }

        else{
            VoterDto voterDto = myConverter.fromVoterToVoterDto(votersService.findNthVoter(n));

            List<CandidateDto> votersCandidatesDto = candidateDao.findAll().stream()
                    .filter(c -> c.getConstituency().getId().equals(voterDto.getConstituencyId()))
                    .filter(c -> c.isActive())
                    .map(c -> myConverter.fromCandidateToCandidateDto(c)).collect(Collectors.toList());

            ElectionConfiguration electionConfiguration = ElectionConfiguration.builder().voterDto(voterDto)
                    .votersCandidatesDto(votersCandidatesDto).chosenCandidateId(0L).n(n).build();

            model.addAttribute("isConfigurationSet", electionService.isConfigurationSet());
            model.addAttribute("electionConfiguration", electionConfiguration);

            return "election/poll";
        }
    }

    @PostMapping("/poll")
    public String pollPost(Model model, @ModelAttribute ElectionConfiguration electionConfiguration){

        Candidate chosenCandidate = candidateDao.findOneById(electionConfiguration.getChosenCandidateId())
                .orElseThrow(NullPointerException::new);

        chosenCandidate.setVotes(chosenCandidate.getVotes()+1);
        candidateDao.update(chosenCandidate);

        Constituency constituency = constituencyDao.findOneById(chosenCandidate.getConstituency().getId())
                .orElseThrow(NullPointerException::new);

        constituency.setVotes(constituency.getVotes()+1);
        constituencyDao.update(constituency);

        int n = electionConfiguration.getN();
        if (n < votersService.getVotersNumber() - 1){
            ++n;
            return "redirect:/election/poll/" + n;
        }


        List<ConstituencyDto> constituenciesDto = constituencyDao.findAll().stream()
                .filter(c -> c.getCandidates().size() > 0)
                .map(c -> myConverter.fromConstituencyToConstituencyDto(c))
                .collect(Collectors.toList());

        List<CandidateDto> candidatesDto = candidateDao.findAll().stream()
                .map(c -> myConverter.fromCandidateToCandidateDto(c)).collect(Collectors.toList());

        Map<ConstituencyDto, String> winnersMap = electionService.findWinners();

        List<Constituency> constituenciesWithoutWinners = new ArrayList<>();

        for(Map.Entry<ConstituencyDto, String> e : winnersMap.entrySet()) {
            if((e.getValue().equals("BRAK"))){
                constituenciesWithoutWinners.add(myConverter.fromConstituencyDtoToConstituency(e.getKey()));
            }
        }

        voterDao.findAll().stream()
                .filter(v -> (!constituenciesWithoutWinners.contains(v.getConstituency())))
                .forEach(v -> voterDao.delete(v.getId()));


        for(Constituency c: constituenciesWithoutWinners) {
            Optional<Candidate> secondCandidateOp = candidateDao.findAll().stream()
                    .filter(can -> can.getConstituency().equals(c))
                    .sorted((can1, can2) -> Integer.compare(can2.getVotes(), can1.getVotes()))
                    .limit(2).min(Comparator.comparingInt(Candidate::getVotes));

            Candidate secondCandidate = secondCandidateOp.orElseThrow(NullPointerException::new);

            for (Candidate can : candidateDao.findAll()) {
                if(can.getConstituency().getId().equals(c.getId())){
                    if(can.getVotes() < secondCandidate.getVotes()){
                        can.setActive(false);
                    }
                    can.resetVotes();
                    can.getConstituency().resetVotes();
                    candidateDao.update(can);
                    constituencyDao.update(can.getConstituency());
                }
            }
        }

        model.addAttribute("winnersMap", winnersMap);
        model.addAttribute("constituenciesDto", constituenciesDto);
        model.addAttribute("candidatesDto", candidatesDto);

        return "election/result";
    }

    @GetMapping("stop")
    public String stopGet() {
        voterDao.findAll().forEach(v -> voterDao.delete(v.getId()));

        for (Candidate c: candidateDao.findAll()){
            c.setActive(true);
            c.resetVotes();
            candidateDao.update(c);
        }

        for (Constituency c: constituencyDao.findAll()){
            c.resetVotes();
            constituencyDao.update(c);
        }

        return "index";
    }
}
