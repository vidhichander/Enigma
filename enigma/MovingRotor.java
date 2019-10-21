package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Vidhi Chander
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _permutation = perm;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        super.set((setting() + 1) % alphabet().size());
    }

    @Override
    boolean atNotch() {
        for (char c : _notches.toCharArray()) {
            if (setting() == alphabet().toInt(c)) {
                return true;
            }

        }
        return false;
    }


    /** The notches for this rotor. */
    private String _notches;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;
}
