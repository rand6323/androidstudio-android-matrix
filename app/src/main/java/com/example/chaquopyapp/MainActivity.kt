package com.example.chaquopyapp

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import android.view.inputmethod.InputMethodManager

class MainActivity : AppCompatActivity() {
    private var rowCount = 2
    private var colCount = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val py = Python.getInstance()
        val module = py.getModule("calc_matrix")

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val rowMinusButton = findViewById<Button>(R.id.rowMinusButton)
        val rowPlusButton = findViewById<Button>(R.id.rowPlusButton)
        val colMinusButton = findViewById<Button>(R.id.colMinusButton)
        val colPlusButton = findViewById<Button>(R.id.colPlusButton)
        val rowCountText = findViewById<TextView>(R.id.rowCountText)
        val colCountText = findViewById<TextView>(R.id.colCountText)
        val scalarInput = findViewById<EditText>(R.id.scalarInput)
        val scalarButton = findViewById<Button>(R.id.scalarButton)
        val transposeButton = findViewById<Button>(R.id.transposeButton)
        val inverseButton = findViewById<Button>(R.id.inverseButton)
        val detButton = findViewById<Button>(R.id.detButton)
        val resultView = findViewById<TextView>(R.id.resultView)

        // グリッドの初期表示
        updateGrid(gridLayout, rowCount, colCount)

        val MAX = 5
        val MIN = 1

        // 初期表示
        rowCountText.text = rowCount.toString()
        colCountText.text = colCount.toString()

        // 行「−」
        rowMinusButton.setOnClickListener {

            if (rowCount > MIN) {
                rowCount--
                rowCountText.text = rowCount.toString()
                updateGrid(gridLayout, rowCount, colCount)
            }

        }

        // 行「＋」
        rowPlusButton.setOnClickListener {
            if (rowCount < MAX) {
                rowCount++
                rowCountText.text = rowCount.toString()
                updateGrid(gridLayout, rowCount, colCount)
            }
        }

        // 列「−」
        colMinusButton.setOnClickListener {
            if (colCount > MIN) {
                colCount--
                colCountText.text = colCount.toString()
                updateGrid(gridLayout, rowCount, colCount)
            }
        }

        // 列「＋」
        colPlusButton.setOnClickListener {
            if (colCount < MAX) {
                colCount++
                colCountText.text = colCount.toString()
                updateGrid(gridLayout, rowCount, colCount)
            }
        }

        scalarButton.setOnClickListener {

            hideKeyboard()

            val all = collectAllInputs(gridLayout)
            var cValue = scalarInput.text.toString().toDoubleOrNull()

            val cAny: Any = cValue ?: 1.0  // nullならデフォルト値1.0を使用
            val resScalar = callMatrixFunction(module, "scalar_mult", all, rowCount, colCount, cAny)
            resultView.text = resScalar.toString()

        }

        transposeButton.setOnClickListener {

            hideKeyboard()

            val all = collectAllInputs(gridLayout)

            val resTrans = callMatrixFunction(module, "transpose", all, rowCount, colCount)
            resultView.text = resTrans.toString()

        }

        inverseButton.setOnClickListener {

            hideKeyboard()

            val all = collectAllInputs(gridLayout)

            val resInv = callMatrixFunction(module, "inverse", all, rowCount, colCount)
            resultView.text = resInv.toString()

        }

        detButton.setOnClickListener {

            hideKeyboard()

            val all = collectAllInputs(gridLayout)

            val resDet = callMatrixFunction(module, "determinant", all, rowCount, colCount)
            resultView.text = resDet.toString()

        }

    }

    // グリッドの更新
    private fun updateGrid(gridLayout: GridLayout, rows: Int, cols: Int) {
        gridLayout.removeAllViews()
        gridLayout.rowCount = rows
        gridLayout.columnCount = cols
        val sizePx = (60 * resources.displayMetrics.density).toInt()

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val et = EditText(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        rowSpec = GridLayout.spec(i)
                        columnSpec = GridLayout.spec(j)
                        width = sizePx; height = sizePx
                    }
                    hint = "0"
                    inputType = InputType.TYPE_CLASS_NUMBER
                    gravity = Gravity.CENTER
                    background = ContextCompat.getDrawable(context, R.drawable.cell_border)
                }
                gridLayout.addView(et)
            }
        }
    }


    fun collectAllInputs(gridLayout: GridLayout): List<String> {
        val inputs = mutableListOf<String>()
        for (i in 0 until gridLayout.childCount) {
            val child = gridLayout.getChildAt(i)
            if (child is EditText) {
                val text = child.text.toString()
                inputs.add(if (text.isEmpty()) "0" else text)
            }
        }
        return inputs
    }

    private fun hideKeyboard() {
        currentFocus?.let { view ->
            val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * List<String> → Python list への変換と関数呼び出しをまとめたヘルパー
     */
    fun callMatrixFunction(
        module: PyObject,
        funcName: String,
        values: List<String>,
        rows: Int,
        cols: Int,
        vararg extraArgs: Any
    ): PyObject {
        val py = Python.getInstance()
        val builtins = py.getBuiltins()
        val arrayObj = PyObject.fromJava(values.toTypedArray())
        val pyList = builtins.callAttr("list", arrayObj)
        return module.callAttr(funcName, pyList, rows, cols, *extraArgs)
    }

}
