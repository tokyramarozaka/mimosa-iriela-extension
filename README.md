# 🔎 Overview
A normative extension of the agent-based modeling platform MIMOSA. This extension aims at agents to be able to represent and reason about norms which can be : Regulative Norms, or Constitutive Norms. Regulative Norms tells the agent what ought to (not) be done in certain situations, while Constitutive Norms binds existing resources to concepts via a "count as" relation. 

In this project we have 3 types of Regulative Norms : obligations, prohibitions, and permission according to Ostrom &amp; Crawford. 

The end-goal is to be able to describe a planning problem with norms with a dedicated language (DOMAIN-SPECIFIC LANGUAGE), which are endowed by its organizations through the various roles that the agent must fulfill, and see how it affects its behavior. 

We use automated planning internally to resolve the planning problem afterwards which is extended to account for norms.

# ⚙ Tools

This extension requires the following tools : 

`Java 11 or higher`

`Maven`

`Lombok`

`JUnit 5`

# 🖋 Theoric background

The tool is based upon the Partial-Order Planner (POP), which refines a plan until it becomes executable and fulfill all the agent's goals. We start off by having a plan with just the initial and final situation, which will be improved by resolving its "flaws". In addition to the standard types of flaws, we propose three other types of flaws relative to regulativeNorms, and their resolvers.

`Missing obligation`

`Missing prohibition` 

`Missing permission`

This will allow agents to have a clear mean to comply / circumvent / violate a norm. 

# 🧪 Test model

To illustrate the type of problem this tool can solve, we propose a toy model on a single agent, whose goal is to feed his family. Its possible actions are defined by the organizations it is a part of : 
- hunt
- harvest
- cut 
- move

![alt text](https://github.com/tokyramarozaka/mimosa-extension/blob/master/Proof%20of%20concept.drawio%20(5).png)

# 🎤 Contributions

Feel free to reach out to `jean-pierre.muller@cirad.fr` or `tokyramarozaka@gmail.com` if you have any feedback or comments on the current tool. Documentation is coming soon. 
