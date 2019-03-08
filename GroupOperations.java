import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.util.io.Base64;

public class GroupOperations {

	private static int count = PublicParams.COUNT;
	private static long sl;
	private static long el;

	static BigInteger bigMod;
//	static Out out = new Out("a256_properties_test1.times");
	static Out out = new Out("aggVote20190307.times");
	
	public static void GroupOperations() throws IOException, NoSuchAlgorithmException {

		Pairing pairing = PairingFactory.getPairing("a256.properties");
		bigMod = pairing.getG1().getOrder();

		GPairingTime(pairing);
		GTexpTime(pairing);
		GTMulTime(pairing);
		G1MulTime(pairing);
		G1MulSmallTime(pairing);
		G1AddTime(pairing);

		H2G1Time(pairing);
		Hash2BigIntegerTime(pairing);

		out.close();
	}

	private static void printAndWriteData(String outStr, double total) {
		System.out.println(outStr + " £º£º£º " + total / count);
		out.println(outStr + " £º£º£º " + total / count);
	}

	private static void Hash2BigIntegerTime(Pairing pairing)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		double total = 0;

		for (int i = 0; i < count; i++) {
			sl = System.nanoTime();
			BigInteger bi = new BigInteger(
					PublicParams.STRING_FOR_HASH_2_BIGINTEGER_TEST.getBytes(StandardCharsets.UTF_8));
			bi = bi.mod(bigMod);
			el = System.nanoTime();
			total = total + (el - sl);
		}

		total = total / 1000000;
		printAndWriteData("Hash2BigInteger", total);
	}

	private static void G1AddTime(Pairing pairing) {
		double total = 0;

		for (int i = 0; i < count; i++) {
			Element G_1 = pairing.getG1().newRandomElement().getImmutable();
			Element G_1_p = pairing.getG1().newRandomElement().getImmutable();

			sl = System.nanoTime();
			G_1.add(G_1_p);
			el = System.nanoTime();
			total = total + (el - sl);
		}
		total = total / 1000000;
		printAndWriteData("G1 add", total);
	}

	private static void H2G1Time(Pairing pairing) throws IOException {
		double total = 0;
		
		for (int i = 0; i < count; i++) {
			String originalString = PublicParams.STRING_FOR_HASH_2_G1_TEST;
			
			sl = System.nanoTime();
			byte[] oiginalBytes = originalString.getBytes(StandardCharsets.UTF_8);
			Element rin = pairing.getG1().newElementFromHash(oiginalBytes, 0, oiginalBytes.length);
			el = System.nanoTime();
			total = total + (el - sl);
		}
		total = total / 1000000;
		printAndWriteData("H2G1", total);
	}

	// private static long runningTimeOfOneH2G1Operation(Pairing pairing) throws
	// IOException {
	// String originalString = PublicParams.STRING_FOR_HASH_2_G1_TEST;
	// sl = System.nanoTime();
	// // Element rin = Utilities.hash2Element(Utilities.randomlong(),
	// // pairing);
	// // Element rin =
	// //
	// pairing.getG1().newElementFromBytes(Base64.decode(PublicParams.STRING_FOR_HASH_2_G1_TEST));
	// // byte[] oiginalBytes =
	// // originalString.getBytes(StandardCharsets.UTF_8);
	// // pairing.getG1().newElement().setFromHash(oiginalBytes, 0,
	// // oiginalBytes.length);
	// byte[] oiginalBytes = originalString.getBytes(StandardCharsets.UTF_8);
	// // Element rin =
	// // pairing.getG1().newElement().setFromHash(oiginalBytes,0,
	// // oiginalBytes.length);
	// Element rin = pairing.getG1().newElementFromHash(oiginalBytes, 0,
	// oiginalBytes.length);
	// // System.out.println(rin);
	// el = System.nanoTime();
	// return (el - sl);
	// }

	private static void GTMulTime(Pairing pairing) {
		double total = 0;

		for (int i = 0; i < count; i++) {
			Element GT_1 = pairing.getGT().newRandomElement().getImmutable();
			Element GT_2 = pairing.getGT().newRandomElement().getImmutable();

			sl = System.nanoTime();
			GT_1.mul(GT_2);
			el = System.nanoTime();
			total = total + (el - sl);
		}
		total = total / 1000000;
		printAndWriteData("GT mul", total);
	}

	private static void GTexpTime(Pairing pairing) {
		double total = 0;

		for (int i = 0; i < count; i++) {
			Element G_1 = pairing.getGT().newRandomElement().getImmutable();
			BigInteger x = Utilities.randomBig(bigMod);
			sl = System.nanoTime();
			G_1.pow(x);
			el = System.nanoTime();
			total = total + (el - sl);
		}
		total = total / 1000000;
		printAndWriteData("GT exp ", total);
	}

	private static void GPairingTime(Pairing pairing) {
		double total = 0;

		for (int i = 0; i < count; i++) {
			Element G_1 = pairing.getG1().newRandomElement().getImmutable();
			Element G_2 = pairing.getG1().newRandomElement().getImmutable();

			sl = System.nanoTime();
			pairing.pairing(G_1, G_2);
			el = System.nanoTime();
			total = total + (el - sl);
		}
		total = total / 1000000;
		printAndWriteData("Pairing", total);
	}

	private static void G1MulTime(Pairing pairing) {
		double total = 0;

		for (int i = 0; i < count; i++) {
			Element G_1 = pairing.getG1().newRandomElement().getImmutable();
			BigInteger x = Utilities.randomBig(bigMod);

			sl = System.nanoTime();
			G_1.mul(x);
			el = System.nanoTime();
			total = total + (el - sl);
		}
		total = total / 1000000;
		printAndWriteData("G1 mul", total);
	}

	private static void G1MulSmallTime(Pairing pairing) {
		double total = 0;

		for (int i = 0; i < count; i++) {
			Element G_1 = pairing.getG1().newRandomElement().getImmutable();
			BigInteger x = Utilities.randomBig(PublicParams.smallMod);

			sl = System.nanoTime();
			G_1.mul(x);
			el = System.nanoTime();
			total = total + (el - sl);
		}
		total = total / 1000000;
		printAndWriteData("G1 mul small", total);
	}

}
