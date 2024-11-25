public class AdamsMoultonIntegrator {

  // ... (other methods and variables)

  private void sanityChecks(FirstOrderDifferentialEquations equations,
                             final double t0, final double[] y0,
                             final double t, final double[] y)
    throws DerivativeException, IntegratorException {
    // existing method
  }

  private void setEquations(FirstOrderDifferentialEquations equations) {
    // existing method
  }

  private void resetEvaluations() {
    // existing method
  }

  private boolean requiresDenseOutput() {
    // existing method
  }

  private AbstractStepInterpolator createInterpolator(boolean forward) {
    // existing method
  }

  private double estimateError(double[][] yDotK, double[] y, double[] yTmp, double stepSize) {
    // existing method
  }

  private void computeDerivatives(double t, double[] y, double[] yDotK) throws DerivativeException {
    // existing method
  }

  private double initializeStep(FirstOrderDifferentialEquations equations,
                                  boolean forward, int order,
                                  double[] scale, double t, double[] y,
                                  double[] yDotK0, double[] yTmp,
                                  double[] yDotK1)
      throws IntegratorException {
    // existing method
  }

  private void filterStep(double hNew, boolean forward, boolean lastStep) {
    // existing method
  }

}