package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // create a new AList Ns to store the number of runs
        AList<Integer> Ns = new AList<Integer>();
        // create a new AList times to store run time (second)
        AList<Double> times = new AList<Double>();
        // create a new AList opCounts to store how many times need to run addLast
        AList<Integer> opCounts = new AList<Integer>();




        // run a for loop from 1000 to 128000
        //each time increment by 2 times the i
        //so the N should be 1000, 2000, 4000, 8000...
        for (int i = 1000; i <= 128000; i *= 2) {
            int numberOfOp = 0; // need to call how many times' addLast
            AList<Integer> size = new AList<>();
            // create a new stop watch
            Stopwatch sw = new Stopwatch();
            // create a variable track time in second
            for (int j = 0; j < i; j++) {
                // track what is the time for each addLast
                // ex. from 1 to 1000 add those time up
                size.addLast(j);
                // increased the call time for addLast
                numberOfOp++;
            }
            // add the N to the list Ns
            Ns.addLast(i);
            // add time to the times list
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            // add numbers of addLast run count in opCounts
            opCounts.addLast(numberOfOp);

        }

        printTimingTable(Ns, times, opCounts);

    }
}
