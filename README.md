🥑 "Harvest-AI" Demo Grocery Delivery App

Built with 🤖 Google Antigravity & Vibe Coding (Gemini 3 Pro)

A full-stack, real-time online grocery delivery demonstration application, created in less than 4 hours using Vibe Coding—a new paradigm that leverages Google's agentic development assistant, Antigravity.

The goal of this project is to prove that complex, production-ready MVPs can be generated and deployed with minimal human intervention, focusing on high-level task delegation rather than line-by-line coding.

✨ The Vibe Coding Advantage

This application was not manually coded. It was built by orchestrating a fleet of specialized AI agents within the Antigravity IDE, achieving spectacular developer productivity and minimizing cognitive overhead.

The entire project lifecycle—from setting up the database schema to designing the responsive UI and integrating the backend API endpoints—was executed through a single, continuous Vibe Coding session.

Key Antigravity Highlights

Agentic Architecture: The Gemini 3 Pro model within Antigravity dynamically generated a complete Implementation Plan and dispatched parallel agents (Editor Agent, Terminal Agent, Browser Agent) to handle file creation, dependency installation, and UI validation simultaneously.

Weightless Development: Tasks like setting up CRUD operations for products and managing authentication boilerplate were completed autonomously, demonstrating the platform's ability to solve complex, repetitive tasks instantly.

Self-Correction: Agents automatically identified and resolved initial configuration errors and API inconsistencies, proving the sophisticated error-solving capabilities Antigravity adds to the developer workflow.

🛒 Application Features

The Harvest-AI app provides a clean, mobile-first experience for ordering groceries:

Real-Time Product Catalog: Browse a dynamic list of items with categories, prices, and stock indicators (data pulled from a Supabase instance).

Intuitive Cart Management: Easily add, remove, and update quantities of items in the persistent shopping cart.

User Authentication: Secure sign-up and sign-in flow.

Responsive UI/UX: A modern, fully responsive interface built with React and Tailwind CSS, automatically optimized for all device sizes by the Browser Agent.

Basic Checkout Flow: A simulated order placement system that clears the cart and logs the order in the database.

🛠️ Technical Stack (AI-Selected)

Component

Technology

Rationale (AI Decision)

Frontend

React + TypeScript

High-performance, declarative UI development.

Styling

Tailwind CSS

Utility-first approach for rapid, responsive styling.

Backend

Node.js (Express)

Lightweight, non-blocking I/O for API services.

Database

Supabase (PostgreSQL)

Managed, real-time database with built-in auth and endpoints.

🚀 Getting Started (Run Locally)

Since the Antigravity platform already managed the build process, getting the app running is quick:

Prerequisites

Node.js (v18+)

An active Supabase project (Project URL and Anon Key needed).

Installation

Clone Repository:

git clone [https://github.com/vibe-coded-demos/harvest-ai-grocery.git](https://github.com/vibe-coded-demos/harvest-ai-grocery.git)
cd harvest-ai-grocery


Environment Setup: Create a .env file in the root directory and paste your credentials:

REACT_APP_SUPABASE_URL="YOUR_SUPABASE_URL"
REACT_APP_SUPABASE_ANON_KEY="YOUR_SUPABASE_ANON_KEY"


Install Dependencies:

npm install


Run the App:

npm start


The application will now be running on http://localhost:3000.

💡 Next Steps: Vibe-Code Your Own Features

Want to extend this project? Open the project in Google Antigravity and give the AI agent a new mission.

Example Prompts for Expansion:

Mission: Add a "Favourites" list feature for logged-in users, complete with a database migration and a heart icon on the product cards.

Mission: Refactor the current Express REST API to use GraphQL for product queries.

Mission: Implement a dark mode toggle button on the navbar.

Happy Vibe Coding!
