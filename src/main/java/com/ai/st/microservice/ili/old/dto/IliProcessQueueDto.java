package com.ai.st.microservice.ili.old.dto;

import java.io.Serializable;

public class IliProcessQueueDto implements Serializable {

    private static final long serialVersionUID = 645140052635565979L;

    public static final Long VALIDATOR = (long) 1;

    private Long type;
    private IliValidatorBackgroundDto ilivalidatorData;

    public IliProcessQueueDto() {

    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public IliValidatorBackgroundDto getIlivalidatorData() {
        return ilivalidatorData;
    }

    public void setIlivalidatorData(IliValidatorBackgroundDto ilivalidatorData) {
        this.ilivalidatorData = ilivalidatorData;
    }

}
