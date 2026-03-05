# LocalVitrine UX Audit (Phase 1)

## Scope

- Reviewed the main authenticated user flow (`dashboard` -> `project detail` -> `editor`).
- Reviewed the admin flow (`admin dashboard` -> `users` -> `templates`).
- Focused on consistency, clarity, feedback, and responsive behavior.

## Findings

### Navigation and Flow

- User flow was generally clear, but action labels mixed French and English.
- Destructive actions had minimal confirmation context.
- Some success states were implicit (list reload) without explicit user feedback.

### Consistency Gaps

- Admin pages used custom raw buttons while user pages used shared `UiButtonComponent`.
- Error display patterns were inconsistent across pages.
- Table/card wrappers varied in style and spacing.

### Feedback and States

- Missing success messages for some actions (toggle/delete in admin pages).
- Limited busy-state locking, allowing accidental repeated clicks.
- Editor did not surface a clear "unsaved changes" state.

### Responsiveness and Accessibility

- Admin tables could become dense on smaller widths.
- Focus states existed globally but were not consistently paired with semantic components.

## Quick Wins Implemented

- Standardized inline alerts with reusable global classes:
  - `lv-alert`
  - `lv-alert--error`
  - `lv-alert--success`
- Added reusable table container style:
  - `lv-card-table-wrap`
- Refactored admin users/templates actions to use `UiButtonComponent`.
- Added success feedback after create/update/toggle/delete actions.
- Added busy-state guards for user management actions to prevent duplicate requests.
- Improved destructive confirmation copy for delete actions.
- Improved editor topbar with clearer copy and "unsaved changes" state.
- Added lightweight responsive optimization for admin users table by hiding less-critical column on narrow widths.

## Structural Fixes (Next Phases)

- Introduce a shared data-table component with built-in empty/loading/error/skeleton states.
- Introduce shared page header component (title/subtitle/actions slot) to unify layout rhythm.
- Add a unified toast/snackbar service for global feedback instead of inline per-page only.
- Normalize terminology and localization (French-first dictionary, status labels, action verbs).
- Add keyboard-first modal/dialog component for destructive confirmations (replace `confirm()`).

## Execution Status

- Phase 1 audit and quick wins: completed.
- Remaining phases proceed incrementally to avoid regressions in existing EPIC 5/6 flows.
