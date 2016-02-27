package com.information_security.encrypters;

public class RandomXorEncoder implements Encrypt {

	private int A;
	private int C;
	private int T0;
	private int b;
	private int T[];

	public RandomXorEncoder() {
		A = 5;
		C = 3;
		T0 = 7;
		b = 6;
	}

	public RandomXorEncoder(int a, int c, int t0) {
		A = a;
		C = c;
		T0 = t0;
		b = 6;
	}

	public RandomXorEncoder(int a, int c, int t0, int b) {
		A = a;
		C = c;
		T0 = t0;
		this.b = b;
	}

	@Override
	public String encrypt(String input) {
		return decrypt(input);
	}

	@Override
	public String decrypt(String input) {
		try{
			generateTArray(input.length());
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < input.length(); i++) {
				result.append((char) (input.charAt(i) ^ T[i]));
			}
			return result.toString();	
		}
		catch(Exception e){
			return null;
		}
	}

	private void generateTArray(int length) {
		T = new int[length];
		T[0] = T0;
		for (int i = 1; i < T.length; i++) {
			T[i] = (A * T[i - i] + C) % ((int) Math.pow(2, b));
		}
	}
}
