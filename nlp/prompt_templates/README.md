# Prompt templates (WS-15)

One file per prompt, versioned: `describe.v1.md`, `ask.v1.md`, `screen_summarize.v1.md`, `intent.v1.md`, `ocr_fallback.v1.md`, `translate.v1.md`.

Each file has front matter (`name`, `version`, `max_tokens`) then a `## system` and a `## user` section with `{slots}`.

Rules: short answers (describe = 1–2 sentences), left/right relative wording, "I may be wrong" when unsure, no safety claims, intent returns JSON steps from the whitelist only. Changes are evaluated on the golden set before merging.
