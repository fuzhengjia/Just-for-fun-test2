# Riemann Zeta Function Zeros Calculator

[English](./README.md) | [中文](./README.zh-CN.md)

A Java implementation for computing and verifying the non-trivial zeros of the Riemann Zeta function on the critical line.

## Project Overview

This project calculates and displays the first 10 non-trivial zeros of the Riemann Zeta function ζ(s), which lie on the critical line Re(s) = 1/2 in the complex plane. These zeros are of profound importance in number theory, particularly in relation to the Riemann Hypothesis.

## Algorithm Key Logic

### 1. Dirichlet Series (Direct Summation)
For Re(s) > 1:
```
ζ(s) = Σ(1/n^s) for n=1 to ∞
```
The implementation uses a truncated series with 5000 terms.

### 2. Functional Equation
For Re(s) ≤ 1, the code uses the functional equation:
```
ζ(s) = π^(s-1/2) * Γ((1-s)/2) / ζ(1-s)
```
This allows computation of ζ(s) in the critical strip by relating it to values with Re(s) > 1.

### 3. Gamma Function Approximation
The Gamma function Γ(z) is approximated using Stirling's formula and recurrence relations for accurate computation in the functional equation.

## Project Structure

```
zeta-zeros/
├── pom.xml
├── src/main/java/com/zeta/
│   └── ZetaZeros.java
└── lib/
    └── commons-math3-3.6.1.jar
```

## Build & Run

```bash
mvn compile exec:java -Dexec.mainClass="com.zeta.ZetaZeros"
```

Or compile and run directly:
```bash
javac -cp "lib/*:src/main/java" src/main/java/com/zeta/ZetaZeros.java
java -cp "lib/*:src/main/java" com.zeta.ZetaZeros
```

## Output

The program outputs the first 10 non-trivial zeros on the critical line and verifies them by computing ζ(1/2 + it) ≈ 0.
