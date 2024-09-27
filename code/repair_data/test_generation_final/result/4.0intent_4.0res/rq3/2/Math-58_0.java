import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.fitting.GaussianCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

public class GaussianFitter {
    private WeightedObservedPoints observations;

    public GaussianFitter(WeightedObservedPoints observations) {
        this.observations = observations;
    }

    public double[] fit() {
        GaussianCurveFitter fitter = GaussianCurveFitter.create();
        double[] guess = (new ParameterGuesser(observations)).guess();
        return fitter.fit(observations.toList());
    }

    private class ParameterGuesser {
        private WeightedObservedPoints observations;

        public ParameterGuesser(WeightedObservedPoints observations) {
            this.observations = observations;
        }

        public double[] guess() {
            // Implement guessing logic here (example: calculate initial mean, sigma, and norm)
            double[] defaultGuess = {1.0, 1.0, 1.0}; // Example default guess for amplitude, mean, and sigma
            return defaultGuess;
        }
    }

    private WeightedObservedPoints getObservations() {
        return observations;
    }

    public static void main(String[] args) {
        // Example usage
        WeightedObservedPoints obs = new WeightedObservedPoints();
        obs.add(1.0, 2.0);
        obs.add(2.0, 3.0);
        obs.add(3.0, 4.0);

        GaussianFitter fitter = new GaussianFitter(obs);
        double[] parameters = fitter.fit();
        System.out.println("Fitted parameters: Amplitude = " + parameters[0] + ", Mean = " + parameters[1] + ", Sigma = " + parameters[2]);
    }
}