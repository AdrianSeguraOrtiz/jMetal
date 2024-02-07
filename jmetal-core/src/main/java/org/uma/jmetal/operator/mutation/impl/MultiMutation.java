/**
 * This class implements a multi-mutation operator in JMetal, allowing the application of four different mutation operators: Uniform Mutation, Polynomial Mutation, Linked Polynomial Mutation, and Non-Uniform Mutation.
 * The probability of each mutation operator being executed is determined by the probabilities pUniform, pPolynomial, pLinkedPolynomial, and pNonUniform respectively.
 */
package org.uma.jmetal.operator.mutation.impl;

import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.solution.doublesolution.repairsolution.RepairDoubleSolution;
import org.uma.jmetal.util.errorchecking.Check;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

public class MultiMutation implements MutationOperator<DoubleSolution> {
    private double probability;
    private RandomGenerator<Double> randomGenerator;
    private UniformMutation operatorUniform;
    private double pUniform;
    private PolynomialMutation operatorPolynomial;
    private double pPolynomial;
    private LinkedPolynomialMutation operatorLinkedPolynomial;
    private double pLinkedPolynomial;
    private NonUniformMutation operatorNonUniform;
    private double pNonUniform;

    /**
     * Constructor that initializes the multi-mutation operator.
     * @param probability Global mutation probability.
     * @param solutionRepair Solution repairer.
     * @param pUniform Probability of executing the Uniform Mutation operator.
     * @param pPolynomial Probability of executing the Polynomial Mutation operator.
     * @param pLinkedPolynomial Probability of executing the Linked Polynomial Mutation operator.
     * @param pNonUniform Probability of executing the Non-Uniform Mutation operator.
     * @param perturbationUniform Perturbation for the Uniform Mutation operator.
     * @param distributionIndexPolynomial Distribution index for the Polynomial Mutation operator.
     * @param distributionIndexLinkedPolynomial Distribution index for the Linked Polynomial Mutation operator.
     * @param perturbationNonUniform Perturbation for the Non-Uniform Mutation operator.
     * @param maxIterationsNonUniform Maximum number of iterations for the Non-Uniform Mutation operator.
     */
    public MultiMutation(
            double probability, 
            RepairDoubleSolution solutionRepair,
            double pUniform, 
            double pPolynomial, 
            double pLinkedPolynomial, 
            double pNonUniform,  
            double perturbationUniform, 
            double distributionIndexPolynomial,
            double distributionIndexLinkedPolynomial,
            double perturbationNonUniform, 
            int maxIterationsNonUniform
        ){

        this.probability = probability;
        this.randomGenerator = () -> JMetalRandom.getInstance().nextDouble();

        this.operatorUniform = new UniformMutation(probability, perturbationUniform, solutionRepair, randomGenerator);
        this.operatorPolynomial = new PolynomialMutation(probability, distributionIndexPolynomial, solutionRepair, randomGenerator);
        this.operatorLinkedPolynomial = new LinkedPolynomialMutation(probability, distributionIndexLinkedPolynomial, solutionRepair, randomGenerator);
        this.operatorNonUniform = new NonUniformMutation(probability, perturbationNonUniform, maxIterationsNonUniform, solutionRepair, randomGenerator);

        double sum = pUniform + pPolynomial + pLinkedPolynomial + pNonUniform;
        this.pUniform = pUniform / sum;
        this.pPolynomial = pPolynomial / sum;
        this.pLinkedPolynomial = pLinkedPolynomial / sum;
        this.pNonUniform = pNonUniform / sum;
    }

    /**
     * Executes the multi-mutation operator on a solution.
     * @param solution Solution to be mutated.
     * @return Mutated solution after applying the mutation operator.
     */
    @Override
    public DoubleSolution execute(DoubleSolution solution) {
        Check.notNull(solution);

        double r = randomGenerator.getRandomValue();
        if (r <= pUniform) {
            return operatorUniform.execute(solution);
        } else if (r <= pUniform + pPolynomial) {
            return operatorPolynomial.execute(solution);
        } else if (r <= pUniform + pPolynomial + pLinkedPolynomial) {
            return operatorLinkedPolynomial.execute(solution);
        } else {
            return operatorNonUniform.execute(solution);
        }
    }

    /**
     * Gets the global mutation probability.
     * @return The global mutation probability.
     */
    public double mutationProbability() {
        return probability;
    }

    /**
     * Gets the probability of executing the Uniform Mutation operator.
     * @return The probability of executing the Uniform Mutation operator.
     */
    public double pUniform() {
        return pUniform;
    }

    /**
     * Gets the probability of executing the Polynomial Mutation operator.
     * @return The probability of executing the Polynomial Mutation operator.
     */
    public double pPolynomial() {
        return pPolynomial;
    }

    /**
     * Gets the probability of executing the Linked Polynomial Mutation operator.
     * @return The probability of executing the Linked Polynomial Mutation operator.
     */
    public double pLinkedPolynomial() {
        return pLinkedPolynomial;
    }

    /**
     * Gets the probability of executing the Non-Uniform Mutation operator.
     * @return The probability of executing the Non-Uniform Mutation operator.
     */
    public double pNonUniform() {
        return pNonUniform;
    }
}
