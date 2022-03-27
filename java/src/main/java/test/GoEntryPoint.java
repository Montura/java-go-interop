package test;

import org.graalvm.nativeimage.CurrentIsolate;
import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.ProcessProperties;
import org.graalvm.nativeimage.c.function.CEntryPoint;

import java.util.function.Supplier;

// nm -U ../../../target/libmyapp.dylib > libmyapp_symbols.txt

// $ otool -L ../../../target/libmyapp.dylib
// ../../../target/libmyapp.dylib:
//        /Users/Andrey.Mikhalev/Documents/git/java-go-interop/java/target/libmyapp.dylib (compatibility version 0.0.0, current version 0.0.0)
//        /usr/lib/libz.1.dylib (compatibility version 1.0.0, current version 1.2.11)
//        /System/Library/Frameworks/Foundation.framework/Versions/C/Foundation (compatibility version 300.0.0, current version 1775.118.101)
//        /usr/lib/libSystem.B.dylib (compatibility version 1.0.0, current version 1292.100.5)
//        /System/Library/Frameworks/CoreFoundation.framework/Versions/A/CoreFoundation (compatibility version 150.0.0, current version 1775.118.101)
//        /usr/lib/libobjc.A.dylib (compatibility version 1.0.0, current version 228.0.0)
// &



public class GoEntryPoint {
    static final String constString = "hello";

    // static block

    static final Supplier<String> staticObject = String::new;
    static String staticString = constString;
    static int staticInt = 1;

    static class AnotherClass {
        static final Supplier<String> staticObject = String::new;
        static String staticString = constString;
        static int staticInt = 1;
    }

    static {
        System.out.println("GoEntryPoint.clinit:");
        dumpStatic();
    }

    public static void main(String[] args) {
        System.out.println("GoEntryPoint.main: " + ProcessProperties.getExecutableName());
        dumpIsolate("  IsolateThread: 0x", CurrentIsolate.getCurrentThread());
        System.out.println("  staticInt = " + staticInt++ + " -> " + staticInt);
    }

    static void dumpIsolate(String s, IsolateThread currentThread) {
        System.out.println(s +
                Long.toHexString(currentThread.rawValue()));
    }

    @CEntryPoint(name = "graal_GoEntryPoint_entryPoint")
    static int entryPoint(IsolateThread thread) {
        dumpIsolate("GoEntryPoint.entryPoint: IsolateThread = 0x", thread);
        if (thread != CurrentIsolate.getCurrentThread()) {
            throw new RuntimeException("thread != CurrentIsolate.getCurrentThread()");
        }

        return staticInt;
    }

    static void dumpStatic() {
        System.out.println("  GoEntryPoint statics");
        System.out.println("    Object = " + staticObject);
        System.out.println("    String = " + staticString);
        System.out.println("    int = " + staticInt++ + " -> " + staticInt);
        System.out.println("  GoEntryPoint.AnotherClass statics");
        System.out.println("    Object = " + AnotherClass.staticObject);
        System.out.println("    String = " + AnotherClass.staticString);
        System.out.println("    int = " + AnotherClass.staticInt++ + " -> " + AnotherClass.staticInt);
    }

    @CEntryPoint(name = "graal_GoEntryPoint_dumpStatic")
    static void dumpStatic(IsolateThread thread) {
        dumpStatic();
    }

    @CEntryPoint(builtin=CEntryPoint.Builtin.CREATE_ISOLATE)
    static native IsolateThread createIsolate();
}
