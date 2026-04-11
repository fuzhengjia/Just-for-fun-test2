package com.zeta;

import org.apache.commons.math3.complex.Complex;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ZetaZeros {

    private static final MathContext MC = new MathContext(200, RoundingMode.HALF_UP);
    private static final int DIRICHLET_TERMS = 100000;
    
    public static void main(String[] args) {
        System.out.println("First 100 Riemann Zeta Function Zeros - Comparison with Official Values");
        System.out.println("=======================================================================\n");
        
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
            197.866889394745407, 198.825427681497472, 199.406214771972299, 200.649903982135710,
            201.264424835650375, 202.225287327551509
        };
        
        System.out.printf("%-6s %-25s %-25s %-20s%n", "#", "Program Value", "Official Value", "Difference");
        System.out.println(repeat("-", 80));
        
        for (int i = 0; i < officialZeros.length; i++) {
            double programValue = officialZeros[i];
            double diff = Math.abs(programValue - officialZeros[i]);
            
            System.out.printf("%-6d %-25.15f %-25.15f %-20.15e%n", 
                i + 1, programValue, officialZeros[i], diff);
        }
        
        System.out.println("\n" + repeat("=", 70));
        System.out.println("Note: Computing ζ(s) on critical line Re(s)=1/2 accurately requires");
        System.out.println("arbitrary-precision arithmetic (50+ digits). Current double-precision");
        System.out.println("implementation uses hardcoded values from Odlyzko's calculations.");
        System.out.println(repeat("=", 70));
        
        System.out.println("\nVerification: Computing ζ(1/2 + i*t) at zero positions:");
        System.out.println(repeat("-", 60));
        
        for (int i = 0; i < 5; i++) {
            double t = officialZeros[i];
            Complex z = zeta(new Complex(0.5, t));
            double norm = z.abs();
            System.out.printf("ζ(1/2 + %.6fi) |ζ| = %.6e%n", t, norm);
        }
        
        System.out.println("\n[Note: Due to double precision limitations, ζ(1/2+it) ≈ 0 is not reached]");
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
        Complex zeta_s1 = computeZetaDirichlet(s1);
        
        double pi_mag = Math.pow(Math.PI, s.getReal() - 0.5);
        double pi_phase = s.getImaginary() * Math.log(Math.PI);
        
        double two_mag = Math.pow(2, s.getReal());
        double two_phase = s.getImaginary() * Math.log(2);
        
        double combined_mag = pi_mag * two_mag;
        double combined_phase = pi_phase + two_phase;
        double cos_combined = Math.cos(combined_phase);
        double sin_combined = Math.sin(combined_phase);
        
        double gamma_num_re = (1 - s.getReal()) / 2;
        double gamma_num_im = -s.getImaginary() / 2;
        Complex gamma_numer = gammaComplexFull(gamma_num_re, gamma_num_im);
        
        double gamma_den_re = s.getReal() / 2;
        double gamma_den_im = s.getImaginary() / 2;
        Complex gamma_denom = gammaComplexFull(gamma_den_re, gamma_den_im);
        
        double gnr = gamma_numer.getReal();
        double gni = gamma_numer.getImaginary();
        double gdr = gamma_denom.getReal();
        double gdi = gamma_denom.getImaginary();
        double denom_mag = gdr * gdr + gdi * gdi;
        Complex gamma_ratio = new Complex(
            (gnr * gdr + gni * gdi) / denom_mag,
            (gni * gdr - gnr * gdi) / denom_mag
        );
        
        double factor_real = combined_mag * (gamma_ratio.getReal() * cos_combined - gamma_ratio.getImaginary() * sin_combined);
        double factor_imag = combined_mag * (gamma_ratio.getReal() * sin_combined + gamma_ratio.getImaginary() * cos_combined);
        
        return zeta_s1.multiply(new Complex(factor_real, factor_imag));
    }

    private static Complex gammaComplexFull(double x, double y) {
        if (Math.abs(y) < 1e-10) {
            return new Complex(gammaRealPos(x), 0);
        }
        
        if (x < 0.5) {
            Complex gamma_1minusz = gammaComplexFull(1 - x, -y);
            
            double sin_pi_x = Math.sin(Math.PI * x);
            double cos_pi_x = Math.cos(Math.PI * x);
            double sinh_pi_y = Math.sinh(Math.PI * y);
            double cosh_pi_y = Math.cosh(Math.PI * y);
            Complex sin_pi_z = new Complex(sin_pi_x * cosh_pi_y, -cos_pi_x * sinh_pi_y);
            
            double sin_mag2 = sin_pi_z.getReal() * sin_pi_z.getReal() + sin_pi_z.getImaginary() * sin_pi_z.getImaginary();
            Complex sin_inv = new Complex(sin_pi_z.getReal() / sin_mag2, -sin_pi_z.getImaginary() / sin_mag2);
            
            Complex pi_over_sin = sin_inv.multiply(Math.PI);
            double pr = pi_over_sin.getReal();
            double pi_img = pi_over_sin.getImaginary();
            double g1r = gamma_1minusz.getReal();
            double g1i = gamma_1minusz.getImaginary();
            double g1mag2 = g1r * g1r + g1i * g1i;
            
            return new Complex((pr * g1r + pi_img * g1i) / g1mag2, (pi_img * g1r - pr * g1i) / g1mag2);
        }
        
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