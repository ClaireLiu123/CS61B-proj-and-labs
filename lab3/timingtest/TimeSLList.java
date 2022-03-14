package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
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
        timeGetLast();
    }

    public static void timeGetLast() {
        // create a new AList Ns to store the number of runs
        AList<Integer> Ns = new AList<Integer>();
        // create a new AList times to store run time (second)
        AList<Double> times = new AList<Double>();
        // create a new AList opCounts to store how many times need to run addLast
        AList<Integer> opCounts = new AList<Integer>();

        // M = 10000;
        int m = 10000;
        for(int i = 1000; i < 128000; i *= 2){
            SLList<Integer> newList = new SLList<>();
            Stopwatch sw = new Stopwatch();
            for(int j = 0; j < i; j++){
                // add every number from 0 to 1000 to the list
                newList.addLast(i);
            }
            Ns.addLast(i);
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            opCounts.addLast(m);

        }
        printTimingTable(Ns, times, opCounts);
    }

}
