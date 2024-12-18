//import com.itextpdf.forms.PdfAcroForm;
//import com.itextpdf.forms.fields.PdfFormField;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfReader;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.kernel.pdf.StampingProperties;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;
//import com.itextpdf.signatures.*;
//
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.security.KeyStore;
//import java.security.PrivateKey;
//import java.security.cert.Certificate;
//import java.util.Collections;
//
//public class PDFWithSignature {
//    // Path for PDF file creation
//    private static final String DEST = "example.pdf";
//
//    public static void main(String[] args) throws Exception {
//        // Step 1: Create PDF
//        createPdf(DEST);
//
//        // Step 2: Optional - Digitally Sign the PDF using DSA
//        String signedPdf = "signed_example.pdf";
//        signPdf(DEST, signedPdf, "dsa_keystore.jks", "password");
//    }
//
//    public static void createPdf(String dest) throws IOException {
//        PdfWriter writer = new PdfWriter(dest);
//        PdfDocument pdfDoc = new PdfDocument(writer);
//        Document document = new Document(pdfDoc);
//
//        // Add some content
//        document.add(new Paragraph("This is a PDF with an optional signature."));
//
//        // Add an optional signature field
//        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
//        PdfFormField signatureField = PdfFormField.createSignature(pdfDoc);
//        signatureField.setFieldName("SignatureField");
//        form.addField(signatureField);
//
//        document.close();
//    }
//
//    public static void signPdf(String src, String dest, String keyStorePath, String keyStorePassword) throws Exception {
//        // Load keystore
//        KeyStore keyStore = KeyStore.getInstance("JKS");
//        try (FileInputStream keyStoreStream = new FileInputStream(keyStorePath)) {
//            keyStore.load(keyStoreStream, keyStorePassword.toCharArray());
//        }
//
//        // Retrieve private key and certificate
//        String alias = Collections.list(keyStore.aliases()).get(0); // Assuming one alias
//        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyStorePassword.toCharArray());
//        Certificate[] chain = keyStore.getCertificateChain(alias);
//
//        // Sign the PDF
//        PdfReader reader = new PdfReader(src);
//        PdfWriter writer = new PdfWriter(dest);
//        PdfDocument pdfDoc = new PdfDocument(reader, writer);
//
//        PdfSigner signer = new PdfSigner(pdfDoc, new FileOutputStream(dest), new StampingProperties());
//        IExternalSignature pks = new PrivateKeySignature(privateKey, "SHA256", "BC");
//        IExternalDigest digest = new BouncyCastleDigest();
//        signer.signDetached(digest, pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CMS);
//
//        pdfDoc.close();
//    }
//}
