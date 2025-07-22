import numpy as np

def input_matrix(values, rows, cols):
    data = [float(x) for x in values]
    return np.array(data).reshape(rows, cols)

def scalar_mult(values, rows, cols, c):
    mat = np.array([float(x) for x in values]).reshape(rows, cols)
    print("scalar_mult input:\n", mat, "c=", c)
    return (c * mat)

def transpose(values, rows, cols):
    mat = np.array([float(x) for x in values]).reshape(rows, cols)
    print("transpose input:\n", mat)
    return mat.T  # 𝙰ᵀ を返す

def inverse(values, rows, cols):
    mat = np.array([float(x) for x in values]).reshape(rows, cols)
    try:
        inv = np.linalg.inv(mat)
    except np.linalg.LinAlgError as e:
        # 正則でない場合にエラーメッセージを返す
        return f"逆行列は存在しません（特異行列）"
    return inv

def determinant(values, rows, cols):
    mat = np.array([float(x) for x in values]).reshape(rows, cols)
    det = np.linalg.det(mat)
    print("determinant input det:", det)
    return det  # スカラーを返す :contentReference[oaicite:2]{index=2}