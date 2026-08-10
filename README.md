# Apitap Android

## About this app

Apitap is an all-in-one social commerce platform that combines marketing, social media, and sales into one solution. It helps businesses promote products, engage customers, and increase revenue through tools like mobile marketing, in-store advertising, messaging, and direct sales.

Designed for modern merchants, Apitap simplifies operations and provides everything needed to grow and succeed in today's digital marketplace.

---

# AI Assistant Integration

## Overview

The Apitap Android app integrates an AI Assistant through a secure WebView.

The AI Assistant is hosted and managed by the Apitap AI web system. The Android application is responsible primarily for:

- Launching the AI WebView
- Providing the current screen/context
- Passing relevant entity IDs when applicable
- Handling authentication using a short-lived single-use token
- Handling required Android permissions
- Restricting WebView navigation to the approved AI domain
- Providing a floating AI button throughout the application

The AI website is responsible for:

- AI interface and UI
- AI responses
- Business logic
- AI capabilities
- Navigation inside the AI experience
- Product, merchant, order and other AI-related workflows

Android should not duplicate the AI business logic or build native screens for the AI experience.

---

# AI Entry Point

The current integration uses a **floating AI button**.

The floating button should be available throughout the relevant Apitap screens.

When the user taps the button:

```text
Apitap Screen
      ↓
Floating AI Button
      ↓
AI WebView
      ↓
https://ai.atapai.com/embed
