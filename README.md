# Autonomous Insurance Claims Processing Agent

## Overview

The **Autonomous Insurance Claims Processing Agent** is a lightweight
rule-based system designed to process **First Notice of Loss (FNOL)**
documents.

The system automatically performs the following tasks:

-   Extracts structured claim information from **PDF and TXT files**
-   Validates mandatory claim fields
-   Classifies the claim
-   Determines the appropriate **workflow routing decision**

The final output is produced as a **structured JSON response**
containing:

-   Extracted fields
-   Missing fields
-   Recommended routing decision
-   Reasoning behind the decision

This project demonstrates how automation can be applied to **early-stage
insurance claim processing workflows**.

------------------------------------------------------------------------

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

------------------------------------------------------------------------

# Project Components

## 1. PdfExtractor

The **PdfExtractor** module extracts text from **PDF and TXT FNOL
documents**.

The system uses a hybrid extraction approach combining:

-   Regex pattern detection
-   Section-based parsing
-   Line-by-line fallback parsing

This ensures reliable extraction from:

-   Semi-structured PDFs
-   Structured TXT claim reports

------------------------------------------------------------------------

## 2. Claim Model

The `Claim` class represents the structured claim data extracted from
the FNOL document.

### Policy Information

-   Policy Number
-   Policyholder Name
-   Effective Dates

### Incident Information

-   Date of Loss
-   Time of Loss
-   Location
-   Description

### Involved Parties

-   Claimant
-   Third Parties
-   Contact Details

### Asset Details

-   Asset Type
-   Asset ID
-   Estimated Damage

### Additional Fields

-   Claim Type
-   Attachments
-   Initial Estimate

------------------------------------------------------------------------

## 3. ClaimValidator

The **ClaimValidator** checks whether mandatory fields are missing.

If required fields are missing, they are returned in a list used by the
routing engine.

Example:

``` json
"missingFields": [
  "policyNumber",
  "location"
]
```

------------------------------------------------------------------------

## 4. ClaimRouter

The **ClaimRouter** determines the correct processing workflow using
rule-based logic.

### Routing Rules

  Rule                                  Result
  ------------------------------------- ---------------------
  Description contains fraud keywords   Investigation Flag
  Missing mandatory fields              Manual Review
  Claim type = injury                   Specialist Queue
  Estimated damage \< 25000             Fast-track
  Otherwise                             Standard Processing

### Fraud Keywords Checked

-   fraud
-   inconsistent
-   staged

------------------------------------------------------------------------

## 5. Main Application

The **Main class** orchestrates the entire processing pipeline:

1.  Reads file or folder input
2.  Extracts claim fields
3.  Validates mandatory fields
4.  Applies routing rules
5.  Produces JSON output

------------------------------------------------------------------------

# Example Input

Example TXT FNOL document:

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

------------------------------------------------------------------------

# Example Output

``` json
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
```

------------------------------------------------------------------------

# Technology Stack

  Technology         Purpose
  ------------------ -------------------------------
  Java 21            Core application
  Maven              Build & dependency management
  Apache PDFBox      PDF text extraction
  Jackson Databind   JSON serialization

------------------------------------------------------------------------

# Setup and Installation

Follow the steps below to run the project locally.

## 1. Clone the Repository

Clone the project from GitHub:

``` bash
git clone https://github.com/your-username/autonomous-claims-agent.git
```

Navigate into the project directory:

``` bash
cd autonomous-claims-agent
```

------------------------------------------------------------------------

## 2. Build the Project

Clean previous builds:

``` bash
mvn clean
```

Compile the project:

``` bash
mvn compile
```

Install dependencies and package the application:

``` bash
mvn install
```

Alternatively run everything in one command:

``` bash
mvn clean install
```

------------------------------------------------------------------------

## 3. Run the Application

### Run with a Single File

``` bash
mvn exec:java --% -Dexec.args=file path
```

### Run with Folder Input

``` bash
mvn exec:java --% -Dexec.args=folder-path
```

### Example for single file

``` bash
mvn exec:java --% -Dexec.args=sample-claims/test.pdf
```

------------------------------------------------------------------------

# Prerequisites

Ensure the following tools are installed:

  Tool    Version
  ------- ---------
  Java    21+
  Maven   3.8+
  Git     Latest

Check installation:

``` bash
java -version
mvn -version
git --version
```

------------------------------------------------------------------------

# Design Considerations

### Hybrid Extraction Strategy

The extractor combines:

-   Regex-based extraction
-   Section-based parsing
-   Line-based fallback parsing

This ensures robust extraction from both:

-   Semi-structured PDF FNOL documents
-   Structured TXT claim reports

------------------------------------------------------------------------

# Future Improvements

Potential improvements include:

-   NLP-based entity extraction
-   Machine learning fraud detection
-   REST API for real-time claim processing
-   Database persistence layer
-   Integration with claim management systems
