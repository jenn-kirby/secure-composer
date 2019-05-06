/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Music;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jenn
 */
public class KeySignature {

    //C, C#, D, D#, E, F, F#, G, G#, A, A#, B
    //0, 1,  2, 3,  4, 5, 6,  7, 8,  9, 10, 11

    private String key;
    private int[] keyNotes = new int[7];

    private int[] C = GenerateC();
    private int[] CSharp = new int[7];
    private int[] D = new int[7];
    private int[] DSharp = new int[7];
    private int[] E = new int[7];
    private int[] F = new int[7];
    private int[] FSharp = new int[7];
    private int[] G = new int[7];
    private int[] GSharp = new int[7];
    private int[] A = new int[7];
    private int[] ASharp = new int[7];
    private int[] B = new int[7];

    private int[] CMinor = new int[7];
    private int[] CSharpMinor = new int[7];
    private int[] DMinor = new int[7];
    private int[] DSharpMinor = new int[7];
    private int[] EMinor = new int[7];
    private int[] FMinor = new int[7];
    private int[] FSharpMinor = new int[7];
    private int[] GMinor = new int[7];
    private int[] GSharpMinor = new int[7];
    private int[] AMinor = new int[7];
    private int[] ASharpMinor = new int[7];
    private int[] BMinor = new int[7];


    public KeySignature(String keySig)
    {
        key = keySig.replaceAll("#", "Sharp");
        if (key.endsWith("m"))
            key+="inor";
        GenerateKey();
    }
    public void GenerateKey()
    {
        Object paramsObj[] = {};
        try {
            Method thisMethod = KeySignature.class.getDeclaredMethod("Generate" + key);
            keyNotes = (int[]) thisMethod.invoke(this, paramsObj);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(KeySignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(KeySignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(KeySignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(KeySignature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(KeySignature.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("notes in key: ");
        for (int i=0; i<keyNotes.length; i++)
        {
            System.out.print(keyNotes[i] + " ");
        }
    }

    public int[] PutInKey(int[] msgs)
    {
        //this method modifies the msgs array so that the notes fit within the specified key
        boolean inkey = true;
        for (int i=0; i<msgs.length; i++)
        {
            int msg = msgs[i];
            msg %=12;
            for (int j=0; j<keyNotes.length; j++)
            {
                if (msg==keyNotes[j])
                {
                    inkey=true;
                    break;
                }
                else
                {
                    inkey=false;
                }
            }
            if (!inkey)
                msgs[i] = msgs[i]+1;
        }
        return msgs;
    }

    public int[] GenerateC()
    {
        int[] C1 = {0, 2, 4, 5, 7, 9, 11};
        return C = C1;
    }
    public int[] GenerateCSharp()
    {
        for (int i=0; i<C.length; i++)
        {
            CSharp[i] = (C[i]+1)%12;
        }
        return CSharp;
    }
    public int[] GenerateD()
    {
        for (int i=0; i<C.length; i++)
        {
            D[i] = (C[i]+2)%12;
        }
        return D;
    }
    public int[] GenerateDSharp()
    {
        for (int i=0; i<C.length; i++)
        {
            DSharp[i] = (C[i]+3)%12;
        }
        return DSharp;
    }
    public int[] GenerateE()
    {
        for (int i=0; i<C.length; i++)
        {
            E[i] = (C[i]+4)%12;
        }
        return E;
    }
    public int[] GenerateF()
    {
        for (int i=0; i<C.length; i++)
        {
            F[i] = (C[i]+5)%12;
        }
        return F;
    }
    public int[] GenerateFSharp()
    {
        for (int i=0; i<C.length; i++)
        {
            FSharp[i] =(C[i]+6)%12;
        }
        return FSharp;
    }
    public int[] GenerateG()
    {
        for (int i=0; i<C.length; i++)
        {
            G[i] = (C[i]+7)%12;
        }
        return G;
    }
    public int[] GenerateGSharp()
    {
        for (int i=0; i<C.length; i++)
        {
            GSharp[i] = (G[i]+8)%12;
        }
        return GSharp;
    }
    public int[] GenerateA()
    {
        for (int i=0; i<C.length; i++)
        {
            A[i] = (C[i]+9)%12;
        }
        return A;
    }
    public int[] GenerateASharp()
    {
        for (int i=0; i<C.length; i++)
        {
            ASharp[i] = (C[i]+10)%12;
        }
        return ASharp;
    }
    public int[] GenerateB()
    {
        for (int i=0; i<C.length; i++)
        {
            B[i] = (C[i]+11)%12;
        }
        return B;
    }

    //minors
    public int[] GenerateCminor()
    {
        return CMinor = GenerateDSharp();
    }
    public int[] GenerateCSharpminor()
    {
        return CSharpMinor = GenerateE();
    }
    public int[] GenerateDminor()
    {
        return DMinor = GenerateF();
    }
    public int[] GenerateDSharpminor()
    {
        return DSharpMinor = GenerateFSharp();
    }
    public int[] GenerateEminor()
    {
        return EMinor = GenerateG();
    }
    public int[] GenerateFminor()
    {
        return FMinor = GenerateGSharp();
    }
    public int[] GenerateFSharpminor()
    {
        return FSharpMinor = GenerateA();
    }
    public int[] GenerateGminor()
    {
        return GMinor = GenerateASharp();
    }
    public int[] GenerateGSharpminor()
    {
        return GSharpMinor = GenerateB();
    }
    public int[] GenerateAminor()
    {
        return AMinor = C;
    }
    public int[] GenerateASharpminor()
    {
        return ASharpMinor = GenerateCSharp();
    }
    public int[] GenerateBminor()
    {
        return BMinor = GenerateD();
    }
}
