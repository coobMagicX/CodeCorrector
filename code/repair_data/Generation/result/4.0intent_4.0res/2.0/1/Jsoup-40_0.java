public class DocumentType extends Node {
    public DocumentType(String name, String publicId, String systemId, String baseUri) {
        super(baseUri);
        
        // Remove the validation that checks if 'name' is not empty to allow empty strings.
        attr("name", name);
        attr("publicId", publicId);
        attr("systemId", systemId);
    }

    public String nodeName() {
        return "#doctype";
    }
}