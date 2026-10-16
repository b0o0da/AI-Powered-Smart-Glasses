# Branching and review rules

## Branches

| Branch | Purpose | Who merges |
| --- | --- | --- |
| `main` | Gate releases only. Tagged `gate-1` … `gate-4`, `v1.0`. | Mostafa S., after a gate review |
| `develop` | Integration branch. Every feature PR targets it. Merged at least twice a week. | PR author, after approval + green CI |
| `feature/<area>-<description>` | One Notion task (or a tight group) per branch. | — |
| `fix/<area>-<description>` | Bug fixes, linked to a `BUG-xxx` row in Notion. | — |
| `release/gate-N` | Optional stabilization branch in the gate week. | Mostafa S. |

Both `main` and `develop` are protected: no direct pushes, no force pushes, a PR with at least one approval and green CI is required. Set it up once with `scripts/setup_branch_protection.sh`.

## Branch prefixes by area

| Prefix | Owners | Typical paths |
| --- | --- | --- |
| `feature/android-*` | Mostafa S., Loay | `android/` |
| `feature/backend-*` | Mostafa S., Mahmoud | `backend/`, `contracts/`, `android/contracts/`, repo tooling |
| `feature/engine-*` | Marwan | `android/engine/`, consent gate |
| `feature/cv-*` | Mostafa O., Marwan | `computer_vision/`, `models/`, camera code |
| `feature/voice-*` | Boda | voice code, `nlp/prompt_templates/` |
| `feature/screen-*` | Loay | screen and notification code |
| `feature/data-*` | Mahmoud | `datasets/`, database |
| `feature/test-*` | Loay | `tests/` |

The prefix follows the **task's** area, not the person. The exact branch name is in each Notion task's *Branch* field.

## Pull requests

1. Title: `AVN-<Task ID>: <imperative summary>` (for example `AVN-27: Create repo layout and branch rules`).
2. Fill in the PR template: Notion link, what changed, how it was tested, evidence.
3. Reviewer: the task's *Reviewer* in Notion. It is always someone from another role. CODEOWNERS adds owners automatically.
4. Merge only with approval and green CI. Prefer **squash merge** into `develop`; use a **merge commit** from `develop` into `main`.
5. After merge: set the Notion task to *Done*, paste the PR link, attach evidence.

## Contract changes

Anything under `contracts/` or `docs/api-contract.md` after the Gate 1 freeze follows `contracts/README.md`:

- a Technical Decision in Notion and `docs/decisions/` (`TD-xxx`),
- a version bump in `contracts/README.md` and `docs/api-contract.md`,
- the `contract-change` label, and review from every consumer listed in the Integration Seams database.

## Commits

- Small and focused. Present tense: `Add FakeDetector replay from JSON`.
- Never commit secrets, models, datasets, face images, screenshots or field recordings. CI scans for secrets on every PR.
