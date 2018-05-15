package com.app.controllers;

import com.app.dao.CandidateDao;
import com.app.dao.ConstituencyDao;
import com.app.dao.VoterDao;
import com.app.model.Voter;
import com.app.model.VoterConfiguration;
import com.app.model.dto.ConstituencyDto;
import com.app.model.Constituency;
import com.app.model.dto.MyConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/voters")
public class VotersController {
    private ConstituencyDao constituencyDao;
    private VoterDao voterDao;
    private MyConverter myConverter;
    private CandidateDao candidateDao;

    public VotersController(ConstituencyDao constituencyDao, MyConverter myConverter,
                            VoterDao voterDao, CandidateDao candidateDao) {
        this.constituencyDao = constituencyDao;
        this.myConverter = myConverter;
        this.voterDao = voterDao;
        this.candidateDao = candidateDao;
    }

    @GetMapping("/insert")
    public String votersInsertGet(Model model) {
        List<ConstituencyDto> constituenciesDto = constituencyDao.findAll().stream()
                .filter(c -> c.getCandidates().size() > 0)
                .sorted(Comparator.comparing(Constituency::getName))
                .map(myConverter::fromConstituencyToConstituencyDto)
                .collect(Collectors.toList());

        List<Integer> numbers = constituenciesDto.stream().map(c -> 0).collect(Collectors.toList());

        model.addAttribute("voterConfiguration", new VoterConfiguration(constituenciesDto, numbers));
        model.addAttribute("constituenciesDto", constituenciesDto);
        return "voters/insert";
    }

    @PostMapping("/insert")
    public String votersInsertPost(@ModelAttribute @Valid VoterConfiguration voterConfiguration) {
        List<ConstituencyDto> constituenciesDto = constituencyDao.findAll().stream()
                .filter(c -> c.getCandidates().size() > 0)
                .sorted(Comparator.comparing(Constituency::getName))
                .map(myConverter::fromConstituencyToConstituencyDto)
                .collect(Collectors.toList());

        voterConfiguration.setConstituenciesDto(constituenciesDto);

        voterDao.findAll().forEach(v -> voterDao.delete(v.getId()));
        Voter voter;
        for (int i = 0; i < constituenciesDto.size(); i++) {
            for (int j = 0; j < voterConfiguration.getVoterNumbers().get(i); j++) {
                voter = new Voter();
                voterDao.add(voter);
                voter.setConstituency(constituencyDao.findOneById(constituenciesDto.get(i).getConstituencyId()).orElseThrow(NullPointerException::new));
                voterDao.update(voter);
            }
        }

        candidateDao.findAll()
                .forEach(c -> {
                    c.setActive(true);
                    c.resetVotes();
                    c.getConstituency().resetVotes();
                    candidateDao.update(c);
                    constituencyDao.update(c.getConstituency());
                });

        return "redirect:/";
    }
}




