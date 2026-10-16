#!/usr/bin/env bash
# One-time GitHub setup: create `develop`, make it the default branch, and protect
# `main` and `develop`. Run by a repo admin with the GitHub CLI logged in (`gh auth login`).
#
# Usage: ./scripts/setup_branch_protection.sh [owner/repo]
set -euo pipefail

REPO="${1:-b0o0da/AI-Powered-Smart-Glasses}"
command -v gh >/dev/null || { echo "Install the GitHub CLI: https://cli.github.com"; exit 1; }

echo "== Creating develop from main (if missing)"
if ! gh api "repos/$REPO/branches/develop" >/dev/null 2>&1; then
  SHA="$(gh api "repos/$REPO/git/ref/heads/main" --jq .object.sha)"
  gh api -X POST "repos/$REPO/git/refs" -f ref=refs/heads/develop -f sha="$SHA" >/dev/null
fi

echo "== Making develop the default branch; enabling auto-delete of merged branches"
gh api -X PATCH "repos/$REPO" \
  -f default_branch=develop \
  -F delete_branch_on_merge=true \
  -F allow_squash_merge=true -F allow_merge_commit=true -F allow_rebase_merge=false >/dev/null

protect() {
  local branch="$1" approvals="$2"
  echo "== Protecting $branch ($approvals approval(s), CI required, no force push)"
  gh api -X PUT "repos/$REPO/branches/$branch/protection" --input - >/dev/null <<JSON
{
  "required_status_checks": {
    "strict": true,
    "contexts": ["Contracts", "Secret scan", "Backend", "Android", "Title has a Notion task ID", "Branch name follows docs/branching.md"]
  },
  "enforce_admins": false,
  "required_pull_request_reviews": {
    "required_approving_review_count": $approvals,
    "dismiss_stale_reviews": true,
    "require_code_owner_reviews": false
  },
  "restrictions": null,
  "allow_force_pushes": false,
  "allow_deletions": false,
  "required_conversation_resolution": true
}
JSON
}

protect develop 1
protect main 1

echo "== Enabling secret scanning push protection (works on public repos / with GHAS)"
gh api -X PATCH "repos/$REPO" --input - >/dev/null <<'JSON' || echo "   (not available for this repo plan; CI gitleaks still runs)"
{"security_and_analysis":{"secret_scanning":{"status":"enabled"},"secret_scanning_push_protection":{"status":"enabled"}}}
JSON

echo "Done. Once CODEOWNERS has real handles, set require_code_owner_reviews to true."
