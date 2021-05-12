package com.vet24.models.pet;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    private static final String QR_CODE_IMAGE_PATH = "./images/QRCode-300x300.png";

    public static byte[] generateQRCodeImage(String text) throws Exception {
        /*QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);*/

        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            // Установить кодировку
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300, hints);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }
}

    //private static final String QR_CODE_IMAGE_PATH = "./images/QRCode-300x300.png";
    /*private static final Resource QRCode = new ClassPathResource("static/images/QRCode-300x300.png");

    private static void generateQRCodeImage(String text, int width, int height, Resource filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(String.valueOf(filePath));
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);*/