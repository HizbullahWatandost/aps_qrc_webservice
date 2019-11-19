package com.aps.qrc.service;

import com.aps.qrc.model.Merchant;

import java.io.InputStream;

public interface QRCodeService {

    byte[] generateQRCodeImage(String qrCharacterSet, String qrErrorCorrectionLevel, int width, int height,
                               String qrContent, String qrImgExtension);

    String readQRCodeImage(InputStream inputStream);

    Merchant convertQRContentToMerchantObject(String qrStringContent);
}
