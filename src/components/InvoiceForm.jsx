import React, { useMemo, useState } from "react";
import { createInvoice } from "../Services/InvoiceServices.jsx";

function Label({ children, htmlFor }) {
  return (
    <label htmlFor={htmlFor} className="block text-sm font-medium text-slate-700">
      {children}
    </label>
  );
}

function Input(props) {
  return (
    <input
      {...props}
      className={
        "mt-1 block w-full rounded-lg border border-slate-300 bg-white px-3 py-2 text-slate-900 placeholder:text-slate-400 shadow-sm transition focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 " +
        (props.className || "")
      }
    />
  );
}

function Select(props) {
  return (
    <select
      {...props}
      className={
        "mt-1 block w-full rounded-lg border border-slate-300 bg-white px-3 py-2 text-slate-900 shadow-sm transition focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 " +
        (props.className || "")
      }
    />
  );
}

function Textarea(props) {
  return (
    <textarea
      {...props}
      className={
        "mt-1 block w-full rounded-lg border border-slate-300 bg-white px-3 py-2 text-slate-900 placeholder:text-slate-400 shadow-sm transition focus:border-indigo-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 " +
        (props.className || "")
      }
    />
  );
}

function SectionTitle({ children }) {
  return (
    <div className="mt-6 mb-2 flex items-center gap-2">
      <span className="h-5 w-1.5 rounded-full bg-indigo-500"></span>
      <h3 className="text-base font-semibold text-slate-900">{children}</h3>
    </div>
  );
}

function InvoiceForm({ onSave }) {
  const [seller, setSeller] = useState({
    companyName: "",
    companyAddress: "",
    contactNumber: "",
    email: "",
    taxId: "",
    logoUrl: "",
  });

  const [client, setClient] = useState({
    name: "",
    address: "",
    contactNumber: "",
    email: "",
    taxId: "",
  });

  const [meta, setMeta] = useState({
    invoiceNumber: "",
    invoiceDate: "",
    dueDate: "",
    paymentTerms: "",
    currency: "USD",
    taxRatePercent: 0,
    discountPercent: 0,
    discountAmount: 0,
  });

  const [items, setItems] = useState([
    { description: "", quantity: 1, unitPrice: 0 },
  ]);

  const [submitting, setSubmitting] = useState(false);
  const [status, setStatus] = useState({ type: "", message: "" });

  const totals = useMemo(() => {
    const lineTotals = items.map((it) => (Number(it.quantity) || 0) * (Number(it.unitPrice) || 0));
    const subtotal = lineTotals.reduce((a, b) => a + b, 0);
    const tax = subtotal * ((Number(meta.taxRatePercent) || 0) / 100);
    const discountPct = subtotal * ((Number(meta.discountPercent) || 0) / 100);
    const discount = discountPct + (Number(meta.discountAmount) || 0);
    const grandTotal = Math.max(subtotal + tax - discount, 0);
    return { subtotal, tax, discount, grandTotal };
  }, [items, meta.taxRatePercent, meta.discountPercent, meta.discountAmount]);

  const handleSellerChange = (e) => setSeller((s) => ({ ...s, [e.target.name]: e.target.value }));
  const handleClientChange = (e) => setClient((c) => ({ ...c, [e.target.name]: e.target.value }));
  const handleMetaChange = (e) => setMeta((m) => ({ ...m, [e.target.name]: e.target.value }));

  const handleItemChange = (index, field, value) => {
    setItems((prev) => prev.map((it, i) => (i === index ? { ...it, [field]: value } : it)));
  };

  const addItem = () => setItems((prev) => [...prev, { description: "", quantity: 1, unitPrice: 0 }]);
  const removeItem = (index) => setItems((prev) => prev.filter((_, i) => i !== index));

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!client.name?.trim()) {
      setStatus({ type: "error", message: "Client name is required." });
      return;
    }

    setSubmitting(true);
    setStatus({ type: "", message: "" });

    const legacyFriendly = {
      customerName: client.name,
      customerEmail: client.email,
      date: meta.invoiceDate || new Date().toISOString().slice(0, 10),
      totalAmount: Number(totals.grandTotal.toFixed(2)),
      items: items.map((it) => ({
        description: it.description,
        quantity: Number(it.quantity) || 0,
        unitPrice: Number(it.unitPrice) || 0,
        lineTotal: (Number(it.quantity) || 0) * (Number(it.unitPrice) || 0),
      })),
    };

    const payload = {
      ...legacyFriendly,
      seller,
      client,
      meta: {
        ...meta,
        subtotal: totals.subtotal,
        tax: totals.tax,
        discount: totals.discount,
        grandTotal: totals.grandTotal,
      },
    };

    try {
      await createInvoice(payload);
      setStatus({ type: "success", message: "Invoice saved successfully." });
      onSave?.();
      setSeller({ companyName: "", companyAddress: "", contactNumber: "", email: "", taxId: "", logoUrl: "" });
      setClient({ name: "", address: "", contactNumber: "", email: "", taxId: "" });
      setMeta({ invoiceNumber: "", invoiceDate: "", dueDate: "", paymentTerms: "", currency: "USD", taxRatePercent: 0, discountPercent: 0, discountAmount: 0 });
      setItems([{ description: "", quantity: 1, unitPrice: 0 }]);
    } catch (err) {
      const msg = err?.response?.data?.message || err?.message || "Failed to save invoice.";
      setStatus({ type: "error", message: msg });
      // eslint-disable-next-line no-console
      console.error("Invoice save failed", err);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <div className="rounded-lg border border-slate-200 bg-slate-50 p-4">
            <SectionTitle>🧑‍💼 Business (Seller) Details</SectionTitle>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <Label htmlFor="companyName">Company name</Label>
                <Input id="companyName" name="companyName" value={seller.companyName} onChange={handleSellerChange} required />
              </div>
              <div>
                <Label htmlFor="email">Email address</Label>
                <Input id="email" type="email" name="email" value={seller.email} onChange={handleSellerChange} />
              </div>
              <div className="md:col-span-2">
                <Label htmlFor="companyAddress">Company address</Label>
                <Input id="companyAddress" name="companyAddress" value={seller.companyAddress} onChange={handleSellerChange} />
              </div>
              <div>
                <Label htmlFor="contactNumber">Contact number</Label>
                <Input id="contactNumber" name="contactNumber" value={seller.contactNumber} onChange={handleSellerChange} />
              </div>
              <div>
                <Label htmlFor="taxId">GST/VAT/Tax ID</Label>
                <Input id="taxId" name="taxId" value={seller.taxId} onChange={handleSellerChange} />
              </div>
              <div className="md:col-span-2">
                <Label htmlFor="logoUrl">Company logo URL (optional)</Label>
                <Input id="logoUrl" name="logoUrl" value={seller.logoUrl} onChange={handleSellerChange} />
                {seller.logoUrl ? (
                  <div className="mt-2 flex items-center gap-3">
                    <img src={seller.logoUrl} alt="Logo preview" className="h-10 w-10 rounded object-cover ring-1 ring-slate-200" onError={(e) => (e.currentTarget.style.display = 'none')} />
                    <span className="text-xs text-slate-500">Preview</span>
                  </div>
                ) : null}
              </div>
            </div>
          </div>

          <div className="rounded-lg border border-slate-200 bg-slate-50 p-4">
            <SectionTitle>👤 Client (Buyer) Details</SectionTitle>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <Label htmlFor="clientName">Client name</Label>
                <Input id="clientName" name="name" value={client.name} onChange={handleClientChange} required />
              </div>
              <div>
                <Label htmlFor="clientEmail">Email address</Label>
                <Input id="clientEmail" type="email" name="email" value={client.email} onChange={handleClientChange} />
              </div>
              <div className="md:col-span-2">
                <Label htmlFor="clientAddress">Client address</Label>
                <Input id="clientAddress" name="address" value={client.address} onChange={handleClientChange} />
              </div>
              <div>
                <Label htmlFor="clientContact">Contact number</Label>
                <Input id="clientContact" name="contactNumber" value={client.contactNumber} onChange={handleClientChange} />
              </div>
              <div>
                <Label htmlFor="clientTaxId">Client Tax ID</Label>
                <Input id="clientTaxId" name="taxId" value={client.taxId} onChange={handleClientChange} />
              </div>
            </div>
          </div>

          <div className="rounded-lg border border-slate-200 bg-slate-50 p-4">
            <SectionTitle>🧾 Invoice Metadata</SectionTitle>
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <div>
                <Label htmlFor="invoiceNumber">Invoice number</Label>
                <Input id="invoiceNumber" name="invoiceNumber" value={meta.invoiceNumber} onChange={handleMetaChange} />
              </div>
              <div>
                <Label htmlFor="invoiceDate">Invoice date</Label>
                <Input id="invoiceDate" type="date" name="invoiceDate" value={meta.invoiceDate} onChange={handleMetaChange} />
              </div>
              <div>
                <Label htmlFor="dueDate">Due date</Label>
                <Input id="dueDate" type="date" name="dueDate" value={meta.dueDate} onChange={handleMetaChange} />
              </div>
              <div>
                <Label htmlFor="paymentTerms">Payment terms</Label>
                <Input id="paymentTerms" name="paymentTerms" value={meta.paymentTerms} onChange={handleMetaChange} placeholder="e.g., Net 30 days" />
              </div>
              <div>
                <Label htmlFor="currency">Currency</Label>
                <Select id="currency" name="currency" value={meta.currency} onChange={handleMetaChange}>
                  <option>USD</option>
                  <option>EUR</option>
                  <option>GBP</option>
                  <option>INR</option>
                  <option>AUD</option>
                  <option>CAD</option>
                </Select>
              </div>
              <div>
                <Label htmlFor="taxRatePercent">Tax rate (%)</Label>
                <Input id="taxRatePercent" type="number" step="0.01" name="taxRatePercent" value={meta.taxRatePercent} onChange={handleMetaChange} />
              </div>
              <div>
                <Label htmlFor="discountPercent">Discount (%)</Label>
                <Input id="discountPercent" type="number" step="0.01" name="discountPercent" value={meta.discountPercent} onChange={handleMetaChange} />
              </div>
              <div>
                <Label htmlFor="discountAmount">Discount (flat)</Label>
                <Input id="discountAmount" type="number" step="0.01" name="discountAmount" value={meta.discountAmount} onChange={handleMetaChange} />
              </div>
            </div>
          </div>

          <div className="rounded-lg border border-slate-200 bg-slate-50 p-4">
            <SectionTitle>🧮 Line Items</SectionTitle>
            <div className="overflow-x-auto rounded-lg border border-slate-200 bg-white shadow-sm">
              <table className="min-w-full">
                <thead className="bg-slate-50">
                  <tr>
                    <th className="px-4 py-2 text-left text-sm font-semibold text-slate-700 w-[45%]">Description</th>
                    <th className="px-4 py-2 text-left text-sm font-semibold text-slate-700 w-[15%]">Quantity</th>
                    <th className="px-4 py-2 text-left text-sm font-semibold text-slate-700 w-[20%]">Unit price</th>
                    <th className="px-4 py-2 text-left text-sm font-semibold text-slate-700 w-[20%]">Line total</th>
                    <th className="px-4 py-2"></th>
                  </tr>
                </thead>
                <tbody>
                  {items.map((it, idx) => (
                    <tr key={idx} className="odd:bg-white even:bg-slate-50 hover:bg-indigo-50/50 transition">
                      <td className="px-4 py-2">
                        <Input value={it.description} onChange={(e) => handleItemChange(idx, 'description', e.target.value)} placeholder="Item description" />
                      </td>
                      <td className="px-4 py-2">
                        <Input type="number" min="0" step="1" value={it.quantity} onChange={(e) => handleItemChange(idx, 'quantity', e.target.value)} />
                      </td>
                      <td className="px-4 py-2">
                        <Input type="number" min="0" step="0.01" value={it.unitPrice} onChange={(e) => handleItemChange(idx, 'unitPrice', e.target.value)} />
                      </td>
                      <td className="px-4 py-2 text-sm text-slate-700">
                        {(((Number(it.quantity) || 0) * (Number(it.unitPrice) || 0)).toFixed(2))}
                      </td>
                      <td className="px-4 py-2 text-right">
                        <button type="button" className="btn-outline" onClick={() => removeItem(idx)} disabled={items.length === 1}>❌ Remove</button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <div className="mt-3">
              <button type="button" className="btn-primary" onClick={addItem}>➕ Add item</button>
            </div>
          </div>
        </div>

        <aside className="lg:col-span-1">
          <div className="sticky top-4 space-y-6">
            {status.type === "success" && (
              <div className="rounded-lg border border-emerald-200 bg-emerald-50 px-3 py-2 text-sm text-emerald-800">{status.message}</div>
            )}
            {status.type === "error" && (
              <div className="rounded-lg border border-rose-200 bg-rose-50 px-3 py-2 text-sm text-rose-800">{status.message}</div>
            )}

            <div className="rounded-xl border border-indigo-200 bg-indigo-50">
              <div className="p-4 space-y-2">
                <div className="flex items-center justify-between text-sm text-slate-700"><span>Subtotal</span><strong>{totals.subtotal.toFixed(2)} {meta.currency}</strong></div>
                <div className="flex items-center justify-between text-sm text-slate-700"><span>Tax</span><strong>{totals.tax.toFixed(2)} {meta.currency}</strong></div>
                <div className="flex items-center justify-between text-sm text-slate-700"><span>Discount</span><strong>-{totals.discount.toFixed(2)} {meta.currency}</strong></div>
                <div className="border-t border-indigo-200" />
                <div className="flex items-center justify-between text-2xl font-bold text-slate-900"><span>Grand Total</span><span>{totals.grandTotal.toFixed(2)} {meta.currency}</span></div>
              </div>
            </div>



            <button type="submit" disabled={submitting} className="btn-primary w-full justify-center disabled:opacity-60">
              {submitting ? "Saving..." : "💾 Save Invoice"}
            </button>
          </div>
        </aside>
      </div>
    </form>
  );
}

export default InvoiceForm;
