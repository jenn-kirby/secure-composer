/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Steg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Jenn
 */
public class Steganography {

    public static final byte[] VERSION_BYTE= {'1','2','2'};
    // WAVE file header size is 44 bytes
    public static final int OFFSET_WAVE= 44;
    public static final short HEADER_LENGTH= 15* 4;

    //  Uncompressed/Compressed  Encrypted/Unencrypted  Message/File
    public static final byte UUM= 0;
    public static final byte UEM= 2;
    public static final byte CUM= 4;
    public static final byte CEM= 6;

    private static Cipher cipher;

    private static SecretKeySpec spec;

    private static File masterFile;
    // This byte stores the features being used by the file
    private static byte features;
    private static int inputFileSize;
    private static int i, j, inputOutputMarker, messageSize, tempInt;
    private static short compressionRatio= 0;
    private static byte byte1, byte2, byte3, byteArrayIn[];
    private static ByteArrayOutputStream byteOut;

    // 0-arg constructor
    public Steganography()
    {
        System.out.println("Steganography ready...");
    }

    // Embeds a message into a Master file
    public boolean embedMessage(File masterFile, File outputFile, String msg, int compression, String password)
    {
        if(msg==null)
        {
            System.out.println("msg null");
            return false;
        }
        if(msg.length()<1)
        {
            System.out.println("msg length < 1");
            return false;
        }

        if(password!= null && password.length()<8)
        {
            System.out.println("password not null & less than 8 characters");
            return false;
        }

        messageSize= msg.length();

        if(compression!= -1)
        {
            // Make sure that the compression is a valid numerical between 0 and 9
            if(compression< 0)		compression= 0;
            if(compression>9)		compression= 9;

            if(password== null)
                features= CUM;
            else
                features= CEM;
        }
        else
        {
            if(password== null)
                features= UUM;
            else
                features= UEM;
        }

        try
        {
            byteOut= new ByteArrayOutputStream();
            // Convert message into a character array
            byte []messageArray= msg.getBytes();
            messageSize= messageArray.length;
            inputFileSize= (int) masterFile.length();

            // create a byte array of length equal to size of input file
            byteArrayIn= new byte[inputFileSize];

            // Open the input file read all bytes into byteArrayIn
            DataInputStream in= new DataInputStream(new FileInputStream(masterFile));
            in.read(byteArrayIn, 0, inputFileSize);
            in.close();


            // Skip past WAVE HEADER OFFSET bytes
            byteOut.write(byteArrayIn, 0, OFFSET_WAVE);
            inputOutputMarker= OFFSET_WAVE;


            // Convert the 32 bit input file size into byte array
            byte tempByte[]= new byte[4];
            for(i=24, j=0; i>=0; i-=8, j++)
            {
                tempInt= inputFileSize;
                tempInt>>= i;
                tempInt&= 0x000000FF;
                tempByte[j]= (byte) tempInt;
            }
            // Embed 4 byte input File size array into the master file
            embedBytes(tempByte);

            // Write the remaining bytes
            byteOut.write(byteArrayIn, inputOutputMarker, inputFileSize- inputOutputMarker);
            inputOutputMarker= inputFileSize;

            // Embed the 3 byte version information into the file
            writeBytes(VERSION_BYTE);

            // Write 1 byte for features
            writeBytes(new byte[]{features});

            // Compress the message if required
            if(features== CUM || features== CEM)
            {
                ByteArrayOutputStream arrayOutputStream= new ByteArrayOutputStream();
                ZipOutputStream zOut= new ZipOutputStream(arrayOutputStream);
                ZipEntry entry= new ZipEntry("MESSAGE");
                zOut.setLevel(compression);
                zOut.putNextEntry(entry);

                zOut.write(messageArray, 0, messageSize);
                zOut.closeEntry();
                zOut.finish();
                zOut.close();

                // Get the compressed message byte array
                messageArray= arrayOutputStream.toByteArray();
                compressionRatio= (short) ((double)messageArray.length / (double)messageSize * 100.0);
                messageSize= messageArray.length;
            }

            // Embed 1 byte compression ratio into the output file
            writeBytes(new byte[]{(byte) compressionRatio});


            // Encrypt the message if required
            if(features== UEM || features== CEM)
            {
                cipher= Cipher.getInstance("DES");
                spec= new SecretKeySpec(password.substring(0, 8).getBytes(), "DES");
                cipher.init(Cipher.ENCRYPT_MODE, spec);
                messageArray= cipher.doFinal(messageArray);
                messageSize= messageArray.length;
            }

            // Convery the 32 bit message size into byte array
            tempByte= new byte[4];
            for(i=24, j=0; i>=0; i-=8, j++)
            {
                tempInt= messageSize;
                tempInt>>= i;
                tempInt&= 0x000000FF;
                tempByte[j]= (byte) tempInt;
            }
            // Embed 4 byte messageSize array into the master file
            writeBytes(tempByte);

            // Embed the message
            writeBytes(messageArray);

            DataOutputStream out= new DataOutputStream(new FileOutputStream(outputFile));
            //out.write(byteArrayOut, 0, byteArrayOut.length);
            byteOut.writeTo(out);
            out.close();
        }
        catch(EOFException e)
        {
            System.out.println("EOF: " + e);
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
            e.printStackTrace();
            return false;
        }
        System.out.println("Message embedded successfully in file '"+ outputFile.getName()+ "'.");
        return true;
    }
    //embeds msg to byte array
    public boolean embedMessage(byte[] master, File outputFile, String msg, int compression, String password)
    {
        if(msg==null)
        {
            System.out.println("msg null");
            return false;
        }
        if(msg.length()<1)
        {
            System.out.println("msg length < 1");
            return false;
        }

        if(password!= null && password.length()<8)
        {
            System.out.println("password not null & less than 8 characters");
            return false;
        }

        messageSize= msg.length();

        if(compression!= -1)
        {
            // Make sure that the compression is a valid numerical between 0 and 9
            if(compression< 0)		compression= 0;
            if(compression>9)		compression= 9;

            if(password== null)
                features= CUM;
            else
                features= CEM;
        }
        else
        {
            if(password== null)
                features= UUM;
            else
                features= UEM;
        }
        try
        {
            byteOut= new ByteArrayOutputStream();
            // Convert message into a character array
            byte []messageArray= msg.getBytes();
            messageSize= messageArray.length;
            //inputFileSize= (int) masterFile.length();
            inputFileSize= master.length;


            //read all bytes into byteArrayIn
            byteArrayIn = master;

            // Skip past WAVE HEADER OFFSET bytes
            byteOut.write(byteArrayIn, 0, OFFSET_WAVE);
            inputOutputMarker= OFFSET_WAVE;


            // Convert the 32 bit input file size into byte array
            byte tempByte[]= new byte[4];
            for(i=24, j=0; i>=0; i-=8, j++)
            {
                    tempInt= inputFileSize;
                    tempInt>>= i;
                    tempInt&= 0x000000FF;
                    tempByte[j]= (byte) tempInt;
            }
            // Embed 4 byte input File size array into the master file
            embedBytes(tempByte);

            // Write the remaining bytes
            byteOut.write(byteArrayIn, inputOutputMarker, inputFileSize- inputOutputMarker);
            inputOutputMarker= inputFileSize;

            // Embed the 3 byte version information into the file
            writeBytes(VERSION_BYTE);

            // Write 1 byte for features
            writeBytes(new byte[]{features});

            // Compress the message if required
            if(features== CUM || features== CEM)
            {
                ByteArrayOutputStream arrayOutputStream= new ByteArrayOutputStream();
                ZipOutputStream zOut= new ZipOutputStream(arrayOutputStream);
                ZipEntry entry= new ZipEntry("MESSAGE");
                zOut.setLevel(compression);
                zOut.putNextEntry(entry);

                zOut.write(messageArray, 0, messageSize);
                zOut.closeEntry();
                zOut.finish();
                zOut.close();

                // Get the compressed message byte array
                messageArray= arrayOutputStream.toByteArray();
                compressionRatio= (short) ((double)messageArray.length / (double)messageSize * 100.0);
                messageSize= messageArray.length;
            }

            // Embed 1 byte compression ratio into the output file
            writeBytes(new byte[]{(byte) compressionRatio});


            // Encrypt the message if required
            if(features== UEM || features== CEM)
            {
                cipher= Cipher.getInstance("DES");
                spec= new SecretKeySpec(password.substring(0, 8).getBytes(), "DES");
                cipher.init(Cipher.ENCRYPT_MODE, spec);
                messageArray= cipher.doFinal(messageArray);
                messageSize= messageArray.length;
            }

            // Convery the 32 bit message size into byte array
            tempByte= new byte[4];
            for(i=24, j=0; i>=0; i-=8, j++)
            {
                tempInt= messageSize;
                tempInt>>= i;
                tempInt&= 0x000000FF;
                tempByte[j]= (byte) tempInt;
            }
            // Embed 4 byte messageSize array into the master file
            writeBytes(tempByte);

            // Embed the message
            writeBytes(messageArray);

            DataOutputStream out= new DataOutputStream(new FileOutputStream(outputFile));
            //out.write(byteArrayOut, 0, byteArrayOut.length);
            byteOut.writeTo(out);
            out.close();
        }
        catch(EOFException e)
        {
            System.out.println("EOF: " + e);
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
            e.printStackTrace();
            return false;
        }
        System.out.println("Message embedded successfully in file '"+ outputFile.getName()+ "'.");
        return true;
    }
    public boolean embedMessage(byte[] master, File outputFile, String msg, int compression)
    {
        //this method does not further encrypt the msg.
        if(msg==null)
        {
            System.out.println("msg null");
            return false;
        }
        if(msg.length()<1)
        {
            System.out.println("msg length < 1");
            return false;
        }

        messageSize= msg.length();

        if(compression!= -1)
        {
            // Make sure that the compression is a valid numerical between 0 and 9
            if(compression< 0)		compression= 0;
            if(compression>9)		compression= 9;

            features= CUM;
        }
        else
        {
            features= UUM;
        }
        try
        {
            byteOut= new ByteArrayOutputStream();
            // Convert message into a character array
            byte []messageArray= msg.getBytes();
            messageSize= messageArray.length;
            //inputFileSize= (int) masterFile.length();
            inputFileSize= master.length;


            //read all bytes into byteArrayIn
            byteArrayIn = master;

            // Skip past WAVE HEADER OFFSET bytes
            byteOut.write(byteArrayIn, 0, OFFSET_WAVE);
            inputOutputMarker= OFFSET_WAVE;


            // Convert the 32 bit input file size into byte array
            byte tempByte[]= new byte[4];
            for(i=24, j=0; i>=0; i-=8, j++)
            {
                    tempInt= inputFileSize;
                    tempInt>>= i;
                    tempInt&= 0x000000FF;
                    tempByte[j]= (byte) tempInt;
            }
            // Embed 4 byte input File size array into the master file
            embedBytes(tempByte);

            // Write the remaining bytes
            byteOut.write(byteArrayIn, inputOutputMarker, inputFileSize- inputOutputMarker);
            inputOutputMarker= inputFileSize;

            // Embed the 3 byte version information into the file
            writeBytes(VERSION_BYTE);

            // Write 1 byte for features
            writeBytes(new byte[]{features});

            // Compress the message if required
            if(features== CUM || features== CEM)
            {
                ByteArrayOutputStream arrayOutputStream= new ByteArrayOutputStream();
                ZipOutputStream zOut= new ZipOutputStream(arrayOutputStream);
                ZipEntry entry= new ZipEntry("MESSAGE");
                zOut.setLevel(compression);
                zOut.putNextEntry(entry);

                zOut.write(messageArray, 0, messageSize);
                zOut.closeEntry();
                zOut.finish();
                zOut.close();

                // Get the compressed message byte array
                messageArray= arrayOutputStream.toByteArray();
                compressionRatio= (short) ((double)messageArray.length / (double)messageSize * 100.0);
                messageSize= messageArray.length;
            }

            // Embed 1 byte compression ratio into the output file
            writeBytes(new byte[]{(byte) compressionRatio});


            // Convery the 32 bit message size into byte array
            tempByte= new byte[4];
            for(i=24, j=0; i>=0; i-=8, j++)
            {
                tempInt= messageSize;
                tempInt>>= i;
                tempInt&= 0x000000FF;
                tempByte[j]= (byte) tempInt;
            }
            // Embed 4 byte messageSize array into the master file
            writeBytes(tempByte);

            // Embed the message
            writeBytes(messageArray);

            DataOutputStream out= new DataOutputStream(new FileOutputStream(outputFile));
            //out.write(byteArrayOut, 0, byteArrayOut.length);
            byteOut.writeTo(out);
            out.close();
        }
        catch(EOFException e)
        {
            System.out.println("EOF: " + e);
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e);
            e.printStackTrace();
            return false;
        }
        System.out.println("Message embedded successfully in file '"+ outputFile.getName()+ "'.");
        return true;
    }

    // Retrieves an embedded message from a Master file
    public String retrieveMessage(StegInfo info, String password)
    {
        String messg= null;
        features= info.getFeatures();

        try
        {
            masterFile= info.getFile();
            byteArrayIn= new byte[(int) masterFile.length()];

            DataInputStream in= new DataInputStream(new FileInputStream(masterFile));
            in.read(byteArrayIn, 0, (int)masterFile.length());
            in.close();

            messageSize= info.getDataLength();

            if(messageSize<=0)
            {
                System.out.println("Unexpected size of message: 0.");
                return("#FAILED#");
            }

            byte[] messageArray= new byte[messageSize];
            inputOutputMarker= info.getInputMarker();
            readBytes(messageArray);

            //Decrypt the message if required
            if(features== CEM || features== UEM)
            {
                password= password.substring(0, 8);
                byte passwordBytes[]= password.getBytes();
                cipher= Cipher.getInstance("DES");
                spec= new SecretKeySpec(passwordBytes, "DES");
                cipher.init(Cipher.DECRYPT_MODE, spec);
                try
                {
                    messageArray= cipher.doFinal(messageArray);
                }
                catch(Exception bp)
                {
                    System.out.println("Incorrent Password");
                    bp.printStackTrace();
                    return "#FAILED#";
                }
                messageSize= messageArray.length;
            }

            // Uncompress the message if required
            if(features== CUM || features== CEM)
            {
                ByteArrayOutputStream by= new ByteArrayOutputStream();
                DataOutputStream out= new DataOutputStream(by);

                ZipInputStream zipIn= new ZipInputStream(new ByteArrayInputStream(messageArray));
                zipIn.getNextEntry();
                byteArrayIn= new byte[1024];
                while((tempInt= zipIn.read(byteArrayIn, 0, 1024))!= -1)
                        out.write(byteArrayIn, 0, tempInt);

                zipIn.close();
                out.close();
                messageArray= by.toByteArray();
                messageSize= messageArray.length;
            }
            messg= new String(StegInfo.byteToCharArray(messageArray));
        }
        catch(Exception e)
        {
            System.out.println("An Error has occured: "+ e);
            e.printStackTrace();
            return("#FAILED#");
        }

        System.out.println("Message size: "+ messageSize+ " B");
        return messg;
    }

    public String retrieveMessage(StegInfo info)
    {
        String messg= null;
        features= info.getFeatures();

        try
        {
            masterFile= info.getFile();
            byteArrayIn= new byte[(int) masterFile.length()];

            DataInputStream in= new DataInputStream(new FileInputStream(masterFile));
            in.read(byteArrayIn, 0, (int)masterFile.length());
            in.close();

            messageSize= info.getDataLength();

            if(messageSize<=0)
            {
                System.out.println("Unexpected size of message: 0.");
                return("#FAILED#");
            }

            byte[] messageArray= new byte[messageSize];
            inputOutputMarker= info.getInputMarker();
            readBytes(messageArray);

            // Uncompress the message if required
            if(features== CUM || features== CEM)
            {
                ByteArrayOutputStream by= new ByteArrayOutputStream();
                DataOutputStream out= new DataOutputStream(by);

                ZipInputStream zipIn= new ZipInputStream(new ByteArrayInputStream(messageArray));
                zipIn.getNextEntry();
                byteArrayIn= new byte[1024];
                while((tempInt= zipIn.read(byteArrayIn, 0, 1024))!= -1)
                        out.write(byteArrayIn, 0, tempInt);

                zipIn.close();
                out.close();
                messageArray= by.toByteArray();
                messageSize= messageArray.length;
            }
            messg= new String(StegInfo.byteToCharArray(messageArray));
        }
        catch(Exception e)
        {
            System.out.println("An Error has occured: "+ e);
            e.printStackTrace();
            return("#FAILED#");
        }

        System.out.println("Message size: "+ messageSize+ " B");
        return messg;
    }



    // Method used to write bytes into the output array
    private static void embedBytes(byte[] bytes)
    {
        int size= bytes.length;

        for(int i=0; i< size; i++)
        {
            byte1= bytes[i];
            for(int j=6; j>=0; j-=2)
            {
                byte2= byte1;
                byte2>>= j;
                byte2&= 0x03;

                byte3= byteArrayIn[inputOutputMarker];
                byte3&= 0xFC;
                byte3|= byte2;
                byteOut.write(byte3);
                inputOutputMarker++;
            }
        }
    }

    // Method used to write bytes into the output array
    private static void writeBytes(byte[] bytes)
    {
        int size= bytes.length;
        for(int i=0; i< size; i++)
        {
            byteOut.write(bytes[i]);
            inputOutputMarker++;
        }
    }

    // Method used to read bytes into the output array
    private static void readBytes(byte[] bytes)
    {
        int size= bytes.length;

        for(i=0; i< size; i++)
        {
            bytes[i]= byteArrayIn[inputOutputMarker];
            inputOutputMarker++;
        }
    }

}

