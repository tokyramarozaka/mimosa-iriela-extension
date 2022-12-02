# 🔎 Overview
A normative extension of the agent-based modeling platform MIMOSA. This extension aims at agents to be able to represent and reason about regulativeNorms. We categorize regulativeNorms in 3 types : obligations, prohibitions, and permission according to Ostrom &amp; Crawford. 

The end-goal is to write a planning problem, with a set of institutions and organizations which endows regulativeNorms to its members, and see how they affect the agent's behavior. 

# ⚙ Tools

This extension requires : 

`Java 11 or higher`

`Maven`

`Lombok`

`JUnit 5`

# 🖋 Theoric background

The tool is based upon the Partial-Order Planner (POP), which refines a plan until it becomes executable. In addition to the standard types of flaws, we propose three other types of flaws relative to regulativeNorms, and their resolvers.

`Missing obligation`

`Missing prohibition` 

`Missing permission`

# 🎤 Contributions

Feel free to reach out to `jean-pierre.muller@cirad.fr` or `tokyramarozaka@gmail.com` if you have any feedback or comments on the current tool. Documentation is coming soon. 
