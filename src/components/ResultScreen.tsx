import { ShieldAlert, ShieldCheck, Shield, CheckCircle2, Search, X } from "lucide-react";

type RiskLevel = "GREEN" | "ORANGE" | "RED";

interface Reason {
  title: string;
  description: string;
}

interface ResultData {
  riskLevel: RiskLevel;
  score: number;
  title: string;
  categories: string[];
  description: string;
  reasons: Reason[];
  checks: string[];
  action: string;
}

interface ResultScreenProps {
  result: ResultData;
  onReset: () => void;
}

export function ResultScreen({ result, onReset }: ResultScreenProps) {
  const getRiskStyles = (level: RiskLevel) => {
    switch (level) {
      case "GREEN":
        return {
          bg: "bg-green-500/10",
          border: "border-green-500/20",
          text: "text-green-500",
          headerBg: "bg-green-500",
          headerText: "text-black",
          icon: <ShieldCheck className="w-12 h-12 mb-4 mx-auto" />,
          label: "Basso rischio",
        };
      case "ORANGE":
        return {
          bg: "bg-orange-500/10",
          border: "border-orange-500/20",
          text: "text-orange-500",
          headerBg: "bg-orange-950 border border-orange-500/30",
          headerText: "text-orange-500",
          icon: <Shield className="w-8 h-8 mb-2 mx-auto text-orange-500" />,
          label: "Sospetta",
        };
      case "RED":
        return {
          bg: "bg-red-500/10",
          border: "border-red-500/20",
          text: "text-red-500",
          headerBg: "bg-red-950 border border-red-500/30",
          headerText: "text-red-500",
          icon: <ShieldAlert className="w-8 h-8 mb-2 mx-auto text-red-500" />,
          label: "Codice Rosso",
        };
    }
  };

  const styles = getRiskStyles(result.riskLevel);

  return (
    <div className="w-full max-w-2xl mx-auto pb-12 animate-in fade-in slide-in-from-bottom-4 duration-500 relative">
      <button
        onClick={onReset}
        className="absolute -top-12 right-0 p-2 text-gray-400 hover:text-white bg-gray-900 rounded-full"
      >
        <X className="w-5 h-5" />
      </button>

      {/* Main Card */}
      <div className={`rounded-3xl p-8 text-center mb-6 shadow-[0_0_40px_-10px_rgba(0,0,0,0.5)] ${styles.headerBg} ${styles.border}`}>
        {styles.icon}
        <h1 className={`text-2xl font-bold mb-1 ${result.riskLevel === 'GREEN' ? styles.headerText : 'text-white'}`}>
          {styles.label}
        </h1>
        <p className={`text-sm mb-4 font-medium ${result.riskLevel === 'GREEN' ? styles.headerText : 'text-gray-300'}`}>
          {result.title}
        </p>
        <div className="inline-block bg-black/40 rounded-full px-4 py-1.5 text-xs font-semibold text-gray-300 backdrop-blur-sm">
          Livello di rischio: {result.score}/100
        </div>
      </div>

      <p className="text-gray-300 text-[15px] leading-relaxed mb-6 px-2">
        {result.description}
      </p>

      {result.categories.length > 0 && (
        <div className="mb-8 px-2">
          <h3 className="text-sm font-bold text-white mb-3">Categorie rilevate</h3>
          <div className="flex flex-wrap gap-2">
            {result.categories.map((cat, idx) => (
              <span key={idx} className="bg-gray-800/80 border border-gray-700 text-gray-300 text-xs px-3 py-1.5 rounded-full font-medium">
                {cat}
              </span>
            ))}
          </div>
        </div>
      )}

      {/* Action Block */}
      <div className="mb-8">
         <h3 className="text-sm font-bold text-white mb-3 flex items-center px-2">
          <ShieldAlert className="w-4 h-4 mr-2 text-white" />
          Cosa fare?
        </h3>
        <div className="bg-white text-black p-5 rounded-2xl font-bold text-[15px]">
          {result.action}
        </div>
      </div>

      {/* Reasons Section */}
      <div className="mb-8 px-2">
        <h3 className="text-sm font-bold text-white mb-4 flex items-center">
          <Search className="w-4 h-4 mr-2 text-blue-400" />
          Perché Checkly lo pensa?
        </h3>
        <div className="space-y-3">
          {result.reasons.map((reason, idx) => (
            <div key={idx} className="bg-gray-900/50 border border-gray-800/50 p-4 rounded-2xl">
              <div className="flex items-start">
                <div className={`w-2 h-2 rounded-full mt-1.5 mr-3 flex-shrink-0 ${styles.text.replace('text-', 'bg-')}`} />
                <div>
                  <h4 className="text-sm font-bold text-gray-200 mb-1">{reason.title}</h4>
                  <p className="text-xs text-gray-400 leading-relaxed">{reason.description}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Checks Section */}
      <div className="px-2">
        <h3 className="text-sm font-bold text-white mb-4 flex items-center">
          <CheckCircle2 className="w-4 h-4 mr-2 text-gray-400" />
          Cosa abbiamo controllato
        </h3>
        <div className="space-y-3">
           {result.checks.map((check, idx) => (
            <div key={idx} className="flex items-center text-xs text-gray-400">
              <CheckCircle2 className="w-4 h-4 mr-2 text-green-500/70" />
              {check}
            </div>
           ))}
        </div>
      </div>

      <div className="mt-12 flex justify-center">
         <button
            onClick={onReset}
            className="bg-white text-black hover:bg-gray-200 transition-colors px-6 py-3 rounded-full font-bold text-sm flex items-center"
         >
           Analizza un altro
         </button>
      </div>
    </div>
  );
}
