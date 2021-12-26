/*
 ******************************************************************************
 * Created by CrackStation.net on 2018.12.03
 * Copyright © 2018 CrackStation.net. Free reuse with citation of source.
 * Website: https://crackstation.net/hashing-security.htm
 * Code Provided At GitHub:
 * https://github.com/defuse/password-hashing/blob/master/PasswordStorage.java
 * Documentation by Osman Balci on 2021-07-13
 ******************************************************************************
 */
package edu.vt.globals;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;


public class Password {

    // @SuppressWarnings("serial") suppresses serialVersionUID warning messages
    @SuppressWarnings("serial")
    static public class InvalidHashException extends Exception {
        public InvalidHashException(String message) {
            super(message);
        }
        public InvalidHashException(String message, Throwable source) {
            super(message, source);
        }
    }

    @SuppressWarnings("serial")
    static public class CannotPerformOperationException extends Exception {
        public CannotPerformOperationException(String message) {
            super(message);
        }
        public CannotPerformOperationException(String message, Throwable source) {
            super(message, source);
        }
    }

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    //-----------------------------------------------------------------
    // These constants may be changed without breaking existing hashes.
    //-----------------------------------------------------------------
    public static final int SALT_BYTE_SIZE = 24;
    public static final int HASH_BYTE_SIZE = 18;


    public static final int PBKDF2_ITERATIONS = 64000;


    public static final int HASH_SECTIONS = 5;

    public static final int HASH_ALGORITHM_INDEX = 0;
    public static final int ITERATION_INDEX = 1;
    public static final int HASH_SIZE_INDEX = 2;
    public static final int SALT_INDEX = 3;
    public static final int PBKDF2_INDEX = 4;

    //  Create a Salted Hashed password String from the user-entered password String
    public static String createHash(String password) throws CannotPerformOperationException {
        return createHash(password.toCharArray());
    }

    public static String createHash(char[] password) throws CannotPerformOperationException {

        //-----------------------
        // Generate a Random Salt
        //-----------------------
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);

        //---------------------------------------------------------
        // Hash the Given Password with the Randomly Generated Salt
        //---------------------------------------------------------
        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        int hashSize = hash.length;


        String parts = "sha1:"
                + PBKDF2_ITERATIONS
                + ":" + hashSize
                + ":" + toBase64(salt)
                + ":" + toBase64(hash);
        /*
         Return a String containing the parts (parameters or sections)
         to be securely stored into the database and retrieved from.
         */
        return parts;
    }

    /*
    --------------------------------------------------------
     ***    Validate the password entered by the user    ***
     password    = password entered by the user as a String
     correctHash = String containing the parts created above
    --------------------------------------------------------
     */

    // Verifying Password
    public static boolean verifyPassword(String password, String correctHash)
            throws CannotPerformOperationException, InvalidHashException {
        return verifyPassword(password.toCharArray(), correctHash);
    }

    public static boolean verifyPassword(char[] password, String correctHash)
            throws CannotPerformOperationException, InvalidHashException {

        // Decode the hash into its parts (parameters or sections):
        String[] params = correctHash.split(":");
        if (params.length != HASH_SECTIONS) {
            throw new InvalidHashException(
                    "Fields are missing from the password hash."
            );
        }

        // Currently, Java only supports SHA1.
        if (!params[HASH_ALGORITHM_INDEX].equals("sha1")) {
            throw new CannotPerformOperationException(
                    "Unsupported hash type."
            );
        }

        int iterations = 0;
        try {
            iterations = Integer.parseInt(params[ITERATION_INDEX]);
        } catch (NumberFormatException ex) {
            throw new InvalidHashException(
                    "Could not parse the iteration count as an integer.",
                    ex
            );
        }

        if (iterations < 1) {
            throw new InvalidHashException(
                    "Invalid number of iterations. Must be >= 1."
            );
        }

        byte[] salt = null;
        try {
            // Convert the Salt string argument into an array of bytes.
            salt = fromBase64(params[SALT_INDEX]);
        } catch (IllegalArgumentException ex) {
            throw new InvalidHashException(
                    "Base64 decoding of salt failed.",
                    ex
            );
        }

        byte[] hash = null;
        try {
            // Convert the Hash string argument into an array of bytes.
            hash = fromBase64(params[PBKDF2_INDEX]);
        } catch (IllegalArgumentException ex) {
            throw new InvalidHashException(
                    "Base64 decoding of pbkdf2 output failed.",
                    ex
            );
        }

        int storedHashSize = 0;
        try {
            storedHashSize = Integer.parseInt(params[HASH_SIZE_INDEX]);
        } catch (NumberFormatException ex) {
            throw new InvalidHashException(
                    "Could not parse the hash size as an integer.",
                    ex
            );
        }

        if (storedHashSize != hash.length) {
            throw new InvalidHashException(
                    "Hash length doesn't match stored hash length."
            );
        }


        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);


        return slowEquals(hash, testHash);
    }


    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }


    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws CannotPerformOperationException {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            return skf.generateSecret(spec).getEncoded();

        } catch (NoSuchAlgorithmException ex) {
            throw new CannotPerformOperationException(
                    "Hash algorithm not supported.",
                    ex
            );
        } catch (InvalidKeySpecException ex) {
            throw new CannotPerformOperationException(
                    "Invalid key spec.",
                    ex
            );
        }
    }

    // Converts the String argument into an array of bytes.
    private static byte[] fromBase64(String hex)
            throws IllegalArgumentException {
        return Base64.getDecoder().decode(hex);
    }

    // Converts an array of bytes into a String.
    private static String toBase64(byte[] array) {
        return Base64.getEncoder().encodeToString(array);
    }

}
