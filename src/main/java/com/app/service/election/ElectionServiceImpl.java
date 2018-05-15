package com.app.service.election;

import com.app.dao.CandidateDao;
import com.app.dao.ConstituencyDao;
import com.app.dao.VoterDao;
import com.app.model.dto.CandidateDto;
import com.app.model.dto.ConstituencyDto;
import com.app.model.dto.MyConverter;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class ElectionServiceImpl implements ElectionService {
    private ConstituencyDao constituencyDao;
    private MyConverter myConverter;
    private CandidateDao candidateDao;
    private VoterDao voterDao;

    public ElectionServiceImpl(ConstituencyDao constituencyDao, MyConverter myConverter,
                               CandidateDao candidateDao, VoterDao voterDao) {
        this.constituencyDao = constituencyDao;
        this.myConverter = myConverter;
        this.candidateDao = candidateDao;
        this.voterDao = voterDao;
    }

    @Override
    public Map<ConstituencyDto, String> findWinners() {
        Map<ConstituencyDto, String> winnersMap = new LinkedHashMap<>();

        Map<ConstituencyDto, Integer> votesMap = new LinkedHashMap<>();

        List<ConstituencyDto> constituenciesDto = constituencyDao.findAll().stream()
                .filter(c -> c.getCandidates().size() > 0)
                .map(c -> myConverter.fromConstituencyToConstituencyDto(c))
                .collect(Collectors.toList());

        List<CandidateDto> candidatesDto = candidateDao.findAll().stream()
                .map(c -> myConverter.fromCandidateToCandidateDto(c))
                .collect(Collectors.toList());

        for(ConstituencyDto cDt : constituenciesDto) {
            winnersMap.put(cDt, "BRAK");
            votesMap.put(cDt, cDt.getConstituencyVotes());
        }

        for(CandidateDto canDto : candidatesDto) {
            ConstituencyDto constituencyDto = myConverter
                    .fromConstituencyToConstituencyDto(constituencyDao.findOneById(canDto.getConstituencyId()).orElseThrow(NullPointerException::new));
            if (canDto.getCandidateVotes() * 2 > votesMap.get(constituencyDto)) {
                winnersMap.put(constituencyDto, canDto.getCandidateNameAndSurname());
            }
        }
        return winnersMap;
    }

    @Override
    public Boolean isConfigurationSet() {
        return (!voterDao.findAll().isEmpty())&&(!candidateDao.findAll().isEmpty());
    }
}
