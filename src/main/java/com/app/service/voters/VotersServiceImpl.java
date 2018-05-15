package com.app.service.voters;

import com.app.dao.VoterDao;
import com.app.model.Voter;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class VotersServiceImpl implements VotersService {
    private VoterDao voterDao;

    public VotersServiceImpl(VoterDao voterDao){
        this.voterDao = voterDao;
    }

    @Override
    public Voter findNthVoter(Integer n) {
        List<Voter> elements = voterDao.findAll();
        if (n < 0 || n >= elements.size()) {
            throw new IllegalArgumentException("BAD INDEX");
        }
        return elements.get(n);
    }

    @Override
    public Integer getVotersNumber() {
        List<Voter> elements = voterDao.findAll();
        return elements == null ? 0 : elements.size();
    }
}
