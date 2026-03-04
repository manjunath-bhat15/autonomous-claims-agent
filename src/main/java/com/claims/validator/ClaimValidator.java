package com.claims.validator;

import com.claims.model.Claim;
import java.util.ArrayList;
import java.util.List;

public class ClaimValidator {

    public List<String> validate(Claim claim){

        List<String> missingFields = new ArrayList<>();

        if(claim.getPolicyNumber() == null){
            missingFields.add("policyNumber");
        } 
        if(claim.getPolicyholderName() == null){
            missingFields.add("policyholderName");
        }
        if(claim.getDateOfLoss() == null){
            missingFields.add("dateOfLoss");
        }
        
        if(claim.getLocation() == null){
            missingFields.add("location");
        }

        if(claim.getDescription() == null){
            missingFields.add("description");
        }

        if(claim.getEstimatedDamage() == null){
            missingFields.add("estimatedDamage");
        }

        if(claim.getClaimType() == null){
            missingFields.add("claimType");
        }
        return missingFields;
    }
}