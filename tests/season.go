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
	case Spring:
		return "Spring"
	}
	return "unknown"
}

func main() {
	x := Spring
	fmt.Println(x)
}