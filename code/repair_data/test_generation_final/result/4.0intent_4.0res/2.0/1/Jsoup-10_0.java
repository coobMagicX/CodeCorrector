public String absUrl(String attributeKey) {
    Validate.notEmpty(attributeKey);

    String relUrl = attr(attributeKey);
    if (!hasAttr(attributeKey)) {
        return ""; // nothing to make absolute with
    } else {
        URL base;
        try {
            base = new URL(baseUri);
            // Identify if the relative URL is a query
            if (relUrl.startsWith("?")) {
                // Ensure the base URL ends with a file name and not a directory path
                String baseUrl = base.toExternalForm();
                // If the base URL ends with a '/', remove it unless it is the only path character
                if (baseUrl.endsWith("/") && baseUrl.lastIndexOf("/") > baseUrl.indexOf("//") + 1) {
                    baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
                }
                // Append the relative URL which starts with a query
                return baseUrl + relUrl;
            } else {
                // Normal resolution for non-query relative URLs
                URL abs = new URL(base, relUrl);
                return abs.toExternalForm();
            }
        } catch (MalformedURLException e) {
            // If the base URL is malformed, try to interpret the relative URL as an absolute URL
            try {
                URL abs = new URL(relUrl);
                return abs.toExternalForm();
            } catch (MalformedURLException e2) {
                return "";
            }
        }
    }
}