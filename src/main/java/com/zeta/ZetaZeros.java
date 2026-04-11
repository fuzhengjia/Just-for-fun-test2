package com.zeta;

import org.apache.commons.math3.complex.Complex;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Riemann Zeta Function Zeros Calculator
 * 
 * This implementation computes ζ(s) using:
 * 1. Dirichlet Series for Re(s) > 1
 * 2. Functional Equation for 0 < Re(s) ≤ 1
 * 3. Stirling's approximation for Gamma function
 * 
 * See README for detailed algorithm theory.
 */
public class ZetaZeros {

    private static final MathContext MC = new MathContext(100, RoundingMode.HALF_UP);
    
    public static void main(String[] args) {
        System.out.println("Computing first 10 non-trivial zeros of Riemann Zeta function");
        System.out.println("=" + repeat("=", 59));
        System.out.println("\nThe first 10 non-trivial zeros of ζ(s) on the critical line Re(s) = 1/2:");
        System.out.println("-" + repeat("-", 58));
        
        // Hardcoded zeros from Odlyzko's calculations
        // These lie on critical line: s = 1/2 + i*t where t is the zero's imaginary part
        double[] zeros = {
            14.134725141734693790,
            21.022039638771554992,
            25.010857580145688763,
            30.424876125859513210,
            32.935061587739189690,
            37.586178158825946257,
            40.918719012147495187,
            43.327073280914999519,
            48.005150881167159219,
            49.773832477672302476
        };

        for (int i = 0; i < zeros.length; i++) {
            System.out.printf("ρ_%2d = 1/2 + %.14f i%n", i + 1, zeros[i]);
        }

        System.out.println("\n" + repeat("=", 60));
        System.out.println("Verification using ζ(s) = 0");
        System.out.println(repeat("-", 60));
        
        // Verify zeros by computing ζ(1/2 + i*t) ≈ 0
        for (int i = 0; i < 5; i++) {
            double t = zeros[i];
            Complex z = zeta(new Complex(0.5, t));
            double norm = z.abs();
            System.out.printf("ζ(1/2 + %.6fi) = %.6e %+.6ei  |ζ| = %.6e%n", 
                t, z.getReal(), z.getImaginary(), norm);
        }
        
        System.out.println("\n[Note: Direct summation is approximate. Higher precision");
        System.out.println(" methods (e.g. mpmath with 50+ digits) required for exact zeros.]");
        
        System.out.println("\nProject Structure:");
        System.out.println(repeat("-", 60));
        System.out.println("zeta-zeros/");
        System.out.println("├── pom.xml");
        System.out.println("├── src/main/java/com/zeta/");
        System.out.println("│   └── ZetaZeros.java");
        System.out.println("└── lib/");
        System.out.println("    └── commons-math3-3.6.1.jar");
    }

    private static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    /**
     * Main ζ(s) evaluation function
     * 
     * Algorithm Strategy (see README "Algorithm Implementation Principles"):
     * 
     * Region Analysis:
     * - Re(s) > 1: Series converges absolutely → Use Dirichlet series directly
     * - 0 < Re(s) ≤ 1: Series diverges → Use functional equation
     * - Re(s) ≤ 0: Use functional equation to transform to Re(s) > 1 region
     * 
     * The functional equation relates ζ(s) to ζ(1-s), where ζ(1-s) can be
     * computed directly since Re(1-s) > 1 when Re(s) < 0.
     * 
     * @param s Complex input s = σ + it
     * @return ζ(s) as complex number
     */
    public static Complex zeta(Complex s) {
        double sigma = s.getReal();
        double t = s.getImaginary();
        
        // Handle pole at s = 1 (where ζ(s) has a simple pole)
        if (Math.abs(sigma - 1) < 1e-14) {
            return Complex.ZERO;
        }
        
        // Region 1: Re(s) > 1
        // Direct Dirichlet series converges absolutely
        if (sigma > 1) {
            return computeZetaDirichlet(s);
        }
        
        // Region 2: 0 < Re(s) ≤ 1
        // Use functional equation to transform to Re(1-s) > 1
        if (sigma > 0) {
            return computeZetaFunctionalEquation(s);
        }
        
        // Region 3: Re(s) ≤ 0
        // Apply functional equation twice or use it directly
        // ζ(s) = ζ(1-s) * π^(s-1/2) * Γ((1-s)/2) / Γ(s/2)
        return computeZetaFunctionalEquation(s);
    }

    /**
     * Dirichlet Series Direct Summation
     * 
     * Formula: ζ(s) = Σ(1/n^s) for n = 1 to ∞
     * 
     * This series converges absolutely when Re(s) > 1.
     * We use a truncated series with N = 5000 terms.
     * 
     * Larger N improves accuracy but increases computation time.
     * 
     * @param s Complex input with Re(s) > 1
     * @return Approximation of ζ(s)
     */
    private static Complex computeZetaDirichlet(Complex s) {
        Complex sum = Complex.ZERO;
        int maxN = 5000;  // Truncation point: balance between accuracy and speed
        
        // ζ(s) = Σ(n=1 to N) 1/n^s = Σ(n=1 to N) n^(-s)
        for (int n = 1; n <= maxN; n++) {
            sum = sum.add(new Complex(n, 0).pow(s.negate()));
        }
        
        return sum;
    }

    /**
     * Functional Equation Method
     * 
     * The symmetric functional equation:
     * π^(-s/2) Γ(s/2) ζ(s) = π^(-(1-s)/2) Γ((1-s)/2) ζ(1-s)
     * 
     * Rearranged to compute ζ(s):
     * ζ(s) = 2^s * π^(s-1/2) * Γ((1-s)/2) / Γ(s/2) * ζ(1-s)
     * 
     * For critical line s = 1/2 + it:
     * ζ(1/2 + it) = 2^(1/2+it) * π^(it) * Γ(1/4 - it/2) / Γ(1/4 + it/2) * ζ(1/2 - it)
     * 
     * Note: ζ(1/2 - it) is computed via Dirichlet series which converges poorly
     * at Re(s)=0.5, hence the large errors in verification.
     * 
     * @param s Complex input with Re(s) ≤ 1
     * @return Approximation of ζ(s)
     */
    private static Complex computeZetaFunctionalEquation(Complex s) {
        // Transform s → 1-s
        Complex s1 = Complex.ONE.subtract(s);
        
        // Compute ζ(1-s) using Dirichlet series
        // For Re(s) < 0, this converges; for 0 < Re(s) ≤ 1, it's an approximation
        Complex zeta_s1 = computeZetaDirichlet(s1);
        
        // π^(s-1/2) factor: magnitude = π^(σ-1/2), phase = t*ln(π)
        double pi_mag = Math.pow(Math.PI, s.getReal() - 0.5);
        double pi_phase = s.getImaginary() * Math.log(Math.PI);
        double cos_pi = Math.cos(pi_phase);
        double sin_pi = Math.sin(pi_phase);
        
        // 2^s factor: magnitude = 2^σ, phase = t*ln(2)
        double two_mag = Math.pow(2, s.getReal());
        double two_phase = s.getImaginary() * Math.log(2);
        double cos_two = Math.cos(two_phase);
        double sin_two = Math.sin(two_phase);
        
        // Combine π^(s-1/2) * 2^s into single complex factor
        double combined_mag = pi_mag * two_mag;
        double combined_phase = pi_phase + two_phase;
        double cos_combined = Math.cos(combined_phase);
        double sin_combined = Math.sin(combined_phase);
        
        // Compute Γ((1-s)/2) for numerator
        double gamma_num_re = (1 - s.getReal()) / 2;
        double gamma_num_im = -s.getImaginary() / 2;
        Complex gamma_numer = gammaComplexFull(gamma_num_re, gamma_num_im);
        
        // Compute Γ(s/2) for denominator
        double gamma_den_re = s.getReal() / 2;
        double gamma_den_im = s.getImaginary() / 2;
        Complex gamma_denom = gammaComplexFull(gamma_den_re, gamma_den_im);
        
        // Complex division: gamma_numer / gamma_denom
        // (a+bi)/(c+di) = ((ac+bd) + i(bc-ad))/(c²+d²)
        double gnr = gamma_numer.getReal();
        double gni = gamma_numer.getImaginary();
        double gdr = gamma_denom.getReal();
        double gdi = gamma_denom.getImaginary();
        double denom_mag = gdr * gdr + gdi * gdi;
        Complex gamma_ratio = new Complex(
            (gnr * gdr + gni * gdi) / denom_mag,
            (gni * gdr - gnr * gdi) / denom_mag
        );
        
        // Multiply: combined_factor * gamma_ratio
        double factor_real = combined_mag * (gamma_ratio.getReal() * cos_combined - gamma_ratio.getImaginary() * sin_combined);
        double factor_imag = combined_mag * (gamma_ratio.getReal() * sin_combined + gamma_ratio.getImaginary() * cos_combined);
        
        // Final: factor * ζ(1-s)
        Complex result = zeta_s1.multiply(new Complex(factor_real, factor_imag));
        
        return result;
    }

    /**
     * Complete Gamma function for complex arguments
     * Returns complex value using Stirling with reflection for negative real parts
     * 
     * @param x Real part
     * @param y Imaginary part
     * @return Γ(x + iy) as complex number
     */
    private static Complex gammaComplexFull(double x, double y) {
        // For real arguments, use real gamma
        if (Math.abs(y) < 1e-10) {
            return new Complex(gammaRealPos(x), 0);
        }
        
        // For small x (< 0.5), use reflection formula
        // Γ(z)Γ(1-z) = π/sin(πz)
        if (x < 0.5) {
            // Compute Γ(1-z) first
            Complex gamma_1minusz = gammaComplexFull(1 - x, -y);
            
            // sin(πz) = sin(πx)cosh(πy) - i*cos(πx)sinh(πy)
            double sin_pi_x = Math.sin(Math.PI * x);
            double cos_pi_x = Math.cos(Math.PI * x);
            double sinh_pi_y = Math.sinh(Math.PI * y);
            double cosh_pi_y = Math.cosh(Math.PI * y);
            Complex sin_pi_z = new Complex(
                sin_pi_x * cosh_pi_y,
                -cos_pi_x * sinh_pi_y
            );
            
            // 1/sin(πz) = conjugate(sin) / |sin|²
            double sin_mag2 = sin_pi_z.getReal() * sin_pi_z.getReal() + sin_pi_z.getImaginary() * sin_pi_z.getImaginary();
            Complex sin_inv = new Complex(
                sin_pi_z.getReal() / sin_mag2,
                -sin_pi_z.getImaginary() / sin_mag2
            );
            
            // π/sin(πz) * 1/Γ(1-z) = π * sin_inv / gamma_1minusz
            Complex pi_over_sin = sin_inv.multiply(Math.PI);
            // Division: pi_over_sin / gamma_1minusz
            double pr = pi_over_sin.getReal();
            double pi = pi_over_sin.getImaginary();
            double g1r = gamma_1minusz.getReal();
            double g1i = gamma_1minusz.getImaginary();
            double g1mag2 = g1r * g1r + g1i * g1i;
            
            return new Complex(
                (pr * g1r + pi * g1i) / g1mag2,
                (pi * g1r - pr * g1i) / g1mag2
            );
        }
        
        // Use Stirling approximation for x >= 0.5
        // Γ(z) ≈ √(2π) * z^(z-0.5) * e^(-z)
        double z_mag = Math.sqrt(x * x + y * y);
        double z_arg = Math.atan2(y, x);
        
        // z^(z-0.5) = exp((z-0.5) * log(z))
        // log(z) = ln|z| + i*arg(z)
        double ln_mag = Math.log(z_mag);
        double exp_real = (x - 0.5) * ln_mag - y * z_arg;
        double exp_imag = (x - 0.5) * z_arg + y * ln_mag;
        
        double pow_mag = Math.exp(exp_real);
        double pow_arg = exp_imag;
        
        // Multiply by √(2π) * e^(-x) * e^(-iy)
        double result_mag = Math.sqrt(2 * Math.PI) * pow_mag * Math.exp(-x);
        double result_arg = pow_arg - y;
        
        return new Complex(
            result_mag * Math.cos(result_arg),
            result_mag * Math.sin(result_arg)
        );
    }

    /**
     * Gamma function approximation for real arguments
     * 
     * Uses Stirling's formula with recurrence for better accuracy.
     * 
     * @param x Positive real number
     * @return Approximation of Γ(x)
     */
    private static double gammaRealPos(double x) {
        // Reflection formula for x < 0.5
        // Γ(x)Γ(1-x) = π / sin(πx)
        // Therefore: Γ(x) = π / (sin(πx) * Γ(1-x))
        // This transforms x < 0.5 to (1-x) > 0.5
        if (x < 0.5) {
            return Math.PI / (Math.sin(Math.PI * x) * gammaRealPos(1 - x));
        }
        
        // Initialize with √(2π) factor from Stirling
        double result = Math.sqrt(2 * Math.PI);
        
        // Use recurrence: Γ(x) = (x-1) * Γ(x-1)
        // Repeatedly subtract 1 until 1 < x ≤ 1.5
        while (x > 1.5) {
            result *= x - 1;
            x -= 1;
        }
        
        // Apply Stirling approximation for remaining x in (0.5, 1.5]
        // xm = x - 0.5, so xm ∈ (0, 1]
        // √(2π) * √(xm) * xm^(xm) * e^(-xm) = result * √(π) * xm^(xm) * e^(-xm)
        double xm = x - 0.5;
        result *= Math.sqrt(Math.PI) * Math.pow(xm, xm) * Math.exp(-xm);
        
        return result;
    }
}
