import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;

public class Solver {
    private UnivariateRealSolver solver;

    public Solver() {
        UnivariateRealSolverFactory factory = UnivariateRealSolverFactory.newInstance();
        this.solver = factory.newDefaultSolver();
    }

    public double solve(final UnivariateRealFunction f, double min, double max, double initial) 
    throws MaxIterationsExceededException, FunctionEvaluationException {
        solver.setFunction(f);
        return solver.solve(min, max, initial);
    }
}