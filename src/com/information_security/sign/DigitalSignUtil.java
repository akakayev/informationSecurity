package com.information_security.sign;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DigitalSignUtil {

	private static final int SIMPLE_NUMBER_LIMIT = 2000;
	int p;
	int q;
	int a;
	int k;
	int x;
	int y;

	public DigitalSignUtil() {
		generateParams();
	}

	public Message generateMessageWithSign(String messageText) {
		Message message = new Message(messageText);
		int h = message.hashFunction();
		if (h % p == 0)
			h = 1;
		System.out.println("h= " + h);
		while (true) {
			k = generateK();
			System.out.println("k= " + k);
			int r = calculateR();
			System.out.println("r= " + r);
			int r1 = calculateR1(r);
			System.out.println("r1= " + r1);
			if (r1 == 0) {
				generateParams();
				continue;
			}
			int s = calculateS(x, r1, k, h, q);
			if (s == 0) {
				generateParams();
				continue;
			}
			message.setR1(r1);
			message.setS(s);
			break;
		}
		return message;
	}

	public boolean checkSign(Message message) {
		int r1 = message.getR1();
		System.out.println("r1= "+r1);
		int s = message.getS();
		System.out.println("s= "+s);
		if ((r1 <= 0 && r1 >= q) || (s <= 0 && s >= q))
			return false;
		int h = message.hashFunction() % p % q;
		if (h % p == 0)
			h = 1;
		int v=genV(h, q);
		int z1=genZ1(s, v, q);
		int z2=genZ2(r1, v, q);
		return genU(a, z1, y, z2, p, q) == r1;
	}

	public void generateParams() {
		while (true) {
			p = generateP();
			if(p<30)continue;
			System.out.println("p= " + p);
			try {
				q = generateQ();
				if(q<30)continue;
				System.out.println("q= " + q);
			} catch (Exception e1) {
				System.out.println("q=0");
				continue;
			}
			try {
				a = calculateA();
				System.out.println("a= " + a);
			} catch (Exception e) {
				System.out.println("a=0");
				continue;
			}
			x = generateX();
			y = calculateY(a, x, p);
			System.out.println("x= " + x);
			System.out.println("y= " + y);
			break;
		}
	}

	private int generateK() {
		return generateRandomIntUpTo(q);
	}

	private int generateSimpleNumber(int limit) {
		List<Integer> simples = new LinkedList<Integer>();
		simples.add(2);
		for (int i = 3; i < limit; i++) {
			if (!hasDividersIn(simples, i))
				simples.add(i);
		}
		return simples.get(new Random().nextInt(simples.size() - simples.size() / 2) + simples.size() / 2);
	}

	private boolean hasDividersIn(List<Integer> list, int x) {
		for (int i : list) {
			if (x % i == 0)
				return true;
		}
		return false;
	}

	public class Message {
		private String messageText;
		private int r1;
		private int s;

		public Message(String text) {
			messageText = text;
		}

		public int hashFunction() {
			int code = 0;
			for (int i = 0; i < messageText.length(); i++) {
				char[] symbolCode = Integer.toBinaryString(messageText.charAt(i)).toCharArray();
				for (char c : symbolCode) {
					if (c == '1')
						code++;
				}
			}
			return code;
		}

		public int getR1() {
			return r1;
		}

		public void setR1(int r1) {
			this.r1 = r1;
		}

		public int getS() {
			return s;
		}

		public void setS(int s) {
			this.s = s;
		}

		public String getMessageText() {
			return messageText;
		}
	}

	public static void main(String[] args) {
		Message m = new DigitalSignUtil().new Message("message");
		List<Boolean> res = new LinkedList<Boolean>();
		for (int i = 0; i < 10; i++) {
			DigitalSignUtil s = new DigitalSignUtil();
			m = s.generateMessageWithSign("message");
			// System.out.println(s.checkSign(m));
			if (s.checkSign(m))
				res.add(true);
		}
		System.out.println("otvet= " + res.size());
	}

	private int generateP() {
		ArrayList<Integer> sequanseOfSimpleNumbers = getSequanseOfSimpleNumbers(SIMPLE_NUMBER_LIMIT);
		return sequanseOfSimpleNumbers.get((int) (Math.random() * sequanseOfSimpleNumbers.size()));
	}

	private int generateQ() throws Exception {
		ArrayList<Integer> sequanseOfSimpleNumbers = getSequanseOfSimpleNumbers(p);
		for (int i = sequanseOfSimpleNumbers.size() - 1; i > 0; i--) {
			if ((p - 1) % sequanseOfSimpleNumbers.get(i) == 0) {
				return sequanseOfSimpleNumbers.get(i);
			}
		}
		throw new Exception("");
	}

	private int calculateA() throws Exception {
		for (a = p - 2; a > 0; a--) {
			BigInteger a1 = new BigInteger(a + "");
			if (a1.pow(q).mod(new BigInteger(p + "")).intValue() == 1)
				return a;
		}
		throw new Exception("A=0");
	}

	private int generateX() {
		return generateRandomIntUpTo(q);
	}

	private int calculateR1(int r) {
		return r % q;
	}

	private int calculateR() {
		return calculateY(a, k, p);
	}

	private int calculateY(int a, int x, int p) {
		BigInteger y = new BigInteger(a + "");
		return y.pow(x).mod(new BigInteger(p + "")).intValue();
	}

	private int calculateS(int x, int r1, int k, int m, int q) {
		return (x * r1 + k * m) % q;
	}

	private int genV(int h, int Q) {
		BigInteger h_m = new BigInteger(h + "");
		BigInteger q = new BigInteger(Q + "");
		return (h_m.pow(q.intValue() - 2)).mod(q).intValue();
	}

	private int genZ1(int s, int v, int q) {
		return s * v % q;
	}

	private int genZ2(int r1, int v, int q) {
		return ((q - r1) * v) % q;
	}

	private int genU(int A, int z1, int Y, int z2, int P, int Q) {
		BigInteger a = new BigInteger(String.valueOf(A));
		BigInteger y = new BigInteger(String.valueOf(Y));
		BigInteger p = new BigInteger(String.valueOf(P));
		BigInteger q = new BigInteger(String.valueOf(Q));
		BigInteger u;
		try {
			u = (a.pow(z1).multiply(y.pow(z2))).mod(p).mod(q);
		} catch (Exception e) {
			System.out.println("bug " + z1 + " " + z2);
			return 0;
		}
		System.out.println("u= "+u.intValue());
		return u.intValue();
	}

	private int generateRandomIntUpTo(int max) {
		max = (int) (Math.random() * max);
		if (max == 0)
			max = 1;
		return max;
	}

	private ArrayList<Integer> getSequanseOfSimpleNumbers(int max) {
		ArrayList<Integer> sequance = new ArrayList<Integer>();
		sequance.add(2);
		for (int i = 3; i < max; i++) {
			for (int j = 0; j < sequance.size(); j++) {
				if (i % sequance.get(j) == 0)
					break;
				if (j == sequance.size() - 1) {
					sequance.add(i);
				}
			}
		}
		return sequance;
	}
}
