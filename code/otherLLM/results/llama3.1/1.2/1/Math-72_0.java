public class Main {
    public static void main(String[] args) throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction function = x -> x * x - 4;
        
        double rootWithinInterval = new BrentSolver().solve(function, -5, 5);
        System.out.println("Root within interval: " + rootWithinInterval);

        double rootFromThreePoints = new BrentSolver().solve(function, 
                1, function.value(1), 
                2, function.value(2), 
                3, function.value(3));
        System.out.println("Root from three points: " + rootFromThreePoints);
    }
}