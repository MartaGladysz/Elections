package com.app.dao;

import com.app.dao.generic.AbstractGenericDaoImpl;
import com.app.model.Candidate;
import org.springframework.stereotype.Repository;

@Repository
public class CandidateDaoImpl extends AbstractGenericDaoImpl<Candidate> implements CandidateDao{}

