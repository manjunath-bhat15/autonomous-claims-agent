# Autonomous Insurance Claims Processing Agent

## Overview

This project implements a lightweight Autonomous Insurance Claims Processing Agent that processes First Notice of Loss (FNOL) documents.

The system extracts key claim information from PDF and TXT documents, validates required fields, classifies the claim, and determines the appropriate workflow route.

The final result is returned as a **structured JSON response** containing:

--Extracted fields
--Missing fields
--Recommended routing decision
--Explanation for the decision

---

# System Architecture

FNOL Document (PDF / TXT)
        │
        ▼
   PdfExtractor
(Field Extraction Layer)
        │
        ▼
  ClaimValidator
(Mandatory Field Check)
        │
        ▼
   ClaimRouter
(Routing Decision Engine)
        │
        ▼
     JSON Output

## Project Components

1. PdfExtractor



2️⃣ Claim Model

The Claim class stores all extracted information.

Fields

Policy Information

Policy Number

Policyholder Name

Effective Dates

Incident Information

Date of Loss

Time of Loss

Location

Description

Involved Parties

Claimant

Third Parties

Contact Details

Asset Details

Asset Type

Asset ID

Estimated Damage

Additional Fields

Claim Type

Attachments

Initial Estimate

3️⃣ ClaimValidator

Checks if mandatory fields are missing.

Missing fields are returned in a list used for routing decisions.

Example:

"missingFields": [
  "policyNumber",
  "location"
]
4️⃣ ClaimRouter

Determines the correct workflow route using rule-based logic.

Routing Rules
Rule	Result
Description contains fraud keywords	Investigation Flag
Missing mandatory fields	Manual Review
Claim type = injury	Specialist Queue
Estimated damage < 25000	Fast-track
Otherwise	Standard Processing

Fraud keywords checked:

fraud

inconsistent

staged

5️⃣ Main Application

The Main class orchestrates the processing pipeline:

Reads file or folder input

Extracts claim fields

Validates mandatory fields

Applies routing rules

Produces JSON output

Example Input

TXT FNOL document:

Policy Number: POL-778899
Policyholder Name: Arjun Kumar
Date of Loss: 04/05/2026
Time of Loss: 10:15 AM
Location: Hyderabad
Description: Minor vehicle damage
Asset Type: Vehicle
Asset ID: TS09AB1234
Estimated Damage: 15000
Claim Type: property
Example Output
{
  "extractedFields": {
    "policyNumber": "POL-778899",
    "policyholderName": "Arjun Kumar",
    "dateOfLoss": "04/05/2026",
    "timeOfLoss": "10:15 AM",
    "location": "Hyderabad",
    "estimatedDamage": 15000,
    "claimType": "property"
  },
  "missingFields": [],
  "recommendedRoute": "Fast-track",
  "reasoning": "Estimated damage below 25,000 threshold."
}
Technology Stack
Technology	Purpose
Java 21	Core application
Maven	Build & dependency management
Apache PDFBox	PDF extraction
Jackson Databind	JSON serialization
How To Run
Build the project
mvn clean install
Run with a single file
mvn exec:java --% -Dexec.args=test.txt
Run with folder input
mvn exec:java --% -Dexec.args=claims-folder
Design Considerations
Hybrid Extraction Strategy

The extractor combines:

Regex detection

Section-based parsing

Line-based fallback parsing

This ensures reliable extraction from both:

Semi-structured PDFs

Structured TXT files

Future Improvements

NLP-based entity extraction

Machine learning fraud detection

REST API for real-time claim processing

Database persistence layer

Integration with claim management systems