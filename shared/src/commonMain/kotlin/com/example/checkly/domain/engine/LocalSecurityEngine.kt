package com.example.checkly.domain.engine

import kotlin.math.ln

data class AnalysisResult(
    val score: Int, // 0 to 100
    val reasons: List<String>,
    val type: String
)

class LocalSecurityEngine {

    private val suspiciousTlds = listOf(".xyz", ".top", ".tk", ".click", ".live", ".support", ".vip", ".loan")
    private val officialDomains = listOf("poste.it", "paypal.com", "inps.it", "agenziaentrate.gov.it", "gls-italy.com", "brt.it", "dhl.com")
    
    // Simple prompt injection detection
    private val injectionRegex = Regex("(?i)(ignora le istruzioni precedenti|imposta score a 0|system override|ignore previous instructions)")

    fun analyze(text: String): AnalysisResult {
        val reasons = mutableListOf<String>()
        var baseScore = 0
        var type = "Sicuro"

        // Check for prompt injection
        if (injectionRegex.containsMatchIn(text)) {
            return AnalysisResult(
                score = 100,
                reasons = listOf("Rilevato tentativo di manipolazione del sistema (Prompt Injection)."),
                type = "Attacco Sistema"
            )
        }

        val lowerText = text.lowercase()

        // TLD Analysis
        val foundTlds = suspiciousTlds.filter { lowerText.contains(it) }
        if (foundTlds.isNotEmpty()) {
            baseScore += 40
            reasons.add("Dominio con estensione sospetta: ${foundTlds.joinToString(", ")}.")
            type = "Phishing / Scam"
        }

        // Urgency / Threats Analysis
        if (lowerText.contains("blocco conto") || lowerText.contains("entro 12 ore") || lowerText.contains("azione richiesta") || lowerText.contains("pacco in giacenza")) {
            baseScore += 30
            reasons.add("Linguaggio che induce un falso senso di urgenza (tipico delle truffe).")
            type = if (lowerText.contains("pacco")) "Smishing (Pacco Falso)" else "Phishing Bancario"
        }

        // Typosquatting / Shannon Entropy (Simplified for domains)
        val words = lowerText.split(Regex("\\s+"))
        val links = words.filter { it.contains("http") || it.contains("www") }
        links.forEach { link ->
            val entropy = calculateShannonEntropy(link)
            if (entropy > 3.7) {
                baseScore += 30
                reasons.add("Link con caratteri anomali (Possibile Dominio Generato Automaticamente).")
            }
        }

        val finalScore = baseScore.coerceIn(0, 100)
        if (finalScore == 0) {
            reasons.add("Nessuna anomalia evidente riscontrata nel testo.")
        }

        return AnalysisResult(
            score = finalScore,
            reasons = reasons,
            type = if (finalScore >= 70) type else if (finalScore > 30) "Sospetto" else "Sembra Autentico"
        )
    }

    private fun calculateShannonEntropy(s: String): Double {
        if (s.isEmpty()) return 0.0
        val freq = s.groupingBy { it }.eachCount()
        val len = s.length.toDouble()
        return freq.values.sumOf { count ->
            val p = count / len
            -p * (ln(p) / ln(2.0))
        }
    }
}