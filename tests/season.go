// from https://www.sohamkamani.com/golang/enums/
package main

import "fmt"

type Season int64

const (
	Summer Season = iota
	Autumn
	Winter
	Spring
)

func (s Season) String() string {
	switch s {
	case Summer:
		return "summer"
	case Autumn:
		return "autumn"
	case Winter:
		return "winter"
	}
	return "unknown"
}

func main() {
	x := Spring

	// will print "unknown"
	fmt.Println(x.String())
}
