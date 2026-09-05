# DriveU deployment

Frontend: Vercel (`angularapp`)\nBackend: Render Free (`springapp`)\nDatabase: Supabase Free PostgreSQL\nEmail: Brevo HTTPS API (avoids Render SMTP restrictions)\n
Render variables: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DB_DRIVER=org.postgresql.Driver`, `MAIL_PROVIDER=brevo`, `MAIL_API_KEY`, `MAIL_FROM_EMAIL`, `MAIL_FROM_NAME=DriveU`, `GEMINI_API_KEY`, optional `GEMINI_API_URL`.

Vercel root directory: `angularapp`; build command: `npm run build`. Replace the backend placeholder in `angularapp/src/environments/environment.prod.ts` after the Render URL is known.
