package cz.swisz.so.task5;

import cz.swisz.so.task5.strategy.Strategy1;
import cz.swisz.so.task5.strategy.Strategy2;
import cz.swisz.so.task5.strategy.Strategy3;

import java.util.ArrayList;

public class Main {
    private static void testStrategy(String strategyName, DistributionSimulation simulation) {
        simulation.start();

        System.out.println(strategyName);
        System.out.print("Sr obciazenie procesorow: ");

        double totalAverageLoad = 0.0;
        for (CPU cpu : simulation.getCpus()) {
            System.out.printf("%.1f%% ", cpu.getAverageLoad() * 100.0);
            totalAverageLoad += cpu.getAverageLoad();
        }
        System.out.println();

        totalAverageLoad /= simulation.getCpus().size();

        System.out.printf("Globalne sr obciazenie: %.1f%%\n", totalAverageLoad * 100.0);

        double stDev = 0.0;
        int loadReqs = 0;
        for (CPU cpu : simulation.getCpus()) {
            stDev += Math.pow(cpu.getAverageLoad() - totalAverageLoad, 2.0);
            loadReqs += cpu.getLoadRequestCount();
        }
        stDev /= simulation.getCpus().size();
        stDev = Math.sqrt(stDev);

        System.out.printf("Odchylenie standardowe: %.1f%%\n", stDev * 100.0);
        System.out.printf("Zapytania o obciazenie: %d\n", loadReqs);
        System.out.printf("Liczba migracji: %d\n", simulation.getMigrationCount());
        System.out.printf("Laczny czas: %.2f\n", simulation.getTotalTime());
        System.out.println();
    }

    private static ArrayList<Process> cloneList(ArrayList<Process> original) {
        ArrayList<Process> newList = new ArrayList<>();

        for (Process p : original) {
            newList.add(p.cloneProcess());
        }

        return newList;
    }


    public static void main(String[] args) {
        final int N = 10;
        final double p = 0.7; // threshold
        final double r = 0.4; // strat3 threshold
        final int z = 24; // retry count

        ArrayList<Process> generated = new ArrayList<>();
        Generator.generateProcessList(generated, N, 500);

        testStrategy("Strategia 1", new Strategy1(cloneList(generated), N, p, z));
        testStrategy("Strategia 2", new Strategy2(cloneList(generated), N, p, z));
        testStrategy("Strategia 3", new Strategy3(cloneList(generated), N, p, r));
    }
}
