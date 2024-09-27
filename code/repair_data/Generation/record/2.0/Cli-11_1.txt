private static void appendOption(final StringBuffer buff, 
                                 final Option option, 
                                 final boolean required)
{
    if (!required)
    {
        buff.append("[");
    }

    if (option.getOpt() != null && !option.getOpt().isEmpty())
    {
        buff.append("-").append(option.getOpt());
    }
    else if (option.getLongOpt() != null && !option.getLongOpt().isEmpty())
    {
        buff.append("--").append(option.getLongOpt());
    }

    // if the Option has a value
    if (option.hasArg() && (option.getArgName() != null) && !option.getArgName().isEmpty())
    {
        buff.append(" <").append(option.getArgName()).append(">");
    }

    // if the Option is not a required option
    if (!required)
    {
        buff.append("]");
    }
}