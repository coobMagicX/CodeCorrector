public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
    if (property == null) {
        return this;
    }
    JsonFormat.Value format = findFormatOverrides(serializers, property, handledType());
    if (format == null || (!format.hasPattern() && !format.hasLocale() && !format.hasTimeZone())) {
        DateFormat defaultDateFormat = serializers.getConfig().getDateFormat();
        if (defaultDateFormat instanceof SimpleDateFormat) {
            SimpleDateFormat df = (SimpleDateFormat) defaultDateFormat.clone();
            df.setTimeZone(serializers.getTimeZone());
            return withFormat(Boolean.FALSE, df);
        } else {
            return this; // Fallback to default if not a SimpleDateFormat
        }
    }
    // Simple case first: serialize as numeric timestamp?
    JsonFormat.Shape shape = format.getShape();
    if (shape.isNumeric()) {
        return withFormat(Boolean.TRUE, null);
    }

    // 08-Jun-2017, tatu: With [databind#1648], this gets bit tricky..
    // First: custom pattern will override things
    if ((shape == JsonFormat.Shape.STRING) || format.hasPattern() || format.hasLocale() || format.hasTimeZone()) {
        TimeZone tz = format.getTimeZone() != null ? format.getTimeZone() : serializers.getTimeZone();
        final String pattern = format.hasPattern() ? format.getPattern() : StdDateFormat.DATE_FORMAT_STR_ISO8601;
        final Locale loc = format.hasLocale() ? format.getLocale() : serializers.getLocale();
        SimpleDateFormat df = new SimpleDateFormat(pattern, loc);
        df.setTimeZone(tz);
        return withFormat(Boolean.FALSE, df);
    }

    // No custom pattern, locale, or timezone, so use the default DateFormat
    DateFormat defaultDateFormat = serializers.getConfig().getDateFormat();
    if (defaultDateFormat instanceof SimpleDateFormat) {
        SimpleDateFormat df = (SimpleDateFormat) defaultDateFormat.clone();
        df.setTimeZone(serializers.getTimeZone());
        return withFormat(Boolean.FALSE, df);
    } else {
        return this; // Fallback to default if not a SimpleDateFormat
    }
}