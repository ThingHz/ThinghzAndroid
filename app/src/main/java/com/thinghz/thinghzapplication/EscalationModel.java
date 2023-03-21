package com.thinghz.thinghzapplication;

import java.io.Serializable;

public class EscalationModel implements Serializable {
    private String escalation;

    public EscalationModel(String escalation) {
        this.escalation = escalation;
    }

    public String getEscalation() {
        return escalation;
    }
}
