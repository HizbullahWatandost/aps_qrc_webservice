package com.aps.qrc.controller;

import com.aps.qrc.exception.CustomErrorType;
import com.aps.qrc.model.Merchant;
import com.aps.qrc.service.QRCodeService;
import com.aps.qrc.util.PaymentFields;
import com.aps.qrc.util.QRCodeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@RestController
@RequestMapping("/qrcodeapi")
public class QRCodeRestAPIController {

    public static final Logger logger = LoggerFactory.getLogger(QRCodeRestAPIController.class);

    // Service which will do all creation and reading of QR Code image
    @Autowired
    QRCodeService qrCodeService;

    // request for generating QR Code image for the Merchant object.
    @RequestMapping(value = "/generator/", method = RequestMethod.POST)
    public ResponseEntity<?> generateQRImage(@RequestBody Merchant merchant, UriComponentsBuilder ucBuilder,
                                             HttpServletResponse response) throws Exception {

        logger.info("Creating QR Code For Payment Details: {}", merchant);

        // if the user doesn't provide any field for merchant object
        if (merchant == null) {
            logger.error("Failed to create QR image for empty merchant object!");
            return new ResponseEntity<Object>(new CustomErrorType("Can't create QR Image for empty merchant details!"),
                    HttpStatus.BAD_REQUEST);
        }

        // checking for missing or empty fields
        else if (merchant.getQrVersion() == 0
                || String.valueOf(merchant.getQrVersion()).isEmpty()
                || merchant.getQrType().isEmpty()
                || merchant.getMerchantCompanyName().isEmpty()
                || merchant.getMerchantCategory().isEmpty()
                || merchant.getBankAccount().isEmpty()) {
            logger.warn("Merchant fields are not complete, fields are missing!");
            return new ResponseEntity<Object>(new CustomErrorType("Merchant fields error!"), HttpStatus.BAD_REQUEST);
        }

        // checking for invalid merchant fields, it identifies dummy or invalid value for fields
        else if (String.valueOf(merchant.getQrVersion()).length() > 2
                || merchant.getQrType().length() > 1
                || merchant.getMerchantCompanyName().length() < 2
                || merchant.getMerchantCompanyName().length() > 50
                || merchant.getMerchantCategory().length() < 3
                || merchant.getMerchantCategory().length() > 25
                || merchant.getBankAccount().length() < 8
                || merchant.getBankAccount().length() > 15) {
            logger.warn("Fields are not as per the defined standard, it should be like {}",
                    "[qrVersion = 2, qrType = \"C\", merchantCompanyName = \"APS\", merchantCategory=\"IT\",PAN=\"123456789098765\"]");
            return new ResponseEntity<Object>(new CustomErrorType("Invalid merchant details!"), HttpStatus.BAD_REQUEST);
        }

        // the string builder to hold the payment fields
        StringBuilder qr_content = new StringBuilder();

        qr_content.append(PaymentFields.QRCODE_VERSION
                + PaymentFields.KEY_VALUE_DELIMITER
                + merchant.getQrVersion()
                + PaymentFields.FIELDS_DELIMITER);

        qr_content.append(PaymentFields.QRCode_TYPE
                + PaymentFields.KEY_VALUE_DELIMITER
                + merchant.getQrType()
                + PaymentFields.FIELDS_DELIMITER);

        qr_content.append(PaymentFields.MERCHANT_COMPANY_NAME
                + PaymentFields.KEY_VALUE_DELIMITER
                + merchant.getMerchantCompanyName()
                + PaymentFields.FIELDS_DELIMITER);

        qr_content.append(PaymentFields.MERCHANT_CATEGORY
                + PaymentFields.KEY_VALUE_DELIMITER
                + merchant.getMerchantCategory()
                + PaymentFields.FIELDS_DELIMITER);

        qr_content.append(PaymentFields.PAN
                + PaymentFields.KEY_VALUE_DELIMITER
                + merchant.getBankAccount());

        // here, we set the response type which should be an image
        response.setContentType("image/" + QRCodeProperties.QR_IMAGE_TYPE);
        OutputStream outputStream = response.getOutputStream();
        // displaying the image on the screen, which the user can save it
        outputStream.write(qrCodeService.generateQRCodeImage(
                QRCodeProperties.QR_CHARACTER_SET,
                QRCodeProperties.QR_ERROR_CORRECTION_LEVEL,
                QRCodeProperties.WIDTH, QRCodeProperties.HEIGHT,
                qr_content.toString(),
                QRCodeProperties.QR_IMAGE_TYPE));
        outputStream.flush();
        outputStream.close();

        logger.info("QR Image Has Been Successfully Generated With Payment Details Such As : {}", qr_content);

        // setting the header of response to indicate successful response
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/qrcodeapi/generator/{success}").buildAndExpand("success").toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/reader/", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<?> readQRImage(@RequestParam("file") MultipartFile file, HttpServletResponse response) {

        Merchant merchant_details = null;
        logger.info("Decoding The Imported QR Image: {}", file.getOriginalFilename());

        // Reading the content of QR Code image and storing it in the string variable
        String qr_img_content = null;
        try {
            qr_img_content = qrCodeService.readQRCodeImage(file.getInputStream());
            // Converting QR Content from string to merchant object
            merchant_details = qrCodeService.convertQRContentToMerchantObject(qr_img_content);

        } catch (NullPointerException npe) {
            logger.warn("InvalidQRCodeError: the QR image content can't be read!");
            return new ResponseEntity<Object>(new CustomErrorType("InvalidQRCodeError: can't read the content of this QR image,"), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            logger.warn("Exception: exception occured -> {}", ex);
            return new ResponseEntity<Object>(new CustomErrorType("InvalidQRCodeImg: invalid QR code image found"), HttpStatus.BAD_REQUEST);
        }
        logger.info("The QR Image Has Been Successfully Decoded: {}", merchant_details);
        // returns the QR Content as response in JSON format
        return new ResponseEntity<Merchant>(merchant_details, HttpStatus.OK);
    }
}
