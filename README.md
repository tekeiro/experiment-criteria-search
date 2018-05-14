IJPAQuery
--------------
 * buildQueryStr() : String
 * buildCountQueryStr() : String
 * performQuery() : List<T>
 * performCountQuery() : Long
 * buildQueryReturnsObject(objectResultantClass: Class<T>, String... selectColumns) : List<T>
 * buildQueryAsMap(String... selectColumns) : List<Map<String, Object>>


 ICriteriaSet
 ---------------
   * setOrderBy(String... orderBy) : this
   * setLimit(int maxResults) : this
   * setFirstResult(int firstResult) : this
   * setGroups(String... groupings) : this
   * addJoins(String... joins)


 I  implements ICriteriaSet
 ----------------------
  Boolean ---
  	* is_True
  		%f = TRUE
  	* is_False
  		%f = FALSE
  	* is_Null
  		%f IS NULL
  	* is_NotNull
  		%f IS NOT NULL

  Logic ----
   * with_Gt (val)
   		%f > val
   * with_Lt (val)
   		%f < val
   * with_GtEq (val)
   		%f >= val
   * with_LtEq (val)
   		%f <= val
   * with_Eq (val)
   		%f = val
   * with_NotEq (val)
   		%f <> val

  Numbers ---
   * with_InRange  (val1, val2)
   		%f BETWEEN val1 AND val2

  Containment ---
  	* with_In  (val1 : Array|Iterable|T...|Collection)
  		%f IN (item,)
  	* with_NotIn (val1 : Array|Iterable|T...|Collection)
  		%f NOT IN (item,)

  String ---
  	* with_StartsWith (val1)
  		%f LIKE 'VAL1%'
  	* with_EndsWith (val1)
  		%f LIKE '%VAL1'
  	* with_Containing (val1)
  		%f LIKE '%VAL1%'

  Dates ---
  	* with_DatesBetween (Date1, Date2)
  		%f BETWEEN date1 AND date2



Annotations
-----
  ###  Class
  
    @DbEntity(value(nombreEntidad) : String, letter : String, defaultJoins: String[]?)
    	Examples: @DbEntity(value="Libro", letter="l", defaultJoins="LEFT JOIN l.genero g")

  ### Methods
  	@DbField(value(nombreColumna) : String)
  		Examples: @DbField("l.genero")
  	@DbPropertyPath(value(rutaPropiedad) : String)
  		Examples: @DbPropertyPath("l.id")



Clase CriteriaData
------------------

 * criterio : String
 * dbColumna : String
 * operation : OperationSupported
 * propertyPath : String
 
 
