public List<String> getMatchingOptions(String opt) {
    opt = Util.stripLeadingHyphens(opt);
    
    List<String> matchingOpts = new ArrayList<String>();

    // Check for exact match first
    if (longOpts.containsKey(opt)) {
        matchingOpts.add(opt);
        return matchingOpts;
    }

    // If no exact match, find options that start with the given prefix
    for (String longOpt : longOpts.keySet()) {
        if (longOpt.startsWith(opt)) {
            matchingOpts.add(longOpt);
        }
    }
    
    return matchingOpts;
}