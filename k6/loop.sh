#!/bin/sh
PERIOD_SECONDS=${PERIOD_SECONDS:-20}
while true; do
  # Run your command here
  k6 run /config/script.js
  sleep $PERIOD_SECONDS
done