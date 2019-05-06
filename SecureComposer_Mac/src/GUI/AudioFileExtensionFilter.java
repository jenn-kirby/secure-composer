package GUI;


import java.io.File;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jenn
 */
public class AudioFileExtensionFilter extends javax.swing.filechooser.FileFilter
{
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".wav");
    }

    public String getDescription() {
        return "WAV files";
    }
}
