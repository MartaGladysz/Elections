package com.app.service.constituency;

import com.app.dao.ConstituencyDao;
import com.app.model.Constituency;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConstituencyServiceImpl implements ConstituencyService {
    private ConstituencyDao constituencyDao;

    public ConstituencyServiceImpl(ConstituencyDao constituencyDao){

        this.constituencyDao = constituencyDao;
    }

    @Override
    public Boolean isPresent(String constitunecyDtoName) {
        List<Constituency> constituencies = constituencyDao.findAll();
        List<String> constituenciesNames = constituencies.stream()
                .map(c -> c.getName().toUpperCase()).collect(Collectors.toList());

        return(constituenciesNames.contains(constitunecyDtoName.toUpperCase()));
    }
}
