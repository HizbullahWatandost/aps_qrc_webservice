package com.aps.qrc.helper;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

@Component
public class ZXingHelper {

    public byte[] qrCodeImageGenerator(String qrCharacterSet, String qrErrorCorrectionLevel, int width, int height,
                                       String qrContent, String qrImgExtension) {
        try {
            Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, qrCharacterSet);

            // setting the border size default = 4
            hintMap.put(EncodeHintType.MARGIN, 2);
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.valueOf(qrErrorCorrectionLevel));

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height, hintMap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(byteMatrix, qrImgExtension, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    // Reading QR Image (second approach)
    public String readQRImg(InputStream inputStream) throws NotFoundException, IOException {

        BufferedImage qrimage = ImageIO.read(inputStream);
        int[] pixels = qrimage.getRGB(0, 0, qrimage.getWidth(), qrimage.getHeight(), null, 0, qrimage.getWidth());
        RGBLuminanceSource source = new RGBLuminanceSource(qrimage.getWidth(), qrimage.getHeight(), pixels);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }

}
