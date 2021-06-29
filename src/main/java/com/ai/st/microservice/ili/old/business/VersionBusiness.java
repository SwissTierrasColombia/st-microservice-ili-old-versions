package com.ai.st.microservice.ili.old.business;

import com.ai.st.microservice.ili.old.dto.QueryTypeDto;
import com.ai.st.microservice.ili.old.dto.VersionDataDto;
import com.ai.st.microservice.ili.old.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.ili.old.dto.QueryDto;
import com.ai.st.microservice.ili.old.entities.ConceptEntity;
import com.ai.st.microservice.ili.old.entities.ModelEntity;
import com.ai.st.microservice.ili.old.entities.QueryEntity;
import com.ai.st.microservice.ili.old.entities.QueryTypeEntity;
import com.ai.st.microservice.ili.old.entities.VersionConceptEntity;
import com.ai.st.microservice.ili.old.entities.VersionEntity;
import com.ai.st.microservice.ili.old.services.IConceptService;
import com.ai.st.microservice.ili.old.services.IVersionService;

@Component
public class VersionBusiness {

    @Autowired
    private IVersionService versionService;

    @Autowired
    private IConceptService conceptService;

    public VersionDataDto getDataVersion(String versionName, Long conceptId) throws BusinessException {

        VersionDataDto versionDataDto;

        VersionEntity versionEntity = versionService.getVersionByName(versionName);

        ConceptEntity conceptEntity = conceptService.getConceptById(conceptId);

        if (versionEntity != null && conceptEntity != null) {

            versionDataDto = new VersionDataDto();
            versionDataDto.setVersion(versionName);

            VersionConceptEntity versionConcept = versionEntity.getVersionsConcepts().stream()
                    .filter(vC -> vC.getConcept().getId().equals(conceptId)).findAny().orElse(null);

            String models = "";
            for (ModelEntity modelEntity : versionConcept.getModels()) {
                models += modelEntity.getName() + ";";
            }

            for (QueryEntity queryEntity : versionConcept.getQuerys()) {
                QueryDto queryDto = new QueryDto();
                queryDto.setId(queryEntity.getId());
                queryDto.setQuery(queryEntity.getQuery());
                QueryTypeEntity queryTypeEntity = queryEntity.getQueryType();
                queryDto.setQueryType(new QueryTypeDto(queryTypeEntity.getId(), queryTypeEntity.getName()));
                versionDataDto.getQueries().add(queryDto);
            }

            versionDataDto.setUrl(versionConcept.getUrl());
            versionDataDto.setModels(models);

        } else {
            throw new BusinessException("No se ha encontrado la versión");
        }

        return versionDataDto;
    }

}
