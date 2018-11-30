# >(G)< SCPIGW >(G)<
## SAP Cloud Platform Integration Groovy Wrapper
### Introduction
SCPIGW is a groovy class whose aim is to simplify and clarify the interface between SAP Cloud Platform Integration (SCPI) and the Apache Camel Framework its running on. The interface between these two platforms can be confusing and its easy to lose track of whats happening in complicated integration scenarios. SCPIGW helps by creating a more consistent and human readable interface to the SCPI platform as a whole.

The other aim of SCPIGW is to add features to the SCPI work flow that increase developer productivity. The biggest feature of SCPIGW is enabling the local testing of groovy scripts that can be moved to the SCPI platform with little to no modification. The current state of script Testing on SCPI is terrible. As an integration grows in size, the deploy times increase which greatly impact a developers ability to write groovy scripts quickly and efficiently. SCPIGW also provides a nice logging feature that can be used during development, and enabled/disabled using externalized variables.

### Motivation
When working with groovy scripts, a common design pattern is the following:

1. Content Modifier Step: Typically used to pull in externalized variables or persistent variables that the groovy script needs to access.
2. Script Step: The actual groovy script runs, using variables set in the content modifier step. The groovy script usually sets message property variables before it exits to be used in a write variable step.
3. Write Variables Step: Properties written by the groovy script are then written to NEW variables to be persisted for later runs of the integration.

The issues with this design pattern are first that all three of these steps can be done inside the groovy script, but second that the variables are being replicated which leads to bugs or integrations that fail.

I suspect that the reasons I see this design pattern are because there is no guide on how to perform all of these operations from a groovy script, and second that the naming of all of these operations are extremely convoluted due to the SCPI Message Class being used on the Apache Camel Framework.

By eliminating this design pattern and rolling these three steps all into the script step, integrations are much easier to follow. This drastically reduces the sizes of integrations and makes scripts easier to understand. Everything that a script deals with is actually used in the script, and not spread out into other steps of the integration using replicated variables.

### Usage and Architecture
SCPIGW works by taking the SAP Message Class and then getting then getting the exchange and camel context that the message is inside. With these Apache Camel Objects, SCPIGW is able to perform operations that the message class cannot. When the script is complete, SCPIGW is able to save its self inside the camel context and be loaded into any script step that will be run later in the integration. This gives SCPIGW some nice logging features and makes scripts easier to set up and more consistent.
