package main

// #cgo CFLAGS: -I${SRCDIR}/../../../../target
// #cgo LDFLAGS: -L${SRCDIR}/../../../../target
// #cgo LDFLAGS: -lmyapp
// #include <libmyapp.h>
import "C"

import "fmt"

func main() {
	fmt.Println("Hi from go")
	C.run_main(0, nil)

	isolate := C.Java_test_createIsolate()
	summator := C.Java_test_createSummator(isolate)
	for i := 1; i < 10; i++ {
		x := C.Java_test_callCalc(isolate, summator, C.double(1.0/float64(i)))
		fmt.Println(x)
	}
}
