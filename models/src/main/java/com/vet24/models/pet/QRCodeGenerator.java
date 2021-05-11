package com.vet24.models.pet;


import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.*;
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

    //private static final String QR_CODE_IMAGE_PATH = "./images/QRCode-300x300.png";
    /*private static final Resource QRCode = new ClassPathResource("static/images/QRCode-300x300.png");

    private static void generateQRCodeImage(String text, int width, int height, Resource filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(String.valueOf(filePath));
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) {
        try {
            generateQRCodeImage("This is my first QR Code", 300, 300, QRCode);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
    }*/




    /*String qrCodeData = "Hello World!";
    String filePath = "QRCode.png";
    String charset = "UTF-8"; // or "ISO-8859-1"
    Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);



        public void start () throws IOException, WriterException, NotFoundException {
            createQRCode(qrCodeData, filePath, charset, hintMap, 200,200);
            System.out.println("QR Code image created successfully!");

            System.out.println("Data read from QR Code: " + readQRCode(filePath, charset, hintMap));
        }


    public static void createQRCode(String qrCodeData, String filePath,
                                    String charset, Map hintMap, int qrCodeheight, int qrCodewidth)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(qrCodeData.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
        MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath
                .lastIndexOf('.') + 1), new File(filePath));
    }


    public static String readQRCode(String filePath, String charset, Map hintMap)
            throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
                hintMap);
        return qrCodeResult.getText();
    }*/
}
