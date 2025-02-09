/** @type {import('tailwindcss').Config} */
export default {
    darkMode: ["class"],
  content: ["./index.html", "./src/**/*.{ts,tsx,js,jsx}"],
   theme: {
    extend: {
      colors: {
        primary: '#cdd3f6',
        secondary: '#c3e9cb',
        mainYellow: '#f8fdba',
      },
    },
  },
  plugins: [require("tailwindcss-animate")],
}

