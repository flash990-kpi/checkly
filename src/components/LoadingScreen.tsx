"use client";

import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { CheckCircle2, Circle } from "lucide-react";

const steps = [
  "Lettura del contenuto...",
  "Comprensione del messaggio...",
  "Controllo dei link e domini...",
  "Confronto con le truffe note...",
  "Valutazione finale...",
];

export function LoadingScreen() {
  const [currentStep, setCurrentStep] = useState(0);

  useEffect(() => {
    // Simulate progression through steps
    const interval = setInterval(() => {
      setCurrentStep((prev) => (prev < steps.length - 1 ? prev + 1 : prev));
    }, 600); // 3 seconds total / 5 steps = 600ms per step

    return () => clearInterval(interval);
  }, []);

  return (
    <div className="flex flex-col items-center justify-center w-full max-w-md mx-auto py-12 px-4">
      {/* Animated Shield Icon */}
      <motion.div
        animate={{ scale: [0.9, 1.1, 1] }}
        transition={{ repeat: Infinity, duration: 2 }}
        className="relative mb-8"
      >
        <div className="w-24 h-24 rounded-full border-4 border-gray-800 flex items-center justify-center relative">
           <motion.div
             className="absolute inset-0 rounded-full border-4 border-t-white border-r-white border-b-transparent border-l-transparent"
             animate={{ rotate: 360 }}
             transition={{ repeat: Infinity, duration: 1.5, ease: "linear" }}
           />
           <svg
              viewBox="0 0 24 24"
              fill="none"
              stroke="white"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
              className="w-10 h-10"
            >
              <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
              <path d="m9 12 2 2 4-4" />
            </svg>
        </div>
      </motion.div>

      <h2 className="text-xl font-bold text-white mb-2 text-center">Analisi in corso...</h2>
      <p className="text-gray-400 text-sm mb-8 text-center">Stiamo controllando tutto per te. Un momento.</p>

      <div className="w-full space-y-4">
        {steps.map((step, index) => (
          <div key={index} className="flex items-center space-x-3">
            {index < currentStep ? (
              <CheckCircle2 className="w-5 h-5 text-green-500 flex-shrink-0" />
            ) : index === currentStep ? (
              <CheckCircle2 className="w-5 h-5 text-green-500 opacity-50 flex-shrink-0 animate-pulse" />
            ) : (
              <Circle className="w-5 h-5 text-gray-700 flex-shrink-0" />
            )}
            <span
              className={`text-sm font-medium ${
                index <= currentStep ? "text-gray-200" : "text-gray-600"
              }`}
            >
              {step}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}
