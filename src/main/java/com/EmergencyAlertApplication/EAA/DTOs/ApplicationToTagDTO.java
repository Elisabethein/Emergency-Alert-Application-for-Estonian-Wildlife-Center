package com.EmergencyAlertApplication.EAA.DTOs;

import com.EmergencyAlertApplication.EAA.Entities.Application;
import com.EmergencyAlertApplication.EAA.Entities.Tag;

public class ApplicationToTagDTO {
    private Application application;
    private Tag tag;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
