import java.io.IOException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
 
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
 
public class MatrixMultiplication {

    // MAPPER 1
    public static class M1Mapper extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String inputLine = value.toString();
            String[] input_split = inputLine.split(" ");
            Text mapperKey = new Text();
            Text keyValue = new Text();
            mapperKey.set(input_split[1]);
            keyValue.set("X," + input_split[0] + "," + input_split[2]);
            context.write(mapperKey, keyValue);
        }
    }
    public static class M2Mapper extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String inputLine = value.toString();
            String[] input_split = inputLine.split(" ");
            Text mapperKey = new Text();
            Text keyValue = new Text();
            mapperKey.set(input_split[0]);
            keyValue.set("Y," + input_split[1] + "," + input_split[2]);
            context.write(mapperKey, keyValue);
        }
    }

    // REDUCER 1
    public static class Reduce1 extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String[] value;
            ArrayList<Entry<Integer, Float>> list1 = new ArrayList<Entry<Integer, Float>>();
            ArrayList<Entry<Integer, Float>> list2 = new ArrayList<Entry<Integer, Float>>();
            for (Text val : values) {
                value = val.toString().split(",");
                if (value[0].equals("X")) {
                    list1.add(new SimpleEntry<Integer, Float>(Integer.parseInt(value[1]), Float.parseFloat(value[2])));
                } else {
                    list2.add(new SimpleEntry<Integer, Float>(Integer.parseInt(value[1]), Float.parseFloat(value[2])));
                }
            }
            String i,k;
            float l1_val,l2_val;
            Text keyValue = new Text();
            for (Entry<Integer, Float> l1 : list1) {
                i = Integer.toString(l1.getKey());
                l1_val = l1.getValue();
                for (Entry<Integer, Float> l2 : list2) {
                    k = Integer.toString(l2.getKey());
                    l2_val = l2.getValue();
                    keyValue.set(i + "," + k + "," + Float.toString(l1_val*l2_val));
                    context.write(null, keyValue);
                }
            }
        }
    }

    // MAPPER 2
    public static class Mapper2 extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String inputLine = value.toString();
            String[] input_split = inputLine.split(",");
            Text mapperKey = new Text();
            Text keyValue = new Text();
            mapperKey.set("("+input_split[0] + "," + input_split[1]+")");
            keyValue.set(input_split[2]);
            context.write(mapperKey, keyValue);
        }
    }

    // REDUCER 2
    public static class Reduce2 extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            double count=0.0;
            for (Text val : values)
                count=count+Double.parseDouble(val.toString());
            Text keyValue = new Text();
            keyValue.set(key + " " + Double.toString(count));
            context.write(null, keyValue);
        }
    }

    // DRIVER CODE
    public static void main(String[] args) throws Exception {
 
        // Job1
        Configuration conf = new Configuration();
        Job job1 = Job.getInstance(conf, "MatrixMatrixMultiplicationPart1");
        job1.setJarByClass(MatrixMultiplication.class);
        job1.setReducerClass(Reduce1.class); 
        job1.setInputFormatClass(TextInputFormat.class);
        job1.setOutputFormatClass(TextOutputFormat.class);        
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class); 
        MultipleInputs.addInputPath(job1, new Path("/matrix/M1.txt"), TextInputFormat.class,  M1Mapper.class);
        MultipleInputs.addInputPath(job1, new Path("/matrix/M2.txt"), TextInputFormat.class,  M2Mapper.class);
        FileOutputFormat.setOutputPath(job1, new Path("/matrix/matrix_output/mapreduce1"));
        job1.waitForCompletion(true);
        System.out.println("MapReduce 1 execution completed.");

        // Job2
        Configuration conf1 = new Configuration();
        Job job2 = Job.getInstance(conf1, "MatrixMatrixMultiplicationPart2");
        job2.setJarByClass(MatrixMultiplication.class); 
        job2.setMapperClass(Mapper2.class);
        job2.setReducerClass(Reduce2.class); 
        job2.setInputFormatClass(TextInputFormat.class);
        job2.setOutputFormatClass(TextOutputFormat.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(Text.class); 
        FileInputFormat.addInputPath(job2, new Path("/matrix/matrix_output/mapreduce1/part-r-00000"));
        FileOutputFormat.setOutputPath(job2, new Path("/matrix/matrix_output/mapreduce2"));
        job2.waitForCompletion(true);
        System.out.println("MapReduce 2 execution completed.");
    }
}
