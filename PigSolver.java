/**
 * PigSolver is a class that uses value iteration to solve the game of Pig.
 * It calculates the optimal strategy and the probability of winning
 * given the current scores and turn total.
 */
public class PigSolver {
    int goal; // The score a player needs to reach to win the game
    double epsilon; // Convergence threshold for value iteration
    double[][][] p; // 3D array to store probabilities of winning
    boolean[][][] flip; // 3D array to store whether rolling is better than holding

    /**
     * Constructor for PigSolver.
     * Initializes the goal, epsilon, and arrays for probabilities and actions.
     * Also starts the value iteration process.
     *
     * @param goal    The score needed to win the game
     * @param epsilon The convergence threshold for value iteration
     */
    PigSolver(int goal, double epsilon) {
        this.goal = goal;
        this.epsilon = epsilon;
        p = new double[goal][goal][goal];
        flip = new boolean[goal][goal][goal];

        // Perform value iteration to compute optimal strategies
        valueIterate();
    }

    /**
     * Performs value iteration to compute the probabilities of winning
     * and the optimal actions (roll or hold) for each state.
     */
    void valueIterate() {
        double maxChange; //Tracks the maximum change in probabilities during iteration
        do {
            maxChange = 0.0;
            //Iterates over all possible states
            for (int i = 0; i < goal; i++) {
                for (int j = 0; j < goal; j++) {
                    for (int k = 0; k < goal - i; k++) {
                        double oldProb = p[i][j][k]; //Stores the old probability for comparison

                        // --- Roll (flip) action ---
                        // Pig-out on rolling a 1: lose turn total, opponent plays with same scores
                        double pigOut = (1.0 / 6) * (1.0 - pWin(j, i, 0));
                        //Adds the face value to turn total and continue
                        double continueRoll = 0.0;
                        for (int face = 2; face <= 6; face++) {
                            continueRoll += (1.0 / 6) * pWin(i, j, k + face);
                        }
                        double pFlip = pigOut + continueRoll;

                        
                        //Hold and let the opponent play
                        double pHold = 1.0 - pWin(j, i + k, 0);

                        //Choose the best action either roll or hold 
                        p[i][j][k] = Math.max(pFlip, pHold);
                        flip[i][j][k] = (pFlip > pHold);

            
                        maxChange = Math.max(maxChange, Math.abs(p[i][j][k] - oldProb));
                    }
                }
            }
        } while (maxChange >= epsilon); //Continues until changes are below the threshold
    }

    /**
     * Computes the probability of winning given the current state.
     *
     * @param i Player's current score
     * @param j Opponent's current score
     * @param k Turn total
     * @return Probability of winning from the given state
     */
    public double pWin(int i, int j, int k) {
        if (i + k >= goal) 
            return 1.0;
        else if (j >= goal) 
            return 0.0;
        else
            return p[i][j][k]; 
    }

    /**
     * Outputs the hold values table, showing the optimal turn total
     * at which the player should hold for each state.
     */
    public void outputHoldValues() {
        for (int i = 0; i < goal; i++) {
            for (int j = 0; j < goal; j++) {
                int k = 0;
                //Finds the first turn total where holding is better than rolling
                while (k < goal - i && flip[i][j][k])
                    k++;
                System.out.print(k + " "); 
            }
            System.out.println();
        }
    }

   
    public static void main(String[] args) {
        PigSolver solver = new PigSolver(100, 1e-9);

        //Computes the probability of winning at the start of the game
        double startProb = solver.pWin(0, 0, 0);

        
        System.out.printf("Win probability at start: %.6f%n", startProb);

        //Output the hold values table
        //solver.outputHoldValues();
    }
}