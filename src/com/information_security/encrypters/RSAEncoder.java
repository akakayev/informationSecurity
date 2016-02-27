package com.information_security.encrypters;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class RSAEncoder implements Encrypt {

	private int d;
	private int p;
	private int q;
	private int n;
	private int e;

	public RSAEncoder(int p, int q) {
		n = p * q;
		int f = (p - 1) * (q - 1);
		e = generateE(f);
		d = resolveD(f, e);
	}

	@Override
	public String encrypt(String inputMessage) {
		StringBuilder result = new StringBuilder("");
		for (char c : inputMessage.toCharArray()) {
			result.append((char)new BigInteger((int) c + "").pow(e).mod(new BigInteger(String.valueOf(n))).intValue());
		}
		return result.toString();
	}

	@Override
	public String decrypt(String inputMessage) {
		StringBuilder result = new StringBuilder("");
		for (char c : inputMessage.toCharArray()) {
			result.append((char)new BigInteger((int) c + "").pow(d).mod(new BigInteger(String.valueOf(n))).intValue());
		}
		return result.toString();
	}

	private int generateE(long f) {
		int e = 0;
		List<Integer> list = new LinkedList<Integer>();
		for (int i = 2; i < f; i++) {
			e = i;
			if (areSimple(e, f)) {
				list.add(e);
				continue;
			}
		}
		return list.get((int) (Math.random() * list.size()));
	}

	private  int resolveD(int f, int e) {
		int d = 0;
		for (int k = 1; k < f; k++) {
			if (((1 + k * f) % e) == 0) {
				d = (1 + k * f) / e;
				break;
			}
		}
		return d;
	}

	private boolean areSimple(long a, long b) {
		for (int i = 2; i <= Math.max(a, b); i++) {
			if ((a % i == 0) & (b % i == 0))
				return false;
		}
		return true;
	}

	public static boolean isSimple(int x) {
		for (int i = 2; i < x; i++) {
			if ((x % i == 0))
				return false;
		}
		return true;
	}

	public int getD() {
		return d;
	}

	public int getP() {
		return p;
	}

	public int getQ() {
		return q;
	}

	public int getN() {
		return n;
	}

	public int getE() {
		return e;
	}
}
