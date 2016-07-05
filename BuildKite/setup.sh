#!/usr/bin/env bash

set -o pipefail

echo "--- Setting up environment"
chmod 777 BuildKite/test.sh
$AGENT_SCRIPT_HOME/agent-port.sh "./BuildKite/test.sh"