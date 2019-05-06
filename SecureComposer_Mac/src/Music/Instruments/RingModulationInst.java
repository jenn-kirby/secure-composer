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

/**
 * A basic ring modulation synthesis instrument
 * which is basic Amplitude Modulation
 * @author Andrew Brown
 */

public final class RingModulationInst extends jm.audio.Instrument{
	//----------------------------------------------
	// Attributes
	//----------------------------------------------
	/** the sample rate passed to the instrument */
	private int sampleRate;
        /** the sample rate passed to the instrument */
	private int channels;

	//----------------------------------------------
	// Constructor
	//----------------------------------------------
	/**
	 * Basic default constructor to set an initial
	 * sampling rate.
	 * @param sampleRate
	 */
	public RingModulationInst(int sampleRate){
	    this.sampleRate = sampleRate;
	    this.channels = 1;
	}
        public RingModulationInst(int sampleRate, int channels){
	    this.sampleRate = sampleRate;
	    this.channels = channels;
	}

	//----------------------------------------------
	// Methods
	//----------------------------------------------
     /**
	 * Initialisation method used to build the objects that
	 * this instrument will use.
	 */
	public void createChain(){
		Oscillator modulator = new Oscillator(this,
			Oscillator.SINE_WAVE, this.sampleRate,
			this.channels);
		modulator.setFrqRatio((float)2.1);
		Envelope env = new Envelope(modulator,
			new double[] {0.0, 0.0, 1.0, 1.0});
		Oscillator carrier = new Oscillator(env,
		Oscillator.SINE_WAVE, Oscillator.AMPLITUDE);
		Envelope env2 = new Envelope(carrier,
			new double[] {0.0, 0.0, 0.05, 1.0, 1.0, 0.0});
		SampleOut sout = new SampleOut(env2);
	}
}
