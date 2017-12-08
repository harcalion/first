package es.upm.fi.gtd.first.janet;




/**
 * Class <B>Matrix</B> is a class implementing matrices
 * and a number of useful methods operating on them.
 * @author Tue Lehn-Schi&oslash;ler 
 * @author Mads Ulrik Mogensen
 * @author Carsten Knudsen
 **/
public class Matrix implements MatrixType {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Instance fields
    private static Vector vector = new Vector( 1 );
        
    /**
     * The double array val contains the <B>Matrix</B> elements.
     * Should never be addressed directly;
     * all access through the methods set and get.
     * @see Matrix#set
     * @see Matrix#get
     **/
    private double[][] val;
    /**
     * The number of rows in the <B>Matrix</B>.
     **/
    private int row;
    /**
     * The number of columns in the <B>Matrix</B>.
     **/
    private int col;
    
    // Constructors 

    /**
     * Creates <B>Matrix</B> from a double[][]
     **/
    public Matrix( double[][] a ) { 
	this.row = a.length; 
	this.col = a[ 0 ].length;
	this.val = new double[ row ][ col ];
	for (int i = 1; i <= row; i++)
	    for(int j = 1; j <= col; j++)
		set( i, j, a[ i - 1 ][ j - 1 ] );
    }
    
    /**
     * Creates <B>Matrix</B> from <B>Matrix</B>
     **/
    
    public Matrix( Matrix a ) {
	this.row = a.row; 
	this.col = a.col;
	val = new double[ row ][ col ];
	for (int i = 1;i <= a.row; i++)
	    for(int j = 1; j <= a.col; j++)
		set( i, j, a.get( i, j ) );
    }
    
    /**
     * Creates a row*col <B>Matrix</B> with val in all fields   
     **/
    
    public Matrix( int row, int col, double value ) {
	this.row = row;
	this.col = col;
	this.val = new double[ row ][ col ];
	for (int i = 1; i <= row; i++)
	    for(int j = 1; j <= col; j++)
		set( i, j, value );
	
    }
    
    /**
     * Creates a row*col Matrix with all fields initialized to 0.
     **/
    
    public Matrix( int row, int col ) {
	this( row, col, 0 );
    }
    
    // Class Methods


    //
    // Implementation of MatrixType interface
    //

    /**
     *
     **/
    public MatrixType getMatrixType( int n, int m ) {
	return new Matrix( n, m );
    }
    
    /**
     *
     **/
    public MatrixType getMatrixType( double[][] m ) {
	return new Matrix( m );
    }
    
    /**
     *
     **/
    public MatrixType getMatrixType( MatrixType m ) {
	return new Matrix( m.getArray() );
    }
    
    /**
     *
     **/
    public int getRowDimension() {
	return row;
    }

    /**
     *
     **/
    public int getColumnDimension() {
	return col;
    }

    /**
     *
     **/
    public VectorType getVectorType() {
	return vector;
    }
    

    // this is support for NumericalRecipes only;  it will be removed later;
    public int getRowNum() {
	return row;
    }
    public int getColNum() {
	return col;
    }

    /**
     *
     **/
    public MatrixType add( MatrixType a, MatrixType b ) {
	return Matrix.add( (Matrix)getMatrixType( a ), (Matrix)getMatrixType( b ) );
    }

    /**
     *
     **/
    public MatrixType sub( MatrixType a, MatrixType b ) {
	return Matrix.sub( (Matrix)getMatrixType( a ), (Matrix)getMatrixType( b ) );
    }

    /**
     *
     **/
    public MatrixType mul( MatrixType a, MatrixType b ) {
	return Matrix.mul( (Matrix)getMatrixType( a ), (Matrix)getMatrixType( b ) );
    }

    /**
     *
     **/
    public VectorType mul( MatrixType a, VectorType x ) {
	Vector y = new Vector( x.getArray() );
	return Matrix.mul( (Matrix)getMatrixType( a ), y );
    }
    
    /**
     *
     **/
    public MatrixType mul( MatrixType a, double c ) {
	return ( new Matrix( a.getArray() ) ).mul( c );
    }

    /**
     * Returns a reference to the actual double array holding the matrix elements.
     **/
    public double[][] getArray() {
    	return val;
    }

    /**
     *
     **/
    public MatrixType unit( int n ) {
	return Matrix.unit( n, true );
    }

    /**
     *
     **/
    public VectorType solve( MatrixType m, VectorType x ) {
	return Matrix.solveLinearSystem( (Matrix)getMatrixType( m ), 
					 new Vector( x.getArray() ) );
    }

    /**
     * The method returns the determinant of the MatrixType.
     **/
    public double det( MatrixType m ) {
	es.upm.fi.gtd.first.jama.Matrix M = new es.upm.fi.gtd.first.jama.Matrix( m.getArray() );
	return M.det();
    }

    /**
     * The method returns the inverse of the MatrixType.
     **/
    public MatrixType inverse( MatrixType m ) {
	es.upm.fi.gtd.first.jama.Matrix M = new es.upm.fi.gtd.first.jama.Matrix( m.getArray() );
	return new Matrix( M.inverse().getArray() );
    }

    //
    // End of implementation of MatrixType interface
    //

    /**
     * Checks whether the matrices a and b have identical dimensions.
     * If checkEqualSize returns true addition and subtraction of
     * matrices are valid operations. 
     * If false is returned most methods invoking checkEqualSize
     * will issue a runtime exception.
     **/
    public static boolean checkEqualSize(Matrix a, Matrix b) {
	return ( a.getRowDimension() == b.getRowDimension() && 
	     a.getColumnDimension() == b.getColumnDimension() );
    }
    
    /**
     * Adds the matrices a and b and returns the sum.
     * The dimensions of the matrices must be identical.
     * @throws RuntimeException if dimensions of matrices are not identical.
     **/
    public static Matrix add(Matrix a,Matrix b) {
	if ( checkEqualSize( a, b ) ) {
	    Matrix c = new Matrix( a );
	    for (int i = 1; i <= a.getRowDimension(); i++) {
		for(int j = 1; j <= a.getColumnDimension(); j++) {
		    c.set( i, j, a.get( i, j ) + b.get( i, j ) );}
	    }
	    return c;
	}
	else
	    throw new RuntimeException("Dimension error in Matrix add.");
    }
    
    /**
     * Subtracts the matrices a and b and returns the difference.
     * The dimensions of the matrices must be identical.
     * @throws RuntimeException if dimensions of matrices are not identical.
     **/
    public static Matrix sub( Matrix a, Matrix b ) {
	if ( checkEqualSize( a, b ) ) {
	    Matrix c = new Matrix( a );
	    for (int i = 1; i <= a.getRowDimension(); i++) {
		for(int j = 1; j <= a.getColumnDimension(); j++) {
		    c.set( i, j, a.get( i, j ) - b.get( i, j ) );}
	    }
	    return c;
	}
	else
	    throw new RuntimeException("Dimension error in Matrix sub.");	
    }
    
    /**
     * Multiplies the matrices a and b and returns the product.
     * The number of columns in a must equal the number of rows
     * in b for matrix multiplication to be valid.
     * @throws RuntimeException if the number of columns in a differs
     * from the number of rows in b.
     **/
    public static Matrix mul( Matrix a, Matrix b ) {
	int rows = a.getRowDimension();
	int cols = b.getColumnDimension();
	double temp = 0.0;
	if ( a.getColumnDimension() == b.getRowDimension() ) {
	    Matrix c = new Matrix( rows, cols, 0 );		
	    for (int i = 1; i <= rows; i++) {
		for (int k = 1; k <= cols; k++) {
		    temp=0.0;
		    for (int j = 1; j <= a.getColumnDimension(); j++) {
			temp += ( a.get( i, j ) * b.get( j, k ) );
		    }
		    c.set( i, k, temp );	
		}
	    }			
	    return c;
	}
	else	
	    throw new RuntimeException("Dimension error in Matrix mul.");	
    }
    
    /**
     * Multiplies the <B>Vector</B> a and the <B>Matrix</B> b, and returns the product.
     * The length of the <B>Vector</B> must be identical to the number
     * of rows in the <B>Matrix</B>.
     * @throws RuntimeException if the length of the <B>Vector</B> differs from
     * the number of rows in the <B>Matrix</B>.
     **/
    public static Vector mul( Vector a, Matrix b ) {
	int length = a.length();
	int cols = b.getColumnDimension();
	int rows = b.getRowDimension();
	double temp = 0.0;
	if ( length == rows ) {
	    Vector c = new Vector( cols, 0 );		
	    for (int i = 1; i <= cols; i++) {
		temp=0;
		for  (int k = 1; k <= rows; k++) {
		    temp += a.get( i  ) * b.get( 1, k );
		}
		c.set( i, temp );
	    }			
	    return c;
	}
	else	
	    throw new RuntimeException("Dimension error in Vector-Matrix mul.");	
    }
    
    /**
     * Multiplies the <B>Matrix</B> a and the <B>Vector</B> b, and returns the product.
     * The number of columns in the <B>Matrix</B> and the length
     * of the <B>Vector</B> must coincide.
     * @throws RuntimeException if the number of columns in the <B>Matrix</B> 
     * and the length of the <B>Vector</B> differ.
     **/
    public static Vector mul( Matrix a, Vector b ) {
	int rows = a.getRowDimension();
	int cols = a.getColumnDimension();
	int length = b.length();
	double temp;
	if ( cols == length ) {
	    Vector c = new Vector( rows, 0 ); // corrected CK 07082002 rows was length
	    for (int i = 1; i <= rows; i++) {
		temp = 0.0;
		for  (int j = 1; j <= cols; j++) {
		    temp += ( a.get( i, j ) * b.get( j ));
		}
		c.set( i, temp );
	    }			
	    return c;
	}
	else	
	    throw new RuntimeException("Dimension error in Matrix-Vector mul.");	
    }
    
    /**
     * Returns the negated <B>Matrix</B> of the parameter,
     * i.e. the <B>Matrix</B> where all elements have changed sign.
     **/
    public static Matrix neg(Matrix a) {
	Matrix b = new Matrix ( a.getRowDimension(), a.getColumnDimension() );
	for (int i = 1; i <= a.getRowDimension(); i++) {
	    for(int j = 1; j <= a.getColumnDimension(); j++) {
		b.set( i, j, - a.get( i, j ) );}
	}
	return b;
    }

    /**
     * Returns an n by n unit <B>Matrix</B>.
     **/
    public static Matrix unit( int n, boolean dummy ) {
	Matrix a = new Matrix( n, n, 0 );
	for (int i = 1; i <= n; i++) {
	    a.set( i, i, 1.0 );
	}
	return a;
    }
    
    // Instance Methods
    
    /**
     * Set value of matrix(i,j) to val.
     **/
    public void set( int i, int j, double value ) {
	val[ i - 1 ][ j - 1 ] = value;
    }
    
    
    /**
     * Get value of field matrix(row,col)   
     **/	
    public double get( int row, int col ) {
	return val[ row - 1 ][ col - 1 ];
    }
    
    
    /**
     * Returns the eigenvalues of the <CODE>this</CODE> Matrix.
     * The real parts are returned in {@link Janet.VectorType} real,
     * and the imaginary parts in {@link Janet.VectorType} imag,
     * which are encapsulated in a {@link java.util.Vector}.
     **/
    public java.util.Vector getEigenvalues() {
	es.upm.fi.gtd.first.jama.EigenvalueDecomposition ed = new es.upm.fi.gtd.first.jama.Matrix( getArray() ).eig();
	VectorType real = getVectorType().getVectorType( ed.getRealEigenvalues() );
	VectorType imag = getVectorType().getVectorType( ed.getImagEigenvalues() );
	java.util.Vector<VectorType> vector = new java.util.Vector<VectorType>();
	vector.add( real  );
	vector.add( imag );
	return vector;
    }
        
    /**
     * Returns a {@link java.util.Vector} containing the eigenvector
     * of the Matrix.
     **/
    public java.util.Vector getEigenvectors() {
	es.upm.fi.gtd.first.jama.EigenvalueDecomposition ed = new es.upm.fi.gtd.first.jama.Matrix( getArray() ).eig();
	es.upm.fi.gtd.first.jama.Matrix eigenvectorMatrix = ed.getV();
	int dim = getRowDimension();
	MatrixType A = getMatrixType( dim, dim );
	java.util.Vector<VectorType> eigenvectors = new java.util.Vector<VectorType>();
	VectorType eigenvector;
	for (int i = 0; i < dim; i++) {
	    eigenvector = A.getVectorType().getVectorType( dim );
	    for (int j = 0; j < dim; j++) 
		eigenvector.set( j + 1, eigenvectorMatrix.get( j, i ) );
	    eigenvectors.add( eigenvector );
	}
	return eigenvectors;
    }
        
    /**
     * Checks whether the <B>Matrix</B> this is identical
     * to the <B>Matrix</B> a.
     * @return true if a and this have the same elements in all places
     * else false is returned.
     **/
    public boolean equals( Matrix a ) {
	if ( checkEqualSize( this, a ) ) {
	    int rows = this.getRowDimension();
	    int cols = this.getColumnDimension();
	    for (int i = 1; i <= rows; i++)
		for (int j = 1; j <= cols; j++)
		    if ( this.get( i, j ) != a.get( i, j ) )
			return false; 	
	    return true;
	}
	return false;
    }
    
    
    /**
     * Multiplies each field in a <B>Matrix</B> by the
     * corresponding field in a specified <B>Matrix</B>, b.
     * @throws RuntimeException if dimensions of matrices differ.
     **/
    public void mul( Matrix b ) {
	if ( checkEqualSize( this, b ) ) {
	    int rows = this.getRowDimension();
	    int cols = this.getColumnDimension();
	    for (int i = 1; i <= rows; i++) {
		for  (int j = 1; j <= cols; j++) {
		    this.set( i, j, this.get( i, j ) * b.get( i, j ) );	
		}
	    }			
	}
	else	
	    throw new RuntimeException("Error in elementwise mul of Matrix and Matrix.");	
    }
    
    /**
     * Multiplies each elmement in the <B>Matrix</B> this by a constant c.
     **/
    public Matrix mul( double c ) {
	int rows = this.getRowDimension();
	int cols = this.getColumnDimension();
	for (int i = 1; i <= rows; i++) {
	    for  (int j = 1; j <= cols; j++) {
		this.set( i, j, c * this.get( i, j ) );	
	    }
	}	
	return this;
    }

    /**
     * Transposes this <B>Matrix</B> and returns a new <B>Matrix</B>.
     **/
    public MatrixType transpose() {
	int rows = this.getRowDimension();
	int cols = this.getColumnDimension();
	MatrixType retMat = new Matrix( cols, rows );

	for (int i = 1; i <= rows; i++)
	    for(int j = 1; j <= cols; j++)
		retMat.set( j, i, this.get( i, j ) );

	return retMat;
    } // transpose

    /**
     * Internal work method for swapping two rows.
     * This method will not be available for long.
     **/
    private static Matrix rowswap( Matrix m ) {
	double temp;
	for (int i = 1; i <= 2; i++) {
	    temp = m.get( 1, i );
	    m.set( 1, i, m.get( 2, i ) );
	    m.set( 2, i, temp );
	}
	return m;
    }
    
    /**
     * Solves the <B>Matrix</B> equation: A*X=B,
     * where A, X and B are matrices.
     * Returns new matrix X. 
     * Works only for 2x2-matrices.
     **/
    public static Matrix solve( Matrix m3, Matrix m4 ) {
	
	Matrix m1 = new Matrix(m3);
	Matrix m2 = new Matrix(m4);
	
	if ( m1.getRowDimension() == 2 && 
	     m2.getRowDimension() == 2 && 
	     m1.getColumnDimension() == 2 && 
	     m2.getColumnDimension() == 2 ) {
	    double m12, m22;
	    double m11 = m1.get(1,1);
	    double m21 = m1.get(2,1);
	    if ( m11 != 0 ) {
		if ( m11 != 1 ) {
		    m1.set( 1, 1, 1 );
		    
		    m1.set( 1, 2, m1.get( 1, 2 ) / m11 );
		    m2.set( 1, 1, m2.get( 1, 1 ) / m11 );
		    m2.set( 1, 2, m2.get( 1, 2 ) / m11 );
		    
		}
	    }
	    else if ( m21 != 0 ) {
		m1 = rowswap( m1 );
		m2 = rowswap( m2 );
		m11 = m21;
		if ( m11 != 1 ) {
		    m1.set( 1, 1, 1);
		    m1.set( 1, 2, m1.get( 1, 2 ) / m11 );
		    m2.set( 1, 1, m2.get( 1, 1 ) / m11 );
		    m2.set( 1, 2, m2.get( 1, 2 ) / m11 );
		}
	    }
	    else 
		throw new RuntimeException("Singular Matrix");
	    m21 = m1.get( 2, 1 );
	    if ( m21 != 0 ) {
		double factor = m21;
		//m1.set(1,2,0);
		m1.set( 2, 2, m1.get(2,2) - factor * m1.get( 1, 2 ) );
		m2.set( 2, 1, m2.get(2,1) - factor * m2.get( 1, 1 ) );
		m2.set( 2, 2, m2.get(2,2) - factor * m2.get( 1, 2 ) );
	    }
	    m22 = m1.get( 2, 2 );
	    if ( m22 != 0 ) {
		m1.set( 2, 2, 1 );
		m2.set( 2, 1, m2.get( 2, 1 ) / m22 );
		m2.set( 2, 2, m2.get( 2, 2 ) / m22 );
	    }
	    else 
		throw new RuntimeException("Singular Matrix.");	
	    m12 = m1.get(1,2);
	    if ( m12 != 0 ) {
		m2.set( 1, 1, m2.get( 1, 1 ) - m12 * m2.get( 2, 1 ) );
		m2.set( 1, 2, m2.get( 1, 2 ) - m12 * m2.get( 2, 2 ) );		
	    }
	    return m2;
	}
	else 
	    throw new RuntimeException("\"solve\" works only for 2x2 matrices");
	
    }
    
    /**
     * Solves the Matrix-equation A*inv(A)=I.
     * @return The new <B>Matrix</B> inv(A).
     * The actual computation is carried out by {@link Jama.Matrix}.
     **/
    public static Matrix invert( Matrix A ) {
	return new Matrix ( new es.upm.fi.gtd.first.jama.Matrix( A.getArray() ).inverse().getArray() );  
    }
    
    /**
     * Solves the Matrix-equation A*X=B.
     * returns new Vector, X.
     * The method relies on Jama to solve the linear system.
     **/
    public static Vector solveLinearSystem( Matrix A, Vector B ) {
	es.upm.fi.gtd.first.jama.Matrix M = new es.upm.fi.gtd.first.jama.Matrix( A.getArray() );
	es.upm.fi.gtd.first.jama.Matrix V = new es.upm.fi.gtd.first.jama.Matrix( M.getRowDimension(), 1 );
	for (int i = 0; i < M.getRowDimension(); i++)
	    V.set( i, 0, B.get( i + 1 ) );
	es.upm.fi.gtd.first.jama.Matrix S = M.solve( V );
	Vector v = new Vector( S.getRowDimension() );
	for (int i = 0; i < S.getRowDimension(); i++)
	    v.set( i + 1, S.get( i, 0 ) );
	return v;
    }
   
    /**
     * Converts the <B>Matrix</B> this into a <B>String</B>.
     * @return A <B>String</B> representing a <B>Matrix</B>.
     **/
    @Override
    public String toString() {
	String retur = "[";
	for (int i = 1; i <= this.getRowDimension(); i++) {
	    retur+= "[" ;
	    for (int j = 1; j <= this.getColumnDimension(); j++)
		retur += this.get( i, j ) + " ";
	    retur += "]";
	}
	retur += "]";
	return retur;
    } // toString

    /**
     * Returns an XML version of the Matrix.
     **/
    public String toXML() {
	StringBuffer buffer = new StringBuffer( 1024 );
	int rows = getRowDimension();
	int cols = getColumnDimension();
	buffer.append( "<matrix>" );
	buffer.append( StandardValues.CR );
	buffer.append( "<rows>" );
	buffer.append( rows );
	buffer.append( " </rows>" );
	buffer.append( StandardValues.CR );
	buffer.append( "<columns>" );
	buffer.append( cols );
	buffer.append( " </columns>" );
	buffer.append( StandardValues.CR );
	for (int i = 0; i < rows; i++) {
	    for (int j = 0; j < cols; j++) {
		buffer.append( "<real>" );
		buffer.append( get( i, j ) );
		buffer.append( "</real>" );
	    } // for
	    buffer.append( StandardValues.CR );
	} // for
	buffer.append( "</matrix>" );
	buffer.append( StandardValues.CR );
	return buffer.toString();
    } // toXML
    
    /**
     *
     *
     **/
    public static VectorType solveSVD( MatrixType M, VectorType b ) {
	es.upm.fi.gtd.first.jama.SingularValueDecomposition svd = 
	    new es.upm.fi.gtd.first.jama.SingularValueDecomposition( new es.upm.fi.gtd.first.jama.Matrix( M.getArray() ) );
	es.upm.fi.gtd.first.jama.Matrix U = svd.getU();
	es.upm.fi.gtd.first.jama.Matrix V = svd.getV();
	es.upm.fi.gtd.first.jama.Matrix S = svd.getS();
	MatrixType JU = new Matrix( U.getArray() );
	MatrixType JS = new Matrix( S.getArray() );
	MatrixType JV = new Matrix( V.getArray() );
	JU = JU.transpose();
	for (int i = 1; i <= JS.getRowDimension(); i++) {
	    double z = JS.get( i, i );
	    if ( Math.abs( z ) > 1e-12 ) JS.set( i, i, 1.0 / z );
	}
	MatrixType A = JU.mul( JV, JU.mul( JS, JU ) );
	return A.mul( A, b );
    } // solveSVD
    
} // Matrix
