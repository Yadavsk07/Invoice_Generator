import React, { useState } from "react";
import InvoiceForm from "./components/InvoiceForm";
import InvoiceList from "./components/InvoiceList";

function App() {
  const [refresh, setRefresh] = useState(false);

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100">
      <div className="max-w-6xl mx-auto p-4 space-y-4">
        <div className="rounded-xl bg-gradient-to-r from-indigo-600 to-purple-600 text-white shadow-lg">
          <div className="p-6">
            <div className="flex items-center gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-white/20">🧾</div>
              <div>
                <h1 className="text-2xl font-bold tracking-tight">Invoice Generator</h1>
                <p className="text-indigo-100">Create and manage invoices, then download polished PDFs.</p>
              </div>
            </div>
          </div>
        </div>

        <div className="card">
          <div className="card-body">
            <InvoiceForm onSave={() => setRefresh(!refresh)} />
          </div>
        </div>
        <div className="card">
          <div className="card-body">
            <InvoiceList key={refresh} />
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
