# Invoice Generator (React + Vite + TailwindCSS)

An invoice generator frontend built with React and Vite, styled with TailwindCSS. It lets you create invoices, list saved invoices, and download polished PDFs.

**Live Demo**: https://incandescent-profiterole-ecec33.netlify.app/

## Features
- Create invoices with customer, items, tax, and totals
- Persist and list saved invoices
- Download invoice as PDF
- Clean, responsive UI with TailwindCSS

## Tech Stack
- React 18 + Vite
- TailwindCSS

## Getting Started
1. Install dependencies:
   ```bash
   npm install
   ```
2. Run in development:
   ```bash
   npm run dev
   ```
3. Build for production:
   ```bash
   npm run build
   ```
4. Preview production build:
   ```bash
   npm run preview
   ```

## Project Structure
- `src/components/InvoiceForm.jsx`: Create invoices
- `src/components/InvoiceList.jsx`: List and manage invoices
- `src/Services/InvoiceServices.jsx`: Service utilities
- `src/App.jsx`: App shell

## Environment Variables
If you have a backend API, set `VITE_API_URL` in a `.env` file at the project root.
```env
VITE_API_URL=http://localhost:8080
```

## Deployment (Netlify)
This project is deployed on Netlify. Since it’s a SPA, add a redirect so all routes serve `index.html`:

- File: `public/_redirects`
  ```
  /* /index.html 200
  ```

When deploying via Netlify:
- Build command: `npm run build`
- Publish directory: `dist`

## License
MIT
