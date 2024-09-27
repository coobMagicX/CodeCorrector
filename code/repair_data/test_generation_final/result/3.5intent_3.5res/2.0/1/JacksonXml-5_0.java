protected XmlSerializerProvider(XmlSerializerProvider src) {
    super(src);
    // 21-May-2018, tatu: As per [dataformat-xml#282], should NOT really copy
    // root name lookup as that may link back to diff version, configuration
    _rootNameLookup = null;
    // 29-Jun-2021, tatu: Exclude features that are disabled in the copy
    _formatFeatures = src._formatFeatures;
}