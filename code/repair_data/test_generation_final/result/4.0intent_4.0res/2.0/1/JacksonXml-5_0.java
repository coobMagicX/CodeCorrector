protected XmlSerializerProvider(XmlSerializerProvider src) {
    super(src);
    // 21-May-2018, tatu: As per [dataformat-xml#282], should NOT really copy
    //    root name lookup as that may link back to different version, configuration
    // We create a new instance of RootNameLookup to avoid linking back to the original configuration
    _rootNameLookup = new RootNameLookup();
}