package com.claims.model;

public class Claim {

    // Policy Information
    private String policyNumber;
    private String policyholderName;
    private String effectiveDates;

    // Incident Information
    private String dateOfLoss;
    private String timeOfLoss;
    private String location;
    private String description;

    // Involved Parties
    private String claimant;
    private String thirdParties;
    private String contactDetails;

    // Asset Details
    private String assetType;
    private String assetId;
    private Integer estimatedDamage;

    // Other Mandatory Fields
    private String claimType;
    private String attachments;
    private Integer initialEstimate;

    // --- Getters and Setters ---

    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }

    public String getPolicyholderName() { return policyholderName; }
    public void setPolicyholderName(String policyholderName) { this.policyholderName = policyholderName; }

    public String getEffectiveDates() { return effectiveDates; }
    public void setEffectiveDates(String effectiveDates) { this.effectiveDates = effectiveDates; }

    public String getDateOfLoss() { return dateOfLoss; }
    public void setDateOfLoss(String dateOfLoss) { this.dateOfLoss = dateOfLoss; }

    public String getTimeOfLoss() { return timeOfLoss; }
    public void setTimeOfLoss(String timeOfLoss) { this.timeOfLoss = timeOfLoss; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getClaimant() { return claimant; }
    public void setClaimant(String claimant) { this.claimant = claimant; }

    public String getThirdParties() { return thirdParties; }
    public void setThirdParties(String thirdParties) { this.thirdParties = thirdParties; }

    public String getContactDetails() { return contactDetails; }
    public void setContactDetails(String contactDetails) { this.contactDetails = contactDetails; }

    public String getAssetType() { return assetType; }
    public void setAssetType(String assetType) { this.assetType = assetType; }

    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }

    public Integer getEstimatedDamage() { return estimatedDamage; }
    public void setEstimatedDamage(Integer estimatedDamage) { this.estimatedDamage = estimatedDamage; }

    public String getClaimType() { return claimType; }
    public void setClaimType(String claimType) { this.claimType = claimType; }

    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }

    public Integer getInitialEstimate() { return initialEstimate; }
    public void setInitialEstimate(Integer initialEstimate) { this.initialEstimate = initialEstimate; }
}