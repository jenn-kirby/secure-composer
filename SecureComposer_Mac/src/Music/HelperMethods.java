package Music;


import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jenn
 */
public class HelperMethods {

    public double scale(double newmin, double newmax, double oldmin, double oldmax, double value)
    {
        //return (value / ((oldmax - oldmin) / (newmax - newmin))) + newmin;
        return (((value-oldmin)/oldmax) * newmax) + newmin;
    }
    public int scale(int newmin, int newmax, int oldmin, int oldmax, int value)
    {
        float newVal;
        newVal = value-oldmin;
        newVal = newVal/oldmax;
        newVal = newVal*newmax;
        newVal = newVal + newmin;

        return (int) newVal;
        //return (((value-oldmin)/oldmax) * newmax) + newmin;
    }
    public int positiveInt(int value)
    {
        if(value<0)
            return value*-1;
        else
            return value;
    }
    public byte[] auToByteArray(File file)
    {
        int inputFileSize= (int) file.length();
        // create a byte array of length equal to size of input file
        byte[] byteArrayIn= new byte[inputFileSize];

        // Open the input file read all bytes into byteArrayIn
        DataInputStream in;
        try
        {
            in = new DataInputStream(new FileInputStream(file));
            in.read(byteArrayIn, 0, inputFileSize);
            in.close();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(HelperMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(HelperMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        return byteArrayIn;
    }


}
