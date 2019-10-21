package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Vidhi Chander
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the collection of rotor slots I have. */
    ArrayList<Rotor> machineRotors() {
        return _machineRotors;
    }

    /** Return the collection of all possible rotors I have. */
    Collection<Rotor> allRotors() {
        return _allRotors;
    }


    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _machineRotors = new ArrayList<Rotor>();
        for (String s : rotors) {
            for (Rotor r : _allRotors) {
                if (s.equalsIgnoreCase(r.name())) {
                    _machineRotors.add(r);
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        int i = 1;
        for (char c : setting.toCharArray()) {
            _machineRotors.get(i).set(c);
            i += 1;
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        int i = 1;
        int k = 0;
        int index = _machineRotors.size() - 1;

        while (i < (_machineRotors.size() - 1)) {
            if ((_machineRotors.get(i + 1).atNotch()
                    && _machineRotors.get(i).rotates()) && k == 0) {
                _machineRotors.get(i).advance();
                k = 0;
                if (!(_machineRotors.get(i + 1).equals
                        (_machineRotors.get(index)))) {
                    _machineRotors.get(i + 1).advance();
                }
                k = k + 1;
            }
            i = i + 1;
        }
        _machineRotors.get(index).advance();

        if (_plugboard != null) {
            c = _plugboard.permute(c);
        }
        for (int j = _machineRotors.size() - 1; j >= 0; j -= 1) {
            c = _machineRotors.get(j).convertForward(c);
        }
        for (int j = 1; j < _machineRotors.size(); j++) {
            c = _machineRotors.get(j).convertBackward(c);
        }

        if (_plugboard != null) {
            c = _plugboard.permute(c);
        }
        return c;
    }


    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String message = "";
        int converted;
        for (char s : msg.toCharArray()) {

            converted = convert(_alphabet.toInt(s));
            message += _alphabet.toChar(converted);
        }
        return message;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Plugboard for machine. */
    private Permutation _plugboard;

    /** Number of rotors. */
    private int _numRotors;

    /** Number of pawls. */
    private int _pawls;

    /** Collection of all rotors. */
    private Collection<Rotor> _allRotors;

    /** Arraylist of all rotors inserted into machine. */
    private ArrayList<Rotor> _machineRotors = new ArrayList<Rotor>();
}
