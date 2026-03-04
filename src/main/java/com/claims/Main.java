package com.claims;

import com.claims.extractor.PdfExtractor;
import com.claims.model.Claim;
import com.claims.router.ClaimRouter;
import com.claims.validator.ClaimValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.out.println("Usage: mvn exec:java -Dexec.args=<file-or-folder>");
            return;
        }

        File input = new File(args[0]);

        if (!input.exists()) {
            System.out.println("File or folder not found.");
            return;
        }

        if (input.isDirectory()) {

            System.out.println("Processing all files in folder: " + input.getName());
            System.out.println("===============================================\n");

            File[] files = input.listFiles();

            if (files != null) {
                for (File file : files) {

                    if (file.isFile()) {
                        processFile(file);
                        System.out.println("-----------------------------------------------\n");
                    }
                }
            }

        } else {
            processFile(input);
        }
    }

    private static void processFile(File file) throws Exception {

        System.out.println("Processing: " + file.getName());

        PdfExtractor extractor = new PdfExtractor();
        Claim claim = extractor.extract(file.getPath());

        ClaimValidator validator = new ClaimValidator();
        List<String> missingFields = validator.validate(claim);

        ClaimRouter router = new ClaimRouter();
        String[] routeResult = router.determineRoute(claim, missingFields);

        Map<String, Object> output = new HashMap<>();
        output.put("extractedFields", claim);
        output.put("missingFields", missingFields);
        output.put("recommendedRoute", routeResult[0]);
        output.put("reasoning", routeResult[1]);

        ObjectMapper mapper = new ObjectMapper();

        System.out.println(
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(output)
        );
    }
}