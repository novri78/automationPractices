#!/usr/bin/env bash
set -euo pipefail

RERUN=target/rerun.txt
FILTERED=target/rerun-flaky.txt

: > "$FILTERED"

if [[ -f "$RERUN" && -s "$RERUN" ]]; then
  echo "Filtering $RERUN for @flaky tags..."
  while IFS= read -r line; do
    # line format: path:line
    file="${line%%:*}"
    lineno="${line##*:}"
    if [[ ! -f "$file" ]]; then
      echo "Skip missing file: $file"
      continue
    fi
    # look a few lines before to find tags
    start=$(( lineno > 5 ? lineno - 5 : 1 ))
    if sed -n "${start},${lineno}p" "$file" | grep -q "@flaky"; then
      echo "$line" >> "$FILTERED"
    fi
  done < "$RERUN"

  if [[ -s "$FILTERED" ]]; then
    echo "Found flaky entries, rerunning those scenarios from $FILTERED"
    mvn -B -Dheadless=true -Dcucumber.options="@${FILTERED} --tags @flaky" test || true
  else
    echo "No @flaky scenarios found in rerun list"
  fi
else
  echo "No rerun.txt or it's empty"
fi
