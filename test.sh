#!/bin/bash
set -e

cd student-management && mvn clean test

if [ -f "./src/target/site/jacoco/jacoco.xml" ]; then
    echo "=== Résumé couverture ==="
    echo "StudentService: $(grep -A 3 'StudentService' .src/target/site/jacoco/jacoco.xml | grep 'LINE' | grep -o 'COVEREDRATIO="[^"]*"' | cut -d'"' -f2)"
    echo "StudentController: $(grep -A 3 'StudentController' .src/target/site/jacoco/jacoco.xml | grep 'LINE' | grep -o 'COVEREDRATIO="[^"]*"' | cut -d'"' -f2)"
    echo "DepartmentController: $(grep -A 3 'DepartmentController' .src/target/site/jacoco/jacoco.xml | grep 'LINE' | grep -o 'COVEREDRATIO="[^"]*"' | cut -d'"' -f2)"
fi
mvn sonar:sonar -Dsonar.login=sqa_5ae7efd1d8dc5b2e0e9a4b7182324964ab97f098
