public class Matrix implements IMatrix {

    private double[][] doubleMatrixNumber;


    public Matrix() {
        doubleMatrixNumber = new double[DEFAULT_MATRIX_SIZE][DEFAULT_MATRIX_SIZE];
    }

    public Matrix(int a, int b) {
        doubleMatrixNumber = new double[a > DEFAULT_MATRIX_SIZE ? a : DEFAULT_MATRIX_SIZE][b > DEFAULT_MATRIX_SIZE ? b : DEFAULT_MATRIX_SIZE];
    }

    public Matrix(double[][] doubleArray) {
        doubleMatrixNumber = doubleArray;
    }

    @Override
    public int getRows() {
        return doubleMatrixNumber.length;
    }

    @Override
    public int getColumns() {
        return doubleMatrixNumber[0].length;
    }

    @Override
    public double getValueAt(int rowIndex, int colIndex) throws IndexOutOfBoundsException {
        if (rowIndex < 0 || rowIndex >= getRows()) {
            throw new IndexOutOfBoundsException("неверный индекс строки");
        }
        if (colIndex < 0 || colIndex >= getColumns()) {
            throw new IndexOutOfBoundsException("неверный индекс столбца");
        }
        return doubleMatrixNumber[rowIndex][colIndex];
    }

    @Override
    public void setValueAt(int rowIndex, int colIndex, double value) throws IndexOutOfBoundsException {
        if (rowIndex < 0 || rowIndex >= getRows()) {
            throw new IndexOutOfBoundsException("неверный индекс строки");
        }
        if (colIndex < 0 || colIndex >= getColumns()) {
            throw new IndexOutOfBoundsException("неверный индекс столбца");
        }
        doubleMatrixNumber[rowIndex][colIndex] = value;
    }

    @Override
    public IMatrix add(IMatrix otherMatrix) throws IllegalArgumentException, NullPointerException {
        if (this.getRows() != otherMatrix.getRows()) {
            throw new IllegalArgumentException("Не совпадают размеры матриц по строкам");
        }
        if (this.getColumns() != otherMatrix.getColumns()) {
            throw new IllegalArgumentException("Не совпадают размеры матриц по столбцам");
        }
        Matrix resultAdd = new Matrix(this.getRows(), this.getColumns());
        double[][] sumArrays = new double[this.getRows()][this.getColumns()];
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                resultAdd.setValueAt(i, j, this.getValueAt(i, j) + otherMatrix.getValueAt(i, j));
            }
        }
        return resultAdd;
    }

    @Override
    public IMatrix sub(IMatrix otherMatrix) throws IllegalArgumentException, NullPointerException {
        if (otherMatrix.isNullMatrix()) {
            throw new NullPointerException("Вторая матрица нулевая");
        }
        if (this.getRows() != otherMatrix.getRows() && this.getColumns() != otherMatrix.getColumns()) {
            throw new IllegalArgumentException("Не совпадают размеры матриц");
        }
        Matrix resultSub = new Matrix(this.getRows(), this.getColumns());
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                resultSub.setValueAt(i, j, this.getValueAt(i, j) - otherMatrix.getValueAt(i, j));
            }
        }
        return resultSub;
    }

    @Override
    public IMatrix mul(IMatrix otherMatrix) throws IllegalArgumentException, NullPointerException {
        if (otherMatrix.isNullMatrix()) {
            throw new NullPointerException("Вторая матрица нулевая");
        }
        if (this.getColumns() != otherMatrix.getRows()) {
            throw new IllegalArgumentException("Не совпадают размеры матриц");
        }
        Matrix resultMul = new Matrix(this.getRows(), otherMatrix.getColumns());
        for (int i = 0; i < this.getRows(); i++) {
            for (int k = 0; k < otherMatrix.getColumns(); k++) {
                double value = 0;
                for (int j = 0; j < this.getColumns(); j++) {
                    value += this.getValueAt(i, j) * otherMatrix.getValueAt(j, k);
                }
                resultMul.setValueAt(i, k, value);
            }
        }
        return resultMul;
    }

    @Override
    public IMatrix mul(double value) {
        Matrix resultMul = new Matrix(getRows(), getColumns());
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                resultMul.setValueAt(i, j, getValueAt(i, j) * value);
            }
        }
        return resultMul;
    }

    @Override
    public IMatrix transpose() {
        Matrix resultTranspose = new Matrix(getColumns(), getRows());
        for (int i = 0; i < getColumns(); i++) {
            for (int j = 0; j < getRows(); j++) {
                resultTranspose.setValueAt(i, j, getValueAt(j, i));
            }
        }
        return resultTranspose;
    }

    @Override
    public void fillMatrix(double value) {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                setValueAt(i, j, value);
            }
        }
    }

    @Override
    public double determinant() {
        if (isSquareMatrix()) {
            double determinant = 0;
            if (getColumns() > 2) {
                for (int i = 0; i < getColumns(); i++) {
                    double val = getValueAt(0, i);
                    Matrix minor = minorMatrix(i, this);
                    double det = minor.determinant();
                    if (i % 2 == 0) {
                        determinant += val * det;
                    } else {
                        determinant -= val * det;
                    }
                }
            } else {
                determinant = getValueAt(0, 0) * getValueAt(1, 1) -
                        getValueAt(0, 1) * getValueAt(1, 0);
            }
            return determinant;
        }
        return 0.0 / 0.0;
    }

    private Matrix minorMatrix(int colIndex, Matrix matrix) {
        int size = matrix.getRows();
        Matrix resultMinorMatrix = new Matrix(size - 1, size - 1);
        for (int i = 1; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (j < colIndex) {
                    resultMinorMatrix.setValueAt(i - 1, j, matrix.getValueAt(i, j));
                } else if (j != colIndex) {
                    resultMinorMatrix.setValueAt(i - 1, j - 1, matrix.getValueAt(i, j));
                }
            }
        }
        return resultMinorMatrix;
    }

    @Override
    public boolean isNullMatrix() {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                if (getValueAt(i, j) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isIdentityMatrix() {
        if (this.isSquareMatrix()) {
            for (int i = 0; i < getRows(); i++) {
                for (int j = 0; j < getColumns(); j++) {
                    if (i == j && getValueAt(i, j) != 1) {
                        return false;
                    }
                    if (i != j && getValueAt(i, j) != 0) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isSquareMatrix() {
        if (getRows() == getColumns()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void printToConsole() {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                System.out.print(getValueAt(i, j) + " ");
            }
            System.out.println();
        }
    }
}
