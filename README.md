# Design optimizer


# How to run
1. Install the following python libraries
```
pip install flask
pip install gekko
```
2. run the data/python/API.py to initialize the solver service
3. Run the workflow.java file. 
   - This will generate a random design and output the storage size and the query cost in "result" (CostResult.java)
   - If you use a transformation all the code except line 42 ( generator.main(args); ) need to be run
4. Two transformation functions are implemented in edu.upc.essi.catalog.ops.Transformations.
