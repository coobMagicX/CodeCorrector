private static void appendOption(final StringBuffer buff, 
                                 final Option option, 
                                 final boolean required)
{
    if (!required)
    {
        buff.append("[");
    }

    if (option.getOpt() != null)
    {
        buff.append("-").append(option.getOpt());
    }
    else
    {
        buff.append("--").append(option.getLongOpt());
    }

    // if the Option has a value and the argName is not empty
    if (option.hasArg() && (option.getArgName() != null) && !option.getArgName().isEmpty())
    {
        buff.append(" <").append(option.getArgName()).append(">");
    }

    if (!required)
    {
        buff.append("]");
    }
}