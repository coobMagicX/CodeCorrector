private void checkRequiredOptions() throws MissingOptionException {
    // if there are required options that have not been processed
    if (requiredOptions.size() > 0) {
        Iterator<Option> iter = requiredOptions.iterator();
        StringBuilder buff = new StringBuilder();

        // loop through the required options
        while (iter.hasNext()) {
            buff.append(iter.next().getOpt());
            if (iter.hasNext()) {
                buff.append(", ");
            }
        }

        throw new MissingOptionException("Missing required option(s): " + buff.toString());
    }
}