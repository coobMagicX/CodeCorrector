protected void _serializeXmlNull(JsonGenerator jgen) throws IOException {
    // 14-Nov-2016, tatu: As per [dataformat-xml#213], we may have explicitly
    //    configured root name...
    if (jgen instanceof ToXmlGenerator) {
        // Utilize the dynamic root name instead of a static ROOT_NAME_FOR_NULL
        String dynamicRootName = ((ToXmlGenerator) jgen).getDynamicRootName();
        _initWithRootName((ToXmlGenerator) jgen, dynamicRootName);
    }
    super.serializeValue(jgen, null);
}