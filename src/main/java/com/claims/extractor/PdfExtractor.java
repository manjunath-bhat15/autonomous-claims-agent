package com.claims.extractor;
import com.claims.model.Claim;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfExtractor {

    public Claim extract(String filePath) throws Exception {

        String text = readFile(filePath);

        text = text.replace("\r", "");
        text = text.replaceAll("\\n+", "\n");
        Claim claim = new Claim();
        claim.setPolicyNumber(
                findBetween(text, "POLICY NUMBER", "CONTACT")
        );

        claim.setPolicyholderName(
                extractName(text)
        );

        claim.setDateOfLoss(
                findSimplePattern(text, "\\d{2}/\\d{2}/\\d{4}")
        );

        claim.setTimeOfLoss(
                findSimplePattern(text, "\\d{1,2}:\\d{2}\\s?(AM|PM)")
        );

        claim.setLocation(
                extractLocation(text)
        );

        claim.setDescription(
                findBetween(text,
                        "DESCRIPTION OF ACCIDENT",
                        "INSURED VEHICLE")
        );

        claim.setEstimatedDamage(extractDamage(text));
        claim.setInitialEstimate(claim.getEstimatedDamage());

        claim.setAssetType("Vehicle");
       
        if (claim.getPolicyNumber() == null)
            claim.setPolicyNumber(findLineValue(text, "Policy Number:"));

        if (claim.getPolicyholderName() == null)
            claim.setPolicyholderName(findLineValue(text, "Policyholder Name:"));

        if (claim.getEffectiveDates() == null)
            claim.setEffectiveDates(findLineValue(text, "Policy Effective Dates:"));

        if (claim.getDateOfLoss() == null)
            claim.setDateOfLoss(findLineValue(text, "Date of Loss:"));

        if (claim.getTimeOfLoss() == null)
            claim.setTimeOfLoss(findLineValue(text, "Time of Loss:"));

        if (claim.getLocation() == null)
            claim.setLocation(findLineValue(text, "Location"));

        if (claim.getDescription() == null)
            claim.setDescription(findLineValue(text, "Description"));

        if (claim.getAssetType() == null)
            claim.setAssetType(findLineValue(text, "Asset Type:"));

        if (claim.getAssetId() == null)
            claim.setAssetId(findLineValue(text, "Asset ID:"));

        if (claim.getEstimatedDamage() == null) {
            Integer damage = parseNumber(findLineValue(text, "Estimated Damage"));
            claim.setEstimatedDamage(damage);
            claim.setInitialEstimate(damage);
        }

        if (claim.getClaimType() == null)
            claim.setClaimType(findLineValue(text, "Claim Type:"));

        if (claim.getClaimant() == null)
            claim.setClaimant(findLineValue(text, "Claimant Name:"));

        if (claim.getThirdParties() == null)
            claim.setThirdParties(findLineValue(text, "Third Parties"));

        if (claim.getContactDetails() == null)
            claim.setContactDetails(findLineValue(text, "Contact"));

        if (claim.getAttachments() == null)
            claim.setAttachments(findLineValue(text, "Attachments"));

        //Check if claim type can be inferred from description
        if (claim.getClaimType() == null) {
            if (claim.getDescription() != null && claim.getDescription().toLowerCase().contains("injur")) {
                claim.setClaimType("injury");
            } else {
                claim.setClaimType("property");
            }
        }

        return claim;
    }

    private String findBetween(String text, String start, String end) {
        String upper = text.toUpperCase();
        int startIndex = upper.indexOf(start.toUpperCase());
        if (startIndex == -1) return null;
        startIndex += start.length();
        int endIndex = upper.indexOf(end.toUpperCase(), startIndex);
        if (endIndex == -1) return null;
        if (endIndex > startIndex) {
            return text.substring(startIndex, endIndex).trim();
        }
        return null;
    }

    private String extractName(String text) {
        int index = text.toUpperCase().indexOf("NAME OF INSURED");
        if (index == -1) return null;
        String remaining = text.substring(index).trim();
        String[] lines = remaining.split("\n");
        for (String line : lines) {
            String clean = line.trim();
            if (clean.isEmpty()) continue;
            if (clean.contains("First") || clean.contains("Middle"))
                continue;
            if (clean.toUpperCase().contains("NAME OF INSURED")) 
                continue;
            return clean;
        }
        return null;
    }

    private String extractLocation(String text) {
        int index = text.toUpperCase().indexOf("LOCATION OF LOSS:");
        if (index == -1) return null;
        String remaining = text.substring(index + "LOCATION OF LOSS:".length()).trim();
        String[] lines = remaining.split("\n");
        if (lines.length > 0) {
            String location = lines[0];
            location = location.replace("STREET:", "").trim();
            return location;
        }
        return null;
    }

    private Integer extractDamage(String text) {
        Pattern pattern = Pattern.compile(
                "ESTIMATE AMOUNT:\\s*(\\d+)",
                Pattern.CASE_INSENSITIVE
        );
    Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (Exception ignored) {}
        }
        return null;
    }

    private String findSimplePattern(String text, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group().trim();
        }
        return null;
    }

    private String findLineValue(String text, String label) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.toLowerCase().startsWith(label.toLowerCase())) {
                return line.substring(line.indexOf(":") + 1).trim();
            }
        }
        return null;
    }

    private Integer parseNumber(String value) {
        if (value == null) return null;
        try {
            return Integer.parseInt(value.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }
    }
    // Reads text content from a file, supporting both PDF and plain text formats
    private String readFile(String filePath) throws Exception {

        if (filePath.toLowerCase().endsWith(".pdf")) {

            PDDocument document = PDDocument.load(new File(filePath));
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            document.close();

            return text;
        }

        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}