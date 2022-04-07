package com.ai.st.microservice.ili.old.rabbitmq.listerners;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ai.st.microservice.ili.old.business.VersionBusiness;
import com.ai.st.microservice.ili.old.dto.VersionDataDto;
import com.ai.st.microservice.ili.old.services.ZipService;
import com.ai.st.microservice.ili.old.dto.IliProcessQueueDto;
import com.ai.st.microservice.ili.old.dto.IliValidatorBackgroundDto;
import com.ai.st.microservice.ili.old.dto.ValidationDto;
import com.ai.st.microservice.ili.old.services.IlivalidatorService;
import com.ai.st.microservice.ili.old.services.RabbitMQSenderService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.RandomStringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQIliListerner {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${st.temporalDirectory}")
    private String stTemporalDirectory;

    @Autowired
    private ZipService zipService;

    @Autowired
    private IlivalidatorService ilivalidatorService;

    @Autowired
    private RabbitMQSenderService rabbitService;

    @Autowired
    private VersionBusiness versionBusiness;

    @RabbitListener(queues = "${st.rabbitmq.queueIliOld.queue}", concurrency = "${st.rabbitmq.queueIliOld.concurrency}")
    public void iliProcess(IliProcessQueueDto data) {

        log.info("ili process started");

        if (data.getType().equals(IliProcessQueueDto.VALIDATOR)) {
            this.ilivalidator(data.getIlivalidatorData());
        }

    }

    public void ilivalidator(IliValidatorBackgroundDto data) {

        log.info("validation started #" + data.getRequestId());

        ValidationDto validationDto = new ValidationDto();
        Boolean validation = false;

        try {

            VersionDataDto versionData = versionBusiness.getDataVersion(data.getVersionModel(), data.getConceptId());
            if (versionData != null) {

                Path path = Paths.get(data.getPathFile());
                String fileName = path.getFileName().toString();
                String fileExtension = FilenameUtils.getExtension(fileName);

                String pathFileXTF = "";

                String nameDirectory = "ili_process_validation_" + RandomStringUtils.random(7, false, true);
                Path tmpDirectory = Files.createTempDirectory(Paths.get(stTemporalDirectory), nameDirectory);
                Path tmpDirectoryLog = Files.createTempDirectory(Paths.get(stTemporalDirectory),
                        RandomStringUtils.random(7, false, true));

                if (fileExtension.equalsIgnoreCase("zip")) {

                    List<String> paths = zipService.unzip(data.getPathFile(), new File(tmpDirectory.toString()));
                    pathFileXTF = tmpDirectory.toString() + File.separator + paths.get(0);

                } else if (fileExtension.equalsIgnoreCase("xtf")) {
                    pathFileXTF = data.getPathFile();
                }

                if (pathFileXTF.isEmpty()) {
                    log.error("there is not file xtf.");
                } else {

                    String logFileValidation = Paths.get(tmpDirectoryLog.toString(), "ilivalidator.log").toString();
                    String logFileValidationXTF = Paths.get(tmpDirectory.toString(), "ilivalidator.xtf").toString();

                    String pathTomlFile = null;
                    if (data.getSkipGeometryValidation()) {

                        try {
                            final Path pathToml = Files.createTempFile("myTomlFile", ".toml");

                            String dataFile = "[\"PARAMETER\"]\n" + "defaultGeometryTypeValidation=\"off\"";

                            // Writing data here
                            byte[] buf = dataFile.getBytes();
                            Files.write(pathToml, buf);

                            // Delete file on exit
                            pathToml.toFile().deleteOnExit();

                            pathTomlFile = pathToml.toFile().getAbsolutePath();

                        } catch (IOException e) {
                            log.error("Error creating toml file: " + e.getMessage());
                        }

                    }

                    validation = ilivalidatorService.validate(pathFileXTF, versionData.getUrl(),
                            versionData.getModels(), null, logFileValidation, logFileValidationXTF, pathTomlFile);

                    log.info("validation successful with result: " + validation);

                    if (!validation) {
                        validationDto.setErrors(searchErrors(logFileValidation));
                        validationDto.setLog(logFileValidation);
                    } else {
                        try {
                            FileUtils.deleteDirectory(tmpDirectoryLog.toFile());
                        } catch (Exception e) {
                            log.error("It has not been possible delete the directory (log): " + e.getMessage());
                        }
                    }

                    try {
                        FileUtils.deleteDirectory(tmpDirectory.toFile());
                    } catch (Exception e) {
                        log.error("It has not been possible delete the directory: " + e.getMessage());
                    }

                }

            }

        } catch (Exception e) {
            log.error("validation failed # " + data.getRequestId() + " : " + e.getMessage());
            validationDto.setErrors(new ArrayList<>(Collections.singletonList(e.getMessage())));
        }

        validationDto.setIsValid(validation);
        validationDto.setFilenameTemporal(data.getPathFile());
        validationDto.setGeometryValidated(!data.getSkipGeometryValidation());
        validationDto.setSkipErrors(data.getSkipErrors());
        validationDto.setReferenceId(data.getReferenceId());

        validationDto.setUserCode(data.getUserCode());
        validationDto.setObservations(data.getObservations());

        validationDto.setRequestId(data.getRequestId());
        validationDto.setSupplyRequestedId(data.getSupplyRequestedId());

        rabbitService.sendStatsValidationQueueProducts(validationDto);
    }

    public List<String> searchErrors(String pathFile) {
        List<String> errors = new ArrayList<>();
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(new File(pathFile), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                boolean errorFound = line.contains("Error:");
                if (errorFound) {
                    errors.add(line);
                }
            }
        } finally {
            LineIterator.closeQuietly(it);
        }

        return errors;
    }

}
