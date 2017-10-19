# NMDFormats
This package provide functionality for using xml-formats defined by NMD in Java, for converting to hierarchical data model, and for extracting annotated documentation from xsd. 
An example for the data format "biotic" is included.

For adapting to other formats:
1. Make sure the xsd is annotated according to Commons - Documentation (see src/main/resources documentation.xsd). The appinfo annotations are the only one that are strictly required for relational conversion to work.
2. Set up jaxb bindings to make generated classes extend HierarchicalData.HierarchicalData (see src/main/bindings/bioticv_4.xjb for an example)
3. Implement a format specific handler for leaf nodes (interface: HierarchicalData.RelationalConversion.ILeafNodeHandler)

The generated classes provide generic navigation to parent object in the hiearchical model. If neither this or conversion to relational model is required, consider jaxb-bindings in https://git.imr.no/Formats/commons-biotic-jaxb.git for a simple pojo mapping that only allows for direct navigation to child objects.