import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ArgumentParser {

    private static final Set<String> TRUES = new HashSet<>(Arrays.asList("true", "yes", "1"));
    private static final Set<String> FALSES = new HashSet<>(Arrays.asList("false", "no", "0"));

    public int parseArguments(Parameters params) throws CmdLineException {
        String param = params.getParameter(0);

        if (param == null) {
            setter.addValue(true);
            return 0;
        } else {
            String lowerParam = param.toLowerCase();
            if (TRUES.contains(lowerParam)) {
                setter.addValue(true);
            } else if (FALSES.contains(lowerParam)) {
                setter.addValue(false);
            } else {
                throw new CmdLineException("Invalid boolean value: " + param);
            }
            return 1;
        }
    }
}