package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import static enigma.TestUtils.*;

/** The suite of all JUnit Integration tests.
 *  @author Vidhi Chander
 */
public class IntegrationTests {

    @Test
    public void testDoubleStep() {
        Alphabet ac = new CharacterRange('A', 'D');
        Rotor one = new Reflector("R1", new Permutation("(AC) (BD)", ac));
        Rotor two = new MovingRotor("R2", new Permutation("(ABCD)", ac), "C");
        Rotor three = new MovingRotor("R3", new Permutation("(ABCD)", ac), "C");
        Rotor four = new MovingRotor("R4", new Permutation("(ABCD)", ac), "C");
        String setting = "AAA";
        Rotor[] machineRotors = {one, two, three, four};
        String[] rotors = {"R1", "R2", "R3", "R4"};
        Machine mach = new Machine(ac, 4, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        assertEquals("AAAA", getSetting(ac, machineRotors));
        mach.convert('a');
        assertEquals("AAAB", getSetting(ac, machineRotors));
        mach.convert('b');
        assertEquals("AAAC", getSetting(ac, machineRotors));
        mach.convert('c');
        assertEquals("AABD", getSetting(ac, machineRotors));
        mach.convert('b');
        assertEquals("AABA", getSetting(ac, machineRotors));
        mach.convert('b');
        assertEquals("AABB", getSetting(ac, machineRotors));
        mach.convert('b');
        assertEquals("AABC", getSetting(ac, machineRotors));
        mach.convert('x');
        assertEquals("AACD", getSetting(ac, machineRotors));
        mach.convert('g');
        assertEquals("ABDA", getSetting(ac, machineRotors));
        mach.convert('b');
        assertEquals("ABDB", getSetting(ac, machineRotors));
        mach.convert('m');
        assertEquals("ABDC", getSetting(ac, machineRotors));
        mach.convert('n');
        assertEquals("ABAD", getSetting(ac, machineRotors));
    }

    /** Helper method to get the String
     * representation of the current Rotor settings */
    private String getSetting(Alphabet alph, Rotor[] machineRotors) {
        String currSetting = "";
        for (Rotor r : machineRotors) {
            currSetting += alph.toChar(r.setting());
        }
        return currSetting;
    }
}
