package test;

import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.c.function.CEntryPoint;

public class Summator {
    private double sum;

    public double calc(double x) {
        sum += x;
        return sum;
    }

    @CEntryPoint(name = "Java_test_callCalc")
    public static double callCalc(IsolateThread isolate, Summator s, double x) {
        return s.calc(x);
    }

    @CEntryPoint(name = "Java_test_callCalcN")
    public static double callCalcN(IsolateThread isolate, Summator s, double x, long n) {
        for (int i = 0; i < n-1; i++) {
            s.calc(x);
        }
        return s.calc(x);
    }

    @CEntryPoint(name = "Java_test_createSummator")
    public static Summator createSummator(IsolateThread isolate) {
        return new Summator();
    }

    @CEntryPoint(name = "Java_test_createIsolate", builtin = CEntryPoint.Builtin.CREATE_ISOLATE)
    public static native IsolateThread createIsolate();
}
