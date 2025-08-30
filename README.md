# React + Vite

This project uses React with Vite and TailwindCSS for styling.

- Dev: `npm run dev`
- Build: `npm run build`
- Preview: `npm run preview`

## Styling: TailwindCSS
Tailwind is configured via `tailwind.config.js` and `postcss.config.js`.
Global styles are in `src/index.css` with Tailwind directives.

Content scanning paths: `index.html`, `src/**/*.{js,jsx,ts,tsx}`.

## API URL
Set `VITE_API_URL` in a `.env` at the project root to point to your backend (default `http://localhost:8080`).

```env
VITE_API_URL=http://localhost:8080
```

## Expanding the ESLint configuration

If you are developing a production application, we recommend using TypeScript with type-aware lint rules enabled. Check out the [TS template](https://github.com/vitejs/vite/tree/main/packages/create-vite/template-react-ts) for information on how to integrate TypeScript and [`typescript-eslint`](https://typescript-eslint.io) in your project.
