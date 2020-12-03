echo "-XX:+UseSerialGC 256"
java -jar -Xms256m -Xmx256m -XX:+UseSerialGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs/dump build/libs/gcDemo-0.1.jar
echo ""
echo "-XX:+UseSerialGC 8G"
java -jar -Xms8G -Xmx8G -XX:+UseSerialGC  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs/dump build/libs/gcDemo-0.1.jar
echo " -------------------- "
echo "-XX:+UseParallelGC 256"
java -jar -Xms256m -Xmx256m -XX:+UseParallelGC  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs/dump build/libs/gcDemo-0.1.jar
echo ""
echo "-XX:+UseParallelGC 8G"
java -jar -Xms8G -Xmx8G -XX:+UseParallelGC  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs/dump build/libs/gcDemo-0.1.jar
echo " -------------------- "
echo "-XX:+UseG1GC 256"
java -jar -Xms256m -Xmx256m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs/dump build/libs/gcDemo-0.1.jar
echo ""
echo "-XX:+UseG1GC 8G"
java -jar -Xms8G -Xmx8G -XX:+UseG1GC -XX:MaxGCPauseMillis=10 -XX:InitiatingHeapOccupancyPercent=20 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs/dump build/libs/gcDemo-0.1.jar
echo " -------------------- "
echo "-XX:+UseShenandoahGC 256"
java -jar -Xms256m -Xmx256m -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs/dump build/libs/gcDemo-0.1.jar
echo ""
echo "-XX:+UseShenandoahGC 8G"
java -jar -Xms8G -Xmx8G -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=logs/dump build/libs/gcDemo-0.1.jar
