# Computer vision (WS-05, WS-06, WS-09, WS-10, WS-24)

- `training/` — fine-tuning scripts (detector, currency classifier). Runs tracked in Weights & Biases.
- `export/` — export to LiteRT (TFLite) or ONNX, plus a check that the exported model matches `models/manifest.json` input/output.
- `eval/` — evaluation on frozen held-out sets: precision/recall per class, latency per device, reports committed as Markdown.

Data never goes in Git (see `datasets/README.md`). Licence decision for the detector: TD-001.
