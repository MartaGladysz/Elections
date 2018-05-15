package com.app.controllers;


import com.app.dao.CandidateDao;
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.MyConverter;
import com.app.service.constituency.ConstituencyService;
import com.app.validators.ConstituencyDtoValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import com.app.dao.ConstituencyDao;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/constituency")
public class ConstituencyController {
    private ConstituencyDao constituencyDao;
    private CandidateDao candidateDao;
    private MyConverter myConverter;
    private ConstituencyService constituencyService;

    public ConstituencyController(ConstituencyDao constituencyDao,  CandidateDao candidateDao,
                                  MyConverter myConverter, ConstituencyService constituencyService) {
        this.constituencyDao = constituencyDao;
        this.candidateDao = candidateDao;
        this.myConverter = myConverter;
        this.constituencyService = constituencyService;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {

        binder.setValidator(new ConstituencyDtoValidator(constituencyService));
    }

    @GetMapping("/selectAll")
    public String constituencySelectAll(Model model){
        List<ConstituencyDto> constituenciesDto = constituencyDao.findAll().stream()
                .map(c -> myConverter.fromConstituencyToConstituencyDto(c))
                .collect(Collectors.toList());

        model.addAttribute("constituenciesDto", constituenciesDto);
        model.addAttribute("constituencyDto", new ConstituencyDto());
        return "constituency/selectAll";
    }


    @GetMapping("/insert")
    public String constituencyInsertGet(Model model){
        model.addAttribute("constituencyDto", new ConstituencyDto());
        model.addAttribute("errors", new HashMap<>());

        return "constituency/insert";
    }

    @PostMapping("/insert")
    public String constituencyInsertPost(Model model, @ModelAttribute @Valid ConstituencyDto constituencyDto, BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        if (result.hasErrors()) {
            List<FieldError> errorsList = result.getFieldErrors();

            for (FieldError e : errorsList) {
                errors.put(e.getField(), e.getCode());
            }

            model.addAttribute("constituencyDto", new ConstituencyDto());
            model.addAttribute("errors", errors);

            return "constituency/insert";
        }

        constituencyDao.add(myConverter.fromConstituencyDtoToConstituency(constituencyDto));

        return "redirect:/constituency/selectAll";
    }

    @GetMapping("/delete/{id}")
    public String constituencyDelete(@PathVariable Long id){
        candidateDao.findAll().stream()
                .filter(c -> c.getConstituency().getId().equals(id))
                .forEach(c -> candidateDao.delete(c.getId()));
        constituencyDao.delete(id);
        return "redirect:/constituency/selectAll";
    }



}
