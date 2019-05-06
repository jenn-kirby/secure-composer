/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Music;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JApplet;

/**
 *
 * @author Jenn
 */

public class AudioPlay
{
    static SoundList soundList;
    static String wavFile = "tones.wav";
    static File file;
    String filename;
    static AudioClip onceClip;
    AudioClip clip;
    static URL codeBase;
    static URL url;

 
    public AudioPlay(String filename)
    {
        file = new File(filename);
        try {
            clip = Applet.newAudioClip(file.toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(AudioPlay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void stop()
    {
        clip.stop(); //Cut short the one-time sound.
    }
    public void play()
    {
        clip.play();
    }
}

class SoundList extends java.util.Hashtable {
  JApplet applet;

  URL baseURL;

  public SoundList(URL baseURL) {
    super(5); //Initialize Hashtable with capacity of 5 entries.
    this.baseURL = baseURL;
  }

  public void startLoading(String relativeURL) {
    new SoundLoader(this, baseURL, relativeURL);
  }

  public AudioClip getClip(String relativeURL) {
    return (AudioClip) get(relativeURL);
  }

  public void putClip(AudioClip clip, String relativeURL) {
    put(relativeURL, clip);
  }
}

class SoundLoader extends Thread {
  SoundList soundList;

  URL completeURL;

  String relativeURL;

  public SoundLoader(SoundList soundList, URL baseURL, String relativeURL) {
    this.soundList = soundList;
    try {
      completeURL = new URL(baseURL, relativeURL);
    } catch (MalformedURLException e) {
      System.err.println(e.getMessage());
    }
    this.relativeURL = relativeURL;
    setPriority(MIN_PRIORITY);
    start();
  }

    @Override
  public void run() {
    AudioClip audioClip = Applet.newAudioClip(completeURL);
    soundList.putClip(audioClip, relativeURL);
  }
}
