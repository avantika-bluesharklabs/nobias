package com.nobias.businesslogic.crypt;

import android.os.Build;

import java.util.UUID;

public class CryptUniqueIdGenerator {

    public static String DEVICE_ID = "";
    public static String ENCRYPTED_DEVICE_ID = "";

    /**
     *
     * @return Unique Device Id
     */
    public static String getCryptUniqueId() {

        if (DEVICE_ID != null && DEVICE_ID.length() > 0)
            return DEVICE_ID;

        @SuppressWarnings("deprecation")
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10)
                + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10)
                + (Build.DEVICE.length() % 10)
                + (Build.MANUFACTURER.length() % 10)
                + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial;
        try {
            serial = Build.class.getField("SERIAL").get(null)
                    .toString();

            // Go ahead and return the serial for api => 9
            DEVICE_ID = new UUID(m_szDevIDShort.hashCode(), serial.hashCode())
                    .toString();

            return DEVICE_ID;
        } catch (Exception exception) {
            serial = "XdeQ2Dr4Xe6L";
        }

        DEVICE_ID = new UUID(m_szDevIDShort.hashCode(), serial.hashCode())
                .toString();
        return DEVICE_ID;
    }

    /**
     *
     * @return Encrypted Device ID
     */
    public static String getEncryptedUniqueID() {

        if (ENCRYPTED_DEVICE_ID != null && ENCRYPTED_DEVICE_ID.length() > 0)
            return ENCRYPTED_DEVICE_ID;
        String ddid = getCryptUniqueId();
        try {
            CryptLib _crypt = new CryptLib();
            ENCRYPTED_DEVICE_ID = _crypt.EncryptString( ddid);
        } catch (Exception e) {
            ENCRYPTED_DEVICE_ID = ddid;
            e.printStackTrace();
        }

        return ENCRYPTED_DEVICE_ID.trim();

    }
}
