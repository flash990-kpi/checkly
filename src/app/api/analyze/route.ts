import { NextResponse } from "next/server";

export async function POST(request: Request) {
  try {
    await request.json();

    // Simulate processing time
    await new Promise((resolve) => setTimeout(resolve, 3000));

    // For the MVP, we always return a static "Red Code" result to demonstrate
    // the requested UI features (reasons, categories, action).
    const mockResponse = {
      status: "success",
      result: {
        riskLevel: "RED", // Can be "GREEN", "ORANGE", "RED"
        score: 85,
        title: "Probabile truffa del falso pacco",
        categories: ["Falso pacco", "Phishing"],
        description: "Abbiamo rilevato diversi segnali compatibili con una truffa.",
        reasons: [
          {
            title: "Senso di urgenza",
            description: "Il messaggio crea un senso di urgenza spingendoti ad agire rapidamente.",
          },
          {
            title: "Dominio sospetto",
            description: "Il link porta a un dominio che non corrisponde all'azienda nominata.",
          },
          {
            title: "Pattern conosciuto",
            description: "Sono presenti caratteristiche tipiche delle campagne di phishing.",
          },
        ],
        checks: [
          "Estrazione e lettura del testo",
          "Analisi del comportamento richiesto",
          "Verifica del dominio e del link",
          "Confronto con le truffe conosciute",
        ],
        action: "NON CLICCARE SUL LINK E NON INSERIRE DATI. Ignora o cancella il messaggio.",
      },
    };

    return NextResponse.json(mockResponse);
  } catch {
    return NextResponse.json(
      { status: "error", message: "Errore durante l'analisi." },
      { status: 500 }
    );
  }
}
