protected void _serializeXmlNull(JsonGenerator jgen) throws IOException {
    // 14-Nov-2016, tatu: As per [dataformat-xml#213], we may have explicitly
    //    configured root name...
    if (jgen instanceof ToXmlGenerator) {
        ToXmlGenerator xgen = (ToXmlGenerator) jgen;
        QName customRootName = _rootNameFromConfig(); // Assume this method fetches the current configured root name
        if (customRootName == null) {
            customRootName = ROOT_NAME_FOR_NULL; // Fallback to default if no configuration is present
        }
        _initWithRootName(xgen, customRootName);
    }
    super.serializeValue(jgen, null);
}