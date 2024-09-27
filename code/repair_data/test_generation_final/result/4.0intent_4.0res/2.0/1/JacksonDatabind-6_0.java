protected Date parseAsISO8601(String dateStr, ParsePosition pos) {
    /* 21-May-2009, tatu: DateFormat has very strict handling of
     * timezone modifiers for ISO-8601. So we need to do some scrubbing.
     */

    int len = dateStr.length();
    char c = dateStr.charAt(len - 1);
    DateFormat df;

    if (len <= 10 && Character.isDigit(c)) {
        df = _formatPlain;
        if (df == null) {
            df = _formatPlain = _cloneFormat(DATE_FORMAT_PLAIN, DATE_FORMAT_STR_PLAIN, _timezone, _locale);
        }
    } else if (c == 'Z') {
        df = _formatISO8601_z;
        if (df == null) {
            df = _formatISO8601_z = _cloneFormat(DATE_FORMAT_ISO8601_Z, DATE_FORMAT_STR_ISO8601_Z, _timezone, _locale);
        }
        if (dateStr.charAt(len - 4) == ':') {
            StringBuilder sb = new StringBuilder(dateStr);
            sb.insert(len - 1, ".000");
            dateStr = sb.toString();
        }
    } else {
        if (hasTimeZone(dateStr)) {
            if (dateStr.charAt(len - 3) == ':') {
                StringBuilder sb = new StringBuilder(dateStr);
                sb.delete(len - 3, len - 2);
                dateStr = sb.toString();
            } else if (dateStr.charAt(len - 3) == '+' || dateStr.charAt(len - 3) == '-') {
                dateStr += "00";
            }
            int timeZoneIndex = dateStr.lastIndexOf('+') != -1 ? dateStr.lastIndexOf('+') : dateStr.lastIndexOf('-');
            int tIndex = dateStr.indexOf('T');
            if (timeZoneIndex > tIndex) {
                StringBuilder sb = new StringBuilder(dateStr);
                int dotIndex = dateStr.lastIndexOf('.', timeZoneIndex);
                int colonIndex = dateStr.lastIndexOf(':', timeZoneIndex);
                if (dotIndex < 0 || dotIndex < tIndex) {
                    if (colonIndex < 0 || colonIndex < tIndex) {
                        sb.insert(timeZoneIndex, ":00.000");
                    } else {
                        sb.insert(timeZoneIndex, ".000");
                    }
                    dateStr = sb.toString();
                } else {
                    int msEnd = dotIndex + 4;
                    if (msEnd > timeZoneIndex) {
                        sb.insert(timeZoneIndex, "000".substring(0, msEnd - timeZoneIndex));
                        dateStr = sb.toString();
                    }
                }
            }
            df = _formatISO8601;
            if (df == null) {
                df = _formatISO8601 = _cloneFormat(DATE_FORMAT_ISO8601, DATE_FORMAT_STR_ISO8601, _timezone, _locale);
            }
        } else {
            StringBuilder sb = new StringBuilder(dateStr);
            int timeLen = len - dateStr.lastIndexOf('T') - 1;
            if (timeLen <= 8) {
                if (timeLen == 5 || timeLen == 2) { // Only hour or hour and minutes
                    sb.append(":00"); // Add missing seconds
                }
                sb.append(".000");
            }
            sb.append('Z');
            dateStr = sb.toString();
            df = _formatISO8601_z;
            if (df == null) {
                df = _formatISO8601_z = _cloneFormat(DATE_FORMAT_ISO8601_Z, DATE_FORMAT_STR_ISO8601_Z, _timezone, _locale);
            }
        }
    }
    return df.parse(dateStr, pos);
}