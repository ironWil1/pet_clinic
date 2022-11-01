FROM maven:3.8-jdk-11-slim AS  populate_cache_stage
COPY src /root/src
WORKDIR /root/src
RUN mvn -B versions:set -DprocessAllModules=true -DnewVersion=0.0.0.1-PRE-POPULATED-BUILD \
 && mvn -B clean install -DskipITs=true -DskipTests=true \
 && mvn -B org.apache.maven.plugins:maven-dependency-plugin:3.1.1:go-offline \
 && cd /root/.m2/repository \
 && du -sh ./* \
 && (find . -name '0.0.0.1-PRE-POPULATED-BUILD'  -exec rm -r {} \; || true )\
 && du -sh ./*

FROM maven:3.6.1-jdk-11-slim
COPY --from=populate_cache_stage /root/.m2 /root/.m2
WORKDIR /root/src
