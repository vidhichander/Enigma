package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Vidhi
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        addCycle(_cycles);
    }

    /** Add the cycle c0->c1->...->cm->c0 to an array, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        String[] cyclesList;
        cyclesList = cycle.split("()");
        int j = 0;
        for (String s : cyclesList) {
            if (s.equals("(")) {
                j += 1;
            }
        }
        int y = 0;
        int k = 0;
        if (j != 0) {
            cycleAlist = new String[j][];
            for (int i = 0; i < cyclesList.length; i++) {
                if (!cyclesList[i].equals("(") && !cyclesList[i].equals(" ")) {
                    if (cyclesList[i].equals(")")) {
                        cycleAlist[k] = new String[y];
                        y = -1;
                        k += 1;
                    }
                    y += 1;

                }
            }
            y = 0;
            k = 0;
            for (int i = 0; i < cyclesList.length; i++) {
                if (!cyclesList[i].equals("(") && !cyclesList[i].equals(" ")) {
                    if (cyclesList[i].equals(")")) {
                        y = 0;
                        k += 1;
                    } else {
                        cycleAlist[k][y] = cyclesList[i];
                        y++;
                    }
                }
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int newp = wrap(p);
        int k = 0;
        int int2string;
        if (cycleAlist != null) {
            while (k < cycleAlist.length) {
                for (int i = 0; i < cycleAlist[k].length; i++) {
                    int2string = _alphabet.toInt(cycleAlist[k][i].charAt(0));
                    if (int2string == newp) {
                        if ((i + 1) >= cycleAlist[k].length) {
                            return _alphabet.toInt(cycleAlist[k][0].charAt(0));
                        } else {
                            return _alphabet.toInt
                                    (cycleAlist[k][i + 1].charAt(0));

                        }
                    }


                }
                k += 1;
            }
        }
        return newp;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int newp = wrap(c);
        int k = 0;
        boolean found = false;
        int int2string;
        if (cycleAlist != null) {
            while (k < cycleAlist.length) {
                for (int i = 0; i < cycleAlist[k].length; i++) {
                    int2string = _alphabet.toInt(cycleAlist[k][i].charAt(0));
                    if (int2string == newp) {
                        found = true;
                        if (i == 0) {
                            return _alphabet.toInt
                                    (cycleAlist[k][cycleAlist[k].length - 1]
                                            .charAt(0));
                        } else {
                            return _alphabet.toInt
                                    (cycleAlist[k][i - 1].charAt(0));

                        }
                    }


                }
                k += 1;
            }
        }
        return newp;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int per = permute(alphabet().toInt(p));
        char newvalue = alphabet().toChar(per);
        return newvalue;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int inv = invert(alphabet().toInt(c));
        return alphabet().toChar(inv);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation. */
    private String _cycles;

    /** Cycles of this permutation sorted into a 2D array. */
    private String [][] cycleAlist;
}
