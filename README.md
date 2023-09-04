# 🖋 Theoric background

The tool is based upon the Partial-Order Planner (POP), which refines a plan until it becomes executable and fulfill all the agent's goals. We start off by having a plan with just the initial and final situation, which will be improved by resolving its "flaws". In addition to the standard types of flaws, we propose three other types of flaws relative to regulativeNorms, and their resolvers.

`Missing obligation`

`Missing prohibition` 

`Missing permission`

This will allow agents to have a clear mean to comply / circumvent / violate a norm. 

# 🧪 Test model

To illustrate the type of problem this tool can solve, we propose a planning problelm for a single agent, named IRIELA, whose goal is to feed his family but is part of a lot of organizations : village.member, exploitation.farmer. 

Its possible actions are defined by the organizations it is a part of : 
- move
- cut
- fish
- getLicense

![alt text](https://github.com/tokyramarozaka/mimosa-iriela-extension/blob/master/iriela-overview.png)

# 🌐 Institutions

In the IRIELA planning problem, a norm MUST belong to some institution. It is then acquired by playing some role inside that institution by organizing which object plays which role and acquire which norms. This is called organization and is the core of our description about norms.

Using a simple, but verbose Kotlin-like syntax, all the institutions inside of the IRIELA planning problem.

![alt text](https://github.com/tokyramarozaka/mimosa-iriela-extension/blob/master/iriela-institutions-v.1.1.png)

# 👁‍🗨 Syntax overview

To showcase a better grasp of how a planning problem might look like through our upcoming Domain-Specific Language (DSL) which allow to craft such planning problems, here is a snippet of the whole planning problem, including all those who acuquire roles and the norms from the aformentionned institutions.

![alt text](https://github.com/tokyramarozaka/mimosa-iriela-extension/blob/master/iriela-planning-problem.png)

# 🎤 Contributions

Feel free to reach out to `jean-pierre.muller@cirad.fr` or `tokyramarozaka@gmail.com` if you have any feedback or comments on the current tool. Documentation is coming soon. 
