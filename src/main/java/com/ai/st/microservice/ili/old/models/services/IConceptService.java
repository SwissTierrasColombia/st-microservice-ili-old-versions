package com.ai.st.microservice.ili.old.models.services;

import com.ai.st.microservice.ili.old.entities.ConceptEntity;

public interface IConceptService {

    public Long getCount();

    public ConceptEntity createConcept(ConceptEntity conceptEntity);

    public ConceptEntity getConceptById(Long id);

}
