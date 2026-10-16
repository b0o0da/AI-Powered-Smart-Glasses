#!/usr/bin/env bash
# Create or update the repo's issue/PR labels. Safe to re-run.
# Usage: ./scripts/create_labels.sh [owner/repo]
set -euo pipefail

REPO="${1:-b0o0da/AI-Powered-Smart-Glasses}"
command -v gh >/dev/null || { echo "Install the GitHub CLI: https://cli.github.com"; exit 1; }

label() { gh label create "$1" --repo "$REPO" --color "$2" --description "$3" --force >/dev/null && echo "ok $1"; }

# Type
label "type:bug"        d73a4a "Something is broken"
label "type:blocker"    b60205 "Work cannot continue"
label "blocked"         b60205 "Waiting on another task, device or decision"
label "contract-change" 5319e7 "Changes contracts/ or docs/api-contract.md; needs TD-xxx"
label "dependencies"    0366d6 "Dependency update"

# Severity (matches Notion Bugs & Blockers)
label "sev:critical" b60205 "Missed/false safety alert, crash, privacy leak, SOS failure"
label "sev:high"     d93f0b "Severity high"
label "sev:medium"   fbca04 "Severity medium"
label "sev:low"      c2e0c6 "Severity low"

# Area (matches branch prefixes)
for a in android backend engine cv voice screen data test; do
  label "area:$a" 1d76db "Area: $a"
done

# Gates
for g in 1 2 3 4; do label "gate:G$g" 0e8a16 "Needed for Gate $g"; done

# Workstreams WS-01 .. WS-30
for i in $(seq -w 1 30); do label "ws-$i" ededed "Workstream WS-$i"; done
