package com.ai.st.microservice.ili.old.services;

import javax.transaction.Transactional;

import com.ai.st.microservice.ili.old.entities.VersionConceptEntity;
import com.ai.st.microservice.ili.old.repositories.VersionConceptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VersionConceptService implements IVersionConceptService {

	@Autowired
	private VersionConceptRepository versionConceptRepository;

	@Override
	@Transactional
	public VersionConceptEntity createVersionConcept(VersionConceptEntity versionConcept) {
		return versionConceptRepository.save(versionConcept);
	}

}
