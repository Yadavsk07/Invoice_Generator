import React, { useEffect, useMemo, useState } from "react";
import { getInvoices, deleteInvoice, downloadPDF } from "../Services/InvoiceServices.jsx";

function InvoiceList() {
  const [invoices, setInvoices] = useState([]);
  const [query, setQuery] = useState("");

  useEffect(() => {
    loadInvoices();
  }, []);

  const loadInvoices = () => {
    getInvoices()
      .then((res) => {
        const data = res?.data;
        const list = Array.isArray(data)
          ? data
          : Array.isArray(data?.content)
          ? data.content
          : Array.isArray(data?.data)
          ? data.data
          : [];
        setInvoices(list);
      })
      .catch(() => setInvoices([]));
  };

  const handleDelete = (id) => {
    deleteInvoice(id).then(() => loadInvoices());
  };

  const handleDownload = (id) => {
    downloadPDF(id).then((res) => {
      const url = window.URL.createObjectURL(new Blob([res.data], { type: "application/pdf" }));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `invoice_${id}.pdf`);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    });
  };



  const renderClient = (inv) => inv?.customerName || inv?.client?.name || "-";
  const renderEmail = (inv) => inv?.customerEmail || inv?.client?.email || "-";
  const renderTotal = (inv) => {
    const total = inv?.totalAmount ?? inv?.meta?.grandTotal ?? 0;
    const currency = inv?.meta?.currency || "";
    return `${Number(total).toFixed(2)}${currency ? ` ${currency}` : ""}`;
  };

  const filtered = useMemo(() => {
    const list = Array.isArray(invoices) ? invoices : [];
    const q = query.trim().toLowerCase();
    if (!q) return list;
    return list.filter((inv) => {
      const values = [String(inv?.id ?? ""), renderClient(inv), renderEmail(inv)];
      return values.some((v) => (v || "").toLowerCase().includes(q));
    });
  }, [invoices, query]);

  return (
    <div className="w-full">
      <div className="mb-3 flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
        <h2 className="text-xl font-semibold text-slate-900">Invoice List</h2>
        <div className="flex items-center gap-2">
          <div className="relative">
            <input
              type="text"
              placeholder="Search by id, client, or email..."
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              className="w-64 rounded-md border border-slate-300 bg-white px-3 py-1.5 text-sm shadow-sm placeholder:text-slate-400 focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
            <span className="pointer-events-none absolute right-2 top-1/2 -translate-y-1/2 text-slate-400">🔎</span>
          </div>
          <button onClick={loadInvoices} className="btn-outline">↻ Refresh</button>
        </div>
      </div>
      <div className="overflow-x-auto rounded-lg border border-slate-200 bg-white shadow-sm">
        <table className="min-w-full">
          <thead className="bg-slate-50">
            <tr>
              <th className="px-4 py-2 text-left text-sm font-semibold text-slate-700 w-[8%]">ID</th>
              <th className="px-4 py-2 text-left text-sm font-semibold text-slate-700 w-[24%]">Client</th>
              <th className="px-4 py-2 text-left text-sm font-semibold text-slate-700 w-[28%]">Email</th>
              <th className="px-4 py-2 text-left text-sm font-semibold text-slate-700 w-[16%]">Total</th>
              <th className="px-4 py-2 text-left text-sm font-semibold text-slate-700 w-[24%]">Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((inv) => (
              <tr key={inv.id} className="odd:bg-white even:bg-slate-50 hover:bg-indigo-50/50 transition">
                <td className="px-4 py-2 text-sm text-slate-700">{inv.id}</td>
                <td className="px-4 py-2 text-sm text-slate-700">{renderClient(inv)}</td>
                <td className="px-4 py-2 text-sm text-slate-700">{renderEmail(inv)}</td>
                <td className="px-4 py-2 text-sm text-slate-700">{renderTotal(inv)}</td>
                <td className="px-4 py-2">
                  <div className="flex gap-2">
                    <button className="inline-flex items-center rounded-md bg-rose-600 px-3 py-1.5 text-sm font-semibold text-white shadow-sm hover:bg-rose-500" onClick={() => handleDelete(inv.id)}>🗑️ Delete</button>
                    <button className="inline-flex items-center rounded-md bg-emerald-600 px-3 py-1.5 text-sm font-semibold text-white shadow-sm hover:bg-emerald-500" onClick={() => handleDownload(inv.id)}>⬇️ Download PDF</button>
                  </div>
                </td>
              </tr>
            ))}
            {filtered.length === 0 && (
              <tr>
                <td colSpan={5} className="px-4 py-10 text-center text-sm text-slate-500">No invoices match your search.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default InvoiceList;
