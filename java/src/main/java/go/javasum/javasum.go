package javasum

// #cgo CFLAGS: -I${SRCDIR}/../../java/target
// #cgo LDFLAGS: -L${SRCDIR}/../../java/target
// #cgo LDFLAGS: -lmyapp
// #include <libmyapp.h>
// #include <stdlib.h>
import "C"

import (
	"unsafe"
)

type JavaSum struct {
	isolate  *C.graal_isolatethread_t
	summator unsafe.Pointer
}

func CreateJavaSum() JavaSum {
	var isolate *C.graal_isolatethread_t = C.Java_test_createIsolate()
	summator := C.Java_test_createSummator(isolate)
	return JavaSum{
		isolate:  isolate,
		summator: summator,
	}
}

func (s JavaSum) Calc(x float64) float64 {
	return float64(C.Java_test_callCalc(s.isolate, s.summator, C.double(x)))
}

func (s JavaSum) CalcNTimes(n int64, x float64) float64 {
	return float64(C.Java_test1_callCalcN(s.isolate, s.summator, C.double(x), C.longlong(n)))
}
