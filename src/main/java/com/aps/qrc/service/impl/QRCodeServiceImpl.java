package com.aps.qrc.service.impl;

import com.aps.qrc.helper.ZXingHelper;
import com.aps.qrc.model.Merchant;
import com.aps.qrc.service.QRCodeService;
import com.google.zxing.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service("qrCodeService")
public class QRCodeServiceImpl implements QRCodeService {

    @Autowired
    private ZXingHelper zXingHelper;

    @Override
    public byte[] generateQRCodeImage(String qrCharacterSet, String qrErrorCorrectionLevel, int width, int height,
                                      String qrContent, String qrImgExtension) {
        return zXingHelper.qrCodeImageGenerator(qrCharacterSet, qrErrorCorrectionLevel, width, height, qrContent, qrImgExtension);
    }

    @Override
    public String readQRCodeImage(InputStream inputStream) {
        // TODO Auto-generated method stub
        try {
            return zXingHelper.readQRImg(inputStream);
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Merchant convertQRContentToMerchantObject(String qrStringContent) {

        Merchant merchant = new Merchant();
        String[] qr2Txt = qrStringContent.split("\\*");

        for (String paymentField : qr2Txt) {

            String[] keyValuePair = paymentField.split("\\:");
            int key = Integer.parseInt(keyValuePair[0]);
            String value = keyValuePair[1];

            switch (key) {
                case 1:
                    merchant.setQrVersion(Integer.parseInt(value));
                    break;
                case 2:
                    merchant.setQrType(value);
                    break;
                case 3:
                    merchant.setMerchantCompanyName(value);
                    break;
                case 4:
                    merchant.setMerchantCategory(value);
                    break;
                case 5:
                    merchant.setBankAccount(value);
                    break;
            }
        }
        return merchant;
    }
}
