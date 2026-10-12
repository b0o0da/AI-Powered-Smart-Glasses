# Models

Weights are never committed. `manifest.json` lists every model the app or the evaluation uses; `scripts/fetch_models.py` downloads them here and checks their SHA-256.

Manifest entry:

```json
{
  "name": "detector",
  "version": "coco-baseline-0.1",
  "file": "detector.tflite",
  "url": "https://.../detector.tflite",
  "sha256": "<64 hex chars>",
  "license": "AGPL-3.0 | Apache-2.0 | ...",
  "source": "where it came from and how it was trained",
  "input": "e.g. 320x320x3 float32, letterboxed",
  "output": "e.g. [1, N, 6] boxes xyxy + score + class",
  "labels": "detector_labels.txt"
}
```

The Android app reads the file name, input and output description from the manifest so the detector can be swapped (TD-001) without code changes. Copy the files the app needs into `android/app/src/main/assets/` with `python scripts/fetch_models.py --android`.
