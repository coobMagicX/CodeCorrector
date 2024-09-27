public byte[] getBinaryValue(Base64Variant b64variant)
throws IOException, JsonParseException
{
    // Multiple possibilities...
    JsonNode n = currentNode();
    if (n != null) {
        // [databind#2096]: although `binaryValue()` works for real binary node
        // and embedded "POJO" node, coercion from TextNode may require variant, so:
        if (n.isBinary()) { // checking if it is a true binary node
            byte[] data = n.binaryValue();
            if (data != null) {
                return data;
            }
        }
        if (n.isPojo()) { // handling embedded POJO that could contain binary data
            Object ob = ((POJONode) n).getPojo();
            if (ob instanceof byte[]) {
                return (byte[]) ob;
            } else if (ob instanceof InetAddress) {
                return ((InetAddress) ob).getAddress();
            }
        }
        if (n.isTextual()) { // handling TextNode conversion using the provided Base64 variant
            String base64 = n.asText();
            return b64variant.decode(base64);
        }
    }
    // otherwise return null to mark we have no binary content
    return null;
}