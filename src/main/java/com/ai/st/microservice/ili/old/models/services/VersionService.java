package com.ai.st.microservice.ili.old.models.services;

import java.util.List;

import javax.transaction.Transactional;

import com.ai.st.microservice.ili.old.models.repositories.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.ili.old.entities.VersionEntity;

@Service
public class VersionService implements IVersionService {

    @Autowired
    private VersionRepository versionRepository;

    @Override
    public Long getCount() {
        return versionRepository.count();
    }

    @Override
    @Transactional
    public VersionEntity createVersion(VersionEntity versionEntity) {
        return versionRepository.save(versionEntity);
    }

    @Override
    public VersionEntity getVersionByName(String name) {
        return versionRepository.findByName(name);
    }

    @Override
    public List<VersionEntity> getVersions() {
        return versionRepository.findAll();
    }

}
