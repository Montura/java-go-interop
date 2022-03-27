package main

import "fmt"
import "unsafe"
// #cgo CFLAGS: -I${SRCDIR}/../../../../target
// #cgo LDFLAGS: -L${SRCDIR}/../../../../target
// #cgo LDFLAGS: -lmyapp
// #include <libmyapp.h>
//
// static graal_isolatethread_t* createGraalVM()  {
//    return GoEntryPoint__createIsolate__47680a780ae370686adc21cf4dae918cc6b51ef6();
// }
import "C"



func main() {
  fmt.Println("Hi from go")
  C.run_main(0, nil)

  isolate := C.createGraalVM()
  fmt.Println(unsafe.Pointer(isolate))
}

