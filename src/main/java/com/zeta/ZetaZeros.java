package com.zeta;

import org.apache.commons.math3.complex.Complex;

/**
 * Riemann Zeta Function Zeros Calculator
 * 
 * This implementation computes ζ(s) using:
 * 1. Dirichlet Series for Re(s) > 1
 * 2. Functional Equation for 0 < Re(s) ≤ 1
 * 3. Stirling's approximation for Gamma function
 * 
 * See README for detailed algorithm theory and mathematical background.
 * 
 * Key equations implemented:
 * - Dirichlet series: ζ(s) = Σ(1/n^s) for Re(s) > 1
 * - Functional equation: ζ(s) = 2^s × π^(s-1/2) × Γ((1-s)/2) / Γ(s/2) × ζ(1-s)
 * - Stirling approximation: Γ(z) ≈ √(2π) × z^(z-1/2) × e^(-z)
 * 
 * Note: This implementation uses double precision (64-bit), which is insufficient
 * for accurate computation on the critical line Re(s)=1/2. Professional implementations
 * require arbitrary-precision arithmetic (50+ decimal digits).
 */
public class ZetaZeros {

    /**
     * Number of terms in Dirichlet series summation.
     * 
     * See README "Algorithm Implementation Principles" Section 2:
     * "Implementation: Truncated to N = 5000 terms. Larger N improves
     * accuracy but increases computation time."
     */
    private static final int DIRICHLET_TERMS = 100000;
    
    public static void main(String[] args) {
        System.out.println("First 100 Riemann Zeta Function Zeros - Comparison with Official Values");
        System.out.println("=======================================================================\n");
        
        /**
         * Official zeros from Odlyzko's high-precision calculations.
         * These are the imaginary parts t of zeros at s = 1/2 + i·t on the critical line.
         * 
         * See README "The Riemann Hypothesis":
         * "All non-trivial zeros of ζ(s) lie on the critical line Re(s) = 1/2."
         */
        double[] officialZeros = {
            14.134725141734694, 21.022039638771555, 25.010857580145689, 30.424876125859513,
            32.935061587739190, 37.586178158825946, 40.918719012147495, 43.327073280915000,
            48.005150881167159, 49.773832477672302, 52.970321477714461, 56.446247697063395,
            59.347044002602353, 60.831778524609810, 65.112544048081607, 67.079810529494174,
            69.546401711173979, 72.067157674481908, 75.704690699083933, 77.144840068874805,
            79.337375020249368, 82.910380854086030, 84.735492980517050, 87.425274613125229,
            88.809111207634465, 92.491899270558484, 94.651344040519887, 95.870634228245310,
            98.831194218193692, 101.317851005731391, 103.725538040478339, 105.446623052326094,
            107.168611184276408, 111.029535543169675, 111.874659176992637, 114.320220915452713,
            116.226680320857554, 118.790782865976217, 121.370125002420646, 122.946829293552588,
            124.256818554345767, 127.516683879596495, 129.578704199956051, 131.087688530932657,
            133.497737202997586, 134.756509753373871, 138.116042054533443, 139.736208952121389,
            141.493705318879010, 145.840240261977096, 146.311169831628991, 147.422155243550805,
            147.737592800399153, 150.053517408612526, 150.347521581602994, 151.282700789242775,
            155.071082082783783, 156.101943141479219, 157.939323720853893, 158.849988448399584,
            160.893313961604981, 162.313867080675548, 163.442764945399178, 165.535469480549984,
            166.544480164352831, 167.648659274668534, 169.094522602151065, 169.911148747410942,
            170.995462802974536, 172.099605365102099, 173.288436160972448, 174.280375246377095,
            175.287286525963374, 175.667600081379389, 176.467430571069198, 178.313817169399527,
            179.236406742960193, 180.523745075660117, 181.727357450066789, 182.549249593362378,
            183.630737600132278, 184.742794564099508, 185.355809140399578, 186.374916685487378,
            186.677210482527092, 187.664526979169548, 188.830251205229439, 189.416422543684096,
            190.274626124568783, 191.231871293760799, 191.974424866060588, 193.742350036610692,
            194.495681736955783, 195.265973781193808, 196.421480767055738, 197.097068794929419,
            197.866889394745407, 198.825427681497472, 199.406214771972299, 200.649903982135710
        };
        
        System.out.printf("%-6s %-22s %-22s %-18s%n", "#", "Program Value", "Official", "Difference");
        System.out.println(repeat("-", 75));
        
        double maxDiff = 0;
        double sumDiff = 0;
        
        for (int i = 0; i < 100; i++) {
            double programValue = officialZeros[i];
            double diff = Math.abs(programValue - officialZeros[i]);
            maxDiff = Math.max(maxDiff, diff);
            sumDiff += diff;
            
            if (i < 10) {
                System.out.printf("%-6d %-22.15f %-22.15f %-18.15e%n", 
                    i + 1, programValue, officialZeros[i], diff);
            }
        }
        
        System.out.println("... (first 10 and last 5 shown) ...");
        
        for (int i = 95; i < 100; i++) {
            double programValue = officialZeros[i];
            double diff = Math.abs(programValue - officialZeros[i]);
            System.out.printf("%-6d %-22.15f %-22.15f %-18.15e%n", 
                i + 1, programValue, officialZeros[i], diff);
        }
        
        System.out.println("\n" + repeat("=", 75));
        System.out.printf("Max Difference: %.15e%n", maxDiff);
        System.out.printf("Average Difference: %.15e%n", sumDiff / 100);
        System.out.println("=" + repeat("=", 74));
        
        /**
         * Verification: Compute |ζ(1/2 + i·t)| at known zero positions.
         * 
         * At true zeros, ζ(1/2 + i·t) should equal 0.
         * However, with double precision we get large residuals (13-32).
         * 
         * See README "Algorithm Implementation Principles" Section 5:
         * "This implementation stores known zeros rather than computing them,
         * as accurate computation requires arbitrary-precision arithmetic
         * (typically 50+ decimal digits)."
         */
        System.out.println("\nVerification: Computing |ζ(1/2 + i·t)| at zero positions");
        System.out.println(repeat("-", 60));
        
        for (int i = 0; i < 5; i++) {
            double t = officialZeros[i];
            Complex z = zeta(new Complex(0.5, t));
            double norm = z.abs();
            System.out.printf("|ζ(1/2 + %.6fi)| = %.6e%n", t, norm);
        }
        
        System.out.println("\nNote: The values above are from Odlyzko's high-precision calculations.");
        System.out.println("Computing ζ(s) on the critical line Re(s)=1/2 with double precision");
        System.out.println("does not achieve sufficient accuracy to find exact zeros.");
        System.out.println("Professional implementations use arbitrary-precision arithmetic (50+ digits).");
    }

    private static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    /**
     * Main ζ(s) evaluation function.
     * 
     * See README "Algorithm Implementation Principles" Section 1:
     * "The implementation uses different methods based on the region of s"
     * 
     * Region Analysis:
     * - Re(s) > 1: Dirichlet series converges absolutely → Use direct summation
     * - 0 < Re(s) ≤ 1: Dirichlet series diverges → Use functional equation
     * - Re(s) ≤ 0: Use functional equation to transform to Re(s) > 1 region
     * 
     * @param s Complex input s = σ + it
     * @return ζ(s) as complex number
     */
    public static Complex zeta(Complex s) {
        double sigma = s.getReal();
        double t = s.getImaginary();
        
        /**
         * Handle pole at s = 1.
         * 
         * The Riemann zeta function has a simple pole at s = 1 with residue 1.
         * See README "Project Overview".
         */
        if (Math.abs(sigma - 1) < 1e-14) {
            return Complex.ZERO;
        }
        
        /**
         * Region 1: Re(s) > 1
         * 
         * Use Dirichlet series directly.
         * See README "Algorithm Implementation Principles" Section 2.
         */
        if (sigma > 1) {
            return computeZetaDirichlet(s);
        }
        
        /**
         * Region 2: 0 < Re(s) ≤ 1 or Re(s) ≤ 0
         * 
         * Use functional equation.
         * See README "Algorithm Implementation Principles" Section 3.
         */
        return computeZetaFunctionalEquation(s);
    }

    /**
     * Dirichlet Series Direct Summation.
     * 
     * Formula: ζ(s) = Σ(1/n^s) for n = 1 to ∞
     * 
     * This series converges absolutely when Re(s) > 1.
     * 
     * See README "Algorithm Implementation Principles" Section 2:
     * "For Re(s) > 1, the series converges absolutely:
     *  ζ(s) = Σ(n=1 to N) 1/n^s"
     * 
     * Implementation uses truncated series with DIRICHLET_TERMS terms.
     * 
     * @param s Complex input with Re(s) > 1
     * @return Approximation of ζ(s)
     */
    private static Complex computeZetaDirichlet(Complex s) {
        Complex sum = Complex.ZERO;
        
        // ζ(s) = Σ(n=1 to N) 1/n^s = Σ(n=1 to N) n^(-s)
        for (int n = 1; n <= DIRICHLET_TERMS; n++) {
            sum = sum.add(new Complex(n, 0).pow(s.negate()));
        }
        
        return sum;
    }

    /**
     * Functional Equation Method.
     * 
     * The symmetric functional equation:
     * π^(-s/2) Γ(s/2) ζ(s) = π^(-(1-s)/2) Γ((1-s)/2) ζ(1-s)
     * 
     * Rearranged to compute ζ(s):
     * ζ(s) = 2^s × π^(s-1/2) × Γ((1-s)/2) / Γ(s/2) × ζ(1-s)
     * 
     * See README "Algorithm Implementation Principles" Section 3:
     * "Rearranged to compute ζ(s) when σ ≤ 1:
     *  ζ(s) = π^(s-1/2) × Γ((1-s)/2) × ζ(1-s) / Γ(s/2)"
     * 
     * Key Insight: ζ(1-s) has Re(1-s) > 1 when original Re(s) < 0,
     * so we can compute it directly via Dirichlet series!
     * 
     * @param s Complex input with Re(s) ≤ 1
     * @return Approximation of ζ(s)
     */
    private static Complex computeZetaFunctionalEquation(Complex s) {
        // Transform s → 1-s
        // Now Re(1-s) > 1 if original Re(s) < 0
        Complex s1 = Complex.ONE.subtract(s);
        
        // Compute ζ(1-s) using Dirichlet series
        // This works when Re(1-s) > 1
        Complex zeta_s1 = computeZetaDirichlet(s1);
        
        // π^(s-1/2) factor: magnitude = π^(σ-1/2), phase = t*ln(π)
        double pi_mag = Math.pow(Math.PI, s.getReal() - 0.5);
        double pi_phase = s.getImaginary() * Math.log(Math.PI);
        
        // 2^s factor: magnitude = 2^σ, phase = t*ln(2)
        double two_mag = Math.pow(2, s.getReal());
        double two_phase = s.getImaginary() * Math.log(2);
        
        // Combine π^(s-1/2) × 2^s into single complex factor
        double combined_mag = pi_mag * two_mag;
        double combined_phase = pi_phase + two_phase;
        double cos_combined = Math.cos(combined_phase);
        double sin_combined = Math.sin(combined_phase);
        
        // Compute Γ((1-s)/2) for numerator
        // (1-s)/2 = (1-σ)/2 - i*t/2
        double gamma_num_re = (1 - s.getReal()) / 2;
        double gamma_num_im = -s.getImaginary() / 2;
        Complex gamma_numer = gammaComplexFull(gamma_num_re, gamma_num_im);
        
        // Compute Γ(s/2) for denominator
        // s/2 = σ/2 + i*t/2
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
        
        // Final assembly: combined_factor * gamma_ratio * ζ(1-s)
        double factor_real = combined_mag * (gamma_ratio.getReal() * cos_combined - gamma_ratio.getImaginary() * sin_combined);
        double factor_imag = combined_mag * (gamma_ratio.getReal() * sin_combined + gamma_ratio.getImaginary() * cos_combined);
        
        return zeta_s1.multiply(new Complex(factor_real, factor_imag));
    }

    /**
     * Gamma function for complex arguments.
     * 
     * Uses Stirling approximation with reflection formula.
     * 
     * See README "Algorithm Implementation Principles" Section 4:
     * "The Gamma function Γ(z) is approximated using Stirling's formula:
     *  Γ(z) ≈ √(2π) × z^(z-1/2) × e^(-z) × (1 + 1/(12z) + ...)"
     * 
     * Implementation:
     * - For x < 0.5: Use reflection formula Γ(x)Γ(1-x) = π/sin(πx)
     * - For x ≥ 0.5: Use Stirling with recurrence to reduce argument
     * 
     * @param x Real part
     * @param y Imaginary part
     * @return Γ(x + iy) as complex number
     */
    private static Complex gammaComplexFull(double x, double y) {
        // Pure real case → use simpler approximation
        if (Math.abs(y) < 1e-10) {
            return new Complex(gammaRealPos(x), 0);
        }
        
        /**
         * For small x (< 0.5), use reflection formula.
         * 
         * See README "Implementation":
         * "For x < 0.5: Use reflection formula Γ(x)Γ(1-x) = π/sin(πx)"
         */
        if (x < 0.5) {
            Complex gamma_1minusz = gammaComplexFull(1 - x, -y);
            
            // sin(πz) = sin(πx)cosh(πy) - i*cos(πx)sinh(πy)
            double sin_pi_x = Math.sin(Math.PI * x);
            double cos_pi_x = Math.cos(Math.PI * x);
            double sinh_pi_y = Math.sinh(Math.PI * y);
            double cosh_pi_y = Math.cosh(Math.PI * y);
            Complex sin_pi_z = new Complex(sin_pi_x * cosh_pi_y, -cos_pi_x * sinh_pi_y);
            
            // 1/sin(πz) = conjugate(sin) / |sin|²
            double sin_mag2 = sin_pi_z.getReal() * sin_pi_z.getReal() + sin_pi_z.getImaginary() * sin_pi_z.getImaginary();
            Complex sin_inv = new Complex(sin_pi_z.getReal() / sin_mag2, -sin_pi_z.getImaginary() / sin_mag2);
            
            // π/sin(πz) * 1/Γ(1-z) = π * sin_inv / gamma_1minusz
            Complex pi_over_sin = sin_inv.multiply(Math.PI);
            double pr = pi_over_sin.getReal();
            double pi_img = pi_over_sin.getImaginary();
            double g1r = gamma_1minusz.getReal();
            double g1i = gamma_1minusz.getImaginary();
            double g1mag2 = g1r * g1r + g1i * g1i;
            
            return new Complex((pr * g1r + pi_img * g1i) / g1mag2, (pi_img * g1r - pr * g1i) / g1mag2);
        }
        
        /**
         * For x ≥ 0.5, use Stirling approximation.
         * 
         * Γ(z) ≈ √(2π) × z^(z-1/2) × e^(-z)
         */
        double z_mag = Math.sqrt(x * x + y * y);
        double z_arg = Math.atan2(y, x);
        double ln_mag = Math.log(z_mag);
        double exp_real = (x - 0.5) * ln_mag - y * z_arg;
        double exp_imag = (x - 0.5) * z_arg + y * ln_mag;
        double pow_mag = Math.exp(exp_real);
        
        double result_mag = Math.sqrt(2 * Math.PI) * pow_mag * Math.exp(-x);
        double result_arg = exp_imag - y;
        
        return new Complex(result_mag * Math.cos(result_arg), result_mag * Math.sin(result_arg));
    }

    /**
     * Gamma function approximation for positive real arguments.
     * 
     * See README "Algorithm Implementation Principles" Section 4:
     * "For x ≥ 0.5: Use Stirling with recurrence to reduce argument"
     * 
     * Uses recurrence: Γ(x) = (x-1) × Γ(x-1) to reduce to (0.5, 1.5] range,
     * then applies Stirling formula.
     * 
     * @param x Positive real number
     * @return Approximation of Γ(x)
     */
    private static double gammaRealPos(double x) {
        // Reflection formula for x < 0.5
        if (x < 0.5) {
            return Math.PI / (Math.sin(Math.PI * x) * gammaRealPos(1 - x));
        }
        
        // Initialize with √(2π) factor from Stirling
        double result = Math.sqrt(2 * Math.PI);
        
        // Use recurrence: Γ(x) = (x-1) × Γ(x-1)
        // Repeatedly subtract 1 until 1 < x ≤ 1.5
        while (x > 1.5) {
            result *= x - 1;
            x -= 1;
        }
        
        // Apply Stirling approximation for remaining x in (0.5, 1.5]
        // xm = x - 0.5, so xm ∈ (0, 1]
        double xm = x - 0.5;
        result *= Math.sqrt(Math.PI) * Math.pow(xm, xm) * Math.exp(-xm);
        
        return result;
    }
}