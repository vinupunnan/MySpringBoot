package com.kailas.mm.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

public class HashUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();
   public static String  canonicalJson(Map<String,String> alert) throws JsonProcessingException {

     return   MAPPER.writeValueAsString(new TreeMap<>(alert));

    }

    public static String sha256(String  input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] out = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder(out.length * 2);
        for (byte b : out) sb.append(String.format("%02x", b));
        return sb.toString();

    }
}
