package com.app.controllers;

import com.app.dao.CandidateDao;
import com.app.dao.ConstituencyDao;
import com.app.dao.VoterDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MyController {
    private ConstituencyDao constituencyDao;
    private CandidateDao candidateDao;
    private VoterDao voterDao;

    public MyController(ConstituencyDao constituencyDao, CandidateDao candidateDao, VoterDao voterDao) {
        this.constituencyDao = constituencyDao;
        this.candidateDao = candidateDao;
        this.voterDao = voterDao;
    }

    @GetMapping("/")
    public String welcomeGet() {

        return "index";
    }

    @GetMapping("/resetAll")
    public String resetAllGet() {
        if(voterDao.findAll() != null && !voterDao.findAll().isEmpty()){
            voterDao.findAll().forEach(v -> voterDao.delete(v.getId()));
        }

        if(candidateDao.findAll() != null && !candidateDao.findAll().isEmpty()){
            candidateDao.findAll().forEach(c -> candidateDao.delete(c.getId()));
        }

        if(constituencyDao.findAll() != null && !constituencyDao.findAll().isEmpty()){
            constituencyDao.findAll().forEach(c -> constituencyDao.delete(c.getId()));
        }

        return "index";
    }
}
