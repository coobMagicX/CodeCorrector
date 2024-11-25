public class LevenbergMarquardtOptimizer {
    // ...

    public void updateResidualsAndCost() {
        // Update residual values and calculate cost
        // ...
    }

    public VectorialPointValuePair newVectorialPointValuePair(double[] point, double objective) {
        return new VectorialPointValuePair(point, objective);
    }

    private boolean checkerConverged(int iterations, VectorialPointValuePair previous, VectorialPointValuePair current) {
        if (checker == null) {
            // Use default convergence checker
            return Math.abs(current.getObjective() - previous.getObjective()) <= costRelativeTolerance &&
                   Math.abs(current.getResiduals()[0]) <= parRelativeTolerance * xNorm;
        } else {
            // Use user-defined convergence checker
            return checker.converged(iterations, previous, current);
        }
    }

    private boolean checkConvergence(VectorialPointValuePair current) {
        if (Math.abs(current.getObjective() - previous.getObjective()) <= costRelativeTolerance &&
            Math.abs(current.getResiduals()[0]) <= parRelativeTolerance * xNorm) {
            return true;
        } else {
            return false;
        }
    }

    private void updateStepBound(double ratio, double delta, double lmPar, double[] point, double[] residuals) {
        if (ratio <= 0.25) {
            // Update step bound based on ratio
            delta = (actRed < 0) ? (0.5 * dirDer / (dirDer + 0.5 * actRed)) : 0.5;
            if ((0.1 * cost >= previousCost) || (tmp < 0.1)) {
                tmp = 0.1;
            }
            delta = tmp * Math.min(delta, 10.0 * lmNorm);
            lmPar /= tmp;
        } else if ((lmPar == 0) || (ratio >= 0.75)) {
            // Update step bound based on ratio
            delta = 2 * lmNorm;
            lmPar *= 0.5;
        }
    }

    public void optimize() {
        while (!checkConvergence(current)) {
            // ...
        }
    }
}