

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;

public class Utilities {

	/**
	 * Hashing with SHA256
	 *
	 * @param input
	 *            String to hash
	 * @return String hashed
	 */
	public static String sha256(String input) {
		String sha256 = null;
		try {
			MessageDigest msdDigest = MessageDigest.getInstance("SHA-256");
			msdDigest.update(input.getBytes("UTF-8"), 0, input.length());
			sha256 = DatatypeConverter.printHexBinary(msdDigest.digest());
		} catch (Exception e) {
			Logger.getLogger(sha256).log(Level.SEVERE, null, e);
		}
		return sha256;
	}
	
	/**
	 * Hashing with SHA256
	 *
	 * @param input
	 *            String to hash
	 * @return String hashed
	 */
	public static byte[] sha2561(String input) {
		try {
			MessageDigest msdDigest = MessageDigest.getInstance("SHA-256");
			msdDigest.update(input.getBytes("UTF-8"), 0, input.length());
			return msdDigest.digest();
		} catch (Exception e) {
			
		}
		return null;
	}
	

	private static BigInteger LIMIT = BigInteger.valueOf(PublicParams.LIMIT);
	private static BigInteger LEAPS = BigInteger.valueOf(PublicParams.LEAPES);
	private static BigInteger LEAPS_DIVIDE = BigInteger.valueOf(4);

	// kangaroo in the group G1
	public static int longKangaroo(Element generator, Element num) {
		/*
		 * Pollard's lambda algorithm for finding discrete logs * which are
		 * known to be less than a certain limit LIMIT
		 */
		int kanResult = -1;
		Element trap, x;
		Element Num = num;
		Element[] table = new Element[32];
		int i, j, m;
		BigInteger dm, dn, s;
		BigInteger[] distance = new BigInteger[32];

		for (s = BigInteger.ONE, m = 1;; m++) {
			distance[m - 1] = s;
			s = s.add(s);
			BigInteger aLEAPS = LEAPS.divide(LEAPS_DIVIDE);
			if ((s.add(s)).divide(BigInteger.valueOf(m)).compareTo(aLEAPS) > 0)
				break;
		}

		for (i = 0; i < m; i++) {
			/* create table */
			table[i] = generator.pow(distance[i]);
			// System.out.println("trap failed... : " + table[i]);
		}

		x = generator.pow(LIMIT);
		// System.out.println("setting trap..." + m);
		for (dn = BigInteger.ZERO, j = 0; j < LEAPS.intValue(); j++) {
			/* set traps beyond LIMIT using tame kangaroo */
			try {
				i = (x.toBigInteger().intValue()) % m; /* random function */
			} catch (Exception e) {
				i = 0;
			}
			x = x.mul(table[i]);
			dn = dn.add(distance[i]);
		}

		trap = x;
		// Random randomWild = new Random(System.currentTimeMillis());
		for (dm = BigInteger.ZERO;;) {
			try {
				i = (x.toBigInteger().intValue()) % m;
				;
			} catch (Exception e) {
				i = 0;
			}
			Num = Num.mul(table[i]);
			dm = dm.add(distance[i]);

			if (Num.equals(trap))
				break;
			if (dm.compareTo(LIMIT.add(dn)) > 0)
				break;
		}
		if (dm.compareTo(LIMIT.add(dn)) > 0) { /* trap stepped over */
			// System.out.println("trap failed... : " + dm);
			return kanResult;
		}

		// System.out.println("get the kangaroo...");
		// System.out.println("MOD = " + dm);
		// System.out.println("Discrete log of y = " +
		// (LIMIT.add(dn).subtract(dm)));
		kanResult = (LIMIT.add(dn).subtract(dm)).intValue();
		return kanResult;

	}

	// kangaroo in the group G1
	public static int longKangarooRandom(Element generator, Element num, int limit) {
		/*
		 * Pollard's lambda algorithm for finding discrete logs * which are
		 * known to be less than a certain limit LIMIT
		 */
		int kanResult = -1;
		Element trap, x;
		Element Num = num;
		Element[] table = new Element[32];
		int i, j, m;
		BigInteger dm, dn, s;
		BigInteger[] distance = new BigInteger[32];
		BigInteger thisLimit = BigInteger.valueOf(limit);
		BigInteger leaps = BigInteger.valueOf((long) Math.ceil(Math.sqrt(limit)));

		for (s = BigInteger.ONE, m = 1;; m++) {
			distance[m - 1] = s;
			s = s.add(s);
			BigInteger aLEAPS = leaps.divide(LEAPS_DIVIDE);
			if ((s.add(s)).divide(BigInteger.valueOf(m)).compareTo(aLEAPS) > 0)
				break;
		}

		for (i = 0; i < m; i++) {
			/* create table */
			table[i] = generator.pow(distance[i]);
			// System.out.println("trap failed... : " + table[i]);
		}

		x = generator.pow(thisLimit);
		// System.out.println("setting trap..." + m);
		for (dn = BigInteger.ZERO, j = 0; j < leaps.intValue(); j++) {
			/* set traps beyond LIMIT using tame kangaroo */
			try {
				i = (x.toBigInteger().intValue()) % m; /* random function */
			} catch (Exception e) {
				i = 0;
			}
			x = x.mul(table[i]);
			dn = dn.add(distance[i]);
		}

		trap = x;
		// Random randomWild = new Random(System.currentTimeMillis());
		for (dm = BigInteger.ZERO;;) {
			try {
				i = (x.toBigInteger().intValue()) % m;
				;
			} catch (Exception e) {
				i = 0;
			}
			Num = Num.mul(table[i]);
			dm = dm.add(distance[i]);

			if (Num.equals(trap))
				break;
			if (dm.compareTo(thisLimit.add(dn)) > 0)
				break;
		}
		if (dm.compareTo(thisLimit.add(dn)) > 0) { /* trap stepped over */
			// System.out.println("trap failed... : " + dm);
			return kanResult;
		}

		// System.out.println("get the kangaroo...");
		// System.out.println("MOD = " + dm);
		// System.out.println("Discrete log of y = " +
		// (LIMIT.add(dn).subtract(dm)));
		kanResult = (thisLimit.add(dn).subtract(dm)).intValue();
		return kanResult;

	}

	// kangaroo in the group G1
	public static long kangaroo(Element generator, Element num) {
		/*
		 * Pollard's lambda algorithm for finding discrete logs * which are
		 * known to be less than a certain limit LIMIT
		 */
		int kanResult = -1;
		Element trap, x;
		Element Num = num;
		Element[] table = new Element[32];
		int i, j, m;
		BigInteger dm, dn, s;
		BigInteger[] distance = new BigInteger[32];

		for (s = BigInteger.ONE, m = 1;; m++) {
			distance[m - 1] = s;
			s = s.add(s);
			BigInteger aLEAPS = LEAPS.divide(LEAPS_DIVIDE);
			if ((s.add(s)).divide(BigInteger.valueOf(m)).compareTo(aLEAPS) > 0)
				break;
		}

		for (i = 0; i < m; i++) {
			/* create table */
			table[i] = generator.pow(distance[i]);
			// System.out.println("trap failed... : " + table[i]);
		}

		x = generator.pow(LIMIT);
		// System.out.println("setting trap..." + m);
		for (dn = BigInteger.ZERO, j = 0; j < LEAPS.intValue(); j++) {
			/* set traps beyond LIMIT using tame kangaroo */
			try {
				i = (x.toBigInteger().intValue()) % m; /* random function */
			} catch (Exception e) {
				i = 0;
			}
			x = x.mul(table[i]);
			dn = dn.add(distance[i]);
		}

		long sl = System.currentTimeMillis();
		trap = x;
		// Random randomWild = new Random(System.currentTimeMillis());
		for (dm = BigInteger.ZERO;;) {
			try {
				i = (x.toBigInteger().intValue()) % m;
				;
			} catch (Exception e) {
				i = 0;
			}
			Num = Num.mul(table[i]);
			dm = dm.add(distance[i]);

			if (Num.equals(trap))
				break;
			if (dm.compareTo(LIMIT.add(dn)) > 0)
				break;
		}
		if (dm.compareTo(LIMIT.add(dn)) > 0) { /* trap stepped over */
			// System.out.println("trap failed... : " + dm);
			long el = System.currentTimeMillis();
			return el - sl;
		}

		// System.out.println("get the kangaroo...");
		// System.out.println("MOD = " + dm);
		// System.out.println("Discrete log of y = " +
		// (LIMIT.add(dn).subtract(dm)));
		kanResult = (LIMIT.add(dn).subtract(dm)).intValue();
		long el = System.currentTimeMillis();
		return el - sl;

	}

	// kangaroo in the group G1
	public static int linearSearch(Element generator, Element num, int limit) {
		int kanResult = -1;

		BigInteger LIMIT = BigInteger.valueOf(limit);
		Element Num = generator;
		BigInteger s = BigInteger.ONE;

		for (s = BigInteger.ONE; s.compareTo(LIMIT) <= 0; s = s.add(BigInteger.ONE)) {
			if (Num.equals(num))
				return s.intValue();
			Num = Num.mul(generator);
		}
		return kanResult;
	}

	// kangaroo in the group G1
	public static int linearSearch(Element generator, Element num) {
		int kanResult = -1;

		Element Num = generator;
		BigInteger s = BigInteger.ONE;

		for (s = BigInteger.ONE; s.compareTo(LIMIT) <= 0; s = s.add(BigInteger.ONE)) {
			if (Num.equals(num))
				return s.intValue();
			Num = Num.mul(generator);
		}
		return kanResult;
	}

	public static int linearSearchRandom(Element generator, Element num, int limit2) {
		// TODO Auto-generated method stub
		int kanResult = -1;

		Element Num = generator;
		BigInteger s = BigInteger.ONE;
		BigInteger LIMIT = BigInteger.valueOf(limit2);

		for (s = BigInteger.ONE; s.compareTo(LIMIT) <= 0; s = s.add(BigInteger.ONE)) {
			if (Num.equals(num))
				return s.intValue();
			Num = Num.mul(generator);
		}
		return kanResult;
	}
	
	
	/**
	 * generate a random long identity
	 *
	 * @param input
	 *            null
	 * @return long
	 */
	public static BigInteger randomBig(BigInteger mod) {
		Random rnd = new Random();
		long seed = System.nanoTime();
		rnd.setSeed(seed);
		BigInteger ranBig = new BigInteger(1024, rnd);
		ranBig = ranBig.mod(mod);
		return ranBig;
	}
	
	/**
	 * generate a random long identity
	 *
	 * @param input
	 *            null
	 * @return long
	 */
	public static long randomlong() {
		Random rnd = new Random();
		long seed = System.currentTimeMillis();
		rnd.setSeed(seed);
		BigInteger result = BigInteger.probablePrime(1000, rnd);
		if(result.compareTo(BigInteger.ZERO)>0){
			result = result.negate();
		}
		return result.longValue();
	}
	
	/**
	 * generate a random long identity
	 *
	 * @param input
	 *            null
	 * @return long
	 */
	public static long randomlong2() {
		Random rnd = new Random();
		return rnd.nextLong();
	}
	

}
