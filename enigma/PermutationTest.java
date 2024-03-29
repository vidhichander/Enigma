package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkSize() {
        perm = new Permutation("(AELTPHQXRU) "
                + "(BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        assertEquals(alpha.length(), perm.size());
    }

    @Test
    public void checkPermute() {
        perm = new Permutation("(AELTPHQXRU) "
                + "(BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        assertEquals(0, perm.permute(20));
        assertEquals(13, perm.permute(10));
        assertEquals(18, perm.permute(18));

    }

    @Test
    public void checkInvert() {
        perm = new Permutation("(AELTPHQXRU) "
                + "(BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        assertEquals(20, perm.invert(0));
        assertEquals(10, perm.invert(13));
        assertEquals(18, perm.invert(18));
    }

    @Test
    public void testInvertChar() {
        Permutation p = new Permutation("(PNH) "
                + "(ABDFIKLZYXW) (JC)", new CharacterRange('A', 'Z'));
        assertEquals(p.invert('B'), 'A');
        assertEquals(p.invert('G'), 'G');
        assertEquals(p.invert('P'), 'H');
        assertEquals(p.invert('J'), 'C');
    }

    @Test
    public void testPermuteChar() {
        Permutation p = new Permutation("(PNH) "
                + "(ABDFIKLZYXW) (JC)", new CharacterRange('A', 'Z'));
        assertEquals(p.permute('W'), 'A');
        assertEquals(p.permute('G'), 'G');
        assertEquals(p.permute('P'), 'N');
        assertEquals(p.permute('J'), 'C');
    }









}
