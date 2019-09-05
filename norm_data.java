
package javaapplication2;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.core.learning.DataSet;
import org.neuroph.core.learning.DataSetRow;
import org.neuroph.util.norm.MaxMinNormalizer;
import org.neuroph.util.norm.Normalizer;


public class JavaApplication2 {

       
    public static void main(String[] args) throws IOException {
        
       
        String file="C:\\Users\\500054698\\Documents\\NetBeansProjects\\JavaApplication2\\src\\javaapplication2\\Sample.csv";
        int input=6;
        int output=0;
        String delimeter=",";
        boolean lcm=true;
        DataSet dataSet;
        dataSet = createFromFile(file,input,output,delimeter,lcm);
      

        Normalizer norm = new MaxMinNormalizer();
        norm.normalize(dataSet);
       
        PrintStream o=new PrintStream(new File("C:\\Users\\500054698\\Documents\\NetBeansProjects\\JavaApplication2\\src\\javaapplication2\\output.txt"));
        PrintStream console= System.out;
        
        System.setOut(o);
        for (DataSetRow dataSetRow : dataSet.getRows()) {
        System.out.println(Arrays.toString(dataSetRow.getInput()));}

       System.setOut(console);
        for (DataSetRow dataSetRow : dataSet.getRows()) {
        System.out.println("Input: " + Arrays.toString(dataSetRow.getInput()));}


       
    }
    public static DataSet createFromFile(String filePath, int inputsCount, int outputsCount, String delimiter, boolean loadColumnNames) throws IOException {
       
        if (filePath == null) throw new IllegalArgumentException("File name cannot be null!");
        if (inputsCount <= 0) throw new IllegalArgumentException("Number of inputs cannot be <= 0 : "+inputsCount);
        if (outputsCount < 0) throw new IllegalArgumentException("Number of outputs cannot be < 0 : "+outputsCount);
        if ((delimiter == null) || delimiter.isEmpty())
            throw new IllegalArgumentException("Delimiter cannot be null or empty!");

        try ( BufferedReader reader = new BufferedReader(new FileReader(filePath)) ) {
            DataSet dataSet = new DataSet(inputsCount, outputsCount);
            dataSet.setFilePath(filePath);            

            String line = null;

            if (loadColumnNames) {
                // get column names from the first line
                line = reader.readLine();
                String[] colNames = line.split(delimiter);
                dataSet.setColumnNames(colNames);
            }

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(delimiter);

                double[] inputs = new double[inputsCount];
                double[] outputs = new double[outputsCount];

                if (values[0].equals("")) {
                    continue; // skip if line was empty
                }
                for (int i = 0; i < inputsCount; i++) {
                    inputs[i] = Double.parseDouble(values[i]);
                }

                for (int i = 0; i < outputsCount; i++) {
                    outputs[i] = Double.parseDouble(values[inputsCount + i]);
                }

                if (outputsCount > 0) {
                    dataSet.addRow(new DataSetRow(inputs, outputs));
                } else {
                    dataSet.addRow(new DataSetRow(inputs));
                }
            }

            reader.close();
            
            return dataSet;

        } catch (FileNotFoundException ex) {
            throw new NeurophException("Could not find data set file!", ex);
        } catch (IOException ex) {
             throw new NeurophException("Error reading data set file!", ex);
        } catch (NumberFormatException ex) {
             ex.printStackTrace();
            throw new NeurophException("Bad number format in data set file!", ex); 
        }

    }
}
