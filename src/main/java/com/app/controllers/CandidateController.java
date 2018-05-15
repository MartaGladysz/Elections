package com.app.controllers;

import com.app.dao.CandidateDao;
import com.app.dao.ConstituencyDao;
import com.app.model.Candidate;
import com.app.model.Constituency;
import com.app.model.dto.CandidateDto;
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.MyConverter;
import com.app.service.candidate.CandidateService;
import com.app.validators.CandidateDtoValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/candidate")
public class CandidateController {
    private CandidateDao candidateDao;
    private ConstituencyDao constituencyDao;
    private MyConverter myConverter;
    private CandidateService candidateService;

    public CandidateController(CandidateDao candidateDao, ConstituencyDao constituencyDao,
                               MyConverter myConverter, CandidateService candidateService) {
        this.candidateDao = candidateDao;
        this.constituencyDao = constituencyDao;
        this.myConverter = myConverter;
        this.candidateService = candidateService;
    }


    @InitBinder
    protected void initBinder(WebDataBinder binder) {

        binder.setValidator(new CandidateDtoValidator(candidateService));
    }


    @GetMapping("/insert")
    public String candidateInsertGet(Model model){
        List<ConstituencyDto> constituenciesDto = constituencyDao.findAll().stream()
                .map(c -> myConverter.fromConstituencyToConstituencyDto(c))
                .collect(Collectors.toList());

        model.addAttribute("candidateDto", new CandidateDto());
        model.addAttribute("constituenciesDto", constituenciesDto);
        model.addAttribute("errors", new HashMap<>());
        return "candidate/insert";
    }


    @PostMapping("/insert")
    public String candidateInsertPost(Model model, @ModelAttribute @Valid CandidateDto candidateDto, BindingResult result){
        List<ConstituencyDto> constituenciesDto = constituencyDao.findAll().stream()
                .map(c -> myConverter.fromConstituencyToConstituencyDto(c))
                .collect(Collectors.toList());

        if (result.hasErrors()) {
            List<FieldError> errorsList = result.getFieldErrors();
            Map<String, String> errors = new HashMap<>();

            for (FieldError e : errorsList) {
                errors.put(e.getField(), e.getCode());
            }

            model.addAttribute("candidateDto", candidateDto);
            model.addAttribute("constituenciesDto", constituenciesDto);
            model.addAttribute("errors", errors);
            return "candidate/insert";
        }

        Candidate candidate = myConverter.fromCandidateDtoToCandidate(candidateDto);

        candidate.setActive(true);
        candidateDao.add(candidate);
        candidate.setConstituency(constituencyDao.findOneById(candidateDto.getConstituencyId()).orElseThrow(NullPointerException::new));
        candidateDao.update(candidate);

        return "redirect:/candidate/selectAll";
    }

    @GetMapping("/selectAll")
    public String candidateSelectAllGet(Model model){
        List<CandidateDto> candidatesDto = candidateDao.findAll().stream()
                .map(c -> myConverter.fromCandidateToCandidateDto(c)).collect(Collectors.toList());

        model.addAttribute("candidatesDto", candidatesDto);
        return "candidate/selectAll";
    }


    @GetMapping("/delete/{id}")
    public String candidateDelete(@PathVariable Long id){
        Candidate candidate = candidateDao.findOneById(id).orElseThrow(NullPointerException::new);
        candidate.setConstituency(null);
        candidateDao.update(candidate);
        candidateDao.delete(id);

        return "redirect:/candidate/selectAll";
    }

    @GetMapping("/update/{id}")
    public String candidateUpdateGet(@PathVariable Long id, Model model){
        Candidate candidate = candidateDao.findOneById(id).orElseThrow(NullPointerException::new);
        CandidateDto candidateDto = myConverter.fromCandidateToCandidateDto(candidate);

        List<ConstituencyDto> constituenciesDto = constituencyDao.findAll().stream()
                .map(myConverter::fromConstituencyToConstituencyDto)
                .collect(Collectors.toList());

        model.addAttribute("candidateDto", candidateDto);
        model.addAttribute("constituenciesDto", constituenciesDto);
        model.addAttribute("errors", new HashMap<>());
        return "candidate/update";
    }


    @PostMapping("/update")
    public String candidateUpdatePost(Model model, @ModelAttribute @Valid CandidateDto candidateDto, BindingResult result){
        if(result.hasErrors()){
            List<FieldError> errorsList = result.getFieldErrors();
            Map<String, String> errors = new HashMap<>();

            for(FieldError e: errorsList){
                errors.put(e.getField(), e.getCode());
            }
            List<ConstituencyDto> constituenciesDto = constituencyDao.findAll().stream()
                    .map(c -> myConverter.fromConstituencyToConstituencyDto(c))
                    .collect(Collectors.toList());

            model.addAttribute("candidateDto", candidateDto);
            model.addAttribute("constituenciesDto", constituenciesDto);
            model.addAttribute("errors", errors);
            return "candidate/insert";
        }

        Candidate candidate = myConverter.fromCandidateDtoToCandidate(candidateDto);

        candidate.setConstituency(constituencyDao.findOneById(candidateDto.getConstituencyId()).orElseThrow(NullPointerException::new));
        candidate.setActive(true);
        candidateDao.update(candidate);
        return "redirect:/candidate/selectAll";
    }


    @GetMapping("/list")
    public String constituencyAndCandidatesSelectAll(Model model){
        if(!candidateService.isConfigurationSet()){
            model.addAttribute("isConfigurationSet", candidateService.isConfigurationSet());
            model.addAttribute("constituencies", new ArrayList<>());
            model.addAttribute("candidates", new ArrayList<>());
            model.addAttribute("constituencyCandidatesMap", new HashMap<>());
            return "candidate/list";
        }

        else{
            Map<String, List<String>> constituencyCandidatesMap = new LinkedHashMap<>();
            List<Candidate> candidates = candidateDao.findAll();

            Set<Constituency> constituencies = new HashSet<>();

            for(Candidate candidate: candidates){
                constituencies.add(candidate.getConstituency());
            }

            for(Constituency constituency: constituencies){
                constituencyCandidatesMap.put(constituency.getName(), new ArrayList<>());
                for(Candidate candidate: candidates) {
                    if(candidate.getConstituency().getId().equals(constituency.getId())){
                        constituencyCandidatesMap.get(constituency.getName()).add(candidate.getName()+" "+candidate.getSurname());
                    }
                }
            }

            model.addAttribute("isConfigurationSet", candidateService.isConfigurationSet());
            model.addAttribute("constituencies", constituencies);
            model.addAttribute("candidates", candidates);
            model.addAttribute("constituencyCandidatesMap", constituencyCandidatesMap);
            return "candidate/list";
        }
    }
}

