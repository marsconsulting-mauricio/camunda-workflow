import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'  // ← NOVA LINHA

export default defineConfig({
  plugins: [react(), tailwindcss()],  // ← ADICIONE AQUI
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
