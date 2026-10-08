"use client";

import { useState } from "react";
import { ImagePlus, Link as LinkIcon, MessageSquare } from "lucide-react";
import { LoadingScreen } from "@/components/LoadingScreen";
import { ResultScreen } from "@/components/ResultScreen";

type AppState = "IDLE" | "ANALYZING" | "RESULT";

// Match the ResultData interface from ResultScreen
type RiskLevel = "GREEN" | "ORANGE" | "RED";
interface ResultData {
  riskLevel: RiskLevel;
  score: number;
  title: string;
  categories: string[];
  description: string;
  reasons: { title: string; description: string }[];
  checks: string[];
  action: string;
}

export default function Home() {
  const [appState, setAppState] = useState<AppState>("IDLE");
  const [result, setResult] = useState<ResultData | null>(null);

  const handleAnalyze = async () => {
    setAppState("ANALYZING");

    try {
      const response = await fetch("/api/analyze", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ input: "dummy data", type: "text" }),
      });

      const data = await response.json();
      if (data.status === "success") {
        setResult(data.result);
        setAppState("RESULT");
      } else {
        // Handle error (fallback to IDLE for simplicity here)
        setAppState("IDLE");
        alert("Errore durante l'analisi.");
      }
    } catch (error) {
      console.error(error);
      setAppState("IDLE");
      alert("Errore di rete.");
    }
  };

  const handleReset = () => {
    setAppState("IDLE");
    setResult(null);
  };

  return (
    <main className="min-h-screen p-6 md:p-12 flex flex-col items-center justify-center relative overflow-hidden bg-[#050505]">

      {/* Background glow effect */}
      <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-green-500/5 rounded-full blur-[120px] pointer-events-none" />

      {appState === "IDLE" && (
        <div className="w-full max-w-md mx-auto text-center z-10 animate-in fade-in zoom-in-95 duration-300">
          <div className="mb-8">
            <h1 className="text-3xl md:text-4xl font-bold tracking-tight text-white mb-4">
              Hai ricevuto qualcosa di sospetto?
            </h1>
            <p className="text-gray-400 text-sm md:text-base">
              Caricalo, premi Analizza e ti diciamo se è una possibile truffa.
            </p>
          </div>

          <div className="space-y-4 mb-8">
            <button className="w-full flex items-center p-4 bg-gray-900/50 hover:bg-gray-800/80 border border-gray-800 rounded-2xl transition-all group">
              <div className="w-10 h-10 rounded-full bg-blue-500/10 flex items-center justify-center mr-4 group-hover:scale-110 transition-transform">
                <ImagePlus className="w-5 h-5 text-blue-400" />
              </div>
              <div className="text-left">
                <div className="font-semibold text-gray-200">Carica uno screenshot</div>
              </div>
            </button>

            <button className="w-full flex items-center p-4 bg-gray-900/50 hover:bg-gray-800/80 border border-gray-800 rounded-2xl transition-all group">
              <div className="w-10 h-10 rounded-full bg-purple-500/10 flex items-center justify-center mr-4 group-hover:scale-110 transition-transform">
                <LinkIcon className="w-5 h-5 text-purple-400" />
              </div>
              <div className="text-left">
                <div className="font-semibold text-gray-200">Incolla un link</div>
              </div>
            </button>

            <button className="w-full flex items-center p-4 bg-gray-900/50 hover:bg-gray-800/80 border border-gray-800 rounded-2xl transition-all group">
               <div className="w-10 h-10 rounded-full bg-green-500/10 flex items-center justify-center mr-4 group-hover:scale-110 transition-transform">
                <MessageSquare className="w-5 h-5 text-green-400" />
              </div>
              <div className="text-left">
                <div className="font-semibold text-gray-200">Incolla un messaggio</div>
              </div>
            </button>
          </div>

          <button
            onClick={handleAnalyze}
            className="w-full bg-white text-black py-4 rounded-2xl font-bold text-lg hover:bg-gray-200 transition-colors shadow-lg shadow-white/10"
          >
            ANALIZZA
          </button>
        </div>
      )}

      {appState === "ANALYZING" && (
        <div className="w-full max-w-md mx-auto z-10 animate-in fade-in duration-300">
           <LoadingScreen />
        </div>
      )}

      {appState === "RESULT" && result && (
        <div className="w-full z-10">
          <ResultScreen result={result} onReset={handleReset} />
        </div>
      )}
    </main>
  );
}
