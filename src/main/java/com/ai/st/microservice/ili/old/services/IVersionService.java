package com.ai.st.microservice.ili.old.services;

import java.util.List;

import com.ai.st.microservice.ili.old.entities.VersionEntity;

public interface IVersionService {

    public Long getCount();

    public VersionEntity createVersion(VersionEntity versionEntity);

    public VersionEntity getVersionByName(String name);

    public List<VersionEntity> getVersions();

}
