package com.aps.qrc.util;


/**
 * QR Code Payment fields
 *
 * @author BeauTifuL_WorlD
 */
public class PaymentFields {
    // The delimiter which separate payment fields
    public static final String KEY_VALUE_DELIMITER = ":";
    public static final String FIELDS_DELIMITER = "*";
    // The payment fields
    public static final Integer QRCODE_VERSION = 1;
    public static final Integer QRCode_TYPE = 2;
    public static final Integer MERCHANT_COMPANY_NAME = 3;
    public static final Integer MERCHANT_CATEGORY = 4;
    public static final Integer PAN = 5;
}
