private void checkRequiredOptions()
    throws MissingOptionException
{
    // if there are required options that have not been
    // processed
    if (requiredOptions.size() > 0)
    {
        Iterator iter = requiredOptions.iterator();
        StringBuffer buff = new StringBuffer("Missing required option: ");

        // loop through the required options
        while (iter.hasNext())
        {
            if (buff.length() > 26) { // "Missing required option: " is 26 characters long
                buff.append(", ");
            }
            buff.append(iter.next());
        }

        throw new MissingOptionException(buff.toString());
    }
}