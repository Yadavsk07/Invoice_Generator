import axios from "axios";

const API_URL = `${import.meta.env.VITE_API_URL ?? 'http://localhost:8080'}/api/invoices`;

// Debug logging to verify environment variable
console.log('Environment VITE_API_URL:', import.meta.env.VITE_API_URL);
console.log('Final API URL:', API_URL);

export const getInvoices = () => axios.get(API_URL);
export const createInvoice = (invoice) => axios.post(API_URL, invoice, {
  headers: { Accept: 'text/plain, */*' },
  responseType: 'text',
  transformResponse: [(data) => data],
});
export const getInvoiceById = (id) => axios.get(`${API_URL}/${id}`);
export const deleteInvoice = (id) => axios.delete(`${API_URL}/${id}`);
export const downloadPDF = (id) => {
    return axios.get(`${API_URL}/${id}/pdf`, {
        responseType: 'blob'
    });
};
