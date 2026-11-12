#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

# Build the executable JAR
mvn -q -DskipTests clean package

# Find the JAR without hardcoding the version
jar_file="$(find target -maxdepth 1 -type f -name 'terminal-quest-*.jar' -print -quit)"

if [[ -z "$jar_file" ]]; then
    echo "Error: executable JAR not found." >&2
    exit 1
fi

# Temporary files for save data and game output
save_file="$(mktemp)"
output="$(mktemp)"
trap 'rm -f "$save_file" "$output"' EXIT

# Simulate player commands
printf 'status\nexplore\nattack\nsave\nnew\nload\nquit\n' \
    | TERMINAL_QUEST_SAVE="$save_file" java -jar "$jar_file" > "$output"

# Verify expected behavior
grep -q 'Game saved successfully' "$output"
grep -q 'Game loaded successfully' "$output"
grep -q 'Goodbye!' "$output"

echo "Smoke test passed: game starts, saves, loads, and exits."
