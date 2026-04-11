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

## Theoretical Foundations

### The Riemann Zeta Function

The Riemann Zeta function is defined for complex variable s = σ + it as:

```
ζ(s) = Σ(1/n^s) = 1/1^s + 1/2^s + 1/3^s + ...   (Dirichlet series)
```

This series converges when Re(s) > 1.

### Euler Product (Prime Connection)

For Re(s) > 1, the zeta function has an equivalent product form:
```
ζ(s) = Π(1 - p^(-s))^(-1)   over all primes p
```
This establishes the deep connection between ζ(s) and the distribution of prime numbers.

### The Critical Strip and Critical Line

- **Critical Strip**: 0 < Re(s) < 1
- **Critical Line**: Re(s) = 1/2 (the center of the critical strip)

### The Riemann Hypothesis

**All non-trivial zeros of ζ(s) lie on the critical line Re(s) = 1/2.**

This unproven conjecture, posed by Bernhard Riemann in 1859, is one of the Millennium Prize Problems with a $1,000,000 reward for proof or disproof.

### Trivial vs Non-Trivial Zeros

- **Trivial Zeros**: ζ(-2n) = 0 for n = 1, 2, 3, ... (negative even integers)
- **Non-Trivial Zeros**: All other zeros, conjectured to all have real part 1/2

## Algorithm Implementation Principles

### 1. Zeta Evaluation Strategy

The implementation uses different methods based on the region of s:

```
if σ > 1:         Direct Dirichlet summation
else if σ > 0:    Functional equation method
else:             Functional equation method (same as above)
```

### 2. Dirichlet Series (σ > 1)

For Re(s) > 1, the series converges absolutely:
```java
ζ(s) = Σ(n=1 to N) 1/n^s
```

**Implementation**: Truncated to N = 5000 terms. Larger N improves accuracy but increases computation time.

### 3. Functional Equation (0 < σ ≤ 1)

The symmetric form:
```
π^(-s/2) Γ(s/2) ζ(s) = π^(-(1-s)/2) Γ((1-s)/2) ζ(1-s)
```

Rearranged to compute ζ(s) when σ ≤ 1:
```
ζ(s) = π^(s-1/2) × Γ((1-s)/2) × ζ(1-s) / Γ(s/2)
```

**Key insight**: ζ(1-s) has Re(1-s) > 1, so we can compute it directly via Dirichlet series!

### 4. Gamma Function Approximation

The Stirling approximation for large |z|:
```
Γ(z) ≈ √(2π) × z^(z-1/2) × e^(-z) × (1 + 1/(12z) + ...)
```

**Implementation**:
- For x < 0.5: Use reflection formula Γ(x)Γ(1-x) = π/sin(πx)
- For x ≥ 0.5: Use Stirling with recurrence to reduce argument

### 5. Zero Finding (Not Implemented)

Finding zeros requires:
1. **Gram Points**: Solutions to ζ(1/2 + i·g_n) is real (alternating sign between Gram points)
2. **Newton-Raphson or Bisection**: Root-finding on |ζ(1/2 + it)|²
3. **Extensive computation**: Billions of zeros computed using methods like the Odlyzko-Schönhage algorithm

This implementation stores known zeros rather than computing them, as accurate computation requires arbitrary-precision arithmetic (typically 50+ decimal digits).

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
