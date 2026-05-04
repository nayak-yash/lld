package service;

import models.OtpInfo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OtpService {
    private final Map<String, String> otpToSlotMap = new HashMap<>();
    private final Map<String, String> otpToLockerMap = new HashMap<>();
    private final Map<String, String> slotToOtpMap = new HashMap<>();

    public OtpInfo generateOtp(String lockerName, String slotId) {
        String otp = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        LocalDate expiry = LocalDate.now().plusDays(3);
        otpToSlotMap.put(otp, slotId);
        otpToLockerMap.put(otp, lockerName);
        slotToOtpMap.put(lockerName + ":" + slotId, otp);

        System.out.println("[OtpService] Generated OTP: " + otp + " for locker '" + lockerName + "', slot '" + slotId + "', valid until " + expiry);
        return new OtpInfo(otp, expiry);
    }

    public String validateAndGetSlotId(String otp, String lockerName) {
        if (!otpToSlotMap.containsKey(otp)) {
            throw new RuntimeException("Invalid OTP: " + otp);
        }
        String expectedLocker = otpToLockerMap.get(otp);
        if (!expectedLocker.equals(lockerName)) {
            throw new RuntimeException("OTP does not belong to locker: " + lockerName);
        }
        return otpToSlotMap.get(otp);
    }

    public void invalidateOtp(String otp) {
        String lockerName = otpToLockerMap.get(otp);
        String slotId = otpToSlotMap.get(otp);
        if (lockerName != null && slotId != null) {
            slotToOtpMap.remove(lockerName + ":" + slotId);
        }
        otpToSlotMap.remove(otp);
        otpToLockerMap.remove(otp);
        System.out.println("[OtpService] OTP " + otp + " has been invalidated.");
    }

    public String getOtpBySlot(String lockerName, String slotId) {
        return slotToOtpMap.get(lockerName + ":" + slotId);
    }
}
