package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Vidhi Chander
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine M = readConfig();
        String next;
        ArrayList<Rotor> machineRotors;
        String line = "";
        while (_input.hasNextLine()) {
            next = _input.nextLine();
            if (next.equals("")) {
                _output.println();
            } else {
                if (next.charAt(0) == '*') {
                    setUp(M, next);
                } else {
                    next = next.toUpperCase();
                    for (char c : next.toCharArray()) {
                        if (_alphabet.contains(c)) {
                            line += c;
                        }
                    }
                    machineRotors = M.machineRotors();
                    if (machineRotors.size() == 0) {
                        throw error("No configuration");
                    } else if (!(machineRotors.get(0).reflecting())) {
                        throw error(("Reflector in wrong place"));
                    } else {
                        printMessageLine(M.convert(line));
                        line = "";
                    }
                }
            }
        }


    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            ArrayList<Rotor> allRotor = new ArrayList<Rotor>();
            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
            int numRotors = 1;
            int pawls = 0;
            char begin;
            char end;
            if (_config.hasNextLine()) {
                String s = _config.next();
                begin = s.charAt(0);
                end = s.charAt(s.length() - 1);
                _alphabet = new CharacterRange(begin, end);
            }
            if (_config.hasNextLine()) {
                numRotors = _config.nextInt();
                pawls = _config.nextInt();
            }
            while (_config.hasNext()) {
                allRotor.add(readRotor());
            }

            for (Rotor s : allRotor) {
                if (s != null) {
                    allRotors.add(s);
                }
            }
            return new Machine(_alphabet, numRotors, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = "";
            if (_config.hasNext()) {
                name = _config.next();
            } else {
                return null;
            }
            String hold = "";
            char type = 'A';
            String notches = "";
            String cycles = "";

            if (_config.hasNext()) {
                hold = _config.next();
                int len = hold.length();
                if (len > 0) {
                    type = hold.charAt(0);
                    if (len > 1) {
                        notches = hold.substring(1, len);
                    }

                }
            }
            while (_config.hasNext("\\(.+\\)")) {
                cycles += _config.next();
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            if (type == 'M') {
                return new MovingRotor(name, perm, notches);
            } else if (type == 'N') {
                return new FixedRotor(name, perm);
            } else if (type == 'R') {
                return new Reflector(name, perm);
            } else {
                throw new NoSuchElementException();
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] rotors = new String[M.numRotors()];
        Scanner set = new Scanner(settings);
        set.next();
        String plug = "";
        for (int i = 0; i < M.numRotors(); i++) {
            if (set.hasNext()) {
                rotors[i] = set.next();
            }
        }
        boolean error = false;
        int rep = 0;
        int numPawls = 0;
        for (String s : rotors) {
            for (Rotor r : M.allRotors()) {
                if (s.equalsIgnoreCase(r.name())) {
                    error = true;
                    if (r.rotates()) {
                        numPawls += 1;
                    }
                }
            }
            for (String repeat : rotors) {
                if (s.equalsIgnoreCase(repeat)) {
                    rep = rep + 1;
                }
            }
            rep = 0;
            if (!error) {
                throw error("Bad rotor name");
            }
            error = false;
        }
        if (rep > 1) {
            throw error("Duplicate rotor name");
        }
        if (numPawls != M.numPawls()) {
            throw error("Wrong number of arguments");
        }
        M.insertRotors(rotors);

        String setting;
        if (set.hasNext()) {
            setting = set.next();
            if (!(setting.length() == M.numRotors() - 1)) {
                throw error("Wheel settings too short");
            } else {
                M.setRotors(setting);
            }
        }

        while (set.hasNext()) {
            plug += set.next() + " ";
        }
        if (!plug.equals("")) {
            M.setPlugboard(new Permutation(plug.substring(0,
                    plug.length() - 1), _alphabet));
        }

    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        int length = msg.length();
        String output = "";
        while (length > 0) {
            if (length <= 5) {
                output += msg.substring(0, length);
                _output.println(output);
                length = 0;
                output = "";
            } else {
                output += msg.substring(0, 5) + " ";
                msg = msg.substring(5, length);
                length = msg.length();
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Collection of all rotors. */

}
