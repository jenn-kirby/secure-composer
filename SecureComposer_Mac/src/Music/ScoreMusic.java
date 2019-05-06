/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Music;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;

/**
 *
 * @author Jenn
 */


public class ScoreMusic implements Runnable {
    HelperMethods helper = new HelperMethods();
    private int[] msgs;
    public Score score = new Score("Basic Music");
    public ScoreMusic(int[] msgs) {
        this.msgs = msgs;
    }

    public void run() {
        score = SineWithBass(msgs);
        GUI.GUI.setScore(score);
    }
    public Score getScore()
    {
        return score;
    }

    public Score SineWithBass(int[] msgs)
    {
        Score s = new Score();
        //s.setTempo(60);
        if (msgs[0]<=20)
        s.setTempo(msgs[0]+20);
        else
        s.setTempo(msgs[0]);
        s.setTimeSignature(msgs[0]%16, msgs[1]%16);
        Part p = new Part("Sine", 0);
        Part p2 = new Part("Bass", 0);
        //double[] ampPoints = new double[msgs.length];
        for(int i=0; i<msgs.length; i++)
        {
            int noteVal = msgs[i];
            noteVal-=12;//bring down an octave
            noteVal = helper.positiveInt(noteVal);
            Note n = new Note(noteVal, 4.0);
            //n.setPan(Math.random());
            n.setPan(helper.scale(0.2, 0.8, 0, 128, msgs[i]));
            n.setDuration(helper.scale(0.2, 3, 0, 128, msgs[i]));
            n.setDynamic(helper.scale(20, 100, 0, 128, msgs[i]));
            Phrase phrase1  = new Phrase(n, Math.random() * 10);
            p.addPhrase(phrase1);
        }
        for (int i=0; i<msgs.length/8; i++)
        {
            int index = ((i+1)*8)-1;//get every 8th note
            int noteVal = msgs[index];
            noteVal = noteVal/12 + 24;//pitch down note
            Note n = new Note(noteVal, 4.0);
            //n.setPan(Math.random());
            n.setPan(helper.scale(0.2, 0.8, 0, 128, msgs[index]));
            n.setDuration(helper.scale(0.2, 3, 0, 128, msgs[index]));
            n.setDynamic(helper.scale(20, 100, 0, 128, msgs[index]));
            Phrase phrase2  = new Phrase(n, Math.random() * 10);
            p2.addPhrase(phrase2);
        }

        s.addPart(p);
        s.addPart(p2);
        return s;//part
    }
}
