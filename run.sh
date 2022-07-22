# run Maven with debug output
# batch mode and piping console output through file helps to get readable logs in Quarterfal
mvn --batch-mode -X -e compile  exec:java@CheckerRunner > output

cat output

