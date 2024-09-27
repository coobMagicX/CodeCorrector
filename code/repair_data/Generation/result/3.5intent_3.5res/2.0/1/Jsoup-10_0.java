public String absUrl(String attributeKey) {
    Validate.notEmpty(attributeKey);

    String relUrl = attr(attributeKey);
    if (!hasAttr(attributeKey)) {
        return ""; // nothing to make absolute with
    } else {
        try {
            URL abs = new URL(baseUri);

            if (!relUrl.startsWith("?") && !relUrl.startsWith("/")) { // check if the relUrl is a query URL or an absolute URL
                URL base = new URL(baseUri + "/");
                abs = new URL(base, relUrl);
            }
            return abs.toExternalForm();
        } catch (MalformedURLException e) {
            return "";
        }
    }
}