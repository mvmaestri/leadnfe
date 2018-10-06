# @Library('leadnfe') _

<img
src="https://user-images.githubusercontent.com/875669/35621130-2acb1e78-0638-11e8-8777-0f56edc79c32.png"
height=48 width=48 alt="Jenkins CI logo" />

The official documentation [can be found here](https://jenkins.io/doc/book/pipeline/shared-libraries/). It thoroughly describes the general concept, but some details may not be so apparent when trying to pull pieces of your script code into a library.

The most important part here is also mentioned in the official doc: such libraries are considered **trusted**. There might be a chance that if you developed pipelines you’d run into problems when calling internal Jenkins or Java/Groovy APIs, where Jenkins failed the pipeline saying something about the sandbox, rejection, lack of permissions, etc.

This is because pipelines scripts kept, e.g., directly in the job (which is one of the ways to debug parts of your pipelines) are run in a Groovy sandbox that is the cause of all these ‘pleasures’.