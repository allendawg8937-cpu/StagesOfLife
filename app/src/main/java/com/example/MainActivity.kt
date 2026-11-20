git fetch origin

git checkout 0bcf6e13c80e9e592569e4dccb7e2607d0b9f6fd -- app/src/main/java/com/example/MainActivity.kt

git add app/src/main/java/com/example/MainActivity.kt

git commit -m "Fix MainActivity Kotlin source"

git push origin main
#!/usr/bin/env bash
set -euo pipefail

FILE="app/src/main/java/com/example/MainActivity.kt"
BACKUP="${FILE}.broken-backup"

# Preserve the accidentally overwritten file.
cp "$FILE" "$BACKUP"

# Restore the complete Kotlin implementation.
git show 0bcf6e13c80e9e592569e4dccb7e2607d0b9f6fd:"$FILE" > "$FILE"

# Confirm that the file contains Kotlin, not Bash.
grep -q '^package com.example' "$FILE"
! grep -q '^#!/usr/bin/env bash' "$FILE"

echo "MainActivity.kt successfully restored."

git diff --check
git add "$FILE"
git commit -m "Restore MainActivity Kotlin source"
git push origin main
