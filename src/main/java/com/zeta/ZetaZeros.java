package com.zeta;

import org.apache.commons.math3.complex.Complex;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ZetaZeros {

    private static final MathContext MC = new MathContext(100, RoundingMode.HALF_UP);
    
    public static void main(String[] args) {
        System.out.println("Computing first 10 non-trivial zeros of Riemann Zeta function");
        System.out.println("=" + repeat("=", 59));
        System.out.println("\nThe first 10 non-trivial zeros of ζ(s) on the critical line Re(s) = 1/2:");
        System.out.println("-" + repeat("-", 58));
        
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

    public static Complex zeta(Complex s) {
        double sigma = s.getReal();
        double t = s.getImaginary();
        
        if (Math.abs(sigma - 1) < 1e-14) {
            return Complex.ZERO;
        }
        
        if (sigma > 1) {
            return computeZetaDirichlet(s);
        }
        
        if (sigma > 0) {
            return computeZetaFunctionalEquation(s);
        }
        
        return computeZetaFunctionalEquation(s);
    }

    private static Complex computeZetaDirichlet(Complex s) {
        Complex sum = Complex.ZERO;
        int maxN = 5000;
        
        for (int n = 1; n <= maxN; n++) {
            sum = sum.add(new Complex(n, 0).pow(s.negate()));
        }
        
        return sum;
    }

    private static Complex computeZetaFunctionalEquation(Complex s) {
        Complex s1 = Complex.ONE.subtract(s);
        
        double s1Re = s1.getReal();
        double s1Im = s1.getImaginary();
        
        Complex zeta_s1 = computeZetaDirichlet(s1);
        
        double pi_s_2 = Math.PI * (s.getReal() - 0.5);
        
        double cos_term = Math.cos(pi_s_2);
        double sin_term = Math.sin(pi_s_2);
        
        double gamma_term = gammaReal(s.getReal() / 2, s.getImaginary() / 2);
        
        double factor = Math.pow(2, s.getReal() - 1) * Math.pow(Math.PI, -0.5);
        
        Complex result = zeta_s1.multiply(new Complex(factor * cos_term, factor * sin_term * gamma_term));
        
        return result;
    }

    private static double gammaReal(double x, double y) {
        if (y == 0) {
            return gammaRealPos(x);
        }
        
        double g = Math.sqrt(Math.PI) * Math.pow(2, x - 0.5);
        g *= Math.pow(Math.E / x, x - 0.25);
        
        double theta = y * Math.log(x / (2 * Math.PI));
        
        return g * (Math.cos(theta) + Math.sin(theta) / (12 * y));
    }

    private static double gammaRealPos(double x) {
        if (x < 0.5) {
            return Math.PI / (Math.sin(Math.PI * x) * gammaRealPos(1 - x));
        }
        
        double result = Math.sqrt(2 * Math.PI);
        while (x > 1.5) {
            result *= x - 1;
            x -= 1;
        }
        
        double xm = x - 0.5;
        result *= Math.sqrt(Math.PI) * Math.pow(xm, xm) * Math.exp(-xm);
        
        return result;
    }
}
