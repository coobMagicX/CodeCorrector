public DocumentType(String name, String publicId, String systemId, String baseUri) {
    super(baseUri);

    if (name == null || name.isEmpty()) {
        name = "Untitled";
    }
    if (publicId == null || publicId.isEmpty()) {
        publicId = "";
    }
    if (systemId == null || systemId.isEmpty()) {
        systemId = "";
    }
    
    attr("name", name);
    attr("publicId", publicId);
    attr("systemId", systemId);
}