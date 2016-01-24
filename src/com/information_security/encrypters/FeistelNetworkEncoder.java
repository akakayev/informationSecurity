package com.information_security.encrypters;

public class FeistelNetworkEncoder implements Encrypt {

	private int R;
	private char X1[];
	private char X2[];

	public FeistelNetworkEncoder(int R) {
		this.R = R;
	}

	@Override
	public String encrypt(String inputMessage) {
		devideMessage(inputMessage);
		for (int i = 1; i <= R; i++) {
			char buffer[] = X2;
			X2 = X1;
			X1 = xor(buffer, F(X1, i));
		}
//		return new String(new char[]{(char)X1[0],(char)X1[1],(char)X1[2]})+new String(new char[]{(char)X2[0],(char)X2[1],(char)X2[2]});
		return new String(X1) + new String(X2);
	}

	@Override
	public String decrypt(String inputMessage) {
		if (inputMessage.length() % 2 != 0) {
			return null;
		}
		devideMessage(inputMessage);
		for (int i = R; i >0; i--) {
			char buffer[] = X1;
			X1 = X2;
			X2 = xor(buffer, F(X2, i));
		}
//		return new String(new char[]{(char)X1[0],(char)X1[1],(char)X1[2]})+new String(new char[]{(char)X2[0],(char)X2[1],(char)X2[2]});
		return new String(X1) + new String(X2);
	}

	private char[] F(char X[], int r) {
		char result[] = new char[X.length];
		int i = 0;
		for (char c : X) {
			result[i++] = (char)(c+r);
		}
		return result;
	}

	private char[] xor(char X1[], char X2[]) {
		char result[] = new char[X1.length];
		int i = 0;
		for (char c : result) {
			result[i] =  (char)((X1[i] ^ X2[i]));
			i++;
		}
		return result;
	}

	private void devideMessage(String inputMessage) {
		char X[] = inputMessage.toCharArray();
		X1 = new char[X.length / 2 + X.length % 2];
		X2 = new char[X.length / 2 + X.length % 2];
		if (X.length % 2 != 0) {
			X2[X.length / 2] = (int)' ';
		}
		for (int j = 0; j < X1.length; j++) {
			X1[j] = X[j];
		}
		for (int j = (X.length % 2==0)?X.length / 2:X.length / 2+1 ,i=0; j < X.length; j++,i++) {
			X2[i] = X[j];
		}
	}
	
	public static void main(String[] args) {
		FeistelNetworkEncoder encoder= new FeistelNetworkEncoder(12);
		System.out.println(encoder.encrypt("Feistel network"));
		System.out.println(encoder.decrypt(encoder.encrypt("Feistel network")));
	}
}
