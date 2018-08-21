package org.apache.fineract.organisation.office.domain;

import java.util.HashMap;
import java.util.Map;

public enum OfficeType {
    ROOT(1, "officeType.root"),
    GROUP(2, "officeType.group"),
    OFFICE(3, "officeType.office");

    private final long value;
    private final String code;

    OfficeType(long value, String code) {
        this.value = value;
        this.code = code;
    }

    public long getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    private static final Map<Long, OfficeType> intToEnumMap = new HashMap<>();

    static {
        for (final OfficeType type : OfficeType.values()) {
            intToEnumMap.put(type.value, type);
        }
    }

    public static OfficeType fromInt(final long i) {
        final OfficeType status = intToEnumMap.get(Long.valueOf(i));
        return status;
    }

    @Override
    public String toString() {
        return name().toString();
    }
}
