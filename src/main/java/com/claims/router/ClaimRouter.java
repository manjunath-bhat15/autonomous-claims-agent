package com.claims.router;

import com.claims.model.Claim;
import java.util.List;

public class ClaimRouter {

    public String[] determineRoute(Claim claim, List<String> missingFields) {
        String description = "";
        if (claim.getDescription() != null) {
            description = claim.getDescription().toLowerCase();
        }

        // Rule 1: Fraud keywords
        if (description.contains("fraud") ||
            description.contains("inconsistent") ||
            description.contains("staged")) {

            return new String[] {
                "Investigation Flag",
                "Fraud-related keywords detected in description."
            };
        }

        // Rule 2: Missing mandatory fields
        if (!missingFields.isEmpty()) {

            return new String[] {
                "Manual Review",
                "Mandatory fields are missing."
            };
        }

        // Rule 3: Injury claim type
        if ("injury".equalsIgnoreCase(claim.getClaimType())) {

            return new String[] {
                "Specialist Queue",
                "Claim type identified as injury."
            };
        }

        // Rule 4: Fast-track condition
        if (claim.getEstimatedDamage() != null &&
            claim.getEstimatedDamage() < 25000) 
            {
            return new String[] {
                "Fast-track",
                "Estimated damage below 25,000 threshold."
            };
        }
        return new String[] {
            "Standard Processing",
            "Claim meets standard processing rules."
        };
    }
}