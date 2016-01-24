package com.information_security.encrypters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SubstitutionEncoder implements Encrypt {

	private Map<Character, Character> encryptTable;
	private Map<Character, Character> decryptTable;

	public SubstitutionEncoder() {
//		File file = new File("/home/artyom/Desktop/s.txt");
		File file = new File("C:\\Users\\Артём\\Desktop\\учеба\\ЗИ\\laba1_ID\\src\\s.txt");
		encryptTable = new HashMap<Character, Character>();
		decryptTable = new HashMap<Character, Character>();
		try (BufferedReader in = new BufferedReader(
				new FileReader(file))) {
			String line = "";
			while ((line = in.readLine()) != null) {
				encryptTable.put(line.charAt(0), line.charAt(line.length() - 1));
				decryptTable.put(line.charAt(line.length() - 1), line.charAt(0));
			}
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}

	}

	@Override
	public String encrypt(String input) {
		StringBuilder otput = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			otput.append(encryptTable.get(input.charAt(i)));
		}
		return otput.toString();
	}

	@Override
	public String decrypt(String input) {
		StringBuilder otput = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			otput.append(decryptTable.get(input.charAt(i)));
		}
		return otput.toString();
	}

}
