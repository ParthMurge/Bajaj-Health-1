package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BajajP1Application {

//	public static void main(String[] args) {
//		SpringApplication.run(BajajP1Application.class, args);
//	}
	
	public static void main(String[] args) {
//        if (args.length != 2) {
//            System.out.println("Please provide the PRN number and the JSON file path.");
//            return;
//        }

//        String prn = args[0].toLowerCase();
        String prn = "240340120122";
//        String filePath = args[1];
        String filePath = "D:\\example.txt";

        try {
            // Step 2: Read and Parse JSON File
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(jsonContent.toString());

            // Step 3: Traverse JSON to find the first instance of "destination"
            String destinationValue = findDestination(jsonObject);

            if (destinationValue == null) {
                System.out.println("No 'destination' key found in the JSON file.");
                return;
            }

            // Step 4: Generate a random 8-character alphanumeric string
            String randomString = generateRandomString(8);

            // Step 5: Concatenate PRN, destination value, and random string
            String combinedString = prn + destinationValue + randomString;

            // Step 6: Generate MD5 hash
            String hash = generateMD5Hash(combinedString);

            // Step 7: Print the result in the format <hash>;<random string>
            System.out.println(hash + ";" + randomString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String findDestination(JSONObject jsonObject) {
        for (Object keyObj : jsonObject.keySet()) {
            String key = keyObj.toString();
            if (key.equals("destination")) {
                return jsonObject.getString(key);
            } else if (jsonObject.get(key) instanceof JSONObject) {
                String result = findDestination(jsonObject.getJSONObject(key));
                if (result != null) return result;
            }
        }
        return null;
    }


    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private static String generateMD5Hash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : messageDigest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
