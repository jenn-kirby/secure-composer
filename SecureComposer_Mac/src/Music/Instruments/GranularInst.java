/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Music.Instruments;

/**
 *
 * @author Jenn
 */

import jm.audio.io.*;
import jm.audio.synth.*;
import jm.audio.AudioObject.*;
import jm.audio.Instrument;

public class GranularInst extends jm.audio.Instrument{
	//----------------------------------------------
	// Attributes
	//----------------------------------------------
	/** the name of the sample file */
	private String fileName;
	/** How many channels is the sound file we are using */
	private int numOfChannels;
	/** the base frequency of the sample file to be read in */
	private double baseFreq;
	/** should we play the wholeFile or just what we need for note duration */
	private boolean wholeFile;
	/** The points to use in the construction of Envelopes */
	private EnvPoint[] pointArray = new EnvPoint[10];

        private int duration;
        private int grainsPerSecond;
        private boolean randomIndex;
        private boolean randomGrainDur;
        private boolean randomFreq;

	private Granulator grain;
	private int bufsize;
        private int sampleRate = 44100;
        private Volume vol;
        private StereoPan pan;
        Instrument[] ensemble;
	//----------------------------------------------
	// Constructor
	//----------------------------------------------
	/**
	 * Constructor
	 */
	public GranularInst(String fileName,int bufsize){
		this.fileName = fileName;
		this.bufsize = bufsize;
                this.numOfChannels = 2;
                this.duration = 50;
                this.grainsPerSecond = 100;
                this.randomIndex = false;
                this.randomGrainDur = false;
                this.randomFreq = false;
	}
        public GranularInst(String fileName,int bufsize, int dur, int gps){
		this.fileName = fileName;
		this.bufsize = bufsize;
                this.numOfChannels = 2;
                this.duration = dur;
                this.grainsPerSecond = gps;
                this.randomIndex = false;
                this.randomGrainDur = false;
                this.randomFreq = false;
	}
        public GranularInst(String fileName, boolean randomIndex,
                boolean randomGrainDur, boolean randomFreq){
		this.fileName = fileName;
                this.numOfChannels = 2;
                this.duration = 50;
                this.grainsPerSecond = 100;
                this.randomIndex = randomIndex;
                this.randomGrainDur = randomGrainDur;
                this.randomFreq = randomFreq;
                
	}

	//----------------------------------------------
	// Methods
	//----------------------------------------------
	/**
	 * Create the Audio Chain for this Instrument
	 * and assign the primary Audio Object(s). The
	 * primary audio object(s) are the one or more
	 * objects which head up the chain(s)
	 */
	public void createChain(){
		//define the chain
		//SampleIn osc = new SampleIn(this,this.fileName);
                SampleIn osc = new SampleIn(this,this.fileName, false, true);
                grain = new Granulator(osc, sampleRate, numOfChannels, duration,grainsPerSecond);
		setRandomIndex(randomIndex);
                setRandomGrainDuration(randomGrainDur);
                setRandomFreq(randomFreq);
                vol = new Volume(grain,1.0f);//changed from 0.5 to 1.0
                Volume vol2 = new Volume(vol,1.0f);//changed from 0.1 to 1.0
                pan = new StereoPan(vol2);
                SampleOut sout = new SampleOut(pan);
	}

	public void setGrainsPerSecond(int sp){
		grain.setGrainsPerSecond(sp);
	}

	public void setGrainDuration(int gdur){
		grain.setGrainDuration(gdur);
	}

	public void setFreqMod(float fmod){
		grain.setFreqMod(fmod);
	}
	public void setRandomIndex(boolean b){
		grain.setRandomIndex(b);
	}
	public void setRandomGrainDuration(boolean b){
		grain.setRandomGrainDuration(b);
	}

	public void setRandomGrainTop(int top){
		grain.setRandomGrainTop(top);
	}
	public void setRandomGrainBottom(int b){
		grain.setRandomGrainBottom(b);
	}
	public void setRandomFreq(boolean bool){
		grain.setRandomFreq(bool);
	}
	public void setRandomFreqBottom(double d){
		grain.setRandomFreqBottom((float) d);
	}
	public void setRandomFreqTop(double d){
		grain.setRandomFreqTop((float) d);
	}
        public void setVolume(float d){
		vol.setVolume(d);
	}
        public void setPan(float p){
		pan.setPan(p);
	}
}