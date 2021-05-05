package com.vet24.models.enums;

public enum ProcedureTypeEnum {
    VACCINATION(Values.VACCINATION),
    ECHINOCOCCUS(Values.ECHINOCOCCUS),
    EXTERNAL_PARASITE(Values.EXTERNAL_PARASITE);

    private ProcedureTypeEnum(String val) {
        if (!this.name().equals(val))
            throw new IllegalArgumentException("Incorrect use of ProcedureTypeEnum");
    }

    public static class Values {
        public static final String VACCINATION = "VACCINATION";
        public static final String ECHINOCOCCUS = "ECHINOCOCCUS";
        public static final String EXTERNAL_PARASITE = "EXTERNAL_PARASITE";
    }
}
