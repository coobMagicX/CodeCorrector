public class RootFinder {
    public double findRoot(double[] x, double[] y, int nbPoints, AllowedSolutions allowedSolutions) {
        // Implement the root-finding algorithm using the provided methods

        // ...
        
        return nextX;
    }

    public interface AllowedSolutions {
        void handleSolution(double root);
    }
    
    public static class MathInternalError extends Exception {
        public MathInternalError(String message) {
            super(message);
        }
    }
}