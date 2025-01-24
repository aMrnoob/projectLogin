package com.example.demo.model;

import java.util.concurrent.ConcurrentHashMap;

public class OtpStorage {
	public static final ConcurrentHashMap<String, OtpEntry> otpStorage = new ConcurrentHashMap<>();
}
