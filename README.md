
# Matrix Multiplication

Matrix Multiplication of two matrices using Hadoop MapReduce.

# Requirements

1. Hadoop 2 (Tested on Hadoop 2.4.1)
2. Java JDK 1.8

# Commands to execute

0. Start the Hadoop daemons (Ex: namenode, datanode, etc.)

    ```bash
    start-dfs.sh
    start-yarn.sh
    ```

    To check whether they are all running successfully

    ```bash
    jps
    ```

1. Create files `m1.txt` and `m2.txt` for Matrix 1 and Matrix 2 respectively\
**Example**: A 3x3 Matrix is represented in the form of

	```txt
	3 3
	1 2 3
	4 5 6
	7 8 9
	```

2. Execute  `ReadInput.java`

	```bash
	javac ReadInput.java
	java ReadInput
	```

	_Generates M1.txt and M2.txt_

3.  Create a folder named `matrix` in HDFS

    ```bash
    hdfs dfs -mkdir /matrix
    ```

4. Copy the files `M1.txt` and `M2.txt` to `/matrix` folder in HDFS

    ```bash
    hdfs dfs -put M1.txt /matrix
    hdfs dfs -put M2.txt /matrix
    ```

5.  Execute `MatrixMultiplication.java`

    ```
    javac MatrixMultiplication.java -cp $(hadoop classpath)
    export HADOOP_CLASSPATH=.
    hadoop MatrixMultiplication
    ```

6.  To view the output in terminal
	
	Output of MapReduce**1**:

    ```bash
    hdfs dfs -cat /matrix/matrix_output/mapreduce1/part-r-00000
    ```

	Output of MapReduce**2**:

    ```bash
    hdfs dfs -cat /matrix/matrix_output/mapreduce2/part-r-00000
    ```

# Output

1. MapReduce1 Output

	```txt
	2,2,21.0
	2,1,14.0
	2,0,7.0
	1,2,12.0
	1,1,8.0
	1,0,4.0
	0,2,3.0
	0,1,2.0
	0,0,1.0
	2,2,48.0
	2,1,40.0
	2,0,32.0
	1,2,30.0
	1,1,25.0
	1,0,20.0
	0,2,12.0
	0,1,10.0
	0,0,8.0
	2,2,81.0
	2,1,72.0
	2,0,63.0
	1,2,54.0
	1,1,48.0
	1,0,42.0
	0,2,27.0
	0,1,24.0
	0,0,21.0
	```

2. MapReduce2 Output

	```txt
	(0,0) 30.0
	(0,1) 36.0
	(0,2) 42.0
	(1,0) 66.0
	(1,1) 81.0
	(1,2) 96.0
	(2,0) 102.0
	(2,1) 126.0
	(2,2) 150.0
	```
