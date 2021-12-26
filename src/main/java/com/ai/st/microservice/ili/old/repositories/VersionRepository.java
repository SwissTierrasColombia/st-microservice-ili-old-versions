package com.ai.st.microservice.ili.old.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.microservice.ili.old.entities.VersionEntity;

public interface VersionRepository extends CrudRepository<VersionEntity, Long> {

	VersionEntity findByName(String name);

	@Override
	List<VersionEntity> findAll();

}
