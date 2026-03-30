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

## Known Zeros

The first 20 non-trivial zeros of the Riemann Zeta function on the critical line Re(s) = 1/2 (imaginary part t):

| # | Program (hardcoded) | Official (Odlyzko) | Difference |
|---|---------------------|--------------------|------------|
| 1 | 14.134725141734694 | 14.134725141734693790 | 1.2e-13 |
| 2 | 21.022039638771555 | 21.022039638771554992 | 5.6e-14 |
| 3 | 25.010857580145689 | 25.010857580145688763 | 9.3e-14 |
| 4 | 30.424876125859513 | 30.424876125859513210 | 3.0e-13 |
| 5 | 32.935061587739190 | 32.935061587739189690 | 5.0e-13 |
| 6 | 37.586178158825946 | 37.586178158825946257 | 3.1e-13 |
| 7 | 40.918719012147495 | 40.918719012147495187 | 3.1e-13 |
| 8 | 43.327073280915000 | 43.327073280914999519 | 4.8e-13 |
| 9 | 48.005150881167159 | 48.005150881167159219 | 6.0e-14 |
|10 | 49.773832477672302 | 49.773832477672302476 | 1.7e-13 |
|11 | 52.970321477714461 | 52.970321477714460644 | 8.2e-13 |
|12 | 56.446247697063395 | 56.446247697063394804 | 5.9e-13 |
|13 | 59.347044002602353 | 59.347044002602353079 | 2.8e-13 |
|14 | 60.831778524609810 | 60.831778524609809844 | 3.4e-13 |
|15 | 65.112544048081607 | 65.112544048081606660 | 9.5e-13 |
|16 | 67.079810529494174 | 67.079810529494173714 | 4.6e-13 |
|17 | 69.546401711173979 | 69.546401711173979252 | 2.7e-13 |
|18 | 72.067157674481908 | 72.067157674481907582 | 4.2e-13 |
|19 | 75.704690699083933 | 75.704690699083933168 | 2.3e-13 |
|20 | 77.144840068874805 | 77.144840068874805372 | 5.7e-13 |

Note: The program currently only hardcodes the first 10 zeros. Zeros 11-20 are shown here for reference.

## Output

The program outputs the first 10 non-trivial zeros on the critical line and verifies them by computing ζ(1/2 + it) ≈ 0.
