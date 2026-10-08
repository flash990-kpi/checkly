# Checkly - The "Google Lens" for Scams

Checkly is a free, incredibly easy-to-use application designed to help anyone—regardless of technical expertise—determine if a message, link, email, or page they received is a potential scam.

## Architecture
Checkly is built with a serverless, modular architecture to ensure immediate scalability, ease of maintenance, and zero friction for the user (no login required).

*   **Frontend:** Next.js (App Router), React, Tailwind CSS, Framer Motion (for smooth animations).
*   **Backend (MVP):** Next.js API Routes (Serverless Functions) to handle the analysis pipeline.
*   **Database (Future):** NoSQL Database (e.g., MongoDB or Firebase Firestore) for storing the threat database, known patterns, and anonymized analysis history.
*   **Core Engine (Future):**
    *   **Input Processor:** Handles image upload, link parsing, or text extraction.
    *   **OCR/Computer Vision:** Extracts text, URLs, and entities from images.
    *   **NLP/AI Module:** Understands the semantic meaning of the message (e.g., urgency, package delivery, payment request) instead of just relying on keywords.
    *   **Threat Database Lookup:** Checks extracted data against known scams, domains, and phishing campaigns.
    *   **Online Verification:** Automatically checks domain reputation, age, HTTPS certificates, and blacklists.
    *   **Risk Scorer:** Combines all signals into a final score (0-100).

## User Flow
1.  **Input:** The user opens the app and is greeted by a simple prompt: "Hai ricevuto qualcosa di sospetto?". They can:
    *   Upload a screenshot.
    *   Paste a link.
    *   Paste a text message.
2.  **Action:** The user presses the single large button: **"ANALIZZA"**.
3.  **Processing:** A loading screen appears, showing a step-by-step progression of the analysis (Extracting text, Checking links, Recognizing patterns, Verifying domains, Calculating risk).
4.  **Result:** The user is presented with a simple, color-coded result (Green: Low Risk, Orange: Suspicious, Red: Red Code), a clear explanation of *why* the result was given, what was checked, and a clear directive on *what to do next*.

## Necessary Screens
*   **Home Screen:** Minimalist design with the main call to action and input options.
*   **Loading/Analysis Screen:** Shows the real-time progression of the analysis steps.
*   **Result Screen:** The final evaluation, complete with risk score, reasoning, checks performed, and actionable advice.

## Database Structure (Threat DB)
A scalable NoSQL document structure is recommended:
*   `ScamCategories`: Name, description, associated keywords, typical patterns, brands frequently impersonated.
*   `KnownDomains`: Domain name, reputation score, first seen date, reports, associated scam categories.
*   `KnownPatterns`: Regular expressions or semantic embeddings of common scam phrases (e.g., "pacco fermo", "conto bloccato").
*   `AnalysisHistory` (Anonymized): Timestamp, input type, identified categories, final score, extracted domains (for continuous learning and DB updates).

## Risk Scoring System
The final score (0-100) is a weighted combination of multiple factors, avoiding single points of failure (e.g., a new domain isn't automatically a red code if the text is benign).
*   **0 - 30 (Basso rischio - Green):** No significant threat indicators.
*   **31 - 65 (Sospetta - Orange):** One or more anomalous elements. User should proceed with caution.
*   **66 - 100 (Codice Rosso - Red):** Strong indicators of phishing/scam, dangerous link, or high match with known scam patterns.
*   *Factors:* Semantic intent (urgency, data request), domain age/reputation, match with known scam categories (e.g., Fake Courier, Bank Phishing), presence of suspicious redirects.

## API/Services Needed (Production)
*   **OCR Service:** Google Cloud Vision API, AWS Textract, or an on-premise model.
*   **NLP/AI:** OpenAI API (GPT-4o/mini) or a fine-tuned open-source model (Llama 3, Mistral) for semantic analysis and categorization.
*   **Threat Intelligence/Domain Verification:** Google Safe Browsing API, VirusTotal API (used internally, completely hidden from the user), WHOIS/DNS lookup services.

## Privacy and Security
*   **Data Minimization:** Only process data necessary for the analysis.
*   **No Persistent Storage:** Uploaded images and personal texts are processed in memory and immediately discarded after analysis. They are NOT stored unless explicit user consent is given to "help improve the service".
*   **Anonymization:** Any data saved for improving the threat database must be stripped of PII (Personally Identifiable Information) like phone numbers, names, and personal emails.
*   **Secure Transport:** All communications are encrypted via HTTPS/TLS.

## Strategy for Updating the Threat Database
*   The database must be detached from the app bundle so it can be updated dynamically via a cloud backend.
*   Automated ingestion of public threat feeds (e.g., PhishTank, OpenPhish).
*   Automated analysis of user submissions: If many users submit a similar new pattern that gets flagged by the AI, it can be proposed for addition to the known threat DB.
*   Manual review and tuning by security analysts to ensure low false positives.
