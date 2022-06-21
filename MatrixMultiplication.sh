#! /bin/sh
echo "\e[96m1)\e[0m Removing the folders/files of the last execution from HDFS....."
hdfs dfs -rm -r /matrix
echo "\e[92mDone\e[0m\n"

echo "\e[96m2)\e[0m Compiling \"ReadInput.java\"....."
javac ReadInput.java
echo "\e[92mDone\e[0m\n"

echo "\e[96m3)\e[0m Executing \"ReadInput.class\" - Creating \"matrix_input.txt\" from \"m1.txt\" and \"m2.txt\"....."
java ReadInput
echo "\e[92mDone\e[0m\n"

echo "\e[96m4)\e[0m Creating a folder named \"matrix\" in HDFS....."
hdfs dfs -mkdir /matrix
echo "\e[92mDone\e[0m\n"

echo "\e[96m5)\e[0m Copying \"M1.txt\" and \"M2.txt\" to HDFS, from local storage....."
hdfs dfs -put M1.txt /matrix
hdfs dfs -put M2.txt /matrix
echo "\e[92mDone\e[0m\n"

echo "\e[96m6)\e[0m Compiling \"MatrixMultiplication.java\"....."
javac MatrixMultiplication.java -cp $(hadoop classpath)
echo "\e[92mDone\e[0m\n"

echo "\e[96m7)\e[0m Executing MatrixMultiplication (MapReduce)......"
export HADOOP_CLASSPATH=.
hadoop MatrixMultiplication
echo "\e[92mDone\e[0m\n\n"

echo "\e[1;4mMapReduce Output\e[0m\n"

echo "\e[1mMapReduce1 Output\e[0m"
hdfs dfs -cat /matrix/matrix_output/mapreduce1/part-r-00000
echo "\n\e[1mMapReduce2 Output\e[0m"
hdfs dfs -cat /matrix/matrix_output/mapreduce2/part-r-00000
echo "\n\e[92mCompleted\e[0m"
rm M1.txt
rm M2.txt
echo ""