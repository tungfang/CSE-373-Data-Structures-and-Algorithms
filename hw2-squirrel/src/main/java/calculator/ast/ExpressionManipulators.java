package calculator.ast;

import calculator.interpreter.Environment;
import calculator.errors.EvaluationError;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
// import misc.exceptions.NotYetImplementedException;

/**
 * All of the public static methods in this class are given the exact same parameters for
 * consistency. You can often ignore some of these parameters when implementing your
 * methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private
 * pairs in some cases.
 */
public class ExpressionManipulators {
    /**
     * Checks to make sure that the given node is an operation AstNode with the expected
     * name and number of children. Throws an EvaluationError otherwise.
     */
    private static void assertNodeMatches(AstNode node, String expectedName, int expectedNumChildren) {
        if (!node.isOperation()
                && !node.getName().equals(expectedName)
                && node.getChildren().size() != expectedNumChildren) {
            throw new EvaluationError("Node is not valid " + expectedName + " node.");
        }
    }

    /**
     * Accepts an 'toDouble(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'toDouble'.
     * - The 'node' parameter has exactly one child: the AstNode to convert into a double.
     *
     * Postconditions:
     *
     * - Returns a number AstNode containing the computed double.
     *
     * For example, if this method receives the AstNode corresponding to
     * 'toDouble(3 + 4)', this method should return the AstNode corresponding
     * to '7'.
     * 
     * This method is required to handle the following binary operations
     *      +, -, *, /, ^
     *  (addition, subtraction, multiplication, division, and exponentiation, respectively) 
     * and the following unary operations
     *      negate, sin, cos
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    public static AstNode handleToDouble(Environment env, AstNode node) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the locations specified by "your code here"
        // in the 'toDoubleHelper' method.
        //
        // If you're not sure why we have a public method calling a private
        // recursive helper method, review your notes from CSE 143 (or the
        // equivalent class you took) about the 'public-private pair' pattern.

        assertNodeMatches(node, "toDouble", 1);
        AstNode exprToConvert = node.getChildren().get(0);
        return new AstNode(toDoubleHelper(env.getVariables(), exprToConvert));
    }

    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        if (node.isNumber()) { // Base case: node is a number node.
            return node.getNumericValue(); // cast to double
        } else if (node.isVariable()) { // Base case: node is a variable node.
            String name = node.getName();
            if (!isDefined(variables, node)) { // case => variable is undefined: throw Evaluation error
                throw new EvaluationError("Variable " + name + " is undefined");
            } else { // case => variable is defined: return the value of variable
                AstNode definedVar = variables.get(name);
                return toDoubleHelper(variables, definedVar);
            }
        } else { // Recursive case: node is an operation node
            String name = node.getName();
            // You may assume the expression node has the correct number of children.
            // If you wish to make your code more robust, you can also use the provided
            // "assertNodeMatches" method to verify the input is valid.
            AstNode value1 = node.getChildren().get(0);
            if (node.getChildren().size() == 2) {
                AstNode value2 = node.getChildren().get(1);
                if (name.equals("+")) {
                    // operate plus on the children;
                    return toDoubleHelper(variables, value1) + toDoubleHelper(variables, value2);
                } else if (name.equals("-")) {
                    // operate subtract on the children
                    return toDoubleHelper(variables, value1) - toDoubleHelper(variables, value2);
                } else if (name.equals("*")) {
                    // operate multiplication on the children
                    return toDoubleHelper(variables, value1) * toDoubleHelper(variables, value2);
                } else if (name.equals("/")) {
                    // operate divide on the children
                    return toDoubleHelper(variables, value1) / toDoubleHelper(variables, value2);
                } else if (name.equals("^")) {
                    // operate exponential on the children
                    return Math.pow(toDoubleHelper(variables, value1), toDoubleHelper(variables, value2));
                }
            }
            if (name.equals("negate")) {
                // operate negate on the children
                return -1.0 * toDoubleHelper(variables, value1);
            } else if (name.equals("sin")) {
                // operate sin on the single children
                return Math.sin(toDoubleHelper(variables, value1));
            } else if (name.equals("cos")){ // operate cosine on the single children
                return Math.cos(toDoubleHelper(variables, value1));
            } else {
                throw new EvaluationError("Operation " + name + " is undefined");
            }
        }
    }

    /**
     * Accepts a 'simplify(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'simplify'.
     * - The 'node' parameter has exactly one child: the AstNode to simplify
     *
     * Postconditions:
     *
     * - Returns an AstNode containing the simplified inner parameter.
     *
     * For example, if we received the AstNode corresponding to the expression
     * "simplify(3 + 4)", you would return the AstNode corresponding to the
     * number "7".
     *
     * Note: there are many possible simplifications we could implement here,
     * but you are only required to implement a single one: constant folding.
     *
     * That is, whenever you see expressions of the form "NUM + NUM", or
     * "NUM - NUM", or "NUM * NUM", simplify them.
     */
    public static AstNode handleSimplify(Environment env, AstNode node) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        //         to your "handleToDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        //         to call your "handleToDouble" method in some way
        // Hint 3: When implementing your private pair, think carefully about
        //         when you should recurse. Do you recurse after simplifying
        //         the current level? Or before?

        assertNodeMatches(node, "simplify", 1);
        AstNode exprToSimplify = node.getChildren().get(0);
        return handlesSimplifyHelper(env.getVariables(), exprToSimplify); // Returns the simplified Ast tree
    }

    private static AstNode handlesSimplifyHelper(IDictionary<String, AstNode> variables, AstNode node) {
        // We want to simplify when the left and right nodes are numbers or defined variable => call toDouble helper
        // We want to leave the expression when the nodes are undefined variable
        // We want to keep stepping down if the operation nodes have operational leaves.
        // base case if the given node is a num
        if (node.isNumber() || (node.isVariable() && !isDefined(variables, node))) {
            return node;
        } else if (node.isVariable() && isDefined(variables, node)) { // base case if the given node is a var
            return handlesSimplifyHelper(variables, variables.get(node.getName()));
        } else {
            String operation = node.getName();
            // Recurse to get the bottom left node
            AstNode left = handlesSimplifyHelper(variables, node.getChildren().get(0));
            // every node is guaranteed to have at least one child so we could use .get(0) to recurse
            IList<AstNode> newChildren = new DoubleLinkedList<>();
            // save it to the new list for further operation
            // or just return a num if there isn't any operation to do
            newChildren.add(left);
            // store that list to a new node for return
            AstNode res = new AstNode(operation, newChildren);
            if (toSimplifyOperator(operation) || operation.equals("/")) {
                // If node is an operator. It then has two node so we have to recurse to the bottom right as well
                AstNode right = handlesSimplifyHelper(variables, node.getChildren().get(1));
                // add the bottom right node to the list
                newChildren.add(right);
                // if two nodes are num and the operation is able to simply
                // return the new node with the new calculated value
                if (left.isNumber() && right.isNumber() && toSimplifyOperator(operation)) {
                    return new AstNode(toDoubleHelper(variables, res));
                }
            }
            return res;
        }
    }

    // Test and return if given operation should be further evaluated.
    // Note: only if it can get an exact result and we do not attempt to simplify division.
    private static boolean toSimplifyOperator(String operation) {
        return operation.equals("+") || operation.equals("-") || operation.equals("*") || operation.equals("^");
    }

    // Helper method that checks if a given node variable is defined
    private static boolean isDefined(IDictionary<String, AstNode> variables, AstNode node) {
        return node.isVariable() && variables.containsKey(node.getName());
    }

    /**
     * Accepts an Environment variable and a 'plot(exprToPlot, var, varMin, varMax, step)'
     * AstNode and generates the corresponding plot on the ImageDrawer attached to the
     * environment. Returns some arbitrary AstNode.
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This method will receive the AstNode corresponding to 'plot(3 * x, x, 2, 5, 0.5)'.
     * Your 'handlePlot' method is then responsible for plotting the equation
     * "3 * x", varying "x" from 2 to 5 in increments of 0.5.
     *
     * In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4
     * 4
     * >>> step := 0.01
     * 0.01
     * >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if varMin > varMax
     * @throws EvaluationError  if 'var' was already defined
     * @throws EvaluationError  if 'step' is zero or negative
     */
    public static AstNode plot(Environment env, AstNode node) {
        assertNodeMatches(node, "plot", 5);
        IDictionary<String, AstNode> variables = env.getVariables();
        AstNode exprToPlot = node.getChildren().get(0);
        String var = node.getChildren().get(1).getName();
        double varMin = toDoubleHelper(variables, node.getChildren().get(2));
        double varMax = toDoubleHelper(variables, node.getChildren().get(3));
        double step = toDoubleHelper(variables, node.getChildren().get(4));
        if (varMin > varMax) {
            throw new EvaluationError("Min is greater than max");
        } else if (isDefined(variables, node.getChildren().get(1))) {
            throw new EvaluationError("Var is already defined");
        } else if (step <= 0) {
            throw new EvaluationError("Step is zero or negative");
        }
        IList<Double> xSet = new DoubleLinkedList<>();
        IList<Double> ySet = new DoubleLinkedList<>();
        for (double xVal = varMin; xVal <= varMax; xVal += step) {
            xSet.add(xVal);
            variables.put(var, new AstNode(xVal)); // Defines the plotting variable
            // Throws EvaluationError if any other variable is undefined
            ySet.add(toDoubleHelper(variables, exprToPlot));
        }
        variables.remove(var); // Ensure var is removed from variables dictionary after plot
        // Note: every single function we add MUST return an
        // AST node that your "simplify" function is capable of handling.
        // However, your "simplify" function doesn't really know what to do
        // with "plot" functions (and what is the "plot" function supposed to
        // evaluate to anyways?) so we'll settle for just returning an
        // arbitrary number.
        //
        // When working on this method, you should uncomment the following line:
        env.getImageDrawer().drawScatterPlot("plot", "x", "output", xSet, ySet);
        return new AstNode(1);
    }
}
