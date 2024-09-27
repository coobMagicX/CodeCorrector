public boolean hasAttr(String attributeKey) {
    Validate.notNull(attributeKey);
    
    if (attributeKey.startsWith("abs:")) {
        attributeKey = attributeKey.substring(4);
        return attributes.hasKey(attributeKey) || attributes.hasKey("abs:" + attributeKey);
    } else if (attributeKey.contains(":")) {
        return attributes.hasKey(attributeKey) || attributes.hasKey(attributeKey.replace(":", ""));
    } else {
        return attributes.hasKey(attributeKey);
    }
}