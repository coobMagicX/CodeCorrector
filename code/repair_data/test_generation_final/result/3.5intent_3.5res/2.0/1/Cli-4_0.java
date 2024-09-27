private void checkRequiredOptions() throws MissingOptionException {
    // if there are required options that have not been processsed
    if (requiredOptions.size() > 0) {
        Iterator<String> iter = requiredOptions.iterator();
        List<String> missingOptions = new ArrayList<>();

        // loop through the required options
        while (iter.hasNext()) {
            String option = iter.next();
            if (!processedOptions.contains(option)) {
                missingOptions.add(option);
            }
        }

        if (!missingOptions.isEmpty()) {
            throw new MissingOptionException("Missing required options: " + String.join(", ", missingOptions));
        }
    }
}