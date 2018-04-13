# ESTOCADA-CATALOG
This repository contains the contributions from the paper "Managing Polyglot Systems Metadata using Hypergraphs." This is an implementation of the catalog described in the above paper for the ESTOCATA polyglot system. Further details on the structure of the polyglot system are available [here](https://www.google.es/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0ahUKEwiz1trewLTaAhVIBywKHQ5kCgUQFggsMAA&url=http%3A%2F%2Fcidrdb.org%2Fcidr2015%2FPapers%2FCIDR15_Paper7.pdf&usg=AOvVaw3QPWteYnPDSLwdjdGXVpG7). (Bugiotti F, Bursztyn D, Deutsch A, Ileana I, Manolescu I. Invisible glue: scalable self-tuning multi-stores. InConference on Innovative Data Systems Research (CIDR) 2015 Jan 4.)

# How to run


1. Change HG_LOCATION variable to the path where you want the hypergraphdb to store the data (edu.upc.essi.catalog.constants.Const.java)
2. Run the run.java file. If the hypergraph data is not available, it will automatically create the database.
3.  Type in the Atom names that you want to generate the queries for separated by commas. Then you will be presented with the queries regarding where each of the data segments lies, in the native query languages.
4.  Type exit to exit the prompt

P.S: The code also contains query generation for wide-column stores. But, in ESTOCADA they do not use wide-column stores. Instead, they use a key-value store to store bus route information. Therefore, No project queries cannot be generated on them as it is a simple k-v structure.
