package com.appdynamics.extensions.extensionstarter;

public class Constants {

    public static final String DEFAULT_METRIC_UPLOAD_ENDPOINT = "Server%20&%20Infrastructure%20Monitoring/metric-data?" +
            "metric-path=Application%20Infrastructure%20Performance%7CRoot%7CCustom%20Metrics%7CExtension%20Starter%20" +
            "CI%7CIncomingRequests&time-range-type=BEFORE_NOW&duration-in-mins=10&output=JSON";

    public static final String METRIC_WITH_PROPS_ENDPOINT = "Server%20&%20Infrastructure%20Monitoring/metric-data?metric" +
            "-path=Application%20Infrastructure%20Performance%7CRoot%7CCustom%20Metrics%7CExtension%20Starter%20CI%7C" +
            "Outgoing%20Requests&time-range-type=BEFORE_NOW&duration-in-mins=10&output=JSON";

    public static final String DERIVED_METRIC_ENDPOINT = "Server%20&%20Infrastructure%20Monitoring/metric-data?metric-" +
            "path=Application%20Infrastructure%20Performance%7CRoot%7CCustom%20Metrics%7CExtension%20Starter%20CI%7C" +
            "Total%20Number%20of%20Requests&time-range-type=BEFORE_NOW&duration-in-mins=10&output=JSON";

    public static final String CLUSTER_METRIC_ENDPOINT = "Server%20&%20Infrastructure%20Monitoring/metric-data?metric-" +
            "path=Application%20Infrastructure%20Performance%7CRoot%7CCustom%20Metrics%7CExtension%20Starter%20CI%7" +
            "CMaster%7CRequests&time-range-type=BEFORE_NOW&duration-in-mins=10&output=JSON";

    public static final String DEFAULT_METRIC_CHAR_REPLACEMENT_ENDPOINT = "Server%20&%20Infrastructure%20Monitoring/" +
            "metric-data?metric-path=Application%20Infrastructure%20Performance%7CRoot%7CCustom%20Metrics%7CExtension%20" +
            "Starter%20CI%7CCharacter%20Replacement%7CPipe&time-range-type=BEFORE_NOW&duration-in-mins=10&output=JSON";

    public static final String DEFAULT_REPLACEMENT_OVERRIDDEN_ENDPOINT = "Server%20&%20Infrastructure%20Monitoring/" +
            "metric-data?metric-path=Application%20Infrastructure%20Performance%7CRoot%7CCustom%20Metrics%7CExtension%" +
            "20Starter%20CI%7CCharacter%20Replacement%7CComma%25&time-range-type=BEFORE_NOW&duration-in-mins=10&output=JSON";

    public static final String MULTIPLE_CHAR_REPLACEMENT_ENDPOINT = "Server%20&%20Infrastructure%20Monitoring/metric-data" +
            "?metric-path=Application%20Infrastructure%20Performance%7CRoot%7CCustom%20Metrics%7CExtension%20Starter%20CI" +
            "%7CCharacter%20Replacement%7C%23Colon&time-range-type=BEFORE_NOW&duration-in-mins=10&output=JSON";

    public static final String NON_ASCII_REPLACEMENT_ENDPOINT = "Server%20&%20Infrastructure%20Monitoring/metric-data?" +
            "metric-path=Application%20Infrastructure%20Performance%7CRoot%7CCustom%20Metrics%7CExtension%20Starter%20" +
            "CI%7CCharacter%20Replacement%7CMemory%20Used&time-range-type=BEFORE_NOW&duration-in-mins=10&output=JSON";

    public static final String INVALID_CHAR_REPLACEMENT_ENDPPOINT = "Server%20&%20Infrastructure%20Monitoring/metric-" +
            "data?metric-path=Application%20Infrastructure%20Performance%7CRoot%7CCustom%20Metrics%7CExtension%20Starter" +
            "%20CI%7CCharacter%20Replacement%7CQuestionMark&time-range-type=BEFORE_NOW&duration-in-mins=10&output=JSON";
}
