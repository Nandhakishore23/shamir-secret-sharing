import java.math.BigInteger;
import java.util.*;

public class ShamirSecret {

    // Function to perform Lagrange interpolation at x=0 (to find secret)
    private static BigInteger lagrangeInterpolation(List<int[]> points, int k) {
        BigInteger secret = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            int xi = points.get(i)[0];
            BigInteger yi = new BigInteger(points.get(i)[1] + "");

            BigInteger num = BigInteger.ONE;  // numerator
            BigInteger den = BigInteger.ONE;  // denominator

            for (int j = 0; j < k; j++) {
                if (i == j) continue;

                int xj = points.get(j)[0];
                num = num.multiply(BigInteger.valueOf(-xj));
                den = den.multiply(BigInteger.valueOf(xi - xj));
            }

            // term = yi * (num/den)
            BigInteger term = yi.multiply(num).divide(den);
            secret = secret.add(term);
        }

        return secret;
    }

    public static void main(String[] args) {
        // Hardcoded Example 2 JSON
        Map<Integer, String[]> shares = new HashMap<>();
        shares.put(1, new String[]{"6", "13444211440455345511"});
        shares.put(2, new String[]{"15", "aed7015a346d635"});
        shares.put(3, new String[]{"15", "6aeeb69631c227c"});
        shares.put(4, new String[]{"16", "e1b5e05623d881f"});
        shares.put(5, new String[]{"8", "316034514573652620673"});
        shares.put(6, new String[]{"3", "2122212201122002221120200210011020220200"});
        shares.put(7, new String[]{"3", "20120221122211000100210021102001201112121"});
        shares.put(8, new String[]{"6", "20220554335330240002224253"});
        shares.put(9, new String[]{"12", "45153788322a1255483"});
        shares.put(10, new String[]{"7", "1101613130313526312514143"});

        int n = 10;
        int k = 7;

        // Step 1: Collect first k shares (can choose any k)
        List<int[]> points = new ArrayList<>();

        int count = 0;
        for (int key : shares.keySet()) {
            if (count >= k) break;

            String[] share = shares.get(key);
            int base = Integer.parseInt(share[0]);
            BigInteger value = new BigInteger(share[1], base);

            points.add(new int[]{key, value.intValue()});
            count++;
        }

        // Step 2: Apply Lagrange Interpolation
        BigInteger secret = lagrangeInterpolation(points, k);

        System.out.println("Reconstructed Secret: " + secret);
    }
}
