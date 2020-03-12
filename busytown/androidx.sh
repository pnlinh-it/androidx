#!/bin/bash
set -e

cd "$(dirname $0)"

# Run Gradle
impl/build.sh --no-daemon buildOnServer --profile "$@"
impl/build.sh --no-daemon listTaskOutputs "$@"

# Merge some output files
python3 impl/merge_outputs.py "mergeBuildInfo" "mergeLibraryMetrics"

# Parse performance profile reports (generated with the --profile option above) and re-export the metrics in an easily machine-readable format for tracking
impl/parse_profile_htmls.sh
