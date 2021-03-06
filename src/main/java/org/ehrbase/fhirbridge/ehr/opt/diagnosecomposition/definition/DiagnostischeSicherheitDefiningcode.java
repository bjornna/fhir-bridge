package org.ehrbase.fhirbridge.ehr.opt.diagnosecomposition.definition;

import org.ehrbase.client.classgenerator.EnumValueSet;

public enum DiagnostischeSicherheitDefiningcode implements EnumValueSet {
    BESTATIGT("Bestätigt", "Die Diagnose wurde anhand anerkannter Kriterien bestätigt.", "local", "at0076"),

    VERMUTET("Vermutet", "Die Diagnose wurde mit einem niedrigen Grad an Sicherheit gestellt.", "local", "at0074"),

    WAHRSCHEINLICH("Wahrscheinlich", "Die Diagnose wurde mit einem hohen Maß an Sicherheit gestellt.", "local", "at0075");

    private String value;

    private String description;

    private String terminologyId;

    private String code;

    DiagnostischeSicherheitDefiningcode(String value, String description, String terminologyId,
                                        String code) {
        this.value = value;
        this.description = description;
        this.terminologyId = terminologyId;
        this.code = code;
    }

    public String getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTerminologyId() {
        return this.terminologyId;
    }

    public String getCode() {
        return this.code;
    }
}
