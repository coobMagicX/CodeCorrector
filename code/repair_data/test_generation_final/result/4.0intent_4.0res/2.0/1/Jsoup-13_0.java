public Node attr(String attributeKey, String attributeValue) {
    if (attributeKey.equals("abs:href")) {
        attributeValue = resolveAbsoluteURL(attributeValue);
    }
    attributes.put(attributeKey, attributeValue);
    return this;
}

private String resolveAbsoluteURL(String url) {
    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        // Assuming 'baseURL' is a field or can be obtained somehow
        url = baseURL + url;
    }
    return url;
}