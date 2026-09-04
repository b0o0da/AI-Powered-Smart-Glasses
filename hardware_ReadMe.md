# 🛠️ AI-Powered Smart Glasses — Hardware BOM & Shopping List (Egypt)

> **On pricing:** I could not pull live, itemized EGP prices from specific Egyptian store pages (RAM Electronics, Dev Boards Market, Amazon.eg, Noon) through search — their current catalog prices aren't reliably indexed. What's below is built from **current global USD pricing** (checked live — note the Raspberry Pi RAM shortage pushing 2026 prices up significantly) **converted at today's rate of ≈ 51 EGP/USD**, then adjusted up ~15–30% to account for typical Egypt import markup on electronics. Treat every price as an **estimate range**, and confirm the actual number on the store's site or by calling/WhatsApp before buying — component prices in Egypt swing with USD/EGP volatility and import costs. Where I flag "estimate," that's not a verified live price.

---

## Important 2026 context: Raspberry Pi prices are unusually high right now

A global DRAM/memory shortage has pushed official Raspberry Pi 5 prices up 70–90% between late 2025 and mid-2026 (8GB model rose from ~$80 to ~$95–125 MSRP; 16GB from $120 to ~$145–205). This materially changes the MVP hardware calculus — the Pi 5 is no longer the "cheap" option it used to be. **This is a strong argument for either the Raspberry Pi 4 (unaffected by this pricing wave) or a smartphone-based MVP for a budget-conscious student build.**

---

## 1. Main Computing Unit

| Option | Approx. global price (USD) | Est. EGP (with import markup) | Notes |
|---|---|---|---|
| Raspberry Pi Zero 2 W | ~$18 | ~1,400–1,900 EGP (estimate) | Too weak for real-time YOLO+OCR; only viable if you offload inference to a phone/cloud |
| Raspberry Pi 4 (4GB) | ~$55 (unaffected by the RAM shortage) | ~3,500–4,800 EGP (estimate) | **Recommended MVP compute** — mature ecosystem, cheaper than Pi 5 right now, sufficient for YOLOv8n at reduced resolution |
| Raspberry Pi 5 (8GB) | ~$95–125 (elevated due to 2026 memory shortage) | ~7,500–10,500 EGP (estimate) | Meaningfully faster, but currently a poor value vs. Pi 4 given the price spike |
| NVIDIA Jetson Orin Nano (8GB dev kit) | ~$249 | ~19,000–24,000 EGP (estimate) | Real hardware-accelerated inference; best for the Advanced config, overkill/too pricey for MVP |
| Smartphone-based (repurpose an existing mid-range Android phone) | $0 if reused | 0 EGP if you already own one | **Best cost/performance option for a budget MVP** — modern phone CPUs/NPUs comfortably run YOLOv8n + OCR, and you already have camera, mic, speaker, battery, and connectivity built in |

**Recommendation for MVP:** If a team member already owns a spare Android phone, **use it as the compute unit** — it eliminates ~half your BOM (camera, mic, speaker, battery, Wi-Fi/Bluetooth all included) and sidesteps the current Pi 5 price spike entirely. If you need a dedicated headless device, go with the **Raspberry Pi 4 (4GB)**, not the Pi 5, given current pricing — the Pi 5's speed advantage isn't worth ~2x the cost for a demo-grade MVP.

---

## 2. Camera

| Option | Resolution/FPS | FoV | Autofocus | Low-light | Notes | Est. EGP |
|---|---|---|---|---|---|---|
| Raspberry Pi Camera Module 3 | 12MP, up to 1080p60 | Standard/Wide variant available | Yes | Good | Best official option if using a Pi | ~1,800–2,800 EGP (estimate) |
| Raspberry Pi Camera Module (v2, older) | 8MP, 1080p30 | Standard | No | Fair | Cheaper, still works fine for detection | ~900–1,500 EGP (estimate) |
| Generic USB webcam (e.g. Logitech C270-class) | 720p–1080p | Standard | Varies | Fair | Easiest to wire, works on Pi or laptop | ~700–1,800 EGP (estimate) |
| Small wide-angle USB "spy/board" camera module | 1080p | Wide (~120°) | Usually fixed-focus | Fair | Best physical form factor for mounting on a glasses frame (small + light) | ~600–1,400 EGP (estimate) |
| Phone's built-in camera | Excellent | Excellent | Yes | Excellent | Free if using the phone-based MVP option | 0 EGP |

**Recommendation:** For a Pi-based build, the small wide-angle USB board camera is the best fit for physically mounting on glasses (light, flat, flexible cable). For the phone-based MVP, just use the phone's own camera.

---

## 3. Audio Input (Microphone)

| Option | Notes | Est. EGP |
|---|---|---|
| USB microphone (mini lavalier/clip style) | Simplest to wire to a Pi, decent quality | ~400–900 EGP (estimate) |
| I2S MEMS microphone (e.g. INMP441 breakout) | Best noise rejection, needs GPIO wiring + a bit more setup | ~150–350 EGP (estimate) |
| Bluetooth microphone/headset | Adds Bluetooth pairing complexity, more failure points for a demo | ~500–1,500 EGP (estimate) |
| Phone's built-in mic | Free, good quality, if using phone-based MVP | 0 EGP |

**Recommendation:** USB microphone for simplicity on the Pi builds — it "just works" without driver hassle, which matters when you have a demo deadline. Upgrade to the I2S MEMS mic in the "Better Prototype" tier once the basics are proven.

---

## 4. Audio Output (Speaker)

| Option | Notes | Est. EGP |
|---|---|---|
| Small 3W speaker + amp module (e.g. MAX98357 I2S amp + speaker) | Cheap, simple, but blocks/covers an ear if mounted near it | ~250–600 EGP (estimate) |
| Bone-conduction speaker module | **Best fit for a wearable** — keeps ears open, safer for walking around, feels more like real smart-glasses | ~800–2,000 EGP (estimate, often needs import) |
| Bluetooth open-ear earbuds (e.g. sport/open-ear style) | Good middle ground — wireless, keeps ear canal open | ~1,200–3,500 EGP (estimate) |
| Wired earbuds (reuse existing) | Free if you already own a pair, simplest to demo reliably | 0 EGP |

**Recommendation:** For the *demo-safe* choice, use a small wired speaker or earbuds (fewest points of failure — no Bluetooth pairing to go wrong mid-demo). For the "feels like real smart glasses" experience, budget for a bone-conduction module in the Recommended/Advanced configuration.

---

## 5. Battery & Power

- **Battery type:** 3.7V Li-Po pouch cell (single-cell) for a compact wearable build, OR a standard USB power bank for the absolute-simplest MVP.
- **Capacity:** 2000–3000 mAh Li-Po gives a reasonable balance of runtime vs. weight for a head-worn or pocket-worn pack.
- **Runtime estimate:** A Raspberry Pi 4 draws roughly 3–5W under load (camera + inference active). A 3000mAh Li-Po at 3.7V ≈ 11.1 Wh → **roughly 2–3 hours of active runtime** through a boost converter (accounting for conversion losses), or **4–6+ hours in the phone-based MVP** since modern phone batteries are 15–20 Wh+.
- **Voltage regulator:** required if using a raw Li-Po (3.7V) to power a Pi (needs stable 5V) — use a dedicated Li-Po boost/UPS HAT (e.g. a "Pi UPS" module) rather than a bare boost converter, for safety.
- **Charging module:** TP4056-style Li-Po charging board with protection circuit (over-charge/over-discharge/short-circuit protection) is non-negotiable — do not run a bare Li-Po without one.
- **USB-C charging:** recommended for convenience; most charging modules now come in USB-C variants.
- **Safety:** Li-Po cells can be a fire risk if punctured, overcharged, or short-circuited — always use a protection circuit, never leave charging unattended overnight, and store/transport in a fire-safe pouch if possible for a demo day.
- **Power bank alternative (simplest, safest for MVP):** a standard 10,000mAh USB-C power bank (~$8–15 / ~500–900 EGP estimate) skips all Li-Po safety concerns entirely and gives ~4–6 hours of Pi runtime — **this is the recommended MVP choice** given you're a student team on a deadline, not a hardware-safety lab.

| Item | Est. EGP |
|---|---|
| 10,000mAh USB-C power bank (MVP recommendation) | ~500–900 EGP (estimate) |
| 3.7V 2000–3000mAh Li-Po cell | ~250–500 EGP (estimate) |
| TP4056 charging/protection module | ~50–120 EGP (estimate) |
| Li-Po UPS/boost HAT for Pi | ~600–1,200 EGP (estimate) |

---

## 6. Connectivity

- **Wi-Fi & Bluetooth:** already built into Raspberry Pi 4/5 and any smartphone — **no extra purchase needed.**
- **USB:** built-in on all options.
- **GPS:** 🔴 not needed for MVP (no navigation feature planned) — skip.
- **Mobile hotspot:** use your phone's hotspot for internet access during demos if Wi-Fi isn't available at the venue — no hardware purchase, just a plan consideration.

---

## 7. Sensors — Only Add What Earns Its Place

| Sensor | Needed? | Reasoning |
|---|---|---|
| IMU / Accelerometer / Gyroscope | 🔴 Skip for MVP | Only useful for head-orientation-aware features you're not building yet |
| GPS | 🔴 Skip | No navigation feature in MVP |
| Proximity sensor | 🔴 Skip | Not part of any MVP use case |
| Ambient light sensor | 🔴 Skip | Camera exposure auto-adjusts; not worth the added complexity |
| Distance/depth sensor | 🔴 Skip for MVP | Depth estimation is an explicitly optional/post-MVP feature — a discrete distance sensor (e.g. VL53L0X, ~150–300 EGP estimate) is a legitimate cheap upgrade *if* you pursue "how far is it" as a stretch goal, but don't buy it up front |

**Bottom line:** buy zero extra sensors for the MVP. If the team later commits to a specific 🟡/🔴 stretch feature that needs one, buy it then — not speculatively.

---

## 8. Physical Glasses & Mounting

| Item | Notes | Est. EGP |
|---|---|---|
| Cheap plastic-frame glasses (no prescription, or thrift/dollar-store style) | Use as the mounting base — don't buy expensive frames you'll be drilling/gluing into | ~50–200 EGP (estimate), or reuse an old pair for free |
| 3D-printed camera/electronics mount (if you have access to a printer at university) | Custom bracket to clip electronics to the frame's arm/temple | Filament cost only, ~50–150 EGP (estimate) if you design it yourselves |
| Hot glue / double-sided mounting tape / small cable clips | For attaching wiring along the frame without a full custom mount | ~50–100 EGP (estimate) |
| Thin flexible wiring / ribbon cable (for camera, if physically separate from the compute unit) | Keep runs short and flexible to avoid stressing the frame | ~50–150 EGP (estimate) |
| Small project box / enclosure for the compute unit + battery (worn on a lanyard, in a pocket, or on a headband) | Keeps the heavy/bulky Pi + battery off the glasses frame itself — glasses only carry camera + mic + speaker | ~100–300 EGP (estimate) |

**Physical design recommendation:** don't try to fit the Raspberry Pi and battery *onto* the glasses themselves — it's heavy and will break the frame. Mount only the camera, a small mic, and a thin speaker/earpiece on the glasses; run a thin cable down to a small enclosure worn on a lanyard, headband, or in a pocket carrying the Pi/phone and battery. This is exactly how most real prototype smart-glasses projects are built.

---

## Three Hardware Configurations

### Configuration A — Budget MVP
Repurposed Android phone (own the whole pipeline — camera, mic, speaker, battery, connectivity) + a cheap glasses frame with only a small clip-on camera/mic wired to the phone via USB, OR fully phone-only with the phone itself held/clipped near the frame.
- **Total additional hardware cost:** as low as **~800–1,500 EGP** (estimate) if a phone and glasses frame are already owned, mainly for a mount/clip and a small USB microphone/camera if not using the phone's own camera directly.
- **Performance:** good — modern phone CPUs handle YOLOv8n and OCR at usable speed.
- **Battery life:** 4–6+ hours (phone battery).
- **Advantages:** cheapest by far, fewest new components, most reliable (phones "just work").
- **Disadvantages:** less "glasses-like" if the phone itself isn't glasses-mounted; less impressive as a hardware build for a portfolio.

### Configuration B — Recommended (university demo)
Raspberry Pi 4 (4GB) + Pi Camera Module or USB board camera + USB microphone + small wired speaker/earbuds + 10,000mAh power bank + basic 3D-printed/glued mount.
- **Total hardware cost:** roughly **~7,000–11,000 EGP** (estimate, all-in).
- **Performance:** solid for the MVP feature set at reduced resolution/quantized models.
- **Battery life:** ~3–5 hours via power bank.
- **Advantages:** genuinely embedded/wearable-feeling, avoids current Pi 5 price spike, safest power setup (no bare Li-Po).
- **Disadvantages:** more setup/wiring work than the phone option; a bit heavier.

### Configuration C — Advanced
NVIDIA Jetson Orin Nano + Pi Camera Module 3 (wide) + I2S MEMS microphone + bone-conduction speaker + Li-Po pack with protection circuit and boost/UPS HAT.
- **Total hardware cost:** roughly **~24,000–32,000 EGP** (estimate, all-in).
- **Performance:** best — real hardware-accelerated inference, lowest latency, could run detection fully on-device without cloud dependency.
- **Battery life:** ~2–4 hours depending on pack size and Jetson power mode.
- **Advantages:** most production-like, most impressive technically, best offline capability.
- **Disadvantages:** by far the most expensive, most complex to assemble safely (Li-Po), longest setup time — real risk for a time-boxed student project.

---

## Final Bill of Materials (Recommended Configuration B)

| Component | Recommended Model | Qty | Purpose | MVP? | Approx. Price (EGP, estimate) | Store (check current price) | Alternative |
|---|---|---:|---|---|---:|---|---|
| Compute unit | Raspberry Pi 4 (4GB) | 1 | Main processing | 🟢 | 3,500–4,800 | RAM Electronics, Dev Boards Market, Amazon.eg | Repurposed Android phone (free) |
| Camera | Small wide-angle USB board camera | 1 | Visual input | 🟢 | 600–1,400 | RAM Electronics, Amazon.eg, Noon | Pi Camera Module v2 |
| Microphone | USB clip microphone | 1 | Voice input | 🟡 | 400–900 | Amazon.eg, Noon | I2S MEMS mic (INMP441) |
| Speaker | Small wired speaker or reused earbuds | 1 | Audio output | 🟢 | 0–600 | Amazon.eg, Noon | Bone-conduction speaker |
| Battery | 10,000mAh USB-C power bank | 1 | Portable power | 🟢 | 500–900 | Amazon.eg, Noon, Jumia | Li-Po pack + UPS HAT |
| microSD card (32–64GB, A2 class) | For Pi OS + models | 1 | Storage/boot | 🟢 | 300–600 | RAM Electronics, Amazon.eg | — |
| Push button + wiring | Momentary tactile button | 1 | Capture trigger | 🟢 | 20–50 | RAM Electronics, Dev Boards Market | Wake-word (software, free) |
| Glasses frame | Cheap plastic frame | 1 | Mounting base | 🟢 | 50–200 (or reuse) | Any optical shop / pharmacy | — |
| Mounting materials | Tape, clips, small enclosure | — | Physical assembly | 🟢 | 150–400 | Hardware store | 3D printing (if available) |
| **Total MVP (Config A)** | | | | | **~800–1,500** | | |
| **Total Recommended (Config B)** | | | | | **~7,000–11,000** | | |
| **Total Advanced (Config C)** | | | | | **~24,000–32,000** | | |

**Must Buy:** compute unit (if not reusing a phone), camera, microSD card, button, mounting materials.
**Can Reuse:** laptop (for development, not the wearable itself), smartphone (as compute unit or backup camera), power bank, wired/Bluetooth earbuds, USB microphone if anyone already owns a decent one.
**Optional (add later, not now):** bone-conduction speaker, I2S MEMS mic upgrade, distance sensor, second camera for stereo/depth, Jetson upgrade.

---

## Architecture Cost/Trade-off Comparison

| Factor | Camera → Pi/Phone → Local AI → TTS → Speaker (fully local) | Camera → Pi/Phone → Backend/API → LLM → TTS → Speaker (hybrid, cloud LLM) |
|---|---|---|
| Cost | Higher upfront hardware (need enough local compute), no ongoing API fees | Lower upfront hardware, small ongoing API cost per query |
| Latency | Can be fast if hardware is capable (esp. Jetson); local small-LLM phrasing is often *worse and slower* than a cloud call on weak hardware | LLM call adds ~1–2s network latency, but phrasing quality is much higher |
| Internet dependency | None (fully offline capable) | Required for the LLM/cloud-TTS step |
| Privacy | Best — no data leaves the device | Frames/audio sent to a third-party API |
| Battery consumption | Higher (sustained local inference draws more power) | Lower (device does less heavy compute, network radio draws less than sustained CPU/GPU inference) |
| AI performance/quality | Detection is fine locally; **local LLM phrasing on Pi-class hardware is genuinely weak** | Much higher-quality natural language responses |
| Implementation difficulty | Harder — quantization, offline LLM setup, more moving parts | Easier — call an API, less on-device complexity |

**Recommendation:** Use the **hybrid architecture** (local CV detection + OCR on-device, cloud LLM call for phrasing/reasoning) for the MVP. Pure local-LLM phrasing on Raspberry Pi-class hardware tends to produce noticeably clunkier, less natural responses — not a good trade for a demo where the "wow" factor is largely the response quality. Reserve a fully local/offline mode for a post-MVP stretch goal if you specifically want to demonstrate privacy/offline capability.

---

## "If I were a student building this on a limited budget, here's my exact shopping list, in priority order:"

1. **Reuse a spare Android phone** as the compute + camera + mic + speaker + battery, if any team member has one sitting in a drawer — this alone can take your hardware budget close to zero.
2. If you need a dedicated headless device instead: **Raspberry Pi 4 (4GB)** — not the 5, given the 2026 memory-driven price spike.
3. **microSD card (32–64GB, A2-rated)** — don't skimp here, a slow card will bottleneck everything.
4. **Small wide-angle USB board camera** — best size/weight for glasses mounting.
5. **USB clip microphone** — simplest reliable audio input.
6. **10,000mAh USB-C power bank** — safest, simplest power source; skip Li-Po wiring entirely for MVP.
7. **A cheap/old glasses frame** you don't mind modifying, plus tape/clips/a small project box for mounting — don't buy a "nice" frame.
8. **A basic push-button** for capture triggering — cheapest, most reliable trigger method; skip wake-word detection for MVP.
9. *Only after the above works end-to-end:* consider a bone-conduction speaker or I2S mic as a "polish" upgrade for the final demo, using leftover budget.

Skip entirely for a first-term MVP: Jetson boards, Li-Po battery packs, any additional sensors (IMU/GPS/distance), a second camera, and any face-recognition or custom-training work.
