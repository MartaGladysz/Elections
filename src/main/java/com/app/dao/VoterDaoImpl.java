package com.app.dao;

import com.app.dao.generic.AbstractGenericDaoImpl;
import com.app.model.Voter;
import org.springframework.stereotype.Repository;

@Repository
public class VoterDaoImpl extends AbstractGenericDaoImpl<Voter> implements VoterDao {}
