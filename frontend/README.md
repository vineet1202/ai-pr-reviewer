# AI Code Reviewer Frontend

Minimal React + TypeScript + Vite interface for the Spring Boot AI Code Reviewer API.

## Run locally

1. Start the Spring Boot backend on port `8080`.
2. In this folder, install dependencies and start Vite:

```powershell
npm install
npm run dev
```

3. Open `http://localhost:5173` and select **Sign in with GitHub**. Complete the OAuth flow, then return to the frontend and submit a PR URL.

Vite proxies `/api`, `/oauth2`, and `/login` to the backend, which avoids a browser CORS configuration during local development.

## Build

```powershell
npm run build
```

For production, serve the generated `dist` files from the same origin as the backend or configure Spring Security CORS and the OAuth redirect/base URL for the deployed frontend domain.
