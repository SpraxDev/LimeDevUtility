package de.sprax2013.lime.math;

import org.jetbrains.annotations.NotNull;

/**
 * @author Boann (https://stackoverflow.com/a/26227947/9346616)
 * @see #solve(String)
 */
public class MathSolver {
    private final String str;

    private int ch;
    private int pos = -1;

    private MathSolver(String str) {
        this.str = str;
    }

    /**
     * The given string is parsed and correctly calculated
     * <br><br>
     *
     * <b>Supported features (+ syntax)</b>
     * <ul>
     *     <li>Multiplication (<code>*</code>)</li>
     *     <li>Division (<code>/</code>)</li>
     *     <li>Addition (<code>+</code>)</li>
     *     <li>Subtraction (<code>-</code>)</li>
     *     <li>Exponentiation (<code>^</code>)</li>
     *     <li>Parentheses (<code>(</code> and <code>)</code>)</li>
     *     <li>Multiplication and division operators have precedence over the addition and subtraction operators</li>
     *     <li>Constants</li>
     *     <li>Functions (see below)</li>
     * </ul>
     * <br>
     *
     * <b>Supported constants:</b>
     * <ul>
     *     <li><code>PI</code></li>
     *     <li><code>E</code></li>
     * </ul>
     * <br>
     *
     * <b>Supported functions:</b>
     * <ul>
     *     <li><code>sqrt(a value)</code></li>
     *     <li><code>logN(a value)</code> where <code>N</code> is the base of the logarithm (positive integer)</li>
     *     <li><code>ln(a value)</code> (or <code>log(a value)</code>)</li>
     *     <li><code>lg(a value)</code></li>
     *     <li><code>sin(angle measured in degrees)</code></li>
     *     <li><code>cos(angle measured in degrees)</code></li>
     *     <li><code>tan(angle measured in degrees)</code></li>
     * </ul>
     * <br>
     *
     * <b>Examples</b>
     * <ol>
     *     <li><code>"2" // =&gt; 2</code></li>
     *     <li><code>"2*PI" // =&gt; 6.283185307179586</code></li>
     *     <li><code>"2*5 + 1" // =&gt; 11.0</code></li>
     *     <li><code>"log2(32)" // =&gt; 5.0</code></li>
     *     <li><code>"((4 - 2^3 + 1) * -sqrt(3*3 + 4*4)) / 2" // =&gt; 7.5</code></li>
     * </ol>
     *
     * @param str The mathematical term
     *
     * @return The value of the given term
     *
     * @see Math#sqrt(double)
     * @see Math#log(double)
     */
    @SuppressWarnings("UnusedReturnValue")
    public static double solve(@NotNull String str) {
        return new MathSolver(str.toLowerCase()).parse();
    }

    private double parse() {
        nextChar();
        double x = parseExpression();

        if (pos < str.length()) {
            throw new IllegalArgumentException("Unexpected character at index " + pos);
        }

        return x;
    }

    private void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ') {
            nextChar();
        }

        if (ch == charToEat) {
            nextChar();

            return true;
        }

        return false;
    }

    // Grammar:
    // expression = term | expression `+` term | expression `-` term
    // term = factor | term `*` factor | term `/` factor
    // factor = `+` factor | `-` factor | `(` expression `)`
    // | number | functionName factor | factor `^` factor

    private double parseExpression() {
        double x = parseTerm();

        for (; ; ) {
            if (eat('+')) {
                x += parseTerm(); // addition
            } else if (eat('-')) {
                x -= parseTerm(); // subtraction
            } else {
                return x;
            }
        }
    }

    private double parseTerm() {
        double x = parseFactor();

        for (; ; ) {
            if (eat('*')) {
                x *= parseFactor(); // multiplication
            } else if (eat('/')) {
                x /= parseFactor(); // division
            } else {
                return x;
            }
        }
    }

    private double parseFactor() {
        if (eat('+')) {
            return parseFactor(); // unary plus
        }
        if (eat('-')) {
            return -parseFactor(); // unary minus
        }

        double x;
        int startPos = this.pos;
        if (eat('(')) { // parentheses
            x = parseExpression();
            eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
            while ((ch >= '0' && ch <= '9') || ch == '.') {
                nextChar();
            }

            x = Double.parseDouble(str.substring(startPos, this.pos));
        } else if (ch >= 'a' && ch <= 'z') { // functions
            while ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) {
                nextChar();
            }

            String func = str.substring(startPos, this.pos);
            switch (func) {
                /* constants */
                case "sqrt":
                    x = Math.sqrt(parseFactor());
                    break;

                case "log":
                case "ln":
                    x = Math.log(parseFactor());
                    break;
                case "lg":
                    x = Math.log10(parseFactor());
                    break;

                case "sin":
                    x = Math.sin(Math.toRadians(parseFactor()));
                    break;
                case "cos":
                    x = Math.cos(Math.toRadians(parseFactor()));
                    break;
                case "tan":
                    x = Math.tan(Math.toRadians(parseFactor()));
                    break;

                /* constants */
                case "pi":
                    x = Math.PI;
                    break;
                case "e":
                    x = Math.E;
                    break;

                default:
                    /* function logN */
                    if (func.startsWith("log")) {
                        String nStr = func.substring(3);

                        try {
                            int n = Integer.parseInt(nStr);

                            x = parseFactor();

                            if (n == 10) {
                                x = Math.log10(x);
                            } else {
                                x = Math.log(x) / Math.log(n);
                            }

                            break;
                        } catch (NumberFormatException ex) {
                            throw new IllegalArgumentException("Invalid value for N in logN: " + nStr);
                        }
                    }

                    throw new IllegalArgumentException("Unknown function: " + func);
            }
        } else {
            throw new IllegalArgumentException("Unexpected character at index " + pos);
        }

        if (eat('^')) {
            x = Math.pow(x, parseFactor()); // exponentiation
        }

        return x;
    }
}