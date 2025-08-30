import java.math.BigInteger;
import java.util.*;

public class ShamirSecret {

    public static void main(String[] args) {
        // ---------------- TEST CASE 1 ----------------
        Map<String, Object> testCase1 = new LinkedHashMap<>();
        testCase1.put("n", 4);
        testCase1.put("k", 3);
        testCase1.put("shares", Map.of(
                "1", new String[]{"10", "4"},
                "2", new String[]{"2", "111"},
                "3", new String[]{"10", "12"},
                "6", new String[]{"4", "213"}
        ));

        // ---------------- TEST CASE 2 ----------------
        Map<String, Object> testCase2 = new LinkedHashMap<>();
        testCase2.put("n", 10);
        testCase2.put("k", 7);
        testCase2.put("shares", Map.of(
                "1", new String[]{"6", "13444211440455345511"},
                "2", new String[]{"15", "aed7015a346d635"},
                "3", new String[]{"15", "6aeeb69631c227c"},
                "4", new String[]{"16", "e1b5e05623d881f"},
                "5", new String[]{"8", "316034514573652620673"},
                "6", new String[]{"3", "2122212201122002221120200210011020220200"},
                "7", new String[]{"3", "20120221122211000100210021102001201112121"},
                "8", new String[]{"6", "20220554335330240002224253"},
                "9", new String[]{"12", "45153788322a1255483"},
                "10", new String[]{"7", "1101613130313526312514143"}
        ));

        // Run both
        runTestCase("Test Case 1", testCase1);
        runTestCase("Test Case 2", testCase2);
    }

    private static void runTestCase(String name, Map<String, Object> testCase) {
        int n = (int) testCase.get("n");
        int k = (int) testCase.get("k");
        Map<String, String[]> shares = (Map<String, String[]>) testCase.get("shares");

        System.out.println("\n---------------- " + name + " ----------------");
        System.out.println("n = " + n + ", k = " + k);

        // Step 1: Convert shares into decimal (x, y)
        Map<Integer, BigInteger> points = new LinkedHashMap<>();
        for (String key : shares.keySet()) {
            int x = Integer.parseInt(key);
            String base = shares.get(key)[0];
            String value = shares.get(key)[1];
            BigInteger y = new BigInteger(value, Integer.parseInt(base));
            points.put(x, y);
        }

        // Step 2: Pick first k shares
        List<Integer> xVals = new ArrayList<>();
        List<BigInteger> yVals = new ArrayList<>();
        int count = 0;
        for (Map.Entry<Integer, BigInteger> entry : points.entrySet()) {
            if (count == k) break;
            xVals.add(entry.getKey());
            yVals.add(entry.getValue());
            count++;
        }

        // Step 3: Lagrange Interpolation at x=0
        BigInteger secret = lagrangeInterpolation(xVals, yVals, BigInteger.ZERO);

        // ----------- OUTPUT ------------
        System.out.println("Shares provided: ");
        for (Map.Entry<Integer, BigInteger> entry : points.entrySet()) {
            System.out.println("  x=" + entry.getKey() + ", y=" + entry.getValue());
        }
        System.out.println("✅ Reconstructed Secret: " + secret);
    }

    // Lagrange Interpolation (BigInteger)
    private static BigInteger lagrangeInterpolation(List<Integer> x, List<BigInteger> y, BigInteger atX) {
        BigInteger result = BigInteger.ZERO;
        int k = x.size();

        for (int i = 0; i < k; i++) {
            BigInteger xi = BigInteger.valueOf(x.get(i));
            BigInteger yi = y.get(i);

            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i == j) continue;
                BigInteger xj = BigInteger.valueOf(x.get(j));
                num = num.multiply(atX.subtract(xj));
                den = den.multiply(xi.subtract(xj));
            }

            BigInteger term = yi.multiply(num).divide(den);
            result = result.add(term);
        }
        return result;
    }
}
