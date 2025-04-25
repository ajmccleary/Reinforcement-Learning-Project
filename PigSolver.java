public class PigSolver {
    int goal;
    double epsilon;
    double[][][] p;
    boolean[][][] flip;

    // constructor
    PigSolver(int goal, double epsilon) {
        //initialize variables
        this.goal = goal;
        this.epsilon = epsilon;
        p = new double[goal][goal][goal];
        flip = new boolean[goal][goal][goal];

        valueIterate();
    }

    void valueIterate() {
      double maxChange;
        do {
            maxChange = 0.0;
            for (int i = 0; i < goal; i++) // for all i
                for (int j = 0; j < goal; j++) // for all j
                    for (int k = 0; k < goal - i; k++) { // for all k
                        double oldProb = p[i][j][k];
                        //changed to utilize probabilities of winning upon flipping and holding from Q1
                        double pFlip = (
                            (1.0 - pWin(j, i, 0))
                            + pWin(i, j, k + 2)
                            + pWin(i, j, k + 3)
                            + pWin(i, j, k + 4)
                            + pWin(i, j, k + 5)
                            + pWin(i, j, k + 6)
                            ) / 6;
                        
                        double pHold = 1.0 - pWin(j, i + k, 0);

                        //System.out.println("i=" + i + ", j=" + j + ", k=" + k + ": pFlip=" + pFlip + ", pHold=" + pHold);

                        p[i][j][k] = Math.max(pFlip, pHold);
                        flip[i][j][k] = pFlip > pHold;
                        double change = Math.abs(p[i][j][k] - oldProb);
                        maxChange = Math.max(maxChange, change);
                    }
        } while (maxChange >= epsilon);
    }

    public double pWin(int i, int j, int k) { 
        if (i + k >= goal)
            return 1.0;
        else if (j >= goal)
            return 0.0;
        else return p[i][j][k];
    }

    public void outputHoldValues() {
        for (int i = 0; i < goal; i++) {
            for (int j = 0; j < goal; j++) {
                int k = 0;
                while (k < goal - i && flip[i][j][k])
                    k++;    
                System.out.print(k + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        new PigSolver(100, 1e-9).outputHoldValues();
    }
}
